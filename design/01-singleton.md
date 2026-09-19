# Singleton Pattern

## Mục đích
Đảm bảo một class chỉ có duy nhất một instance và cung cấp điểm truy cập global tới instance đó.

## Khi nào dùng
- Database connection (kết nối duy nhất tới DB)
- Logger (một logger toàn cầu)
- Configuration manager (quản lý cấu hình ứng dụng)
- Cache manager

## Cách dùng
Dùng static field hoặc lazy initialization để tạo instance duy nhất. Ẩn constructor để ngăn tạo instance mới.

## Bài toán 1: Database Connection
```csharp
class Database {
    private static Database instance;
    
    private Database() { }
    
    public static Database GetInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
}

var db1 = Database.GetInstance();
var db2 = Database.GetInstance();
Console.WriteLine(db1 == db2); // True - cùng 1 instance
```

## Bài toán 2: Logger
```csharp
class Logger {
    private static Logger instance;
    private List<string> logs = new();
    
    private Logger() { }
    
    public static Logger GetInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    public void Log(string msg) {
        logs.Add(msg);
    }
}

var log1 = Logger.GetInstance();
var log2 = Logger.GetInstance();
log1.Log("Event 1");
log2.Log("Event 2");
// Cùng 1 instance
```

## Bài toán 3: App Settings
```csharp
class Settings {
    private static Settings instance;
    private Dictionary<string, string> data = new();
    
    private Settings() {
        data["theme"] = "dark";
        data["lang"] = "en";
    }
    
    public static Settings GetInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
    
    public string Get(string key) => data[key];
    public void Set(string key, string val) => data[key] = val;
}

var s1 = Settings.GetInstance();
var s2 = Settings.GetInstance();
s1.Set("theme", "light");
Console.WriteLine(s2.Get("theme")); // light
```
