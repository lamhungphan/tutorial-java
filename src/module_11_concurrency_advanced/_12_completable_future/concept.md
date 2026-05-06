# 12 — `CompletableFuture` & Async Pipeline

## Lý thuyết

`CompletableFuture<T>` (Java 8) khắc phục giới hạn của `Future`:

- **Compose** chain async operations.
- **Combine** nhiều future.
- **Error handling** functional.
- **Timeout** built-in (J9+).
- **Custom executor** cho mỗi stage.

`CompletableFuture` implement `Future` + `CompletionStage`. `CompletionStage` là interface API chính.

## Tạo

```java
CompletableFuture.completedFuture(value);              // đã xong
CompletableFuture.failedFuture(throwable);             // đã fail
CompletableFuture.supplyAsync(() -> compute());        // async chạy commonPool
CompletableFuture.supplyAsync(supplier, customExec);   // tự pool
CompletableFuture.runAsync(() -> sideEffect());        // void

var cf = new CompletableFuture<String>();
cf.complete("ok");                                     // manual complete
cf.completeExceptionally(ex);                          // manual fail
```

## Transform / Compose

| Method | Map type | Sync/Async |
|--------|----------|-----------|
| `thenApply(fn)` | `T -> U` | sync trên callback thread |
| `thenApplyAsync(fn)` | `T -> U` | chạy trên `commonPool` |
| `thenApplyAsync(fn, exec)` | `T -> U` | chạy trên `exec` |
| `thenAccept(c)` | `T -> void` | side effect |
| `thenRun(r)` | `() -> ()` | bỏ qua kết quả |
| `thenCompose(fn)` | `T -> CF<U>` | flat map (chain) |
| `thenCombine(other, fn)` | kết hợp 2 | merge |

→ `thenCompose` thay `flatMap` — quan trọng nhất khi chain nhiều API call.

## Combine

| Method | Mô tả |
|--------|------|
| `thenCombine(other, fn)` | đợi cả 2 → apply `fn(a, b)` |
| `applyToEither(other, fn)` | lấy CÁI XONG TRƯỚC |
| `acceptEither(other, c)` | tương tự, side effect |
| `CompletableFuture.allOf(f1, f2, ...)` | đợi tất cả → `CF<Void>` (tự lấy result từng cái) |
| `CompletableFuture.anyOf(f1, f2, ...)` | bất kỳ cái nào → `CF<Object>` |

```java
// Tự convert allOf -> List<Result>
var futures = List.of(f1, f2, f3);
var all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
    .thenApply(__ -> futures.stream().map(CompletableFuture::join).toList());
```

## Error handling

| Method | Khi chạy | Có truy cập result/exception? |
|--------|----------|-------------------------------|
| `exceptionally(fn)` | chỉ khi fail | `Throwable -> T` |
| `exceptionallyCompose(fn)` | chỉ khi fail | `Throwable -> CF<T>` |
| `handle(bi)` | luôn (success + fail) | `(T, Throwable) -> U` |
| `whenComplete(c)` | luôn | `(T, Throwable) -> void` (side effect, không thay đổi result) |

```java
fetchData()
    .thenApply(this::process)
    .exceptionally(ex -> "fallback")        // chỉ chạy nếu trên fail
    .whenComplete((r, ex) -> log(r, ex));    // log success + fail
```

## Timeout (Java 9+)

```java
cf.orTimeout(500, MILLISECONDS);                    // fail với TimeoutException
cf.completeOnTimeout(fallback, 500, MILLISECONDS);  // fallback value
```

Trước J9 phải dùng `CompletableFuture.delayedExecutor` hoặc thư viện ngoài.

## Patterns

### 1. Tránh `commonPool` cho I/O

`supplyAsync(supplier)` mặc định chạy trên `ForkJoinPool.commonPool` — **shared toàn JVM**, parallelism nhỏ. I/O blocking sẽ chiếm worker.

→ **Luôn pass executor riêng**:

