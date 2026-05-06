package module_2_core._27_constructors;

public class human {
    String the_name;
    int the_age;
    double the_weight;

    human(String name, int age, double weight){
        this.the_name=name;
        this.the_age=age;
       this.the_weight=weight;
    }
    void eat(){
        System.out.println(this.the_name+" is eating");
    }
    void drink(){
        System.out.println(this.the_name+" is drinking");
    }
}

