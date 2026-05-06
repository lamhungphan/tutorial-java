# 12 — Reflection & Annotations

## 1. Định nghĩa & vai trò

**Reflection** (`java.lang.reflect`) là khả năng chương trình **tự kiểm tra & sửa đổi** chính nó tại runtime: đọc class metadata, gọi method, đọc/ghi field, tạo instance — mà không cần biết tên class lúc compile.

**Annotations** (`java.lang.annotation`) là metadata gắn lên class/method/field — có thể đọc bằng reflection (RUNTIME) hoặc xử lý compile-time (annotation processor).

Đây là nền tảng của:

- **Frameworks**: Spring (`@Component`, `@Autowired`), Hibernate (`@Entity`), JUnit (`@Test`), Jackson (`@JsonProperty`).
- **Tools**: Lombok, MapStruct, Dagger, Micronaut.
- **Dynamic proxy**: AOP, RPC client, mock.
- **Serialization**: Jackson, Gson đọc `Field` qua reflection.
- **Dependency Injection container** scan classpath qua reflection / index.

Trade-off: reflection chậm hơn direct call (~100x ban đầu, ~5-10x sau JIT), bypass type check, và bị **module system** giới hạn từ Java 9+.

---

## 2. Reflection API

### 2.1. Lấy `Class<?>`

```java
Class<?> c1 = String.class;                  // class literal
Class<?> c2 = "abc".getClass();              // từ instance
Class<?> c3 = Class.forName("java.util.HashMap"); // từ tên (init class)
Class<?> c4 = Class.forName("java.util.HashMap", false, loader); // không init
```

### 2.2. Khám phá class

```java
Class<?> cls = Map.class;
cls.getName();                  // "java.util.Map"
cls.getSimpleName();            // "Map"
cls.getModifiers();             // bitmask: PUBLIC, ABSTRACT, INTERFACE
cls.isInterface();              // true
cls.getPackage();               // package
cls.getInterfaces();            // implemented interfaces
cls.getSuperclass();            // superclass (null cho Object/interface/primitive)
cls.getDeclaredFields();        // fields khai báo trực tiếp (tất cả modifier)
cls.getFields();                // chỉ public, INCLUDING inherited
cls.getDeclaredMethods();       // method khai báo
cls.getMethods();               // public + inherited
cls.getDeclaredConstructors();
cls.getTypeParameters();        // generics info
cls.getAnnotations();
```

### 2.3. `Constructor`, `Method`, `Field`

```java
Class<?> cls = ArrayList.class;
Constructor<?> c = cls.getDeclaredConstructor(int.class);
Object list = c.newInstance(10);

Method add = cls.getMethod("add", Object.class);
add.invoke(list, "hello");

Field size = cls.getDeclaredField("size");
size.setAccessible(true);     // bypass private
int s = size.getInt(list);
```

### 2.4. Generic info

Erasure xoá generic ở runtime (xem [`11_generics_type_erasure.md`](11_generics_type_erasure.md)) **trừ** lưu trong `Signature` attribute:

```java
Method m = MyClass.class.getMethod("getList");
Type retType = m.getGenericReturnType();
if (retType instanceof ParameterizedType pt) {
    System.out.println(pt.getRawType());          // List
    System.out.println(pt.getActualTypeArguments()[0]);  // String
}
```

### 2.5. Modifier bitmask

```java
int mods = m.getModifiers();
Modifier.isPublic(mods);
Modifier.isStatic(mods);
Modifier.isAbstract(mods);
```

---

## 3. `MethodHandle` & `VarHandle` — alternative API

`java.lang.invoke` (J7+, J9+) là **lớp thấp hơn** reflection, gần JIT hơn → nhanh hơn 5-10x.

### 3.1. `MethodHandle`

```java
MethodHandles.Lookup lookup = MethodHandles.lookup();
MethodHandle mh = lookup.findVirtual(String.class, "length", MethodType.methodType(int.class));
int len = (int) mh.invoke("hello");   // 5
```

So với `Method.invoke`:

- Không box primitive thành `Object`.
- JIT inline được (phần nào).
- Dùng nội bộ cho `LambdaMetafactory` (lambda Java 8).

### 3.2. `VarHandle` (J9+)

Thay `sun.misc.Unsafe` cho atomic field access:

```java
class Counter {
    private volatile int value;
    private static final VarHandle VH;
    static {
        try {
            VH = MethodHandles.lookup().findVarHandle(Counter.class, "value", int.class);
        } catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    public void increment() {
        VH.getAndAdd(this, 1);          // atomic
    }
}
```

`VarHandle` cung cấp: `get/set` (plain), `getVolatile/setVolatile`, `getOpaque`, `getAcquire/setRelease`, `compareAndSet`, `getAndAdd`, `getAndSet`, `weakCompareAndSet`, ...

