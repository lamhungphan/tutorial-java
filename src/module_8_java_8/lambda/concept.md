## Biểu thức Lambda

### Lý thuyết

- Biểu thức Lambda được giới thiệu trong Java 8 để hỗ trợ lập trình hàm.
- Lambda cho phép bạn viết các đoạn mã ngắn gọn hơn, đặc biệt hữu ích khi làm việc với các biểu thức nội tuyến.
- Cấu trúc của Lambda:
  ```
  (tham_số) -> { thân_hàm }
  ```
- Các thành phần của Lambda:
    - **Danh sách tham số**: Có thể không có hoặc có nhiều tham số.
    - **Toán tử mũi tên (->)**: Phân tách danh sách tham số và phần thân hàm.
    - **Thân hàm**: Chứa logic cần thực hiện.

### Ví dụ

```java
import java.util.*;

public class LambdaExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("An", "Bình", "Cường");

        // Sử dụng Lambda để duyệt danh sách
        names.forEach(name -> System.out.println(name));

        // Lambda với logic phức tạp hơn
        names.forEach(name -> {
            if (name.startsWith("A")) {
                System.out.println("Tên bắt đầu bằng A: " + name);
            }
        });
    }
}
```