# Decorator Pattern

## Mục đích
Attach thêm responsibilities tới một object động. Decorators cung cấp flexible alternative tới subclassing để extend functionality.

## Khi nào dùng
- Thêm features tới object mà không modify source code
- Kết hợp nhiều features một cách flexible
- Thay thế inheritance để tránh explosion of subclasses
- Tính năng cần được bật/tắt

## Cách dùng
Decorator wraps original object và có cùng interface. Decorator thêm functionality trước/sau call method.

## Bài toán 1: Coffee Decorator
```javascript
class Coffee {
  getDescription() {
    return "Coffee";
  }

  getCost() {
    return 5;
  }
}

class CoffeeDecorator {
  constructor(coffee) {
    this.coffee = coffee;
  }

  getDescription() {
    return this.coffee.getDescription();
  }

  getCost() {
    return this.coffee.getCost();
  }
}

class MilkDecorator extends CoffeeDecorator {
  getDescription() {
    return this.coffee.getDescription() + ", Milk";
  }

  getCost() {
    return this.coffee.getCost() + 1;
  }
}

class CaramelDecorator extends CoffeeDecorator {
  getDescription() {
    return this.coffee.getDescription() + ", Caramel";
  }

  getCost() {
    return this.coffee.getCost() + 1.5;
  }
}

let coffee = new Coffee();
console.log(coffee.getDescription(), "$" + coffee.getCost()); // Coffee $5

coffee = new MilkDecorator(coffee);
console.log(coffee.getDescription(), "$" + coffee.getCost()); // Coffee, Milk $6

coffee = new CaramelDecorator(coffee);
console.log(coffee.getDescription(), "$" + coffee.getCost()); // Coffee, Milk, Caramel $7.5
```

## Bài toán 2: File Encryption/Compression
```javascript
class FileData {
  constructor(filename, data) {
    this.filename = filename;
    this.data = data;
  }

  save() {
    return `Saving ${this.filename}: ${this.data}`;
  }
}

class FileDecorator {
  constructor(file) {
    this.file = file;
  }

  save() {
    return this.file.save();
  }
}

class EncryptionDecorator extends FileDecorator {
  save() {
    return this.file.save() + " [ENCRYPTED]";
  }
}

class CompressionDecorator extends FileDecorator {
  save() {
    return this.file.save() + " [COMPRESSED]";
  }
}

let file = new FileData("document.txt", "Secret data");
console.log(file.save()); // Saving document.txt: Secret data

file = new CompressionDecorator(file);
console.log(file.save()); // Saving document.txt: Secret data [COMPRESSED]

file = new EncryptionDecorator(file);
console.log(file.save()); // Saving document.txt: Secret data [COMPRESSED] [ENCRYPTED]
```

## Bài toán 3: UI Component Decorator
```javascript
class TextBox {
  render() {
    return `<input type="text">`;
  }
}

class TextBoxDecorator {
  constructor(textBox) {
    this.textBox = textBox;
  }

  render() {
    return this.textBox.render();
  }
}

class BorderDecorator extends TextBoxDecorator {
  render() {
    return `<div style="border: 1px solid black">${this.textBox.render()}</div>`;
  }
}

class ShadowDecorator extends TextBoxDecorator {
  render() {
    return `<div style="box-shadow: 0 0 10px rgba(0,0,0,0.1)">${this.textBox.render()}</div>`;
  }
}

class PaddingDecorator extends TextBoxDecorator {
  render() {
    return `<div style="padding: 10px">${this.textBox.render()}</div>`;
  }
}

let textbox = new TextBox();
console.log(textbox.render()); // <input type="text">

textbox = new BorderDecorator(textbox);
console.log(textbox.render()); // <div style="border: 1px solid black"><input type="text"></div>

textbox = new ShadowDecorator(textbox);
textbox = new PaddingDecorator(textbox);
console.log(textbox.render()); 
// Multiple decorators wrapped
```
