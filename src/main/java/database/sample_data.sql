-- =================================================================
-- PHẦN 0: THIẾT LẬP KẾT NỐI VÀ DATABASE
-- =================================================================
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;
SET sql_mode = '';

USE asset_management;

-- Xóa dữ liệu cũ trong các bảng để tránh xung đột
-- Lưu ý: Thứ tự xóa rất quan trọng để không vi phạm ràng buộc khóa ngoại
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE AssetRequestItem;
TRUNCATE TABLE AssetRequest;
TRUNCATE TABLE Asset;
TRUNCATE TABLE Vendor;
TRUNCATE TABLE AssetCategory;
TRUNCATE TABLE Employee;
TRUNCATE TABLE Department;
SET FOREIGN_KEY_CHECKS = 1;

-- =================================================================
-- PHẦN 1: TẠO DỮ LIỆU CHO PHÒNG BAN VÀ NHÂN VIÊN
-- =================================================================

-- Dữ liệu cho 10 phòng ban
INSERT INTO `Department` (`department_id`, `department_name`, `head_employee_id`) VALUES
(1, 'Ban Giám đốc', NULL),
(2, 'Phòng Kế toán', NULL),
(3, 'Phòng Nhân sự', NULL),
(4, 'Phòng IT', NULL),
(5, 'Phòng Kinh doanh', NULL),
(6, 'Phòng Marketing', NULL),
(7, 'Phòng Hành chính', NULL),
(8, 'Phòng Dự án', NULL),
(9, 'Phòng Chăm sóc khách hàng', NULL),
(10, 'Phòng Kỹ thuật', NULL);

-- Dữ liệu cho nhân viên (khoảng 60 người)
-- first_name: Tên
-- last_name: Họ và tên lót
-- username: firstname + first character of lastname
-- Mật khẩu cho tất cả tài khoản là: 12345
INSERT INTO `Employee` (`employee_id`, `last_name`, `first_name`, `email`, `phone_number`, `department_id`, `role`, `username`, `password`, `status`) VALUES
-- Tài khoản đặc biệt
(1, 'Hệ thống', 'Quản trị', 'admin@company.com', '0901112220', 4, 'Admin', 'admin', '12345', 'Active'),
(2, 'Cấp cao', 'Quản lý', 'manager@company.com', '0901112221', 5, 'Manager', 'manager', '12345', 'Active'),
(3, 'Văn phòng', 'Nhân viên', 'staff@company.com', '0901112222', 5, 'Staff', 'staff', '12345', 'Active'),

-- Ban Giám đốc (Department ID: 1)
(4, 'An', 'Nguyễn Văn', 'an.nv@company.com', '0910000001', 1, 'Manager', 'annv', '12345', 'Active'), -- Giám đốc
(5, 'Bình', 'Trần Thị', 'binh.tt@company.com', '0910000002', 1, 'Staff', 'binhtt', '12345', 'Active'), -- Trợ lý

-- Phòng Kế toán (Department ID: 2)
(6, 'Huyền', 'Lê Thị', 'huyen.lt@company.com', '0912345678', 2, 'Manager', 'huyenlt', '12345', 'Active'),
(7, 'Minh', 'Trần Văn', 'minh.tv@company.com', '0912345679', 2, 'Staff', 'minhtv', '12345', 'Active'),
(8, 'Yến', 'Hoàng Thị', 'yen.ht@company.com', '0912345680', 2, 'Staff', 'yenht', '12345', 'Active'),
(9, 'Dũng', 'Vũ Văn', 'dung.vv@company.com', '0912345681', 2, 'Staff', 'dungvv', '12345', 'Active'),

-- Phòng Nhân sự (Department ID: 3)
(10, 'Dung', 'Phạm Thị', 'dung.pt@company.com', '0987654321', 3, 'Manager', 'dungpt', '12345', 'Active'),
(11, 'An', 'Nguyễn Hữu', 'an.nh@company.com', '0987654322', 3, 'Staff', 'annh', '12345', 'Active'),
(12, 'Mai', 'Vũ Thị', 'mai.vt@company.com', '0987654323', 3, 'Staff', 'maivt', '12345', 'Active'),
(13, 'Hương', 'Đỗ Thị', 'huong.dt@company.com', '0987654324', 3, 'Staff', 'huongdt', '12345', 'Active'),
(14, 'Nam', 'Bùi Văn', 'nam.bv@company.com', '0987654325', 3, 'Staff', 'nambv', '12345', 'Active'),

-- Phòng IT (Department ID: 4)
(15, 'Tùng', 'Hoàng Văn', 'tung.hv@company.com', '0901112223', 4, 'Manager', 'tunghv', '12345', 'Active'),
(16, 'Đức', 'Phan Văn', 'duc.pv@company.com', '0901112224', 4, 'Staff', 'ducpv', '12345', 'Active'),
(17, 'Linh', 'Lưu Thị', 'linh.lt@company.com', '0901112225', 4, 'Staff', 'linhlt', '12345', 'Active'),
(18, 'Giang', 'Đinh Văn', 'giang.dv@company.com', '0901112226', 4, 'Staff', 'giangdv', '12345', 'Active'),
(19, 'Trang', 'Ngô Thị', 'trang.nt@company.com', '0901112227', 4, 'Staff', 'trangnt', '12345', 'Active'),

-- Phòng Kinh doanh (Department ID: 5)
(20, 'Long', 'Đặng Văn', 'long.dv@company.com', '0911223344', 5, 'Manager', 'longdv', '12345', 'Active'),
(21, 'Ngọc', 'Bùi Thị', 'ngoc.bt@company.com', '0911223345', 5, 'Staff', 'ngocbt', '12345', 'Active'),
(22, 'Sơn', 'Lý Văn', 'son.lv@company.com', '0911223346', 5, 'Staff', 'sonlv', '12345', 'Active'),
(23, 'Thảo', 'Trịnh Thị', 'thao.tt@company.com', '0911223347', 5, 'Staff', 'thaott', '12345', 'Active'),
(24, 'Phúc', 'Hồ Văn', 'phuc.hv@company.com', '0911223348', 5, 'Staff', 'phuchv', '12345', 'Active'),
(25, 'Lan', 'Mai Thị', 'lan.mt@company.com', '0911223349', 5, 'Staff', 'lanmt', '12345', 'Active'),

