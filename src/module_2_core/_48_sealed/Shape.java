package module_2_core._48_sealed;

// Sealed Classes (Java 17, JEP 409)
//
// `sealed` cho phép tác giả CONTROL CHÍNH XÁC tập hợp subtype.
// Subtype phải khai báo 1 trong 3 modifier:
//   - `final`        : đóng — không cho mở rộng tiếp.
//   - `sealed`       : tiếp tục bị giới hạn permits.
//   - `non-sealed`   : mở lại — bất kỳ ai cũng có thể extends.
//
// Sealed interface / class cho phép EXHAUSTIVE switch — không cần default.

public sealed interface Shape permits Circle, Square, Triangle, Polygon {
    double area();
}

final class Circle implements Shape {
    private final double r;
    public Circle(double r) { this.r = r; }
    public double r() { return r; }
    @Override public double area() { return Math.PI * r * r; }
    @Override public String toString() { return "Circle(r=" + r + ")"; }
}

final class Square implements Shape {
    private final double size;
    public Square(double size) { this.size = size; }
    public double size() { return size; }
    @Override public double area() { return size * size; }
    @Override public String toString() { return "Square(size=" + size + ")"; }
}

final class Triangle implements Shape {
    private final double base, height;
    public Triangle(double base, double height) { this.base = base; this.height = height; }
    public double base()   { return base; }
    public double height() { return height; }
    @Override public double area() { return base * height / 2; }
    @Override public String toString() { return "Triangle(base=" + base + ", height=" + height + ")"; }
}

// Sealed sub-hierarchy: Polygon vẫn bị quản lý.
sealed class Polygon implements Shape permits RegularPolygon, IrregularPolygon {
    private final int sides;
    public Polygon(int sides) { this.sides = sides; }
    public int sides() { return sides; }
    @Override public double area() { return 0; }
    @Override public String toString() { return "Polygon(sides=" + sides + ")"; }
}

final class RegularPolygon extends Polygon {
    private final double sideLen;
    public RegularPolygon(int sides, double sideLen) {
        super(sides);
        this.sideLen = sideLen;
    }
    @Override public double area() {
        return 0.25 * sides() * sideLen * sideLen / Math.tan(Math.PI / sides());
    }
}

// `non-sealed` mở lại hierarchy: bất kỳ subclass nào cũng được.
non-sealed class IrregularPolygon extends Polygon {
    public IrregularPolygon(int sides) { super(sides); }
}
