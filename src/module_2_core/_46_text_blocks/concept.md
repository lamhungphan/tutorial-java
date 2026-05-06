# Text Blocks (Java 15, JEP 378)

## Lý thuyết

Text block là string literal nhiều dòng, mở bằng `"""` + newline, đóng bằng `"""`.

```java
String s = """
        Line 1
        Line 2
        """;
```

- Type vẫn là `String` thường — không có class mới.
- **Incidental whitespace strip**: cột tối thiểu (so giữa các dòng + dòng đóng `"""`) sẽ bị xoá.
- **Essential whitespace** (indent có chủ ý) được giữ nguyên.

## Khác String thường

| | String cũ | Text block |
|-|-----------|------------|
| Newline | `"\n"` | newline thật |
| Quote `"` | phải `\"` | dùng trực tiếp |
| Đa dòng | `+ "\n" +` | chỉ enter |
| Indent | escape mệt | tự strip |

## Escape đặc biệt

| Escape | Tác dụng |
|--------|----------|
| `\s` | giữ space ở cuối dòng (vì compiler trim trailing space) |
| `\` ở cuối dòng | line continuation (nối với dòng kế) |
| `\"""` | đặt 3 dấu `"` trong text block |

## Method tiện ích

- `String.formatted(args...)` (J15) — shortcut `String.format(this, args)`.
- `String.stripIndent()` — strip thủ công.
- `String.translateEscapes()` — dịch escape `\n`, `\t`...

## Use case kinh điển

- JSON, SQL, GraphQL query.
- HTML/XML template.
- Help message, banner.
- Embedded test fixture.
- Multi-line error.

## Pitfall

- **Trailing whitespace** bị xoá. Cần giữ space cuối dòng → dùng `\s`.
- **Cột đóng `"""` quan trọng**: kéo nó ra trái → tăng indent giữ lại.
- Đừng nhầm giữa text block và raw string của Kotlin/Python — Java vẫn xử lý escape (`\n`, `\t`).
- Text block **không có** string interpolation built-in. Java 21-22 từng có *string templates* (preview) nhưng đã rút lại để redesign. Vẫn phải dùng `.formatted()` / `String.format()` / `MessageFormat`.

## Best practice (senior view)

- Dùng cho mọi string đa dòng — code dễ đọc hơn nhiều.
- Khi nhúng JSON/SQL, đặt `"""` ngay sau `=` để rõ ranh giới.
- Format động qua `.formatted()` thay `+`.
- Thận trọng với **whitespace** — chạy thử so sánh `\s` vs có/không indent.

## Câu hỏi phỏng vấn

1. Text block khác String thường ở runtime không? (Không — cùng class.)
2. Vì sao indent bị strip? Quyết định bởi cái gì? (Dòng đóng `"""`.)
3. Tại sao Java 22 remove String Templates?
4. Khi nào KHÔNG nên dùng text block? (Single-line ngắn — mất sạch sẽ.)

## Tham chiếu

- [JEP 378: Text Blocks](https://openjdk.org/jeps/378)
- [Programmer's Guide to Text Blocks](https://docs.oracle.com/en/java/javase/21/text-blocks/index.html)
