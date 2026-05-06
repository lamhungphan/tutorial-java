package module_11_concurrency_advanced._11_structured_concurrency;

// LƯU Ý: Structured Concurrency là PREVIEW từ J21 (JEP 453, JEP 462, JEP 480, JEP 499).
// Để chạy demo này cần Java 21+ và bật flag preview:
//    java --enable-preview --source 21 StructuredConcurrencyDemo.java
//
// API có thể thay đổi. Cú pháp dưới đây theo JEP 480 (J23 preview).
// File này dùng comment thay vì compile thật để tránh lỗi.

public class StructuredConcurrencyDemo {

    record User(String name) {}
    record Order(int id, double total) {}

    public static void main(String[] args) {
        System.out.println("""
                Đây là pseudo-code minh hoạ Structured Concurrency.
                Để compile cần --enable-preview và import jdk.incubator.concurrent / java.util.concurrent.StructuredTaskScope tuỳ phiên bản.
                Xem concept.md để hiểu nguyên lý.
                """);
    }

    /*
    // PSEUDO CODE — pattern Shutdown-on-Failure: 1 task fail -> cancel hết
    static OrderSummary loadOrderSummary(int userId) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<User> userTask    = scope.fork(() -> userService.findById(userId));
            Subtask<List<Order>> ords = scope.fork(() -> orderService.findByUser(userId));

            scope.join();          // đợi tất cả xong (hoặc 1 fail)
            scope.throwIfFailed(); // re-throw exception nếu có

            return new OrderSummary(userTask.get(), ords.get());
        }
    }

    // Pattern Shutdown-on-Success: lấy task ĐẦU TIÊN xong success, cancel còn lại
    static String firstAvailable() throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            scope.fork(() -> queryServerA());
            scope.fork(() -> queryServerB());
            scope.fork(() -> queryServerC());
            scope.join();
            return scope.result();   // first success
        }
    }

    // Custom policy
    class CollectAllResults<T> extends StructuredTaskScope<T> {
        final Queue<T> results = new ConcurrentLinkedQueue<>();
        @Override protected void handleComplete(Subtask<? extends T> sub) {
            if (sub.state() == State.SUCCESS) results.add(sub.get());
        }
    }

    // Đặc điểm:
    //   1. Owner thread không được rời try-with-resources cho đến khi join.
    //   2. Subtask tạo ra virtual thread tự động.
    //   3. Hierarchy thread rõ ràng -> stack trace có thread parent.
    //   4. Cancel propagate downward — interrupt parent -> tất cả con bị interrupt.
    */
}
