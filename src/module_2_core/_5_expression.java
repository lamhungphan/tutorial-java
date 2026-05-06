package module_2_core;

import java.util.Scanner;

public class _5_expression {
    public static void main(String[] args) {
            //  OPERATOR
            //expression    operands & operators
            //operands      values, variables, numbers, quantity
            //operators     + - * / %

        int friends = 10;
        friends = friends / 3 ;
        System.out.println(friends);

//          // MATH
        double x;
        double y;
        double z;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter size x: ");
        x = scanner.nextDouble();
        System.out.println("Enter size y: ");
        y = scanner.nextDouble();
        z = Math.sqrt((x*x) + (y*y));
        System.out.println("The hypotenuse is: "+z);
        scanner.close();

    }
}
