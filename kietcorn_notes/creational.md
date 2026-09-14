
# Creational Design Patterns — Tổng hợp 5 mẫu

Nhóm Creational trả lời câu hỏi: **"Object được tạo ra như thế nào để linh hoạt, không lệ thuộc cứng vào `new`?"**

Có 5 pattern chính: **Singleton, Factory Method, Abstract Factory, Builder, Prototype**.

---

## 1. Singleton

### Đời sống liên hệ

Một quốc gia chỉ có **1 tổng thống** tại một thời điểm. Dù bao nhiêu người muốn "hỏi ý kiến tổng thống", họ đều được dẫn tới **cùng một người** — không ai có quyền "tạo thêm" một tổng thống khác.

### Vấn đề giải quyết

Đảm bảo một class **chỉ có đúng 1 instance** trong toàn bộ ứng dụng, và cung cấp 1 điểm truy cập toàn cục tới instance đó. Dùng khi: quản lý cấu hình chung (Config), kết nối database dùng chung (Connection Pool), logger dùng chung.

### Code Java

```java
public class AppConfig {
    private static AppConfig instance;
    private String environment;

    private AppConfig() {
        // Constructor private -> chặn "new" từ bên ngoài
        this.environment = "production";
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String env) {
        this.environment = env;
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        AppConfig config1 = AppConfig.getInstance();
        AppConfig config2 = AppConfig.getInstance();

        config1.setEnvironment("staging");

        System.out.println(config2.getEnvironment()); // In ra "staging"
        System.out.println(config1 == config2);        // true -> cùng 1 object duy nhất
    }
}
```

**Điểm mấu chốt cần nhớ**: `config1 == config2` trả về `true` — chứng minh cả 2 biến đang trỏ tới **cùng một vùng bộ nhớ**, dù được gọi ở 2 nơi khác nhau.

---

## 2. Factory Method

### Đời sống liên hệ

Bạn gọi điện tới **một nhà máy sản xuất xe** và nói "cho tôi 1 chiếc xe điện". Bạn không cần biết dây chuyền nào, kỹ sư nào sản xuất ra nó — nhà máy tự quyết định, bạn chỉ nhận được sản phẩm cuối theo đúng loại yêu cầu.

### Vấn đề giải quyết

Nơi gọi code (client) **không cần biết class cụ thể nào đang được tạo** — chỉ cần biết interface chung. Việc quyết định tạo class nào được giao cho 1 "method nhà máy" xử lý dựa trên điều kiện (VD: input runtime).

### Code Java

```java
// Interface chung
interface Vehicle {
    void drive();
}

class Car implements Vehicle {
    public void drive() {
        System.out.println("Lái ô tô trên đường");
    }
}

class ElectricCar implements Vehicle {
    public void drive() {
        System.out.println("Lái xe điện, không tiếng ồn");
    }
}

// "Nhà máy" quyết định tạo loại xe nào
class VehicleFactory {
    public static Vehicle createVehicle(String type) {
        switch (type) {
            case "car": return new Car();
            case "electric": return new ElectricCar();
            default: throw new IllegalArgumentException("Loại xe không hợp lệ: " + type);
        }
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        Vehicle v1 = VehicleFactory.createVehicle("car");
        Vehicle v2 = VehicleFactory.createVehicle("electric");

        v1.drive(); // Lái ô tô trên đường
        v2.drive(); // Lái xe điện, không tiếng ồn

        // Client code không cần biết Car hay ElectricCar là gì bên trong,
        // chỉ cần biết interface Vehicle
    }
}
```

**Điểm mấu chốt cần nhớ**: `Demo` chỉ làm việc với kiểu `Vehicle` (interface), không hề `new Car()` hay `new ElectricCar()` trực tiếp — nếu sau này thêm `HybridCar`, chỉ cần sửa trong `VehicleFactory`, không đụng tới `Demo`.

---

## 3. Abstract Factory

### Đời sống liên hệ

Bạn đặt mua **một bộ nội thất theo phong cách Victorian** (ghế, bàn, tủ đều cùng phong cách) hoặc **một bộ theo phong cách Modern**. Bạn không tự chọn lẻ từng món — bạn chọn "bộ" để đảm bảo mọi món **đồng bộ với nhau**.

### Vấn đề giải quyết

Tạo ra **cả một họ (family) object có liên quan**, đảm bảo chúng tương thích với nhau — tránh tình trạng lẫn lộn (VD: ghế Victorian đi với bàn Modern).

### Code Java

