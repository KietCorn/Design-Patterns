# Factory Pattern

## Mục đích
Tạo object mà không cần chỉ định class cụ thể. Factory sẽ chọn class phù hợp dựa trên input.

## Khi nào dùng
- Tạo nhiều loại object từ cùng một base class
- Giảm dependency giữa client code và concrete classes
- Khi class nào được dùng được quyết định lúc runtime

## Cách dùng
Tạo factory function hoặc factory class nhận parameter và trả về instance của class phù hợp.

## Bài toán 1: Transport Factory
```javascript
class Car {
  drive() {
    return "🚗 Driving car...";
  }
}

class Bike {
  drive() {
    return "🏍️ Riding bike...";
  }
}

class Truck {
  drive() {
    return "🚙 Driving truck...";
  }
}

class TransportFactory {
  static create(type) {
    switch(type) {
      case "car": return new Car();
      case "bike": return new Bike();
      case "truck": return new Truck();
      default: throw new Error("Unknown transport");
    }
  }
}

const car = TransportFactory.create("car");
console.log(car.drive()); // 🚗 Driving car...
```

## Bài toán 2: Payment Method Factory
```javascript
class CreditCard {
  pay(amount) {
    return `Paid ${amount}$ with Credit Card`;
  }
}

class PayPal {
  pay(amount) {
    return `Paid ${amount}$ with PayPal`;
  }
}

class Bitcoin {
  pay(amount) {
    return `Paid ${amount}$ with Bitcoin`;
  }
}

class PaymentFactory {
  static create(method) {
    const methods = {
      "card": () => new CreditCard(),
      "paypal": () => new PayPal(),
      "crypto": () => new Bitcoin()
    };
    return methods[method]?.() || null;
  }
}

const payment = PaymentFactory.create("paypal");
console.log(payment.pay(100)); // Paid 100$ with PayPal
```

## Bài toán 3: Database Connection Factory
```javascript
class MySQLConnection {
  connect() {
    return "Connected to MySQL";
  }
}

class PostgresConnection {
  connect() {
    return "Connected to PostgreSQL";
  }
}

class MongoConnection {
  connect() {
    return "Connected to MongoDB";
  }
}

class DBFactory {
  static create(dbType) {
    const connections = {
      "mysql": () => new MySQLConnection(),
      "postgres": () => new PostgresConnection(),
      "mongo": () => new MongoConnection()
    };
    const Connection = connections[dbType];
    return Connection ? new Connection() : null;
  }
}

const db = DBFactory.create("postgres");
console.log(db.connect()); // Connected to PostgreSQL
```
