package module_2_core;

import java.util.Scanner;

public class _24_printf {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //printf() =    an optional methods to control, format, and display text to the console window
        //              two arguments = format String_methods + (object/variable/value)
        //              % [flags] [precision] [width] [conversion-character]
        boolean myBoolean = true;
        char myChar = '#';
        String myString = "Bro";
        int myInt = 25;
        double myDouble = 35.5;
        System.out.printf("%d This is a format String\n",123);
        System.out.printf("%d\n",myInt);
        System.out.printf("%b\n",myBoolean);
        System.out.printf("%c\n",myChar);
        System.out.printf("%s\n",myString);

       //[width]
       //minimum number of characters to be written as output
        System.out.printf("Hello %5s\n",myString);
        System.out.printf("Hello %-5s\n\n",myString);       //drag the result

        //[precision]
        //sets number of digits of precision when outputting floating point values
        System.out.printf("You have this much money %.2f\n\n",myDouble);

        //[flags]
        //adds an effect to output based on the flag added to format specifier
        // -    left-justify
        // +    output a plus (+) or minus (-) sign for a numeric value
        // 0    numeric values are zero-padded
        // ,    comma grouping separator if numbers > 1000
        System.out.printf("You have this much money %020f",myDouble);
    }
}
