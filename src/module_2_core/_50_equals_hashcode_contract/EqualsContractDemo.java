package module_2_core._50_equals_hashcode_contract;

import java.util.Objects;

public class EqualsContractDemo {

    // 5 thuộc tính của equals (JLS / Object Javadoc):
    //  1. Reflexive: x.equals(x) == true
    //  2. Symmetric: x.equals(y) == y.equals(x)
    //  3. Transitive: x.equals(y) && y.equals(z) -> x.equals(z)
    //  4. Consistent: gọi nhiều lần cho cùng input phải trả cùng kết quả
    //  5. Non-null: x.equals(null) == false

    // hashCode contract:
    //  1. Consistent
    //  2. equals -> hashCode bằng nhau
    //  3. !equals -> hashCode CÓ THỂ bằng (collision OK, nhưng càng phân tán càng tốt)

    static class CaseInsensitiveString {
        private final String s;
        CaseInsensitiveString(String s) { this.s = Objects.requireNonNull(s); }

        @Override
        public boolean equals(Object o) {
            // SAI: cho phép so với String thường -> phá đối xứng
            // if (o instanceof String t) return s.equalsIgnoreCase(t);

            return o instanceof CaseInsensitiveString c
                    && s.equalsIgnoreCase(c.s);
        }

        @Override
        public int hashCode() {
            return s.toLowerCase().hashCode();   // PHẢI dùng cùng dạng normalize!
        }
    }

    // Demo phá vỡ symmetry nếu equals so cả String:
    static class CaseInsensitiveStringBroken {
        private final String s;
        CaseInsensitiveStringBroken(String s) { this.s = s; }
        @Override
        public boolean equals(Object o) {
            if (o instanceof CaseInsensitiveStringBroken c) return s.equalsIgnoreCase(c.s);
            if (o instanceof String t) return s.equalsIgnoreCase(t);    // gây đối xứng!
            return false;
        }
        @Override
        public int hashCode() { return s.toLowerCase().hashCode(); }
    }

    public static void main(String[] args) {
        var a = new CaseInsensitiveString("Hello");
        var b = new CaseInsensitiveString("HELLO");
        System.out.println("a.equals(b) = " + a.equals(b));          // true
        System.out.println("hash a = " + a.hashCode() + ", b = " + b.hashCode()); // bằng

        // Phá đối xứng:
        var x = new CaseInsensitiveStringBroken("Java");
        String y = "JAVA";
        System.out.println("x.equals(y) = " + x.equals(y));   // true
        System.out.println("y.equals(x) = " + y.equals(x));   // false! -> ASYMMETRIC
        // Hệ quả: HashSet/HashMap cư xử bất thường.

        // Reflexive check
        System.out.println("a.equals(a) = " + a.equals(a));

        // Non-null check
        System.out.println("a.equals(null) = " + a.equals(null));

        // PHẢI luôn check `o == null` và `o instanceof X` (instanceof tự kiểm null).
    }
}
