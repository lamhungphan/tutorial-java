package module_3_oop._39_encapsulation;

public class Main {
    public static void main(String[] args) {
        //Encapsulation =   attributes of a class will be hidden or private,
        //                  Can be accessed only through methods (getters & setters)
        //                  You should make attributes private if you don't have a reason to make public/protected
        Car car1 = new Car("Toyota","Raize",2021);

        //System.out.println(car1.make);  //---> error
        System.out.println(car1.getMake());
        System.out.println(car1.getModel());
        System.out.println(car1.getYear());

        //car.year = 2022; //---> can not find because of private


    }
}
