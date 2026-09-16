# 10 Design Patterns — Cheat Sheet (1 tờ A4)

---

**SINGLETON**
- Ý tưởng: Như 1 Tổng thống — cả nước chỉ có 1, hỏi ai cũng ra đúng người đó.
- Bài toán: Không dùng → mỗi nơi `new Config()` một lần, data không nhất quán, tốn tài nguyên.
- Code tối giản:
```java
class Config {
    private static Config instance;
    private Config() {}
    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }
}
```
- Dùng: `Config.getInstance()` // mọi nơi đều trả về cùng 1 object

---

**FACTORY METHOD**
- Ý tưởng: Như chuỗi nhượng quyền — lớp cha quy định "phải bán bánh", mỗi chi nhánh tự quyết làm loại bánh nào.
- Bài toán: Không dùng → `if-else` tràn lan để quyết định `new Zombie()` hay `new Robot()`, thêm loại mới phải sửa code cũ.
- Code tối giản:
```java
interface Enemy { void attack(); }
class Zombie implements Enemy { public void attack() { System.out.println("Cắn!"); } }

abstract class Spawner {
    abstract Enemy createEnemy(); // Factory Method
    void spawn() { createEnemy().attack(); }
}
class ZombieSpawner extends Spawner {
    Enemy createEnemy() { return new Zombie(); }
}
```
- Dùng: `new ZombieSpawner().spawn()` // thêm loại = thêm subclass, không sửa Spawner

---

**ABSTRACT FACTORY**
- Ý tưởng: Như chọn phong cách nội thất — chọn "Nhật" thì bàn + ghế + đèn đều ra kiểu Nhật, không bao giờ trộn lẫn.
- Bài toán: Không dùng → dễ trộn lẫn `WindowsButton` với `MacCheckbox`, giao diện mất đồng bộ.
- Code tối giản:
```java
interface Button { void render(); }
interface UIFactory { Button createButton(); }

class WinButton implements Button { public void render() { System.out.println("Win Button"); } }
class WinFactory implements UIFactory { public Button createButton() { return new WinButton(); } }

class MacButton implements Button { public void render() { System.out.println("Mac Button"); } }
class MacFactory implements UIFactory { public Button createButton() { return new MacButton(); } }
```
- Dùng: `UIFactory f = new WinFactory(); f.createButton().render()` // đổi factory = đổi cả bộ

---

**BUILDER**
- Ý tưởng: Như order burger — chọn từng phần tùy thích, cuối cùng mới ra sản phẩm hoàn chỉnh.
- Bài toán: Không dùng → constructor 8 tham số, dễ nhầm thứ tự, phải truyền `null` cho field không dùng.
- Code tối giản:
```java
class Email {
    String to, subject, body, cc;
    private Email() {}
    static class Builder {
        String to, subject, body, cc = "";
        Builder to(String v) { to=v; return this; }
        Builder subject(String v) { subject=v; return this; }
        Builder body(String v) { body=v; return this; }
        Builder cc(String v) { cc=v; return this; }
        Email build() { Email e=new Email(); e.to=to; e.subject=subject; e.body=body; e.cc=cc; return e; }
    }
}
```
- Dùng: `new Email.Builder().to("a@b.com").subject("Hi").body("...").build()`

---

**PROTOTYPE**
- Ý tưởng: Như photo copy bản vẽ — không vẽ lại từ đầu, chỉ copy rồi sửa chỗ cần thay đổi.
- Bài toán: Không dùng → phải `new` và set lại hàng chục field mỗi lần tạo object tương tự, tốn tài nguyên.
- Code tối giản:
```java
class Contract implements Cloneable {
    String customer, terms;
    Contract(String c, String t) { customer=c; terms=t; }
    public Contract clone() {
        try { return (Contract) super.clone(); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}
```
- Dùng: `Contract c2 = template.clone(); c2.customer = "Nguyễn A";` // terms giữ nguyên

---

**ADAPTER**
- Ý tưởng: Như bộ chuyển phích cắm — thiết bị Mỹ cắm vừa ổ Việt Nam mà không sửa gì cả hai.
- Bài toán: Không dùng → phải sửa thư viện bên thứ ba hoặc viết lại toàn bộ code cũ để khớp interface.
- Code tối giản:
```java
interface Exporter { void export(String data); }
class JsonLib { void toJson(String data) { System.out.println("JSON: " + data); } } // không sửa được

class JsonAdapter implements Exporter {
    JsonLib lib = new JsonLib();
    public void export(String data) { lib.toJson(data); } // chuyển đổi ở đây
}
```
- Dùng: `Exporter e = new JsonAdapter(); e.export("data")` // client không biết bên trong là JsonLib

