package module_11_concurrency_advanced._12_completable_future;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CompletableFutureBasics {

    public static void main(String[] args) throws Exception {
        // 1. Tạo CompletableFuture đã hoàn tất
        CompletableFuture<String> done = CompletableFuture.completedFuture("ready");
        System.out.println(done.get());

        // 2. supplyAsync — chạy task async, trả Future
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return 42;
        });
        System.out.println("f1 = " + f1.get());

        // 3. Chain transformation
        CompletableFuture<String> f2 = f1
                .thenApply(n -> n * 2)              // sync trên callback thread
                .thenApply(n -> "value=" + n);
        System.out.println(f2.get());

        // 4. thenCompose — flat map (Future<Future<X>> -> Future<X>)
        CompletableFuture<String> chained = CompletableFuture
                .supplyAsync(() -> 1)
                .thenCompose(n -> CompletableFuture.supplyAsync(() -> "val=" + n));
        System.out.println(chained.get());

        // 5. thenCombine — kết hợp 2 future
        CompletableFuture<Integer> a = CompletableFuture.supplyAsync(() -> { sleep(50); return 10; });
        CompletableFuture<Integer> b = CompletableFuture.supplyAsync(() -> { sleep(80); return 20; });
        CompletableFuture<Integer> sum = a.thenCombine(b, Integer::sum);
        System.out.println("sum = " + sum.get());

        // 6. allOf / anyOf
        var allFutures = List.of(
                CompletableFuture.supplyAsync(() -> { sleep(30); return "A"; }),
                CompletableFuture.supplyAsync(() -> { sleep(50); return "B"; }),
                CompletableFuture.supplyAsync(() -> { sleep(70); return "C"; })
        );
        CompletableFuture<Void> all = CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0]));
        all.join();
        var results = allFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        System.out.println("allOf results = " + results);

        CompletableFuture<Object> any = CompletableFuture.anyOf(allFutures.toArray(new CompletableFuture[0]));
        System.out.println("anyOf first = " + any.get());

        // 7. Error handling
        CompletableFuture<String> err = CompletableFuture.supplyAsync(() -> {
            if (true) throw new RuntimeException("boom");
            return "ok";
        });
        String safe = err.handle((r, ex) -> ex != null ? "fallback: " + ex.getMessage() : r).get();
        System.out.println(safe);

        // exceptionally — chỉ chạy khi fail
        String safe2 = err.exceptionally(ex -> "from exceptionally: " + ex.getMessage()).get();
        System.out.println(safe2);

        // 8. Timeout (J9+)
        CompletableFuture<String> slow = CompletableFuture.supplyAsync(() -> {
            sleep(2000);
            return "late";
        });
        try {
            slow.orTimeout(200, TimeUnit.MILLISECONDS).get();
        } catch (ExecutionException e) {
            System.out.println("timed out: " + e.getCause().getClass().getSimpleName()); // TimeoutException
        }

        // completeOnTimeout — fallback value thay throw
        String fallback = CompletableFuture.supplyAsync(() -> { sleep(1000); return "real"; })
                .completeOnTimeout("fallback", 100, TimeUnit.MILLISECONDS)
                .get();
        System.out.println("with completeOnTimeout: " + fallback);
    }

    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
