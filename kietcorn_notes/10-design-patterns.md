# 10 Design Patterns Cần Biết

---

## NHÓM CREATIONAL

---

## SINGLETON LÀ GÌ?

Tưởng tượng một đất nước chỉ có **một Tổng thống** tại bất kỳ thời điểm nào. Dù bạn hỏi ai, dù bạn tra cứu từ nguồn nào, câu trả lời cũng chỉ trỏ về đúng một người duy nhất đó. Không thể có hai Tổng thống cùng tồn tại song song.

Đây chính là ý tưởng cốt lõi của Singleton: **đảm bảo một class chỉ có duy nhất một instance trong toàn bộ ứng dụng, và cung cấp một điểm truy cập toàn cục đến instance đó.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: Ứng dụng cần quản lý kết nối đến database. Nếu mỗi lần cần truy vấn lại tạo ra một connection mới, ứng dụng sẽ tốn tài nguyên, tạo hàng trăm kết nối đồng thời, dễ gây quá tải database.
- **Cách Singleton giải quyết**: Tạo một `DatabaseConnection` Singleton — lần đầu gọi thì khởi tạo kết nối, các lần sau đều trả về đúng object kết nối đó, đảm bảo chỉ có một kết nối duy nhất được dùng.

**Ví dụ 2**

- **Bài toán**: Ứng dụng cần một hệ thống logging ghi log ra file. Nếu nhiều class đều tự tạo Logger riêng, log có thể bị ghi chồng chéo, mất thứ tự, hoặc nhiều file log xuất hiện không kiểm soát được.
- **Cách Singleton giải quyết**: Class `Logger` là Singleton — mọi nơi trong ứng dụng đều gọi `Logger.getInstance()` và ghi vào cùng một luồng log duy nhất, đảm bảo thứ tự và tính nhất quán.

**Ví dụ 3**

- **Bài toán**: Ứng dụng cần đọc và cache file cấu hình (config) từ `application.properties`. Nếu mỗi class tự đọc file config một lần, vừa tốn I/O, vừa dễ dẫn đến tình trạng mỗi nơi đọc ra giá trị khác nhau nếu file bị thay đổi giữa chừng.
- **Cách Singleton giải quyết**: `ConfigManager` được thiết kế Singleton — đọc file config một lần duy nhất khi khởi tạo, sau đó mọi nơi đều lấy từ cùng một instance đã cache sẵn.

```java
public class ConfigManager {
    private static ConfigManager instance;
    private String dbHost;
    private int dbPort;

    private ConfigManager() {
        this.dbHost = "localhost";
        this.dbPort = 5432;
        System.out.println("ConfigManager: Đọc file config lần đầu và duy nhất.");
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getDbHost() { return dbHost; }
    public int getDbPort() { return dbPort; }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        ConfigManager config1 = ConfigManager.getInstance();
        // Output: ConfigManager: Đọc file config lần đầu và duy nhất.

        ConfigManager config2 = ConfigManager.getInstance();
        // (Không in gì thêm — không khởi tạo lại)

        System.out.println("Cùng một instance? " + (config1 == config2));
        // Output: Cùng một instance? true

        System.out.println("DB Host: " + config1.getDbHost());
        // Output: DB Host: localhost
    }
}
```

**Điểm mấu chốt cần nhớ**: Constructor là `private` — không ai có thể `new ConfigManager()` từ bên ngoài. Toàn bộ quyền kiểm soát tạo instance nằm trong tay chính class đó thông qua `getInstance()`.

### Ý nghĩa của Singleton

Singleton giải quyết bài toán cần đảm bảo chỉ có một instance duy nhất — thường dùng cho các tài nguyên dùng chung toàn cục như config, logger, connection pool. Nên dùng khi việc có nhiều hơn một instance sẽ gây ra lỗi logic hoặc lãng phí tài nguyên. Cần cẩn thận trong môi trường đa luồng (multi-thread) — cần thêm `synchronized` hoặc dùng `volatile` để đảm bảo an toàn.

---

## FACTORY METHOD LÀ GÌ?

Hãy hình dung bạn đến một **tiệm bánh nhượng quyền** (franchise). Mỗi chi nhánh đều bán bánh mì, nhưng chi nhánh ở Hà Nội làm bánh mì kiểu Bắc, chi nhánh ở TP.HCM làm bánh mì kiểu Nam. Bạn chỉ cần nói "cho tôi một chiếc bánh mì" — từng chi nhánh tự quyết định cách họ làm ra loại bánh phù hợp địa phương mình.

Đây chính là ý tưởng cốt lõi của Factory Method: **định nghĩa một interface để tạo object, nhưng để các subclass quyết định sẽ tạo ra object thuộc loại nào.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: Ứng dụng gửi thông báo có thể gửi qua Email hoặc SMS. Nếu dùng `if-else` hay `switch-case` để quyết định tạo object gửi tin, mỗi lần thêm kênh mới phải sửa vào đúng đoạn `if-else` đó — vi phạm Open/Closed Principle.
- **Cách Factory Method giải quyết**: Tạo abstract class `NotificationSender` với factory method `createNotification()`. Các subclass `EmailSender`, `SmsSender` override lại method này để trả về đúng loại notification của mình.

**Ví dụ 2**

- **Bài toán**: Framework xử lý tài liệu hỗ trợ nhiều định dạng: Word, PDF, Excel. Mỗi định dạng cần parser riêng. Code gốc không nên biết chi tiết từng parser — cần một cách mở rộng linh hoạt.
- **Cách Factory Method giải quyết**: Abstract class `DocumentProcessor` có factory method `createParser()`. Subclass `WordProcessor`, `PdfProcessor` tự tạo ra đúng parser tương ứng, còn logic xử lý chung nằm ở lớp cha.

**Ví dụ 3**

