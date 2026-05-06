package module_2_core;

import java.util.Scanner;

public class _9_if_statement {
    public static void main(String[] args) {
         /* IF STATEMENT     */
        Scanner scanner = new Scanner(System.in);
        int age = scanner.nextInt();
        if(age==75){
            System.out.println("You are an adult");
        }
        else if(age>=18){
            System.out.println("Ok Boomer");
        }
        else{
            System.out.println("You are not an adult");
        }
    }
}
