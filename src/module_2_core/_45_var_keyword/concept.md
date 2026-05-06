# `var` — Local Variable Type Inference (Java 10, JEP 286)

## Lý thuyết

`var` cho phép **compiler suy ra** type của biến local từ initializer, giúp giảm boilerplate khi type vế phải đã rõ.

```java
var list = new ArrayList<String>();   // ArrayList<String>
var n = 42;                           // int
```

Đặc điểm quan trọng:

- **Static typing không đổi** — `var` chỉ là cú pháp, runtime không có dynamic type. Type được fix ở compile time.
- **Chỉ dùng cho `local variable`**, không cho field/parameter/return.
- Phải có **initializer rõ ràng** (không `var x;`, không `var x = null;`).
- **`var` không phải keyword** mà là *reserved type name* — biến tên `var` vẫn hợp lệ (nhưng đừng làm thế).

## Khi nào KHÔNG dùng được

| Vị trí | Lý do |
|--------|------|
| `field` của class | scope quá rộng, cần signature rõ ràng |
| `method parameter` | API signature là contract |
| `method return type` | API contract |
| `catch (var e)` | đặc tả không cho phép |
| Lambda parameter | đã có inference riêng |
| Không initializer | không có gì để infer |
| `var x = null` | `null` không có type cụ thể |
| `var arr = {1,2,3}` | array initializer cần explicit type |

## Pitfall (góc senior)

1. **Diamond + `var` mất generic**:
   ```java
   var list = new ArrayList<>();   // ArrayList<Object>!
   ```
   → Luôn viết `new ArrayList<String>()` khi dùng `var`.

2. **`var` với capture wildcard** có thể tạo type không cấp được tham số:
   ```java
   var any = List.of(1, 2.0, "three"); // List<? extends Serializable & Comparable<?>>
   ```

3. **Numeric literal**: `var x = 0;` là `int`, không phải `byte`/`short`. Phải dùng suffix `L`/`f`.

4. **Code review**: `var` ẩn type → phụ thuộc IDE. Đọc PR trên GitHub mà tên biến mơ hồ → không hiểu nhanh.

## Best practice

- Dùng `var` khi RHS làm rõ type (`new XXX()`, factory với type params explicit, stream `.toList()` rõ ràng).
- Đặt **tên biến mô tả** thay cho thông tin type bị mất: `customerCount` thay vì `c`.
- **Tránh `var`** khi type ẩn (kết quả method, deserialize JSON, generic sâu).
- Phối hợp tốt với `for-each`, `try-with-resources`, stream pipeline.

## Câu hỏi phỏng vấn

1. `var` có làm Java thành dynamic typing không? (Không — vẫn static.)
2. Khác biệt với `auto` của C++ hay `let` của JS? (Static, scope local, immutable type.)
3. Code này compile không: `var x = null;`? (Không.)
4. `var list = new ArrayList<>();` — type của `list` là gì? (`ArrayList<Object>`.)
5. Khi nào KHÔNG nên dùng `var`?

## Tham chiếu

- [JEP 286: Local-Variable Type Inference](https://openjdk.org/jeps/286)
- [Style Guidelines for Local Variable Type Inference (Oracle)](https://openjdk.org/projects/amber/guides/lvti-style-guide)
- [JEP 323: Local-Variable Syntax for Lambda Parameters (J11)](https://openjdk.org/jeps/323)
