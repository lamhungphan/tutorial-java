# `equals` & `hashCode` Contract — Effective Java Items 10-11

## Lý thuyết

`Object.equals` và `Object.hashCode` là **contract** mà **mọi** collection hash-based (`HashMap`, `HashSet`, `ConcurrentHashMap`, `LinkedHashMap`, ...) dựa vào để hoạt động đúng.

### Contract của `equals`

5 tính chất (JLS §8.4 / `Object.equals` Javadoc):

| # | Tên | Ý nghĩa |
|---|-----|--------|
| 1 | **Reflexive** | `x.equals(x) == true` |
| 2 | **Symmetric** | `x.equals(y) == y.equals(x)` |
| 3 | **Transitive** | `x.equals(y) && y.equals(z)` → `x.equals(z)` |
| 4 | **Consistent** | Gọi nhiều lần với cùng input → cùng kết quả (nếu state không đổi) |
| 5 | **Non-null** | `x.equals(null) == false` |

### Contract của `hashCode`

| # | Quy tắc |
|---|---------|
| 1 | Consistent (nếu state không đổi). |
| 2 | `a.equals(b)` ⇒ `a.hashCode() == b.hashCode()` (BẮT BUỘC). |
| 3 | `!a.equals(b)` thì hashCode **có thể** bằng nhau (collision được phép, nhưng càng phân tán càng tốt). |

> **Vi phạm quy tắc 2** = bug nghiêm trọng nhất: `HashMap.put(a, v)` rồi `get(b)` nơi `a.equals(b) == true` sẽ **không tìm thấy**.

## Khuôn mẫu chuẩn (Effective Java Item 10)

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;                    // optimization
    if (!(o instanceof MyClass that)) return false;
    return f1 == that.f1                           // primitive
        && Float.compare(f2, that.f2) == 0         // float (NaN handling)
        && Double.compare(f3, that.f3) == 0
        && Objects.equals(field, that.field);      // object — null-safe
}

@Override
public int hashCode() {
    return Objects.hash(f1, f2, f3, field);
}
```

> Tốt hơn nữa cho hot path: cache `hashCode` (như `String`):
>
> ```java
> private int hash;   // 0 mặc định
> public int hashCode() {
>     int h = hash;
>     if (h == 0) hash = h = Objects.hash(...);
>     return h;
> }
> ```

## Lý do phổ biến gặp bug

### Bug 1: Override `equals` mà quên `hashCode`

```java
class BadKey {
    int x;
    @Override public boolean equals(Object o) { ... }
    // hashCode kế thừa Object -> identity-based
}
```

`HashSet.contains(new BadKey(1))` trả `false` dù vừa add. Đây là bug **kinh điển** ở pre-Lombok.

### Bug 2: `hashCode` hằng

```java
public int hashCode() { return 42; }
```

Hợp lệ về contract, nhưng mọi key vào 1 bucket → `HashMap` tuyến tính → O(N). Java 8 chuyển sang red-black tree (treeify ≥ 8) — vẫn O(log N) nhưng worse-case.

### Bug 3: `equals` dùng `getClass`

```java
if (o == null || getClass() != o.getClass()) return false;
```

Không tương thích với inheritance hợp lệ (không nhận subtype) — phá Liskov. Effective Java khuyến nghị `instanceof`.

### Bug 4: `equals` so với type khác

```java
class CIString {
    public boolean equals(Object o) {
        if (o instanceof String t) return s.equalsIgnoreCase(t);   // SAI!
        ...
    }
}
```

Phá đối xứng: `cis.equals(str)` true nhưng `str.equals(cis)` false. `HashSet` chứa cả 2 sẽ hành xử bất ổn.

### Bug 5: `hashCode` không nhất quán với `equals`

```java
equals(): so sánh field a, b
hashCode(): chỉ dùng a
```

Hợp lệ. Nhưng:

```java
equals(): so sánh case-insensitive
hashCode(): không lower-case
```

Vi phạm contract → bug.

## Records — giải pháp đơn giản nhất

Record (J16+) auto-generate `equals`/`hashCode`/`toString` đúng contract dựa trên **tất cả** component. → KHÔNG VIẾT TAY nữa nếu DTO/value object đơn giản.

```java
public record Point(int x, int y) {}   // equals/hashCode đã đúng
```

## Khi cần có `equals` thì cũng cần `hashCode` — và ngược lại?

- **Có override `equals`** → **bắt buộc** override `hashCode`.
- **Override `hashCode`** mà không override `equals` → hợp pháp nhưng thường vô nghĩa (chỉ cải thiện hash distribution mà thôi).

## `@EqualsAndHashCode` của Lombok

```java
@EqualsAndHashCode
@EqualsAndHashCode.Include / Exclude
```

Tiện cho POJO. Nhưng:

- Macro nên IDE/javac plugin đặc thù.
- Nếu có thể, **prefer record** thay Lombok.

## Pitfall

- Mutable field trong key của `HashMap` — **đừng**. Sau khi put, sửa field → hashCode đổi → key "biến mất".
- Dùng float/double trong equals: phải `Float.compare` (xử lý NaN, +0/-0). `==` cho double sẽ sai với NaN.
- `equals` không được throw exception (trừ NullPointerException khi argument là `null` — và thực ra cũng không nên).

## Câu hỏi phỏng vấn

1. 5 tính chất của `equals`?
2. Tại sao `equals` mà không `hashCode` thì phá HashSet?
3. `hashCode` trả hằng số có vi phạm contract không? Hậu quả?
4. `getClass` vs `instanceof` trong equals — chọn cái nào, vì sao?
5. Record giải quyết bài toán này thế nào?
6. `equals` cho mutable field có hợp lệ không?
7. `Objects.hash` khác `Objects.hashCode` thế nào?
8. Cache `hashCode` cho String là pattern gì?
9. Kế thừa class có override equals — subclass có nên thêm field rồi override lại? (Effective Java item 10 — generally no, prefer composition.)

## Tham chiếu

- *Effective Java* (3rd ed.) — Item 10: Obey the general contract when overriding equals.
- *Effective Java* (3rd ed.) — Item 11: Always override hashCode when you override equals.
- [`Object.equals` Javadoc](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object))
- [`Objects` utility](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html)
