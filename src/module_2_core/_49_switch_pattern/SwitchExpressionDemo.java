package module_2_core._49_switch_pattern;

import java.time.DayOfWeek;

public class SwitchExpressionDemo {

    // Switch EXPRESSION (Java 14, JEP 361) — switch trả về giá trị, không cần break.
    // Cú pháp `case X ->` (arrow) thay `case X:`.

    public static void main(String[] args) {
        // 1. So sánh statement (cũ) vs expression (mới)
        DayOfWeek day = DayOfWeek.WEDNESDAY;

        // ---- Switch statement (cũ) ----
        int hoursOld;
        switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                hoursOld = 8;
                break;
            case SATURDAY:
            case SUNDAY:
                hoursOld = 0;
                break;
            default:
                throw new IllegalStateException();
        }
        System.out.println("hoursOld = " + hoursOld);

        // ---- Switch expression (J14+) ----
        int hours = switch (day) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> 8;
            case SATURDAY, SUNDAY -> 0;
        };
        System.out.println("hours = " + hours);

        // 2. Multi-statement với `yield`
        String desc = switch (day) {
            case SATURDAY, SUNDAY -> "weekend";
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> {
                String prefix = day == DayOfWeek.FRIDAY ? "TGIF " : "";
                yield prefix + "workday";
            }
        };
        System.out.println(desc);

        // 3. Switch expression EXHAUSTIVE — enum -> compiler check.
        //    Bỏ một case sẽ không compile.

        // 4. KHÔNG fall-through như switch statement cũ — tránh được bug quên `break`.

        // 5. Switch trên int / String vẫn hoạt động:
        String role = "admin";
        int level = switch (role) {
            case "admin" -> 100;
            case "user"  -> 10;
            case "guest" -> 1;
            default      -> 0;
        };
        System.out.println("level = " + level);
    }
}
