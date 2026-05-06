package module_2_core._30_toString;

public class Main {
    public static void main(String[] args) {
        //toString() =  special methods that all objects inherit,
        //              that returns a String methods that "textually represents" an object.
        //              can be used both implicitly and explicitly

        Car car = new Car();
        //System.out.println(Food);    //address of the Car object in memory
        System.out.println(car.toString());     //explicitly
        System.out.println(car);                //implicitly
    }
}
