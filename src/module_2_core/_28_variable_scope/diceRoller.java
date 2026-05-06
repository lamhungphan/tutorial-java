package module_2_core._28_variable_scope;

import java.util.Random;
public class diceRoller {
    //Random random;    // global var
    //int number;       // global var
    //roll();           // global var
    diceRoller(){   //constructor
        Random random = new Random();       //local var
        int number = 0;                     //local var
        roll(random, number);
    }
    void roll(Random random, int number){
        number = random.nextInt(6)+1;
        System.out.println(number);
    }
}

