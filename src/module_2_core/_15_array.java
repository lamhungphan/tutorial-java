package module_2_core;

public class _15_array {
    public static void main(String[] args) {
        /*  ARRAY = to store multiple values in a single variable    */
        String[] cars = {"Camaro", "Corvette", "RAM","BMW"};
        cars[0] = "Mustang";    // update
            System.out.println(cars[3]);
        String[] bikes = new String[4];
        bikes[0] = "Asama";
        bikes[1] = "Martin";
        bikes[2] = "Cube";
        bikes[3] = "Giant";
        for(int i=0; i<bikes.length; i++) {
            System.out.print(bikes[i]+" ");
        }
    }
}
