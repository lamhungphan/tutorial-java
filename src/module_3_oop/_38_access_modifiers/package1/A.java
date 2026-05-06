package module_3_oop._38_access_modifiers.package1;
import module_3_oop._38_access_modifiers.package2.*;
public class A {
    protected   String protectedMessage = "This is protected";

    public static void main(String[] args) {
    C c = new C();
        System.out.println(c.publicMessage);

    }
    /*
    public static void Main(String[] args) {
    B b = new B();
        System.out.println(b.privateMessage);
    }
     */
}
