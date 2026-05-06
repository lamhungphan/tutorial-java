package module_9_multithreading.sort;

import java.util.Random;


public class BubbleSort {
    public static void main(String[] args) {
        int[] arr = new int[10000];
        Random random = new Random();

        // Khởi tạo mảng ngẫu nhiên
        for (int i = 0; i < arr.length; i++) {
            arr[i] =(int) (10000 * Math.random());
        }

        // Sắp xếp mảng bằng Bubble Sort
        bubbleSort(arr);

        // In mảng đã được sắp xếp
        System.out.println("Mảng đã được sắp xếp:");
        int i = 0;
        for (int num : arr) {
            i++;
            if(i == 20){
                System.out.println("");
                i = 0;
            }
            System.out.print(num + " ");
        }
    }

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // Đổi chỗ hai phần tử
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }

            // Nếu không có lần đổi chỗ nào trong vòng lặp này, mảng đã được sắp xếp
            if (!swapped) {
                break;
            }
        }
    }
}