---

**FACADE**
- Ý tưởng: Như tổng đài ngân hàng — nói "mở tài khoản", họ lo hết mọi quy trình, mình không cần biết bên trong.
- Bài toán: Không dùng → client phải tự gọi đúng thứ tự 5-6 subsystem, dễ sai, khó bảo trì.
- Code tối giản:
```java
class Lights { void dim() { System.out.println("Dim lights"); } }
class Projector { void on() { System.out.println("Projector on"); } }
class Audio { void on() { System.out.println("Audio on"); } }

class TheaterFacade {
    Lights l=new Lights(); Projector p=new Projector(); Audio a=new Audio();
    void watch() { l.dim(); p.on(); a.on(); } // ẩn sự phức tạp
}
```
- Dùng: `new TheaterFacade().watch()` // 1 dòng thay vì gọi 3+ subsystem thủ công

---

**DECORATOR**
- Ý tưởng: Như thêm topping cà phê — thêm sữa, đường, kem tùy ý, mỗi lớp bọc thêm tính năng vào lớp trước.
- Bài toán: Không dùng → cần N tổ hợp tính năng thì phải tạo N subclass (bold, italic, bold+italic...) — bùng nổ class.
- Code tối giản:
```java
interface Coffee { int cost(); }
class Simple implements Coffee { public int cost() { return 20000; } }

class Milk implements Coffee {
    Coffee c;
    Milk(Coffee c) { this.c = c; }
    public int cost() { return c.cost() + 5000; } // gọi lớp bên trong rồi cộng thêm
}
```
- Dùng: `new Milk(new Milk(new Simple())).cost()` // 30000 — bọc tùy ý, không sửa Simple

---

**STRATEGY**
- Ý tưởng: Như chọn cách đến sân bay — taxi/buýt/xe máy đều đến được, đổi cách đi tùy tình huống.
- Bài toán: Không dùng → `if-else` chọn thuật toán nằm lẫn trong business logic, thêm thuật toán phải sửa class chính.
- Code tối giản:
```java
interface SortStrategy { void sort(int[] a); }
class BubbleSort implements SortStrategy { public void sort(int[] a) { /*...*/ } }
class QuickSort  implements SortStrategy { public void sort(int[] a) { /*...*/ } }

class Sorter {
    SortStrategy s;
    Sorter(SortStrategy s) { this.s = s; }
    void sort(int[] a) { s.sort(a); } // delegate hoàn toàn
}
```
- Dùng: `new Sorter(new QuickSort()).sort(arr)` // đổi strategy = đổi thuật toán, Sorter không đổi

---

**OBSERVER**
- Ý tưởng: Như đăng ký kênh YouTube — kênh đăng video thì subscriber tự nhận, kênh không cần biết ai đang theo dõi.
- Bài toán: Không dùng → Subject phải tự gọi trực tiếp từng service, thêm service mới phải sửa Subject.
- Code tối giản:
```java
interface Observer { void update(String event); }

class Product {
    List<Observer> list = new ArrayList<>();
    void add(Observer o) { list.add(o); }
    void notify(String e) { for (Observer o : list) o.update(e); }
    void setStock(int s) { if (s==0) notify("Hết hàng!"); }
}
class EmailAlert implements Observer {
    public void update(String e) { System.out.println("Email: " + e); }
}
```
- Dùng: `p.add(new EmailAlert()); p.setStock(0)` // tự động notify tất cả, thêm listener không sửa Product

---

| Pattern | Ghi nhớ 1 câu |
|---|---|
| Singleton | 1 instance duy nhất — `getInstance()` |
| Factory Method | Subclass tạo **loại** object nào |
| Abstract Factory | Đổi factory = đổi cả **họ** object |
| Builder | Xây từng bước — `.field().build()` |
| Prototype | Tạo mới bằng **clone()** |
| Adapter | **Chuyển đổi** interface không khớp |
| Facade | **Ẩn** phức tạp sau 1 method đơn giản |
| Decorator | **Bọc thêm lớp** tính năng tại runtime |
| Strategy | **Hoán đổi** thuật toán tự do |
| Observer | Subject **notify** → Observer tự cập nhật |
