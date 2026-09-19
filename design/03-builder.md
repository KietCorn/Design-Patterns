# Builder Pattern

## Mục đích
Tách quá trình tạo object phức tạp khỏi đại diện của nó, cho phép tạo object từng bước.

## Khi nào dùng
- Object có nhiều thuộc tính optional
- Constructor có quá nhiều parameters
- Cần tạo object theo các bước riêng biệt
- Tạo object với nhiều variations

## Cách dùng
Tạo Builder class có method để set từng thuộc tính, rồi gọi `build()` để tạo object cuối cùng.

## Bài toán 1: Pizza Builder
```csharp
class Pizza {
    public string Size { get; set; }
    public string Crust { get; set; }
    public List<string> Toppings { get; set; }
}

class PizzaBuilder {
    private string size = "medium";
    private string crust = "thin";
    private List<string> toppings = new();
    
    public PizzaBuilder AddSize(string s) { 
        size = s; return this; 
    }
    public PizzaBuilder AddCrust(string c) { 
        crust = c; return this; 
    }
    public PizzaBuilder AddTopping(string t) { 
        toppings.Add(t); return this; 
    }
    
    public Pizza Build() {
        return new Pizza { Size = size, Crust = crust, Toppings = toppings };
    }
}

var pizza = new PizzaBuilder()
    .AddSize("large")
    .AddCrust("thick")
    .AddTopping("cheese")
    .Build();
```

## Bài toán 2: HTTP Request Builder
```csharp
class HttpRequest {
    public string Method { get; set; }
    public string Url { get; set; }
    public Dictionary<string, string> Headers { get; set; }
}

class RequestBuilder {
    private string url;
    private string method = "GET";
    private Dictionary<string, string> headers = new();
    
    public RequestBuilder(string u) => url = u;
    
    public RequestBuilder SetMethod(string m) { 
        method = m; return this; 
    }
    public RequestBuilder SetHeader(string k, string v) { 
        headers[k] = v; return this; 
    }
    
    public HttpRequest Build() {
        return new HttpRequest { Method = method, Url = url, Headers = headers };
    }
}

var req = new RequestBuilder("/users")
    .SetMethod("POST")
    .SetHeader("token", "abc123")
    .Build();
```

## Bài toán 3: Computer Builder
```csharp
class Computer {
    public string CPU { get; set; }
    public string RAM { get; set; }
    public string GPU { get; set; }
}

class ComputerBuilder {
    private string cpu = "Intel";
    private string ram = "8GB";
    private string gpu = "None";
    
    public ComputerBuilder SetCPU(string c) { cpu = c; return this; }
    public ComputerBuilder SetRAM(string r) { ram = r; return this; }
    public ComputerBuilder SetGPU(string g) { gpu = g; return this; }
    
    public Computer Build() {
        return new Computer { CPU = cpu, RAM = ram, GPU = gpu };
    }
}

var pc = new ComputerBuilder()
    .SetCPU("AMD")
    .SetRAM("16GB")
    .SetGPU("RTX3080")
    .Build();
```
