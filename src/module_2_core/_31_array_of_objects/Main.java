package module_2_core._31_array_of_objects;

public class Main {
    public static void main(String[] args) {
        /* 1
        Food[] refrigerator = new Food[3];

        Food food1 = new Food("pizza");
        Food food2 = new Food("hamburger");
        Food food3 = new Food("pasta");

        refrigerator[0] = food1;
        refrigerator[1] = food2;
        refrigerator[2] = food3;
         */

        /*2  */
        Food food1 = new Food("pizza");
        Food food2 = new Food("hamburger");
        Food food3 = new Food("pasta");

        Food[] refrigerator = {food1, food2, food3};

        System.out.println(refrigerator[1].name);
    }
}
