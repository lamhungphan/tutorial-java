package module_3_oop._36_supper_keyword;

public class Person {
    String name;
    int age;

    //constructor
    Person(String name, int age){
        this.name=name;
        this.age=age;
    }

    //toString
    public String toString(){
        return this.name + "\n" + this.age + "\n";
    }
}
