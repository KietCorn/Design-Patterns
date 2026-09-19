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

## Bài toán 1: Traffic Light
```csharp
interface IState {
    void Execute(Light light);
}

class RedLight : IState {
    public void Execute(Light light) {
        Console.WriteLine("STOP");
        light.SetState(new GreenLight());
    }
}

class GreenLight : IState {
    public void Execute(Light light) {
        Console.WriteLine("GO");
        light.SetState(new YellowLight());
    }
}

class YellowLight : IState {
    public void Execute(Light light) {
        Console.WriteLine("WAIT");
        light.SetState(new RedLight());
    }
}

class Light {
    private IState state;
    public Light() => state = new RedLight();
    public void SetState(IState s) => state = s;
    public void Change() => state.Execute(this);
}

var light = new Light();
light.Change(); // STOP
light.Change(); // GO
light.Change(); // WAIT
```

## Bài toán 2: Order Status
```csharp
interface IState {
    void Handle(Order order);
}

class Pending : IState {
    public void Handle(Order order) {
        Console.WriteLine("Pending");
        order.SetState(new Processing());
    }
}

class Processing : IState {
    public void Handle(Order order) {
        Console.WriteLine("Processing");
        order.SetState(new Shipped());
    }
}

class Shipped : IState {
    public void Handle(Order order) => Console.WriteLine("Shipped");
}

class Order {
    private IState state;
    public Order() => state = new Pending();
    public void SetState(IState s) => state = s;
    public void Next() => state.Handle(this);
}

var order = new Order();
order.Next(); // Pending
order.Next(); // Processing
order.Next(); // Shipped
```

## Bài toán 3: Media Player
```csharp
interface IState {
    void Play(Player p);
    void Pause(Player p);
}

class Playing : IState {
    public void Play(Player p) => Console.WriteLine("Already playing");
    public void Pause(Player p) {
        Console.WriteLine("Pausing");
        p.SetState(new Paused());
    }
}

class Paused : IState {
    public void Play(Player p) {
        Console.WriteLine("Playing");
        p.SetState(new Playing());
    }
    public void Pause(Player p) => Console.WriteLine("Already paused");
}

class Player {
    private IState state;
    public Player() => state = new Paused();
    public void SetState(IState s) => state = s;
    public void Play() => state.Play(this);
    public void Pause() => state.Pause(this);
}

var player = new Player();
player.Play();   // Playing
player.Pause();  // Pausing
player.Play();   // Playing
```
