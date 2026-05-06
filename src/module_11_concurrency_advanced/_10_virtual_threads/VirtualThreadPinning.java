package module_11_concurrency_advanced._10_virtual_threads;

import java.util.concurrent.Executors;

public class VirtualThreadPinning {

    // PINNING — virtual thread bị KẸT vào carrier (không free được).
    // 2 nguyên nhân chính:
    //   1. Block bên trong synchronized block (J21).
    //   2. Native (JNI) call.
    //
    // Khi pin, carrier KHÔNG phục vụ virtual thread khác -> giống như platform thread.
    //
    // Solution:
    //   - Thay synchronized -> ReentrantLock (xử lý park đúng cách)
    //   - Pin trong critical short section thì OK; pin khi I/O blocking -> bottleneck

    static final Object LOCK = new Object();
    static int counter = 0;

    public static void main(String[] args) throws Exception {
        // Bật flag để in cảnh báo khi pin (bằng -Djdk.tracePinnedThreads=full)
        // Tuy nhiên flag này được DEPRECATED ở J24+, từ J24 sẽ không còn pin với synchronized.
        // (Xem JEP 491 — Synchronize Virtual Threads without Pinning.)

        try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 5; i++) {
                int id = i;
                exec.submit(() -> {
                    synchronized (LOCK) {                  // có thể pin trên J21-J23
                        try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                        counter++;
                        System.out.println("vt " + id + " counter=" + counter
                                + " carrier=" + Thread.currentThread());
                    }
                });
            }
        }
        System.out.println("final counter = " + counter);
    }
}
