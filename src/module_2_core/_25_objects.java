package module_2_core;

// create a Food object
public class _25_objects {
    //attributes - the characteristics that the object has
    String make = "Chevrolet";
    String model = "Corvette";
    int year = 2020;
    String color = "black";
    double price = 50000.00;

    //methods - the different action that object have performed
    void drive(){
        System.out.println("You drive the Food");
    }
    void brake(){
        System.out.println("You step on the brakes");
    }


    //Object = an instance of class may contain attributes and methods
    public static void main(String[] args) {
        _25_objects myCar1 = new _25_objects();
        _25_objects myCar2 = new _25_objects();
        System.out.println(myCar1.make);
        System.out.println(myCar1.model);
        System.out.println("\n");
        System.out.println(myCar2.make);
        System.out.println(myCar2.model);
    }
}
