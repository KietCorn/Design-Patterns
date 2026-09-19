# Proxy Pattern

## Mục đích
Cung cấp một **đại diện (surrogate)** cho object khác để kiểm soát truy cập tới object đó.

## Khi nào dùng
- Lazy loading (tải resource nặng khi cần)
- Access control (kiểm tra quyền trước khi truy cập)
- Logging/Caching (thêm logic trước/sau khi gọi real object)
- Remote proxy (đại diện cho object ở server khác)

## Cách dùng
Proxy implement cùng interface với real object. Proxy giữ reference tới real object, thêm logic trước/sau khi delegate tới real object.

## Bài toán 1: Access Control
```csharp
interface IDoc { string Read(); }

class Doc : IDoc {
    public string Read() => "Secret";
}

class ProxyDoc : IDoc {
    private Doc doc = new();
    private string role;
    public ProxyDoc(string r) => role = r;

    public string Read() {
        if (role == "admin") return doc.Read();
        return "Denied";
    }
}

IDoc d1 = new ProxyDoc("admin");
IDoc d2 = new ProxyDoc("guest");
Console.WriteLine(d1.Read()); // Secret
Console.WriteLine(d2.Read()); // Denied
```

## Bài toán 2: Lazy Loading
```csharp
interface IImage { string Show(); }

class HeavyImage : IImage {
    public HeavyImage() => Console.WriteLine("Loading...");
    public string Show() => "Image";
}

class ImageProxy : IImage {
    private HeavyImage img;
    public string Show() {
        if (img == null) img = new HeavyImage();
        return img.Show();
    }
}

IImage img = new ImageProxy();
Console.WriteLine(img.Show()); // Loading... Image
Console.WriteLine(img.Show()); // Image (không load lại)
```

## Bài toán 3: Logging
```csharp
interface IService { string Do(string data); }

class RealService : IService {
    public string Do(string data) => $"Done: {data}";
}

class LogProxy : IService {
    private RealService svc = new();
    public string Do(string data) {
        Console.WriteLine($"[LOG] {data}");
        return svc.Do(data);
    }
}

IService s = new LogProxy();
s.Do("test"); // [LOG] test → Done: test
```
