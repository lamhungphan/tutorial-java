package module_3_oop._39_encapsulation;

public class Car {
    private String make;
    private String model;
    private int year;

    Car(String make, String model, int year){
        //change the body's constructor after declared setter
        this.setMake(make);
        this.setModel(model);
        this.setYear(year);
    }

    //getter    get values that was set to private
    public String getMake(){
        return make;
    }
    public String getModel(){
        return model;
    }
    public int getYear(){
        return year;
    }

    //setter    change/update
    public void setMake(String make) {
        this.make = make;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setYear(int year) {
        this.year = year;
    }
}
