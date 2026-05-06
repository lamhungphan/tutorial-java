package module_2_core;

public class _23_overloaded_methods {
    public static void main(String[] args) {
        //overloaded methods =  methods that share the same name but have different parameters
        //                      methods name + parameters = methods signature
        double x = add(1.0,2,3.1,4);
        System.out.println(x);
    }
    static int add(int a, int b){
        System.out.println("This is overloaded methods #1");
        return a + b;
    }
    static int add(int a, int b, int c) {
        System.out.println("This is overloaded methods #2");
        return a + b + c;
    }
    static int add(int a, int b, int c, int d) {
        System.out.println("This is overloaded methods #3");
        return a + b + c + d;
    }
    static double add(double a, double b){
        System.out.println("This is overloaded methods #4");
        return a + b;
    }
    static double add(double a, double b, double c) {
        System.out.println("This is overloaded methods #5");
        return a + b + c;
    }
    static double add(double a, double b,double c, double d) {
        System.out.println("This is overloaded methods #6");
        return a + b + c + d;
    }
}
