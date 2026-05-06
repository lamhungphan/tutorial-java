package module_2_core._27_constructors;

public class Main {
    public static void main(String[] args) {
        //constructor = special methods that is called when an object is instantiated (created)
        human human1 = new human("Rick",45,70);
        human human2 = new human("Morty",16,50);

        System.out.println(human2.the_name);
        human2.eat();
        human1.drink();
    }
}