- **Bài toán**: Ứng dụng game có nhiều loại kẻ địch: Zombie, Robot, Dragon. Mỗi màn chơi cần tạo ra loại kẻ địch khác nhau. Nếu code trực tiếp `new Zombie()`, `new Robot()` rải rác khắp nơi, rất khó quản lý và mở rộng.
- **Cách Factory Method giải quyết**: Mỗi `EnemySpawner` subclass chịu trách nhiệm tạo một loại kẻ địch. Code game chỉ làm việc với abstract `EnemySpawner`, không cần biết loại cụ thể.

```java
public interface Enemy {
    void attack();
}

public class Zombie implements Enemy {
    public void attack() { System.out.println("Zombie tấn công: Cắn!"); }
}

public class Robot implements Enemy {
    public void attack() { System.out.println("Robot tấn công: Bắn laser!"); }
}

// Creator (abstract)
public abstract class EnemySpawner {
    public abstract Enemy createEnemy(); // Factory Method

    public void spawnAndAttack() {
        Enemy enemy = createEnemy();
        System.out.println("Kẻ địch xuất hiện!");
        enemy.attack();
    }
}

// Concrete Creators
public class ZombieSpawner extends EnemySpawner {
    public Enemy createEnemy() { return new Zombie(); }
}

public class RobotSpawner extends EnemySpawner {
    public Enemy createEnemy() { return new Robot(); }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        EnemySpawner spawner = new ZombieSpawner();
        spawner.spawnAndAttack();
        // Output: Kẻ địch xuất hiện!
        // Output: Zombie tấn công: Cắn!

        spawner = new RobotSpawner();
        spawner.spawnAndAttack();
        // Output: Kẻ địch xuất hiện!
        // Output: Robot tấn công: Bắn laser!
    }
}
```

**Điểm mấu chốt cần nhớ**: `spawnAndAttack()` ở lớp cha không biết mình đang làm việc với `Zombie` hay `Robot` — nó chỉ gọi `createEnemy()` và nhận về một `Enemy`. Đây là sức mạnh của polymorphism kết hợp Factory Method.

### Ý nghĩa của Factory Method

Factory Method giải quyết bài toán: "Tôi biết **khi nào** cần tạo object, nhưng không biết (hoặc không muốn biết) **loại nào** cần tạo." Nên dùng khi một framework muốn cho phép user mở rộng bằng cách thêm subclass mà không sửa code gốc. Khác với Abstract Factory (tạo cả **họ** object), Factory Method chỉ ủy quyền việc tạo **một** loại object cho subclass.

---

## ABSTRACT FACTORY LÀ GÌ?

Tưởng tượng bạn đang trang trí một căn phòng theo **phong cách Scandinavian** hoặc **phong cách Nhật Bản**. Mỗi phong cách có bộ đồ nội thất riêng: ghế sofa, bàn cà phê, đèn ngủ — tất cả phải **đồng bộ với nhau**. Bạn không thể lấy ghế kiểu Nhật kết hợp với bàn kiểu Scandinavian vì sẽ mất thẩm mỹ tổng thể.

Đây chính là ý tưởng cốt lõi của Abstract Factory: **cung cấp một interface để tạo ra các họ object có liên quan với nhau mà không cần chỉ định class cụ thể — đảm bảo các object trong cùng một họ luôn tương thích với nhau.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: Ứng dụng desktop cần hỗ trợ giao diện trên cả Windows và macOS. Button, Checkbox trên mỗi hệ điều hành có giao diện khác nhau nhưng phải nhất quán — không thể lấy Button Windows mix với Checkbox macOS.
- **Cách Abstract Factory giải quyết**: Interface `UIFactory` khai báo `createButton()`, `createCheckbox()`. `WindowsUIFactory` tạo ra bộ UI Windows đồng bộ, `MacUIFactory` tạo bộ UI Mac đồng bộ.

**Ví dụ 2**

- **Bài toán**: Game có hai thế giới: Medieval và Sci-Fi. Mỗi thế giới có nhân vật Hero, Weapon, Enemy đặc trưng riêng. Cần đảm bảo khi chơi Medieval thì tất cả đều thuần Trung Cổ, không trộn lẫn Sci-Fi.
- **Cách Abstract Factory giải quyết**: `WorldFactory` abstract với các method tạo Hero, Weapon, Enemy. `MedievalFactory` và `SciFiFactory` cài đặt cụ thể, đảm bảo mọi object trong một thế giới đều thuộc về thế giới đó.

**Ví dụ 3**

- **Bài toán**: Ứng dụng e-commerce cần hỗ trợ Stripe và PayPal. Mỗi cổng có bộ gồm: `PaymentProcessor` và `InvoiceGenerator` theo chuẩn riêng. Trộn lẫn Stripe processor với PayPal invoice sẽ gây lỗi không tương thích.
- **Cách Abstract Factory giải quyết**: `PaymentFactory` khai báo `createProcessor()` và `createInvoiceGenerator()`. Mỗi cổng thanh toán có factory riêng đảm bảo sự đồng bộ.

