package module_2_core._32_object_passing;

public class Main {
    public static void main(String[] args) {
        Garage garage = new Garage();
        Car car1 = new Car("Volvo");
        Car car2 = new Car("BMW");

        garage.park(car1);
        garage.park(car2);
    }
}
