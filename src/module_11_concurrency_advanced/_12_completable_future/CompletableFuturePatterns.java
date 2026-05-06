package module_11_concurrency_advanced._12_completable_future;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CompletableFuturePatterns {

    // PATTERN 1: Custom executor — TRÁNH dùng commonPool cho I/O
    private static final Executor IO_EXEC = Executors.newFixedThreadPool(50);

    // PATTERN 2: Retry với exponential backoff
    static <T> CompletableFuture<T> withRetry(Supplier<CompletableFuture<T>> op,
                                               int maxRetries, Duration baseBackoff) {
        return op.get().handle((r, ex) -> {
            if (ex == null) return CompletableFuture.completedFuture(r);
            if (maxRetries <= 0) return CompletableFuture.<T>failedFuture(ex);
            long delayMs = baseBackoff.toMillis() * (long) Math.pow(2, 5 - maxRetries);
            System.out.println("  retry, delay=" + delayMs + "ms, maxLeft=" + (maxRetries - 1));
            return CompletableFuture.supplyAsync(() -> null,
                            CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS))
                    .thenCompose(__ -> withRetry(op, maxRetries - 1, baseBackoff));
        }).thenCompose(f -> f);
    }

    // PATTERN 3: Bulk parallel với fail-fast
    // (Đợi tất cả; nếu 1 fail -> tổng thể fail)

    // PATTERN 4: Hedged request — thử 2 server cùng lúc, lấy nhanh nhất
    static <T> CompletableFuture<T> hedged(Supplier<CompletableFuture<T>> primary,
                                            Supplier<CompletableFuture<T>> backup,
                                            Duration delay) {
        var p = primary.get();
        var b = CompletableFuture.supplyAsync(() -> null,
                        CompletableFuture.delayedExecutor(delay.toMillis(), TimeUnit.MILLISECONDS))
                .thenCompose(__ -> backup.get());
        return p.applyToEither(b, x -> x);
    }

    public static void main(String[] args) throws Exception {
        // Demo Pattern 1
        var fut = CompletableFuture.supplyAsync(() -> {
            System.out.println("running on " + Thread.currentThread().getName());
            return "done";
        }, IO_EXEC);
        System.out.println(fut.get());

        // Demo Pattern 2 — retry
        var counter = new java.util.concurrent.atomic.AtomicInteger();
        var result = withRetry(
                () -> CompletableFuture.supplyAsync(() -> {
                    int n = counter.incrementAndGet();
                    System.out.println("attempt " + n);
                    if (n < 3) throw new RuntimeException("fail " + n);
                    return "got it on " + n;
                }, IO_EXEC),
                5, Duration.ofMillis(50)
        );
        System.out.println("retry result = " + result.get());

        // Demo Pattern 4 — hedged
        var hedge = hedged(
                () -> CompletableFuture.supplyAsync(() -> { sleep(300); return "primary"; }, IO_EXEC),
                () -> CompletableFuture.supplyAsync(() -> { sleep(100); return "backup"; }, IO_EXEC),
                Duration.ofMillis(50)   // fire backup sau 50ms nếu primary chưa xong
        );
        System.out.println("hedged = " + hedge.get());

        ((java.util.concurrent.ExecutorService) IO_EXEC).shutdown();
    }

    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
