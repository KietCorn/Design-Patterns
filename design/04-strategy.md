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
```javascript
class CreditCardStrategy {
  pay(amount) {
    return `Paid ${amount}$ using Credit Card (secure)`;
  }
}

class PayPalStrategy {
  pay(amount) {
    return `Paid ${amount}$ using PayPal`;
  }
}

class CashStrategy {
  pay(amount) {
    return `Paid ${amount}$ in Cash`;
  }
}

class ShoppingCart {
  constructor() {
    this.items = [];
    this.paymentStrategy = null;
  }

  setPaymentStrategy(strategy) {
    this.paymentStrategy = strategy;
  }

  checkout() {
    const total = this.items.reduce((sum, price) => sum + price, 0);
    return this.paymentStrategy.pay(total);
  }

  addItem(price) {
    this.items.push(price);
  }
}

const cart = new ShoppingCart();
cart.addItem(50);
cart.addItem(30);

cart.setPaymentStrategy(new CreditCardStrategy());
console.log(cart.checkout()); // Paid 80$ using Credit Card (secure)

cart.setPaymentStrategy(new CashStrategy());
console.log(cart.checkout()); // Paid 80$ in Cash
```

## Bài toán 2: Compression Strategy
```javascript
class ZipCompression {
  compress(file) {
    return `[ZIP] Compressed ${file}`;
  }
}

class RarCompression {
  compress(file) {
    return `[RAR] Compressed ${file}`;
  }
}

class GzipCompression {
  compress(file) {
    return `[GZIP] Compressed ${file}`;
  }
}

class FileArchiver {
  constructor(compression) {
    this.compression = compression;
  }

  setCompression(compression) {
    this.compression = compression;
  }

  archive(file) {
    return this.compression.compress(file);
  }
}

const archiver = new FileArchiver(new ZipCompression());
console.log(archiver.archive("document.pdf")); // [ZIP] Compressed document.pdf

archiver.setCompression(new GzipCompression());
console.log(archiver.archive("document.pdf")); // [GZIP] Compressed document.pdf
```

## Bài toán 3: Sorting Strategy
```javascript
class AscendingSort {
  sort(arr) {
    return [...arr].sort((a, b) => a - b);
  }
}

class DescendingSort {
  sort(arr) {
    return [...arr].sort((a, b) => b - a);
  }
}

class RandomSort {
  sort(arr) {
    return [...arr].sort(() => Math.random() - 0.5);
  }
}

class DataSorter {
  constructor(strategy) {
    this.strategy = strategy;
  }

  setSortStrategy(strategy) {
    this.strategy = strategy;
  }

  execute(data) {
    return this.strategy.sort(data);
  }
}

const data = [5, 2, 8, 1, 9];
const sorter = new DataSorter(new AscendingSort());

console.log(sorter.execute(data)); // [1, 2, 5, 8, 9]

sorter.setSortStrategy(new DescendingSort());
console.log(sorter.execute(data)); // [9, 8, 5, 2, 1]

sorter.setSortStrategy(new RandomSort());
console.log(sorter.execute(data)); // [random order]
```
