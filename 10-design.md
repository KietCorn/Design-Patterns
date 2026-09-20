# 10 Design Pattern (C#) - Bản học thuộc

Mỗi pattern gồm: **định nghĩa ngắn**, **khung cần nhớ**, **3 bài toán** (code cùng một khuôn, chỉ đổi tên).
Đầu file code nhớ thêm: `using System;` và `using System.Collections.Generic;` (khi dùng `List`, `Dictionary`).

---

## 1. Singleton

**Định nghĩa:** Đảm bảo class chỉ có **đúng một đối tượng** và cung cấp một điểm truy cập chung để lấy nó.

**Khung:** biến `private static instance` + constructor `private` + hàm `public static GetInstance()` ("chưa có thì tạo, có rồi thì trả về").

### Ví dụ 1: Logger
**Bài toán:** Nhiều nơi cần ghi log, dùng chung một Logger duy nhất để log không rời rạc.

```csharp
class Logger
{
    private static Logger instance;
    private Logger() { }

    public static Logger GetInstance()
    {
        if (instance == null) instance = new Logger();
        return instance;
    }

    public void Log(string msg) => Console.WriteLine("LOG: " + msg);
}
```

### Ví dụ 2: AppConfig
**Bài toán:** Mọi màn hình phải đọc cùng một bộ cấu hình để không bị lệch nhau.

```csharp
class AppConfig
{
    private static AppConfig instance;
    private AppConfig() { }

    public string AppName = "MyShop";

    public static AppConfig GetInstance()
    {
        if (instance == null) instance = new AppConfig();
        return instance;
    }
}
```

### Ví dụ 3: DatabaseConnection (nhiều luồng)
**Bài toán:** Nhiều người dùng truy cập cùng lúc, chỉ được có một kết nối database chung. Thêm `lock` để hai luồng không cùng tạo hai đối tượng.

```csharp
class DatabaseConnection
{
    private static DatabaseConnection instance;
    private static object lockObj = new object();
    private DatabaseConnection() { }

    public static DatabaseConnection GetInstance()
    {
        lock (lockObj)
        {
            if (instance == null) instance = new DatabaseConnection();
        }
        return instance;
    }

    public void Query(string sql) => Console.WriteLine("Chay: " + sql);
}
```

**Khác nhau:** VD3 chỉ thêm `lockObj` và bọc `if` trong `lock`. `return instance;` nằm **ngoài** `lock`.

---

## 2. Factory (Simple Factory)

**Định nghĩa:** Gom việc **tạo đối tượng** vào một chỗ. Client đưa tên loại, factory `new` class phù hợp và trả về qua interface.

**Khung:** interface sản phẩm → 2 class cài đặt → class `...Factory` có `static Create(string type)`: 2 câu `if` + `return new`, cuối cùng `return null`.

### Ví dụ 1: Notification
**Bài toán:** Gửi thông báo qua Email hoặc SMS theo lựa chọn, không để code khắp nơi tự `new`.

```csharp
interface INotification { void Send(string msg); }
class EmailNotification : INotification { public void Send(string msg) => Console.WriteLine("Email: " + msg); }
class SmsNotification : INotification { public void Send(string msg) => Console.WriteLine("SMS: " + msg); }

class NotificationFactory
{
    public static INotification Create(string type)
    {
        if (type == "email") return new EmailNotification();
        if (type == "sms") return new SmsNotification();
        return null;
    }
}
```

### Ví dụ 2: Shape
**Bài toán:** Phần mềm vẽ hình tròn hoặc vuông theo lựa chọn người dùng.

```csharp
interface IShape { void Draw(); }
class Circle : IShape { public void Draw() => Console.WriteLine("Ve hinh tron"); }
class Square : IShape { public void Draw() => Console.WriteLine("Ve hinh vuong"); }

class ShapeFactory
{
    public static IShape Create(string type)
    {
        if (type == "circle") return new Circle();
        if (type == "square") return new Square();
        return null;
    }
}
```

