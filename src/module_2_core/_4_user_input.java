package module_2_core;

import java.util.Scanner;
public class _4_user_input {
    public static void main(String[] args) {
          /*  SCANNER   */
        Scanner scanner = new Scanner(System.in);

        System.out.println("What is your name? ");
        String name = scanner.nextLine();
        System.out.println("How old are you? ");
        int age = scanner.nextInt();
        scanner.nextLine();         // THIS LINE MAKE METHOD SCAN LINE CAN USE AGAIN
        System.out.println("What is your favorite Food?");
        String food = scanner.nextLine();

        System.out.println("Hello "+name);
        System.out.println("You are "+age+" years old");
        System.out.println("you like "+food);
    }
}
