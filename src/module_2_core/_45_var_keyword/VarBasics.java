package module_2_core._45_var_keyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class VarBasics {
    public static void main(String[] args) {
        // var (Java 10) — local variable type inference.
        // Compiler infer type từ RHS, KHÔNG có dynamic typing như JS.

        var n = 42;                     // int
        var pi = 3.14;                  // double
        var name = "Java 21";           // String
        var list = new ArrayList<Integer>();
        var map = new HashMap<String, List<Integer>>();

        System.out.println(n + " " + pi + " " + name);
        System.out.println(list.getClass().getSimpleName() + " - " + map.getClass().getSimpleName());

        // var trong for & try-with-resources
        for (var i = 0; i < 3; i++) {
            System.out.println("loop var i = " + i);
        }

        // for-each với var rất gọn
        var numbers = List.of(1, 2, 3, 4, 5);
        for (var num : numbers) {
            System.out.println(num);
        }

        // Stream + var (nhưng tránh khi type quan trọng để đọc)
        var sum = IntStream.rangeClosed(1, 10).sum();
        System.out.println("sum = " + sum);
    }
}