```java
private static final Executor IO_POOL = Executors.newFixedThreadPool(50);

CompletableFuture.supplyAsync(() -> httpCall(), IO_POOL);
```

### 2. Retry với backoff

```java
static <T> CompletableFuture<T> withRetry(
        Supplier<CompletableFuture<T>> op, int retries, Duration backoff) {
    return op.get().handle((r, ex) -> {
        if (ex == null) return CompletableFuture.completedFuture(r);
        if (retries <= 0) return CompletableFuture.<T>failedFuture(ex);
        return CompletableFuture.supplyAsync(() -> null,
                    CompletableFuture.delayedExecutor(backoff.toMillis(), MILLISECONDS))
            .thenCompose(__ -> withRetry(op, retries - 1, backoff.multipliedBy(2)));
    }).thenCompose(f -> f);
}
```

### 3. Hedged request

Gửi request đến primary; nếu sau X ms chưa response → gửi đồng thời đến backup; lấy cái nhanh nhất:

```java
var hedged = primary.applyToEither(
    delayed(backup, 50, MILLISECONDS),
    x -> x);
```

Pattern phổ biến cho **tail latency**.

### 4. Bulkhead (rate-limit concurrent)

Dùng `Semaphore` ngoài để giới hạn số CompletableFuture chạy đồng thời (bounded concurrency).

### 5. Fan-out + fan-in

```java
List<CompletableFuture<Result>> tasks = ids.stream()
    .map(id -> CompletableFuture.supplyAsync(() -> fetch(id), IO_POOL))
    .toList();

CompletableFuture<List<Result>> all = CompletableFuture
    .allOf(tasks.toArray(new CompletableFuture[0]))
    .thenApply(__ -> tasks.stream().map(CompletableFuture::join).toList());
```

## Pitfall

- **`get()` không timeout** trong web handler → block thread vĩnh viễn nếu future hang.
- **`thenApply` chạy trên thread completer** — nếu callback nặng → kéo dài stage trước. Dùng `thenApplyAsync` nếu sợ.
- **Default `commonPool` cho I/O** → kẹt parallelStream toàn app.
- **`exceptionally`/`handle`** chỉ apply ở stage đó — exception trong stage sau vẫn cần xử lý.
- **Cancel không propagate đầy đủ** — `cancel(true)` set state nhưng task đang chạy không bị interrupt nếu không được wrap đúng.
- **Stack trace không liền** — async stack rời rạc, debug khó. Java 21 SC + virtual threads cải thiện.
- **Async race** — `thenAccept` + `thenRun` có thể chạy không theo thứ tự nếu không chain đúng.

## So sánh với alternatives

| | `CompletableFuture` | Reactive (Mono/Flux) | Virtual Threads + sync code |
|-|----------------------|------------------------|------------------------------|
| Style | functional callback | reactive stream | imperative |
| Backpressure | không | có | không cần (block free) |
| Streaming | yếu | mạnh | n/a |
| Throughput | tốt | tốt nhất | tốt |
| Học | trung | khó | dễ |
| Debug | trung | khó | dễ |
| Khi chọn | API call vài bước | streaming, websocket | I/O đơn giản, J21+ |

→ **J21+**: nhiều use case có thể thay `CompletableFuture` bằng virtual threads + sync code.

## Câu hỏi phỏng vấn

1. `Future` vs `CompletableFuture`?
2. `thenApply` vs `thenCompose` vs `thenCombine`?
3. `handle` vs `exceptionally` vs `whenComplete`?
4. Vì sao tránh `supplyAsync(supplier)` không pass executor?
5. `allOf` trả về gì? Cách lấy `List<Result>`?
6. Cách implement timeout trong J8 vs J9+?
7. Hedged request là gì? Pattern nào?
8. Khi nào prefer Virtual Threads thay `CompletableFuture`?

## Tham chiếu

- [`CompletableFuture` Javadoc](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html)
- [`CompletionStage`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletionStage.html)
- *Java Concurrency in Practice* — Chapter 6.
- [Modern Java in Action](https://www.manning.com/books/modern-java-in-action) — Chapter 16.