```java
public interface PaymentProcessor {
    void processPayment(double amount);
}

public interface InvoiceGenerator {
    void generateInvoice(double amount);
}

public class StripeProcessor implements PaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("Stripe: Xử lý thanh toán $" + amount);
    }
}

public class StripeInvoice implements InvoiceGenerator {
    public void generateInvoice(double amount) {
        System.out.println("Stripe: Tạo hóa đơn cho $" + amount);
    }
}

public class PaypalProcessor implements PaymentProcessor {
    public void processPayment(double amount) {
        System.out.println("PayPal: Xử lý thanh toán $" + amount);
    }
}

public class PaypalInvoice implements InvoiceGenerator {
    public void generateInvoice(double amount) {
        System.out.println("PayPal: Tạo hóa đơn cho $" + amount);
    }
}

// Abstract Factory
public interface PaymentFactory {
    PaymentProcessor createProcessor();
    InvoiceGenerator createInvoiceGenerator();
}

public class StripeFactory implements PaymentFactory {
    public PaymentProcessor createProcessor() { return new StripeProcessor(); }
    public InvoiceGenerator createInvoiceGenerator() { return new StripeInvoice(); }
}

public class PaypalFactory implements PaymentFactory {
    public PaymentProcessor createProcessor() { return new PaypalProcessor(); }
    public InvoiceGenerator createInvoiceGenerator() { return new PaypalInvoice(); }
}

// Client
public class CheckoutService {
    private PaymentProcessor processor;
    private InvoiceGenerator invoiceGenerator;

    public CheckoutService(PaymentFactory factory) {
        this.processor = factory.createProcessor();
        this.invoiceGenerator = factory.createInvoiceGenerator();
    }

    public void checkout(double amount) {
        processor.processPayment(amount);
        invoiceGenerator.generateInvoice(amount);
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        CheckoutService stripe = new CheckoutService(new StripeFactory());
        stripe.checkout(99.99);
        // Output: Stripe: Xử lý thanh toán $99.99
        // Output: Stripe: Tạo hóa đơn cho $99.99

        CheckoutService paypal = new CheckoutService(new PaypalFactory());
        paypal.checkout(49.50);
        // Output: PayPal: Xử lý thanh toán $49.5
        // Output: PayPal: Tạo hóa đơn cho $49.5
    }
}
```

**Điểm mấu chốt cần nhớ**: `CheckoutService` không biết đang dùng Stripe hay PayPal — nó chỉ làm việc với `PaymentFactory`. Đổi cổng thanh toán chỉ cần truyền factory khác vào, không sửa một dòng nào trong `CheckoutService`.

### Ý nghĩa của Abstract Factory

Abstract Factory giải quyết bài toán cần tạo ra các **họ object** có liên quan mà phải đảm bảo tương thích với nhau. Nên dùng khi hệ thống cần hỗ trợ nhiều "thương hiệu" hoặc "theme" và bạn muốn đảm bảo không bao giờ trộn lẫn giữa các họ. So với Factory Method (ủy quyền tạo **một** loại object), Abstract Factory ủy quyền tạo **cả một bộ** object liên quan — đây là điểm phân biệt quan trọng nhất.

---

## BUILDER LÀ GÌ?

Hãy hình dung bạn đang **đặt một chiếc bánh burger tại quầy order**: bạn chọn loại bánh, loại thịt, muốn thêm phô mai không, thêm rau không, sốt gì... Từng bước bạn "lắp ráp" chiếc burger theo ý mình. Người thu ngân ghi lại từng yêu cầu, và cuối cùng mới đưa ra chiếc burger hoàn chỉnh đúng ý bạn.

Đây chính là ý tưởng cốt lõi của Builder: **tách biệt quá trình xây dựng (construction) một object phức tạp ra khỏi phần biểu diễn (representation) của nó, cho phép cùng một quy trình tạo ra những kết quả khác nhau.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: Class `User` có 10 thuộc tính nhưng không phải field nào cũng bắt buộc. Nếu dùng constructor, phải viết vô số constructor overload hoặc truyền `null` vào các field không dùng — rất khó đọc và dễ nhầm thứ tự tham số.
- **Cách Builder giải quyết**: `UserBuilder` cho phép gọi `.name("Kiet").email("a@b.com").age(25).build()` — chỉ set những field cần thiết, thứ tự tùy ý.

**Ví dụ 2**

- **Bài toán**: Tạo các loại báo cáo (Report): có loại cần header + body, có loại cần header + body + footer + chart. Cùng bước xây dựng nhưng kết quả khác nhau. Nếu xây dựng trực tiếp trong từng class, logic bị lặp lại và khó bảo trì.
- **Cách Builder giải quyết**: `ReportBuilder` abstract định nghĩa các bước `buildHeader()`, `buildBody()`, `buildFooter()`. Các concrete builder `PDFReportBuilder`, `HTMLReportBuilder` cài đặt từng bước theo cách riêng.

**Ví dụ 3**

- **Bài toán**: Ứng dụng gửi email cần tạo object `Email` với nhiều thuộc tính tùy chọn: to, cc, bcc, subject, body. Một số email chỉ cần to + subject + body, một số cần đầy đủ. Constructor với nhiều tham số tùy chọn rất khó dùng đúng.
- **Cách Builder giải quyết**: `EmailBuilder` cho phép linh hoạt set từng phần, chỉ validate khi `.build()` — đảm bảo object Email luôn hợp lệ khi được tạo ra.

```java
public class Email {
    private String to;
    private String subject;
    private String body;
    private String cc;
    private String bcc;

    private Email() {}

    public String toString() {
        return "Email{to='" + to + "', subject='" + subject + "', cc='" + cc + "'}";
    }

    public static class EmailBuilder {
        private String to;
        private String subject;
        private String body;
        private String cc = "";
        private String bcc = "";

        public EmailBuilder to(String to) { this.to = to; return this; }
        public EmailBuilder subject(String s) { this.subject = s; return this; }
        public EmailBuilder body(String b) { this.body = b; return this; }
        public EmailBuilder cc(String cc) { this.cc = cc; return this; }
        public EmailBuilder bcc(String bcc) { this.bcc = bcc; return this; }

        public Email build() {
            if (to == null || subject == null)
                throw new IllegalStateException("Email phải có 'to' và 'subject'");
            Email email = new Email();
            email.to = this.to;
            email.subject = this.subject;
            email.body = this.body;
            email.cc = this.cc;
            email.bcc = this.bcc;
            return email;
        }
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        Email simple = new Email.EmailBuilder()
            .to("boss@company.com")
            .subject("Báo cáo tuần")
            .body("Kính gửi sếp...")
            .build();
        System.out.println(simple);
        // Output: Email{to='boss@company.com', subject='Báo cáo tuần', cc=''}

        Email full = new Email.EmailBuilder()
            .to("team@company.com")
            .subject("Họp khẩn")
            .body("Mời toàn team!")
            .cc("manager@company.com")
            .bcc("hr@company.com")
            .build();
        System.out.println(full);
        // Output: Email{to='team@company.com', subject='Họp khẩn', cc='manager@company.com'}
    }
}
```

