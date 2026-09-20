# 📊 SO SÁNH NHANH 3 PATTERNS

## Bảng so sánh

| Tiêu chí | **Singleton** | **Factory** | **Builder** |
|----------|---|---|---|
| **Mục đích** | 1 instance duy nhất | Tạo loại objects khác | Xây objects phức tạp |
| **Số lượng** | ❌ KHÔNG (chỉ 1) | ✅ CÓ (nhiều loại) | ✅ CÓ (nhưng 1 cái) |
| **Khi dùng** | Logger, Config, DB | Car/Bike, Payment, UI | Computer, Request, Pizza |
| **Ví dụ** | "Một vị vua" | "Máy tạo types" | "Xây từng bước" |

---

## 🎯 Chọn cái nào?

```
❓ Cần duy nhất 1 cái trong toàn app?
   → SINGLETON
   
❓ Cần tạo nhiều loại objects khác nhau?
   → FACTORY
   
❓ Object phức tạp + nhiều optional properties?
   → BUILDER
```

---

## 📝 So sánh kỹ hơn

### SINGLETON vs FACTORY

```
SINGLETON:
  getInstance() → Logger (lần 1)
  getInstance() → Logger (lần 2, CÓ cùng)
  
FACTORY:
  create("car") → Car object (lần 1)
  create("bike") → Bike object (lần 2, KHÁC loại)
```

### FACTORY vs BUILDER

```
FACTORY - Tạo 1 loại, xong
  TransportFactory.create("car") → Car() hoàn thành

BUILDER - Tạo 1 cái phức tạp, từng bước
  ComputerBuilder()
    .SetCPU("Intel")
    .SetRAM("32GB")
    .SetGPU("RTX4090")
    .Build() → Computer() hoàn thành
```

---

## 🧠 Ghi nhớ nhanh

| Pattern | Nhớ nhé | Hình ảnh |
|---------|--------|---------|
| **Singleton** | Một vị vua | 👑 |
| **Factory** | Máy tạo | 🏭 |
| **Builder** | Lắp ráp từng bước | 🔧 |

---

## 🔥 Scenario thực tế

### Scenario 1: E-commerce App

```javascript
// SINGLETON: Logger
const logger = Logger.getInstance();
logger.log("User logged in");

// FACTORY: Payment method
const payment = PaymentFactory.create("credit-card");
payment.process(100);

// BUILDER: Order
const order = new OrderBuilder()
  .SetCustomer("John")
  .SetItems([...])
  .SetShipping("express")
  .Build();
```

### Scenario 2: Game Development

```javascript
// SINGLETON: Game engine
const engine = GameEngine.getInstance();

// FACTORY: Create enemies
const enemy1 = EnemyFactory.create("zombie");
const enemy2 = EnemyFactory.create("goblin");

// BUILDER: Create complex level
const level = new LevelBuilder()
  .AddSpawner(...)
  .SetDifficulty("hard")
  .AddBoss(...)
  .Build();
```

---

## ✅ Checklist: Bạn hiểu chưa?

- [ ] Khi nào dùng Singleton (1 cái duy nhất)
- [ ] Khi nào dùng Factory (tạo loại khác)
- [ ] Khi nào dùng Builder (xây phức tạp)
- [ ] Có thể phân biệt 3 cái?
- [ ] Có thể giải thích bằng lời (không code)?
