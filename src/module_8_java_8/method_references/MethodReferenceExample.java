package module_8_java_8.method_references;

import java.util.Arrays;
import java.util.List;

public class MethodReferenceExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Giang", "Hùng", "Thạch");

        System.out.println("\nIn dữ liệu trong mảng theo cách thông thường");
        for (String name : names) {
            System.out.println(name);
        }

        System.out.println("\nSử dụng Method Reference");
        names.forEach(System.out::println);

        System.out.println("\nMethod Reference với phương thức tự định nghĩa");
        names.forEach(MethodReferenceExample::printName);
    }

    public static void printName(String name) {
        System.out.println("Tên: " + name);
    }
}
