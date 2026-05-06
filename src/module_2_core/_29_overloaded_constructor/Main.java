package module_2_core._29_overloaded_constructor;

public class Main {
    public static void main(String[] args) {
        //overloaded constructors = multiple constructors within a class with the same name,
        //                          but have different parameters
        //                          name + parameters = signature
        Pizza_Inn pizza = new Pizza_Inn("thick crust",
                "tomato",
                "mozzarella"
                );
        System.out.println("here are the ingredients of your pizza: ");
        System.out.println(pizza.bread);
        System.out.println(pizza.sauce);
        System.out.println(pizza.cheese);
        System.out.println(pizza.topping);
    }
}