-- Phòng Marketing (Department ID: 6)
(26, 'Hà', 'Ngô Thị', 'ha.nt@company.com', '0955667788', 6, 'Manager', 'hant', '12345', 'Active'),
(27, 'Kiên', 'Trịnh Văn', 'kien.tv@company.com', '0955667789', 6, 'Staff', 'kientv', '12345', 'Active'),
(28, 'Ánh', 'Dương Thị', 'anh.dt@company.com', '0955667790', 6, 'Staff', 'anhdt', '12345', 'Active'),
(29, 'Quân', 'Lại Văn', 'quan.lv@company.com', '0955667791', 6, 'Staff', 'quanlv', '12345', 'Active'),

-- Phòng Hành chính (Department ID: 7)
(30, 'Tâm', 'Vương Thị', 'tam.vt@company.com', '0922334455', 7, 'Manager', 'tamvt', '12345', 'Active'),
(31, 'Hải', 'Tô Văn', 'hai.tv@company.com', '0922334456', 7, 'Staff', 'haitv', '12345', 'Active'),
(32, 'Nga', 'Chu Thị', 'nga.ct@company.com', '0922334457', 7, 'Staff', 'ngact', '12345', 'Active'),

-- Phòng Dự án (Department ID: 8)
(33, 'Mạnh', 'Phí Văn', 'manh.pv@company.com', '0933445566', 8, 'Manager', 'manhpv', '12345', 'Active'),
(34, 'Thu', 'Tạ Thị', 'thu.tt@company.com', '0933445567', 8, 'Staff', 'thutt', '12345', 'Active'),
(35, 'Hậu', 'Đoàn Văn', 'hau.dv@company.com', '0933445568', 8, 'Staff', 'haudv', '12345', 'Active'),
(36, 'Kim', 'Uông Thị', 'kim.ut@company.com', '0933445569', 8, 'Staff', 'kimut', '12345', 'Active'),

-- Phòng Chăm sóc khách hàng (Department ID: 9)
(37, 'Thủy', 'Giang Thị', 'thuy.gt@company.com', '0944556677', 9, 'Manager', 'thuygt', '12345', 'Active'),
(38, 'Thắng', 'Nhữ Văn', 'thang.nv@company.com', '0944556678', 9, 'Staff', 'thangnv', '12345', 'Active'),
(39, 'Loan', 'Quách Thị', 'loan.qt@company.com', '0944556679', 9, 'Staff', 'loanqt', '12345', 'Active'),
(40, 'Phong', 'Âu Dương', 'phong.ad@company.com', '0944556680', 9, 'Staff', 'phongad', '12345', 'Active'),
(41, 'Lý', 'Kha Thị', 'ly.kt@company.com', '0944556681', 9, 'Staff', 'lykt', '12345', 'Active'),

-- Phòng Kỹ thuật (Department ID: 10)
(42, 'Khoa', 'Mạc Văn', 'khoa.mv@company.com', '0966778899', 10, 'Manager', 'khoamv', '12345', 'Active'),
(43, 'Dương', 'Ninh Văn', 'duong.nv@company.com', '0966778900', 10, 'Staff', 'duongnv', '12345', 'Active'),
(44, 'Nguyệt', 'Xa Thị', 'nguyet.xt@company.com', '0966778901', 10, 'Staff', 'nguyetxt', '12345', 'Active'),
(45, 'Sơn', 'Thạch Văn', 'son.tv@company.com', '0966778902', 10, 'Staff', 'sontv', '12345', 'Active'),
(46, 'Quỳnh', 'Tôn Nữ', 'quynh.tn@company.com', '0966778903', 10, 'Staff', 'quynhtn', '12345', 'Active'),
(47, 'Bảo', 'Nguyễn Văn', 'bao.nv@company.com', '0912345682', 2, 'Staff', 'baonv', '12345', 'Deactivated'),
(48, 'Chi', 'Trần Thị', 'chi.tt@company.com', '0987654326', 3, 'Staff', 'chitt', '12345', 'Deactivated'),
(49, 'Đạt', 'Lê Văn', 'dat.lv@company.com', '0901112228', 4, 'Staff', 'datlv', '12345', 'Active'),
(50, 'Tài', 'Phạm Văn', 'tai.pv@company.com', '0911223350', 5, 'Staff', 'taipv', '12345', 'Active'),
(51, 'Giang', 'Hoàng Thị', 'giang.ht@company.com', '0955667792', 6, 'Staff', 'gianght', '12345', 'Active'),
(52, 'Huy', 'Vũ Văn', 'huy.vv@company.com', '0922334458', 7, 'Staff', 'huyvv', '12345', 'Active'),
(53, 'Linh', 'Đỗ Thị', 'linh.dt@company.com', '0933445570', 8, 'Staff', 'linhdt', '12345', 'Deactivated'),
(54, 'Tú', 'Bùi Văn', 'tu.bv@company.com', '0944556682', 9, 'Staff', 'tubv', '12345', 'Active'),
(55, 'Nga', 'Đặng Thị', 'nga.dt@company.com', '0966778904', 10, 'Staff', 'ngadt', '12345', 'Active'),
(56, 'Quang', 'Ngô Văn', 'quang.nv@company.com', '0912345683', 2, 'Staff', 'quangnv', '12345', 'Active'),
(57, 'Thủy', 'Phan Thị', 'thuy.pt@company.com', '0987654327', 3, 'Staff', 'thuypt', '12345', 'Active'),
(58, 'Tín', 'Lưu Văn', 'tin.lv@company.com', '0901112229', 4, 'Staff', 'tinlv', '12345', 'Active'),
(59, 'Hà', 'Đinh Thị', 'ha.dt@company.com', '0911223351', 5, 'Staff', 'hadt', '12345', 'Deactivated'),
(60, 'Trường', 'Nguyễn Xuân', 'truong.nx@company.com', '0955667793', 6, 'Staff', 'truongnx', '12345', 'Active');


-- Cập nhật trưởng phòng cho các phòng ban
UPDATE `Department` SET `head_employee_id` = 4 WHERE `department_id` = 1;
UPDATE `Department` SET `head_employee_id` = 6 WHERE `department_id` = 2;
UPDATE `Department` SET `head_employee_id` = 10 WHERE `department_id` = 3;
UPDATE `Department` SET `head_employee_id` = 15 WHERE `department_id` = 4;
UPDATE `Department` SET `head_employee_id` = 20 WHERE `department_id` = 5;
UPDATE `Department` SET `head_employee_id` = 26 WHERE `department_id` = 6;
UPDATE `Department` SET `head_employee_id` = 30 WHERE `department_id` = 7;
UPDATE `Department` SET `head_employee_id` = 33 WHERE `department_id` = 8;
UPDATE `Department` SET `head_employee_id` = 37 WHERE `department_id` = 9;
UPDATE `Department` SET `head_employee_id` = 42 WHERE `department_id` = 10;


