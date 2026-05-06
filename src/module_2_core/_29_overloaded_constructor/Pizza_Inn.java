package module_2_core._29_overloaded_constructor;

public class Pizza_Inn {
   String bread;
   String sauce;
   String cheese;
   String topping;

   //overloaded
   Pizza_Inn(String bread){
      this.bread = bread;
   }
   Pizza_Inn(String bread, String sauce){
      this.bread = bread;
      this.sauce = sauce;
   }
   Pizza_Inn(String bread, String sauce, String cheese){
      this.bread = bread;
      this.sauce = sauce;
      this.cheese = cheese;
   }
   Pizza_Inn(String bread, String sauce, String cheese, String topping) {
      this.bread = bread;
      this.sauce = sauce;
      this.cheese = cheese;
      this.topping = topping;
   }
}

