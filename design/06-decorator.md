# Decorator Pattern

## Mục đích
Attach thêm responsibilities tới một object động. Decorators cung cấp flexible alternative tới subclassing để extend functionality.

## Khi nào dùng
- Thêm features tới object mà không modify source code
- Kết hợp nhiều features một cách flexible
- Thay thế inheritance để tránh explosion of subclasses
- Tính năng cần được bật/tắt

## Cách dùng
Decorator wraps original object và có cùng interface. Decorator thêm functionality trước/sau call method.

## Bài toán 1: Coffee Decorator
```csharp
interface ICoffee {
    int GetPrice();
    string GetDesc();
}

class Coffee : ICoffee {
    public int GetPrice() => 5;
    public string GetDesc() => "Coffee";
}

class MilkDec : ICoffee {
    private ICoffee coffee;
    public MilkDec(ICoffee c) => coffee = c;
    public int GetPrice() => coffee.GetPrice() + 1;
    public string GetDesc() => coffee.GetDesc() + ", Milk";
}

var coffee = new Coffee() as ICoffee;
Console.WriteLine($"{coffee.GetDesc()} {coffee.GetPrice()}"); // Coffee 5
coffee = new MilkDec(coffee);
Console.WriteLine($"{coffee.GetDesc()} {coffee.GetPrice()}"); // Coffee, Milk 6
```

## Bài toán 2: File Compression
```csharp
interface IFile {
    string Save();
}

class File : IFile {
    public string Save() => "File saved";
}

class CompressDec : IFile {
    private IFile file;
    public CompressDec(IFile f) => file = f;
    public string Save() => file.Save() + " [COMPRESSED]";
}

class EncryptDec : IFile {
    private IFile file;
    public EncryptDec(IFile f) => file = f;
    public string Save() => file.Save() + " [ENCRYPTED]";
}

IFile f = new File();
f = new CompressDec(f);
f = new EncryptDec(f);
Console.WriteLine(f.Save()); // File saved [COMPRESSED] [ENCRYPTED]
```

## Bài toán 3: UI Component
```csharp
interface IComponent {
    string Render();
}

class Input : IComponent {
    public string Render() => "<input>";
}

class BorderDec : IComponent {
    private IComponent comp;
    public BorderDec(IComponent c) => comp = c;
    public string Render() => $"<div border>{comp.Render()}</div>";
}

class PaddingDec : IComponent {
    private IComponent comp;
    public PaddingDec(IComponent c) => comp = c;
    public string Render() => $"<div pad>{comp.Render()}</div>";
}

IComponent input = new Input();
input = new BorderDec(input);
input = new PaddingDec(input);
Console.WriteLine(input.Render());
```
