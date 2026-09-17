# Adapter Pattern

## Mục đích
Chuyển đổi interface của một class thành interface khác mà client mong đợi. Adapter cho phép classes với incompatible interfaces hoạt động cùng nhau.

## Khi nào dùng
- Tích hợp library/API cũ với code mới
- Chuyển đổi giữa các định dạng khác nhau
- Làm cho 2 interfaces không kompatible hoạt động được
- Adapter kế thừa (hoặc wraps) existing class

## Cách dùng
Tạo Adapter class implement target interface và wraps incompatible class, dịch calls từ interface này sang interface khác.

## Bài toán 1: Legacy Payment System Adapter
```javascript
// Old payment system
class OldPaymentGateway {
  processPayment(cardNumber, amount) {
    return `Processing ${amount} from card ${cardNumber}`;
  }
}

// New interface expected
class NewPaymentGateway {
  charge(amount, paymentMethod) {
    throw new Error("Must implement");
  }
}

// Adapter
class PaymentAdapter extends NewPaymentGateway {
  constructor(oldGateway) {
    super();
    this.oldGateway = oldGateway;
  }

  charge(amount, paymentMethod) {
    // Convert new interface to old interface
    return this.oldGateway.processPayment(paymentMethod.cardNumber, amount);
  }
}

const oldGateway = new OldPaymentGateway();
const adapter = new PaymentAdapter(oldGateway);

console.log(adapter.charge(100, { cardNumber: "1234-5678" }));
// Processing 100 from card 1234-5678
```

## Bài toán 2: Data Format Adapter
```javascript
// XML API
class XMLParser {
  parseXML(xmlData) {
    return { status: "parsed", data: xmlData };
  }
}

// JSON interface expected
class JSONInterface {
  parseJSON(jsonData) {
    throw new Error("Must implement");
  }
}

// Adapter
class XMLtoJSONAdapter extends JSONInterface {
  constructor(xmlParser) {
    super();
    this.xmlParser = xmlParser;
  }

  parseJSON(jsonData) {
    // In real scenario, convert JSON to XML
    const xmlData = this.convertToXML(jsonData);
    return this.xmlParser.parseXML(xmlData);
  }

  convertToXML(json) {
    return JSON.stringify(json);
  }
}

const xmlParser = new XMLParser();
const adapter = new XMLtoJSONAdapter(xmlParser);

console.log(adapter.parseJSON({ user: "Alice", age: 30 }));
// { status: 'parsed', data: '{"user":"Alice","age":30}' }
```

## Bài toán 3: USB Cable Adapter (Device Compatibility)
```javascript
// Old device with proprietary connector
class OldDevice {
  useProprietary() {
    return "Connecting via proprietary port";
  }
}

// New standard interface (USB)
class USBDevice {
  useUSB() {
    throw new Error("Must implement");
  }
}

// Adapter
class USBAdapter extends USBDevice {
  constructor(oldDevice) {
    super();
    this.oldDevice = oldDevice;
  }

  useUSB() {
    // Adapt old proprietary connection to USB
    return this.oldDevice.useProprietary() + " -> USB adapter";
  }
}

const oldDevice = new OldDevice();
const adapter = new USBAdapter(oldDevice);

console.log(adapter.useUSB());
// Connecting via proprietary port -> USB adapter

// Now old device works with USB interface
console.log(adapter instanceof USBDevice); // true
```
