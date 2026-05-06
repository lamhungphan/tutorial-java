package module_9_multithreading.synchronization;

public class SumThreadImpl implements Runnable {

    private SumArray sumArray;
    private Thread thread;
    static private int array[];
    private int result;

    public SumThreadImpl(String name, int[] array) {
        this.thread = new Thread(this, name);
        this.array = array;
        this.thread.start();
        this.sumArray = new SumArray();
    }

    @Override
    public void run() {
        System.out.println(thread.getName() + " bat dau chay.");

        result = sumArray.sumArray(array);
    //Cách khác để đồng bộ hóa đối tượng sumArray
    //        synchronized (sumArray) {
    //            result = sumArray.sumArray(array);
    //        }

        System.out.println(thread.getName() + " co tong la " + result);
        System.out.println(thread.getName() + " ket thuc");
    }

    public void join() throws InterruptedException {
        this.thread.join();
    }
}
