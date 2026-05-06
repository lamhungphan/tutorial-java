package module_11_concurrency_advanced._10_virtual_threads;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class VirtualThreadsDemo {

    // Java 21 (JEP 444): Virtual Threads
    //
    // - "User-mode thread" — JVM tự manage, KHÔNG phải OS thread
    // - Tạo CỰC RẺ — có thể tạo hàng triệu virtual thread cùng lúc
    // - Khi block (I/O, sleep, lock) -> JVM PARK virtual thread, free carrier (platform thread)
    // - Tương thích 100% Thread API — code sync không cần đổi
    //
    // -> Cách mạng cho I/O-bound app: không cần CompletableFuture / reactive nữa
    //    code sync đơn giản, scale như async.

    public static void main(String[] args) throws Exception {
        // 1. Tạo thẳng — Thread.ofVirtual()
        Thread vt = Thread.ofVirtual().name("vt-1").start(() ->
                System.out.println("hello from " + Thread.currentThread()));
        vt.join();

        // 2. Executor virtual-per-task — mỗi task 1 virtual thread mới
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            // 100k task song song — không sao
            List<java.util.concurrent.Future<Integer>> futures = IntStream.range(0, 100_000)
                    .mapToObj(i -> exec.submit(() -> {
                        Thread.sleep(Duration.ofMillis(100));   // simulate I/O
                        return i;
                    }))
                    .toList();

            long t0 = System.nanoTime();
            int sum = 0;
            for (var f : futures) sum += f.get();
            long ms = (System.nanoTime() - t0) / 1_000_000;
            System.out.println("100k tasks (sleep 100ms each): " + ms + " ms, sum=" + sum);
            // Với platform thread pool 200, sẽ tốn 50s; virtual thread chỉ ~100-300ms.
        }

        // 3. Inspect: virtual thread không tăng số OS thread
        Thread platform = Thread.ofPlatform().name("plat").start(() -> {});
        Thread virtual  = Thread.ofVirtual().name("virt").start(() -> {});
        platform.join(); virtual.join();
        System.out.println("isVirtual platform: " + platform.isVirtual());
        System.out.println("isVirtual virtual:  " + virtual.isVirtual());
    }
}