**Điểm mấu chốt cần nhớ**: Mỗi method của Builder trả về `this` — đây là kỹ thuật **method chaining** (fluent interface). Constructor của `Email` là `private` nên bắt buộc phải đi qua Builder mới tạo được object, đảm bảo không có Email "nửa vời" tồn tại.

### Ý nghĩa của Builder

Builder giải quyết bài toán tạo object phức tạp với nhiều tham số tùy chọn mà không làm rối constructor. Nên dùng khi object cần nhiều bước khởi tạo, hoặc cần nhiều biến thể của cùng một loại object. So với Factory Method/Abstract Factory (quan tâm đến **loại** object nào được tạo), Builder quan tâm đến **quá trình** tạo ra object đó như thế nào từng bước một.

---

## PROTOTYPE LÀ GÌ?

Tưởng tượng bạn có một **bản vẽ thiết kế ngôi nhà** rất công phu mất cả tháng để vẽ. Khi cần xây thêm 5 ngôi nhà tương tự, bạn không vẽ lại từ đầu — bạn **photo copy bản vẽ gốc** rồi chỉnh sửa những phần cần thay đổi (màu sơn, số phòng...) cho từng ngôi nhà.

Đây chính là ý tưởng cốt lõi của Prototype: **tạo object mới bằng cách sao chép (clone) một object đã có sẵn thay vì khởi tạo từ đầu.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: Ứng dụng tạo nhân vật game. Mỗi nhân vật có hàng chục thuộc tính (level, skills, equipment...) được tính toán phức tạp. Mỗi trận đấu cần tạo 100 nhân vật cùng loại — tạo mới từ đầu 100 lần rất tốn tài nguyên.
- **Cách Prototype giải quyết**: Tạo một nhân vật "mẫu" (prototype), sau đó clone ra 100 bản, chỉ thay đổi tên và vị trí. Tốn ít tài nguyên hơn nhiều so với khởi tạo đầy đủ 100 lần.

**Ví dụ 2**

- **Bài toán**: Ứng dụng vẽ đồ họa cho phép user duplicate các shape. Không thể biết trước user sẽ duplicate loại shape nào. Nếu dùng `instanceof` để kiểm tra rồi `new`, code rất rắc rối và vi phạm Open/Closed.
- **Cách Prototype giải quyết**: Mỗi shape implement `clone()` — khi duplicate, chỉ cần gọi `selectedShape.clone()`, không cần biết đó là hình gì.

**Ví dụ 3**

- **Bài toán**: Hệ thống tạo hợp đồng. Mỗi tháng công ty tạo hàng chục hợp đồng có cùng điều khoản chuẩn, chỉ khác tên khách hàng và ngày ký. Khởi tạo lại toàn bộ hợp đồng từ đầu mỗi lần là dư thừa.
- **Cách Prototype giải quyết**: Lưu một `templateContract` như prototype. Mỗi hợp đồng mới clone từ template rồi chỉ cập nhật tên khách và ngày ký.

```java
public class Contract implements Cloneable {
    private String customerName;
    private String signDate;
    private String terms;

    public Contract(String customerName, String signDate, String terms) {
        this.customerName = customerName;
        this.signDate = signDate;
        this.terms = terms;
    }

    public void setCustomerName(String name) { this.customerName = name; }
    public void setSignDate(String date) { this.signDate = date; }

    public Contract clone() {
        try {
            return (Contract) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone thất bại", e);
        }
    }

    public String toString() {
        return "Contract{customer='" + customerName + "', date='" + signDate + "'}";
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        Contract template = new Contract("TEMPLATE", "N/A", "Điều khoản 1... Điều khoản 2...");

        Contract contractA = template.clone();
        contractA.setCustomerName("Nguyễn Văn A");
        contractA.setSignDate("2026-09-16");

        Contract contractB = template.clone();
        contractB.setCustomerName("Trần Thị B");
        contractB.setSignDate("2026-09-17");

        System.out.println(contractA);
        // Output: Contract{customer='Nguyễn Văn A', date='2026-09-16'}

        System.out.println(contractB);
        // Output: Contract{customer='Trần Thị B', date='2026-09-17'}

        System.out.println("Hai object khác nhau? " + (contractA != contractB));
        // Output: Hai object khác nhau? true
    }
}
```

**Điểm mấu chốt cần nhớ**: Java có sẵn interface `Cloneable` và method `clone()` trong `Object`. Khi clone, Java sao chép toàn bộ giá trị các field — đây là **shallow copy**. Nếu object có field là reference đến object khác, cần cẩn thận implement **deep copy** thủ công để tránh hai bản clone cùng trỏ đến một object con.

### Ý nghĩa của Prototype

Prototype giải quyết bài toán cần tạo nhiều object tương tự nhau mà việc khởi tạo từ đầu tốn kém. Nên dùng khi cần duplicate object mà không phụ thuộc vào class cụ thể của nó. Trong nhóm Creational, Prototype là pattern duy nhất tạo object bằng cách sao chép thay vì xây dựng — điểm đặc trưng để phân biệt với các pattern còn lại.

---

## NHÓM STRUCTURAL

---

## ADAPTER LÀ GÌ?

Tưởng tượng bạn mua một **thiết bị điện từ Mỹ** về Việt Nam. Phích cắm kiểu Mỹ (2 chân dẹt) không vừa ổ cắm Việt Nam (2 chân tròn). Bạn cần một **bộ chuyển đổi (adapter)** — cắm bộ chuyển vào ổ Việt Nam, rồi cắm thiết bị Mỹ vào bộ chuyển. Cả hai bên không cần thay đổi gì, chỉ cần thêm adapter ở giữa.