-- =================================================================
-- PHẦN 2: TẠO DỮ LIỆU CHO TÀI SẢN, DANH MỤC, NHÀ CUNG CẤP
-- =================================================================

-- Dữ liệu cho các danh mục tài sản văn phòng
INSERT INTO `AssetCategory` (`category_id`, `category_name`, `description`) VALUES
(1, 'Máy tính xách tay', 'Các loại laptop cho nhân viên'),
(2, 'Máy tính để bàn', 'Các loại PC cho công việc văn phòng và kỹ thuật'),
(3, 'Màn hình', 'Màn hình máy tính các loại'),
(4, 'Bàn phím', 'Bàn phím có dây và không dây'),
(5, 'Chuột máy tính', 'Chuột có dây và không dây'),
(6, 'Máy in', 'Máy in văn phòng, in laser, in màu'),
(7, 'Máy chiếu', 'Máy chiếu cho phòng họp'),
(8, 'Điện thoại bàn', 'Điện thoại IP cho văn phòng'),
(9, 'Bàn làm việc', 'Bàn làm việc cá nhân và bàn họp'),
(10, 'Ghế văn phòng', 'Ghế xoay, ghế họp'),
(11, 'Tủ tài liệu', 'Tủ sắt, tủ gỗ đựng hồ sơ'),
(12, 'Máy photocopy', 'Máy photocopy đa chức năng'),
(13, 'Máy scan', 'Máy scan tài liệu chuyên dụng'),
(14, 'Thiết bị mạng', 'Router, switch, access point'),
(15, 'Camera an ninh', 'Hệ thống camera giám sát'),
(16, 'Bảng trắng', 'Bảng viết bút lông cho phòng họp'),
(17, 'Máy chấm công', 'Máy chấm công vân tay, thẻ từ'),
(18, 'Máy điều hòa', 'Máy điều hòa không khí cho văn phòng'),
(19, 'Máy lọc nước', 'Máy lọc nước uống nóng lạnh'),
(20, 'Tủ lạnh', 'Tủ lạnh trữ đồ ăn, nước uống'),
(21, 'Quạt', 'Quạt cây, quạt treo tường'),
(22, 'Máy hút bụi', 'Máy hút bụi vệ sinh văn phòng');

-- Dữ liệu cho 20 nhà cung cấp
INSERT INTO `Vendor` (`vendor_id`, `vendor_name`, `contact_person`, `phone_number`, `email`, `address`) VALUES
(1, 'Công ty TNHH Tech-Solutions', 'Trần Mạnh Hùng', '0988111222', 'hung.tm@techsolutions.com', '123 Lê Lợi, Quận 1, TP.HCM'),
(2, 'Tập đoàn Máy tính FPT', 'Nguyễn Thị Lan Anh', '0977333444', 'lananh.nt@fpt.com.vn', '456 Nguyễn Thị Minh Khai, Quận 3, TP.HCM'),
(3, 'Công ty Nội thất Hòa Phát', 'Lê Văn Bình', '0966555666', 'binh.lv@hoaphat.com', '789 Trường Chinh, Quận Tân Bình, TP.HCM'),
(4, 'Siêu thị Điện máy Xanh', 'Phạm Anh Tuấn', '0955777888', 'tuan.pa@dienmayxanh.com', '101 CMT8, Quận 10, TP.HCM'),
(5, 'Công ty Phong Vũ', 'Vũ Minh Đức', '0944999000', 'duc.vm@phongvu.vn', '268 Lý Thường Kiệt, Quận 11, TP.HCM'),
(6, 'Công ty An Phát Computer', 'An Văn Phát', '0933111222', 'phat.av@anphatpc.com.vn', '49 Thái Hà, Đống Đa, Hà Nội'),
(7, 'Thế Giới Di Động', 'Đoàn Văn Hiểu Em', '0922333444', 'em.dvh@thegioididong.com', '258A Nam Kỳ Khởi Nghĩa, Quận 3, TP.HCM'),
(8, 'Công ty Nội thất The One', 'Nguyễn Minh Tâm', '0911555666', 'tam.nm@noithattheone.vn', 'Khu công nghiệp Tân Tạo, Bình Tân, TP.HCM'),
(9, 'Công ty Sao Việt', 'Trần Sao Việt', '0900777888', 'viet.ts@saoviet.net', '333 Tô Hiến Thành, Quận 10, TP.HCM'),
(10, 'Công ty Viễn thông A', 'Hoàng Ngọc Bích', '0988999000', 'bich.hn@vienthonga.com', '777 Trần Hưng Đạo, Quận 5, TP.HCM'),
(11, 'Công ty máy tính Hà Nội', 'Lê Hà Nội', '0977111222', 'hanoi.l@hanoicomputer.vn', '129+131 Lê Thanh Nghị, Hai Bà Trưng, Hà Nội'),
(12, 'Công ty GearVN', 'Phạm Thành Nhân', '0966333444', 'nhan.pt@gearvn.com', '78-80-82 Hoàng Hoa Thám, Phường 12, Tân Bình, TP.HCM'),
(13, 'Công ty ô tô Trường Hải', 'Trần Bá Dương', '0955555666', 'duong.tb@thacogroup.vn', 'Khu công nghiệp Chu Lai, Quảng Nam'),
(14, 'Công ty phần mềm MISA', 'Lữ Thành Long', '0944777888', 'long.lt@misa.com.vn', 'Tòa nhà MISA, Lô 1, CVPM Quang Trung, Quận 12, TP.HCM'),
(15, 'Công ty Camera Vantech', 'Văn Công Tế', '0933999000', 'te.vc@vantech.vn', '44 Ký Con, Quận 1, TP.HCM'),
(16, 'Công ty máy chấm công Ronald Jack', 'Ronald Jack', '0922111222', 'contact@ronaldjack.com', '123 Main Street, USA'),
(17, 'Công ty Ricoh Vietnam', 'Yasunori Ando', '0911333444', 'ando.y@ricoh.com.vn', 'Tầng 17, Tòa nhà E.Town Central, 11 Đoàn Văn Bơ, Quận 4, TP.HCM'),
(18, 'Công ty Cisco Systems Vietnam', 'Lương Thị Lệ Thủy', '0900555666', 'thuy.ltl@cisco.com', 'Phòng 1501, Tầng 15, Keangnam Hanoi Landmark Tower, Hà Nội'),
(19, 'Công ty Dell Việt Nam', 'Ngô Thị Bích Hạnh', '0988777888', 'hanh.ntb@dell.com', 'Tầng 8, Tòa nhà Bitexco Financial, 2 Hải Triều, Quận 1, TP.HCM'),
(20, 'Công ty HP Việt Nam', 'Lim Choon Teck', '0977999000', 'choonteck.lim@hp.com', 'Tầng 20, Tòa nhà Saigon Centre 2, 67 Lê Lợi, Quận 1, TP.HCM');

