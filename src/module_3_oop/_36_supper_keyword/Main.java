package module_3_oop._36_supper_keyword;

public class Main {
    public static void main(String[] args) {
        //super =   keyword refers to the superclass (parent) of an object
        //          very similar to the "this" keyword

        Hero hero1 = new Hero("Batman",42,"$$$");
        Hero hero2 = new Hero("Superman", 54, "everything");

        System.out.println(hero2.toString());
    }
}
