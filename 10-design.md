# 10 Design Pattern (C#) - Bản học thuộc

Mỗi pattern gồm: **Mục đích**, **Khi nào dùng**, **Cách sử dụng** (các bước viết code), rồi **3 bài toán** (code cùng một khuôn, chỉ đổi tên).

**Quy ước của file này:**
- Không dùng `Console.WriteLine`. Hàm **trả về chuỗi** (`=> "..."`) nên kiểu trả về là `string`, không phải `void`.
- Interface và class cài đặt phải **cùng kiểu trả về** (interface `string Sit();` thì class cũng `public string Sit()`).
- Khi dùng `List` / `Dictionary` nhớ `using System.Collections.Generic;`.

---

## 1. Singleton

**Mục đích:** Đảm bảo class chỉ có **đúng một đối tượng** và có một điểm truy cập chung để lấy nó.

**Khi nào dùng:**
- Cả hệ thống chỉ cần một đối tượng dùng chung (log, cấu hình, kết nối database)
- Tạo nhiều đối tượng sẽ gây lệch dữ liệu hoặc tốn tài nguyên

**Cách sử dụng:**
1. Khai báo biến `private static instance` để giữ đối tượng duy nhất
2. Để constructor `private` để bên ngoài không tự `new` được
3. Viết hàm `public static GetInstance()`: chưa có thì tạo, có rồi thì trả về
4. Nếu chạy nhiều luồng, bọc phần kiểm tra trong `lock`

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

    public string Log(string msg) => "LOG: " + msg;
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

    public string Query(string sql) => "Chay: " + sql;
}
```

**Khác nhau:** VD3 chỉ thêm `lockObj` và bọc `if` trong `lock`. `return instance;` nằm **ngoài** `lock`.

---

## 2. Factory (Simple Factory)

**Mục đích:** Gom việc **tạo đối tượng** vào một chỗ. Client chỉ đưa tên loại, không cần biết class cụ thể.

**Khi nào dùng:**
- Có nhiều loại đối tượng cùng interface, loại nào được tạo tùy lựa chọn lúc chạy
- Muốn tránh `new` lặp ở nhiều nơi và dễ thêm loại mới

**Cách sử dụng:**
1. Tạo interface chung cho sản phẩm
2. Viết các class cụ thể cài đặt interface
3. Viết class `...Factory` có hàm `static Create(string type)`: 2 câu `if` chọn loại và `return new ...`, cuối cùng `return null`
4. Client gọi `Factory.Create("...")` và chỉ làm việc qua interface

### Ví dụ 1: Notification
**Bài toán:** Gửi thông báo qua Email hoặc SMS theo lựa chọn, không để code khắp nơi tự `new`.

```csharp
interface INotification { string Send(string msg); }
class EmailNotification : INotification { public string Send(string msg) => "Email: " + msg; }
class SmsNotification : INotification { public string Send(string msg) => "SMS: " + msg; }

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
interface IShape { string Draw(); }
class Circle : IShape { public string Draw() => "Ve hinh tron"; }
class Square : IShape { public string Draw() => "Ve hinh vuong"; }

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
interface IVehicle { string Deliver(); }
class Bike : IVehicle { public string Deliver() => "Giao bang xe may"; }
class Car : IVehicle { public string Deliver() => "Giao bang o to"; }

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

**Mục đích:** Tạo ra **cả một họ** đối tượng liên quan, đảm bảo các sản phẩm cùng họ luôn khớp nhau.

**Khi nào dùng:**
- Có nhiều họ sản phẩm (Windows/Mac, Hiện đại/Cổ điển), mỗi họ gồm nhiều loại sản phẩm
- Các sản phẩm phải cùng họ, không được trộn lẫn

**Cách sử dụng:**
1. Tạo một interface cho mỗi loại sản phẩm (A, B)
2. Viết sản phẩm cụ thể cho từng họ: họ 1 (A1, B1), họ 2 (A2, B2)
3. Tạo interface factory có **nhiều hàm Create** (`CreateA()`, `CreateB()`)
4. Mỗi họ một factory cụ thể, `return new` các sản phẩm của họ mình
5. Client chọn một factory, rồi lấy mọi sản phẩm từ factory đó

### Ví dụ 1: Giao diện Windows / Mac
**Bài toán:** Nút và ô tick phải cùng kiểu hệ điều hành, không được trộn nút Windows với ô tick Mac.