Đây chính là ý tưởng cốt lõi của Adapter: **chuyển đổi interface của một class thành interface khác mà client mong đợi, giúp các class không tương thích interface có thể làm việc cùng nhau.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: Code có sẵn dùng interface `OldLogger` với method `log(String msg)`. Muốn tích hợp thư viện logging mới `NewLogger` với method `writeLog(String level, String msg)`. Không thể sửa thư viện mới, không muốn sửa hết code cũ.
- **Cách Adapter giải quyết**: Tạo `LoggerAdapter` implement `OldLogger`, bên trong giữ reference đến `NewLogger` và chuyển đổi lời gọi `log()` sang `writeLog()`.

**Ví dụ 2**

- **Bài toán**: Ứng dụng dùng interface `PaymentGateway` với method `pay(amount)`. Muốn tích hợp thêm Stripe SDK với method `charge(currency, amount)`. Không thể sửa Stripe SDK.
- **Cách Adapter giải quyết**: `StripeAdapter` implement `PaymentGateway`, wrapper Stripe SDK và chuyển đổi method signature cho phù hợp.

**Ví dụ 3**

- **Bài toán**: Hệ thống hiện tại dùng interface `DataExporter` với method `exportToCsv(List<String> data)`. Muốn thêm khả năng xuất ra JSON bằng thư viện `JsonLibrary` (bên thứ ba) có method `toJson(String[] items)`. Cần tích hợp mà không phá vỡ code cũ.
- **Cách Adapter giải quyết**: Tạo `JsonExporterAdapter` implement `DataExporter`, chuyển đổi `List<String>` sang `String[]` và gọi thư viện JSON.

```java
import java.util.List;

public interface DataExporter {
    void exportToCsv(List<String> data);
}

// Implementation gốc
public class CsvExporter implements DataExporter {
    public void exportToCsv(List<String> data) {
        System.out.println("CSV Export: " + String.join(",", data));
    }
}

// Thư viện bên thứ ba, không thể sửa
public class JsonLibrary {
    public void toJson(String[] items) {
        System.out.println("JSON Export: [\"" + String.join("\", \"", items) + "\"]");
    }
}

// Adapter
public class JsonExporterAdapter implements DataExporter {
    private JsonLibrary jsonLibrary;

    public JsonExporterAdapter(JsonLibrary jsonLibrary) {
        this.jsonLibrary = jsonLibrary;
    }

    public void exportToCsv(List<String> data) {
        String[] items = data.toArray(new String[0]); // chuyển đổi ở đây
        jsonLibrary.toJson(items);
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        List<String> data = java.util.Arrays.asList("Alice", "Bob", "Charlie");

        DataExporter csvExporter = new CsvExporter();
        csvExporter.exportToCsv(data);
        // Output: CSV Export: Alice,Bob,Charlie

        DataExporter jsonExporter = new JsonExporterAdapter(new JsonLibrary());
        jsonExporter.exportToCsv(data);
        // Output: JSON Export: ["Alice", "Bob", "Charlie"]
    }
}
```

**Điểm mấu chốt cần nhớ**: Client gọi `exportToCsv()` trên cả hai exporter mà không biết loại nào là adapter. `JsonExporterAdapter` "dịch" lời gọi từ interface cũ sang API của thư viện mới — đây là bản chất của Adapter.

### Ý nghĩa của Adapter

Adapter giải quyết vấn đề tích hợp code cũ với thư viện mới (hoặc ngược lại) khi interface không tương thích. Nên dùng khi bạn muốn dùng một class có sẵn nhưng interface của nó không khớp với phần còn lại của hệ thống. Khác với Facade (đơn giản hóa interface phức tạp), Adapter không đơn giản hóa — nó **chuyển đổi** interface này sang interface khác.

---

## FACADE LÀ GÌ?

Hãy hình dung bạn gọi điện đến **tổng đài chăm sóc khách hàng** của một ngân hàng. Chỉ cần nói "Tôi muốn mở tài khoản tiết kiệm" — nhân viên tổng đài sẽ lo hết: kết nối với bộ phận tài khoản, bộ phận thẩm định, bộ phận IT... Bạn không cần biết bên trong ngân hàng có bao nhiêu phòng ban hay quy trình phức tạp thế nào.

Đây chính là ý tưởng cốt lõi của Facade: **cung cấp một interface đơn giản, thống nhất cho một hệ thống con phức tạp — che giấu sự phức tạp bên trong.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: Để khởi động một bộ máy tính, cần gọi đúng thứ tự: `CPU.initialize()`, `Memory.load()`, `HardDrive.read()`, `BIOS.check()`... Mỗi lần khởi động, client phải tự nhớ và gọi đúng trình tự — rất dễ sai và khó bảo trì.
- **Cách Facade giải quyết**: Class `ComputerFacade` với method `startComputer()` gói gọn toàn bộ trình tự. Client chỉ cần gọi một method duy nhất.

**Ví dụ 2**

- **Bài toán**: Hệ thống xử lý đơn hàng cần phối hợp `InventoryService`, `PaymentService`, `ShippingService`, `NotificationService`. Client phải tự orchestrate 4 service này — code client phình to, khó test.
- **Cách Facade giải quyết**: `OrderFacade` với method `placeOrder()` điều phối toàn bộ 4 service bên trong. Client chỉ cần gọi `orderFacade.placeOrder(order)`.

**Ví dụ 3**

- **Bài toán**: Hệ thống xem phim tại nhà gồm: `Projector`, `AudioSystem`, `StreamingService`, `Lights`. Để xem phim phải bật đúng thứ tự: tắt đèn, bật projector, bật âm thanh, mở streaming. Rất rắc rối nếu phải làm thủ công mỗi lần.
- **Cách Facade giải quyết**: `HomeTheaterFacade` với method `watchMovie()` và `stopMovie()` đóng gói toàn bộ quy trình.

