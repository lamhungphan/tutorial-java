package module_2_core;

public class _18_wrapper_classes {
    public static void main(String[] args) {
                /*  DATA TYPES  */
        int x;                 // declaration
        x = 123;               // assignment
        long y = 456178871;    //initialization
        double u = .14;
        boolean z = true;
        char symbol = '@';
        String name = "Bro";

        System.out.println("my number is: "+x);
        System.out.println(+y);
        System.out.println(z);
        System.out.println(name);

        //  WRAPPER CLASS = provides a way to use primitive data types as reference data types
        //                     reference data types contain useful methods
        //                     can be used with collections (ex. ArrayList)
        //          |primitive|      |wrapper|
        //            boolean         Boolean
        //            char            Character
        //            int             Integer
        //            double          Double
        // autoboxing =   the automatic conversion that the Java compiler makes between the primitive types and their corresponding object wrapper classes
        // unboxing   =   the reverse of auto boxing. Automatic conversion of wrapper class to primitive
        Boolean a = true;
        Character b = '@';
        Integer c = 123;
        Double d = 3.45;
        String e = "Hung";

        System.out.println("\n");
        System.out.println(a);
    }
}
