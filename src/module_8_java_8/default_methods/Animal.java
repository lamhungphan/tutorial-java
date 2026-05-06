package module_8_java_8.default_methods;

public interface Animal {
    default void eat() {
        System.out.println("eat");
    }

    default void sleep() {
        System.out.println("sleep");
    }

    void makeSound();
}