```csharp
interface IButton { string Draw(); }
interface ICheckbox { string Draw(); }

class WindowsButton : IButton { public string Draw() => "Nut Windows"; }
class WindowsCheckbox : ICheckbox { public string Draw() => "O tick Windows"; }
class MacButton : IButton { public string Draw() => "Nut Mac"; }
class MacCheckbox : ICheckbox { public string Draw() => "O tick Mac"; }

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
interface IChair { string Sit(); }
interface ITable { string Put(); }

class ModernChair : IChair { public string Sit() => "Ghe hien dai"; }
class ModernTable : ITable { public string Put() => "Ban hien dai"; }
class ClassicChair : IChair { public string Sit() => "Ghe co dien"; }
class ClassicTable : ITable { public string Put() => "Ban co dien"; }

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
interface IConnection { string Open(); }
interface IQuery { string Run(string sql); }

class SqlServerConnection : IConnection { public string Open() => "Mo SQL Server"; }
class SqlServerQuery : IQuery { public string Run(string sql) => "SQL Server: " + sql; }
class PostgresConnection : IConnection { public string Open() => "Mo PostgreSQL"; }
class PostgresQuery : IQuery { public string Run(string sql) => "PostgreSQL: " + sql; }

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

**Mục đích:** Tạo đối tượng phức tạp **từng bước một**, tránh constructor dài nhiều tham số.

**Khi nào dùng:**
- Đối tượng có nhiều thành phần, trong đó có thành phần tùy chọn
- Muốn code tạo đối tượng dễ đọc, không sợ nhầm thứ tự tham số

**Cách sử dụng:**
1. Viết class sản phẩm với các trường `public`
2. Viết class builder, giữ một sản phẩm bên trong (`= new ...`)
3. Mỗi thành phần một hàm `SetXxx`: gán giá trị rồi `return this;` (kiểu trả về là **tên class builder**, không phải `void`)
4. Viết hàm `Build()` trả về sản phẩm
5. Client gọi nối tiếp `.SetA(...).SetB(...).Build()`

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

**Mục đích:** Đóng gói mỗi **cách làm** thành một class riêng để **đổi cách làm lúc chạy** mà không sửa class đang dùng.

**Khi nào dùng:**
- Một việc có nhiều cách thực hiện (thanh toán, tính phí, giảm giá)
- Muốn thay `if-else` / `switch` dài bằng các class riêng, dễ thêm cách mới

**Cách sử dụng:**
1. Tạo interface chung có một hàm
2. Viết mỗi cách làm thành một class cài đặt interface
3. Viết Context gồm 4 dòng: field kiểu **interface**, constructor nhận strategy, hàm `Set...` để đổi, hàm gọi `strategy.Hàm(...)`
4. Client chọn strategy đưa vào Context, muốn đổi thì gọi `Set...`

### Ví dụ 1: Thanh toán
**Bài toán:** Giỏ hàng thanh toán bằng thẻ hoặc tiền mặt, thêm cách mới không phải sửa `Cart`.

```csharp
interface IPayment { string Pay(int money); }
class CardPay : IPayment { public string Pay(int money) => "Card: " + money; }
class CashPay : IPayment { public string Pay(int money) => "Cash: " + money; }

class Cart
{
    private IPayment strategy;
    public Cart(IPayment s) => strategy = s;
    public void SetPayment(IPayment s) => strategy = s;
    public string Checkout(int money) => strategy.Pay(money);
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

**Mục đích:** Khi Subject thay đổi thì **tự động báo** cho tất cả Observer đã đăng ký (quan hệ một-nhiều). *Giống đăng ký kênh YouTube.*

**Khi nào dùng:**
- Một đối tượng đổi thì nhiều đối tượng khác cần cập nhật theo (giá cổ phiếu, video mới, thời tiết)
- Subject không nên biết chi tiết từng người nhận

**Cách sử dụng:**
1. Tạo `IObserver` có hàm `Update`
2. Viết Subject có `List<IObserver>` (nhớ `= new List<IObserver>()`), hàm `Subscribe` và `Unsubscribe`
3. Hàm đổi dữ liệu: gán giá trị rồi gọi `Notify()`. Hàm `Notify()` dùng `foreach` gọi `o.Update(...)`
4. Viết observer cụ thể `: IObserver`, lưu dữ liệu nhận được vào field của mình (`Update` trả về `void`)
5. Client đăng ký observer bằng `Subscribe`

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
    public int Price;
    public void Update(int p) => Price = p;
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
    public string Video;
    public void Update(string v) => Video = v;
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
    public int Temp;
    public void Update(int t) => Temp = t;
}
```

*(Cách ngắn hơn bằng `event` của C#: xem Phụ lục C.)*

---

## 7. Decorator

**Mục đích:** **Thêm chức năng** cho đối tượng lúc chạy bằng cách bọc nó trong các lớp vỏ, không sửa class gốc. *Giống mặc thêm áo.*

**Khi nào dùng:**
- Cần thêm chức năng tùy chọn và kết hợp tự do (sữa, đường, phô mai...)
- Dùng kế thừa sẽ phải tạo quá nhiều class cho từng tổ hợp

**Cách sử dụng:**
1. Tạo interface chung
2. Viết đối tượng gốc cài đặt interface
3. Viết mỗi decorator `: Interface`, có field giữ một `Interface` bên trong và constructor nhận đối tượng bên trong
4. Hàm của decorator gọi `inner.Hàm()` **rồi cộng thêm** phần của mình
5. Client bọc chồng: `new Vo2(new Vo1(new Goc()))`

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

**Mục đích:** **Đổi interface** của class có sẵn thành interface mà client mong đợi, không sửa code gốc. *Giống đầu chuyển ổ cắm.*

**Khi nào dùng:**
- Tích hợp thư viện, SDK bên ngoài hoặc code cũ có tên hàm hay kiểu dữ liệu không khớp
- Không sửa được class gốc, hoặc không muốn sửa code đang dùng

**Cách sử dụng:**
1. Xác định interface **Target** mà client mong đợi
2. Giữ nguyên class **Adaptee** có sẵn (tên hàm khác Target)
3. Viết Adapter `: Target`, có field giữ Adaptee và constructor nhận Adaptee
4. Hàm của Target trong Adapter gọi hàm của Adaptee (có thể kèm đổi dữ liệu)
5. Client chỉ làm việc qua Target

### Ví dụ 1: Cổng thanh toán Momo
**Bài toán:** App dùng `IPayment.Pay`, nhưng SDK Momo có hàm `MakeTransaction` kiểu `double`, không sửa được SDK.

```csharp
interface IPayment { string Pay(int money); }
class MomoSdk { public string MakeTransaction(double amount) => "Momo: " + amount; }

