package module_2_core._45_var_keyword;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class VarLimitations {

    // var KHÔNG dùng được cho:
    //   - field/instance variable
    //   - method parameter
    //   - method return type
    //   - constructor parameter
    //   - catch parameter
    //   - lambda parameter (đã có type inference riêng)
    //
    // private var name = "x";       // COMPILE ERROR
    // public var calc(var x) {}     // COMPILE ERROR

    public static void main(String[] args) {
        // KHÔNG hợp lệ vì compiler không suy ra được type:
        //   var x;                    // ERROR — không có initializer
        //   var arr = {1, 2, 3};      // ERROR — array initializer cần type
        //   var nullable = null;      // ERROR — null không phải type cụ thể
        //   var lambda = () -> 1;     // ERROR — target type cần biết trước

        // Cách hợp lệ:
        var arr = new int[]{1, 2, 3};
        Function<Integer, Integer> lambda = x -> x * 2;
        System.out.println("arr[0] = " + arr[0]);
        System.out.println("lambda(5) = " + lambda.apply(5));

        // Pitfall 1: var bị "hút" supertype dễ gây bất ngờ
        var anyList = List.of(1, 2.0, "three"); // List<? extends Serializable & Comparable<?>>
        // anyList.add(4);                       // Immutable + capture wildcard -> không add được
        System.out.println(anyList);

        // Pitfall 2: diamond + var = mất generic
        var list1 = new java.util.ArrayList<>();        // ArrayList<Object>!
        var list2 = new java.util.ArrayList<String>(); // ArrayList<String> đúng

        list1.add("hello");
        list1.add(42);   // ok, vì là Object
        list2.add("world");
        // list2.add(42); // COMPILE ERROR

        System.out.println(list1);
        System.out.println(list2);

        // Pitfall 3: var với primitive — không tự promote
        var b = 100;        // int (KHÔNG phải byte dù literal vừa byte)
        // byte expectsByte = b;  // ERROR — phải cast

        // var với integer literal nhỏ KHÔNG là byte/short
        var x = 5L;         // long do suffix L
        var y = 5.0f;       // float do suffix f
        System.out.println(b + " " + x + " " + y);
    }
}
