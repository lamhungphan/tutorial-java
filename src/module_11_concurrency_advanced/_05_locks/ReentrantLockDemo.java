package module_11_concurrency_advanced._05_locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    // ReentrantLock cung cấp NHIỀU TÍNH NĂNG hơn synchronized:
    //   - Fairness option
    //   - tryLock() / tryLock(timeout)
    //   - lockInterruptibly()
    //   - Multiple Condition (thay wait/notify)
    //   - Inspect: hasQueuedThreads, getHoldCount, ...

    private final ReentrantLock lock = new ReentrantLock(/*fair=*/false);
    private final Condition notFull  = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final int[] buf;
    private int head, tail, count;

    public ReentrantLockDemo(int capacity) { this.buf = new int[capacity]; }

    public void put(int x) throws InterruptedException {
        lock.lock();
        try {
            while (count == buf.length) notFull.await();   // signal-specific
            buf[tail] = x;
            tail = (tail + 1) % buf.length;
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();    // BẮT BUỘC trong finally
        }
    }

    public int take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) notEmpty.await();
            int x = buf[head];
            head = (head + 1) % buf.length;
            count--;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }

    // tryLock cho phép TIMEOUT — không bị deadlock vĩnh viễn
    public boolean tryTransfer(int x, long timeoutMs) throws InterruptedException {
        if (lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS)) {
            try {
                if (count < buf.length) {
                    buf[tail] = x;
                    tail = (tail + 1) % buf.length;
                    count++;
                    notEmpty.signal();
                    return true;
                }
                return false;
            } finally {
                lock.unlock();
            }
        }
        return false; // không lấy được lock trong timeout
    }

    public static void main(String[] args) throws Exception {
        ReentrantLockDemo q = new ReentrantLockDemo(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    q.put(i);
                    System.out.println("put " + i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int v = q.take();
                    System.out.println("    take " + v);
                    Thread.sleep(120);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        producer.start(); consumer.start();
        producer.join(); consumer.join();
    }
}
