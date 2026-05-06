## Default Methods

### Lý thuyết

- Hỗ trợ thêm phương thức vào interface mà không cần sửa đổi các class
  implement interface đó.
- Cú pháp:

```java
public interface TênInterface {
    default void tênPhươngThức() {
// Thân hàm mặc định
    }
}
```

### Ví dụ

```java
interface Vehicle {
    default void start() {
        System.out.println("Khởi động phương tiện.");
    }

    void stop();
}

class Car implements Vehicle {
    @Override
    public void stop() {
        System.out.println("Dừng xe.");
    }
}

public class DefaultMethodExample {
    public static void main(String[] args) {
        Vehicle car = new Car();
        car.start(); // Gọi phương thức mặc định
        car.stop();
    }
}
```