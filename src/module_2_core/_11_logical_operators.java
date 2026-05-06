package module_2_core;

import java.util.Scanner;
public class _11_logical_operators {
    public static void main(String[] args) {
        /*  LOGICAL OPERATORS = used to connect two or more expressions */
                //  &&      (AND)   both conditions must be true
                //  ||      (OR)    either conditions must be true
                //  !       (NOT)   reverses boolean value of a condition
        Scanner scanner = new Scanner(System.in);
        System.out.println("You are playing a game! Press q or Q to quit");
        String response = scanner.next();

        if(!response.equals("q") && !response.equals("Q") ){
            System.out.println("You are still playing the game");
        }
        else{
            System.out.println("Your quit the game");
        }
    }
}
