# 1️⃣ SINGLETON PATTERN

## 🔴 VẤN ĐỀ

Bạn cần **duy nhất 1 cái** trong toàn bộ app:
- 1 Logger toàn cầu (để log mọi chỗ)
- 1 Database connection (kết nối chung)
- 1 Config manager (cấu hình app)

Nếu tạo tùy tiện → 5 instances khác nhau → chaos! 😱

```
Muốn:  Logger ← mọi chỗ dùng cùng 1 instance
Không: 5 Logger ← mỗi nơi 1 instance khác
```

---

## 💡 IDEA (Giải pháp)

Như một **vị vua duy nhất**:
1. **Chỉ có 1 vua** (1 instance duy nhất)
2. **Mọi người biết tìm được vua** (global access)
3. **Không ai có thể tạo vua mới** (constructor bị khóa)

---

## 📊 DIAGRAM

```
┌─────────────────────────┐
│   Singleton: Logger     │
│                         │
│  - instance (duy nhất)  │  ← Chỉ có 1 cái
│  - GetInstance()        │  ← Cách lấy ra
│  - Log(message)         │
│  - (constructor private)│  ← Không ai tạo mới
└─────────────────────────┘
         ↑
      Dùng ở
    mọi nơi
      ↓
  [Module A] → GetInstance() → cùng 1 Logger
  [Module B] → GetInstance() → cùng 1 Logger
  [Module C] → GetInstance() → cùng 1 Logger
```

---

## 🔑 LOGIC (Cách nó hoạt động)

```
Lần 1: GetInstance()
  ├─ instance == null? → YES
  └─ Tạo mới 1 instance
     └─ Lưu vào instance

Lần 2: GetInstance()
  ├─ instance == null? → NO
  └─ Trả về instance cũ

Lần 3, 4, 5...: Cũng trả về instance cũ
```

---

## 🧠 NHỚ NHÉ

> **"Singleton = Một vị vua, cả nước biết tìm ở một chỗ"**

---

## ⚙️ VS PATTERN KHÁC

| Pattern | Mục đích | Tạo nhiều? |
|---------|----------|-----------|
| **Singleton** | 1 instance duy nhất | ❌ KHÔNG |
| Factory | Tạo objects khác loại | ✅ CÓ |
| Builder | Xây dựng objects phức tạp | ✅ CÓ |

---

## 💻 VÍ DỤ CODE

### JavaScript
```javascript
// Logger Singleton
class Logger {
  static instance = null;

  private constructor() {}

  static getInstance() {
    if (Logger.instance === null) {
      Logger.instance = new Logger();
    }
    return Logger.instance;
  }

  log(message) {
    console.log(`[LOG] ${message}`);
  }
}

// Dùng ở A
const loggerA = Logger.getInstance();
loggerA.log("Event A");

// Dùng ở B
const loggerB = Logger.getInstance();
loggerB.log("Event B");

// loggerA === loggerB → true (cùng 1 instance!)
```

### Python
```python
class Logger:
    _instance = None
    
    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance
    
    def log(self, message):
        print(f"[LOG] {message}")

# Dùng
logger1 = Logger()
logger2 = Logger()
print(logger1 is logger2)  # True
```

---

## 🎯 KÍCH HOẠT KHI

✅ Cần **duy nhất 1 cái** trong app  
✅ Cần **global access** tới nó  
✅ Không muốn **tạo mới** lần 2, lần 3  

---

## 📌 CHECKLIST

- [ ] Hiểu: Vấn đề cốn lõi (tại sao cần Singleton)
- [ ] Hiểu: 1 instance = mọi nơi share
- [ ] Hiểu: Constructor bị khóa (private)
- [ ] Hiểu: GetInstance() lần 1 vs lần 2 khác nhau
