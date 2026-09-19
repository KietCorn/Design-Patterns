# 10 Design Patterns Phổ Biến Nhất

Tập hợp 10 design patterns cơ bản và thường dùng nhất trong phát triển phần mềm. Mỗi pattern bao gồm: mục đích, khi nào dùng, cách dùng, và 3 bài toán thực tế với code mẫu.

## Danh Sách Patterns

### Creational Patterns (Tạo Object)

| # | Pattern | Mục Đích | Khi Dùng |
|---|---------|---------|---------|
| 1 | [Singleton](01-singleton.md) | Một class chỉ có 1 instance | Database connection, Logger, Config manager |
| 2 | [Factory](02-factory.md) | Tạo object mà không chỉ định class cụ thể | Multiple object types, Runtime decisions |
| 3 | [Builder](03-builder.md) | Tạo object phức tạp từng bước | Object có nhiều thuộc tính optional |
| 9 | [Abstract Factory](09-abstract-factory.md) | Tạo họ object liên quan | Cross-platform UI, DB abstraction |

### Behavioral Patterns (Hành Vi)

| # | Pattern | Mục Đích | Khi Dùng |
|---|---------|---------|---------|
| 4 | [Strategy](04-strategy.md) | Chọn algorithm tại runtime | Multiple algorithms, Avoid if-else chains |
| 5 | [Observer](05-observer.md) | Notify observers khi state thay đổi | Event handling, Real-time updates |

### Structural Patterns (Kết Cấu)

| # | Pattern | Mục Đích | Khi Dùng |
|---|---------|---------|---------|
| 6 | [Decorator](06-decorator.md) | Thêm functionality vào object động | Add features without modification |
| 7 | [Adapter](07-adapter.md) | Chuyển đổi interface incompatible | Legacy system integration, Format conversion |
| 8 | [Proxy](08-proxy.md) | Kiểm soát truy cập tới object | Lazy loading, Access control, Logging |
| 10 | [Facade](10-facade.md) | Simplified interface cho subsystem phức tạp | Reduce complexity, Hide subsystem details |

---

## Cách Dùng

### 1. **Singleton** - Tạo Instance Duy Nhất
- ✅ Quản lý resource duy nhất (DB, Logger)
- ✅ Đảm bảo single point of access

### 2. **Factory** - Tạo Object Linh Hoạt
- ✅ Loại bỏ dependency vào concrete classes
- ✅ Tập trung logic tạo object

### 3. **Builder** - Tạo Object Phức Tạp
- ✅ Xây dựng object theo bước
- ✅ Đơn giản hóa constructor với nhiều parameters

### 4. **Strategy** - Chọn Thuật Toán
- ✅ Thay đổi algorithm tại runtime
- ✅ Loại bỏ if-else chains

### 5. **Observer** - Cập Nhật Tự Động
- ✅ Event-driven architecture
- ✅ Model-View pattern

### 6. **Decorator** - Thêm Chức Năng Động
- ✅ Không thay đổi source code gốc
- ✅ Kết hợp nhiều features linh hoạt

### 7. **Adapter** - Chuyển Đổi Interface
- ✅ Tích hợp legacy code
- ✅ Bridge giữa 2 interfaces khác nhau

### 8. **Proxy** - Kiểm Soát Truy Cập
- ✅ Lazy loading resource nặng
- ✅ Access control & Logging

### 9. **Abstract Factory** - Tạo Họ Object
- ✅ Cross-platform UI (Windows/Mac)
- ✅ Database abstraction (SQL/Mongo)

### 10. **Facade** - Giản Lược Interface
- ✅ Ẩn complexity của subsystem
- ✅ Unified API cho phức tạp

---

## Phân Loại Nhanh

**Khi cần tạo object:** Singleton, Factory, Builder

**Khi cần thay đổi hành vi:** Strategy, Abstract Factory

**Khi cần thêm chức năng:** Decorator, Adapter, Proxy, Facade

**Khi cần giao tiếp object:** Observer

---

## File Structure

```
design/
├── 01-singleton.md      # One instance only
├── 02-factory.md        # Create objects dynamically
├── 03-builder.md        # Build complex objects step by step
├── 04-strategy.md       # Choose algorithm at runtime
├── 05-observer.md       # Notify on state changes
├── 06-decorator.md      # Add features dynamically
├── 07-adapter.md        # Convert incompatible interfaces
├── 08-proxy.md          # Control access to objects
├── 09-abstract-factory.md # Create families of related objects
├── 10-facade.md         # Simplify complex subsystems
└── README.md            # This file
```

---

## Tips Thực Hành

1. **Đừng dùng quá nhiều patterns** - KISS (Keep It Simple, Stupid)
2. **Pattern là tool, không phải mục đích** - Chỉ dùng khi cần thiết
3. **Combo patterns hiệu quả** - Ví dụ: Factory + Strategy
4. **Refactor khi cần** - Thêm pattern từ từ khi code phức tạp

---

*Mỗi file chứa 3 bài toán thực tế + code mẫu đơn giản nhưng đầy đủ để hiểu và triển khai.*
