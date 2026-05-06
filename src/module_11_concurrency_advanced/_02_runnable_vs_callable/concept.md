# 02 — `Runnable` vs `Callable`, `Future`, `FutureTask`

## Lý thuyết

| | `Runnable` | `Callable<V>` |
|-|------------|---------------|
| Trả giá trị | Không (`void run()`) | Có (`V call()`) |
| Throws checked | Không | Có (`throws Exception`) |
| Phiên bản | Java 1.0 | Java 5 |
| Dùng với | `Thread`, `ExecutorService.execute` | `ExecutorService.submit/invokeAll/invokeAny` |
| Functional interface | yes (`@FunctionalInterface`) | yes |

## `Future<V>`

Đại diện cho **kết quả tương lai** của 1 task async:

```java
Future<Integer> f = pool.submit(callable);
Integer result = f.get();         // block đến khi có kết quả
Integer r2 = f.get(500, MILLISECONDS);  // block tối đa 500ms
boolean done = f.isDone();
boolean cancelled = f.cancel(true);     // mayInterruptIfRunning
```

### `Future.get` quan trọng

- `get()` **block thread gọi** đến khi task xong → tránh dùng trên main loop.
- Throw `ExecutionException` wrapping exception thực → `.getCause()` để xem.
- Throw `InterruptedException` nếu thread gọi bị interrupt.
- Throw `CancellationException` nếu task đã bị cancel.

### `Future.cancel(true)` — chú ý

- Nếu task chưa start → đánh dấu cancelled, không bao giờ chạy.
- Nếu đang chạy + `mayInterruptIfRunning=true` → call `Thread.interrupt()` trên thread đang chạy task. Task phải **xử lý interrupt** mới dừng.
- Nếu task không xử lý interrupt → vẫn chạy đến cùng. Cancel chỉ mang tính "best effort".

## `FutureTask<V>`

Wrapper cho cả `Runnable` và `Callable` thành 1 đối tượng vừa chạy được (`Runnable`) vừa lấy kết quả được (`Future`):

```java
FutureTask<Long> ft = new FutureTask<>(callable);
new Thread(ft).start();
ft.get();
```

Là nền tảng nội bộ của `ExecutorService.submit`.

## `invokeAll` vs `invokeAny`

| | Mô tả |
|-|-------|
| `invokeAll(tasks)` | Submit hết, **block** đến khi MỌI task xong (success / fail). Trả `List<Future>`. |
| `invokeAny(tasks)` | Submit hết, đợi **bất kỳ** task xong success → cancel còn lại, trả result. |

Dùng `invokeAll(tasks, timeout, unit)` để limit tổng thời gian.

## CompletableFuture (J8) thay thế

`Future` thuần bị giới hạn:

- Không compose / chain.
- `get()` block — không non-blocking.
- Không có error handling functional.

→ J8 ra đời `CompletableFuture` (xem [`12_completable_future/`](../_12_completable_future/concept.md)). Trong code hiện đại, **prefer `CompletableFuture`** (hoặc reactive Mono/Flux, hoặc virtual threads).

## Pitfall

- **`Future.get()` không timeout** trên hot path → deadlock toàn service. Luôn dùng `get(timeout, unit)`.
- **Không catch `ExecutionException`** → wrap exception bị nuốt.
- **Cancel không strict** — task hợp tác mới dừng.
- **`invokeAll` block thread caller** — không nên gọi trong thread của Tomcat NIO worker.
- **Submit `Runnable` mất exception** — nếu `Runnable.run` throw, không có ai biết. Dùng `submit(Callable)` để wrap qua `Future` rồi check.

## Câu hỏi phỏng vấn

1. `Runnable` vs `Callable` khác gì?
2. `Future.get()` block thread nào? Có timeout chưa?
3. `cancel(true)` có dừng task ngay lập tức không?
4. Khi task throw exception, làm sao biết?
5. `FutureTask` để làm gì?
6. `invokeAll` vs `invokeAny`?
7. Tại sao prefer `CompletableFuture` thay `Future`?

## Tham chiếu

- [`Callable`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Callable.html), [`Future`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Future.html), [`FutureTask`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/FutureTask.html)
- *Java Concurrency in Practice* — Chapter 6: Task Execution.
