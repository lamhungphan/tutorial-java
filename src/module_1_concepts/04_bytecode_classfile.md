# 04 — Bytecode & `.class` File Format

## 1. Định nghĩa & vai trò

`.class` file là **đặc tả nhị phân chuẩn** của JVM (JVMS Chapter 4). Mọi ngôn ngữ JVM (Java, Kotlin, Scala, Groovy, Clojure) đều phải sinh file theo format này.

JVM bytecode là tập **lệnh stack-based**, mỗi opcode 1 byte (tối đa 256 lệnh) + operand đi kèm. Hiểu bytecode giúp:

- Debug `NoSuchMethodError` / `ClassFormatError`.
- Tối ưu performance (hiểu `invokedynamic`, autoboxing, lambda metafactory).
- Viết tooling: APM agent, bytecode rewriter (`ASM`, `ByteBuddy`, `Javassist`), Lombok, AspectJ.
- Đọc decompiled output, hiểu `javap`.

---

## 2. Cấu trúc `.class` file

```
ClassFile {
    u4             magic;             // 0xCAFEBABE
    u2             minor_version;
    u2             major_version;     // 65 = Java 21
    u2             constant_pool_count;
    cp_info        constant_pool[constant_pool_count - 1];
    u2             access_flags;      // public, final, super, interface, abstract, ...
    u2             this_class;        // index in constant_pool
    u2             super_class;
    u2             interfaces_count;
    u2             interfaces[];
    u2             fields_count;
    field_info     fields[];
    u2             methods_count;
    method_info    methods[];
    u2             attributes_count;
    attribute_info attributes[];      // SourceFile, InnerClasses, BootstrapMethods, ...
}
```

### 2.1. Major version map

| Java | major_version |
|------|----|
| Java 8 | 52 |
| Java 11 | 55 |
| Java 17 | 61 |
| Java 21 | 65 |
| Java 22 | 66 |

### 2.2. Constant pool

Bảng đầu vào trung tâm. Mọi reference (string literal, class name, method signature) đều index vào đây.

Tag chính:

| Tag | Loại |
|-----|------|
| 1  | `Utf8` (string raw) |
| 3  | `Integer` |
| 4  | `Float` |
| 5  | `Long` |
| 6  | `Double` |
| 7  | `Class` |
| 8  | `String` |
| 9  | `Fieldref` |
| 10 | `Methodref` |
| 11 | `InterfaceMethodref` |
| 12 | `NameAndType` |
| 15 | `MethodHandle` |
| 16 | `MethodType` |
| 17 | `Dynamic` |
| 18 | `InvokeDynamic` |
| 19 | `Module` |
| 20 | `Package` |

---

## 3. Bytecode mnemonics quan trọng

### 3.1. Stack & local variable

| Opcode | Ý nghĩa |
|--------|---------|
| `iconst_0..5`, `bipush`, `sipush`, `ldc` | push hằng số |
| `iload`, `aload`, `lload`, `dload` | load biến local lên stack |
| `istore`, `astore` | pop stack vào biến local |
| `dup`, `dup2`, `pop`, `swap` | thao tác trên stack |
| `iadd`, `isub`, `imul`, `idiv`, `irem` | toán tử int |
| `i2l`, `l2i`, `i2d`, `f2i` | type conversion |

### 3.2. Method invocation

| Opcode | Khi nào dùng |
|--------|---------------|
| `invokestatic` | gọi `static` method |
| `invokevirtual` | gọi method instance không phải private/init |
| `invokespecial` | gọi `<init>` (constructor), `private`, `super.method()` |
| `invokeinterface` | gọi method qua interface reference |
| `invokedynamic` | bootstrap-based call site (lambda, string concat từ J9, switch pattern, record `equals/hashCode`...) |

### 3.3. Object & array

| Opcode | Ý nghĩa |
|--------|---------|
| `new` | cấp phát object trên heap (chưa init) |
| `getfield`, `putfield` | đọc/ghi field instance |
| `getstatic`, `putstatic` | đọc/ghi field static |
| `newarray`, `anewarray`, `multianewarray` | cấp phát array |
| `arraylength`, `aaload`, `aastore` | thao tác array |

### 3.4. Control flow

| Opcode | Ý nghĩa |
|--------|---------|
| `if_icmpeq`, `if_icmpne`, `if_icmplt`, `if_icmpge` | so sánh int |
| `ifnull`, `ifnonnull` | so sánh null |
| `goto`, `tableswitch`, `lookupswitch` | nhảy |
| `athrow` | ném exception |
| `ireturn`, `areturn`, `return` | trả về |

### 3.5. Đặc biệt — `invokedynamic`

Lambda Java 8 không tạo class anonymous nữa, mà dùng `invokedynamic` + `LambdaMetafactory`:

- Lần đầu gọi: bootstrap method được kích hoạt, sinh class lambda runtime, **link** call site.
- Lần sau: gọi trực tiếp.

Tương tự, `String s = "a" + b + "c"` từ Java 9 dùng `invokedynamic` với `StringConcatFactory` (thay `StringBuilder` cũ) — nhanh hơn và linh hoạt hơn.

---

## 4. Demo với `javap`

### 4.1. Chuẩn bị

```java
// File: Demo.java
public class Demo {
    private int counter = 0;
    public int add(int a, int b) { return a + b; }
    public void increment() { counter++; }
    public Runnable lambda() { return () -> System.out.println(counter); }
}
```

### 4.2. Compile & xem bytecode

