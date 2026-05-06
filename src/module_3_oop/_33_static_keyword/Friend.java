package module_3_oop._33_static_keyword;

public class Friend {
    String name;
    static int numberOfFriends;
    Friend(String name){
        this.name = name;
        numberOfFriends++;
    }
    static void displayName(){
        System.out.println("You have "+numberOfFriends+" friends");
    }
}