```java
// Các sản phẩm trong 1 họ
interface Chair {
    void sitOn();
}
interface Table {
    void putOn();
}

// Họ Victorian
class VictorianChair implements Chair {
    public void sitOn() { System.out.println("Ngồi ghế Victorian chạm khắc gỗ"); }
}
class VictorianTable implements Table {
    public void putOn() { System.out.println("Đặt đồ lên bàn Victorian"); }
}

// Họ Modern
class ModernChair implements Chair {
    public void sitOn() { System.out.println("Ngồi ghế Modern tối giản"); }
}
class ModernTable implements Table {
    public void putOn() { System.out.println("Đặt đồ lên bàn Modern"); }
}

// Abstract Factory - "hợp đồng" tạo ra 1 bộ
interface FurnitureFactory {
    Chair createChair();
    Table createTable();
}

class VictorianFurnitureFactory implements FurnitureFactory {
    public Chair createChair() { return new VictorianChair(); }
    public Table createTable() { return new VictorianTable(); }
}

class ModernFurnitureFactory implements FurnitureFactory {
    public Chair createChair() { return new ModernChair(); }
    public Table createTable() { return new ModernTable(); }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        // Chỉ cần đổi factory này -> cả bộ nội thất đổi theo, luôn đồng bộ
        FurnitureFactory factory = new VictorianFurnitureFactory();

        Chair chair = factory.createChair();
        Table table = factory.createTable();

        chair.sitOn();  // Ngồi ghế Victorian chạm khắc gỗ
        table.putOn();  // Đặt đồ lên bàn Victorian

        // Đổi sang bộ Modern chỉ bằng cách đổi factory, không sửa gì khác
        factory = new ModernFurnitureFactory();
        factory.createChair().sitOn(); // Ngồi ghế Modern tối giản
    }
}
```

**Điểm mấu chốt cần nhớ**: khác với Factory Method (chỉ tạo 1 loại object), Abstract Factory tạo **nhiều object liên quan trong 1 lần**, và đảm bảo chúng luôn "cùng bộ" với nhau.

---

## 4. Builder

### Đời sống liên hệ

Khi gọi món tại **Subway**, bạn chọn từng bước: loại bánh → loại thịt → rau → sốt. Mỗi người chọn khác nhau, nhưng quy trình xây dựng (build) luôn theo đúng thứ tự các bước, và cuối cùng nhận được 1 chiếc bánh hoàn chỉnh phù hợp với lựa chọn của mình.

### Vấn đề giải quyết

Tách quá trình khởi tạo **phức tạp, nhiều bước, nhiều tham số tùy chọn** ra thành các bước riêng — tránh việc phải viết 1 constructor với hàng chục tham số khó đọc (VD: `new Burger(true, false, true, null, "cheese", ...)`).

### Code Java

```java
class Burger {
    private final String bread;
    private final String meat;
    private final boolean cheese;
    private final boolean lettuce;

    // Constructor private, chỉ Builder mới gọi được
    private Burger(Builder builder) {
        this.bread = builder.bread;
        this.meat = builder.meat;
        this.cheese = builder.cheese;
        this.lettuce = builder.lettuce;
    }

    public void describe() {
        System.out.println("Burger: " + bread + ", " + meat
            + ", cheese=" + cheese + ", lettuce=" + lettuce);
    }

    // Builder nằm bên trong, xây dựng object từng bước
    public static class Builder {
        private String bread;
        private String meat;
        private boolean cheese = false;
        private boolean lettuce = false;

        public Builder setBread(String bread) {
            this.bread = bread;
            return this; // trả về chính nó -> cho phép nối chuỗi (chaining)
        }

        public Builder setMeat(String meat) {
            this.meat = meat;
            return this;
        }

        public Builder addCheese() {
            this.cheese = true;
            return this;
        }

        public Builder addLettuce() {
            this.lettuce = true;
            return this;
        }

        public Burger build() {
            return new Burger(this);
        }
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        Burger burger1 = new Burger.Builder()
                .setBread("Wheat")
                .setMeat("Chicken")
                .addCheese()
                .build();

        Burger burger2 = new Burger.Builder()
                .setBread("White")
                .setMeat("Beef")
                .addCheese()
                .addLettuce()
                .build();

        burger1.describe(); // Burger: Wheat, Chicken, cheese=true, lettuce=false
        burger2.describe(); // Burger: White, Beef, cheese=true, lettuce=true
    }
}
```

**Điểm mấu chốt cần nhớ**: mỗi method của `Builder` trả về `this` — đây là kỹ thuật gọi là **method chaining**, cho phép viết code khởi tạo dễ đọc theo dạng "câu văn" thay vì 1 constructor rối rắm.

