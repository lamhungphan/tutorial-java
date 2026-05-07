package module_11_concurrency_advanced._09_deadlock_demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockSolutions {

    static final ReentrantLock L1 = new ReentrantLock();
    static final ReentrantLock L2 = new ReentrantLock();

    // SOLUTION 1: lock ordering — luôn lấy theo cùng thứ tự (sort by id/hashCode).
//    static void transferOrdered(Account from, Account to, int amount) {
//        Account first  = from.id < to.id ? from : to;
//        Account second = from.id < to.id ? to   : from;
//        synchronized (first) {
//            synchronized (second) {
//                from.balance -= amount;
//                to.balance   += amount;
//            }
//        }
//    }
    record Account(int id, long balance) {     // dùng record cho id; balance mutable thì cần class
        // chỉ minh hoạ thứ tự — thực tế balance là field mutable nên dùng class:
    }

    // SOLUTION 2: tryLock với timeout — không bao giờ deadlock vĩnh viễn
    static boolean transferTry(ReentrantLock from, ReentrantLock to, long timeoutMs) throws InterruptedException {
        long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
        while (System.nanoTime() < deadline) {
            if (from.tryLock(50, TimeUnit.MILLISECONDS)) {
                try {
                    if (to.tryLock(50, TimeUnit.MILLISECONDS)) {
                        try {
                            return true;   // do work
                        } finally { to.unlock(); }
                    }
                } finally { from.unlock(); }
            }
            // Random backoff giảm livelock
            Thread.sleep((long) (Math.random() * 10));
        }
        return false;     // không lấy được trong timeout
    }

    // SOLUTION 3: open call — tránh giữ lock khi gọi method bên ngoài (không control)
    static class Service {
        private final Object stateLock = new Object();
        private int state;

        // BAD: giữ lock khi gọi callback có thể tạo cycle
        public void notifyAllBad(Runnable listener) {
            synchronized (stateLock) {
                listener.run();   // listener có thể acquire lock khác -> deadlock
            }
        }

        // GOOD: copy data ra rồi release lock trước khi gọi
        public int snapshot() {
            int copy;
            synchronized (stateLock) { copy = state; }
            return copy;
        }
    }

    public static void main(String[] args) throws Exception {
        // Demo solution 2 — tryLock không bao giờ kẹt
        boolean ok = transferTry(L1, L2, 200);
        System.out.println("transferTry got both locks? " + ok);
    }
}
