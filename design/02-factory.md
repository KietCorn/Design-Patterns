S

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

```csharp
interface ITransport { string Drive(); }

class Car : ITransport { 
    public string Drive() => "car"; 
}
class Bike : ITransport { 
    public string Drive() => "bike"; 
}

class TransportFactory {
    public static ITransport Create(string type) {
        if (type == "car") return new Car();
        if (type == "bike") return new Bike();
        return null;
    }
}

var car = TransportFactory.Create("car");
Console.WriteLine(car.Drive()); // car
```

## Bài toán 2: Payment Method Factory

```csharp
interface IPayment { string Pay(int money); }

class Card : IPayment { 
    public string Pay(int m) => $"Card paid {m}"; 
}
class PayPal : IPayment { 
    public string Pay(int m) => $"PayPal paid {m}"; 
}

class PaymentFactory {
    public static IPayment Create(string method) {
        if (method == "card") return new Card();
        if (method == "paypal") return new PayPal();
        return null;
    }
}

var pay = PaymentFactory.Create("paypal");
Console.WriteLine(pay.Pay(100)); // PayPal paid 100
```

## Bài toán 3: Animal Factory

```csharp
interface IAnimal { string Sound(); }

class Dog : IAnimal { 
    public string Sound() => "Woof"; 
}
class Cat : IAnimal { 
    public string Sound() => "Meow"; 
}

class AnimalFactory {
    public static IAnimal Create(string type) {
        if (type == "dog") return new Dog();
        if (type == "cat") return new Cat();
        return null;
    }
}

var dog = AnimalFactory.Create("dog");
Console.WriteLine(dog.Sound()); // Woof
```
