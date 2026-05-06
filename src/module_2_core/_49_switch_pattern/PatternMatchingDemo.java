package module_2_core._49_switch_pattern;

import java.util.List;

public class PatternMatchingDemo {

    // Pattern matching for `instanceof` (Java 16, JEP 394) — check + cast 1 lần.
    // Pattern matching for `switch` (Java 21, JEP 441) — switch nhận type pattern + record pattern + guard.

    sealed interface Vehicle permits Car, Truck, Bike, ElectricCar {}
    record Car(String model, int doors) implements Vehicle {}
    record Truck(String model, double cargoTons) implements Vehicle {}
    record Bike(String model) implements Vehicle {}
    record ElectricCar(String model, int doors, double batteryKwh) implements Vehicle {}

    public static void main(String[] args) {
        // --- 1. Pattern matching for instanceof ---
        Object obj = "hello";
        if (obj instanceof String s && s.length() > 3) {
            System.out.println("Long string: " + s.toUpperCase());
        }
        // Trước J16: phải `(String) obj` thủ công.

        // Negation + scope linh hoạt
        if (!(obj instanceof Integer i)) {
            // i KHÔNG visible ở đây (because flow KHÔNG match)
        }
        // sau if -> i cũng không visible (return path khác)

        // --- 2. Type pattern trong switch ---
        Object o = 42L;
        String s = switch (o) {
            case Integer i -> "int: " + i;
            case Long l    -> "long: " + l;
            case Double d  -> "double: " + d;
            case String t  -> "string: " + t;
            case null      -> "null!";          // (J21) — null vào switch không NPE nữa
            default        -> "other: " + o;
        };
        System.out.println(s);

        // --- 3. Record pattern (J21) — deconstruct record ---
        List<Vehicle> fleet = List.of(
                new Car("Civic", 4),
                new Truck("F-150", 1.5),
                new Bike("MTB"),
                new ElectricCar("Model 3", 4, 75.0)
        );

        for (Vehicle v : fleet) {
            String label = switch (v) {
                case Car(String m, int doors)                           -> "Car " + m + ", " + doors + " doors";
                case Truck(String m, double tons)                       -> "Truck " + m + ", " + tons + "t";
                case Bike(String m)                                      -> "Bike " + m;
                case ElectricCar(String m, int doors, double kwh)        -> "EV " + m + " " + kwh + "kWh";
            };
            System.out.println(label);
        }

        // --- 4. Guards với `when` (J21) ---
        for (Vehicle v : fleet) {
            String tier = switch (v) {
                case ElectricCar e when e.batteryKwh() > 70 -> "long-range EV";
                case ElectricCar e                          -> "compact EV";
                case Car c when c.doors() >= 4              -> "family car";
                case Car c                                  -> "sport car";
                case Truck t when t.cargoTons() > 1.0       -> "heavy truck";
                case Truck t                                -> "light truck";
                case Bike b                                 -> "bike";
            };
            System.out.println(v + " -> " + tier);
        }

        // --- 5. Nested record pattern ---
        record Pair(String name, Vehicle vehicle) {}
        Pair pair = new Pair("Alice", new ElectricCar("Model Y", 4, 80));
        String summary = switch (pair) {
            case Pair(String name, ElectricCar(String m, var doors, var kwh)) ->
                    name + " drives " + m + " (" + kwh + " kWh)";
            case Pair(String name, Vehicle v) ->
                    name + " drives " + v;
        };
        System.out.println(summary);
    }
}
