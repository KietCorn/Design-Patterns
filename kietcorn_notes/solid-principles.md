# SOLID Principles

SOLID là **5 nguyên tắc thiết kế hướng đối tượng** giúp code dễ bảo trì, dễ mở rộng, và ít lỗi hơn. Tên SOLID là viết tắt của 5 chữ cái đầu của 5 nguyên tắc.

---

## S — Single Responsibility Principle (SRP)
### "Một class chỉ nên có một lý do để thay đổi"

**Hiểu đơn giản**: Mỗi class chỉ làm **một việc duy nhất**. Nếu class vừa xử lý logic, vừa ghi log, vừa kết nối database — đó là dấu hiệu vi phạm SRP.

**Ví dụ vi phạm**:
```java
class UserService {
    void register(User user) { /* logic đăng ký */ }
    void sendWelcomeEmail(User user) { /* gửi email */ }  // ← không phải việc của UserService
    void saveToDatabase(User user) { /* lưu DB */ }       // ← không phải việc của UserService
}
```

**Sau khi áp dụng SRP**:
```java
class UserService {
    void register(User user) { /* chỉ lo logic đăng ký */ }
}
class EmailService {
    void sendWelcomeEmail(User user) { /* chỉ lo gửi email */ }
}
class UserRepository {
    void save(User user) { /* chỉ lo lưu DB */ }
}
```

**Tại sao quan trọng**: Nếu logic email thay đổi, chỉ sửa `EmailService` — không đụng đến `UserService` hay `UserRepository`. Mỗi class có đúng **một lý do để thay đổi**.

---

## O — Open/Closed Principle (OCP)
### "Mở để mở rộng, đóng để sửa đổi"

**Hiểu đơn giản**: Có thể **thêm tính năng mới** mà **không cần sửa code cũ đang chạy tốt**. Mở rộng bằng cách thêm class mới, không phải sửa class hiện tại.

**Ví dụ vi phạm**:
```java
class DiscountCalculator {
    double calculate(String type, double price) {
        if (type.equals("VIP")) return price * 0.8;
        if (type.equals("Student")) return price * 0.9;
        // Thêm loại mới → phải sửa vào đây → vi phạm OCP
        return price;
    }
}
```

**Sau khi áp dụng OCP**:
```java
interface DiscountStrategy {
    double calculate(double price);
}
class VipDiscount implements DiscountStrategy {
    public double calculate(double price) { return price * 0.8; }
}
class StudentDiscount implements DiscountStrategy {
    public double calculate(double price) { return price * 0.9; }
}
// Thêm loại mới → chỉ thêm class mới, không sửa gì cũ
class NewYearDiscount implements DiscountStrategy {
    public double calculate(double price) { return price * 0.7; }
}
```

**Tại sao quan trọng**: Code cũ đang hoạt động ổn định → không đụng vào → không tạo ra bug mới.

---

## L — Liskov Substitution Principle (LSP)
### "Subclass phải thay thế được cho superclass mà không làm hỏng chương trình"

**Hiểu đơn giản**: Nếu code đang dùng `Animal`, thay bằng `Dog` (subclass của `Animal`) thì chương trình vẫn chạy đúng. Subclass **không được làm yếu hơn** hay **hành xử ngược lại** so với lớp cha.

**Ví dụ vi phạm** (bài toán nổi tiếng hình vuông - hình chữ nhật):
```java
class Rectangle {
    int width, height;
    void setWidth(int w) { width = w; }
    void setHeight(int h) { height = h; }
    int area() { return width * height; }
}

class Square extends Rectangle {
    void setWidth(int w) { width = w; height = w; }  // ← thay đổi cả height!
    void setHeight(int h) { width = h; height = h; } // ← thay đổi cả width!
}

// Code này bị lỗi khi dùng Square thay cho Rectangle:
Rectangle r = new Square();
r.setWidth(5);
r.setHeight(3);
System.out.println(r.area()); // Mong đợi: 15, Thực tế: 9 ← vi phạm LSP!
```

**Cách sửa**: Không nên cho `Square` kế thừa `Rectangle`. Tạo interface `Shape` riêng, cả hai implement độc lập.

```java
interface Shape { int area(); }
class Rectangle implements Shape {
    int w, h;
    Rectangle(int w, int h) { this.w=w; this.h=h; }
    public int area() { return w * h; }
}
class Square implements Shape {
    int side;
    Square(int s) { side=s; }
    public int area() { return side * side; }
}
```

