package module_9_multithreading.synchronization;

public class SynchronizedDemo {

    public static void main(String[] args) {

        int array[] = {1, 2, 3, 4, 5};

        int threadQuantity = 3;
        SumThreadImpl[] threads = new SumThreadImpl[threadQuantity];
        
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new SumThreadImpl("Luong "+ (i+1), array);
        }
        try {
            for (SumThreadImpl thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Luong chinh da bi ngat");
        }

    }
}
