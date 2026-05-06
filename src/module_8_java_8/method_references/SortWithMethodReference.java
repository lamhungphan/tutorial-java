package module_8_java_8.method_references;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SortWithMethodReference {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 1, 7, 3, 2, 3, 8, 9);
        System.out.print("\nCollections: ");
        Collections.sort(numbers);
        System.out.println(numbers);

        System.out.print("\nMethod references: ");
        numbers.sort(Integer::compareTo);
        numbers.forEach(System.out::print);
    }
}
