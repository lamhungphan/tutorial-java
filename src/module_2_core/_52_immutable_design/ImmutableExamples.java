package module_2_core._52_immutable_design;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ImmutableExamples {

    // 5 quy tắc tạo class IMMUTABLE (Effective Java Item 17):
    //  1. KHÔNG có setter / mutator
    //  2. Class final (hoặc constructor private + factory) -> chống subclass mutate
    //  3. Field private final
    //  4. Defensive copy reference vào constructor (input)
    //  5. Defensive copy reference khi return getter (output)
    //
    // Ngoài ra: equals/hashCode đúng + toString.

    // BAD — "tưởng" immutable, thực ra leak reference qua Date / array
    public static final class BadDateRange {
        private final Date start;
        private final Date end;

        public BadDateRange(Date start, Date end) {
            this.start = start;          // SAI: lưu reference -> caller giữ ref vẫn mutate được
            this.end = end;
        }
        public Date getStart() { return start; } // SAI: leak reference
        public Date getEnd()   { return end; }
    }

    // GOOD — defensive copy
    public static final class SafeDateRange {
        private final Date start;
        private final Date end;

        public SafeDateRange(Date start, Date end) {
            // 1. defensive copy ngay khi nhận input (ALSO BEFORE validate để chống TOCTOU race)
            this.start = new Date(start.getTime());
            this.end   = new Date(end.getTime());
            if (this.start.after(this.end))
                throw new IllegalArgumentException("start > end");
        }
        public Date getStart() { return new Date(start.getTime()); }
        public Date getEnd()   { return new Date(end.getTime());   }
    }

    // BEST — dùng kiểu IMMUTABLE có sẵn (LocalDate) + record
    public record DateRange(LocalDate start, LocalDate end) {
        public DateRange {
            Objects.requireNonNull(start);
            Objects.requireNonNull(end);
            if (start.isAfter(end)) throw new IllegalArgumentException();
            // LocalDate đã immutable -> không cần defensive copy
        }
    }

    // Mutable collection trong record -> CẦN copyOf hoặc List.copyOf trong constructor
    public record Team(String name, List<String> members) {
        public Team {
            members = List.copyOf(members);  // immutable + defensive copy
        }
    }

    public static void main(String[] args) {
        // Demo BAD leak
        Date start = new Date(0);
        Date end = new Date(1000_000_000L);
        BadDateRange bad = new BadDateRange(start, end);
        System.out.println("bad start: " + bad.getStart());

        start.setTime(99999999L);          // mutate input
        bad.getStart().setTime(11111111L); // mutate via getter
        System.out.println("bad start after mutate: " + bad.getStart()); // CHANGED!

        // Demo SAFE
        SafeDateRange safe = new SafeDateRange(new Date(0), new Date(1000_000_000L));
        Date stolen = safe.getStart();
        stolen.setTime(99999999L);
        System.out.println("safe start: " + safe.getStart()); // KHÔNG đổi

        // Demo BEST
        DateRange r = new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        System.out.println(r);

        // Team với defensive copy
        var members = new java.util.ArrayList<String>(List.of("Alice"));
        var team = new Team("A", members);
        members.add("Bob");                       // mutate input
        try {
            team.members().add("Charlie");        // throws — view immutable
        } catch (UnsupportedOperationException ex) {
            System.out.println("team is locked: " + ex.getClass().getSimpleName());
        }
        System.out.println(team);
    }
}