### Ví dụ 3: Vehicle
**Bài toán:** Dịch vụ giao hàng chọn xe máy hoặc ô tô tùy đơn hàng.

```csharp
interface IVehicle { void Deliver(); }
class Bike : IVehicle { public void Deliver() => Console.WriteLine("Giao bang xe may"); }
class Car : IVehicle { public void Deliver() => Console.WriteLine("Giao bang o to"); }

class VehicleFactory
{
    public static IVehicle Create(string type)
    {
        if (type == "bike") return new Bike();
        if (type == "car") return new Car();
        return null;
    }
}
```

*(Nếu đề ghi "Factory Method" thì xem Phụ lục A.)*

---

## 3. Abstract Factory

**Định nghĩa:** Tạo ra **cả một họ** đối tượng liên quan, đảm bảo các sản phẩm cùng họ luôn khớp nhau. Khác Factory: factory này có **nhiều hàm Create**.

**Khung:** 2 interface sản phẩm → 4 sản phẩm cụ thể (2 họ × 2 loại) → interface factory có 2 hàm `Create` → 2 factory cụ thể, mỗi cái `return new` sản phẩm của họ mình.

### Ví dụ 1: Giao diện Windows / Mac
**Bài toán:** Nút và ô tick phải cùng kiểu hệ điều hành, không được trộn nút Windows với ô tick Mac.

```csharp
interface IButton { void Draw(); }
interface ICheckbox { void Draw(); }

class WindowsButton : IButton { public void Draw() => Console.WriteLine("Nut Windows"); }
class WindowsCheckbox : ICheckbox { public void Draw() => Console.WriteLine("O tick Windows"); }
class MacButton : IButton { public void Draw() => Console.WriteLine("Nut Mac"); }
class MacCheckbox : ICheckbox { public void Draw() => Console.WriteLine("O tick Mac"); }

interface IGuiFactory
{
    IButton CreateButton();
    ICheckbox CreateCheckbox();
}

class WindowsFactory : IGuiFactory
{
    public IButton CreateButton() => new WindowsButton();
    public ICheckbox CreateCheckbox() => new WindowsCheckbox();
}

class MacFactory : IGuiFactory
{
    public IButton CreateButton() => new MacButton();
    public ICheckbox CreateCheckbox() => new MacCheckbox();
}
```

### Ví dụ 2: Nội thất Hiện đại / Cổ điển
**Bài toán:** Khách chọn phong cách thì ghế và bàn phải cùng phong cách.

```csharp
interface IChair { void Sit(); }
interface ITable { void Put(); }

class ModernChair : IChair { public void Sit() => Console.WriteLine("Ghe hien dai"); }
class ModernTable : ITable { public void Put() => Console.WriteLine("Ban hien dai"); }
class ClassicChair : IChair { public void Sit() => Console.WriteLine("Ghe co dien"); }
class ClassicTable : ITable { public void Put() => Console.WriteLine("Ban co dien"); }

interface IFurnitureFactory
{
    IChair CreateChair();
    ITable CreateTable();
}

class ModernFactory : IFurnitureFactory
{
    public IChair CreateChair() => new ModernChair();
    public ITable CreateTable() => new ModernTable();
}

class ClassicFactory : IFurnitureFactory
{
    public IChair CreateChair() => new ClassicChair();
    public ITable CreateTable() => new ClassicTable();
}
```

### Ví dụ 3: Database SQL Server / PostgreSQL
**Bài toán:** Mỗi loại database có bộ kết nối và bộ truy vấn riêng, không được trộn lẫn.

