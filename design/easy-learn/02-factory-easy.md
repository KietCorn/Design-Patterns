# 2️⃣ FACTORY PATTERN

## 🔴 VẤN ĐỀ

Bạn cần tạo nhiều **loại objects khác nhau** tùy vào input:

```javascript
// ❌ Cách cũ - phải biết chi tiết từng loại
if (type === "car") {
  const obj = new Car(...nhiều params...);
} else if (type === "bike") {
  const obj = new Bike(...nhiều params...);
} else if (type === "truck") {
  const obj = new Truck(...nhiều params...);
}
// Điều này lặp đi lặp lại → CODE DUPLICATION
```

---

## 💡 IDEA (Giải pháp)

Bạn tạo 1 **"máy tạo" (Factory)**:
- Bạn nói: "Tôi cần loại X"
- Machine nhận yêu cầu, **tự động tạo đúng loại**
- Bạn không cần biết chi tiết "làm sao tạo X"

```
Bạn          Factory        Objects
  │            │              │
  ├─"car"─────→│              │
  │            ├─→ new Car()──→│
  │            │              │
  ├─"bike"────→│              │
  │            ├─→ new Bike()─→│
```

---

## 📊 DIAGRAM

```
┌─────────────────────┐
│  TransportFactory   │
│  ┌─────────────────┐│
│  │ Create(type)    ││
│  │  if car→Car()   ││
│  │  if bike→Bike() ││
│  │  if truck→ ...  ││
│  └─────────────────┘│
└─────────────────────┘
         ↓
    ┌────┴─────┬──────────┐
    │           │          │
   Car        Bike       Truck
(objects được factory tạo)
```

---

## 🔑 LOGIC (Cách nó hoạt động)

```
Input: type = "car"
  ↓
Factory nhận type
  ├─ type == "car"? → YES
  ├─ return new Car()
  └─ end

Input: type = "bike"
  ↓
Factory nhận type
  ├─ type == "car"? → NO
  ├─ type == "bike"? → YES
  ├─ return new Bike()
  └─ end
```

---

## 🧠 NHỚ NHÉ

> **"Factory = Máy tạo objects, bạn chỉ cần nói loại, máy tự tạo"**

---

## ⚙️ VS PATTERN KHÁC

| Pattern | Mục đích | Tạo bao nhiêu? |
|---------|----------|---|
| Singleton | 1 instance duy nhất | ❌ KHÔNG |
| **Factory** | Tạo loại objects khác nhau | ✅ CÓ |
| Builder | Xây dựng 1 object phức tạp | ✅ CÓ |

**Khác nhau**:
- **Singleton** → Luôn 1 cái
- **Factory** → Nhiều cái, nhưng loại khác nhau
- **Builder** → 1 cái nhưng setup lâu + phức tạp

---

## 💻 VÍ DỤ CODE

### JavaScript
```javascript
// Base interface (hình dung)
class Transport {
  drive() { }
}

// Implementations
class Car extends Transport {
  drive() { return "Car: Vroom vroom 🚗"; }
}
class Bike extends Transport {
  drive() { return "Bike: Ring ring 🚲"; }
}
class Truck extends Transport {
  drive() { return "Truck: Honk honk 🚚"; }
}

// FACTORY - cái máy tạo
class TransportFactory {
  static create(type) {
    if (type === "car") return new Car();
    if (type === "bike") return new Bike();
    if (type === "truck") return new Truck();
    return null;
  }
}

// DÙNG
const vehicle1 = TransportFactory.create("car");
console.log(vehicle1.drive());  // Car: Vroom vroom

const vehicle2 = TransportFactory.create("bike");
console.log(vehicle2.drive());  // Bike: Ring ring

// ✅ Lợi ích: Không cần import Car, Bike, Truck
// ✅ Chỉ cần gọi Factory.create(type)
```

### Python
```python
class Transport:
    def drive(self): pass

class Car(Transport):
    def drive(self): return "Car: Vroom vroom 🚗"

class Bike(Transport):
    def drive(self): return "Bike: Ring ring 🚲"

class TransportFactory:
    @staticmethod
    def create(transport_type):
        types = {
            "car": Car,
            "bike": Bike,
        }
        return types.get(transport_type)()

# DÙNG
vehicle = TransportFactory.create("car")
print(vehicle.drive())  # Car: Vroom vroom
```

---

## 🎯 KÍCH HOẠT KHI

✅ Tạo **nhiều loại objects khác nhau**  
✅ **Loại nào cần tạo** được quyết định lúc runtime  
✅ Muốn **ẩn chi tiết** tạo từng loại  
✅ Giảm **dependency** giữa client và concrete classes  

---

## 📌 CHECKLIST

- [ ] Hiểu: Vấn đề (code duplication khi tạo nhiều loại)
- [ ] Hiểu: Factory = máy tạo objects
- [ ] Hiểu: Input loại → Output object tương ứng
- [ ] Hiểu: Lợi ích (gọi 1 method thay vì biết chi tiết từng loại)
