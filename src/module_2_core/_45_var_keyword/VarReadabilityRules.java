package module_2_core._45_var_keyword;

import java.util.List;
import java.util.stream.Collectors;

public class VarReadabilityRules {

    record User(String name, int age) {}

    public static void main(String[] args) {
        // RULE 1: dùng var khi RHS đã làm rõ type
        var users = List.of(new User("Alice", 30), new User("Bob", 25));     // OK rõ ràng
        var byName = users.stream().collect(Collectors.toMap(User::name, u -> u));
        System.out.println(byName);

        // RULE 2: KHÔNG dùng var khi type ẩn ý
        // var data = api.fetch();              // người đọc không biết là gì
        // -> List<UserDto> data = api.fetch(); // tốt hơn nhiều

        // RULE 3: tránh var với numeric literal mơ hồ
        var x = 0;                    // int — nhưng nếu cần long thì phải nhớ
        var ratio = 3 / 4;            // 0 (int division!) — tránh dùng var ở đây
        var ratioOk = 3.0 / 4;        // 0.75 — RHS rõ ràng

        // RULE 4: khi dùng var, đặt tên biến tự giải thích type
        var customerOrderCount = computeCount();   // tốt
        var x2 = computeCount();                   // tệ - cả type lẫn intent đều ẩn

        System.out.println(x + " " + ratio + " " + ratioOk);
        System.out.println(customerOrderCount + " " + x2);

        // RULE 5: trong stream pipeline, var cho tên trung gian thường tốt
        var youngUsers = users.stream()
                .filter(u -> u.age() < 28)
                .map(User::name)
                .toList();
        System.out.println(youngUsers);
    }

    static long computeCount() { return 42L; }
}
