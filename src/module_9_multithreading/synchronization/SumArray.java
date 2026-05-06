package module_9_multithreading.synchronization;

public class SumArray {

    private int sum;

    synchronized int sumArray(int numbers[]) {
        sum = 0;

        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
            System.out.println("Tong hien tai cua " + Thread.currentThread().getName() + " la: " + sum);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Luong da bi ngat");
        }
        return sum;
    }

}