class MomoAdapter : IPayment
{
    private MomoSdk momo;
    public MomoAdapter(MomoSdk m) => momo = m;
    public string Pay(int money) => momo.MakeTransaction(money);
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
interface IPrinter { string Print(string text); }
class OldPrinter { public string PrintDocument(string doc) => "In: " + doc; }

class PrinterAdapter : IPrinter
{
    private OldPrinter printer;
    public PrinterAdapter(OldPrinter p) => printer = p;
    public string Print(string text) => printer.PrintDocument(text);
}
```

*(Dạng dùng kế thừa "Class Adapter": xem Phụ lục D.)*

---

## 9. Proxy

**Mục đích:** Đối tượng **đại diện** đứng trước đối tượng thật để **kiểm soát truy cập**. *Giống thư ký của giám đốc.*

**Khi nào dùng:**
- Đối tượng thật tốn tài nguyên, chỉ muốn tạo khi cần (lazy)
- Cần kiểm tra quyền hoặc lưu kết quả (cache) trước khi gọi đối tượng thật

**Cách sử dụng:**
1. Tạo interface chung
2. Viết đối tượng thật cài đặt interface
3. Viết Proxy `: Interface`, **tự giữ** đối tượng thật (tạo sẵn hoặc tạo khi cần)
4. Hàm của Proxy kiểm tra điều kiện bằng `if` rồi mới gọi đối tượng thật
5. Client dùng Proxy như dùng đối tượng thật, không nhận ra sự khác biệt

### Ví dụ 1: Tải ảnh (Virtual Proxy)
**Bài toán:** Trang web có nhiều ảnh lớn, chỉ tạo ảnh thật khi thực sự cần hiển thị.

```csharp
interface IImage { string Display(); }

class RealImage : IImage
{
    private string file;
    public RealImage(string f) => file = f;
    public string Display() => "Hien thi: " + file;
}

class ImageProxy : IImage
{
    private string file;
    private RealImage real;
    public ImageProxy(string f) => file = f;

    public string Display()
    {
        if (real == null) real = new RealImage(file);
        return real.Display();
    }
}
```

### Ví dụ 2: Kiểm tra quyền (Protection Proxy)
**Bài toán:** Báo cáo lương chỉ admin được xem, tách logic bảo mật khỏi class báo cáo.

```csharp
interface IReport { string Read(); }
class SalaryReport : IReport { public string Read() => "Noi dung bang luong"; }

class ReportProxy : IReport
{
    private SalaryReport report = new SalaryReport();
    private string role;
    public ReportProxy(string r) => role = r;

