package module_2_core;

public class _10_switch_case {
    public static void main(String[] args) {
         /*  SWITCH = statement that allows a variable to be tested for equality against a list of values   */

        String day = "pizza";
        switch(day) {
            case "Sunday": System.out.println("It is Sunday");
                //break;
            case "Monday": System.out.println("It is Monday");
                //break;
            case "Tuesday": System.out.println("It is Tuesday");
                //break;
            case "Wednesday": System.out.println("It is Wednesday");
                //break;
            case "Thursday": System.out.println("It is Thursday");
                //break;
            case "Friday": System.out.println("It is Friday");
                //break;
            case "Saturday": System.out.println("It is Saturday");
                //breakcase "Friday": System.out.println("It is Friday");
            default:
                System.out.println("That is not a day");
        }
    }
}
