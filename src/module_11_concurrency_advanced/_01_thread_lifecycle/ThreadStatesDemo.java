package module_11_concurrency_advanced._01_thread_lifecycle;

import java.util.concurrent.locks.LockSupport;

public class ThreadStatesDemo {

    private static final Object lock = new Object();

    public static void main(String[] args) throws Exception {
        // 1. NEW — đã tạo nhưng chưa start
        Thread t1 = new Thread(() -> {});
        System.out.println("t1 = " + t1.getState());        // NEW

        // 2. RUNNABLE — đang chạy hoặc đợi CPU schedule
        Thread t2 = new Thread(() -> {
            long sum = 0;
            for (int i = 0; i < 100_000_000L; i++) sum += i;
        });
        t2.start();
        Thread.sleep(5);
        System.out.println("t2 (CPU bound) = " + t2.getState());   // RUNNABLE
        t2.join();

        // 3. BLOCKED — đợi monitor lock đã bị thread khác giữ
        Thread holder = new Thread(() -> {
            synchronized (lock) {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        });
        Thread waiter = new Thread(() -> {
            synchronized (lock) { /* đợi vào */ }
        });
        holder.start();
        Thread.sleep(50);
        waiter.start();
        Thread.sleep(50);
        System.out.println("waiter = " + waiter.getState());        // BLOCKED
        holder.join(); waiter.join();

        // 4. WAITING — Object.wait() / LockSupport.park() / Thread.join()
        Thread parked = new Thread(LockSupport::park);
        parked.start();
        Thread.sleep(50);
        System.out.println("parked = " + parked.getState());        // WAITING
        LockSupport.unpark(parked);
        parked.join();

        // 5. TIMED_WAITING — sleep, wait(ms), parkNanos, join(ms)
        Thread sleeper = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        });
        sleeper.start();
        Thread.sleep(50);
        System.out.println("sleeper = " + sleeper.getState());      // TIMED_WAITING
        sleeper.join();

        // 6. TERMINATED
        System.out.println("sleeper after join = " + sleeper.getState()); // TERMINATED
    }
}
