package module_3_oop._33_static_keyword;

public class Main {
    public static void main(String[] args) {
        //static =  modifier. A single copy of a variable/method/class is created and shared.
        //          The class "owns" the static member
        Friend friend1 = new Friend("SpongeBob");
        Friend friend2 = new Friend("Patrick");
        Friend friend3 = new Friend("SquidWard");
        Friend friend4 = new Friend("Sandy");

       //System.out.println("You have "+Friend.numberOfFriends+" friends");
        Friend.displayName();
    }
}
