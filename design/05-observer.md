# Observer Pattern

## Mục đích
Định nghĩa one-to-many dependency giữa objects sao cho khi một object thay đổi state, tất cả dependents được notified tự động.

## Khi nào dùng
- Event handling systems
- Real-time updates (stock prices, weather, notifications)
- Model-View patterns (MVC)
- Reactive programming

## Cách dùng
Subject giữ list observers và notify họ khi state thay đổi. Observers subscribe/unsubscribe từ Subject.

## Bài toán 1: Stock Price Alert
```javascript
class Stock {
  constructor(symbol) {
    this.symbol = symbol;
    this.price = 0;
    this.observers = [];
  }

  subscribe(observer) {
    if (!this.observers.includes(observer)) {
      this.observers.push(observer);
    }
  }

  unsubscribe(observer) {
    this.observers = this.observers.filter(obs => obs !== observer);
  }

  setPrice(newPrice) {
    if (this.price !== newPrice) {
      this.price = newPrice;
      this.notify();
    }
  }

  notify() {
    this.observers.forEach(observer => observer.update(this));
  }
}

class Investor {
  constructor(name) {
    this.name = name;
  }

  update(stock) {
    console.log(`${this.name} received alert: ${stock.symbol} is now $${stock.price}`);
  }
}

const apple = new Stock("AAPL");
const investor1 = new Investor("Alice");
const investor2 = new Investor("Bob");

apple.subscribe(investor1);
apple.subscribe(investor2);

apple.setPrice(150); // Both investors get notified
```

## Bài toán 2: Weather Monitoring
```javascript
class WeatherStation {
  constructor() {
    this.temperature = 0;
    this.humidity = 0;
    this.subscribers = [];
  }

  subscribe(subscriber) {
    this.subscribers.push(subscriber);
  }

  unsubscribe(subscriber) {
    this.subscribers = this.subscribers.filter(sub => sub !== subscriber);
  }

  setData(temp, humidity) {
    this.temperature = temp;
    this.humidity = humidity;
    this.notifySubscribers();
  }

  notifySubscribers() {
    this.subscribers.forEach(sub => sub.update({
      temp: this.temperature,
      humidity: this.humidity
    }));
  }
}

class Display {
  constructor(name) {
    this.name = name;
  }

  update(data) {
    console.log(`${this.name}: Temperature=${data.temp}°C, Humidity=${data.humidity}%`);
  }
}

const station = new WeatherStation();
const phone = new Display("Phone");
const tv = new Display("TV");

station.subscribe(phone);
station.subscribe(tv);

station.setData(25, 60);
// Both display devices show the weather
```

## Bài toán 3: Comment Notification System
```javascript
class BlogPost {
  constructor(title) {
    this.title = title;
    this.comments = [];
    this.followers = [];
  }

  addFollower(follower) {
    this.followers.push(follower);
  }

  removeFollower(follower) {
    this.followers = this.followers.filter(f => f !== follower);
  }

  addComment(comment) {
    this.comments.push(comment);
    this.notifyFollowers(comment);
  }

  notifyFollowers(comment) {
    this.followers.forEach(follower => {
      follower.onNewComment(this.title, comment);
    });
  }
}

class User {
  constructor(name) {
    this.name = name;
  }

  onNewComment(postTitle, comment) {
    console.log(`${this.name} notified: New comment on "${postTitle}": ${comment}`);
  }
}

const post = new BlogPost("Design Patterns");
const user1 = new User("Alice");
const user2 = new User("Bob");

post.addFollower(user1);
post.addFollower(user2);

post.addComment("Great article!");
post.addComment("Very helpful!");
```