-- Dữ liệu cho các tài sản trang thiết bị văn phòng
INSERT INTO `Asset` (`asset_id`, `asset_name`, `description`, `serial_number`, `purchase_date`, `purchase_price`, `warranty_expiry_date`, `status`, `category_id`, `vendor_id`) VALUES
-- Laptops (1-15)
(1, 'Laptop Dell XPS 15', 'Laptop cho Giám đốc', 'DXPS15001', '2023-01-15', 45000000, '2026-01-15', 'Available', 1, 19),
(2, 'Laptop ThinkPad X1 Carbon (1)', 'Laptop cho Trưởng phòng', 'TPX1C001', '2023-02-20', 42000000, '2026-02-20', 'Available', 1, 6),
(3, 'Laptop ThinkPad X1 Carbon (2)', 'Laptop cho Trưởng phòng', 'TPX1C002', '2023-02-20', 42000000, '2026-02-20', 'Available', 1, 6),
(4, 'Laptop HP Pavilion 15 (1)', 'Laptop cho nhân viên', 'HPPV15001', '2023-03-10', 18000000, '2025-03-10', 'Available', 1, 20),
(5, 'Laptop HP Pavilion 15 (2)', 'Laptop cho nhân viên', 'HPPV15002', '2023-03-10', 18000000, '2025-03-10', 'Available', 1, 20),
(6, 'Laptop HP Pavilion 15 (3)', 'Laptop cho nhân viên', 'HPPV15003', '2023-03-10', 18000000, '2025-03-10', 'Available', 1, 20),
(7, 'Laptop HP Pavilion 15 (4)', 'Laptop cho nhân viên', 'HPPV15004', '2023-03-10', 18000000, '2025-03-10', 'Available', 1, 20),
(8, 'Laptop Acer Aspire 5 (1)', 'Laptop cho nhân viên', 'ACAS5001', '2023-04-05', 16500000, '2025-04-05', 'Available', 1, 5),
(9, 'Laptop Acer Aspire 5 (2)', 'Laptop cho nhân viên', 'ACAS5002', '2023-04-05', 16500000, '2025-04-05', 'Available', 1, 5),
(10, 'Laptop Acer Aspire 5 (3)', 'Laptop cho nhân viên', 'ACAS5003', '2023-04-05', 16500000, '2025-04-05', 'Available', 1, 5),
(11, 'Macbook Pro 14 M2 (1)', 'Laptop cho Marketing/Design', 'MBP14M201', '2023-06-15', 55000000, '2026-06-15', 'Available', 1, 7),
(12, 'Macbook Pro 14 M2 (2)', 'Laptop cho Marketing/Design', 'MBP14M202', '2023-06-15', 55000000, '2026-06-15', 'Available', 1, 7),
(13, 'Laptop Dell Latitude 5430 (1)', 'Laptop cho nhân viên kinh doanh', 'DLLT54301', '2022-12-20', 22000000, '2025-12-20', 'Available', 1, 19),
(14, 'Laptop Dell Latitude 5430 (2)', 'Laptop cho nhân viên kinh doanh', 'DLLT54302', '2022-12-20', 22000000, '2025-12-20', 'Available', 1, 19),
(15, 'Laptop Dell Latitude 5430 (3)', 'Laptop cho nhân viên kinh doanh', 'DLLT54303', '2022-12-20', 22000000, '2025-12-20', 'Retired', 1, 19),
-- PCs (16-25)
(16, 'PC Dell Vostro (1)', 'Máy tính để bàn cho kế toán', 'DVOS001', '2022-11-10', 15000000, '2025-11-10', 'Available', 2, 19),
(17, 'PC Dell Vostro (2)', 'Máy tính để bàn cho kế toán', 'DVOS002', '2022-11-10', 15000000, '2025-11-10', 'Available', 2, 19),
(18, 'PC Gaming Acer Nitro (1)', 'Máy tính cho phòng IT', 'ACNI001', '2023-05-30', 25000000, '2026-05-30', 'Available', 2, 5),
(19, 'PC Gaming Acer Nitro (2)', 'Máy tính cho phòng IT', 'ACNI002', '2023-05-30', 25000000, '2026-05-30', 'Available', 2, 5),
(20, 'PC HP All-in-One', 'Máy tính cho Lễ tân', 'HPAIO01', '2023-01-01', 19000000, '2025-01-01', 'Available', 2, 20),
(21, 'PC HP ProDesk (1)', 'PC cho nhân viên', 'HPPD001', '2022-10-10', 14000000, '2025-10-10', 'Available', 2, 20),
(22, 'PC HP ProDesk (2)', 'PC cho nhân viên', 'HPPD002', '2022-10-10', 14000000, '2025-10-10', 'Available', 2, 20),
(23, 'PC HP ProDesk (3)', 'PC cho nhân viên', 'HPPD003', '2022-10-10', 14000000, '2025-10-10', 'Available', 2, 20),
(24, 'PC HP ProDesk (4)', 'PC cho nhân viên', 'HPPD004', '2022-10-10', 14000000, '2025-10-10', 'Available', 2, 20),
(25, 'PC HP ProDesk (5)', 'PC cho nhân viên', 'HPPD005', '2022-10-10', 14000000, '2025-10-10', 'Available', 2, 20),
-- Màn hình (26-35)
(26, 'Màn hình Dell UltraSharp 24" (1)', 'Màn hình cho nhân viên', 'DU24001', '2022-11-10', 7000000, '2025-11-10', 'Available', 3, 19),
(27, 'Màn hình Dell UltraSharp 24" (2)', 'Màn hình cho nhân viên', 'DU24002', '2022-11-10', 7000000, '2025-11-10', 'Available', 3, 19),
(28, 'Màn hình Dell UltraSharp 24" (3)', 'Màn hình cho nhân viên', 'DU24003', '2022-11-10', 7000000, '2025-11-10', 'Available', 3, 19),
(29, 'Màn hình Dell UltraSharp 24" (4)', 'Màn hình cho nhân viên', 'DU24004', '2022-11-10', 7000000, '2025-11-10', 'Available', 3, 19),
(30, 'Màn hình LG 27" (1)', 'Màn hình cho phòng IT', 'LG27001', '2023-05-30', 9000000, '2026-05-30', 'Available', 3, 4),
(31, 'Màn hình LG 27" (2)', 'Màn hình cho phòng IT', 'LG27002', '2023-05-30', 9000000, '2026-05-30', 'Available', 3, 4),
(32, 'Màn hình Samsung ViewFinity S8 32" (1)', 'Màn hình cho thiết kế', 'SSVF3201', '2023-06-15', 15000000, '2026-06-15', 'Available', 3, 4),
(33, 'Màn hình Samsung ViewFinity S8 32" (2)', 'Màn hình cho thiết kế', 'SSVF3202', '2023-06-15', 15000000, '2026-06-15', 'Available', 3, 4),
(34, 'Màn hình AOC 24" (1)', 'Màn hình cho nhân viên', 'AOC24001', '2022-09-01', 4000000, '2024-09-01', 'Available', 3, 5),
(35, 'Màn hình AOC 24" (2)', 'Màn hình cho nhân viên', 'AOC24002', '2022-09-01', 4000000, '2024-09-01', 'Retired', 3, 5),
-- Thiết bị văn phòng khác (36-78)
(36, 'Bàn phím Logitech K120 (1)', 'Bàn phím văn phòng', 'LOGK12001', '2022-11-10', 250000, '2024-11-10', 'Available', 4, 5),
(37, 'Bàn phím Logitech K120 (2)', 'Bàn phím văn phòng', 'LOGK12002', '2022-11-10', 250000, '2024-11-10', 'Available', 4, 5),
(38, 'Chuột Logitech M185 (1)', 'Chuột không dây', 'LOGM18501', '2022-11-10', 300000, '2024-11-10', 'Available', 5, 5),
(39, 'Chuột Logitech M185 (2)', 'Chuột không dây', 'LOGM18502', '2022-11-10', 300000, '2024-11-10', 'Available', 5, 5),
(40, 'Máy in Canon LBP2900', 'Máy in phòng Kế toán', 'CAN290001', '2022-09-01', 4500000, '2024-09-01', 'Available', 6, 2),
(41, 'Máy in HP LaserJet Pro M404dn', 'Máy in phòng Nhân sự', 'HPLJM4041', '2023-02-01', 8000000, '2025-02-01', 'Available', 6, 20),
(42, 'Máy in màu Epson L8050', 'Máy in phòng Marketing', 'EPSL80501', '2023-07-01', 7500000, '2025-07-01', 'Available', 6, 4),
(43, 'Máy chiếu Epson EB-X06', 'Máy chiếu phòng họp lớn', 'EPSX06001', '2023-03-10', 12000000, '2025-03-10', 'Available', 7, 4),
(44, 'Máy chiếu ViewSonic PA503XB', 'Máy chiếu phòng họp nhỏ', 'VSPA503X1', '2023-03-10', 9500000, '2025-03-10', 'Available', 7, 9),
(45, 'Điện thoại IP Yealink T31P (1)', 'Điện thoại phòng Kinh doanh', 'YEAT31P01', '2023-01-05', 1500000, '2025-01-05', 'Available', 8, 1),
(46, 'Điện thoại IP Yealink T31P (2)', 'Điện thoại phòng Kinh doanh', 'YEAT31P02', '2023-01-05', 1500000, '2025-01-05', 'Available', 8, 1),
(47, 'Điện thoại IP Yealink T31P (3)', 'Điện thoại phòng Kinh doanh', 'YEAT31P03', '2023-01-05', 1500000, '2025-01-05', 'Available', 8, 1),
(48, 'Bàn làm việc Hòa Phát 1.2m (1)', 'Bàn nhân viên', 'HPB12001', '2022-08-15', 2000000, '2027-08-15', 'Available', 9, 3),
(49, 'Bàn làm việc Hòa Phát 1.2m (2)', 'Bàn nhân viên', 'HPB12002', '2022-08-15', 2000000, '2027-08-15', 'Available', 9, 3),
(50, 'Bàn làm việc Hòa Phát 1.2m (3)', 'Bàn nhân viên', 'HPB12003', '2022-08-15', 2000000, '2027-08-15', 'Available', 9, 3),
(51, 'Bàn làm việc Hòa Phát 1.2m (4)', 'Bàn nhân viên', 'HPB12004', '2022-08-15', 2000000, '2027-08-15', 'Available', 9, 3),
(52, 'Ghế xoay Hòa Phát (1)', 'Ghế nhân viên', 'HPG001', '2022-08-15', 1200000, '2027-08-15', 'Available', 10, 3),
(53, 'Ghế xoay Hòa Phát (2)', 'Ghế nhân viên', 'HPG002', '2022-08-15', 1200000, '2027-08-15', 'Available', 10, 3),
(54, 'Ghế xoay Hòa Phát (3)', 'Ghế nhân viên', 'HPG003', '2022-08-15', 1200000, '2027-08-15', 'Available', 10, 3),
(55, 'Ghế xoay Hòa Phát (4)', 'Ghế nhân viên', 'HPG004', '2022-08-15', 1200000, '2027-08-15', 'Available', 10, 3),
(56, 'Tủ tài liệu sắt Hòa Phát (1)', 'Tủ phòng Kế toán', 'HPTS01', '2022-08-20', 3500000, '2027-08-20', 'Available', 11, 3),
(57, 'Tủ tài liệu sắt Hòa Phát (2)', 'Tủ phòng Nhân sự', 'HPTS02', '2022-08-20', 3500000, '2027-08-20', 'Available', 11, 3),
(58, 'Máy photocopy Ricoh MP 2014AD', 'Máy photo chung', 'RMP20141', '2022-05-10', 25000000, '2025-05-10', 'Available', 12, 17),
(59, 'Máy scan Fujitsu fi-8170', 'Máy scan phòng Hành chính', 'FJ817001', '2023-09-09', 28000000, '2026-09-09', 'Available', 13, 2),
(60, 'Switch Cisco Catalyst 2960', 'Switch mạng tầng 1', 'CS296001', '2022-04-01', 15000000, '2027-04-01', 'Available', 14, 18),
(61, 'Router Wifi Aruba AP22', 'Wifi cho văn phòng', 'ARAP2201', '2022-04-01', 8000000, '2027-04-01', 'Available', 14, 18),
(63, 'Camera Hikvision DS-2CE16D0T (1)', 'Camera ngoài trời', 'HK16D0T01', '2023-10-10', 1200000, '2025-10-10', 'Available', 15, 9),
(64, 'Camera Hikvision DS-2CE16D0T (2)', 'Camera ngoài trời', 'HK16D0T02', '2023-10-10', 1200000, '2025-10-10', 'Available', 15, 9),
(65, 'Bảng trắng Flipchart', 'Bảng phòng họp', 'FLC001', '2022-02-01', 1800000, '2032-02-01', 'Available', 16, 8),
(66, 'Máy chấm công Ronald Jack X628', 'Máy chấm công cửa ra vào', 'RJX62801', '2022-01-15', 3200000, '2025-01-15', 'Available', 17, 16),
(71, 'Bàn phím cơ Keychron K8 (1)', 'Bàn phím cho lập trình viên', 'KCK8001', '2023-11-11', 2500000, '2025-11-11', 'Available', 4, 12),
(72, 'Bàn phím cơ Keychron K8 (2)', 'Bàn phím cho lập trình viên', 'KCK8002', '2023-11-11', 2500000, '2025-11-11', 'Available', 4, 12),
(73, 'Chuột Logitech MX Master 3S (1)', 'Chuột cho lập trình viên/thiết kế', 'LOGMX3S01', '2023-11-11', 2800000, '2025-11-11', 'Available', 5, 12),
(74, 'Chuột Logitech MX Master 3S (2)', 'Chuột cho lập trình viên/thiết kế', 'LOGMX3S02', '2023-11-11', 2800000, '2025-11-11', 'Available', 5, 12),
(75, 'Ghế công thái học Epione Easy Chair (1)', 'Ghế cho nhân viên IT', 'EPEC001', '2023-08-08', 5000000, '2028-08-08', 'Available', 10, 8),
(76, 'Ghế công thái học Epione Easy Chair (2)', 'Ghế cho nhân viên IT', 'EPEC002', '2023-08-08', 5000000, '2028-08-08', 'Available', 10, 8),
(77, 'Bàn nâng hạ tự động Epione (1)', 'Bàn cho Trưởng phòng', 'EPBH001', '2023-08-08', 8000000, '2028-08-08', 'Available', 9, 8),
(78, 'Bàn nâng hạ tự động Epione (2)', 'Bàn cho Trưởng phòng', 'EPBH002', '2023-08-08', 8000000, '2028-08-08', 'Available', 9, 8),
(81, 'Laptop Dell Vostro 3400', 'Laptop dự phòng', 'DV340001', '2021-05-15', 17000000, '2024-05-15', 'Retired', 1, 19),
(82, 'Màn hình Viewsonic 22"', 'Màn hình cũ', 'VS22001', '2020-02-10', 3000000, '2023-02-10', 'Retired', 3, 5),
-- Bổ sung tài sản theo category mới
(83, 'Điều hòa Daikin 12000BTU (1)', 'Điều hòa cho phòng họp lớn', 'DK12K001', '2022-05-20', 15000000, '2025-05-20', 'Available', 18, 4),
(84, 'Điều hòa Daikin 9000BTU (1)', 'Điều hòa cho phòng Giám đốc', 'DK9K001', '2022-05-20', 11000000, '2025-05-20', 'Available', 18, 4),
(85, 'Máy lọc nước Kangaroo KG10A3', 'Máy lọc nước khu vực chung', 'KG10A301', '2023-01-10', 7000000, '2025-01-10', 'Available', 19, 4),
(86, 'Máy lọc nước Karofi KAD-D66', 'Máy lọc nước khu vực chung', 'KRD6601', '2023-01-10', 9500000, '2025-01-10', 'Available', 19, 5),
(87, 'Tủ lạnh Panasonic Inverter 188 lít', 'Tủ lạnh phòng pantry', 'PN188L01', '2022-07-15', 6500000, '2024-07-15', 'Available', 20, 4),
(88, 'Quạt cây Senko (1)', 'Quạt cho văn phòng', 'SNK001', '2022-04-01', 600000, '2024-04-01', 'Available', 21, 5),
(89, 'Quạt cây Senko (2)', 'Quạt cho văn phòng', 'SNK002', '2022-04-01', 600000, '2024-04-01', 'Available', 21, 5),
(90, 'Máy hút bụi Electrolux Z1231', 'Máy hút bụi cho tạp vụ', 'ELZ12311', '2023-03-03', 2500000, '2025-03-03', 'Available', 22, 4);