```csharp
interface IConnection { void Open(); }
interface IQuery { void Run(string sql); }

class SqlServerConnection : IConnection { public void Open() => Console.WriteLine("Mo SQL Server"); }
class SqlServerQuery : IQuery { public void Run(string sql) => Console.WriteLine("SQL Server: " + sql); }
class PostgresConnection : IConnection { public void Open() => Console.WriteLine("Mo PostgreSQL"); }
class PostgresQuery : IQuery { public void Run(string sql) => Console.WriteLine("PostgreSQL: " + sql); }

interface IDatabaseFactory
{
    IConnection CreateConnection();
    IQuery CreateQuery();
}

class SqlServerFactory : IDatabaseFactory
{
    public IConnection CreateConnection() => new SqlServerConnection();
    public IQuery CreateQuery() => new SqlServerQuery();
}

class PostgresFactory : IDatabaseFactory
{
    public IConnection CreateConnection() => new PostgresConnection();
    public IQuery CreateQuery() => new PostgresQuery();
}
```

---

## 4. Builder

**Định nghĩa:** Tạo đối tượng phức tạp **từng bước một** thay vì một constructor dài nhiều tham số.

**Khung:** class sản phẩm (các trường `public`) → class builder giữ sản phẩm → mỗi thành phần một hàm `SetXxx` (gán, rồi `return this;`) → hàm `Build()` trả về sản phẩm. Kiểu trả về của `SetXxx` là **tên class builder**, không phải `void`.

### Ví dụ 1: Computer
**Bài toán:** Máy tính có nhiều linh kiện, lắp từng linh kiện theo tên cho dễ đọc, không nhầm thứ tự.

```csharp
class Computer { public string Cpu, Ram, Ssd; }

class ComputerBuilder
{
    private Computer computer = new Computer();

    public ComputerBuilder SetCpu(string cpu) { computer.Cpu = cpu; return this; }
    public ComputerBuilder SetRam(string ram) { computer.Ram = ram; return this; }
    public ComputerBuilder SetSsd(string ssd) { computer.Ssd = ssd; return this; }
    public Computer Build() => computer;
}
```

### Ví dụ 2: Email
**Bài toán:** Email có nhiều phần (người nhận, tiêu đề, nội dung), phần nào cần mới đặt.

```csharp
class Email { public string To, Subject, Body; }

class EmailBuilder
{
    private Email email = new Email();

    public EmailBuilder SetTo(string to) { email.To = to; return this; }
    public EmailBuilder SetSubject(string subject) { email.Subject = subject; return this; }
    public EmailBuilder SetBody(string body) { email.Body = body; return this; }
    public Email Build() => email;
}
```

### Ví dụ 3: Pizza
**Bài toán:** Pizza có kích cỡ, phô mai, topping tùy khách chọn.

```csharp
class Pizza { public string Size, Cheese, Topping; }

class PizzaBuilder
{
    private Pizza pizza = new Pizza();

    public PizzaBuilder SetSize(string size) { pizza.Size = size; return this; }
    public PizzaBuilder SetCheese(string cheese) { pizza.Cheese = cheese; return this; }
    public PizzaBuilder SetTopping(string topping) { pizza.Topping = topping; return this; }
    public Pizza Build() => pizza;
}
```

*(Nếu đề yêu cầu Builder có Director thì xem Phụ lục B.)*

---

## 5. Strategy

**Định nghĩa:** Đóng gói mỗi **cách làm** thành một class riêng cùng interface. Context giữ một strategy và có thể **đổi lúc chạy**, không cần sửa Context.

**Khung:** interface một hàm → 2 class cụ thể (mỗi class một dòng `=>`) → Context có 4 dòng: field kiểu **interface**, constructor, hàm `Set...`, hàm gọi `strategy.Hàm(...)`.

### Ví dụ 1: Thanh toán
**Bài toán:** Giỏ hàng thanh toán bằng thẻ hoặc tiền mặt, thêm cách mới không phải sửa `Cart`.

```csharp
interface IPayment { void Pay(int money); }
class CardPay : IPayment { public void Pay(int money) => Console.WriteLine("Card: " + money); }
class CashPay : IPayment { public void Pay(int money) => Console.WriteLine("Cash: " + money); }

class Cart
{
    private IPayment strategy;
    public Cart(IPayment s) => strategy = s;
    public void SetPayment(IPayment s) => strategy = s;
    public void Checkout(int money) => strategy.Pay(money);
}
```