```java
public class Projector {
    public void on() { System.out.println("Projector: Bật lên"); }
    public void off() { System.out.println("Projector: Tắt đi"); }
}

public class AudioSystem {
    public void on() { System.out.println("AudioSystem: Bật âm thanh"); }
    public void setVolume(int level) { System.out.println("AudioSystem: Volume = " + level); }
    public void off() { System.out.println("AudioSystem: Tắt âm thanh"); }
}

public class StreamingService {
    public void connect() { System.out.println("Streaming: Kết nối internet"); }
    public void play(String movie) { System.out.println("Streaming: Phát '" + movie + "'"); }
    public void disconnect() { System.out.println("Streaming: Ngắt kết nối"); }
}

public class Lights {
    public void dim() { System.out.println("Lights: Giảm đèn"); }
    public void on() { System.out.println("Lights: Bật đèn"); }
}

// Facade
public class HomeTheaterFacade {
    private Projector projector = new Projector();
    private AudioSystem audio = new AudioSystem();
    private StreamingService streaming = new StreamingService();
    private Lights lights = new Lights();

    public void watchMovie(String movie) {
        System.out.println("--- Chuẩn bị xem phim ---");
        lights.dim();
        projector.on();
        audio.on();
        audio.setVolume(8);
        streaming.connect();
        streaming.play(movie);
    }

    public void stopMovie() {
        System.out.println("--- Dừng xem phim ---");
        streaming.disconnect();
        audio.off();
        projector.off();
        lights.on();
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        HomeTheaterFacade theater = new HomeTheaterFacade();

        theater.watchMovie("Inception");
        // Output: --- Chuẩn bị xem phim ---
        // Output: Lights: Giảm đèn
        // Output: Projector: Bật lên
        // Output: AudioSystem: Bật âm thanh
        // Output: AudioSystem: Volume = 8
        // Output: Streaming: Kết nối internet
        // Output: Streaming: Phát 'Inception'

        theater.stopMovie();
        // Output: --- Dừng xem phim ---
        // Output: Streaming: Ngắt kết nối
        // Output: AudioSystem: Tắt âm thanh
        // Output: Projector: Tắt đi
        // Output: Lights: Bật đèn
    }
}
```

**Điểm mấu chốt cần nhớ**: Client chỉ tương tác với `HomeTheaterFacade` — không biết gì về `Projector`, `AudioSystem`, hay `StreamingService`. Toàn bộ sự phức tạp bị ẩn đi sau một interface đơn giản gồm 2 method.

### Ý nghĩa của Facade

Facade giải quyết vấn đề hệ thống con quá phức tạp, client phải biết quá nhiều thứ để dùng được. Nên dùng khi muốn cung cấp một "lối vào đơn giản" vào một subsystem phức tạp, hoặc khi muốn giảm sự phụ thuộc của client vào các chi tiết bên trong. Khác với Adapter (chuyển đổi interface), Facade **thiết kế** một interface mới hoàn toàn đơn giản hơn.

---

## DECORATOR LÀ GÌ?

Tưởng tượng bạn đang **order cà phê tại Starbucks**: bắt đầu từ ly cà phê đen cơ bản, bạn có thể thêm sữa (+5k), thêm đường (+2k), thêm kem (+10k)... Mỗi lần thêm topping, giá tăng lên, tính năng tăng lên — nhưng bản chất vẫn là ly cà phê, không thay đổi class gốc.

Đây chính là ý tưởng cốt lõi của Decorator: **gắn thêm trách nhiệm (behavior) vào object một cách linh hoạt tại runtime, thay thế cho việc tạo subclass để mở rộng chức năng.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: `TextEditor` có method `write()`. Muốn thêm tính năng: bold, italic, underline — và có thể kết hợp tùy ý. Nếu dùng kế thừa, cần 7 subclass cho mọi tổ hợp — bùng nổ class.
- **Cách Decorator giải quyết**: `BoldDecorator`, `ItalicDecorator` wrap object `TextEditor` gốc, thêm behavior vào `write()`. Muốn bold + italic: `new ItalicDecorator(new BoldDecorator(editor))`.

**Ví dụ 2**

- **Bài toán**: Stream đọc file: cần compress, encrypt, buffer theo thứ tự tùy chọn. Tạo subclass cho từng tổ hợp là không thực tế.
- **Cách Decorator giải quyết**: Java I/O dùng đúng pattern này: `new BufferedInputStream(new GZIPInputStream(new FileInputStream("file")))` — mỗi lớp wrapper thêm một tính năng.

**Ví dụ 3**

- **Bài toán**: Ứng dụng có `NotificationService` gửi email cơ bản. Muốn thêm: ghi log trước khi gửi, đo thời gian gửi, retry nếu thất bại — và có thể bật/tắt từng tính năng độc lập theo môi trường (dev/production).
- **Cách Decorator giải quyết**: Mỗi tính năng phụ trợ là một Decorator riêng, wrap `NotificationService` gốc. Ghép tổ hợp tùy ý không cần tạo thêm subclass.

```java
public interface Coffee {
    String getDescription();
    double getCost();
}

public class SimpleCoffee implements Coffee {
    public String getDescription() { return "Cà phê đen"; }
    public double getCost() { return 20000; }
}

// Base Decorator
public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    public CoffeeDecorator(Coffee coffee) { this.coffee = coffee; }
}

public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) { super(coffee); }
    public String getDescription() { return coffee.getDescription() + " + Sữa"; }
    public double getCost() { return coffee.getCost() + 5000; }
}

public class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) { super(coffee); }
    public String getDescription() { return coffee.getDescription() + " + Đường"; }
    public double getCost() { return coffee.getCost() + 2000; }
}

public class WhipDecorator extends CoffeeDecorator {
    public WhipDecorator(Coffee coffee) { super(coffee); }
    public String getDescription() { return coffee.getDescription() + " + Kem tươi"; }
    public double getCost() { return coffee.getCost() + 10000; }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " - " + coffee.getCost() + "đ");
        // Output: Cà phê đen - 20000.0đ

        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " - " + coffee.getCost() + "đ");
        // Output: Cà phê đen + Sữa - 25000.0đ

        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " - " + coffee.getCost() + "đ");
        // Output: Cà phê đen + Sữa + Đường - 27000.0đ

        coffee = new WhipDecorator(coffee);
        System.out.println(coffee.getDescription() + " - " + coffee.getCost() + "đ");
        // Output: Cà phê đen + Sữa + Đường + Kem tươi - 37000.0đ
    }
}
```

