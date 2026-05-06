package module_9_multithreading.thread_operations;

import java.util.Scanner;

public class ThreadOparationsDemo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int threadQuantity = 5;
        ControlThread[] threads = new ControlThread[threadQuantity];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ControlThread("Luong dieu khien " + (i + 1));
        }

        boolean invalidThreadNum = true;
        int numbChoose = 0;
        try {
            while (invalidThreadNum) {

                System.out.println("Moi ban chon luong muon thao tac");
                numbChoose = Integer.parseInt(scanner.nextLine());

                if (numbChoose < 1 || numbChoose >= threads.length) {
                    System.out.println("Vui long nhap dung thong tin luong");
                    continue;
                } else {
                    invalidThreadNum = false;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Nhap khong dung dinh dang so");
        }

        ControlThread threadChoosed = threads[numbChoose - 1];

        boolean flag = true;
        while (flag) {
            System.out.println("""
                               Nhap lenh dieu khien 
                               (
                               0.Thoat
                               1.Bat dau
                               2.Dung
                               3.Tiep tuc
                               4.Ngat luong
                               )
                               -Vui long nhap dung thu tu cau lenh!""");
            int inputCommand = scanner.nextInt();

            if (inputCommand < 1 || inputCommand > 4) {
                System.out.println("""
                                   Vui long chon cau lenh
                                   0.Thoat
                                   1.Bat dau
                                   2.Dung
                                   3.Tiep tuc
                                   4.Ngat luong""");
                continue;
            }

            switch (inputCommand) {
                case 1 -> {
                    System.out.println("Bat dau " + threadChoosed.getName() + " dang bat dau chay");
                    threadChoosed.start();
                }
                case 2 -> {
                    System.out.println(threadChoosed.getName() + " dang dung.");
                    threadChoosed.pause();
                }
                case 3 -> {
                    System.out.println(threadChoosed.getName() + " tiep tuc.");
                    threadChoosed.resumse();
                }
                case 4 ->
                    System.out.println(threadChoosed.getName() + " ket thuc.");
                case 0 ->
                    flag = false;
                default ->
                    System.out.println("Vui long chon cau lenh dung: 0, 1, 2, 3, hoac 4.");
            }
        }
    }
}
