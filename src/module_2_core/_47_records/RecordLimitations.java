package module_2_core._47_records;

import java.util.List;

public class RecordLimitations {

    // GIỚI HẠN của records — quan trọng để biết khi nào KHÔNG dùng.

    // 1. Record KHÔNG thể extends class khác — implicitly extends java.lang.Record.
    // public record A() extends ArrayList { }   // COMPILE ERROR

    // 2. Record IMPLICITLY final — không thể subclass.
    // public final class Wrapper extends Point {} // COMPILE ERROR (Point là record)

    // 3. Record KHÔNG có instance field ngoài component:
    //    public record User(String name) { private int age; } // ERROR
    // Static field thì OK:
    public record User(String name) {
        public static final int MAX_LEN = 50;
        public static User anonymous() { return new User("anonymous"); }
    }

    // 4. Component field LÀ final, không có setter.
    //    Component KIỂU REFERENCE vẫn có thể bị mutate qua method bên trong:
    public record Team(String name, List<String> members) {}

    public static void main(String[] args) {
        var team = new Team("A", new java.util.ArrayList<>(List.of("Alice")));
        System.out.println(team);
        team.members().add("Bob");                     // OK — mutate được!
        System.out.println(team);

        // -> Để thực sự immutable phải defensive copy trong canonical constructor
        var safe = new SafeTeam("A", List.of("Alice"));
        System.out.println(safe);
        try {
            safe.members().add("Bob");                 // throws — view immutable
        } catch (UnsupportedOperationException e) {
            System.out.println("can't mutate: " + e.getClass().getSimpleName());
        }
    }

    // Defensive copy trong canonical constructor + override accessor
    public record SafeTeam(String name, List<String> members) {
        public SafeTeam {
            members = List.copyOf(members);            // defensive copy + immutable view
        }
    }

    // KHI NÀO KHÔNG DÙNG RECORD?
    //  - Cần mutable state.
    //  - Cần extends class khác (chỉ có thể implements interface).
    //  - JPA Entity (Hibernate yêu cầu no-arg constructor + setter; có solution riêng nhưng phức tạp).
    //  - Cần nhiều logic phức tạp / nhiều invariant ngoài primary fields.
    //  - Cần custom equality KHÔNG dựa trên tất cả component (record auto-gen dựa hết).
}
