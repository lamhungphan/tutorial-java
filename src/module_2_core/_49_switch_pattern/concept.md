# Switch Expression & Pattern Matching

## Phần 1 — Switch Expression (Java 14, JEP 361)

`switch` dạng EXPRESSION trả về giá trị, sử dụng arrow `->`:

```java
int hours = switch (day) {
    case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> 8;
    case SATURDAY, SUNDAY -> 0;
};
```

### Khác biệt so với switch statement cũ

| | Statement (cũ) | Expression (J14+) |
|-|---------------|--------------------|
| Trả giá trị | Không | Có |
| Fall-through | Có (cần `break`) | **Không** |
| Multi-label | `case A: case B:` | `case A, B ->` |
| Block body | `{ ... break; }` | `case A -> { ... yield x; }` |
| Exhaustive check | Không bắt buộc | **Có** với enum/sealed |

### `yield`

Khi case body cần nhiều statement → block + `yield value`:

```java
case FRIDAY -> {
    log("TGIF");
    yield "weekend incoming";
}
```

## Phần 2 — Pattern Matching for `instanceof` (Java 16, JEP 394)

```java
if (obj instanceof String s && s.length() > 3) {
    use(s.toUpperCase());
}
```

- Kết hợp **kiểm tra type + cast** trong 1 expression.
- Biến binding (`s`) **flow scoping**: chỉ visible khi nhánh đảm bảo match.

## Phần 3 — Pattern Matching for `switch` (Java 21, JEP 441)

`switch` nay nhận **pattern** thay chỉ giá trị literal:

```java
String s = switch (obj) {
    case Integer i  -> "int " + i;
    case String t   -> "string " + t;
    case null       -> "null";        // J21+ no NPE
    default         -> "other";
};
```

### Record pattern (JEP 440)

Deconstruct record trong switch:

```java
case Circle(Point p, double r) -> Math.PI * r * r;
case Rectangle(double w, double h) -> w * h;
```

### Guards (`when`)

Điều kiện thêm cho mỗi case:

```java
case Car c when c.doors() >= 4 -> "family";
case Car c -> "sport";
```

### Null handling

J21+, `case null` cho phép xử lý null trực tiếp. Trước đó, `switch(null)` ném `NullPointerException`. Có thể `case null, default` chung 1 nhánh.

## Lợi ích senior view

1. **Loại bỏ chuỗi `if-else if instanceof`** — clearer intent.
2. **Exhaustive check** với sealed type — compiler bắt thiếu nhánh.
3. **Eliminate cast errors** — không còn `(SomeType) obj` rồi sai.
4. **ADT idiomatic**: sealed + record + pattern matching = closed sum types.
5. **Refactor an toàn**: thêm subtype mới vào sealed → mọi switch chưa update đều compile error → không miss case.

## Pitfall

- **Order matters**: pattern cụ thể phải đặt **trước** pattern tổng quát:
  ```java
  case Car c -> ...        // SAI — bắt cả ElectricCar
  case ElectricCar e -> ...
  ```
  Compile error vì pattern bên dưới không reachable.

- **`default` không cần khi sealed exhaustive** — nhưng **nên** thêm `default` cho non-sealed để future-proof.

- **`case null`** mặc định không match — nếu không khai báo, `switch(null)` vẫn NPE.

- **Guards (`when`)** không được CHỈ phụ thuộc vào side effect (compiler không kiểm tra purity, nhưng đừng làm thế).

## Câu hỏi phỏng vấn

1. Switch expression khác statement ở những điểm gì?
2. `yield` để làm gì? Khi nào cần?
3. Pattern matching for switch là gì? Java phiên bản nào?
4. Vì sao thứ tự case quan trọng trong pattern matching?
5. `case null` đã thay đổi như thế nào ở Java 21?
6. Switch trên sealed interface có cần `default`? Vì sao?
7. Record pattern là gì? Cho ví dụ.
8. Guard với `when` để làm gì?

## Tham chiếu

- [JEP 361: Switch Expressions](https://openjdk.org/jeps/361)
- [JEP 394: Pattern Matching for instanceof](https://openjdk.org/jeps/394)
- [JEP 440: Record Patterns](https://openjdk.org/jeps/440)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
