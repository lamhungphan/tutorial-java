package module_11_concurrency_advanced._06_atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;

public class AtomicDemo {

    // Atomic* dùng CAS (Compare-And-Swap) — instruction CPU đảm bảo atomic.
    // KHÔNG cần lock -> non-blocking, latency thấp.

    public static void main(String[] args) throws Exception {
        atomicIntegerExample();
        atomicReferenceCAS();
        absABAProblem();
        longAdderVsAtomicLong();
    }

    // 1. AtomicInteger — counter thread-safe lock-free
    static void atomicIntegerExample() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger();
        Thread[] ts = new Thread[10];
        for (int i = 0; i < 10; i++) {
            ts[i] = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) counter.incrementAndGet();
            });
            ts[i].start();
        }
        for (Thread t : ts) t.join();
        System.out.println("counter = " + counter.get()); // 1_000_000 chính xác

        // updateAndGet — apply function atomic
        AtomicInteger max = new AtomicInteger();
        max.updateAndGet(v -> Math.max(v, 42));
        max.accumulateAndGet(50, Math::max);
        System.out.println("max = " + max);
    }

    // 2. AtomicReference + CAS loop — implement lock-free stack
    static void atomicReferenceCAS() {
        var stack = new LockFreeStack<Integer>();
        stack.push(1); stack.push(2); stack.push(3);
        System.out.println("pop: " + stack.pop());
        System.out.println("pop: " + stack.pop());
    }

    static class LockFreeStack<T> {
        private static class Node<T> { T value; Node<T> next;
            Node(T v, Node<T> n) { value = v; next = n; }
        }
        private final AtomicReference<Node<T>> head = new AtomicReference<>();

        public void push(T v) {
            Node<T> oldHead, newHead;
            do {
                oldHead = head.get();
                newHead = new Node<>(v, oldHead);
            } while (!head.compareAndSet(oldHead, newHead));   // CAS retry loop
        }

        public T pop() {
            Node<T> oldHead, newHead;
            do {
                oldHead = head.get();
                if (oldHead == null) return null;
                newHead = oldHead.next;
            } while (!head.compareAndSet(oldHead, newHead));
            return oldHead.value;
        }
    }

    // 3. ABA problem — A -> B -> A: CAS thấy "vẫn là A" nhưng thực ra bị thay đổi.
    //    Giải pháp: AtomicStampedReference (gắn stamp/version)
    static void absABAProblem() {
        var ref = new AtomicStampedReference<>("A", 0);
        int[] stampHolder = new int[1];
        String current = ref.get(stampHolder);
        int stamp = stampHolder[0];

        // Giả lập thread khác: A -> B -> A (stamp tăng 2 lần)
        ref.compareAndSet("A", "B", 0, 1);
        ref.compareAndSet("B", "A", 1, 2);

        // Bây giờ value lại là "A" — CAS không stamped sẽ tưởng không có gì xảy ra
        boolean success = ref.compareAndSet(current, "C", stamp, stamp + 1);
        System.out.println("CAS with stamp success? " + success); // false — phát hiện ABA!
    }

    // 4. LongAdder vs AtomicLong dưới contention cao
    static void longAdderVsAtomicLong() throws Exception {
        java.util.concurrent.atomic.AtomicLong al = new java.util.concurrent.atomic.AtomicLong();
        LongAdder la = new LongAdder();

        long ms1 = bench("AtomicLong", () -> al.incrementAndGet());
        long ms2 = bench("LongAdder ", () -> la.increment());
        System.out.println("LongAdder " + (ms1 * 1.0 / ms2) + "x faster on contention");
        System.out.println("sum = " + la.sum());
    }

    static long bench(String name, Runnable op) throws Exception {
        int threads = 8;
        int iters   = 1_000_000;
        Thread[] ts = new Thread[threads];
        long t0 = System.nanoTime();
        for (int i = 0; i < threads; i++) {
            ts[i] = new Thread(() -> { for (int j = 0; j < iters; j++) op.run(); });
            ts[i].start();
        }
        for (Thread t : ts) t.join();
        long ms = (System.nanoTime() - t0) / 1_000_000;
        System.out.println(name + " took " + ms + " ms");
        return ms;
    }
}
