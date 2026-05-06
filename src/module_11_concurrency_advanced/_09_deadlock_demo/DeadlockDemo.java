package module_11_concurrency_advanced._09_deadlock_demo;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

public class DeadlockDemo {

    private static final Object L1 = new Object();
    private static final Object L2 = new Object();

    public static void main(String[] args) throws Exception {
        // CHẠY VỚI: -ea để in cảnh báo nếu cần
        // Thread 1 lấy L1 -> đợi L2; Thread 2 lấy L2 -> đợi L1.

        Thread t1 = new Thread(() -> {
            synchronized (L1) {
                System.out.println("[t1] got L1");
                sleep(100);
                synchronized (L2) {
                    System.out.println("[t1] got L2");
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (L2) {                       // SAI THỨ TỰ — luôn lấy L1 trước!
                System.out.println("[t2] got L2");
                sleep(100);
                synchronized (L1) {
                    System.out.println("[t2] got L1");
                }
            }
        }, "t2");

        t1.start(); t2.start();

        // Cho deadlock xảy ra
        Thread.sleep(500);

        // Detect deadlock thông qua ThreadMXBean
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] dead = bean.findDeadlockedThreads();
        if (dead != null) {
            ThreadInfo[] infos = bean.getThreadInfo(dead, true, true);
            System.out.println("\n=== DEADLOCK DETECTED ===");
            for (ThreadInfo i : infos) {
                System.out.println("Thread: " + i.getThreadName());
                LockInfo lock = i.getLockInfo();
                if (lock != null) System.out.println("  waiting on: " + lock);
                System.out.println("  owned by: " + i.getLockOwnerName());
            }
        }

        // Cách "thoát" — interrupt KHÔNG hiệu lực với synchronized
        // Phải kill JVM. Đây là lý do prefer Lock.tryLock(timeout)!
        System.out.println("\n(forcing exit, real deadlock cannot be unstuck)");
        System.exit(0);
    }

    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
