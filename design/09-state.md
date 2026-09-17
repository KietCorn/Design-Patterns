# State Pattern

## Mục đích
Cho phép một object thay đổi behavior khi internal state thay đổi. Object sẽ xuất hiện như class của nó đã thay đổi.

## Khi nào dùng
- Object có behavior khác nhau dựa trên state
- Nhiều conditional statements dựa trên state
- Workflow/state machine (order processing, traffic light)
- Khi state transitions phức tạp

## Cách dùng
Tạo State interface/abstract class. Implement concrete states. Context delegate behavior tới current state object.

## Bài toán 1: Traffic Light State Machine
```javascript
class State {
  execute(trafficLight) {}
}

class RedLight extends State {
  execute(trafficLight) {
    console.log("🔴 STOP - Red Light");
    return "Go to Green";
  }
}

class YellowLight extends State {
  execute(trafficLight) {
    console.log("🟡 WAIT - Yellow Light");
    return "Go to Red";
  }
}

class GreenLight extends State {
  execute(trafficLight) {
    console.log("🟢 GO - Green Light");
    return "Go to Yellow";
  }
}

class TrafficLight {
  constructor() {
    this.state = new RedLight();
  }

  setState(state) {
    this.state = state;
  }

  change() {
    this.state.execute(this);
  }
}

const light = new TrafficLight();
light.change(); // 🔴 STOP - Red Light
light.setState(new GreenLight());
light.change(); // 🟢 GO - Green Light
light.setState(new YellowLight());
light.change(); // 🟡 WAIT - Yellow Light
```

## Bài toán 2: Order Processing States
```javascript
class OrderState {
  handle(order) {}
}

class PendingState extends OrderState {
  handle(order) {
    console.log("📦 Order is pending...");
    order.setState(new ProcessingState());
  }
}

class ProcessingState extends OrderState {
  handle(order) {
    console.log("⚙️ Order is being processed...");
    order.setState(new ShippedState());
  }
}

class ShippedState extends OrderState {
  handle(order) {
    console.log("📮 Order has been shipped!");
    order.setState(new DeliveredState());
  }
}

class DeliveredState extends OrderState {
  handle(order) {
    console.log("✅ Order has been delivered!");
  }
}

class Order {
  constructor() {
    this.state = new PendingState();
  }

  setState(state) {
    this.state = state;
  }

  process() {
    this.state.handle(this);
  }
}

const order = new Order();
order.process(); // 📦 Order is pending...
order.process(); // ⚙️ Order is being processed...
order.process(); // 📮 Order has been shipped!
order.process(); // ✅ Order has been delivered!
```

## Bài toán 3: Media Player States
```javascript
class MediaState {
  play(player) {}
  pause(player) {}
  stop(player) {}
}

class PlayingState extends MediaState {
  play(player) {
    console.log("Already playing");
  }

  pause(player) {
    console.log("▶️➡️⏸️  Pausing...");
    player.setState(new PausedState());
  }

  stop(player) {
    console.log("⏹️ Stopping...");
    player.setState(new StoppedState());
  }
}

class PausedState extends MediaState {
  play(player) {
    console.log("⏸️➡️▶️  Resuming...");
    player.setState(new PlayingState());
  }

  pause(player) {
    console.log("Already paused");
  }

  stop(player) {
    console.log("⏹️ Stopping...");
    player.setState(new StoppedState());
  }
}

class StoppedState extends MediaState {
  play(player) {
    console.log("▶️ Playing...");
    player.setState(new PlayingState());
  }

  pause(player) {
    console.log("Cannot pause - not playing");
  }

  stop(player) {
    console.log("Already stopped");
  }
}

class MediaPlayer {
  constructor() {
    this.state = new StoppedState();
  }

  setState(state) {
    this.state = state;
  }

  play() {
    this.state.play(this);
  }

  pause() {
    this.state.pause(this);
  }

  stop() {
    this.state.stop(this);
  }
}

const player = new MediaPlayer();
player.play();   // ▶️ Playing...
player.pause();  // ▶️➡️⏸️  Pausing...
player.play();   // ⏸️➡️▶️  Resuming...
player.stop();   // ⏹️ Stopping...
```
