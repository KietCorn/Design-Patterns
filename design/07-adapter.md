# Adapter Pattern

## Mục đích
Chuyển đổi interface của một class thành interface khác mà client mong đợi. Adapter cho phép classes với incompatible interfaces hoạt động cùng nhau.

## Khi nào dùng
- Tích hợp library/API cũ với code mới
- Chuyển đổi giữa các định dạng khác nhau
- Làm cho 2 interfaces không kompatible hoạt động được
- Adapter kế thừa (hoặc wraps) existing class

## Cách dùng
Tạo Adapter class implement target interface và wraps incompatible class, dịch calls từ interface này sang interface khác.

## Bài toán 1: Old Payment -> New
```csharp
class OldPayment {
    public string Process(string card, int money) => $"Old: {money}";
}

interface INewPayment {
    string Charge(int money, object method);
}

class PayAdapter : INewPayment {
    private OldPayment old;
    public PayAdapter(OldPayment o) => old = o;
    
    public string Charge(int money, object method) {
        dynamic m = method;
        return old.Process(m.card, money);
    }
}

var oldPay = new OldPayment();
INewPayment adapter = new PayAdapter(oldPay);
Console.WriteLine(adapter.Charge(100, new { card = "1234" })); // Old: 100
```

## Bài toán 2: XML -> JSON
```csharp
class XMLParser {
    public string Parse(string data) => $"XML: {data}";
}

interface IJsonParser {
    string Parse(string data);
}

class XMLAdapter : IJsonParser {
    private XMLParser xml;
    public XMLAdapter(XMLParser x) => xml = x;
    
    public string Parse(string data) {
        return xml.Parse(System.Text.Json.JsonSerializer.Serialize(data));
    }
}

var adapter = new XMLAdapter(new XMLParser());
Console.WriteLine(adapter.Parse("{\"id\":1}")); // XML: "{\"id\":1}"
```

## Bài toán 3: Old Device -> USB
```csharp
class OldDevice {
    public string Connect() => "Old port";
}

interface IUsbDevice {
    string UseUSB();
}

class USBAdapter : IUsbDevice {
    private OldDevice old;
    public USBAdapter(OldDevice o) => old = o;
    
    public string UseUSB() => old.Connect() + " -> USB";
}

var adapter = new USBAdapter(new OldDevice());
Console.WriteLine(adapter.UseUSB()); // Old port -> USB
```
