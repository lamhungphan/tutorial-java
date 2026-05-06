package module_3_oop._42_polymorphism;

public class Main {
    public static void main(String[] args) {
        //polymorphism =    greek word for poly-"many", morph-"from
        //                  The ability of an object to identify as more than one type

        Car car = new Car();
        Bicycle bicycle = new Bicycle();
        Boat boat = new Boat();

        //Car[] racers = {car, bicycle, boat};      // error
        Vehicle[] racers = {car,bicycle,boat};

        for(Vehicle i : racers){
            i.go();
        }

    }
}
