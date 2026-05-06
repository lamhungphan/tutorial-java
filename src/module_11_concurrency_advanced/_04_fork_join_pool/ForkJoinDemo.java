package module_11_concurrency_advanced._04_fork_join_pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinDemo {

    // ForkJoinPool implements WORK-STEALING: thread idle "steal" task từ thread khác.
    // Tốt cho divide-and-conquer (merge sort, tree traversal, parallelStream).
    //
    // 2 base class:
    //   - RecursiveTask<V>   : trả giá trị (compute)
    //   - RecursiveAction    : void

    // 1. RecursiveTask — sum array song song
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000;
        private final long[] arr;
        private final int from, to;
        SumTask(long[] arr, int from, int to) {
            this.arr = arr; this.from = from; this.to = to;
        }
        @Override protected Long compute() {
            int len = to - from;
            if (len <= THRESHOLD) {
                long s = 0;
                for (int i = from; i < to; i++) s += arr[i];
                return s;
            }
            int mid = from + len / 2;
            SumTask left  = new SumTask(arr, from, mid);
            SumTask right = new SumTask(arr, mid, to);
            left.fork();                 // submit left vào pool, không đợi
            long rightResult = right.compute();   // tự compute right
            long leftResult  = left.join();       // đợi left xong
            return leftResult + rightResult;
        }
    }

    // 2. RecursiveAction — quick sort song song
    static class QuickSortAction extends RecursiveAction {
        private final int[] arr;
        private final int lo, hi;
        QuickSortAction(int[] arr, int lo, int hi) {
            this.arr = arr; this.lo = lo; this.hi = hi;
        }
        @Override protected void compute() {
            if (hi - lo < 1024) {                 // base case dùng sort sequential
                java.util.Arrays.sort(arr, lo, hi + 1);
                return;
            }
            int p = partition(arr, lo, hi);
            invokeAll(
                    new QuickSortAction(arr, lo, p - 1),
                    new QuickSortAction(arr, p + 1, hi)
            );
        }
        static int partition(int[] a, int lo, int hi) {
            int pivot = a[hi];
            int i = lo - 1;
            for (int j = lo; j < hi; j++) {
                if (a[j] <= pivot) {
                    i++;
                    int t = a[i]; a[i] = a[j]; a[j] = t;
                }
            }
            int t = a[i + 1]; a[i + 1] = a[hi]; a[hi] = t;
            return i + 1;
        }
    }

    public static void main(String[] args) {
        // Common pool — JVM-wide, default cho parallelStream + CompletableFuture.async
        System.out.println("commonPool parallelism = " + ForkJoinPool.commonPool().getParallelism());

        // 1. Sum
        long[] data = new long[1_000_000];
        for (int i = 0; i < data.length; i++) data[i] = i;
        long sum = ForkJoinPool.commonPool().invoke(new SumTask(data, 0, data.length));
        System.out.println("sum = " + sum);

        // 2. Sort
        int[] arr = new java.util.Random(42).ints(500_000, 0, 1_000_000).toArray();
        long t0 = System.nanoTime();
        new ForkJoinPool().invoke(new QuickSortAction(arr, 0, arr.length - 1));
        long ms = (System.nanoTime() - t0) / 1_000_000;
        System.out.println("sort took " + ms + " ms; sorted=" + isSorted(arr));

        // ParallelStream cũng dùng ForkJoinPool.commonPool
        // -> KHÔNG dùng cho I/O blocking (sẽ chiếm worker thread của cả app)
        long parSum = java.util.stream.LongStream.rangeClosed(1, 100_000_000)
                .parallel()
                .sum();
        System.out.println("parallel stream sum = " + parSum);
    }

    static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i] < a[i - 1]) return false;
        return true;
    }
}