**Điểm mấu chốt cần nhớ**: Mỗi Decorator **implement cùng interface** `Coffee` và **giữ một reference** đến `Coffee` khác bên trong. Mỗi lần gọi `getCost()`, nó gọi `coffee.getCost()` của lớp bên trong rồi cộng thêm phần của mình — chuỗi gọi truyền xuống đến `SimpleCoffee` rồi cộng ngược lên.

### Ý nghĩa của Decorator

Decorator giải quyết bài toán cần thêm tính năng linh hoạt vào object mà không muốn tạo ra "bùng nổ subclass" cho mọi tổ hợp. Nên dùng khi tính năng có thể bật/tắt độc lập và kết hợp tùy ý tại runtime. Khác với kế thừa (mở rộng tĩnh tại compile time), Decorator mở rộng động tại runtime — đây là ưu điểm mấu chốt.

---

## NHÓM BEHAVIORAL

---

## STRATEGY LÀ GÌ?

Tưởng tượng bạn đang đi từ nhà đến sân bay. Bạn có thể **chọn cách di chuyển**: đi taxi (nhanh nhưng tốn tiền), đi xe buýt (rẻ nhưng chậm), đi xe máy (tiện nhưng phải gửi xe). Cùng một mục tiêu "đến sân bay", nhưng bạn linh hoạt **thay đổi chiến lược** tùy tình huống.

Đây chính là ý tưởng cốt lõi của Strategy: **định nghĩa một tập hợp các thuật toán, đóng gói từng cái, và làm cho chúng có thể hoán đổi cho nhau — cho phép thay đổi thuật toán độc lập với client sử dụng nó.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: Hệ thống thanh toán hỗ trợ: thẻ tín dụng, ví điện tử, tiền mặt. Nếu dùng `if-else` trong hàm `pay()` để phân loại, mỗi lần thêm phương thức mới phải sửa vào đúng `if-else` đó — vi phạm Open/Closed.
- **Cách Strategy giải quyết**: Interface `PaymentStrategy` với method `pay(amount)`. Mỗi phương thức thanh toán là một Strategy riêng. `PaymentContext` giữ reference đến strategy và có thể thay đổi runtime.

**Ví dụ 2**

- **Bài toán**: Ứng dụng sắp xếp dữ liệu cần dùng các thuật toán khác nhau: BubbleSort cho tập nhỏ, QuickSort cho tập lớn, MergeSort khi cần ổn định. Logic chọn thuật toán nằm lẫn trong code sort làm class phình to.
- **Cách Strategy giải quyết**: Interface `SortStrategy` với method `sort()`. Mỗi thuật toán là một Strategy. Code gọi tự quyết định truyền vào strategy nào tùy kích thước dữ liệu.

**Ví dụ 3**

- **Bài toán**: Ứng dụng nén file hỗ trợ ZIP, GZIP, RAR. Logic nén của từng định dạng rất khác nhau, nhưng interface với user là như nhau. Cần thiết kế sao cho dễ thêm định dạng mới mà không sửa code hiện có.
- **Cách Strategy giải quyết**: Mỗi định dạng nén implement interface `CompressionStrategy`. `FileCompressor` nhận strategy và delegate việc nén cho strategy đó.

```java
public interface CompressionStrategy {
    void compress(String fileName);
}

public class ZipStrategy implements CompressionStrategy {
    public void compress(String fileName) {
        System.out.println("Nén '" + fileName + "' bằng ZIP");
    }
}

public class GzipStrategy implements CompressionStrategy {
    public void compress(String fileName) {
        System.out.println("Nén '" + fileName + "' bằng GZIP");
    }
}

public class RarStrategy implements CompressionStrategy {
    public void compress(String fileName) {
        System.out.println("Nén '" + fileName + "' bằng RAR");
    }
}

// Context
public class FileCompressor {
    private CompressionStrategy strategy;

    public FileCompressor(CompressionStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(CompressionStrategy strategy) {
        this.strategy = strategy;
    }

    public void compressFile(String fileName) {
        strategy.compress(fileName);
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        FileCompressor compressor = new FileCompressor(new ZipStrategy());
        compressor.compressFile("report.pdf");
        // Output: Nén 'report.pdf' bằng ZIP

        compressor.setStrategy(new GzipStrategy());
        compressor.compressFile("backup.sql");
        // Output: Nén 'backup.sql' bằng GZIP

        compressor.setStrategy(new RarStrategy());
        compressor.compressFile("secret.docx");
        // Output: Nén 'secret.docx' bằng RAR
    }
}
```

**Điểm mấu chốt cần nhớ**: `FileCompressor` (context) không biết đang dùng ZIP, GZIP hay RAR — nó chỉ gọi `strategy.compress()`. Đổi thuật toán chỉ cần `setStrategy(new GzipStrategy())` — không sửa một dòng nào trong `FileCompressor`. Đây là **delegation** thay vì inheritance.

### Ý nghĩa của Strategy

Strategy giải quyết bài toán có nhiều biến thể của một thuật toán và cần có khả năng hoán đổi chúng linh hoạt. Nên dùng khi muốn loại bỏ `if-else` / `switch` phân loại thuật toán, hoặc khi cần thay đổi behavior của object tại runtime. Khác với Template Method (dùng kế thừa, subclass override một phần của thuật toán), Strategy dùng composition — thuật toán nằm hoàn toàn trong object riêng, có thể thay thế độc lập.

---

## OBSERVER LÀ GÌ?

Tưởng tượng bạn **đăng ký nhận thông báo từ một kênh YouTube**. Khi kênh đăng video mới, tất cả subscriber đều tự động nhận thông báo — kênh không cần biết cụ thể ai đang theo dõi, chỉ cần "phát sóng" và ai đã đăng ký sẽ tự nhận. Muốn không nhận nữa, chỉ cần hủy đăng ký.

