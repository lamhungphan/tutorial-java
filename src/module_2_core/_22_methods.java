package module_2_core;

public class _22_methods {
    public static void main(String[] args) {
        // methods = a block of code that is executed whenever it is called upon
        String name = "Bro";
        int age = 21;
        hello(name, age);
    }
    static void hello(String your_name, int your_age){
        System.out.println("Hello "+your_name);
        System.out.println("You are "+your_age);
    }
}