→ Replace cho `Unsafe.compareAndSwapInt` mà không phải dùng internal API.

---

## 4. Dynamic Proxy

`java.lang.reflect.Proxy.newProxyInstance` tạo class implement interface tại runtime, mọi call đi qua `InvocationHandler`:

```java
interface Greeter { void hello(String name); }

InvocationHandler handler = (proxy, method, args) -> {
    System.out.println("BEFORE " + method.getName());
    return null;
};

Greeter g = (Greeter) Proxy.newProxyInstance(
    Greeter.class.getClassLoader(),
    new Class<?>[]{ Greeter.class },
    handler
);
g.hello("world");   // in BEFORE hello
```

Use case: AOP, Mockito, RMI, JDBC abstraction, REST client (Feign, Retrofit).

Hạn chế: chỉ proxy được **interface**. Để proxy class concrete → dùng `CGLib` / `ByteBuddy` (sinh subclass).

---

## 5. Annotations

### 5.1. Định nghĩa

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Audited {
    String value() default "";
    int level() default 1;
    Class<? extends Validator> validator() default Validator.None.class;
}
```

### 5.2. `@Retention` policy

| Policy | Lưu ở | Use case |
|--------|------|----------|
| `SOURCE` | chỉ source — bị bỏ ở `.class` | `@Override`, `@SuppressWarnings`, Lombok, IDE hint |
| `CLASS` (default) | trong `.class` nhưng không load runtime | bytecode transformer (Findbugs, Lombok plugin). |
| `RUNTIME` | có ở runtime, đọc bằng reflection | Spring, JUnit, Jackson |

### 5.3. `@Target`

`TYPE`, `METHOD`, `FIELD`, `PARAMETER`, `CONSTRUCTOR`, `LOCAL_VARIABLE`, `ANNOTATION_TYPE`, `PACKAGE`, `TYPE_PARAMETER` (J8), `TYPE_USE` (J8 — `@NonNull String`).

### 5.4. `@Repeatable` (J8)

```java
@Repeatable(Schedules.class)
public @interface Schedule { String cron(); }
public @interface Schedules { Schedule[] value(); }

@Schedule(cron = "0 8 * * *")
@Schedule(cron = "0 18 * * *")
void runJob() {}
```

### 5.5. Đọc annotation runtime

```java
Audited a = MyClass.class.getAnnotation(Audited.class);
if (a != null) System.out.println(a.value());

for (Method m : MyClass.class.getMethods()) {
    Schedule[] schedules = m.getAnnotationsByType(Schedule.class);
    ...
}
```

---

## 6. Annotation Processing (`APT`) — compile time

`javax.annotation.processing.Processor` cho phép xử lý annotation **lúc `javac` chạy**, sinh ra source/class mới — *không* cần reflection runtime.

```java
@SupportedAnnotationTypes("com.acme.Audited")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class AuditProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> ann, RoundEnvironment env) {
        for (Element e : env.getElementsAnnotatedWith(Audited.class)) {
            // sinh class mới qua Filer / JavaPoet
        }
        return true;
    }
}
```

Khai báo `META-INF/services/javax.annotation.processing.Processor`.

Frameworks dùng APT: **Lombok** (HACK — modify AST), **MapStruct**, **Dagger 2**, **AutoValue**, **Hibernate JPA Static Metamodel**, **Micronaut**, **Quarkus**, **Spring AOT** (J17+).

→ APT **nhanh hơn** runtime reflection vì:

- Code generated tại compile time → JIT quen mặt.
- Không kiểm tra annotation lúc start (như Spring classpath scan).
- Hỗ trợ GraalVM `native-image` (không reflection).

---

## 7. Demo

### 7.1. Reflection invoke

```java
public class Hello {
    public static void main(String[] args) throws Exception {
        Class<?> c = Class.forName("java.util.ArrayList");
        Object list = c.getDeclaredConstructor().newInstance();
        Method add = c.getMethod("add", Object.class);
        add.invoke(list, "hi");
        add.invoke(list, "world");
        System.out.println(list);     // [hi, world]
    }
}
```

### 7.2. Custom annotation framework đơn giản

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Test {}

class MyTests {
    @Test void t1() { System.out.println("t1 ok"); }
    @Test void t2() { throw new RuntimeException("oops"); }
    void notATest() {}
}

class MiniRunner {
    public static void main(String[] args) throws Exception {
        Object instance = MyTests.class.getDeclaredConstructor().newInstance();
        for (Method m : MyTests.class.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                try { m.invoke(instance); System.out.println(m.getName() + " PASS"); }
                catch (Throwable t) { System.out.println(m.getName() + " FAIL " + t.getCause()); }
            }
        }
    }
}
```

### 7.3. Dynamic proxy logging