**Tại sao quan trọng**: Vi phạm LSP gây ra bug khó tìm — code trông đúng cú pháp nhưng kết quả sai khi dùng subclass.

---

## I — Interface Segregation Principle (ISP)
### "Không nên ép class implement những method mà nó không dùng đến"

**Hiểu đơn giản**: Chia nhỏ interface lớn thành nhiều interface nhỏ hơn, chuyên biệt hơn. Client chỉ phụ thuộc vào những method nó thật sự cần.

**Ví dụ vi phạm**:
```java
interface Worker {
    void work();
    void eat();
    void sleep();
}

class Robot implements Worker {
    public void work() { System.out.println("Robot làm việc"); }
    public void eat() { throw new UnsupportedOperationException("Robot không ăn!"); }  // ← vô lý
    public void sleep() { throw new UnsupportedOperationException("Robot không ngủ!"); } // ← vô lý
}
```

**Sau khi áp dụng ISP**:
```java
interface Workable { void work(); }
interface Eatable  { void eat(); }
interface Sleepable { void sleep(); }

class Human implements Workable, Eatable, Sleepable {
    public void work()  { System.out.println("Người làm việc"); }
    public void eat()   { System.out.println("Người ăn cơm"); }
    public void sleep() { System.out.println("Người ngủ"); }
}

class Robot implements Workable {
    public void work() { System.out.println("Robot làm việc"); }
    // Không cần implement eat() và sleep() vô nghĩa
}
```

**Tại sao quan trọng**: Interface "béo phì" ép class phải implement method vô nghĩa → code rác, dễ gây nhầm lẫn.

---

## D — Dependency Inversion Principle (DIP)
### "Phụ thuộc vào abstraction (interface), không phụ thuộc vào implementation cụ thể"

**Hiểu đơn giản**: Class cấp cao không nên biết chi tiết class cấp thấp đang làm gì. Cả hai nên giao tiếp qua interface. Thay `new MySQLDatabase()` trực tiếp bằng inject interface `Database`.

**Ví dụ vi phạm**:
```java
class OrderService {
    MySQLDatabase db = new MySQLDatabase(); // ← phụ thuộc trực tiếp vào MySQL
    void saveOrder(Order o) { db.save(o); }
}
// Muốn đổi sang MongoDB → phải sửa OrderService!
```

**Sau khi áp dụng DIP**:
```java
interface Database {
    void save(Object obj);
}
class MySQLDatabase implements Database {
    public void save(Object obj) { System.out.println("Lưu vào MySQL"); }
}
class MongoDB implements Database {
    public void save(Object obj) { System.out.println("Lưu vào MongoDB"); }
}

class OrderService {
    Database db; // ← phụ thuộc vào interface, không phải implementation
    OrderService(Database db) { this.db = db; } // inject từ bên ngoài
    void saveOrder(Object o) { db.save(o); }
}

// Dùng:
OrderService s1 = new OrderService(new MySQLDatabase());
OrderService s2 = new OrderService(new MongoDB()); // đổi DB không sửa OrderService
```

**Tại sao quan trọng**: Đây là nền tảng của Dependency Injection (DI) — kỹ thuật phổ biến trong Spring, Angular... Giúp code dễ test (inject mock) và dễ thay thế component.

---

## Tổng kết SOLID

| Chữ | Tên | Ghi nhớ 1 câu |
|-----|-----|---------------|
| **S** | Single Responsibility | Mỗi class chỉ làm **1 việc**, có 1 lý do để thay đổi. |
| **O** | Open/Closed | **Mở** để thêm tính năng, **đóng** để không sửa code cũ. |
| **L** | Liskov Substitution | Subclass thay thế superclass mà **không làm hỏng** logic. |
| **I** | Interface Segregation | Interface nhỏ chuyên biệt hơn là **1 interface to** làm đủ thứ. |
| **D** | Dependency Inversion | Phụ thuộc vào **interface**, không phụ thuộc vào class cụ thể. |

> **Liên hệ với Design Patterns**: Strategy và Observer áp dụng DIP (phụ thuộc interface). Factory Method áp dụng OCP (thêm loại mới không sửa code cũ). Decorator áp dụng OCP và SRP. Facade áp dụng SRP (tách trách nhiệm).
