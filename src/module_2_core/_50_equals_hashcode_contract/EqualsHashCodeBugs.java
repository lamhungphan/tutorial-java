package module_2_core._50_equals_hashcode_contract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EqualsHashCodeBugs {

    // BUG 1: Override equals nhưng QUÊN hashCode -> hỏng HashMap/HashSet
    static class BadPoint {
        int x, y;
        BadPoint(int x, int y) { this.x = x; this.y = y; }
        @Override public boolean equals(Object o) {
            if (!(o instanceof BadPoint p)) return false;
            return x == p.x && y == p.y;
        }
        // KHÔNG override hashCode -> dùng Object.hashCode (identity)
    }

    // BUG 2: Override hashCode bằng hằng số -> mọi key vào 1 bucket -> O(N)
    static class SlowPoint {
        int x, y;
        SlowPoint(int x, int y) { this.x = x; this.y = y; }
        @Override public boolean equals(Object o) {
            return o instanceof SlowPoint p && x == p.x && y == p.y;
        }
        @Override public int hashCode() { return 1; }   // hợp lệ nhưng tệ
    }

    // BUG 3: equals dùng getClass thay instanceof -> phá Liskov với subclass.
    static class StrictPoint {
        int x, y;
        StrictPoint(int x, int y) { this.x = x; this.y = y; }
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            StrictPoint p = (StrictPoint) o;
            return x == p.x && y == p.y;
        }
        @Override public int hashCode() { return Objects.hash(x, y); }
    }
    static class ColorPoint extends StrictPoint {
        String color;
        ColorPoint(int x, int y, String color) { super(x, y); this.color = color; }
    }

    // CORRECT — dùng instanceof + Objects.hash. Hoặc tốt hơn — dùng record!
    static class GoodPoint {
        final int x, y;
        GoodPoint(int x, int y) { this.x = x; this.y = y; }
        @Override public boolean equals(Object o) {
            return o instanceof GoodPoint p && x == p.x && y == p.y;
        }
        @Override public int hashCode() { return Objects.hash(x, y); }
        @Override public String toString() { return "(" + x + "," + y + ")"; }
    }

    // BEST — record auto-gen equals/hashCode/toString đúng contract
    record RecPoint(int x, int y) {}

    public static void main(String[] args) {
        // BUG 1: equals đúng nhưng HashSet không nhận ra
        Set<BadPoint> badSet = new HashSet<>();
        badSet.add(new BadPoint(1, 1));
        System.out.println("BadPoint contains?  " + badSet.contains(new BadPoint(1, 1)));
        // -> false! (hash khác nhau, không vào cùng bucket)

        // BUG 2: vẫn đúng nhưng chậm — mọi key cùng bucket -> linked list scan
        Set<SlowPoint> slow = new HashSet<>();
        for (int i = 0; i < 1000; i++) slow.add(new SlowPoint(i, 0));
        long t0 = System.nanoTime();
        slow.contains(new SlowPoint(999, 0));
        System.out.println("SlowPoint contains took (ns): " + (System.nanoTime() - t0));

        // BUG 3: getClass phá tính đối xứng nếu mix với subclass:
        StrictPoint sp = new StrictPoint(1, 2);
        ColorPoint cp = new ColorPoint(1, 2, "red");
        System.out.println("sp.equals(cp) = " + sp.equals(cp));   // false
        System.out.println("cp.equals(sp) = " + cp.equals(sp));   // false (cùng false ở đây — nhưng nếu CP override equals theo instanceof -> đối xứng phá vỡ)

        // GOOD
        Set<GoodPoint> good = new HashSet<>();
        good.add(new GoodPoint(1, 1));
        System.out.println("GoodPoint contains? " + good.contains(new GoodPoint(1, 1)));   // true

        // RECORD — không cần viết gì
        var ages = new HashMap<RecPoint, String>();
        ages.put(new RecPoint(1, 2), "alpha");
        System.out.println("Record key lookup: " + ages.get(new RecPoint(1, 2)));   // alpha
    }
}
