package module_4_exception._44_exception_handling;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ExceptionDemo {
    public static void main(String[] args) {
        // exception =  an event that occurs during the execution of a program that,
        //              disrupts the normal flow of instructions
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter the whole number to divide: ");
            int x = sc.nextInt();

            System.out.print("Enter the whole number to divide by: ");
            int y = sc.nextInt();

            int z = x / y;
            System.out.print("Result: " + z);
        }
        catch(ArithmeticException e){
            System.err.println("You can't divide by zero! IDIOT!!");
        }
        catch(InputMismatchException e){
            System.err.println("PLEASE enter a number!!!");
        }
        catch(Exception e){
            System.out.println("Something went wrong");
        }
        finally {
            System.out.println("Program has been closed");
            sc.close();
        }
    }
}