-- 10 Approved Borrows
INSERT INTO `AssetRequest` (`request_id`, `employee_id`, `request_type`, `request_date`, `status`, `approver_id`, `approval_date`, `rejected_date`, `expected_return_date`) VALUES
(1, 17, 'borrow', '2025-05-10 09:00:00', 'Approved', 15, '2025-05-10 11:00:00', NULL, '2025-08-10'),
(2, 22, 'borrow', '2025-05-11 10:00:00', 'Approved', 20, '2025-05-11 12:00:00', NULL, '2025-07-11'),
(3, 28, 'borrow', '2025-05-12 14:00:00', 'Approved', 26, '2025-05-12 15:30:00', NULL, '2025-06-12'),
(4, 35, 'borrow', '2025-05-13 08:30:00', 'Approved', 33, '2025-05-13 09:00:00', NULL, '2025-05-20'),
(5, 44, 'borrow', '2025-05-14 11:00:00', 'Approved', 42, '2025-05-14 14:00:00', NULL, '2025-09-14'),
(6, 7, 'borrow', '2025-06-01 09:00:00', 'Approved', 6, '2025-06-01 10:00:00', NULL, '2025-12-01'),
(7, 12, 'borrow', '2025-06-02 14:00:00', 'Approved', 10, '2025-06-02 16:00:00', NULL, '2025-08-02'),
(8, 18, 'borrow', '2025-06-03 10:00:00', 'Approved', 15, '2025-06-03 11:00:00', NULL, '2025-06-10'),
(9, 23, 'borrow', '2025-06-04 15:00:00', 'Approved', 20, '2025-06-04 15:30:00', NULL, '2025-07-04'),
(10, 31, 'borrow', '2025-06-05 13:00:00', 'Approved', 30, '2025-06-05 14:00:00', NULL, '2025-06-15');
INSERT INTO `AssetRequestItem` (`request_id`, `asset_id`, `borrow_date`, `condition_on_borrow`) VALUES
(1, 11, '2025-05-10 14:00:00', 'Mới 100%'),
(2, 4, '2025-05-11 13:00:00', 'Còn tốt'),
(3, 12, '2025-05-12 16:00:00', 'Còn tốt'),
(4, 65, '2025-05-13 09:30:00', 'Mới'),
(5, 71, '2025-05-14 15:00:00', 'Mới'),
(6, 5, '2025-06-01 11:00:00', 'Đã qua sử dụng'),
(7, 45, '2025-06-02 16:30:00', 'Còn tốt'),
(8, 75, '2025-06-03 11:30:00', 'Mới'),
(9, 13, '2025-06-04 16:00:00', '95%'),
(10, 56, '2025-06-05 14:30:00', 'Còn tốt');
UPDATE `Asset` SET `status` = 'Borrowed' WHERE `asset_id` IN (11, 4, 12, 65, 71, 5, 45, 75, 13, 56);

