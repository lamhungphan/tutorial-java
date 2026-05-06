# Sealed Classes (Java 17, JEP 409)

## Lý thuyết

`sealed` cho phép tác giả khai báo class/interface và **giới hạn chính xác** ai được phép kế thừa qua `permits`.

```java
public sealed interface Shape
        permits Circle, Square, Triangle, Polygon {}
```

Mỗi subtype phải dùng **một** trong:

| Modifier | Ý nghĩa |
|----------|--------|
| `final` | đóng — không cho extends tiếp |
| `sealed` | tiếp tục bị giới hạn `permits` riêng |
| `non-sealed` | mở lại — bất cứ ai cũng có thể extends |

## Vì sao cần?

Trước Java 17, có 2 trạng thái:

- `public` class → ai cũng extends được (kể cả lib bên ngoài).
- `final` class → đóng hoàn toàn.

Không có cách "mở cho 1 nhóm cụ thể". Sealed lấp khoảng trống đó.

Lợi ích:

1. **Closed type hierarchy** — tác giả kiểm soát domain hoàn toàn, không bị inject subtype lạ.
2. **Exhaustive `switch`** — compiler biết hết permitted subtype → không cần `default`. Nếu thêm subtype mới → mọi switch chưa update đều **compile error** → giảm bug regression.
3. **ADT (Algebraic Data Type)** — kết hợp `sealed interface` + `record` = pattern functional language (Result, Option, Tree).

## Quy tắc

- Permitted class phải ở **cùng module** (hoặc cùng package nếu unnamed module).
- Có thể bỏ `permits` nếu mọi subtype ở **cùng file** — compiler tự suy.
- Sealed **không thay** `final` cho 1 lớp đơn lẻ — nó giải pháp cho hierarchy.

## Pattern matching for switch (J21)

Sealed + record là combo idiomatic:

```java
sealed interface Result<T> permits Success, Failure {}
record Success<T>(T value) implements Result<T> {}
record Failure<T>(String err) implements Result<T> {}

return switch (result) {
    case Success<T>(T v) -> render(v);
    case Failure<T>(String e) -> renderError(e);
    // exhaustive — không cần default
};
```

## Khi nào dùng

| Use case | Sealed phù hợp? |
|----------|----------------|
| Domain model với số subtype xác định (Shape, Event, Token) | yes |
| Result / Either / Option / Tree (ADT) | rất phù hợp |
| Plugin system, mở cho user extends | **không** — dùng interface bình thường |
| Chỉ có 1 implementation | dùng `final` |

## Pitfall

- Sealed cấm cross-module subclass (Java 17 strict). Library tách public API + sealed phải đồng bộ release.
- Reflection vẫn có thể bypass nếu bạn build proxy/CGLib lạ — nhưng compile-time check vẫn ép discipline.
- IDE/compiler check exhaustive trên `switch`, KHÔNG trên `if-else if instanceof` chuỗi → code dễ bị thiếu nhánh khi update permit.

## Câu hỏi phỏng vấn

1. `sealed` giải quyết vấn đề gì so với `final` và `public`?
2. 3 modifier subtype của sealed là gì? Khác nhau ra sao?
3. Permitted subtype phải nằm cùng package không? Cùng module?
4. Khi nào KHÔNG nên dùng sealed?
5. Sealed + record + switch tạo ra pattern gì? (ADT.)
6. Vì sao `switch` trên sealed type không cần `default`?

## Tham chiếu

- [JEP 409: Sealed Classes](https://openjdk.org/jeps/409)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
- [JEP 440: Record Patterns](https://openjdk.org/jeps/440)
