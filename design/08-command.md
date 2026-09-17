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

## Bài toán 1: Text Editor Undo/Redo
```javascript
class Document {
  constructor() {
    this.content = "";
  }

  write(text) {
    this.content += text;
  }

  backspace() {
    this.content = this.content.slice(0, -1);
  }
}

class Command {
  execute() {}
  undo() {}
}

class WriteCommand extends Command {
  constructor(document, text) {
    super();
    this.document = document;
    this.text = text;
  }

  execute() {
    this.document.write(this.text);
  }

  undo() {
    this.document.backspace();
  }
}

class CommandHistory {
  constructor() {
    this.history = [];
  }

  executeCommand(command) {
    command.execute();
    this.history.push(command);
  }

  undo() {
    const command = this.history.pop();
    if (command) command.undo();
  }
}

const doc = new Document();
const history = new CommandHistory();

history.executeCommand(new WriteCommand(doc, "Hello "));
history.executeCommand(new WriteCommand(doc, "World"));
console.log(doc.content); // Hello World

history.undo();
console.log(doc.content); // Hello 
```

## Bài toán 2: Smart Home Control
```javascript
class Light {
  turnOn() {
    return "🔆 Light is ON";
  }

  turnOff() {
    return "🌙 Light is OFF";
  }
}

class Command {
  execute() {}
}

class TurnOnCommand extends Command {
  constructor(light) {
    super();
    this.light = light;
  }

  execute() {
    console.log(this.light.turnOn());
  }
}

class TurnOffCommand extends Command {
  constructor(light) {
    super();
    this.light = light;
  }

  execute() {
    console.log(this.light.turnOff());
  }
}

class RemoteControl {
  constructor() {
    this.commands = {};
  }

  setCommand(button, command) {
    this.commands[button] = command;
  }

  pressButton(button) {
    if (this.commands[button]) {
      this.commands[button].execute();
    }
  }
}

const light = new Light();
const remote = new RemoteControl();

remote.setCommand("ON", new TurnOnCommand(light));
remote.setCommand("OFF", new TurnOffCommand(light));

remote.pressButton("ON");  // 🔆 Light is ON
remote.pressButton("OFF"); // 🌙 Light is OFF
```

## Bài toán 3: Task Queue / Job Scheduler
```javascript
class Command {
  execute() {}
}

class SendEmailCommand extends Command {
  constructor(email, message) {
    super();
    this.email = email;
    this.message = message;
  }

  execute() {
    return `Sending email to ${this.email}: ${this.message}`;
  }
}

class ReportCommand extends Command {
  constructor(reportName) {
    super();
    this.reportName = reportName;
  }

  execute() {
    return `Generating report: ${this.reportName}`;
  }
}

class TaskQueue {
  constructor() {
    this.queue = [];
  }

  addTask(command) {
    this.queue.push(command);
  }

  executeTasks() {
    while (this.queue.length > 0) {
      const command = this.queue.shift();
      console.log(command.execute());
    }
  }
}

const scheduler = new TaskQueue();

scheduler.addTask(new SendEmailCommand("user@example.com", "Welcome!"));
scheduler.addTask(new ReportCommand("Monthly Sales"));
scheduler.addTask(new SendEmailCommand("admin@example.com", "New user registered"));

scheduler.executeTasks();
// Sending email to user@example.com: Welcome!
// Generating report: Monthly Sales
// Sending email to admin@example.com: New user registered
```
