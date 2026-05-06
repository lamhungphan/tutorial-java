package module_2_core;

import java.util.Scanner;

public class _22_method_with_return {
    public static void main(String[] args) {
        // methods = a block of code that is executed whenever it is called upon
        Scanner sc = new Scanner(System.in);
        System.out.print("x = ");
        int x = sc.nextInt();
        System.out.print("y = ");
        int y = sc.nextInt();
        System.out.println("x + y = "+add(x,y));
    }
    static double add(int num_1, int num_2){    //the datatype of methods is the datatype of the result
        return  num_1 + num_2;
    }

}
