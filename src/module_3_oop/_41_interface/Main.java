package module_3_oop._41_interface;

public class Main {
    public static void main(String[] args) {
        // interface =  A template that can be applied to a class.
        //              Similar to inheritance, but specifies what a class has/must do.
        //              Classes can apply more than one interface, inheritance is limited to 1 super

        Hawk hawk = new Hawk();
        Rabbit rabbit = new Rabbit();
        hawk.hunt();
        rabbit.flee();

        System.out.println();

        Fish fish = new Fish();
        fish.hunt();
        fish.flee();
    }
}
