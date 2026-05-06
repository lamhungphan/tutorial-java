package module_8_java_8.method_references;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreatePersonList {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("An", "Bình", "Cường", "Duy");

        // Tạo danh sách đối tượng Person từ danh sách tên
        List<Person> people = names.stream()
                .map(Person::new) // Method Reference tới constructor của Person
                .collect(Collectors.toList());

        // Hiển thị danh sách đối tượng Person
        System.out.println("Danh sách đối tượng Person:");
        people.forEach(System.out::println);
    }
}
