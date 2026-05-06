package module_2_core;

import java.util.ArrayList;
public class _19_arrayList {
    public static void main(String[] args) {
        //  ARRAY LIST =    resizable array.
        //                  Elements can be added and removed after compilation phase
        //                  store reference data types
        ArrayList<String> food = new ArrayList<String>();
        food.add("pho");
        food.add("hot dog");
        food.add("pizza");
        food.set(0,"sushi");
        food.remove(2);
        for(int i=0; i<food.size(); i++){
            System.out.println(food.get(i));
        }
    }
}
