package module_9_multithreading.sort;

import java.util.Random;

public class MergeSort {

    public static void main(String[] args) {
        int[] arr = new int[100000000];
        Random random = new Random();

        // Khởi tạo mảng ngẫu nhiên
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(1000000000); // Giá trị ngẫu nhiên
        }

        // Sắp xếp mảng bằng Merge Sort
        mergeSort(arr, 0, arr.length - 1);

        // In mảng đã được sắp xếp
        System.out.println("Mảng đã được sắp xếp:");
        int i = 0;
        for (int num : arr) {
            i++;
            if (i == 10) {
                System.out.println("");
                i = 0;
            }
            System.out.print(num + " ");
        }
    }

    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            // Tìm chỉ số giữa
            int middle = left + (right - left) / 2;

            // Gọi đệ quy sắp xếp hai nửa
            mergeSort(arr, left, middle);
            mergeSort(arr, middle + 1, right);

            // Hợp nhất hai nửa đã sắp xếp
            merge(arr, left, middle, right);
        }
    }

    public static void merge(int[] arr, int left, int middle, int right) {
        // Kích thước của hai mảng tạm
        int n1 = middle - left + 1;
        int n2 = right - middle;

        // Tạo các mảng tạm
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Sao chép dữ liệu vào các mảng tạm
        for (int i = 0; i < n1; i++) {
            L[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = arr[middle + 1 + j];
        }

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
