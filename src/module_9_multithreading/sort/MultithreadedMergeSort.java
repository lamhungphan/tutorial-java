package module_9_multithreading.sort;

import java.util.Random;

public class MultithreadedMergeSort {

    private static final int ARRAY_SIZE = 100000000; // 1 tỷ phần tử

    public static void main(String[] args) {
        long[] arr = new long[ARRAY_SIZE];
        Random random = new Random();

        // Khởi tạo mảng ngẫu nhiên
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextLong();
        }

        // Sắp xếp mảng bằng Merge Sort đa luồng
        long startTime = System.currentTimeMillis();
        mergeSort(arr, 0, arr.length - 1);
        long endTime = System.currentTimeMillis();

        // In mảng đã được sắp xếp
        System.out.println("Mảng đã được sắp xếp:");
        int i = 0;
        for (long num : arr) {
            i++;
            if (i == 10) {
                System.out.println("");
                i = 0;
            }
            System.out.print(num + " ");
        }

        // Thời gian hoàn thành
        System.out.println("Mảng đã được sắp xếp trong " + (endTime - startTime) + " ms");
    }

    public static void mergeSort(long[] arr, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;

            // Tạo các luồng
            Thread leftThread = new Thread(() -> mergeSort(arr, left, middle));
            Thread rightThread = new Thread(() -> mergeSort(arr, middle + 1, right));

            leftThread.start();
            rightThread.start();

            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            merge(arr, left, middle, right);
        }
    }

    private static void merge(long[] arr, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        // Tạo các mảng tạm
        long[] L = new long[n1];
        long[] R = new long[n2];

        // Sao chép dữ liệu vào các mảng tạm
        System.arraycopy(arr, left, L, 0, n1);
        System.arraycopy(arr, middle + 1, R, 0, n2);

        // Hợp nhất các mảng tạm
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        // Sao chép các phần tử còn lại của L nếu có
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Sao chép các phần tử còn lại của R nếu có
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
}