-- 5 Pending Borrows
INSERT INTO `AssetRequest` (`request_id`, `employee_id`, `request_type`, `request_date`, `status`, `approver_id`, `approval_date`, `rejected_date`, `expected_return_date`) VALUES
(11, 40, 'borrow', '2025-07-15 09:30:00', 'Pending', NULL, NULL, NULL, '2025-08-15'),
(12, 50, 'borrow', '2025-07-16 10:00:00', 'Pending', NULL, NULL, NULL, '2025-10-16'),
(13, 55, 'borrow', '2025-07-17 11:00:00', 'Pending', NULL, NULL, NULL, '2025-07-24'),
(14, 8, 'borrow', '2025-07-18 14:00:00', 'Pending', NULL, NULL, NULL, '2025-09-18'),
(15, 16, 'borrow', '2025-07-19 16:00:00', 'Pending', NULL, NULL, NULL, '2025-07-29');
INSERT INTO `AssetRequestItem` (`request_id`, `asset_id`) VALUES
(11, 46),
(12, 7),
(13, 72),
(14, 16),
(15, 18);

-- 5 Rejected Borrows
INSERT INTO `AssetRequest` (`request_id`, `employee_id`, `request_type`, `request_date`, `status`, `approver_id`, `approval_date`, `rejected_date`, `expected_return_date`) VALUES
(16, 24, 'borrow', '2025-07-01 10:00:00', 'Rejected', 20, NULL, '2025-07-01 11:00:00', '2025-08-01'),
(17, 36, 'borrow', '2025-07-02 11:00:00', 'Rejected', 33, NULL, '2025-07-02 11:30:00', '2025-07-09'),
(18, 45, 'borrow', '2025-07-03 13:00:00', 'Rejected', 42, NULL, '2025-07-03 14:00:00', '2025-07-13'),
(19, 5, 'borrow', '2025-07-04 15:00:00', 'Rejected', 4, NULL, '2025-07-04 16:00:00', '2025-07-11'),
(20, 13, 'borrow', '2025-07-05 16:00:00', 'Rejected', 10, NULL, '2025-07-05 17:00:00', '2025-07-15');
-- No items for rejected requests

