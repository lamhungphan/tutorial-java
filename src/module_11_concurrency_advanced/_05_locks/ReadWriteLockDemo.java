package module_11_concurrency_advanced._05_locks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class ReadWriteLockDemo {

    // ReadWriteLock = nhiều READER song song HOẶC 1 WRITER độc quyền.
    // Tốt khi đọc >> ghi.

    static class CachedConfig {
        private final ReadWriteLock rw = new ReentrantReadWriteLock();
        private final Map<String, String> data = new HashMap<>();

        public String get(String key) {
            rw.readLock().lock();
            try {
                return data.get(key);     // nhiều thread đọc song song
            } finally {
                rw.readLock().unlock();
            }
        }

        public void put(String key, String value) {
            rw.writeLock().lock();        // độc quyền — chặn tất cả reader
            try {
                data.put(key, value);
            } finally {
                rw.writeLock().unlock();
            }
        }
    }

    // StampedLock (J8) — không reentrant nhưng nhanh hơn:
    //   3 mode:
    //     - writeLock     (exclusive)
    //     - readLock      (shared)
    //     - tryOptimisticRead -> ưu việt: KHÔNG lock, chỉ trả stamp; sau đọc check validate(stamp)
    //
    // Optimistic read tránh contention CỰC TỐT khi ghi hiếm.

    static class Point {
        private final StampedLock sl = new StampedLock();
        private double x, y;

        public void move(double dx, double dy) {
            long stamp = sl.writeLock();
            try { x += dx; y += dy; } finally { sl.unlockWrite(stamp); }
        }

        public double distance() {
            // 1. Try optimistic — không block writer
            long stamp = sl.tryOptimisticRead();
            double cx = x, cy = y;
            if (!sl.validate(stamp)) {
                // 2. Fallback — có concurrent write -> đọc lại bằng read lock
                stamp = sl.readLock();
                try { cx = x; cy = y; } finally { sl.unlockRead(stamp); }
            }
            return Math.hypot(cx, cy);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CachedConfig cfg = new CachedConfig();
        cfg.put("env", "prod");

        Thread[] readers = new Thread[5];
        for (int i = 0; i < readers.length; i++) {
            int idx = i;
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 3; j++) System.out.println("R" + idx + "=" + cfg.get("env"));
            });
            readers[i].start();
        }
        for (Thread t : readers) t.join();

        Point p = new Point();
        Thread w = new Thread(() -> { for (int i = 0; i < 10; i++) p.move(1, 1); });
        Thread r = new Thread(() -> System.out.println("distance ≈ " + p.distance()));
        w.start(); r.start();
        w.join(); r.join();
    }
}
