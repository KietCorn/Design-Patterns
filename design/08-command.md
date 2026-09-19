# Command Pattern

## Mục đích
Encapsulate một request thành một object, cho phép parameterize clients với different requests, queue requests, và log requests.

## Khi nào dùng
- Undo/Redo functionality
- Queue operations để execute later
- Schedule tasks (job scheduling)
- Macro recording
- Transaction management

## Cách dùng
Command object encapsulate action và receiver. Invoker thực thi command. Client tạo command object.

## Bài toán 1: Undo/Redo
```csharp
class Doc {
    public string Text { get; set; } = "";
    public void Add(string t) => Text += t;
    public void Undo() => Text = Text.Remove(Text.Length - 1);
}

interface ICommand {
    void Execute();
    void Undo();
}

class AddCmd : ICommand {
    private Doc doc;
    private string text;
    
    public AddCmd(Doc d, string t) { doc = d; text = t; }
    public void Execute() => doc.Add(text);
    public void Undo() => doc.Undo();
}

class History {
    private List<ICommand> cmds = new();
    
    public void Execute(ICommand cmd) {
        cmd.Execute();
        cmds.Add(cmd);
    }
    
    public void Undo() {
        if (cmds.Count > 0) {
            cmds.Last().Undo();
            cmds.RemoveAt(cmds.Count - 1);
        }
    }
}

var doc = new Doc();
var hist = new History();
hist.Execute(new AddCmd(doc, "Hello "));
hist.Execute(new AddCmd(doc, "World"));
Console.WriteLine(doc.Text); // Hello World
hist.Undo();
Console.WriteLine(doc.Text); // Hello
```

## Bài toán 2: Remote Control
```csharp
class Light {
    public string On() => "ON";
    public string Off() => "OFF";
}

interface ICommand {
    void Execute();
}

class OnCmd : ICommand {
    private Light light;
    public OnCmd(Light l) => light = l;
    public void Execute() => Console.WriteLine(light.On());
}

class OffCmd : ICommand {
    private Light light;
    public OffCmd(Light l) => light = l;
    public void Execute() => Console.WriteLine(light.Off());
}

class Remote {
    private Dictionary<string, ICommand> cmds = new();
    public void Set(string btn, ICommand cmd) => cmds[btn] = cmd;
    public void Press(string btn) => cmds[btn]?.Execute();
}

var remote = new Remote();
var light = new Light();
remote.Set("ON", new OnCmd(light));
remote.Set("OFF", new OffCmd(light));
remote.Press("ON"); // ON
```

## Bài toán 3: Task Queue
```csharp
interface ICommand {
    void Execute();
}

class EmailCmd : ICommand {
    private string email, msg;
    public EmailCmd(string e, string m) { email = e; msg = m; }
    public void Execute() => Console.WriteLine($"Email: {msg}");
}

class Queue {
    private List<ICommand> tasks = new();
    public void Add(ICommand cmd) => tasks.Add(cmd);
    public void Execute() => tasks.ForEach(t => t.Execute());
}

var queue = new Queue();
queue.Add(new EmailCmd("user", "Hi"));
queue.Add(new EmailCmd("admin", "Report"));
queue.Execute();
```
