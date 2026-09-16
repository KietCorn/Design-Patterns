
# CREATIONAL PATTERNS — Cheat sheet (A4)

---

### 1. SINGLETON – Đảm bảo chỉ 1 instance duy nhất, truy cập toàn cục

```java
class Singleton {
    private static Singleton inst;
    private Singleton() {}
    public static Singleton getInstance() {
        if (inst == null) inst = new Singleton();
        return inst;
    }
}
// Dùng: Singleton s = Singleton.getInstance();
```

---

### 2. FACTORY METHOD – Tạo 1 object, ẩn class cụ thể khỏi client

```java
interface Vehicle { void drive(); }
class Car implements Vehicle { public void drive() { System.out.println("Car"); } }
class ElectricCar implements Vehicle { public void drive() { System.out.println("EV"); } }

class VehicleFactory {
    static Vehicle create(String type) {
        return type.equals("car") ? new Car() : new ElectricCar();
    }
}
// Dùng: Vehicle v = VehicleFactory.create("car"); v.drive();
```

---

### 3. ABSTRACT FACTORY – Tạo cả 1 họ object liên quan, đồng bộ với nhau

```java
interface Chair { void sitOn(); }
interface Table { void putOn(); }
class ModernChair implements Chair { public void sitOn() { System.out.println("Modern chair"); } }
class ModernTable implements Table { public void putOn() { System.out.println("Modern table"); } }

interface FurnitureFactory {
    Chair createChair();
    Table createTable();
}
class ModernFurnitureFactory implements FurnitureFactory {
    public Chair createChair() { return new ModernChair(); }
    public Table createTable() { return new ModernTable(); }
}
// Dùng: FurnitureFactory f = new ModernFurnitureFactory();
//       f.createChair().sitOn(); f.createTable().putOn();
```

---

### 4. BUILDER – Khởi tạo object nhiều bước, nhiều tham số tùy chọn

```java
class Burger {
    String bread, meat;
    private Burger(Builder b) { bread = b.bread; meat = b.meat; }

    static class Builder {
        String bread, meat;
        Builder setBread(String b) { bread = b; return this; }
        Builder setMeat(String m) { meat = m; return this; }
        Burger build() { return new Burger(this); }
    }
}
// Dùng: Burger b = new Burger.Builder().setBread("Wheat").setMeat("Chicken").build();
```

---

### 5. PROTOTYPE – Nhân bản (clone) từ object có sẵn, tránh tốn chi phí khởi tạo lại

```java
class Enemy implements Cloneable {
    String type; int hp;
    Enemy(String type, int hp) { this.type = type; this.hp = hp; }
    public Enemy clone() {
        try { return (Enemy) super.clone(); }
        catch (CloneNotSupportedException e) { throw new RuntimeException(e); }
    }
}
// Dùng: Enemy orc1 = new Enemy("Orc", 100);
//       Enemy orc2 = orc1.clone(); orc2.hp = 80;
```

---

## Ghi nhớ nhanh (đọc lướt trước khi vào phòng thi)

| Pattern          | 1 câu ghi nhớ                                                              |
| ---------------- | ---------------------------------------------------------------------------- |
| Singleton        | 1 field static + constructor private + method static trả về nó            |
| Factory Method   | 1 method static, if/switch để chọn class trả về                         |
| Abstract Factory | Factory tạo ra**nhiều** object cùng họ (2+ method create)          |
| Builder          | Inner static class`Builder`, mỗi method `return this`, có `.build()` |
| Prototype        | `implements Cloneable`, override `clone()` gọi `super.clone()`        |
