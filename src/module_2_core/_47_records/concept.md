# Records (Java 16, JEP 395)

## Lý thuyết

Record là dạng class đặc biệt **transparent carrier** cho data immutable. Compiler auto-generate:

- `private final` field cho mỗi component.
- `public` accessor (cùng tên component, **không** prefix `get`).
- Canonical constructor.
- `equals(Object)`, `hashCode()`, `toString()` dựa trên **tất cả** component.
- Implicitly `final`, implicitly extends `java.lang.Record`.

```java
public record Point(int x, int y) {}

var p = new Point(3, 4);
p.x();          // 3 — KHÔNG phải getX()
p.toString();   // Point[x=3, y=4]
p.equals(new Point(3, 4));  // true
```

## Compact constructor

Không cần khai báo lại param — chỉ validate/normalize, field tự assigned sau body.

```java
public record Email(String value) {
    public Email {
        Objects.requireNonNull(value);
        if (!value.contains("@")) throw new IllegalArgumentException();
        value = value.toLowerCase();   // gán lại param trước khi field auto-assign
    }
}
```

## Record patterns (Java 21, JEP 440)

Deconstruct record trong `instanceof` & `switch`:

```java
if (obj instanceof Point(int x, int y)) { ... }

double area = switch (shape) {
    case Circle(Point p, double r) -> Math.PI * r * r;
    case Square(Point p, double s) -> s * s;
};
```

Phối hợp `sealed interface` → `switch` **exhaustive** (không cần `default`).

## Giới hạn

| | |
|-|-|
| Extends class | Không (`extends Record` ngầm). Chỉ `implements` interface. |
| Final | Có (implicitly). Không thể subclass. |
| Instance field | Không cho phép thêm ngoài component. Static thì OK. |
| Setter | Không. Mutation? Tạo instance mới với `with`-style (tự viết). |
| Mutable component | Field `final` nhưng kiểu reference (như `List`) vẫn có thể mutate qua method bên trong → cần **defensive copy**. |

## Pitfall

1. **Mutable collection làm component** — tưởng immutable nhưng caller có thể mutate qua accessor:
   ```java
   record Team(String name, List<String> members) {}
   team.members().add("X");   // OK — bug!
   ```
   → Defensive copy trong compact constructor: `members = List.copyOf(members);`.

2. **Override accessor**: cho phép, nhưng phải trả cùng giá trị semantic:
   ```java
   public record Range(int from, int to) {
       public int from() { return Math.min(from, to); }   // hợp pháp
   }
   ```

3. **JPA / Hibernate**: ORM mặc định đòi `no-arg constructor` + setter — record không có. Có workaround (Hibernate 6+) nhưng thường record dùng làm DTO/projection chứ không phải Entity.

4. **Serialization**: record có hỗ trợ riêng, không cần `serialVersionUID`. JSON (Jackson 2.12+, Gson 2.10+) hỗ trợ tốt.

## Khi nào dùng record

| Use case | Nên record? |
|----------|-------------|
| DTO / API response | yes |
| Tuple cho method nhiều giá trị | yes |
| Pair / Triple | yes |
| Map.Entry alternative | yes |
| Domain value object | yes (nếu invariant đơn giản) |
| Algebraic Data Type (ADT) với sealed | **rất nên** — exhaustive switch |
| JPA Entity | thường không |
| Class có nhiều logic | không — record là data carrier |

## Effective Java tương đương

Record thay thế hầu hết "Item 17: Minimize mutability" — đặc biệt loại class chỉ có constructor + getter.

## Câu hỏi phỏng vấn

1. Record có thể extends class khác không? (Không.)
2. Record có thể là abstract không? (Không — implicitly final.)
3. `equals` của record so sánh dựa vào gì? (Tất cả component qua `Objects.equals`.)
4. Khi field là `List`, record còn immutable không? (Có thể không — cần defensive copy.)
5. Compact constructor khác canonical constructor ở đâu?
6. Record có hỗ trợ generic không? (Có.)
7. Khi nào KHÔNG dùng record?

## Tham chiếu

- [JEP 395: Records](https://openjdk.org/jeps/395)
- [JEP 440: Record Patterns](https://openjdk.org/jeps/440)
- [Record Classes (Oracle Tutorial)](https://docs.oracle.com/en/java/javase/21/language/records.html)
