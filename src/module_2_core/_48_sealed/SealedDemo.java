package module_2_core._48_sealed;

import java.util.List;

public class SealedDemo {

    // EXHAUSTIVE switch — compiler check tất cả permitted subtype phải được handle.
    // Nếu thiếu Triangle, compile error.
    static String describe(Shape s) {
        return switch (s) {
            case Circle c       -> "circle r=" + c.r();
            case Square sq      -> "square size=" + sq.size();
            case Triangle t     -> "triangle";
            case Polygon p      -> "polygon sides=" + p.sides();
            // Không cần default! Sealed -> compiler biết hết.
        };
    }

    public static void main(String[] args) {
        List<Shape> shapes = List.of(
                new Circle(2),
                new Square(3),
                new Triangle(4, 5),
                new RegularPolygon(6, 1.0),
                new IrregularPolygon(8)
        );
        for (Shape s : shapes) {
            System.out.printf("%-30s area=%.3f -> %s%n", s, s.area(), describe(s));
        }

        // Sealed PHỐI HỢP TỐT VỚI:
        //   - record (case Circle(var r) -> ...)
        //   - pattern matching switch
        //   - "with" statement (sắp tới — JEP draft)
        //   - tránh bug: thêm subtype mới là COMPILE ERROR ở mọi switch chưa update.
    }
}
