package module_2_core;

import java.util.ArrayList;

public class _21_for_each_loop {
    public static void main(String[] args) {
        //  for-each =  traversing technique to iterate through the elements in an array/collection
        //              less steps, more readable
        //              less flexible

        /* array         String[] animals = {"cat", "dog", "rat", "bird",};        */
        /* collection       */
        ArrayList<String> animals = new ArrayList<String>();
        animals.add("cat");
        animals.add("dog");
        animals.add("rat");
        animals.add("bird");

        for(String i : animals){
            System.out.println(i);
        }

    }
}
