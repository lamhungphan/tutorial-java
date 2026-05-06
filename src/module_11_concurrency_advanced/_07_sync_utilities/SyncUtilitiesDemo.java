package module_11_concurrency_advanced._07_sync_utilities;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SyncUtilitiesDemo {

    public static void main(String[] args) throws Exception {
        countDownLatchDemo();
        cyclicBarrierDemo();
        semaphoreDemo();
        phaserDemo();
    }

    // 1. CountDownLatch — đợi N event xong (1-shot, không reset).
    //    Use case: app khởi động chờ N service init xong; test chờ thread complete.
    static void countDownLatchDemo() throws Exception {
        int N = 3;
        CountDownLatch ready = new CountDownLatch(N);
        ExecutorService pool = Executors.newFixedThreadPool(N);
        for (int i = 0; i < N; i++) {
            int id = i;
            pool.submit(() -> {
                try {
                    Thread.sleep(100 + id * 50);
                    System.out.println("service " + id + " ready");
                } catch (InterruptedException ignored) {}
                finally { ready.countDown(); }
            });
        }
        ready.await();   // block đến khi count = 0
        System.out.println("ALL services ready, app starting...");
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    // 2. CyclicBarrier — N thread đợi nhau tại 1 điểm, REUSABLE (cyclic).
    //    Use case: parallel computation theo phase (game tick, simulation step).
    static void cyclicBarrierDemo() throws Exception {
        int parties = 3;
        CyclicBarrier barrier = new CyclicBarrier(parties, () ->
                System.out.println("[barrier] all parties arrived — proceed phase\n"));
        Thread[] ts = new Thread[parties];
        for (int i = 0; i < parties; i++) {
            int id = i;
            ts[i] = new Thread(() -> {
                try {
                    for (int phase = 0; phase < 3; phase++) {
                        Thread.sleep(50 + id * 50);
                        System.out.println("worker " + id + " finished phase " + phase);
                        barrier.await();   // đợi đủ N rồi tất cả tiếp tục
                    }
                } catch (Exception e) { Thread.currentThread().interrupt(); }
            });
            ts[i].start();
        }
        for (Thread t : ts) t.join();
    }

    // 3. Semaphore — counting permit, dùng để rate-limit / connection pool.
    static void semaphoreDemo() throws Exception {
        Semaphore quota = new Semaphore(3);   // max 3 concurrent
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 8; i++) {
            int id = i;
            pool.submit(() -> {
                try {
                    quota.acquire();    // block nếu hết permit
                    System.out.println("worker " + id + " acquired (avail=" + quota.availablePermits() + ")");
                    Thread.sleep(150);
                    System.out.println("worker " + id + " releasing");
                } catch (InterruptedException ignored) {}
                finally { quota.release(); }
            });
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    // 4. Phaser — như CyclicBarrier nhưng số party có thể THAY ĐỔI runtime
    //    Use case: hierarchical computation, dynamic worker pool.
    static void phaserDemo() throws Exception {
        Phaser phaser = new Phaser(1);   // main thread = 1 party
        for (int i = 0; i < 3; i++) {
            phaser.register();           // dynamic register
            int id = i;
            new Thread(() -> {
                for (int phase = 0; phase < 2; phase++) {
                    System.out.println("phaser worker " + id + " phase " + phase);
                    phaser.arriveAndAwaitAdvance();
                }
                phaser.arriveAndDeregister();
            }).start();
        }

        for (int phase = 0; phase < 2; phase++) {
            System.out.println("[main] phase " + phase + " complete");
            phaser.arriveAndAwaitAdvance();
        }
        phaser.arriveAndDeregister();
    }
}
