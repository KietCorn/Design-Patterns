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
```javascript
class Pizza {
  constructor(builder) {
    this.size = builder.size;
    this.crust = builder.crust;
    this.sauce = builder.sauce;
    this.toppings = builder.toppings;
  }

  describe() {
    return `${this.size} pizza with ${this.crust} crust, ${this.sauce} sauce, ${this.toppings.join(", ")}`;
  }
}

class PizzaBuilder {
  constructor(size = "medium") {
    this.size = size;
    this.crust = "thin";
    this.sauce = "tomato";
    this.toppings = [];
  }

  setCrust(crust) {
    this.crust = crust;
    return this;
  }

  setSauce(sauce) {
    this.sauce = sauce;
    return this;
  }

  addTopping(topping) {
    this.toppings.push(topping);
    return this;
  }

  build() {
    return new Pizza(this);
  }
}

const pizza = new PizzaBuilder("large")
  .setCrust("thick")
  .setSauce("pesto")
  .addTopping("cheese")
  .addTopping("pepperoni")
  .build();

console.log(pizza.describe());
// large pizza with thick crust, pesto sauce, cheese, pepperoni
```

## Bài toán 2: HTTP Request Builder
```javascript
class HttpRequest {
  constructor(builder) {
    this.method = builder.method;
    this.url = builder.url;
    this.headers = builder.headers;
    this.body = builder.body;
  }

  toString() {
    return `${this.method} ${this.url}\nHeaders: ${JSON.stringify(this.headers)}\nBody: ${this.body}`;
  }
}

class RequestBuilder {
  constructor(url) {
    this.url = url;
    this.method = "GET";
    this.headers = {};
    this.body = null;
  }

  setMethod(method) {
    this.method = method;
    return this;
  }

  setHeader(key, value) {
    this.headers[key] = value;
    return this;
  }

  setBody(body) {
    this.body = body;
    return this;
  }

  build() {
    return new HttpRequest(this);
  }
}

const req = new RequestBuilder("https://api.example.com/users")
  .setMethod("POST")
  .setHeader("Content-Type", "application/json")
  .setHeader("Authorization", "Bearer token123")
  .setBody({ name: "John", age: 30 })
  .build();

console.log(req.toString());
```

## Bài toán 3: House Builder
```javascript
class House {
  constructor(builder) {
    this.foundation = builder.foundation;
    this.walls = builder.walls;
    this.roof = builder.roof;
    this.windows = builder.windows;
    this.door = builder.door;
  }

  describe() {
    return `House: ${this.foundation}, ${this.walls}, ${this.roof}, ${this.windows} windows, ${this.door}`;
  }
}

class HouseBuilder {
  foundation(type) {
    this.foundation = type;
    return this;
  }

  walls(type) {
    this.walls = type;
    return this;
  }

  roof(type) {
    this.roof = type;
    return this;
  }

  windows(count) {
    this.windows = count;
    return this;
  }

  door(type) {
    this.door = type;
    return this;
  }

  build() {
    return new House(this);
  }
}

const house = new HouseBuilder()
  .foundation("concrete")
  .walls("brick")
  .roof("tile")
  .windows(8)
  .door("wooden")
  .build();

console.log(house.describe());
// House: concrete, brick, tile, 8 windows, wooden
```
