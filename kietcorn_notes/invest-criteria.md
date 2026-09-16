# INVEST Criteria

INVEST là **6 tiêu chí đánh giá chất lượng của một User Story** trong Agile/Scrum. Một User Story tốt phải đáp ứng cả 6 tiêu chí này. Tên INVEST là viết tắt của 6 chữ cái đầu.

> **User Story là gì?** Là một mô tả ngắn về tính năng từ góc độ người dùng, thường theo format:
> `"Với tư cách là [ai], tôi muốn [làm gì], để [đạt được gì]"`

---

## I — Independent (Độc lập)

**Ý nghĩa**: Các User Story không nên phụ thuộc lẫn nhau. Mỗi story có thể được phát triển, test, và release độc lập.

**Tại sao quan trọng**: Nếu story A phải hoàn thành trước story B mới làm được, team không thể ưu tiên linh hoạt, không thể làm song song — sprint planning trở nên cứng nhắc.

**Vi phạm**:
> Story 1: "User có thể đăng ký tài khoản"
> Story 2: "User có thể đăng nhập" *(phải có Story 1 xong mới làm được)*

**Cách sửa**: Gộp thành 1 story hoặc tách để story 2 không phụ thuộc story 1 về mặt implementation.

**Dấu hiệu nhận biết**: Nếu trong lúc estimate bạn nói "cái này phải làm sau cái kia" — khả năng cao đang vi phạm Independent.

---

## N — Negotiable (Có thể thương lượng)

**Ý nghĩa**: User Story **không phải là hợp đồng cứng nhắc**. Nó là điểm khởi đầu cho cuộc trò chuyện giữa dev và business. Chi tiết có thể được điều chỉnh cho đến khi story được chọn vào sprint.

**Tại sao quan trọng**: Nếu story quá chi tiết và cứng nhắc từ đầu, dev mất đi sự linh hoạt để đề xuất giải pháp tốt hơn. Business cũng mất cơ hội điều chỉnh khi hiểu rõ hơn về technical constraint.

**Vi phạm**: Story mô tả chi tiết đến từng pixel UI, từng API endpoint, từng table database — không còn chỗ để thương lượng.

**Đúng**: Story ghi rõ **what** (cần gì) và **why** (tại sao), không ghi **how** (làm thế nào). "How" là việc của dev trong quá trình refinement.

**Ví dụ tốt**:
> "Với tư cách là khách hàng, tôi muốn tìm kiếm sản phẩm theo tên, để nhanh chóng tìm thấy thứ mình cần."
*(Không ghi: phải dùng Elasticsearch, debounce 300ms, hiển thị 10 kết quả...)*

---

## V — Valuable (Có giá trị)

**Ý nghĩa**: Mỗi story phải mang lại **giá trị rõ ràng cho người dùng hoặc business**. Không có story nào chỉ là "task kỹ thuật" thuần túy.

**Tại sao quan trọng**: Nếu story không có giá trị người dùng, tại sao làm? Business cần thấy được lợi ích để ưu tiên và quyết định đầu tư.

**Vi phạm**:
> "Refactor class UserService" ← đây là technical task, không phải user story

**Cách sửa**: Gắn technical work vào giá trị business:
> "Với tư cách là dev team, chúng tôi muốn refactor UserService, để thời gian xử lý đăng ký giảm xuống dưới 200ms, giúp user có trải nghiệm mượt mà hơn."

**Dấu hiệu nhận biết**: Nếu không thể điền vào phần "để [đạt được gì]" một cách có ý nghĩa — story thiếu giá trị.

---

## E — Estimable (Có thể ước lượng)

**Ý nghĩa**: Team dev phải có thể **ước lượng được độ phức tạp/thời gian** để hoàn thành story. Nếu không estimate được, story cần được làm rõ hoặc tách nhỏ hơn.

**Tại sao quan trọng**: Estimate là đầu vào cho sprint planning. Story không estimate được = không lên kế hoạch được = sprint chaos.

