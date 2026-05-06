package module_2_core;

public class _16_array_2D {
    public static void main(String[] args) {
         /*  2D ARRAY = an array of arrays    */
        String[][] cars =  {{"Camaro","Civic","CX-5"},
                            {"BMW","Ram","Chevrolet"},
                            {"Mustang","Maserati","Carnival"}};
        for(int i=0; i<cars.length; i++){
            for(int j=0; j<cars.length; j++){
                System.out.print(cars[i][j]+" ");
            }
        }
    }
}
