# Enterprise Asset Management System (Java Swing, Hibernate, Maven, MySQL)

## Mô tả dự án

Quản lý mượn/trả thiết bị trong công ty với các chức năng:

- CRUD các bảng (Phòng ban, Nhân viên, Thiết bị, Danh mục, Nhà cung cấp...)
- Tạo và duyệt ticket mượn/trả thiết bị
- Phân quyền: Admin, Manager, Nhân viên
- Quản lý trạng thái thiết bị, kiểm soát nghiệp vụ qua trigger
- Giao diện người dùng bằng Java Swing

## Yêu cầu hệ thống

- Java JDK 8 trở lên (Recommend JDK 17)
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
mysql -u root -p < src/main/java/database/sample_data_v3__no_request_data.sql
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
  mvn exec:java -Dexec.mainClass=com.assetmanagement.Application
  ```
- Hoặc chạy file `Application.java` (class chính) trong IDE.

#### (Tuỳ chọn) Dev auto-login

Chỉ dùng trong môi trường phát triển để bỏ qua màn hình đăng nhập (tuyệt đối **không bật trên production**):

```bash
mvn -Ddev.autoLogin=true exec:java -Dexec.mainClass=com.assetmanagement.Application
```

Mặc định sẽ đăng nhập bằng tài khoản fallback được khai báo trong code (`nguyenvana1 / matkhau1` nếu chưa seed DB). Có thể đổi username/password tạm thời:

```bash
mvn -Ddev.autoLogin=true -Ddev.autoLogin.user=manager -Ddev.autoLogin.pass=12345 exec:java -Dexec.mainClass=com.assetmanagement.Application
```

Muốn tắt, chỉ cần bỏ cờ `-Ddev.autoLogin=true`.

### 5. Kiểm thử tự động

- Đảm bảo đã build project trước (nếu chưa, chạy `mvn compile`).
- Để chạy toàn bộ unit test, sử dụng lệnh sau ở thư mục gốc:
  ```bash
  mvn test
  ```
- Kết quả test sẽ hiển thị trên terminal và lưu trong thư mục `target/surefire-reports/`.
- Các file test nằm trong `src/test/java/`.
- Sử dụng JDK 17 (cập nhật JAVA_HOME nếu cần thiết) nếu gặp lỗi với Mockito inline

Tài liệu tham khảo

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://site.mockito.org/)

## Cấu trúc thư mục

Cấu trúc mã nguồn được tổ chức theo các tầng rõ ràng: `config`, `infrastructure`, `domain`, `security`, `presentation`, `common`. Gốc package thống nhất: `com.assetmanagement`.

```
src/
  main/
    java/
      com/assetmanagement/
        Application.java                # Điểm vào ứng dụng (main class)
        config/
          EnvConfig.java
          HibernateUtil.java
        infrastructure/
          database/
            MySQLConnection.java        # Kết nối JDBC thuần
          persistence/
            repository/                 # Interface (trước đây: dao/*/interfaces)
              device/
              main/
            dao/                        # Triển khai (trước đây: dao/*Impl)
              device/
              main/
        domain/
          model/
            device/                     # Asset, AssetCategory, AssetRequest, ...
            main/                       # Employee, Department, ...
          service/
            device/
            main/
            user/                       # Dịch vụ nghiệp vụ theo nhóm
        security/
          AuthenticationService.java
          UserSession.java
        presentation/
          controller/
            device/
            main/
            user/
          ui/
            views/
              device/
              main/
              user/
              common/
            components/                 # Thành phần UI tái sử dụng
        common/
          constants/
            AppConstants.java
          exception/
            DataAccessException.java
            NotFoundException.java
            ValidationException.java
          util/
            DateUtils.java
            StringUtils.java
            ValidationUtils.java
            UIUtils.java
            UITheme.java
    resources/
      hibernate.cfg.xml
      sql/                               # init.sql, sample_data*.sql
      icons/
      images/
  test/
    java/ (phản chiếu cùng package com.assetmanagement.* cho các lớp test)
```
`exec-maven-plugin` được cấu hình với `mainClass = com.assetmanagement.Application`.

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

## Design Patterns & Kiến trúc dự án

Dự án áp dụng nhiều mẫu thiết kế (Design Patterns) nổi bật nhằm tăng tính mở rộng, dễ bảo trì và chuẩn hóa kiến trúc phần mềm:

- **DAO (Data Access Object):**

  - Tách biệt logic truy xuất dữ liệu với logic nghiệp vụ, giúp việc thay đổi nguồn dữ liệu (MySQL, file, v.v.) dễ dàng hơn.
  - Mỗi entity (ví dụ: Thiết bị, Nhân viên, Phòng ban) đều có lớp DAO riêng để thực hiện các thao tác CRUD. (Ví dụ: `AssetDAO`)

- **MVC (Model - View - Controller):**

  - Tổ chức code theo 3 lớp: Model (dữ liệu, nghiệp vụ), View (giao diện), Controller (điều phối luồng xử lý)
  - Giúp giao diện và logic xử lý tách biệt, dễ mở rộng và kiểm thử. (Ví dụ: `AssetController`, `AssetService`, các view trong `views/`)

- **Singleton (Kết nối Database):**

  - Đảm bảo chỉ có một kết nối tới database trong suốt vòng đời ứng dụng, tránh lãng phí tài nguyên.
  - Lớp `MySQLConnection` được triển khai theo pattern này.

- **Factory:**
  - Tạo các đối tượng (ví dụ: View, Controller) một cách linh hoạt, ẩn đi chi tiết khởi tạo.
  - Giúp dễ dàng mở rộng khi thêm loại đối tượng mới mà không cần sửa code cũ.

## Tiện ích (Utilities) & Constants

- **AppConstants:** Quản lý các hằng số dùng chung cho toàn bộ ứng dụng (trạng thái thiết bị, thông báo, vai trò, định dạng ngày tháng, ...).
- **DateUtils, StringUtils, ValidationUtils:** Các hàm tiện ích hỗ trợ xử lý ngày tháng, chuỗi, kiểm tra dữ liệu đầu vào (validate email, số điện thoại, ...).

## Xử lý Exception

- **Custom Exception:**
  - `DataAccessException`, `NotFoundException`, `ValidationException` giúp quản lý lỗi rõ ràng, dễ kiểm soát và debug hơn trong quá trình phát triển.

Việc áp dụng các pattern, tiện ích và exception này giúp dự án có cấu trúc rõ ràng, dễ bảo trì, mở rộng và tuân thủ các nguyên tắc lập trình hướng đối tượng.

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
