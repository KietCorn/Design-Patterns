# Design Patterns - Cheat Sheet Học Thuộc (.NET)

## 1️⃣ SINGLETON - Một Instance Duy Nhất

```csharp
class Singleton {
    private static Singleton instance;
    private Singleton() { }
    
    public static Singleton GetInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

var s1 = Singleton.GetInstance();
var s2 = Singleton.GetInstance();
Console.WriteLine(s1 == s2); // True
```

**Nhớ:** Static instance, private constructor, GetInstance check rồi create

---

## 2️⃣ FACTORY - Tạo Object Tùy Loại

```csharp
interface ITransport { string Drive(); }
class Car : ITransport { public string Drive() => "car"; }
class Bike : ITransport { public string Drive() => "bike"; }

class Factory {
    public static ITransport Create(string type) {
        if (type == "car") return new Car();
        if (type == "bike") return new Bike();
        return null;
    }
}

var obj = Factory.Create("car");
```

**Nhớ:** Interface + static Create, if-if-if chọn class

---

## 3️⃣ BUILDER - Build Object Bước Bước

```csharp
class Builder {
    private string size = "M";
    private string color = "red";
    
    public Builder SetSize(string s) { size = s; return this; }
    public Builder SetColor(string c) { color = c; return this; }
    public object Build() => new { size, color };
}

var obj = new Builder()
    .SetSize("L")
    .SetColor("blue")
    .Build();
```

**Nhớ:** Return `this` mỗi method, dùng fluent API

---

## 4️⃣ STRATEGY - Chọn Thuật Toán

```csharp
interface IStrategy { string Execute(); }
class StrategyA : IStrategy { public string Execute() => "A"; }
class StrategyB : IStrategy { public string Execute() => "B"; }

class Context {
    private IStrategy strat;
    public Context(IStrategy s) => strat = s;
    public void SetStrategy(IStrategy s) => strat = s;
    public string Execute() => strat.Execute();
}

var ctx = new Context(new StrategyA());
ctx.SetStrategy(new StrategyB());
```

**Nhớ:** Interface strategy, constructor + SetStrategy

---

## 5️⃣ OBSERVER - Notify Khi Thay Đổi

```csharp
interface IObserver {
    void Notify(int data);
}

class Subject {
    private List<IObserver> observers = new();
    
    public void Subscribe(IObserver obs) => observers.Add(obs);
    public void Notify(int data) => observers.ForEach(o => o.Notify(data));
}

class Observer : IObserver {
    public void Notify(int data) => Console.WriteLine(data);
}

var subj = new Subject();
subj.Subscribe(new Observer());
subj.Notify(100);
```

**Nhớ:** List observers, ForEach notify

---

## 6️⃣ DECORATOR - Wrap & Thêm Chức Năng

```csharp
interface IComponent { int GetPrice(); }

class Component : IComponent {
    public int GetPrice() => 10;
}

class Decorator : IComponent {
    private IComponent comp;
    public Decorator(IComponent c) => comp = c;
    public int GetPrice() => comp.GetPrice() + 5;
}

IComponent obj = new Component();
obj = new Decorator(obj);
Console.WriteLine(obj.GetPrice()); // 15
```

**Nhớ:** Interface, constructor wrap object, gọi comp.GetPrice()

---

## 7️⃣ ADAPTER - Chuyển Đổi Interface

```csharp
class OldAPI {
    public string OldMethod(string data) => $"Old: {data}";
}

interface INewAPI {
    string NewMethod(string data);
}

class Adapter : INewAPI {
    private OldAPI old;
    public Adapter(OldAPI o) => old = o;
    public string NewMethod(string data) => old.OldMethod(data);
}

var adapter = new Adapter(new OldAPI());
Console.WriteLine(adapter.NewMethod("test")); // Old: test
```

**Nhớ:** Extends/Implements target interface, wrap old object

---

## 8️⃣ COMMAND - Encapsulate Action

```csharp
interface ICommand {
    void Execute();
}

class Receiver {
    public void Do() => Console.WriteLine("Doing");
}

class Command : ICommand {
    private Receiver receiver;
    public Command(Receiver r) => receiver = r;
    public void Execute() => receiver.Do();
}

var cmd = new Command(new Receiver());
cmd.Execute();
```

**Nhớ:** Interface ICommand, Execute gọi receiver.Do()

---

## 9️⃣ STATE - Behavior Theo State

```csharp
interface IState {
    void Execute(Context ctx);
}

class StateA : IState {
    public void Execute(Context ctx) {
        Console.WriteLine("A");
        ctx.SetState(new StateB());
    }
}

class Context {
    private IState state;
    public Context() => state = new StateA();
    public void SetState(IState s) => state = s;
    public void Execute() => state.Execute(this);
}

var ctx = new Context();
ctx.Execute(); // A
ctx.Execute(); // B
```

**Nhớ:** IState interface, Execute nhận context, SetState trong Execute

---

## 🔟 FACADE - Giản Lược Interface Phức Tạp

```csharp
class SubA { public void Do() => Console.WriteLine("A"); }
class SubB { public void Do() => Console.WriteLine("B"); }

class Facade {
    private SubA a = new();
    private SubB b = new();
    
    public void DoEverything() {
        a.Do();
        b.Do();
    }
}

var facade = new Facade();
facade.DoEverything();
```

**Nhớ:** Giữ subsystems, method wrap logic phức tạp

---

## ⚡ Quick Tips - C#

| Pattern | Cốt Lõi | C# |
|---------|---------|---------|
| Singleton | 1 instance | `private static`, `GetInstance()` |
| Factory | Tạo object | `interface`, `static Create()` |
| Builder | Build bước bước | `return this`, fluent API |
| Strategy | Chọn algorithm | `interface IStrategy` |
| Observer | Notify observers | `List<IObserver>`, `ForEach` |
| Decorator | Wrap object | `interface`, wrap constructor |
| Adapter | Chuyển interface | `class Adapter : INew`, wrap old |
| Command | Encapsulate | `interface ICommand`, Execute |
| State | State machine | `interface IState`, SetState |
| Facade | Simplify API | Giữ subsystems, wrap logic |

---

## 📝 Cách Học Thuộc .NET

1. **Interface trước** → `interface IPattern`
2. **Concrete classes** → implement interface
3. **Constructor** → inject dependencies
4. **Public methods** → gọi internal methods
5. **Dùng var** → C# auto type

---

*C#: Interface → Inherit → Constructor → Method*