    public string Read()
    {
        if (role == "admin") return report.Read();
        return "Khong co quyen";
    }
}
```

### Ví dụ 3: Lưu kết quả truy vấn (Caching Proxy)
**Bài toán:** Truy vấn database chậm, hỏi lại cùng dữ liệu thì trả từ bộ nhớ, không truy vấn lại.

```csharp
interface IDataService { string GetData(string key); }
class DataService : IDataService { public string GetData(string key) => "Du lieu " + key; }

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

**Mục đích:** Cung cấp **một interface đơn giản** để dùng cả hệ thống con phức tạp. *Giống nút "Bắt đầu xem phim" trên remote.*

**Khi nào dùng:**
- Một tác vụ phải gọi nhiều class con theo đúng thứ tự
- Muốn client chỉ gọi một hàm, không cần biết chi tiết bên trong

**Cách sử dụng:**
1. Viết các class con, mỗi class một việc nhỏ
2. Viết class Facade, giữ các class con (`private ... = new ...();`)
3. Viết hàm public gọi lần lượt các hàm của class con đúng thứ tự (không cần interface)
4. Client chỉ gọi hàm của Facade

### Ví dụ 1: Rạp chiếu phim tại nhà
**Bài toán:** Xem phim phải giảm đèn, bật máy chiếu, bật loa đúng thứ tự, gom thành một hàm.

```csharp
class Lights { public string Dim() => "Den giam sang"; }
class Projector { public string On() => "May chieu bat"; }
class Speaker { public string On() => "Loa bat"; }

class HomeTheaterFacade
{
    private Lights lights = new Lights();
    private Projector projector = new Projector();
    private Speaker speaker = new Speaker();

    public string WatchMovie() => lights.Dim() + ", " + projector.On() + ", " + speaker.On();
}
```

### Ví dụ 2: Đặt hàng online
**Bài toán:** Đặt hàng gồm kiểm tra kho, thanh toán, giao hàng, gom thành một hàm để nơi nào cũng gọi giống nhau.

```csharp
class Inventory { public string Check(string item) => "Kiem tra kho " + item; }
class Payment { public string Charge(string item) => "Thanh toan " + item; }
class Shipping { public string Send(string item) => "Giao hang " + item; }

class OrderFacade
{
    private Inventory inventory = new Inventory();
    private Payment payment = new Payment();
    private Shipping shipping = new Shipping();

    public string PlaceOrder(string item) => inventory.Check(item) + ", " + payment.Charge(item) + ", " + shipping.Send(item);
}
```

### Ví dụ 3: Khởi động máy tính
**Bài toán:** Bật máy cần khởi động CPU, nạp RAM, đọc ổ cứng, người dùng chỉ cần một lệnh `Start`.

```csharp
class Cpu { public string Start() => "CPU khoi dong"; }
class Ram { public string Load() => "RAM nap he dieu hanh"; }
class HardDrive { public string Read() => "O cung doc du lieu"; }

class ComputerFacade
{
    private Cpu cpu = new Cpu();
    private Ram ram = new Ram();
    private HardDrive drive = new HardDrive();

    public string Start() => cpu.Start() + ", " + ram.Load() + ", " + drive.Read();
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
- Interface khai báo `string` mà class cài đặt viết `void` (hoặc ngược lại): **kiểu trả về phải khớp**.
- Khai báo field bằng **class cụ thể** thay vì **interface** (Strategy, Decorator, Proxy).
- Quên khởi tạo `List` / `Dictionary` (`= new ...`) hoặc quên `using System.Collections.Generic;`.
- Decorator quên gọi `inner.Hàm()`, Observer quên gọi `Notify()`.
- Chuỗi phải dùng nháy kép `"..."`, nháy đơn `'a'` chỉ dành cho một ký tự.

---

## Phụ lục: các biến thể (chỉ học khi đề yêu cầu)

### A. Factory Method (mỗi sản phẩm một creator)

**Khi nào:** đề ghi "Factory Method", hoặc muốn thêm loại mới mà **không sửa** factory cũ.

```csharp
interface IDocument { string Open(); }
class WordDocument : IDocument { public string Open() => "Mo Word"; }
class PdfDocument : IDocument { public string Open() => "Mo PDF"; }

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

**Khi nào:** đề cho phép dùng tính năng có sẵn của C# (cần `using System;`). Đăng ký bằng `+=`, hủy bằng `-=`. Nếu đề vẽ sơ đồ `Attach/Detach/Notify` thì dùng dạng cổ điển ở mục 6.

```csharp
class Order
{
    public event Action<string> StatusChanged;

    public void SetStatus(string s)
    {
        if (StatusChanged != null) StatusChanged(s);
    }
}
// Đăng ký: order.StatusChanged += OnChanged;   (OnChanged là hàm nhận một string)
```

### D. Class Adapter (dùng kế thừa)

**Khi nào:** đề ghi "Class Adapter" hoặc "dùng kế thừa". Class kế thừa viết **trước**, interface viết **sau** dấu phẩy.

```csharp
interface ILogger { string Log(string msg); }
class OldLogger { public string WriteMessage(string text) => "OLD: " + text; }

class LoggerAdapter : OldLogger, ILogger
{
    public string Log(string msg) => WriteMessage(msg);
}
```
