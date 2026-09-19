# Strategy Pattern

## Mục đích
Định nghĩa một family của algorithms, encapsulate từng một, và làm chúng interchangeable. Strategy cho phép algorithm thay đổi độc lập với client sử dụng nó.

## Khi nào dùng
- Nhiều cách để thực hiện một task
- Chọn algorithm tại runtime dựa trên conditions
- Tránh nhiều if-else hoặc switch-case

## Cách dùng
Tạo interface/base class cho strategies. Implement nhiều strategies khác nhau. Client chọn strategy nào để dùng.

## Bài toán 1: Payment Strategy
```csharp
interface IPaymentStrategy {
    string Pay(int money);
}

class CardPay : IPaymentStrategy {
    public string Pay(int money) => $"Card: {money}";
}
class CashPay : IPaymentStrategy {
    public string Pay(int money) => $"Cash: {money}";
}

class Cart {
    private IPaymentStrategy strategy;
    
    public Cart(IPaymentStrategy s) => strategy = s;
    public void SetPayment(IPaymentStrategy s) => strategy = s;
    public string Checkout(int money) => strategy.Pay(money);
}

var cart = new Cart(new CardPay());
Console.WriteLine(cart.Checkout(100)); // Card: 100
cart.SetPayment(new CashPay());
Console.WriteLine(cart.Checkout(100)); // Cash: 100
```

## Bài toán 2: Compression Strategy
```csharp
interface ICompression {
    string Compress(string file);
}

class ZipComp : ICompression {
    public string Compress(string f) => $"ZIP: {f}";
}
class RarComp : ICompression {
    public string Compress(string f) => $"RAR: {f}";
}

class Archiver {
    private ICompression comp;
    
    public Archiver(ICompression c) => comp = c;
    public void SetComp(ICompression c) => comp = c;
    public string Archive(string f) => comp.Compress(f);
}

var arch = new Archiver(new ZipComp());
Console.WriteLine(arch.Archive("file.txt")); // ZIP: file.txt
arch.SetComp(new RarComp());
Console.WriteLine(arch.Archive("file.txt")); // RAR: file.txt
```

## Bài toán 3: Sort Strategy
```csharp
interface ISortStrategy {
    List<int> Sort(List<int> arr);
}

class SortAsc : ISortStrategy {
    public List<int> Sort(List<int> arr) {
        var result = new List<int>(arr);
        result.Sort();
        return result;
    }
}
class SortDesc : ISortStrategy {
    public List<int> Sort(List<int> arr) {
        var result = new List<int>(arr);
        result.Sort((a, b) => b.CompareTo(a));
        return result;
    }
}

class Sorter {
    private ISortStrategy strat;
    public Sorter(ISortStrategy s) => strat = s;
    public List<int> Execute(List<int> arr) => strat.Sort(arr);
}

var s = new Sorter(new SortAsc());
Console.WriteLine(string.Join(",", s.Execute(new List<int> { 5, 2, 8 }))); // 2,5,8
```
