package module_2_core._47_records;

import java.util.List;
import java.util.Objects;

public class RecordBasics {

    // Records (Java 16, JEP 395) — DTO immutable, auto-generate:
    //  - private final field cho từng component
    //  - public accessor (KHÔNG có get prefix): point.x() KHÔNG phải getX()
    //  - canonical constructor
    //  - equals/hashCode/toString dựa trên TẤT CẢ component
    public record Point(int x, int y) {}

    // Compact constructor — validate input mà không phải gán field thủ công.
    // Field tự được gán SAU compact body.
    public record Range(int from, int to) {
        public Range {
            if (from > to) throw new IllegalArgumentException("from > to");
        }
    }

    // Static factory + thêm method
    public record Email(String value) {
        public Email {
            Objects.requireNonNull(value);
            if (!value.contains("@")) throw new IllegalArgumentException("invalid email");
            value = value.toLowerCase();   // có thể chỉnh trước khi gán
        }

        public static Email of(String raw) { return new Email(raw); }
        public String domain() { return value.substring(value.indexOf('@') + 1); }
    }

    // Records implement interface
    public interface Shape { double area(); }
    public record Circle(double r) implements Shape {
        @Override public double area() { return Math.PI * r * r; }
    }
    public record Rectangle(double w, double h) implements Shape {
        @Override public double area() { return w * h; }
    }

    public static void main(String[] args) {
        var p1 = new Point(3, 4);
        var p2 = new Point(3, 4);
        System.out.println(p1);                  // Point[x=3, y=4]
        System.out.println(p1.x() + " " + p1.y());
        System.out.println(p1.equals(p2));       // true (component equality)
        System.out.println(p1.hashCode() == p2.hashCode());

        var r = new Range(1, 10);
        System.out.println(r);
        try {
            new Range(10, 1);                    // throws
        } catch (IllegalArgumentException ex) {
            System.out.println("caught: " + ex.getMessage());
        }

        var email = Email.of("ALICE@Example.COM");
        System.out.println(email);               // value đã lowercase
        System.out.println("domain: " + email.domain());

        List<Shape> shapes = List.of(new Circle(2), new Rectangle(3, 4));
        for (Shape s : shapes) System.out.println(s + " area=" + s.area());
    }
}
