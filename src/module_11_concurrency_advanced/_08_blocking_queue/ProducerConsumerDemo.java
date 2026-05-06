package module_11_concurrency_advanced._08_blocking_queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class ProducerConsumerDemo {

    // BlockingQueue là interface — implementation chính:
    //   - ArrayBlockingQueue       : bounded, array, FIFO
    //   - LinkedBlockingQueue      : optionally bounded, linked node, FIFO
    //   - SynchronousQueue         : 0 capacity — direct hand-off
    //   - PriorityBlockingQueue    : unbounded, priority order
    //   - DelayQueue               : element có deadline, take khi deadline đến
    //   - LinkedTransferQueue      : nhanh nhất, hỗ trợ transfer (block đến khi consumer lấy)
    //
    // 4 cặp method (xem table trong concept.md):
    //   put / take                 — block
    //   offer / poll               — non-block, return false/null
    //   offer(timeout) / poll(timeout)
    //   add / remove               — throw IllegalStateException nếu fail

    sealed interface Event permits LogEvent, ShutdownEvent {}
    record LogEvent(String msg) implements Event {}
    record ShutdownEvent() implements Event {}

    public static void main(String[] args) throws Exception {
        BlockingQueue<Event> queue = new ArrayBlockingQueue<>(10);

        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    queue.put(new LogEvent("event-" + i));   // block khi full
                    if (i % 5 == 0) Thread.sleep(20);
                }
                queue.put(new ShutdownEvent());              // poison pill
                System.out.println("[producer] done");
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "producer");

        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Event e = queue.take();                  // block khi empty
                    switch (e) {
                        case LogEvent(String msg) -> {
                            System.out.println("    [consumer] processed " + msg);
                            Thread.sleep(50);
                        }
                        case ShutdownEvent() -> {
                            System.out.println("[consumer] shutdown");
                            return;
                        }
                    }
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "consumer");

        producer.start(); consumer.start();
        producer.join(); consumer.join();

        // SynchronousQueue: 0 buffer, producer block đến khi consumer take.
        BlockingQueue<String> sync = new SynchronousQueue<>();
        Thread c2 = new Thread(() -> {
            try {
                Thread.sleep(100);
                System.out.println("got: " + sync.take());
            } catch (InterruptedException ignored) {}
        });
        c2.start();
        long t0 = System.nanoTime();
        sync.put("hello");                                 // BLOCK 100ms vì consumer ngủ
        System.out.println("put took (ms) = " + (System.nanoTime() - t0) / 1_000_000);
        c2.join();

        // poll(timeout) — quan trọng cho graceful shutdown
        BlockingQueue<String> q2 = new LinkedBlockingQueue<>();
        String x = q2.poll(50, TimeUnit.MILLISECONDS);
        System.out.println("polled: " + x);                // null
    }
}
