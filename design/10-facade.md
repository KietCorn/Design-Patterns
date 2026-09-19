# Facade Pattern

## Mục đích
Cung cấp unified, simplified interface tới một set của interfaces trong một subsystem.

## Khi nào dùng
- Giải quyết complexity của subsystem phức tạp
- Decouple client code từ subsystem components
- Library wrapper để simplify API
- Reduce dependencies giữa client và subsystem

## Cách dùng
Tạo Facade class wraps complex subsystem. Facade cung cấp simple methods delegate tới subsystem components.

## Bài toán 1: Home Automation
```csharp
class Light { 
    public string On() => "Light ON"; 
    public string Off() => "Light OFF"; 
}

class AC { 
    public string On() => "AC ON"; 
}

class Security { 
    public string On() => "Security ON"; 
}

class HomeFacade {
    private Light light = new();
    private AC ac = new();
    private Security sec = new();
    
    public void Leave() {
        Console.WriteLine("Leaving...");
        Console.WriteLine(light.Off());
        Console.WriteLine(ac.On());
        Console.WriteLine(sec.On());
    }
}

var home = new HomeFacade();
home.Leave();
```

## Bài toán 2: Database Setup
```csharp
class DB { 
    public string Connect() => "DB connected"; 
}

class Cache { 
    public string Init() => "Cache ready"; 
}

class Logger { 
    public string Setup() => "Logger ready"; 
}

class DBFacade {
    private DB db = new();
    private Cache cache = new();
    private Logger log = new();
    
    public void Initialize() {
        Console.WriteLine(db.Connect());
        Console.WriteLine(cache.Init());
        Console.WriteLine(log.Setup());
    }
}

var facade = new DBFacade();
facade.Initialize();
```

## Bài toán 3: Payment System
```csharp
class Validator { 
    public bool Validate(string card) => card.Length > 0; 
}

class Fraud { 
    public bool Check(int money) => money <= 10000; 
}

class Bank { 
    public string Transfer(int money) => $"Transferred {money}"; 
}

class PayFacade {
    private Validator v = new();
    private Fraud f = new();
    private Bank bank = new();
    
    public void Pay(string card, int money) {
        if (!v.Validate(card)) return;
        if (!f.Check(money)) return;
        Console.WriteLine(bank.Transfer(money));
    }
}

var pay = new PayFacade();
pay.Pay("1234", 100);
```