Đây chính là ý tưởng cốt lõi của Observer: **định nghĩa một mối quan hệ one-to-many giữa các object, sao cho khi một object thay đổi trạng thái, tất cả các object phụ thuộc vào nó đều được thông báo và cập nhật tự động.**

### Ba ví dụ bài toán thực tế

**Ví dụ 1**

- **Bài toán**: Hệ thống giám sát server: khi CPU vượt 90%, cần gửi email alert, ghi log, và gửi SMS. Nếu code trực tiếp trong class `CpuMonitor`, mỗi lần thêm kênh thông báo mới phải sửa vào class đó — vi phạm Single Responsibility và Open/Closed.
- **Cách Observer giải quyết**: `CpuMonitor` là Subject. `EmailAlerter`, `Logger`, `SmsAlerter` là Observer. Thêm kênh mới chỉ cần tạo Observer mới và đăng ký.

**Ví dụ 2**

- **Bài toán**: Trong ứng dụng UI, khi user thay đổi dữ liệu trong bảng, cần đồng thời cập nhật biểu đồ, tổng kết, và bản xem trước. Nếu bảng biết về biểu đồ và tổng kết, sẽ tạo ra coupling chặt — khó test và bảo trì.
- **Cách Observer giải quyết**: `DataTable` là Subject. `Chart`, `Summary`, `Preview` là Observer. Khi data thay đổi, `DataTable` notify — mỗi Observer tự biết cách cập nhật mình. Đây là cơ sở của kiến trúc MVC/MVP.

**Ví dụ 3**

- **Bài toán**: Hệ thống bán hàng: khi `Product` hết hàng, cần thông báo cho `WishlistService`, `InventoryService`, và `AnalyticsService`. Ba service này độc lập nhau và có thể thêm/bớt tùy business requirement.
- **Cách Observer giải quyết**: `Product` là Subject. Ba service là Observer. `Product` chỉ cần gọi `notifyObservers()` — không cần biết đang notify ai.

```java
import java.util.ArrayList;
import java.util.List;

public interface Observer {
    void update(String productName, int stock);
}

public class Product {
    private List<Observer> observers = new ArrayList<>();
    private String name;
    private int stock;

    public Product(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    public void addObserver(Observer observer) { observers.add(observer); }
    public void removeObserver(Observer observer) { observers.remove(observer); }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(name, stock);
        }
    }

    public void setStock(int stock) {
        this.stock = stock;
        System.out.println("[Product] '" + name + "' tồn kho: " + stock);
        if (stock == 0) notifyObservers();
    }
}

public class WishlistService implements Observer {
    public void update(String productName, int stock) {
        System.out.println("[WishlistService] Báo user theo dõi '" + productName + "': Hết hàng!");
    }
}

public class InventoryService implements Observer {
    public void update(String productName, int stock) {
        System.out.println("[InventoryService] Đặt hàng lại '" + productName + "' từ nhà cung cấp.");
    }
}

public class AnalyticsService implements Observer {
    public void update(String productName, int stock) {
        System.out.println("[AnalyticsService] Ghi nhận hết hàng: '" + productName + "'");
    }
}

// ---- Cách gọi và kiểm chứng ----
public class Demo {
    public static void main(String[] args) {
        Product iphone = new Product("iPhone 15", 5);
        iphone.addObserver(new WishlistService());
        iphone.addObserver(new InventoryService());
        iphone.addObserver(new AnalyticsService());

        iphone.setStock(2);
        // Output: [Product] 'iPhone 15' tồn kho: 2
        // (chưa notify vì stock != 0)

        iphone.setStock(0);
        // Output: [Product] 'iPhone 15' tồn kho: 0
        // Output: [WishlistService] Báo user theo dõi 'iPhone 15': Hết hàng!
        // Output: [InventoryService] Đặt hàng lại 'iPhone 15' từ nhà cung cấp.
        // Output: [AnalyticsService] Ghi nhận hết hàng: 'iPhone 15'
    }
}
```

**Điểm mấu chốt cần nhớ**: `Product` không biết `WishlistService`, `InventoryService` hay `AnalyticsService` là gì — nó chỉ biết chúng implement `Observer`. Thêm một service mới chỉ cần `iphone.addObserver(new NewService())` — không sửa một dòng nào trong `Product`. Đây là **loose coupling** trong hành động.

### Ý nghĩa của Observer

Observer giải quyết bài toán một object cần thông báo cho nhiều object khác khi trạng thái thay đổi, mà không tạo coupling chặt giữa chúng. Nên dùng khi số lượng và loại "người nghe" có thể thay đổi — thêm/bớt tại runtime. Observer là nền tảng của event-driven programming, Pub/Sub pattern, và hầu hết các UI framework (React state, Angular EventEmitter).

---

## BẢNG GHI NHỚ NHANH

| Tên Pattern | 1 câu ghi nhớ ngắn gọn |
|---|---|
| **Singleton** | Chỉ một instance duy nhất — mọi nơi gọi đều trả về cùng một object. |
| **Factory Method** | Subclass quyết định tạo **loại** object nào — lớp cha không cần biết. |
| **Abstract Factory** | Tạo cả **họ** object tương thích nhau — đổi factory là đổi cả bộ. |
| **Builder** | Xây object phức tạp **từng bước** — đặc biệt tốt khi có nhiều field tùy chọn. |
| **Prototype** | Tạo object mới bằng cách **clone** — không khởi tạo lại từ đầu. |
| **Adapter** | **Chuyển đổi** interface không tương thích — như bộ chuyển phích cắm. |
| **Facade** | **Đơn giản hóa** hệ thống phức tạp sau một interface duy nhất, dễ dùng. |
| **Decorator** | **Bọc thêm lớp** tính năng vào object mà không thay đổi class gốc. |
| **Strategy** | **Hoán đổi thuật toán** tại runtime — tách "cái gì" ra khỏi "làm thế nào". |
| **Observer** | **Tự động thông báo** — Subject phát, Observer nhận, không cần biết nhau. |
