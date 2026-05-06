package module_2_core._51_enum_advanced;

import java.util.function.DoubleBinaryOperator;

public class StrategyEnum {

    // Effective Java Item 34: prefer enum CONSTANT-SPECIFIC METHOD IMPLEMENTATION
    // (mỗi constant có behavior riêng) thay vì switch trên enum.

    enum Operation {
        PLUS("+") {
            @Override public double apply(double x, double y) { return x + y; }
        },
        MINUS("-") {
            @Override public double apply(double x, double y) { return x - y; }
        },
        TIMES("*") {
            @Override public double apply(double x, double y) { return x * y; }
        },
        DIVIDE("/") {
            @Override public double apply(double x, double y) {
                if (y == 0) throw new ArithmeticException("/ by zero");
                return x / y;
            }
        };

        private final String symbol;
        Operation(String symbol) { this.symbol = symbol; }

        public abstract double apply(double x, double y);

        @Override public String toString() { return symbol; }
    }

    // Cách khác: enum field giữ functional interface
    enum BoolOp {
        AND((a, b) -> a != 0 && b != 0 ? 1 : 0),
        OR((a, b) -> a != 0 || b != 0 ? 1 : 0),
        XOR((a, b) -> (a != 0) != (b != 0) ? 1 : 0);

        private final DoubleBinaryOperator op;
        BoolOp(DoubleBinaryOperator op) { this.op = op; }
        public double apply(double a, double b) { return op.applyAsDouble(a, b); }
    }

    public static void main(String[] args) {
        for (Operation op : Operation.values()) {
            System.out.println("3 " + op + " 4 = " + op.apply(3, 4));
        }
        for (BoolOp op : BoolOp.values()) {
            System.out.println(op + "(1, 0) = " + op.apply(1, 0));
        }
    }
}
