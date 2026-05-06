package module_2_core;

import java.util.Random;

public class _8_random {
    public static void main(String[] args) {
        /* GENERATE RANDOM    */
        Random random = new Random();
        int x = random.nextInt(6)+1;  //random from 1 to 6
        //double y = random.nextDouble();    //random from 0 to number in parentheses ( )
        //boolean z = random.nextBoolean();
        System.out.println(x);
    }
}
