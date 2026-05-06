package module_9_multithreading.sort;

import java.util.Arrays;
import java.util.Random;

public class MultithreadedQuickSort {

    public static void main(String[] args) {
        int[] arr = new int[100000000]; // Mảng chứa 10 triệu phần tử
        Random random = new Random();

        // Khởi tạo mảng ngẫu nhiên
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(1000000); // Giá trị ngẫu nhiên từ 0 đến 999999
        }

        // Sắp xếp mảng bằng Quick Sort đa luồng
        quickSort(arr, 0, arr.length - 1);

        // In mảng đã được sắp xếp (có thể bỏ qua nếu không cần in ra toàn bộ)
        System.out.println("Mảng đã được sắp xếp:");
        System.out.println(Arrays.toString(arr));
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            Thread leftThread = new Thread(() -> quickSort(arr, low, pivotIndex - 1));
            Thread rightThread = new Thread(() -> quickSort(arr, pivotIndex + 1, high));
            leftThread.start();
            rightThread.start();
            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                // Đổi chỗ arr[i] và arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // Đổi chỗ arr[i + 1] và arr[high] (hoặc pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
