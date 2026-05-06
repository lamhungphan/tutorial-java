package module_8_java_8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LambdaExample {
    public static void main(String[] args) {
//        1
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        nums.forEach(num -> {
            if (num % 2 == 0) {
                System.out.println(num);
            }
        });

//        2
        System.out.println("Nhập họ và tên (nhấn Enter để kết thúc): ");
        Scanner scanner = new Scanner(System.in);
        List<String> list = new ArrayList<>();
        while (true) {
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                break;
            }
            list.add(input);
        }

        list.sort((name1, name2) -> Integer.compare(name1.length(), name2.length()));

        System.out.println("Danh sách sau khi sắp xếp theo độ dài:");
        list.forEach(System.out::println);
    }
}