-- 5 Approved Returns
UPDATE `Asset` SET `status` = 'Borrowed' WHERE `asset_id` IN (1, 6, 2, 3, 32);
INSERT INTO `AssetRequest` (`request_id`, `employee_id`, `request_type`, `request_date`, `status`, `approver_id`, `approval_date`, `rejected_date`, `expected_return_date`) VALUES
(21, 1, 'return', '2025-07-20 09:00:00', 'Approved', 15, '2025-07-20 10:00:00', NULL, NULL),
(22, 6, 'return', '2025-07-21 10:00:00', 'Approved', 6, '2025-07-21 11:00:00', NULL, NULL),
(23, 2, 'return', '2025-07-22 11:00:00', 'Approved', 20, '2025-07-22 12:00:00', NULL, NULL),
(24, 3, 'return', '2025-07-23 13:00:00', 'Approved', 30, '2025-07-23 14:00:00', NULL, NULL),
(25, 27, 'return', '2025-07-24 14:00:00', 'Approved', 26, '2025-07-24 15:00:00', NULL, NULL);
INSERT INTO `AssetRequestItem` (`request_id`, `asset_id`, `borrow_date`, `return_date`, `condition_on_borrow`, `condition_on_return`) VALUES
(21, 1, '2025-04-20 09:00:00', '2025-07-20 10:30:00', 'Mới', 'Hơi trầy xước'),
(22, 6, '2025-01-21 10:00:00', '2025-07-21 11:30:00', 'Tốt', 'Bình thường'),
(23, 2, '2024-10-22 11:00:00', '2025-07-22 12:30:00', 'Tốt', 'Bình thường'),
(24, 3, '2025-02-23 13:00:00', '2025-07-23 14:30:00', 'Tốt', 'Bình thường'),
(25, 32, '2025-03-24 14:00:00', '2025-07-24 15:30:00', 'Mới', 'Bình thường');
UPDATE `Asset` SET `status` = 'Available' WHERE `asset_id` IN (1, 6, 2, 3, 32);

-- 5 Pending Returns
INSERT INTO `AssetRequest` (`request_id`, `employee_id`, `request_type`, `request_date`, `status`, `approver_id`, `approval_date`, `rejected_date`, `expected_return_date`) VALUES
(51, 9,  'borrow', '2025-07-10 08:00:00', 'Approved', 1, '2025-07-10 08:05:00', NULL, '2025-08-10'),
(52, 14, 'borrow', '2025-06-11 09:30:00', 'Approved', 1, '2025-06-11 09:35:00', NULL, '2025-07-11'),
(53, 19, 'borrow', '2025-05-12 10:30:00', 'Approved', 1, '2025-05-12 10:35:00', NULL, '2025-06-12'),
(54, 25, 'borrow', '2025-04-13 12:00:00', 'Approved', 1, '2025-04-13 12:05:00', NULL, '2025-05-13'),
(55, 29, 'borrow', '2025-03-14 13:00:00', 'Approved', 1, '2025-03-14 13:05:00', NULL, '2025-04-14');
INSERT INTO `AssetRequestItem` (`request_id`, `asset_id`, `borrow_date`, `condition_on_borrow`) VALUES
(51, 21, '2025-07-10 09:00:00', 'Tốt'),
(52, 22, '2025-06-11 10:00:00', 'Tốt'),
(53, 23, '2025-05-12 11:00:00', 'Tốt'),
(54, 24, '2025-04-13 13:00:00', 'Tốt'),
(55, 25, '2025-03-14 14:00:00', 'Tốt');

