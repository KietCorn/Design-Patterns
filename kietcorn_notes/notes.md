23 Design Patterns trong GoF, có 5 Creational, 7 Structural và 11 Behavioral
Class — "Bản thiết kế": Là đoạn code định nghĩa cấu trúc (có gì) và hành vi (làm được gì) của một loại đối tượng nào đó. Bản thân class không chiếm bộ nhớ để lưu dữ liệu thực tế — nó chỉ là khuôn mẫu.
Object — "Thực thể cụ thể": Là một thực thể tồn tại thật trong bộ nhớ, được tạo ra dựa trên class. Mỗi object có dữ liệu riêng của nó.

Interface giống như một **bản mô tả công việc (job description)** khi tuyển dụng. VD: "Vị trí tài xế — yêu cầu: phải biết lái xe". Bất kỳ ai ứng tuyển vị trí này (Car, ElectricCar, Motorbike...) đều  **phải biết lái xe** , nhưng **cách lái cụ thể có thể khác nhau** — Car lái khác Motorbike, nhưng cả 2 đều đáp ứng đúng yêu cầu "biết lái"

interface Vehicle          ← định nghĩa "hợp đồng": phải có drive()
      ↑ implements
Car, ElectricCar, HybridCar ← các class thực thi hợp đồng đó, mỗi class code khác nhau

Vehicle v = factory.create(...)   ← biến khai báo kiểu interface
v.drive()                          ← POLYMORPHISM: chạy đúng code của class thực tế
                                      (Car/ElectricCar/HybridCar) mà không cần biết trước là class nào

Observer: xoanh quay observer và subject để quan sát

I. Introduction

- Design Pattern giúp giải quyết vấn đề hiệu quả và tối ưu cho việc lập trình
- Tăng tính tái sử dụng của code
- Tăng tính bảo trì, bảo dưỡng và dễ hiểu
- Trước khi giải quyết vấn đề, cần hiểu rõ vấn đề cần giải quyết, từ đó chọn ra Design Pattern phù hợp nhất
- Phải hiểu Design Pattern, cần linh hoạt áp dụng với nhu cầu của bài toán

1. Singleton

- Singleton là một Design Pattern thuộc nhóm Creational Patterns
- Mục đích cốt lõi là đảm bảo một class chỉ có duy nhất một instance và cung cấp một điểm truy cập toàn cục tới instance đó.
- VD: Đồng hồ treo tường trong lớp sẽ thống nhất giờ của cả lớp với nhau, thay vì mỗi người có 1 cái đồng hồ riêng
  -> Single Source of Truth, toàn bộ các phần khác phải tuân theo để đảm bảo nhất quán
- Singleton ra đời với một tôn chỉ duy nhất: "There can be only one"
- Nó giải quyết vấn đề bằng cách tự mình quản lý chính mình.
- Nó chặn đứng mọi nỗ lực khởi tạo tràn lan từ bên ngoài (thông qua private constructor) và chỉ cung cấp một cổng truy cập duy nhất (static method) để truy cập vào tài nguyên chung.
- Chỉ có DUY NHẤT một đối tượng tồn tại trong suốt vòng đời ứng dụng.
  Không cho việc sử dụng new từ bên ngoài

2. Facade Pattern
   Che dấu đi sự phức tạp của kiến trúc bên trong, gọi function để thực thi hành vi
3. Proxy Pattern