---

## 5. Prototype

### Đời sống liên hệ

Thay vì viết lại một bản hợp đồng từ đầu, bạn **photocopy** bản hợp đồng mẫu có sẵn, rồi chỉnh sửa vài chi tiết (tên, ngày tháng) cho phù hợp — nhanh hơn nhiều so với soạn thảo lại từ con số 0.

### Vấn đề giải quyết

Tạo object mới bằng cách **sao chép (clone)** từ 1 object có sẵn, thay vì khởi tạo từ đầu — hữu ích khi việc khởi tạo tốn kém (load dữ liệu, tính toán phức tạp) hoặc khi cần nhiều object gần giống nhau.

### Code Java

```java
class Enemy implements Cloneable {
    private String type;
    private int health;
    private int attackPower;

    public Enemy(String type, int health, int attackPower) {
        this.type = type;
        this.health = health;
        this.attackPower = attackPower;
        System.out.println("Đang khởi tạo enemy từ đầu (tốn chi phí)...");
    }

    @Override
    public Enemy clone() {
        try {
            return (Enemy) super.clone(); // sao chép nhanh, không chạy lại constructor
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void describe() {
        System.out.println(type + " - HP: " + health + ", ATK: " + attackPower);
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        // Chỉ tạo 1 lần "bản gốc" tốn kém
        Enemy prototypeOrc = new Enemy("Orc", 100, 15);

        // Từ đó, nhân bản ra nhiều enemy khác mà không tốn chi phí khởi tạo lại
        Enemy orc1 = prototypeOrc.clone();
        Enemy orc2 = prototypeOrc.clone();
        orc2.setHealth(80); // chỉnh sửa riêng cho bản sao này

        prototypeOrc.describe(); // Orc - HP: 100, ATK: 15
        orc1.describe();         // Orc - HP: 100, ATK: 15
        orc2.describe();         // Orc - HP: 80, ATK: 15

        System.out.println(prototypeOrc == orc1); // false -> là 2 object khác nhau trong bộ nhớ
    }
}
```

**Điểm mấu chốt cần nhớ**: dòng `"Đang khởi tạo enemy từ đầu..."` chỉ in ra **1 lần duy nhất** (khi tạo `prototypeOrc`) — các lần `clone()` sau đó **không chạy lại constructor**, tiết kiệm chi phí khởi tạo.

---

## Bảng tổng hợp để ôn nhanh (Active Recall)

Che cột "Vấn đề giải quyết" và "Ví dụ đời sống" lại, chỉ nhìn tên pattern — thử tự nhớ ra 2 cột kia trước khi mở ra kiểm tra:

| Pattern          | Vấn đề giải quyết                                          | Ví dụ đời sống                   |
| ---------------- | --------------------------------------------------------------- | ------------------------------------- |
| Singleton        | Chỉ 1 instance duy nhất                                       | Tổng thống quốc gia                |
| Factory Method   | Tạo 1 object, ẩn class cụ thể                               | Gọi nhà máy đặt xe               |
| Abstract Factory | Tạo cả 1 họ object đồng bộ                                | Đặt bộ nội thất theo phong cách |
| Builder          | Khởi tạo object nhiều bước, nhiều tùy chọn              | Gọi món ở Subway                   |
| Prototype        | Nhân bản từ object có sẵn, tránh tốn chi phí khởi tạo | Photocopy hợp đồng mẫu            |

## Cách liên kết 5 pattern này với nhau

- **Factory Method** vs **Abstract Factory**: Factory Method tạo **1 sản phẩm**, Abstract Factory tạo **1 bộ sản phẩm liên quan**. Abstract Factory thường được cài đặt bằng cách dùng nhiều Factory Method bên trong.
- **Builder** vs **Abstract Factory**: cả 2 đều tạo object phức tạp, nhưng Builder tập trung vào **từng bước xây dựng tuần tự** của 1 object, còn Abstract Factory tập trung vào **tạo nhiều object cùng lúc, đồng bộ về "họ"**.
- **Singleton** thường được dùng để implement chính bản thân 1 Factory (VD: chỉ cần 1 `VehicleFactory` duy nhất trong ứng dụng) — 2 pattern này hay đi kèm nhau trong thực tế.
- **Prototype** là lựa chọn thay thế khi Factory Method/Abstract Factory tốn kém — thay vì "hỏi nhà máy tạo mới", ta "nhân bản" từ 1 mẫu có sẵn.