UPDATE `Asset` SET `status` = 'Borrowed' WHERE `asset_id` IN (21, 22, 23, 24, 25);
INSERT INTO `AssetRequest` (`request_id`, `employee_id`, `request_type`, `request_date`, `status`, `approver_id`, `approval_date`, `rejected_date`, `expected_return_date`) VALUES
(26, 9, 'return', '2025-08-10 09:00:00', 'Pending', NULL, NULL, NULL, NULL),
(27, 14, 'return', '2025-08-11 10:00:00', 'Pending', NULL, NULL, NULL, NULL),
(28, 19, 'return', '2025-08-12 11:00:00', 'Pending', NULL, NULL, NULL, NULL),
(29, 25, 'return', '2025-08-13 13:00:00', 'Pending', NULL, NULL, NULL, NULL),
(30, 29, 'return', '2025-08-14 14:00:00', 'Pending', NULL, NULL, NULL, NULL);
INSERT INTO `AssetRequestItem` (`request_id`, `asset_id`, `borrow_date`, `condition_on_borrow`) VALUES
(26, 21, '2025-07-10 09:00:00', 'Tốt'),
(27, 22, '2025-06-11 10:00:00', 'Tốt'),
(28, 23, '2025-05-12 11:00:00', 'Tốt'),
(29, 24, '2025-04-13 13:00:00', 'Tốt'),
(30, 25, '2025-03-14 14:00:00', 'Tốt');

-- 10 Approved Borrows bổ sung
INSERT INTO `AssetRequest` (`request_id`, `employee_id`, `request_type`, `request_date`, `status`, `approver_id`, `approval_date`, `rejected_date`, `expected_return_date`) VALUES
(31, 8, 'borrow', '2025-08-16 09:00:00', 'Approved', 6, '2025-08-16 10:00:00', NULL, '2025-09-16'),
(32, 12, 'borrow', '2025-08-17 10:00:00', 'Approved', 10, '2025-08-17 11:00:00', NULL, '2025-09-17'),
(33, 18, 'borrow', '2025-08-18 11:00:00', 'Approved', 15, '2025-08-18 12:00:00', NULL, '2025-09-18'),
(34, 24, 'borrow', '2025-08-19 12:00:00', 'Approved', 20, '2025-08-19 13:00:00', NULL, '2025-09-19'),
(35, 28, 'borrow', '2025-08-20 13:00:00', 'Approved', 26, '2025-08-20 14:00:00', NULL, '2025-09-20'),
(36, 34, 'borrow', '2025-08-21 14:00:00', 'Approved', 33, '2025-08-21 15:00:00', NULL, '2025-09-21'),
(37, 40, 'borrow', '2025-08-22 15:00:00', 'Approved', 37, '2025-08-22 16:00:00', NULL, '2025-09-22'),
(38, 46, 'borrow', '2025-08-23 16:00:00', 'Approved', 42, '2025-08-23 17:00:00', NULL, '2025-09-23'),
(39, 52, 'borrow', '2025-08-24 17:00:00', 'Approved', 37, '2025-08-24 18:00:00', NULL, '2025-09-24'),
(40, 58, 'borrow', '2025-08-25 18:00:00', 'Approved', 42, '2025-08-25 19:00:00', NULL, '2025-09-25');

INSERT INTO `AssetRequestItem` (`request_id`, `asset_id`, `borrow_date`, `condition_on_borrow`) VALUES
(31, 8, '2025-08-16 10:30:00', 'Mới'),
(32, 9, '2025-08-17 11:30:00', 'Tốt'),
(33, 10, '2025-08-18 12:30:00', 'Tốt'),
(34, 88, '2025-08-19 13:30:00', 'Mới'),
(35, 89, '2025-08-20 14:30:00', 'Tốt'),
(36, 90, '2025-08-21 15:30:00', 'Tốt'),
(37, 14, '2025-08-22 16:30:00', 'Mới'),
(38, 66, '2025-08-23 17:30:00', 'Tốt'),
(39, 16, '2025-08-24 18:30:00', 'Tốt'),
(40, 17, '2025-08-25 19:30:00', 'Mới');
UPDATE `Asset` SET `status` = 'Borrowed' WHERE `asset_id` IN (8,9,10,11,12,13,14,15,16,17);

-- 5 Pending Borrows bổ sung
INSERT INTO `AssetRequest` (`request_id`, `employee_id`, `request_type`, `request_date`, `status`, `approver_id`, `approval_date`, `rejected_date`, `expected_return_date`) VALUES
(41, 20, 'borrow', '2025-08-26 09:00:00', 'Pending', NULL, NULL, NULL, '2025-09-26'),
(42, 25, 'borrow', '2025-08-27 10:00:00', 'Pending', NULL, NULL, NULL, '2025-09-27'),
(43, 30, 'borrow', '2025-08-28 11:00:00', 'Pending', NULL, NULL, NULL, '2025-09-28'),
(44, 35, 'borrow', '2025-08-29 12:00:00', 'Pending', NULL, NULL, NULL, '2025-09-29'),
(45, 40, 'borrow', '2025-08-30 13:00:00', 'Pending', NULL, NULL, NULL, '2025-09-30');
INSERT INTO `AssetRequestItem` (`request_id`, `asset_id`) VALUES
(41, 18),
(42, 19),
(43, 20),
(44, 28),
(45, 29);

-- 5 Approved Returns bổ sung
UPDATE `Asset` SET `status` = 'Borrowed' WHERE `asset_id` IN (23,24,25,26,27);
INSERT INTO `AssetRequest` (`request_id`, `employee_id`, `request_type`, `request_date`, `status`, `approver_id`, `approval_date`, `rejected_date`, `expected_return_date`) VALUES
(46, 3, 'return', '2025-08-26 14:00:00', 'Approved', 1, '2025-08-26 15:00:00', NULL, NULL),
(47, 5, 'return', '2025-08-27 15:00:00', 'Approved', 4, '2025-08-27 16:00:00', NULL, NULL),
(48, 7, 'return', '2025-08-28 16:00:00', 'Approved', 6, '2025-08-28 17:00:00', NULL, NULL),
(49, 9, 'return', '2025-08-29 17:00:00', 'Approved', 8, '2025-08-29 18:00:00', NULL, NULL),
(50, 11, 'return', '2025-08-30 18:00:00', 'Approved', 10, '2025-08-30 19:00:00', NULL, NULL);
INSERT INTO `AssetRequestItem` (`request_id`, `asset_id`, `borrow_date`, `return_date`, `condition_on_borrow`, `condition_on_return`) VALUES
(46, 23, '2025-07-26 14:00:00', '2025-08-26 15:30:00', 'Tốt', 'Bình thường'),
(47, 24, '2025-07-27 15:00:00', '2025-08-27 16:30:00', 'Tốt', 'Bình thường'),
(48, 25, '2025-07-28 16:00:00', '2025-08-28 17:30:00', 'Tốt', 'Bình thường'),
(49, 26, '2025-07-29 17:00:00', '2025-08-29 18:30:00', 'Tốt', 'Bình thường'),
(50, 27, '2025-07-30 18:00:00', '2025-08-30 19:30:00', 'Tốt', 'Bình thường');
UPDATE `Asset` SET `status` = 'Available' WHERE `asset_id` IN (23,24,25,26,27);