```java
interface UserRepo { User find(int id); }
class UserRepoImpl implements UserRepo { public User find(int id) { return new User(id); } }

UserRepo proxy = (UserRepo) Proxy.newProxyInstance(
    UserRepo.class.getClassLoader(), new Class<?>[]{UserRepo.class},
    (p, m, args) -> {
        long t0 = System.nanoTime();
        try { return m.invoke(new UserRepoImpl(), args); }
        finally { System.out.printf("%s took %d us%n", m.getName(), (System.nanoTime()-t0)/1000); }
    });
proxy.find(1);
```

---

## 8. Module system & Strong encapsulation (J9+)

Java 9 đưa `JPMS` (`module-info.java`). Reflection mặc định **không thể** truy cập internal API:

```java
// Java 17+: ném InaccessibleObjectException
Field f = String.class.getDeclaredField("value");
f.setAccessible(true);   // báo lỗi!
```

Lý do: `java.base` không `opens java.lang to ALL-UNNAMED`.

Workaround:

- **Tạm**: cờ JVM `--add-opens java.base/java.lang=ALL-UNNAMED`.
- **Đúng**: declare `requires` + `opens` trong `module-info.java`.

> Library cũ dùng `Unsafe`/`Reflection internal` (Hibernate, Lombok, Spring) đã chuyển sang `MethodHandle.privateLookupIn` hoặc `VarHandle` để compatible.

---

## 9. Pitfall & best practice (senior view)

- **Reflection chậm** ngay lần đầu (~µs); cache `Method`/`Field` rồi gọi nhiều lần ổn (~10-100 ns sau JIT). Đừng `getDeclaredMethods()` mỗi call.
- **`setAccessible(true)`** đắt — gọi 1 lần, lưu cached `Method` rồi dùng tiếp.
- **Module reflective access**: trên Java 17+ phải `--add-opens` hoặc dùng `MethodHandles.privateLookupIn(target, lookup)`.
- **Avoid reflection nếu có alternative**:
  - Generic factory → DI container (Spring inject) thay vì `Class.forName`.
  - Bytecode generation compile-time (APT, MapStruct) thay vì runtime mapping.
  - `MethodHandle`/`VarHandle` thay `Method.invoke`/`Field.set`.
- **Annotation runtime đọc** chỉ một lần khi start (Spring làm vậy) — đừng đọc trong hot path.
- **Don't expose `setAccessible`-based** API — vi phạm encapsulation. Reflection nên đóng trong tool/framework, không leak ra business code.
- **Annotation `value()`** mặc định cho 1 attribute — `@Foo("bar")` thay vì `@Foo(value = "bar")`.
- **APT vs runtime reflection**: framework hiện đại (Spring AOT, Quarkus, Micronaut) ưu tiên APT để hỗ trợ GraalVM `native-image` — startup `<100ms`, RAM thấp.
- **Repeatable annotation** chỉ đọc đúng qua `getAnnotationsByType`, không phải `getAnnotation` (sẽ trả container).
- **Đọc generic của field/method** — luôn dùng `getGenericType()` / `getGenericReturnType()`, không `getType()`.

---

## 10. Câu hỏi phỏng vấn điển hình

1. Reflection làm được gì? Khi nào nên / không nên dùng?
2. `MethodHandle` khác `Method.invoke` ở đâu?
3. Dynamic Proxy hoạt động ra sao? Hạn chế?
4. CGLib khác Dynamic Proxy ở điểm nào?
5. `@Retention` có 3 mức — kể & cho ví dụ.
6. Annotation processor (APT) chạy ở giai đoạn nào của `javac`?
7. Vì sao Spring scan classpath chậm? Spring AOT giải quyết bằng cách nào?
8. Erasure ảnh hưởng reflection thế nào? Cách lấy `T` ở runtime?
9. `setAccessible(true)` báo lỗi trên Java 17+ — vì sao? Cách fix?
10. `VarHandle` thay `Unsafe` ra sao?
11. `@Inherited` annotation hoạt động trên class nào? (Class — không phải interface, không phải method.)
12. Spring `@Transactional` nguyên lý hoạt động? (Proxy / AspectJ)

---

## 11. Tham chiếu

- [Java Reflection API — Java 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/reflect/package-summary.html)
- [JLS §9.6 Annotation Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html#jls-9.6)
- [JEP 274: Enhanced Method Handles](https://openjdk.org/jeps/274)
- [JEP 193: Variable Handles](https://openjdk.org/jeps/193)
- [JEP 261: Module System (encapsulation)](https://openjdk.org/jeps/261)
- [JEP 396: Strongly Encapsulate JDK Internals by Default](https://openjdk.org/jeps/396)
- [Lombok internals](https://projectlombok.org/)
- [JavaPoet — code generation](https://github.com/square/javapoet)
