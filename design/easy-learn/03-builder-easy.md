# 3️⃣ BUILDER PATTERN

## 🔴 VẤN ĐỀ

Bạn cần tạo 1 object **phức tạp** với **nhiều thuộc tính**, nhưng **không phải tất cả** đều bắt buộc:

```javascript
// ❌ Cách cũ - Constructor nightmare
class Computer {
  constructor(cpu, ram, gpu, storage, keyboard, mouse, monitor, ...) {
    // 8 params = CHAOS
    // Cái nào bắt buộc? Cái nào optional?
  }
}

// Dùng
const pc = new Computer(
  "Intel i9",   // CPU
  "32GB",       // RAM
  "RTX4090",    // GPU
  "1TB SSD",    // Storage
  null,         // Keyboard (không cần)
  undefined,    // Mouse (không cần)
  null,         // Monitor (không cần)
  // ... 🤯
);
```

**Vấn đề**:
- Constructor có quá nhiều params
- Khó nhớ thứ tự
- Nhiều params là null/undefined
- Khó maintain sau

---

## 💡 IDEA (Giải pháp)

**Builder = Hộp công cụ xây dựng từng bước**

Thay vì tất cả cùng lúc → Xây từng **bước một**:

```
Step 1: SetCPU("Intel")
Step 2: SetRAM("32GB")
Step 3: SetGPU("RTX4090")
Step 4: Build() → Hoàn thành
```

Giống như xây nhà:
1. Đặt nền → SetFoundation()
2. Xây tường → SetWalls()
3. Lợp mái → SetRoof()
4. Hoàn thành → Build()

---

## 📊 DIAGRAM

```
┌──────────────────────────────────┐
│      ComputerBuilder             │
│                                  │
│  - cpu = "default"               │
│  - ram = "default"               │
│  - gpu = "default"               │
│                                  │
│  - SetCPU(val) → return this ────┐
│  - SetRAM(val) → return this ─┐  │
│  - SetGPU(val) → return this ─┼──┼─ Chaining!
│  - Build() → return Computer  │  │
└──────────────────────────────────┘
         ↓
   Dùng step-by-step:
   builder
     .SetCPU("Intel")
     .SetRAM("32GB")
     .SetGPU("RTX4090")
     .Build()  ← Tạo object cuối cùng
         ↓
    Computer object
```

---

## 🔑 LOGIC (Cách nó hoạt động)

```
1. Tạo Builder instance
   builder = new ComputerBuilder()

2. Set CPU
   builder.SetCPU("Intel")
   → Lưu "Intel" vào builder.cpu
   → return builder (để chain tiếp)

3. Set RAM
   builder.SetRAM("32GB")
   → Lưu "32GB" vào builder.ram
   → return builder

4. Build
   builder.Build()
   → Tạo new Computer với cpu, ram, gpu đã lưu
   → return Computer object
```

---

## 🧠 NHỚ NHÉ

> **"Builder = Cầu thang xây dựng, mỗi bước set 1 properties"**

Hoặc: **"Builder = Lắp ráp, set từng bộ phận rồi hoàn thành"**

---

## ⚙️ VS PATTERN KHÁC

| Pattern | Mục đích | Bao nhiêu step? |
|---------|----------|---|
| Singleton | 1 instance duy nhất | 1 |
| Factory | Tạo loại objects khác | 1 |
| **Builder** | Xây dựng 1 object phức tạp | **Nhiều** |

**Khác nhau**:
- **Singleton** → 1 cái, toàn cầu
- **Factory** → Tạo loại khác, nhưng đơn giản
- **Builder** → 1 cái, nhưng **setup từng bước**

---

## 💻 VÍ DỤ CODE

### JavaScript
```javascript
// Object cần xây dựng
class Computer {
  constructor(cpu, ram, gpu) {
    this.cpu = cpu;
    this.ram = ram;
    this.gpu = gpu;
  }

  describe() {
    return `PC: ${this.cpu} CPU, ${this.ram} RAM, ${this.gpu} GPU`;
  }
}

// BUILDER - công cụ xây dựng
class ComputerBuilder {
  constructor() {
    this.cpu = "Intel i5";    // Default
    this.ram = "8GB";         // Default
    this.gpu = "Integrated";  // Default
  }

  SetCPU(cpu) {
    this.cpu = cpu;
    return this;  // ← Chaining!
  }

  SetRAM(ram) {
    this.ram = ram;
    return this;
  }

  SetGPU(gpu) {
    this.gpu = gpu;
    return this;
  }

  Build() {
    return new Computer(this.cpu, this.ram, this.gpu);
  }
}

// DÙNG
const myPC = new ComputerBuilder()
  .SetCPU("Intel i9")
  .SetRAM("32GB")
  .SetGPU("RTX4090")
  .Build();

console.log(myPC.describe());
// PC: Intel i9 CPU, 32GB RAM, RTX4090 GPU

// ✅ Lợi ích:
// - Rõ ràng từng bước
// - Không cần nhớ thứ tự params
// - Dễ bỏ qua cái không cần (GPU mặc định)
```

### Python
```python
class Computer:
    def __init__(self, cpu, ram, gpu):
        self.cpu = cpu
        self.ram = ram
        self.gpu = gpu

    def describe(self):
        return f"PC: {self.cpu}, {self.ram}, {self.gpu}"

class ComputerBuilder:
    def __init__(self):
        self.cpu = "Intel i5"
        self.ram = "8GB"
        self.gpu = "Integrated"

    def set_cpu(self, cpu):
        self.cpu = cpu
        return self  # Chaining

    def set_ram(self, ram):
        self.ram = ram
        return self

    def set_gpu(self, gpu):
        self.gpu = gpu
        return self

    def build(self):
        return Computer(self.cpu, self.ram, self.gpu)

# DÙNG
pc = ComputerBuilder() \
    .set_cpu("Intel i9") \
    .set_ram("32GB") \
    .set_gpu("RTX4090") \
    .build()

print(pc.describe())
# PC: Intel i9, 32GB, RTX4090
```

---

## 🎯 KÍCH HOẠT KHI

✅ Object có **nhiều attributes**  
✅ **Không phải tất cả** đều bắt buộc  
✅ Constructor có **quá nhiều params**  
✅ Muốn **set từng bước** (step-by-step)  

---

## 🔗 CHAINING là gì?

```javascript
// Chaining = gọi liên tiếp
.SetCPU("Intel")
.SetRAM("32GB")
.SetGPU("RTX4090")

// Cách nó hoạt động:
// SetCPU() return this → builder
// SetRAM() được gọi trên builder đó
// SetGPU() được gọi trên builder đó
```

---

## 📌 CHECKLIST

- [ ] Hiểu: Vấn đề (constructor quá nhiều params)
- [ ] Hiểu: Builder = set từng bước
- [ ] Hiểu: Chaining (gọi liên tiếp)
- [ ] Hiểu: Build() = tạo object cuối cùng
- [ ] Hiểu: Khi nào dùng (object phức tạp, nhiều optional)
