# Enum Advanced — Strategy, EnumMap/EnumSet, Singleton

## Lý thuyết

`enum` của Java mạnh hơn nhiều so với enum cổ điển của C — thực ra là **class** ngầm extends `java.lang.Enum`, có thể có:

- Field (best practice: **final**).
- Method (kể cả `abstract`).
- Constructor (private).
- Implement interface.
- **Constant-specific method body** — mỗi constant override behavior riêng.

`enum` đảm bảo:

- **Singleton mỗi constant** — JVM bảo đảm 1 instance duy nhất.
- **Thread-safe** init.
- **Serialization-safe**.
- `==` so sánh hợp lệ (cùng identity).
- `values()`, `valueOf(String)` auto-gen.
- `ordinal()` (vị trí 0-based) — **đừng** dùng cho persistence.

## Pattern 1 — Strategy Enum (Effective Java Item 34)

Mỗi constant có behavior riêng → thay cho `switch` trên enum:

```java
enum Operation {
    PLUS  { public double apply(double x, double y) { return x + y; } },
    MINUS { public double apply(double x, double y) { return x - y; } },
    TIMES { public double apply(double x, double y) { return x * y; } };
    public abstract double apply(double x, double y);
}
```

Ưu điểm:

- Thêm constant mới = compile error nếu thiếu impl → exhaustive.
- Behavior đóng cùng data → cohesion cao.
- Test dễ.

## Pattern 2 — Functional field

```java
enum BoolOp {
    AND((a, b) -> a && b),
    OR((a, b) -> a || b);
    private final BiFunction<Boolean, Boolean, Boolean> op;
    ...
}
```

Phù hợp khi muốn linh hoạt hoặc tái sử dụng strategy ở nơi khác.

## Pattern 3 — Singleton qua enum (Item 3)

```java
public enum DBPool { INSTANCE;  ... }
```

**Cách singleton an toàn nhất**: thread-safe, serialization-safe, reflection-safe miễn phí. Vượt trội DCL/Holder idiom với rủi ro reflection bypass.

## `EnumMap` & `EnumSet`

### `EnumMap<K extends Enum<K>, V>`

- **Array-backed** — index theo `ordinal()`.
- O(1) get/put thực sự, không cần hash.
- Thứ tự iteration = thứ tự khai báo enum.
- Tốn ít memory hơn HashMap rất nhiều.

```java
EnumMap<Day, String> menu = new EnumMap<>(Day.class);
```

### `EnumSet<E extends Enum<E>>`

- **Bitset-backed** — long mask cho enum ≤ 64 constant (`RegularEnumSet`), `long[]` nếu lớn hơn (`JumboEnumSet`).
- Thay thế `int` bitmask kiểu C — type-safe, đẹp.
- Operation: `of`, `range`, `complementOf`, `noneOf`, `allOf`, `copyOf`.

```java
EnumSet<Day> weekend = EnumSet.of(Day.SAT, Day.SUN);
EnumSet<Day> workday = EnumSet.complementOf(weekend);
```

> Cả 2 đều **không thread-safe** — wrap qua `Collections.synchronizedMap` / `synchronizedSet` nếu cần.

## Pitfall

- **Đừng dùng `ordinal()`** cho persistence — thêm/xoá constant đổi ordinal → corrupt data. Persist tên (`name()`).
- **`values()`** trả mảng mới mỗi lần (defensive copy) — cache nếu hot path.
- Enum không thể extends class khác. Nhưng **có thể implements interface**.
- Constant-specific body làm class file phình to vì mỗi constant là 1 inner class.
- **Switch trên enum đôi khi vẫn OK** với Java 21 pattern matching exhaustive — nhưng method override vẫn cleaner.

## So sánh nhanh

| | `HashMap<Day,V>` | `EnumMap<Day,V>` | `int[]` ordinal |
|-|------------------|-------------------|-----------------|
| Memory | high (object overhead) | low (array) | lowest |
| Speed get/put | hash + bucket scan | array index | array index |
| Type-safe | có | có | không |
| Iteration order | undefined | declaration order | natural |

## Câu hỏi phỏng vấn

1. Vì sao `enum` singleton an toàn nhất?
2. `EnumMap` khác `HashMap` thế nào? Khi nào dùng?
3. `EnumSet` chứa thông tin trong bitmask hay bucket? Có giới hạn không?
4. Sao **không nên** dùng `ordinal()` cho persistence?
5. Constant-specific method body là gì? Khi nào dùng?
6. Enum implements interface được không? Extends class? (Có / không.)
7. `values()` trả về cùng instance mỗi lần không? (Không — defensive copy.)

## Tham chiếu

- *Effective Java* (3rd ed.) — Items 34-41 về Enum.
- [`Enum<E>` Javadoc](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Enum.html)
- [`EnumMap`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/EnumMap.html)
- [`EnumSet`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/EnumSet.html)
