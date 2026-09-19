# Abstract Factory Pattern

## Mục đích
Cung cấp interface để tạo **họ (family) các object liên quan** mà không cần chỉ định concrete class. Factory of factories.

## Khi nào dùng
- Tạo nhóm object liên quan (Button + Checkbox cùng theme)
- Hỗ trợ nhiều "family" sản phẩm (Windows vs Mac UI)
- Đảm bảo các object trong cùng family tương thích với nhau
- Mở rộng family mới mà không sửa code cũ

## Cách dùng
Tạo abstract factory interface với các method tạo từng product. Mỗi concrete factory implement tạo family product riêng. Client chỉ dùng abstract factory.

## Bài toán 1: UI Cross-Platform
```csharp
interface IButton { string Render(); }
interface ICheckbox { string Render(); }

class WinBtn : IButton { public string Render() => "Win Btn"; }
class WinChk : ICheckbox { public string Render() => "Win Chk"; }
class MacBtn : IButton { public string Render() => "Mac Btn"; }
class MacChk : ICheckbox { public string Render() => "Mac Chk"; }

interface IUIFactory {
    IButton CreateButton();
    ICheckbox CreateCheckbox();
}

class WinFactory : IUIFactory {
    public IButton CreateButton() => new WinBtn();
    public ICheckbox CreateCheckbox() => new WinChk();
}

class MacFactory : IUIFactory {
    public IButton CreateButton() => new MacBtn();
    public ICheckbox CreateCheckbox() => new MacChk();
}

IUIFactory f = new MacFactory();
Console.WriteLine(f.CreateButton().Render());   // Mac Btn
Console.WriteLine(f.CreateCheckbox().Render());  // Mac Chk
```

## Bài toán 2: Furniture (Modern vs Classic)
```csharp
interface IChair { string Sit(); }
interface ITable { string Use(); }

class ModernChair : IChair { public string Sit() => "Modern"; }
class ModernTable : ITable { public string Use() => "Modern"; }
class ClassicChair : IChair { public string Sit() => "Classic"; }
class ClassicTable : ITable { public string Use() => "Classic"; }

interface IFactory {
    IChair CreateChair();
    ITable CreateTable();
}

class ModernFactory : IFactory {
    public IChair CreateChair() => new ModernChair();
    public ITable CreateTable() => new ModernTable();
}

IFactory f = new ModernFactory();
Console.WriteLine(f.CreateChair().Sit()); // Modern
```

## Bài toán 3: Database (SQL vs Mongo)
```csharp
interface IConn { string Connect(); }
interface ICmd { string Run(string q); }

class SqlConn : IConn { public string Connect() => "SQL OK"; }
class SqlCmd : ICmd { public string Run(string q) => $"SQL: {q}"; }
class MongoConn : IConn { public string Connect() => "Mongo OK"; }
class MongoCmd : ICmd { public string Run(string q) => $"Mongo: {q}"; }

interface IDbFactory {
    IConn CreateConn();
    ICmd CreateCmd();
}

class SqlFactory : IDbFactory {
    public IConn CreateConn() => new SqlConn();
    public ICmd CreateCmd() => new SqlCmd();
}

IDbFactory db = new SqlFactory();
Console.WriteLine(db.CreateConn().Connect());    // SQL OK
Console.WriteLine(db.CreateCmd().Run("SELECT")); // SQL: SELECT
```