### Ví dụ 2: Phí vận chuyển
**Bài toán:** Phí ship khác nhau giữa giao thường và hỏa tốc, `Order` không chứa `if`.

```csharp
interface IShipping { int Fee(int weight); }
class StandardShip : IShipping { public int Fee(int weight) => weight * 10; }
class ExpressShip : IShipping { public int Fee(int weight) => weight * 20; }

class Order
{
    private IShipping strategy;
    public Order(IShipping s) => strategy = s;
    public void SetShipping(IShipping s) => strategy = s;
    public int GetFee(int weight) => strategy.Fee(weight);
}
```

### Ví dụ 3: Giảm giá
**Bài toán:** Khách thường và khách VIP giảm giá khác nhau, thêm loại khách mới không sửa `Shop`.

```csharp
interface IDiscount { int Apply(int price); }
class NormalDiscount : IDiscount { public int Apply(int price) => price; }
class VipDiscount : IDiscount { public int Apply(int price) => price * 80 / 100; }

class Shop
{
    private IDiscount strategy;
    public Shop(IDiscount s) => strategy = s;
    public void SetDiscount(IDiscount s) => strategy = s;
    public int Pay(int price) => strategy.Apply(price);
}
```

---

## 6. Observer

**Định nghĩa:** Quan hệ **một-nhiều**: khi Subject thay đổi, tất cả Observer đã đăng ký được **tự động báo**. (Giống đăng ký kênh YouTube.)

**Khung:** `IObserver` có hàm `Update` → Subject có `List<IObserver>` (nhớ `= new List<IObserver>()`), `Subscribe`, `Unsubscribe`, hàm đổi dữ liệu (gán rồi gọi `Notify()`), hàm `Notify()` dùng `foreach` gọi `o.Update(...)` → Observer cụ thể `: IObserver`.

### Ví dụ 1: Giá cổ phiếu
**Bài toán:** Nhiều nhà đầu tư theo dõi một cổ phiếu, giá đổi thì tự báo cho tất cả.

```csharp
interface IObserver { void Update(int price); }

class Stock
{
    private List<IObserver> observers = new List<IObserver>();
    private int price;

    public void Subscribe(IObserver o) => observers.Add(o);
    public void Unsubscribe(IObserver o) => observers.Remove(o);
    public void SetPrice(int p) { price = p; Notify(); }

    private void Notify()
    {
        foreach (IObserver o in observers) o.Update(price);
    }
}

class Investor : IObserver
{
    private string name;
    public Investor(string n) => name = n;
    public void Update(int price) => Console.WriteLine(name + ": gia moi " + price);
}
```

### Ví dụ 2: Kênh YouTube
**Bài toán:** Kênh có video mới thì báo cho tất cả người đăng ký.

```csharp
interface IObserver { void Update(string video); }

class Channel
{
    private List<IObserver> observers = new List<IObserver>();
    private string video;

    public void Subscribe(IObserver o) => observers.Add(o);
    public void Unsubscribe(IObserver o) => observers.Remove(o);
    public void SetVideo(string v) { video = v; Notify(); }

    private void Notify()
    {
        foreach (IObserver o in observers) o.Update(video);
    }
}

class Subscriber : IObserver
{
    private string name;
    public Subscriber(string n) => name = n;
    public void Update(string video) => Console.WriteLine(name + " nhan: " + video);
}
```

### Ví dụ 3: Trạm thời tiết
**Bài toán:** Nhiệt độ đổi thì mọi màn hình hiển thị phải cập nhật theo.

