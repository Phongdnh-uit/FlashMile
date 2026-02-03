# ADR-001: Thành lập kiến trúc hệ thống cơ bản

## Status

- Proposed
- Date: 1-2-2026

---

## Context

Hệ thống giải quyết bài toán ứng dụng giao hàng chặng cuối (last-mile delivery) cho các đơn hàng thương mại điện tử.

Bối cảnh và constraints:

- Backend sẽ được phát triển bằng Java 21 + Spring Boot.
- Team có 2 dev backend và 1 dev frontend
- Hệ thống dạng monolithic ban đầu, có thể tách microservices trong tương lai, có thể đạt tới trạng thái hyper-scale

Vấn đề cần giải quyết:

- Chọn kiến trúc phù hợp để phát triển nhanh, dễ bảo trì và mở rộng
- Không over-engineer trong giai đoạn đầu

---

## Decision

**Chúng ta quyết định chọn:**

> Monolithic với Clean Architecture kết hợp CQRS cho các module read-heavy.
> Một service non-blocking I/O riêng cho các endpoints cần giao tiếp với websocket hoặc sse để cập nhật trạng thái giao hàng real-time.

---

## Options Considered

Các phương án đã cân nhắc:

1. **Option A – Layered Architecture (Kiến trúc nhiều lớp)**
2. **Option B – Clean Architecture mà không dùng CQRS**
3. **Option C – Clean Architecture kết hợp CQRS**
4. **Option D – Microservices từ đầu**

---

## Pros and Cons

### Option A – Layered Architecture

**Pros**

- Dễ hiểu, phổ biến với nhiều dev
- Triển khai nhanh
- Đơn giản cho các hệ thống nhỏ

**Cons**

- Khó mở rộng khi hệ thống phức tạp
- Dễ dẫn đến tight coupling giữa các lớp
- Khó bảo trì khi domain logic tăng lên

---

### Option B – Clean Architecture mà không dùng CQRS

**Pros**

- Tách biệt rõ ràng giữa các layer
- Dễ bảo trì và mở rộng
- Giúp test dễ dàng hơn

**Cons**

- Có thể phức tạp hơn so với Layered Architecture
- Không tối ưu cho các module read-heavy
- Đòi hỏi dev phải hiểu rõ về kiến trúc

---

### Option C – Clean Architecture kết hợp CQRS

**Pros**

- Tối ưu cho các module read-heavy
- Tách biệt rõ ràng giữa command và query
- Dễ bảo trì và mở rộng theo thời gian
- Hỗ trợ tốt cho các yêu cầu real-time

**Cons**

- Phức tạp hơn trong việc thiết kế và triển khai
- Cần dev hiểu rõ về CQRS và Clean Architecture
- Có thể dẫn đến việc duplicate code giữa command và query
- Over-engineering nếu hệ thống quá đơn giản

### Option D – Microservices từ đầu

**Pros**

- Tách biệt rõ ràng giữa các dịch vụ
- Dễ mở rộng độc lập từng phần
- Phù hợp với hệ thống lớn, phức tạp

**Cons**

- Quản lý phức tạp hơn (deployment, communication, data consistency)
- Overhead về tài nguyên và chi phí vận hành
- Không cần thiết trong giai đoạn đầu khi hệ thống chưa phức tạp
- Yêu cầu team có kinh nghiệm với microservices
- Khó khăn trong việc debug và test tích hợp
- Quá overkill cho hệ thống ban đầu

---

## Decision Rationale

Chúng ta chọn **Option C – Clean Architecture kết hợp CQRS** vì:
- Phù hợp với bối cảnh hệ thống hiện tại và tương lai
- Giúp team phát triển nhanh, dễ bảo trì và mở rộng
- Hỗ trợ tốt cho các yêu cầu real-time trong ứng dụng giao hàng
- Trade-offs chấp nhận được so với lợi ích mang lại

Giải pháp này cân bằng giữa:
- Đơn giản trong giai đoạn đầu
- Khả năng mở rộng và bảo trì trong tương lai
- Chất lượng code và hiệu suất hệ thống

---

## Consequences

Hệ quả sau khi áp dụng quyết định này:

**Tích cực**
- Kiến trúc rõ ràng, dễ hiểu
- Dễ bảo trì và mở rộng theo thời gian
- Tối ưu cho các module read-heavy và real-time
- Giúp team phát triển nhanh chóng và hiệu quả

**Tiêu cực / Trade-offs**
- Cần thời gian để team làm quen với CQRS và Clean Architecture
- Phức tạp hơn trong việc thiết kế và triển khai ban đầu
- Có thể dẫn đến việc duplicate code giữa command và query
- Yêu cầu quản lý tốt hơn về kiến trúc và coding standards
- Over-engineering nếu hệ thống quá đơn giản trong giai đoạn đầu

---

## Guardrails / Principles

Các nguyên tắc bắt buộc để giữ đúng tinh thần quyết định:
- Domain không phụ thuộc framework, không import Spring, JPA, Lombok.
- Application layer không bị bypass cho command, tất cả command phải đi qua application service.
- Chỉ áp dụng CQRS cho các module query đủ phức tạp, tránh over-engineer.
- Query (read) có thể bypass domain layer nếu đơn giản và performance-critical.

---

## Follow-up Actions

Những việc cần làm tiếp theo:

- [ ] Viết coding guideline
- [ ] Review kiến trúc sau 1 tháng

---

## Related ADRs

<!-- - ADR-YYY: <Tên liên quan> (nếu có) -->

---

## Notes

Ghi chú thêm (optional)
