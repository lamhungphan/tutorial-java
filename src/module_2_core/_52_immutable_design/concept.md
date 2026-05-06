# Immutable Design — Effective Java Item 17

## Lý thuyết

Class **immutable** là class mà state instance **không bao giờ thay đổi** sau khi tạo.

Lợi ích:

1. **Thread-safe miễn phí** — share giữa thread không cần đồng bộ.
2. **Hash an toàn** — dùng làm key `HashMap`/`HashSet` không lo state thay đổi.
3. **Defensive copy không cần** — caller không thể hỏng state.
4. **Failure atomicity** — nếu method throw, instance vẫn nguyên vẹn.
5. **Sharing dễ** — cache/intern hợp lệ (`String.intern`, `Integer.valueOf` -128..127).

Examples nổi tiếng: `String`, `Integer`/`Long`/...wrapper, `LocalDate`, `Optional`, `Path`, mọi `record` (về cơ bản).

## 5 quy tắc tạo class immutable

> Effective Java Item 17.

1. **Không có method nào thay đổi state** (không setter, không mutator).
2. **Class final** hoặc constructor private + factory — chống subclass override + mutate.
3. **Mọi field `private final`**.
4. **Defensive copy ở constructor** đối với reference type mutable (Date, array, mutable Collection).
5. **Defensive copy ở getter** đối với reference type mutable.

## Defensive copy — pitfall

`Date`, `Calendar`, `int[]`, `ArrayList` đều mutable → leak qua constructor/getter:

```java
class BadRange {
    private final Date start;
    public BadRange(Date d) { this.start = d; }   // leak — caller giữ ref vẫn mutate được
    public Date getStart() { return start; }       // leak — caller mutate được state nội bộ
}
```

→ `Date` mutable nguy hiểm: caller giữ tham chiếu rồi `setTime()` sẽ phá state.

**Đúng**:

```java
public BadRange(Date d) { this.start = new Date(d.getTime()); }
public Date getStart() { return new Date(start.getTime()); }
```

→ **Tốt hơn**: dùng `LocalDate`/`LocalDateTime`/`Instant` (đã immutable, không cần copy).

## Mutable collection trong record / immutable class

```java
record Team(String name, List<String> members) {}    // members vẫn có thể bị mutate qua accessor
```

Phải defensive copy + immutable view:

```java
public Team {
    members = List.copyOf(members);   // copy + immutable
}
```

## Builder pattern (Item 2)

Khi class immutable có > 4 field hoặc nhiều optional → builder:

```java
HttpRequest req = HttpRequest.get(url)
    .headers(headers)
    .timeoutMs(5_000)
    .build();
```

Lợi ích:

- Tham số có tên (vs telescoping constructor).
- Cho phép default value.
- Validation tập trung.
- Có thể thread-safe nếu builder dùng 1 lần.

## "With" pattern — copy with modification

```java
public HttpRequest withTimeout(int newTimeoutMs) {
    return toBuilder().timeoutMs(newTimeoutMs).build();
}
```

Tạo instance mới với 1 field khác — không mutate gốc. Là pattern functional.

> Java chưa có `with` keyword cho record (đề xuất ở JEP draft). Hiện phải tự viết hoặc dùng Lombok `@With`.

## Effective Java Item 17 ngoại lệ

Cho phép có **derived field cache** (transient computed value) miễn:

- Việc cache không thay đổi observable state.
- Tính toán lại cho cùng input → cùng kết quả.

Ví dụ: `String` cache `hash` lazy trong field `int hash`. Vẫn được coi là immutable.

## Pattern thực tế

| Pattern | Khi nào |
|---------|--------|
| Direct constructor | < 3 field, no optional |
| Static factory + constructor private | cần caching, return subtype |
| Builder | > 4 field hoặc nhiều optional |
| Record | DTO/value object đơn giản |
| Sealed + record | ADT (Result, Option, Tree) |

## Pitfall

- **Đừng `final` field "trong tâm trí"** — phải khai báo thật. JIT mới optimize được.
- **`final` reference field** không có nghĩa nội dung immutable. `final List<X>` vẫn có `add`.
- **Mutable static** trong class immutable phá nguyên tắc — tránh.
- **`clone()`** rủi ro — Effective Java item 13 khuyến nghị **avoid `Cloneable`**, dùng copy constructor / static factory.
- **Thread-safe ≠ immutable** — `ConcurrentHashMap` thread-safe nhưng mutable.

## Câu hỏi phỏng vấn

1. Liệt kê 5 quy tắc tạo immutable class.
2. Vì sao class immutable thread-safe miễn phí?
3. Defensive copy là gì? Khi nào cần?
4. Record có làm class immutable luôn không? (Phụ thuộc kiểu component.)
5. Builder vs telescoping constructor — khi nào chọn cái nào?
6. "With" pattern là gì? Lợi ích?
7. `String.hash` field cache — vẫn được coi immutable?
8. Mutable static field trong immutable class có vấn đề gì?

## Tham chiếu

- *Effective Java* (3rd ed.) — Item 17: Minimize mutability.
- *Effective Java* (3rd ed.) — Item 2: Consider a builder when faced with many constructor parameters.
- *Effective Java* (3rd ed.) — Item 50: Make defensive copies when needed.
- [`String` source — final + cache hash](https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/lang/String.java)
