package module_11_concurrency_advanced._02_runnable_vs_callable;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CallableFutureDemo {

    // Runnable: void run(), KHÔNG trả giá trị, KHÔNG ném checked exception
    // Callable<V>: V call() throws Exception, trả giá trị + checked exception

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            // 1. Submit Runnable -> Future<?>  (kết quả null khi xong)
            Future<?> rf = pool.submit(() -> System.out.println("[runnable] running"));
            rf.get();   // block đến khi xong, trả null

            // 2. Submit Callable<V> -> Future<V>
            Callable<Integer> task = () -> {
                Thread.sleep(200);
                return 42;
            };
            Future<Integer> f = pool.submit(task);
            System.out.println("doing other work...");
            Integer result = f.get();             // block đến khi xong
            System.out.println("got: " + result);

            // 3. Future.get(timeout) — quan trọng cho production
            Future<Integer> slow = pool.submit(() -> {
                Thread.sleep(2000);
                return 99;
            });
            try {
                slow.get(500, TimeUnit.MILLISECONDS);
            } catch (TimeoutException te) {
                System.out.println("timed out -> cancel");
                slow.cancel(true);     // mayInterruptIfRunning=true -> ném InterruptedException trong task
            }

            // 4. invokeAll — submit nhiều, đợi tất cả xong (block)
            List<Callable<String>> tasks = List.of(
                    () -> { Thread.sleep(100); return "A"; },
                    () -> { Thread.sleep(150); return "B"; },
                    () -> { Thread.sleep(50);  return "C"; }
            );
            List<Future<String>> all = pool.invokeAll(tasks);
            for (Future<String> ff : all) System.out.println("invokeAll: " + ff.get());

            // 5. invokeAny — đợi BẤT KỲ task nào complete success, cancel còn lại
            String first = pool.invokeAny(tasks);
            System.out.println("invokeAny first: " + first);

            // 6. FutureTask — wrapper Runnable+Callable, có thể chạy như Runnable
            FutureTask<Long> ft = new FutureTask<>(() -> {
                long s = 0;
                for (int i = 1; i <= 1_000_000; i++) s += i;
                return s;
            });
            new Thread(ft, "manual-thread").start();
            System.out.println("FutureTask result = " + ft.get());

            // 7. Bắt exception trong Callable
            Future<Integer> err = pool.submit(() -> {
                throw new RuntimeException("oops");
            });
            try {
                err.get();
            } catch (ExecutionException e) {
                System.out.println("caught wrapped: " + e.getCause().getMessage());
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
