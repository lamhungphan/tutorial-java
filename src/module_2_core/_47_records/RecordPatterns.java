package module_2_core._47_records;

import java.util.List;
import java.util.Map;

public class RecordPatterns {

    // Records phối hợp tốt với pattern matching (J21 — record patterns).

    record Point(int x, int y) {}
    record Line(Point start, Point end) {}
    record Box(Point topLeft, Point bottomRight) {}

    sealed interface Shape permits Circle, Square, Triangle {}
    record Circle(Point center, double r) implements Shape {}
    record Square(Point topLeft, double size) implements Shape {}
    record Triangle(Point a, Point b, Point c) implements Shape {}

    public static void main(String[] args) {
        Object obj = new Line(new Point(0, 0), new Point(3, 4));

        // 1. Pattern matching for instanceof (J16) với deconstruction (J21)
        if (obj instanceof Line(Point(var x1, var y1), Point(var x2, var y2))) {
            double len = Math.hypot(x2 - x1, y2 - y1);
            System.out.println("length = " + len);
        }

        // 2. Switch expression với record patterns (J21) + sealed
        List<Shape> shapes = List.of(
                new Circle(new Point(0, 0), 2),
                new Square(new Point(1, 1), 3),
                new Triangle(new Point(0, 0), new Point(4, 0), new Point(0, 3))
        );
        for (Shape s : shapes) {
            double area = switch (s) {
                case Circle(Point p, double r) -> Math.PI * r * r;
                case Square(Point p, double size) -> size * size;
                case Triangle(Point a, Point b, Point c) ->
                        Math.abs((b.x() - a.x()) * (c.y() - a.y()) - (c.x() - a.x()) * (b.y() - a.y())) / 2.0;
                // Sealed -> exhaustive, không cần default
            };
            System.out.println(s + " area=" + area);
        }

        // 3. Guards với "when" (J21)
        for (Shape s : shapes) {
            String size = switch (s) {
                case Circle(Point p, double r) when r > 1.5 -> "large circle";
                case Circle c -> "small circle";
                case Square sq when sq.size() > 2 -> "large square";
                case Square sq -> "small square";
                case Triangle t -> "triangle";
            };
            System.out.println(s + " -> " + size);
        }

        // 4. Map.Entry dùng deconstruction
        Map<String, Integer> ages = Map.of("Alice", 30, "Bob", 25);
        for (var entry : ages.entrySet()) {
            // J21+: có thể dùng record pattern thay var
            if (entry instanceof Map.Entry e) {
                System.out.println(e.getKey() + " = " + e.getValue());
            }
        }
    }
}