```bash
$ javac Demo.java
$ javap -c -p -v Demo.class
```

Kết quả tóm tắt cho `add`:

```
public int add(int, int);
  descriptor: (II)I
  flags: ACC_PUBLIC
  Code:
    stack=2, locals=3, args_size=3
       0: iload_1     // load a
       1: iload_2     // load b
       2: iadd
       3: ireturn
```

`increment()`:

```
public void increment();
  Code:
       0: aload_0           // this
       1: dup
       2: getfield #7       // Field counter:I
       5: iconst_1
       6: iadd
       7: putfield #7       // counter = counter + 1
      10: return
```

> Chú ý 2 chỉ thị `getfield`/`putfield` không atomic → đây là gốc rễ race condition khi nhiều thread tăng `counter` đồng thời. Xem [`09_jmm.md`](09_jmm.md).

`lambda()` (cốt lõi):

```
public java.lang.Runnable lambda();
  Code:
     0: aload_0
     1: invokedynamic #14, 0  // InvokeDynamic #0:run:(LDemo;)Ljava/lang/Runnable;
     6: areturn

BootstrapMethods:
  0: #44 REF_invokeStatic java/lang/invoke/LambdaMetafactory.metafactory
     ...
```

### 4.3. Cờ `javap` thường dùng

| Cờ | Tác dụng |
|----|----------|
| `-c` | disassemble bytecode |
| `-p` | hiển thị cả `private` |
| `-v` | verbose: constant pool, attributes |
| `-l` | line numbers + local variable table |
| `-s` | hiển thị internal type signatures |
| `-constants` | static final hằng số |

### 4.4. Method descriptor decoding

```
add(II)I            // (int, int) -> int
toString()Ljava/lang/String;   // () -> String
sort([I)V           // (int[]) -> void
get(Ljava/util/Map;Ljava/lang/Object;)Ljava/lang/Object;   // (Map, Object) -> Object
```

| Ký tự | Loại |
|------|------|
| `B` | byte |
| `C` | char |
| `D` | double |
| `F` | float |
| `I` | int |
| `J` | long |
| `S` | short |
| `Z` | boolean |
| `V` | void |
| `Lname;` | reference (name dùng `/` thay `.`) |
| `[X` | array of X |

---

## 5. Class-File API (Java 24, JEP 457)

Trước Java 24, đọc/ghi `.class` đều dựa vào `ASM` (lib bên thứ ba). JEP 457 đưa **Class-File API** chính thức vào `java.lang.classfile`:

```java
import java.lang.classfile.*;

ClassModel cm = ClassFile.of().parse(Path.of("Demo.class"));
cm.methods().forEach(m -> System.out.println(m.methodName()));
```

API thay thế `ASM` cho mọi tooling (Spring AOT, Quarkus, Gradle). Stable từ Java 24.

---

## 6. Pitfall & best practice (senior view)

- **Bytecode khác source code 1-1**: Lambda thành `invokedynamic`, foreach thành iterator, generics bị erase, autoboxing chèn `Integer.valueOf`. Đọc bytecode để hiểu cost thực sự.
- **String concat trong vòng lặp**: từ Java 9 nó dùng `invokedynamic` thay `StringBuilder` — không còn quá tệ, nhưng vẫn khuyến nghị dùng `StringBuilder` rõ ràng.
- **`<clinit>` (static initializer)** chạy lần đầu class được initialize. Lỗi trong `<clinit>` → `ExceptionInInitializerError` (rất khó debug — production gặp thường vì config sai).
- **`<init>`** là constructor; được `invokespecial` gọi.
- **`StackMapTable` attribute** (J7+): bắt buộc cho verifier — đừng bỏ qua khi gen bytecode tay.
- **Bytecode rewriter** ở runtime (Java agent, instrumentation) cần cẩn thận với class đã được redefine — có giới hạn (không thay schema).
- **Reflection vs `MethodHandle`**: `MethodHandle` (J7+) gần JIT hơn, nhanh hơn `java.lang.reflect`.

---

## 7. Câu hỏi phỏng vấn điển hình

1. JVM là stack-based hay register-based? Khác `Dalvik` (Android, register-based) thế nào?
2. Lambda Java 8 sinh ra anonymous class không? (Không — dùng `invokedynamic` + `LambdaMetafactory`.)
3. `invokevirtual` khác `invokespecial` thế nào? Khi nào dùng `invokeinterface`?
4. Constant pool dùng để làm gì?
5. Method descriptor `(Ljava/util/List;I)Ljava/lang/Object;` đọc thế nào?
6. Tại sao class compile bằng JDK 21 không chạy trên JRE 17? (`major_version` 65 vs 61 → `UnsupportedClassVersionError`)
7. Generics có lưu trong bytecode không? (Có 1 phần qua `Signature` attribute — nhưng instruction đã erase.)

---

## 8. Tham chiếu

- [JVMS Chapter 4: The class File Format](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html)
- [JVMS Chapter 6: The Java Virtual Machine Instruction Set](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html)
- [JEP 457: Class-File API (Preview)](https://openjdk.org/jeps/457)
- [`javap` documentation](https://docs.oracle.com/en/java/javase/21/docs/specs/man/javap.html)
- [ASM library](https://asm.ow2.io/), [ByteBuddy](https://bytebuddy.net/)
- [Tomasz Mikulski — Lambda invokedynamic deep dive](https://blogs.oracle.com/javamagazine/post/behind-the-scenes-how-do-lambda-expressions-really-work-in-java)
