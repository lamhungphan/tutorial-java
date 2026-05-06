## Stream API
### Lý thuyết
- Stream API giúp xử lý dữ liệu theo phong cách hàm (functional-style).
- Hỗ trợ các thao tác như filter, map, reduce, collect, v.v.
- Stream không lưu trữ dữ liệu mà chỉ xử lý dữ liệu theo luồng.

### Ví dụ
```java
import java.util.*;
import java.util.stream.*;

public class StreamAPIExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("An", "Bình", "Cường", "Duy");

        // Lọc các tên bắt đầu bằng 'B'
        List<String> filteredNames = names.stream()
                                          .filter(name -> name.startsWith("B"))
                                          .collect(Collectors.toList());
        System.out.println(filteredNames);

        // Biến đổi các tên thành chữ in hoa
        List<String> upperCaseNames = names.stream()
                                           .map(String::toUpperCase)
                                           .collect(Collectors.toList());
        System.out.println(upperCaseNames);

        // Đếm số tên có độ dài lớn hơn 2
        long count = names.stream()
                          .filter(name -> name.length() > 2)
                          .count();
        System.out.println("Số lượng tên có độ dài > 2: " + count);
    }
}
```