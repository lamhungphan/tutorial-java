package module_11_concurrency_advanced._03_executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorAntipatterns {

    // ANTI-PATTERN 1: Executors.newFixedThreadPool(n)
    //   - dùng LinkedBlockingQueue() KHÔNG GIỚI HẠN
    //   - producer nhanh hơn consumer -> queue tăng vô hạn -> OOM Heap
    //
    // FIX: tạo ThreadPoolExecutor TỰ TAY với BOUNDED queue + RejectedExecutionHandler.

    // ANTI-PATTERN 2: Executors.newCachedThreadPool()
    //   - dùng SynchronousQueue (no buffer) + maxPoolSize = Integer.MAX_VALUE
    //   - mỗi request không có thread idle sẽ TẠO MỚI
    //   - bão traffic -> tạo hàng nghìn thread -> OOM "unable to create native thread" / OS hết FD
    //
    // FIX: bound max thread + bounded queue.

    // ANTI-PATTERN 3: dùng SHARED commonPool cho tác vụ I/O / blocking
    //   - ParallelStream và CompletableFuture.async (default) đều dùng ForkJoinPool.commonPool
    //   - 1 task block dài -> chiếm worker, kéo cả app chậm
    //
    // FIX: pass executor riêng vào CompletableFuture.supplyAsync(..., myExecutor)

    public static void main(String[] args) throws Exception {
        demoFixedPoolUnboundedQueue();
        demoSafePool();
    }

    static void demoFixedPoolUnboundedQueue() {
        ExecutorService bad = Executors.newFixedThreadPool(2);   // queue UNBOUNDED!
        // Producer nhanh hơn consumer -> queue đầy heap.
        // Demo nhanh -> không submit thật, chỉ in cảnh báo:
        System.out.println("[anti] newFixedThreadPool: " + bad);
        bad.shutdown();
    }

    static void demoSafePool() throws Exception {
        ThreadPoolExecutor safe = new ThreadPoolExecutor(
                2, 4,
                30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(50),     // BOUNDED!
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        for (int i = 0; i < 10; i++) {
            int id = i;
            safe.submit(() -> System.out.println("safe task " + id));
        }
        safe.shutdown();
        safe.awaitTermination(5, TimeUnit.SECONDS);

        // Quy tắc thumb sizing pool:
        //   CPU-bound: corePool ≈ Runtime.availableProcessors()
        //   I/O-bound: corePool ≈ N * (1 + waitTime/computeTime)
        //                                 (Little's law)
    }
}
