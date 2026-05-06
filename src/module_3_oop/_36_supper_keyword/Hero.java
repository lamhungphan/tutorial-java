package module_3_oop._36_supper_keyword;

public class Hero extends Person {
    String power;

    //constructor
    Hero(String name, int age, String power){
        super(name, age);
        //this.name=name;
        //this.age=age;
        this.power=power;
    }

    //toString with super
    public String toString(){
        return super.toString() + this.power;
    }
}
