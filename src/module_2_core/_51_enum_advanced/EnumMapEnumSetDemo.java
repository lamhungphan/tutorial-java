package module_2_core._51_enum_advanced;

import java.util.EnumMap;
import java.util.EnumSet;

public class EnumMapEnumSetDemo {

    enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }
    enum Permission { READ, WRITE, DELETE, ADMIN }

    public static void main(String[] args) {
        // ENUMMAP: array-backed (vị trí = ordinal). Nhanh hơn HashMap rất nhiều cho enum key.
        EnumMap<Day, String> menu = new EnumMap<>(Day.class);
        menu.put(Day.MON, "Phở");
        menu.put(Day.WED, "Bún chả");
        menu.put(Day.FRI, "Cơm tấm");
        System.out.println("Menu: " + menu);
        System.out.println("Wed: " + menu.get(Day.WED));

        // ENUMSET: bitset (long mask) — cực nhanh, ít bộ nhớ.
        EnumSet<Day> weekend = EnumSet.of(Day.SAT, Day.SUN);
        EnumSet<Day> workday = EnumSet.complementOf(weekend);
        EnumSet<Day> noon    = EnumSet.range(Day.MON, Day.FRI);
        EnumSet<Day> none    = EnumSet.noneOf(Day.class);
        EnumSet<Day> all     = EnumSet.allOf(Day.class);

        System.out.println("weekend = " + weekend);
        System.out.println("workday = " + workday);
        System.out.println("noon    = " + noon);
        System.out.println("none    = " + none);
        System.out.println("all     = " + all);

        // Phép toán set
        weekend.contains(Day.SAT);            // true
        EnumSet<Day> friOrWeekend = EnumSet.of(Day.FRI);
        friOrWeekend.addAll(weekend);
        System.out.println("FRI ∪ weekend = " + friOrWeekend);

        // EnumSet thay int bitmask kiểu C — type safe + readable
        EnumSet<Permission> perms = EnumSet.of(Permission.READ, Permission.WRITE);
        if (perms.contains(Permission.WRITE)) {
            System.out.println("can write");
        }

        // EnumMap counting
        EnumMap<Day, Integer> count = new EnumMap<>(Day.class);
        for (Day d : Day.values()) count.put(d, 0);
        for (Day d : new Day[]{Day.MON, Day.MON, Day.TUE, Day.FRI, Day.MON}) {
            count.merge(d, 1, Integer::sum);
        }
        System.out.println("count = " + count);
    }
}
