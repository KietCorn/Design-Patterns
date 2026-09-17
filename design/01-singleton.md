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
```javascript
class Database {
  static instance = null;

  constructor() {
    if (Database.instance) return Database.instance;
    this.connection = "Connected to DB";
    Database.instance = this;
  }

  query(sql) {
    return `Executing: ${sql}`;
  }
}

const db1 = new Database();
const db2 = new Database();
console.log(db1 === db2); // true
console.log(db1.query("SELECT * FROM users"));
```

## Bài toán 2: Logger
```javascript
class Logger {
  constructor() {
    if (Logger.instance) return Logger.instance;
    this.logs = [];
    Logger.instance = this;
  }

  log(message) {
    this.logs.push(`[${new Date().toISOString()}] ${message}`);
  }

  getLogs() {
    return this.logs;
  }
}

const logger1 = new Logger();
const logger2 = new Logger();
logger1.log("User login");
logger2.log("User logout");
console.log(logger2.getLogs()); // Cả 2 log từ logger1 và logger2
```

## Bài toán 3: Configuration Manager
```javascript
class Config {
  constructor() {
    if (Config.instance) return Config.instance;
    this.settings = {
      apiUrl: "https://api.example.com",
      timeout: 5000,
      debug: false
    };
    Config.instance = this;
  }

  get(key) {
    return this.settings[key];
  }

  set(key, value) {
    this.settings[key] = value;
  }
}

const config = new Config();
console.log(config.get("apiUrl")); // https://api.example.com
config.set("debug", true);
const config2 = new Config();
console.log(config2.get("debug")); // true - cùng instance
```
