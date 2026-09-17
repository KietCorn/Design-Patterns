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

## Bài toán 1: Home Automation Facade
```javascript
class Light {
  turnOn() {
    return "Lights are on";
  }

  turnOff() {
    return "Lights are off";
  }
}

class AC {
  on() {
    return "AC is on (22°C)";
  }

  off() {
    return "AC is off";
  }
}

class SecuritySystem {
  arm() {
    return "Security system armed";
  }

  disarm() {
    return "Security system disarmed";
  }
}

class HomeAutomationFacade {
  constructor() {
    this.light = new Light();
    this.ac = new AC();
    this.security = new SecuritySystem();
  }

  leaveHome() {
    console.log(this.light.turnOff());
    console.log(this.ac.off());
    console.log(this.security.arm());
  }

  enterHome() {
    console.log(this.security.disarm());
    console.log(this.light.turnOn());
    console.log(this.ac.on());
  }
}

const home = new HomeAutomationFacade();
console.log("--- Leaving Home ---");
home.leaveHome();
console.log("\n--- Entering Home ---");
home.enterHome();
```

## Bài toán 2: Complex Database Facade
```javascript
class Database {
  connect(config) {
    return `Connected to ${config.host}`;
  }
}

class Cache {
  initialize(config) {
    return `Cache initialized on ${config.port}`;
  }
}

class Logger {
  setup(config) {
    return `Logger setup with level: ${config.level}`;
  }
}

class DatabaseFacade {
  constructor(config) {
    this.config = config;
    this.db = new Database();
    this.cache = new Cache();
    this.logger = new Logger();
  }

  initialize() {
    console.log(this.db.connect(this.config.database));
    console.log(this.cache.initialize(this.config.cache));
    console.log(this.logger.setup(this.config.logger));
    return "Database system initialized";
  }

  query(sql) {
    this.logger.setup(this.config.logger);
    return `Query executed: ${sql}`;
  }
}

const config = {
  database: { host: "localhost" },
  cache: { port: 6379 },
  logger: { level: "INFO" }
};

const facade = new DatabaseFacade(config);
facade.initialize();
console.log(facade.query("SELECT * FROM users"));
```

## Bài toán 3: Payment System Facade
```javascript
class CreditCardValidator {
  validate(card) {
    return card.number.length === 16 ? "Card valid" : "Card invalid";
  }
}

class FraudDetector {
  check(amount) {
    return amount > 10000 ? "Fraud detected" : "No fraud detected";
  }
}

class BankAPI {
  transfer(amount, account) {
    return `Transferred $${amount} to ${account}`;
  }
}

class NotificationService {
  sendEmail(email, message) {
    return `Email sent to ${email}: ${message}`;
  }
}

class PaymentFacade {
  constructor() {
    this.validator = new CreditCardValidator();
    this.fraudDetector = new FraudDetector();
    this.bank = new BankAPI();
    this.notification = new NotificationService();
  }

  processPayment(card, amount, account, email) {
    // Step 1: Validate card
    const validation = this.validator.validate(card);
    console.log(validation);
    if (!validation.includes("valid")) return;

    // Step 2: Check fraud
    const fraud = this.fraudDetector.check(amount);
    console.log(fraud);
    if (fraud.includes("detected")) return;

    // Step 3: Transfer money
    const result = this.bank.transfer(amount, account);
    console.log(result);

    // Step 4: Send notification
    const notification = this.notification.sendEmail(email, "Payment successful");
    console.log(notification);
  }
}

const payment = new PaymentFacade();
payment.processPayment(
  { number: "1234567890123456" },
  500,
  "user@bank.com",
  "user@email.com"
);
```
