# Observer Pattern

## Mục đích
Định nghĩa one-to-many dependency giữa objects sao cho khi một object thay đổi state, tất cả dependents được notified tự động.

## Khi nào dùng
- Event handling systems
- Real-time updates (stock prices, weather, notifications)
- Model-View patterns (MVC)
- Reactive programming

## Cách dùng
Subject giữ list observers và notify họ khi state thay đổi. Observers subscribe/unsubscribe từ Subject.

## Bài toán 1: Stock Price Alert
```csharp
interface IObserver {
    void Notify(int price);
}

class Stock {
    private int price;
    private List<IObserver> observers = new();
    
    public void Subscribe(IObserver obs) => observers.Add(obs);
    
    public void SetPrice(int p) {
        price = p;
        observers.ForEach(o => o.Notify(price));
    }
}

class Investor : IObserver {
    private string name;
    public Investor(string n) => name = n;
    public void Notify(int price) => Console.WriteLine($"{name}: ${price}");
}

var stock = new Stock();
stock.Subscribe(new Investor("Alice"));
stock.Subscribe(new Investor("Bob"));
stock.SetPrice(150);
```

## Bài toán 2: Weather Update
```csharp
interface IObserver {
    void Update(int temp);
}

class Weather {
    private int temp;
    private List<IObserver> subs = new();
    
    public void Subscribe(IObserver sub) => subs.Add(sub);
    
    public void SetTemp(int t) {
        temp = t;
        subs.ForEach(s => s.Update(temp));
    }
}

class Phone : IObserver {
    private string name;
    public Phone(string n) => name = n;
    public void Update(int temp) => Console.WriteLine($"{name}: {temp}°C");
}

var w = new Weather();
w.Subscribe(new Phone("Phone1"));
w.Subscribe(new Phone("Phone2"));
w.SetTemp(25);
```

## Bài toán 3: Post Comments
```csharp
interface IFollower {
    void Alert(string msg);
}

class Post {
    private List<IFollower> followers = new();
    
    public void Follow(IFollower f) => followers.Add(f);
    
    public void NewComment(string comment) {
        followers.ForEach(f => f.Alert(comment));
    }
}

class User : IFollower {
    private string name;
    public User(string n) => name = n;
    public void Alert(string msg) => Console.WriteLine($"{name}: {msg}");
}

var post = new Post();
post.Follow(new User("A"));
post.Follow(new User("B"));
post.NewComment("Hello!");
```
