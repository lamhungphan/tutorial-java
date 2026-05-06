package module_3_oop._34_inheritance;

public class Main {
    public static void main(String[] args) {
        //inheritance =     the process where one class acquires,
        //                  the attributes and methods of another
        Car car = new Car();
        Bicycle bicycle = new Bicycle();
        car.stop();
        bicycle.go();

        System.out.println(car.doors);
        System.out.println(bicycle.pedals);
    }
}
