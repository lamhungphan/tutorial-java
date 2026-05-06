## Method References
### Lý thuyết
- Method References là cách tham chiếu trực tiếp đến một phương thức hoặc constructor.
- Cú pháp:
  ```
  ClassName::methodName
  ```
- Các loại Method References:
  - Tham chiếu đến phương thức tĩnh: `ClassName::staticMethod`.
  - Tham chiếu đến phương thức của đối tượng cụ thể: `instance::instanceMethod`.
  - Tham chiếu đến constructor: `ClassName::new`.

### Ví dụ
```java
import java.util.*;

public class MethodReferenceExample {
  public static void main(String[] args) {
    List<String> names = Arrays.asList("An", "Bình", "Cường");

    // Sử dụng Method Reference
    names.forEach(System.out::println);

    // Method Reference với phương thức tự định nghĩa
    names.forEach(MethodReferenceExample::printName);
  }

  public static void printName(String name) {
    System.out.println("Tên: " + name);
  }
}
```