package module_11_concurrency_advanced._03_executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorDemo {

    // ThreadPoolExecutor có 7 tham số config quan trọng:
    //   1. corePoolSize       — số thread "thường trực"
    //   2. maximumPoolSize    — số thread tối đa
    //   3. keepAliveTime      — thread vượt core sống bao lâu khi idle
    //   4. unit               — TimeUnit của keepAliveTime
    //   5. workQueue          — hàng đợi task
    //   6. threadFactory      — tạo thread (đặt name, daemon, ...)
    //   7. RejectedExecutionHandler — xử lý khi không nhận thêm

    public static void main(String[] args) throws Exception {
        // Cấu hình production-grade
        ThreadFactory tf = new NamedThreadFactory("worker");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                4,                                 // core
                8,                                 // max
                60, TimeUnit.SECONDS,              // keep-alive
                new ArrayBlockingQueue<>(100),     // BOUNDED queue
                tf,
                new ThreadPoolExecutor.CallerRunsPolicy()  // backpressure: caller chạy task nếu queue full
        );

        for (int i = 0; i < 20; i++) {
            int id = i;
            try {
                pool.submit(() -> {
                    Thread.sleep(200);
                    System.out.printf("[%s] task %d done%n",
                            Thread.currentThread().getName(), id);
                    return null;
                });
            } catch (RejectedExecutionException e) {
                System.out.println("rejected " + id);
            }
        }

        pool.shutdown();
        if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
            pool.shutdownNow();      // force interrupt all running tasks
        }

        // 4 chính sách rejection mặc định:
        //   - AbortPolicy (default)   — throw RejectedExecutionException
        //   - CallerRunsPolicy        — caller chạy task -> auto backpressure
        //   - DiscardPolicy           — silently drop
        //   - DiscardOldestPolicy     — drop oldest in queue
        //
        // Trong production thường:
        //   - CallerRunsPolicy + bounded queue = back-pressure tự nhiên
        //   - hoặc custom handler ghi log + alert + drop có lựa chọn

        // ScheduledExecutor cho task định kỳ
        ScheduledExecutorService sched = Executors.newScheduledThreadPool(2);
        sched.scheduleAtFixedRate(
                () -> System.out.println("tick " + System.currentTimeMillis()),
                0, 1, TimeUnit.SECONDS);
        Thread.sleep(3500);
        sched.shutdown();
    }

    static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger seq = new AtomicInteger(1);
        private final String prefix;
        NamedThreadFactory(String prefix) { this.prefix = prefix; }
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, prefix + "-" + seq.getAndIncrement());
            t.setDaemon(false);
            t.setUncaughtExceptionHandler((th, ex) ->
                    System.err.println("[uncaught] " + th.getName() + ": " + ex));
            return t;
        }
    }
}
