# Asset Management System (Java Swing + MySQL)

## Mô tả dự án

Quản lý mượn/trả thiết bị trong công ty với các chức năng:

- CRUD các bảng (Phòng ban, Nhân viên, Thiết bị, Danh mục, Nhà cung cấp...)
- Tạo và duyệt ticket mượn/trả thiết bị
- Phân quyền: Admin, Manager, Nhân viên
- Quản lý trạng thái thiết bị, kiểm soát nghiệp vụ qua trigger
- Giao diện người dùng bằng Java Swing

## Yêu cầu hệ thống

- Java JDK 8 trở lên
- MySQL 8+
- IDE: IntelliJ IDEA, NetBeans, Eclipse, VS Code...
- Thư viện JDBC cho MySQL (mysql-connector-java)

## Hướng dẫn cài đặt

### 1. Cài đặt MySQL

- Tải và cài đặt MySQL Server: https://dev.mysql.com/downloads/mysql/

### 2. Chạy migration database

Để tạo database và migrate, sử dụng:

```bash
mysql -u root -p < src/main/java/database/init.sql
```

(Nhập mật khẩu khi được hỏi)

### 3. Nạp dữ liệu mẫu (seed data)

Sau khi migration xong, để thêm dữ liệu mẫu cho các bảng (phục vụ test/demo), chạy:

```bash
mysql -u root -p < src/main/java/database/sample_data.sql
```

(Có thể chỉnh sửa file sample_data.sql nếu muốn thay đổi dữ liệu mẫu)

### 3. Hướng dẫn cấu hình MySQL với Maven

#### Thêm MySQL Connector vào Maven

Tải Maven: https://maven.apache.org/download.cgi

File: apache-maven-3.9.11-bin.zip

Sau đó, chạy lệnh sau để tải dependencies:

```bash
mvn dependency:resolve
```

#### Cấu hình kết nối MySQL qua file .env

1. Copy file `.env.example` thành `.env` ở thư mục gốc project (nếu chưa có).
2. Chỉnh sửa thông tin kết nối phù hợp với máy của bạn:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=asset_management
DB_USER=root
DB_PASSWORD=your_password
```

> **Lưu ý:** Đảm bảo điền đúng thông tin, không để trống password nếu MySQL có mật khẩu. Ứng dụng sẽ tự động đọc file `.env` khi chạy.

### 4. Chạy ứng dụng

- Mở project trong IDE
- Đảm bảo đã thêm thư viện JDBC/MySQL Connector
- Ở root folder, build project:
  ```bash
  mvn compile
  ```
- Chạy ứng dụng bằng Maven:
  ```bash
  mvn exec:java -Dexec.mainClass=App
  ```
- Hoặc chạy file `App.java` trong IDE (nằm trong package `main`)

## Cấu trúc thư mục

```
src/
  main/
    java/
      constants/
      controllers/
        device/
        main/
        user/
      dao/
      database/
      exceptions/
      models/
      services/
      utils/
      views/
        common/
        device/
        main/
        user/
      config/
        EnvConfig.java
        MySQLConnection.java
      App.java
    resources/
      database/
        init.sql
        sample_data.sql
      icons/
      images/
  test/
```

## Phân quyền chức năng

- **Admin**: Quản lý toàn bộ hệ thống, duyệt mọi ticket, CRUD tất cả bảng
- **Manager**: Quản lý phòng ban của mình, tạo/duyệt ticket liên quan
- **Nhân viên**: Tạo ticket, xem thiết bị đang mượn

## Lưu ý khi chuyển sang MySQL

- Một số cú pháp (trigger, identity, constraint, raiserror...) đã được chỉnh lại cho phù hợp với MySQL.
- Đã thay `IDENTITY(1,1)` bằng `AUTO_INCREMENT`.
- Đã thay `NVARCHAR` bằng `VARCHAR` hoặc `TEXT`.
- Đã thay `RAISERROR` bằng `SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = ...`.
- Trigger MySQL đã được viết lại đúng cú pháp.
- Xem file `init.sql` đã chỉnh sửa cho MySQL trong thư mục `src/main/database/`.

## Design Patterns

Dự án áp dụng nhiều mẫu thiết kế (Design Patterns) nổi bật nhằm tăng tính mở rộng, dễ bảo trì và chuẩn hóa kiến trúc phần mềm:

- **DAO (Data Access Object):**

  - Tách biệt logic truy xuất dữ liệu với logic nghiệp vụ, giúp việc thay đổi nguồn dữ liệu (MySQL, file, v.v.) dễ dàng hơn.
  - Mỗi entity (ví dụ: Thiết bị, Nhân viên, Phòng ban) đều có lớp DAO riêng để thực hiện các thao tác CRUD.

- **MVC (Model - View - Controller):**

  - Tổ chức code theo 3 lớp: Model (dữ liệu, nghiệp vụ), View (giao diện), Controller (điều phối luồng xử lý).
  - Giúp giao diện và logic xử lý tách biệt, dễ mở rộng và kiểm thử.

- **Singleton (Kết nối Database):**

  - Đảm bảo chỉ có một kết nối tới database trong suốt vòng đời ứng dụng, tránh lãng phí tài nguyên.
  - Lớp `MySQLConnection` được triển khai theo pattern này.

- **Factory:**
  - Tạo các đối tượng (ví dụ: View, Controller) một cách linh hoạt, ẩn đi chi tiết khởi tạo.
  - Giúp dễ dàng mở rộng khi thêm loại đối tượng mới mà không cần sửa code cũ.

Việc áp dụng các pattern này giúp dự án có cấu trúc rõ ràng, dễ bảo trì, mở rộng và tuân thủ các nguyên tắc lập trình hướng đối tượng.

## Đóng góp

- Fork, tạo branch mới, pull request nếu muốn đóng góp code.

## Liên hệ

- Nhóm phát triển:
  - Trương Công Hiếu
  - Trịnh Bảo Hoàng
  - Lê Văn Lâm
  - Tạ Kim Phúc

---

Nếu gặp lỗi hoặc cần hỗ trợ, vui lòng liên hệ nhóm phát triển hoặc tạo issue trên repository.

## License

This project is licensed under the MIT License.