**Tại sao story không estimate được?**
1. **Quá mơ hồ**: "Cải thiện hiệu năng hệ thống" — cải thiện bao nhiêu? cái gì?
2. **Quá lớn (Epic)**: "Xây dựng toàn bộ module thanh toán"
3. **Team thiếu kiến thức**: Chưa biết technology cần dùng → cần spike/research trước

**Cách sửa**: Làm rõ acceptance criteria, tách nhỏ story, hoặc tạo spike story để research trước.

---

## S — Small (Đủ nhỏ)

**Ý nghĩa**: Story phải đủ nhỏ để **hoàn thành trong 1 sprint** (thường 1-2 tuần). Nếu quá lớn, nó là **Epic** và cần tách nhỏ.

**Tại sao quan trọng**: Story lớn → khó estimate → khó track progress → hay bị "gần xong mãi không xong" → sprint không deliver được value.

**Nguyên tắc thực tế**: Một story lý tưởng hoàn thành trong **1-3 ngày**. Story > 1 tuần thường là dấu hiệu cần tách.

**Vi phạm** (đây là Epic, không phải Story):
> "Xây dựng tính năng đăng ký/đăng nhập cho user"

**Sau khi tách thành Stories nhỏ**:
> Story 1: "User có thể đăng ký bằng email và password"
> Story 2: "User nhận email xác nhận sau khi đăng ký"
> Story 3: "User có thể đăng nhập bằng email và password"
> Story 4: "User có thể reset password qua email"

**Dấu hiệu cần tách**: Story có nhiều "và" trong mô tả — mỗi "và" thường là 1 story riêng.

---

## T — Testable (Có thể kiểm thử)

**Ý nghĩa**: Phải có **tiêu chí rõ ràng để xác định story đã hoàn thành hay chưa** (Acceptance Criteria). Nếu không biết test thế nào, không biết "xong" là gì.

**Tại sao quan trọng**: Không có acceptance criteria → dev không biết dừng lại ở đâu → scope creep → "xong" không rõ ràng → tranh cãi khi review.

**Vi phạm**:
> "User có trải nghiệm tốt khi thanh toán" ← "tốt" là gì? không test được

**Sau khi áp dụng Testable**:
> **Story**: "User có thể thanh toán bằng thẻ tín dụng"
> **Acceptance Criteria**:
> - [ ] User nhập được số thẻ, ngày hết hạn, CVV
> - [ ] Thanh toán thành công hiển thị thông báo xác nhận trong vòng 3 giây
> - [ ] Thanh toán thất bại hiển thị thông báo lỗi rõ ràng
> - [ ] Email xác nhận đơn hàng được gửi trong vòng 1 phút

**Liên hệ thực tế**: Acceptance Criteria chính là đầu vào để viết test case (QA) và viết unit/integration test (dev).

---

## Tổng kết INVEST

| Chữ | Tên | Ghi nhớ 1 câu |
|-----|-----|---------------|
| **I** | Independent | Mỗi story tự làm được, không chờ story khác. |
| **N** | Negotiable | Story là điểm khởi đầu thảo luận, không phải hợp đồng. |
| **V** | Valuable | Mỗi story phải có giá trị rõ ràng cho user/business. |
| **E** | Estimable | Team estimate được — nếu không thì cần làm rõ hơn. |
| **S** | Small | Xong trong 1 sprint — nếu không thì tách nhỏ ra. |
| **T** | Testable | Có Acceptance Criteria rõ ràng — biết "xong" là thế nào. |

---

## SOLID vs INVEST — Dùng ở đâu?

| | SOLID | INVEST |
|---|---|---|
| **Dùng cho** | Thiết kế code / class / architecture | Viết và đánh giá User Story trong Agile |
| **Ai áp dụng** | Dev / Architect | Product Owner, Scrum Master, Dev team |
| **Mục tiêu** | Code dễ bảo trì, dễ mở rộng | Story rõ ràng, có thể deliver được |
| **Khi nào dùng** | Lúc code review, thiết kế hệ thống | Lúc sprint planning, backlog refinement |

> **Tóm lại**: SOLID giúp bạn viết code tốt. INVEST giúp bạn định nghĩa đúng cần làm gì trước khi code. Cả hai là kỹ năng cần có trong môi trường Agile chuyên nghiệp.