```csharp
interface IObserver { void Update(int temp); }

class WeatherStation
{
    private List<IObserver> observers = new List<IObserver>();
    private int temp;

    public void Subscribe(IObserver o) => observers.Add(o);
    public void Unsubscribe(IObserver o) => observers.Remove(o);
    public void SetTemp(int t) { temp = t; Notify(); }

    private void Notify()
    {
        foreach (IObserver o in observers) o.Update(temp);
    }
}

class Display : IObserver
{
    private string name;
    public Display(string n) => name = n;
    public void Update(int temp) => Console.WriteLine(name + ": " + temp + " do");
}
```

*(Cách ngắn hơn bằng `event` của C#: xem Phụ lục C.)*

---

## 7. Decorator

**Định nghĩa:** **Thêm chức năng** cho đối tượng lúc chạy bằng cách bọc nó trong các lớp vỏ cùng interface, không sửa class gốc và không phải tạo nhiều class con. (Giống mặc thêm áo.)

**Khung:** interface → đối tượng gốc → Decorator vừa **`: Interface`** vừa **giữ một `Interface` bên trong**. Hàm của Decorator gọi `inner.Hàm()` **rồi cộng thêm** phần của mình.

### Ví dụ 1: Cà phê
**Bài toán:** Thêm sữa, đường tùy khách, không muốn tạo class cho mọi tổ hợp.

```csharp
interface ICoffee { int GetCost(); }
class SimpleCoffee : ICoffee { public int GetCost() => 20; }

class MilkDecorator : ICoffee
{
    private ICoffee coffee;
    public MilkDecorator(ICoffee c) => coffee = c;
    public int GetCost() => coffee.GetCost() + 5;
}

class SugarDecorator : ICoffee
{
    private ICoffee coffee;
    public SugarDecorator(ICoffee c) => coffee = c;
    public int GetCost() => coffee.GetCost() + 2;
}
```

### Ví dụ 2: Pizza
**Bài toán:** Thêm phô mai, xúc xích tùy khách, giá cộng dồn.

```csharp
interface IPizza { int GetCost(); }
class BasicPizza : IPizza { public int GetCost() => 100; }

class CheeseDecorator : IPizza
{
    private IPizza pizza;
    public CheeseDecorator(IPizza p) => pizza = p;
    public int GetCost() => pizza.GetCost() + 20;
}

class SausageDecorator : IPizza
{
    private IPizza pizza;
    public SausageDecorator(IPizza p) => pizza = p;
    public int GetCost() => pizza.GetCost() + 30;
}
```

### Ví dụ 3: Vé xem phim
**Bài toán:** Thêm bắp, nước tùy khách, giá vé cộng dồn.

```csharp
interface ITicket { int GetCost(); }
class BasicTicket : ITicket { public int GetCost() => 80; }

class PopcornDecorator : ITicket
{
    private ITicket ticket;
    public PopcornDecorator(ITicket t) => ticket = t;
    public int GetCost() => ticket.GetCost() + 30;
}

class DrinkDecorator : ITicket
{
    private ITicket ticket;
    public DrinkDecorator(ITicket t) => ticket = t;
    public int GetCost() => ticket.GetCost() + 20;
}
```

---

## 8. Adapter

**Định nghĩa:** **Đổi interface** của một class có sẵn thành interface mà client mong đợi, để hai bên không khớp vẫn làm việc được mà không sửa code của bên nào. (Giống đầu chuyển ổ cắm.)

**Khung:** interface **Target** → class **Adaptee** có sẵn (tên hàm khác, giữ nguyên) → Adapter `: Target`, giữ Adaptee, constructor nhận Adaptee, hàm của Target **gọi hàm của Adaptee**.

### Ví dụ 1: Cổng thanh toán Momo
**Bài toán:** App dùng `IPayment.Pay`, nhưng SDK Momo có hàm `MakeTransaction` kiểu `double`, không sửa được SDK.

```csharp
interface IPayment { void Pay(int money); }
class MomoSdk { public void MakeTransaction(double amount) => Console.WriteLine("Momo: " + amount); }

class MomoAdapter : IPayment
{
    private MomoSdk momo;
    public MomoAdapter(MomoSdk m) => momo = m;
    public void Pay(int money) => momo.MakeTransaction(money);
}
```

### Ví dụ 2: Cảm biến nhiệt độ
**Bài toán:** Hệ thống cần độ Celsius, cảm biến cũ chỉ trả về độ Fahrenheit.

```csharp
interface ITemperature { double GetCelsius(); }
class OldSensor { public double GetFahrenheit() => 86; }

class SensorAdapter : ITemperature
{
    private OldSensor sensor;
    public SensorAdapter(OldSensor s) => sensor = s;
    public double GetCelsius() => (sensor.GetFahrenheit() - 32) * 5 / 9;
}
```

### Ví dụ 3: Máy in cũ
**Bài toán:** App dùng `IPrinter.Print`, máy in cũ chỉ có hàm `PrintDocument`.

```csharp
interface IPrinter { void Print(string text); }
class OldPrinter { public void PrintDocument(string doc) => Console.WriteLine("In: " + doc); }

class PrinterAdapter : IPrinter
{
    private OldPrinter printer;
    public PrinterAdapter(OldPrinter p) => printer = p;
    public void Print(string text) => printer.PrintDocument(text);
}
```

*(Dạng dùng kế thừa "Class Adapter": xem Phụ lục D.)*

---

## 9. Proxy

**Định nghĩa:** Đối tượng **đại diện** có cùng interface, đứng trước đối tượng thật để **kiểm soát truy cập** (trì hoãn tạo, kiểm tra quyền, cache). (Giống thư ký của giám đốc.)

**Khung:** interface → đối tượng thật `: Interface` → Proxy `: Interface`, **tự giữ đối tượng thật**, hàm của Proxy có **`if` kiểm tra** rồi mới gọi đối tượng thật.

### Ví dụ 1: Tải ảnh (Virtual Proxy)
**Bài toán:** Trang web có nhiều ảnh lớn, chỉ tải ảnh khi thực sự cần hiển thị.

```csharp
interface IImage { void Display(); }

class RealImage : IImage
{
    private string file;
    public RealImage(string f) { file = f; Console.WriteLine("Tai anh: " + f); }
    public void Display() => Console.WriteLine("Hien thi: " + file);
}

class ImageProxy : IImage
{
    private string file;
    private RealImage real;
    public ImageProxy(string f) => file = f;

    public void Display()
    {
        if (real == null) real = new RealImage(file);
        real.Display();
    }
}
```

### Ví dụ 2: Kiểm tra quyền (Protection Proxy)
**Bài toán:** Báo cáo lương chỉ admin được xem, tách logic bảo mật khỏi class báo cáo.

```csharp
interface IReport { void Read(); }
class SalaryReport : IReport { public void Read() => Console.WriteLine("Noi dung bang luong"); }

class ReportProxy : IReport
{
    private SalaryReport report = new SalaryReport();
    private string role;
    public ReportProxy(string r) => role = r;

    public void Read()
    {
        if (role == "admin") report.Read();
        else Console.WriteLine("Khong co quyen");
    }
}
```

### Ví dụ 3: Lưu kết quả truy vấn (Caching Proxy)
**Bài toán:** Truy vấn database chậm, hỏi lại cùng dữ liệu thì trả từ bộ nhớ, không truy vấn lại.

```csharp
interface IDataService { string GetData(string key); }

class DataService : IDataService
{
    public string GetData(string key)
    {
        Console.WriteLine("Truy van database: " + key);
        return "Du lieu " + key;
    }
}

class CacheProxy : IDataService
{
    private DataService service = new DataService();
    private Dictionary<string, string> cache = new Dictionary<string, string>();

    public string GetData(string key)
    {
        if (!cache.ContainsKey(key)) cache[key] = service.GetData(key);
        return cache[key];
    }
}
```

---

## 10. Facade

**Định nghĩa:** Cung cấp **một interface đơn giản** để dùng cả hệ thống con phức tạp. Client gọi **một hàm** thay vì tự gọi và sắp xếp nhiều class con. (Giống nút "Bắt đầu xem phim" trên remote.)

**Khung:** 3 class con (mỗi class một hàm in ra) → Facade giữ các class con (`private ... = new ...();`) → một hàm public gọi lần lượt các hàm của class con **đúng thứ tự**. Không cần interface.

### Ví dụ 1: Rạp chiếu phim tại nhà
**Bài toán:** Xem phim phải giảm đèn, bật máy chiếu, bật loa đúng thứ tự, gom thành một hàm.

```csharp
class Lights { public void Dim() => Console.WriteLine("Den: giam sang"); }
class Projector { public void On() => Console.WriteLine("May chieu: bat"); }
class Speaker { public void On() => Console.WriteLine("Loa: bat"); }

class HomeTheaterFacade
{
    private Lights lights = new Lights();
    private Projector projector = new Projector();
    private Speaker speaker = new Speaker();

    public void WatchMovie()
    {
        lights.Dim();
        projector.On();
        speaker.On();
    }
}
```

### Ví dụ 2: Đặt hàng online
**Bài toán:** Đặt hàng gồm kiểm tra kho, thanh toán, giao hàng, gom thành một hàm để nơi nào cũng gọi giống nhau.

```csharp
class Inventory { public void Check(string item) => Console.WriteLine("Kho: kiem tra " + item); }
class Payment { public void Charge(string item) => Console.WriteLine("Thanh toan: " + item); }
class Shipping { public void Send(string item) => Console.WriteLine("Giao hang: " + item); }

class OrderFacade
{
    private Inventory inventory = new Inventory();
    private Payment payment = new Payment();
    private Shipping shipping = new Shipping();

    public void PlaceOrder(string item)
    {
        inventory.Check(item);
        payment.Charge(item);
        shipping.Send(item);
    }
}
```

### Ví dụ 3: Khởi động máy tính
**Bài toán:** Bật máy cần khởi động CPU, nạp RAM, đọc ổ cứng, người dùng chỉ cần một lệnh `Start`.

```csharp
class Cpu { public void Start() => Console.WriteLine("CPU: khoi dong"); }
class Ram { public void Load() => Console.WriteLine("RAM: nap he dieu hanh"); }
class HardDrive { public void Read() => Console.WriteLine("O cung: doc du lieu"); }

class ComputerFacade
{
    private Cpu cpu = new Cpu();
    private Ram ram = new Ram();
    private HardDrive drive = new HardDrive();

    public void Start()
    {
        cpu.Start();
        ram.Load();
        drive.Read();
    }
}
```

---

## Bảng nhận biết nhanh

| Pattern | Từ khóa bài toán | Dấu hiệu trong code |
|---|---|---|
| Singleton | Chỉ được có **một** đối tượng | `private static instance` + constructor `private` + `GetInstance()` |
| Factory | Tạo đối tượng theo **tên loại** | `Create(string type)` có `if` + `return new` |
| Abstract Factory | Tạo **cả họ** đồng bộ | Interface factory có **nhiều** hàm `CreateXxx()` |
| Builder | Lắp **từng bước** | `SetXxx` có `return this` + `Build()` |
| Strategy | **Đổi cách làm** lúc chạy | Context giữ `IStrategy` + hàm `Set...` |
| Observer | **Báo tin** một-nhiều | `List<IObserver>` + `Notify()` gọi `Update()` |
| Decorator | **Thêm** chức năng, bọc chồng | Vừa `: IX` vừa giữ `IX`, gọi `inner` rồi cộng thêm |
| Adapter | **Đổi** interface cho khớp | `: Target`, giữ Adaptee, hàm Target gọi hàm Adaptee |
| Proxy | **Kiểm soát truy cập** | `: IX`, tự giữ đối tượng thật, có `if` kiểm tra |
| Facade | **Đơn giản hóa** hệ thống con | Một hàm gọi lần lượt nhiều class con, không có interface |

**Phân biệt 3 pattern hay bị hỏi (cùng "bọc" một đối tượng):**
- **Decorator:** giữ nguyên interface, **thêm** chức năng, client tự bọc nhiều lớp.
- **Proxy:** giữ nguyên interface, **kiểm soát truy cập**, tự quản lý đối tượng thật.
- **Adapter:** **đổi** interface để hai bên khớp nhau.

**Phân biệt nhanh khác:** Factory quyết định *tạo cái gì*; Builder quyết định *lắp như thế nào*; Strategy *đổi cách làm* (Context giữ **một**); Observer *báo tin* (Subject giữ **nhiều**).

## Lỗi hay mất điểm khi viết tay

- Quên `: TênInterface` sau tên class cài đặt.
- Quên `public` ở hàm cài đặt interface.
- Khai báo field bằng **class cụ thể** thay vì **interface** (Strategy, Decorator, Proxy).
- Quên khởi tạo `List` / `Dictionary` (`= new ...`) hoặc quên `using System.Collections.Generic;`.
- Decorator quên gọi `inner.Hàm()`, Observer quên gọi `Notify()`.
- Chuỗi phải dùng nháy kép `"..."`, nháy đơn `'a'` chỉ dành cho một ký tự.

---

## Phụ lục: các biến thể (chỉ học khi đề yêu cầu)

### A. Factory Method (mỗi sản phẩm một creator)

**Khi nào:** đề ghi "Factory Method", hoặc muốn thêm loại mới mà **không sửa** factory cũ.

```csharp
interface IDocument { void Open(); }
class WordDocument : IDocument { public void Open() => Console.WriteLine("Mo Word"); }
class PdfDocument : IDocument { public void Open() => Console.WriteLine("Mo PDF"); }

interface IDocumentCreator { IDocument CreateDocument(); }
class WordCreator : IDocumentCreator { public IDocument CreateDocument() => new WordDocument(); }
class PdfCreator : IDocumentCreator { public IDocument CreateDocument() => new PdfDocument(); }
```

### B. Builder theo GoF (có Director)

**Khi nào:** đề yêu cầu có `Director`. Director giữ **thứ tự các bước**, builder quyết định **mỗi bước làm thế nào**.

```csharp
class House
{
    public string Desc = "";
    public void Add(string part) => Desc += part + "; ";
}

interface IHouseBuilder { void BuildWalls(); void BuildRoof(); House GetHouse(); }

class WoodenHouseBuilder : IHouseBuilder
{
    private House house = new House();
    public void BuildWalls() => house.Add("Tuong go");
    public void BuildRoof() => house.Add("Mai ngoi");
    public House GetHouse() => house;
}

class Director
{
    public void Construct(IHouseBuilder b)
    {
        b.BuildWalls();
        b.BuildRoof();
    }
}
```

### C. Observer bằng `event` (ngắn nhất)

**Khi nào:** đề cho phép dùng tính năng có sẵn của C#. Đăng ký bằng `+=`, hủy bằng `-=`. Nếu đề vẽ sơ đồ `Attach/Detach/Notify` thì dùng dạng cổ điển ở mục 6.

```csharp
class Order
{
    public event Action<string> StatusChanged;

    public void SetStatus(string s)
    {
        if (StatusChanged != null) StatusChanged(s);
    }
}
// Đăng ký: order.StatusChanged += s => Console.WriteLine("Email: " + s);
```

### D. Class Adapter (dùng kế thừa)

**Khi nào:** đề ghi "Class Adapter" hoặc "dùng kế thừa". Class kế thừa viết **trước**, interface viết **sau** dấu phẩy.

```csharp
interface ILogger { void Log(string msg); }
class OldLogger { public void WriteMessage(string text) => Console.WriteLine("OLD: " + text); }

class LoggerAdapter : OldLogger, ILogger
{
    public void Log(string msg) => WriteMessage(msg);
}
```
