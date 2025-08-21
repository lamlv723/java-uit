
CREATE DATABASE IF NOT EXISTS asset_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE asset_management;

-- Xóa các foreign key trước khi drop bảng
SET
    FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS AssetRequestItem;

DROP TABLE IF EXISTS AssetRequest;

DROP TABLE IF EXISTS Asset;

DROP TABLE IF EXISTS Vendor;

DROP TABLE IF EXISTS AssetCategory;

DROP TABLE IF EXISTS Employee;

DROP TABLE IF EXISTS Department;

SET
    FOREIGN_KEY_CHECKS = 1;

CREATE TABLE
    Department (
        department_id INT PRIMARY KEY AUTO_INCREMENT, -- ID duy nhất của phòng ban, tự động tăng
        department_name VARCHAR(255) NOT NULL, -- Tên phòng ban
        head_employee_id INT -- ID của trưởng phòng (có thể NULL)
    );

-- Bảng: Employee (Nhân viên)
-- Chứa thông tin về tất cả nhân viên, bao gồm cả người dùng ứng dụng.
CREATE TABLE
    Employee (
        employee_id INT PRIMARY KEY AUTO_INCREMENT, -- ID duy nhất của nhân viên, tự động tăng
        first_name VARCHAR(100) NOT NULL, -- Tên
        last_name VARCHAR(100) NOT NULL, -- Họ
        email VARCHAR(255) UNIQUE NOT NULL, -- Email, duy nhất cho mỗi nhân viên
        phone_number VARCHAR(20), -- Số điện thoại
        department_id INT, -- ID phòng ban mà nhân viên thuộc về (FK tới Department)
        role VARCHAR(50) NOT NULL, -- Vai trò của nhân viên (ví dụ: 'Admin', 'Manager', 'Staff')
        username VARCHAR(50) UNIQUE NOT NULL, -- Tên đăng nhập, duy nhất
        password VARCHAR(255) NOT NULL, -- Mật khẩu
        status VARCHAR(50) NOT NULL DEFAULT 'Active', -- Trạng thái của nhân viên (ví dụ: 'Active', 'Deactivated')
        FOREIGN KEY (department_id) REFERENCES Department (department_id)
    );

-- Cập nhật bảng Department để thêm khóa ngoại tới Employee sau khi Employee được định nghĩa
ALTER TABLE Department ADD CONSTRAINT FK_Departments_HeadEmployee FOREIGN KEY (head_employee_id) REFERENCES Employee (employee_id);

-- Bảng: AssetCategory (Danh mục tài sản)
-- Phân loại các loại tài sản khác nhau (ví dụ: 'Máy tính xách tay', 'Máy chiếu', 'Bàn ghế').
CREATE TABLE
    AssetCategory (
        category_id INT PRIMARY KEY AUTO_INCREMENT, -- ID duy nhất của danh mục, tự động tăng
        category_name VARCHAR(100) NOT NULL UNIQUE, -- Tên danh mục, duy nhất
        description TEXT -- Mô tả chi tiết về danh mục
    );

-- Bảng: Vendor (Nhà cung cấp)
-- Chứa thông tin về các nhà cung cấp tài sản.
CREATE TABLE
    Vendor (
        vendor_id INT PRIMARY KEY AUTO_INCREMENT, -- ID duy nhất của nhà cung cấp, tự động tăng
        vendor_name VARCHAR(255) NOT NULL UNIQUE, -- Tên nhà cung cấp, duy nhất
        contact_person VARCHAR(255), -- Người liên hệ của nhà cung cấp
        phone_number VARCHAR(20), -- Số điện thoại của nhà cung cấp
        email VARCHAR(255), -- Email của nhà cung cấp
        address TEXT -- Địa chỉ của nhà cung cấp
    );

-- Bảng: Asset (Tài sản)
-- Chứa thông tin chi tiết về từng tài sản cụ thể.
CREATE TABLE
    Asset (
        asset_id INT PRIMARY KEY AUTO_INCREMENT, -- ID duy nhất của tài sản, tự động tăng
        asset_name VARCHAR(255) NOT NULL, -- Tên tài sản
        description TEXT, -- Mô tả chi tiết về tài sản
        serial_number VARCHAR(100) UNIQUE NOT NULL, -- Số seri, duy nhất và không được NULL
        purchase_date DATE, -- Ngày mua
        purchase_price DECIMAL(10, 2), -- Giá mua
        warranty_expiry_date DATE, -- Ngày hết hạn bảo hành
        status VARCHAR(50) NOT NULL, -- Trạng thái hiện tại của tài sản ('Available', 'Borrowed', 'Retired')
        category_id INT NOT NULL, -- ID danh mục tài sản (FK tới AssetCategory)
        vendor_id INT, -- ID nhà cung cấp (FK tới Vendor)
        FOREIGN KEY (category_id) REFERENCES AssetCategory (category_id),
        FOREIGN KEY (vendor_id) REFERENCES Vendor (vendor_id)
    );

-- Bảng: AssetRequest (Yêu cầu tài sản - mượn/trả)
-- Chứa thông tin về các yêu cầu mượn hoặc trả tài sản.
CREATE TABLE
    AssetRequest (
        request_id INT PRIMARY KEY AUTO_INCREMENT, -- ID duy nhất của yêu cầu tài sản, tự động tăng
        employee_id INT NOT NULL, -- ID nhân viên tạo yêu cầu (FK tới Employee)
        request_type VARCHAR(50) NOT NULL, -- Loại yêu cầu ('borrow' hoặc 'return')
        request_date DATETIME NOT NULL, -- Ngày tạo yêu cầu
        status VARCHAR(50) NOT NULL, -- Trạng thái của yêu cầu (ví dụ: 'Pending', 'Approved', 'Rejected')
        approver_id INT, -- ID nhân viên phê duyệt (FK tới Employee, có thể NULL)
        approval_date DATETIME, -- Ngày phê duyệt
        rejected_date DATETIME, -- Ngày từ chối yêu cầu
        expected_return_date DATE, -- Ngày dự kiến trả (chỉ áp dụng cho yêu cầu mượn)
        FOREIGN KEY (employee_id) REFERENCES Employee (employee_id),
        FOREIGN KEY (approver_id) REFERENCES Employee (employee_id)
    );

-- Bảng: AssetRequestItem (Chi tiết yêu cầu tài sản)
-- Bảng liên kết giữa AssetRequest và Asset, chi tiết từng tài sản trong một yêu cầu.
CREATE TABLE
    AssetRequestItem (
        request_item_id INT PRIMARY KEY AUTO_INCREMENT, -- ID duy nhất của chi tiết yêu cầu, tự động tăng
        request_id INT NOT NULL, -- ID yêu cầu tài sản (FK tới AssetRequest)
        asset_id INT NOT NULL, -- ID tài sản được mượn/trả (FK tới Asset)
        borrow_date DATETIME, -- Ngày thực tế tài sản được mượn
        return_date DATETIME, -- Ngày thực tế tài sản được trả
        condition_on_borrow TEXT, -- Tình trạng tài sản khi mượn
        condition_on_return TEXT, -- Tình trạng tài sản khi trả
        FOREIGN KEY (request_id) REFERENCES AssetRequest (request_id),
        FOREIGN KEY (asset_id) REFERENCES Asset (asset_id),
        UNIQUE (request_id, asset_id)
    );
--
DELIMITER //
CREATE TRIGGER trg_UpdateAssetStatusOnBorrow AFTER INSERT ON AssetRequestItem FOR EACH ROW
BEGIN
    DECLARE req_type VARCHAR(50);
    DECLARE req_status VARCHAR(50);
    SELECT request_type, status INTO req_type, req_status FROM AssetRequest WHERE request_id = NEW.request_id;
    IF req_type = 'borrow' AND req_status = 'Approved' AND NEW.borrow_date IS NOT NULL THEN
        UPDATE Asset SET status = 'Borrowed' WHERE asset_id = NEW.asset_id AND status = 'Available';
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_PreventBorrowUnavailableAsset BEFORE INSERT ON AssetRequestItem FOR EACH ROW
BEGIN
    DECLARE asset_status VARCHAR(50);
    DECLARE req_type VARCHAR(50);
    SELECT status INTO asset_status FROM Asset WHERE asset_id = NEW.asset_id;
    SELECT request_type INTO req_type FROM AssetRequest WHERE request_id = NEW.request_id;
    IF req_type = 'borrow' AND asset_status != 'Available' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Khong the muon tai san khong co san.';
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_PreventReturnUnborrowedAsset BEFORE INSERT ON AssetRequestItem FOR EACH ROW
BEGIN
    DECLARE asset_status VARCHAR(50);
    DECLARE req_type VARCHAR(50);
    SELECT status INTO asset_status FROM Asset WHERE asset_id = NEW.asset_id;
    SELECT request_type INTO req_type FROM AssetRequest WHERE request_id = NEW.request_id;
    IF req_type = 'return' AND asset_status != 'Borrowed' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Khong the tra tai san khong dang duoc muon.';
    END IF;
END //
DELIMITER ;

-- Trigger 5: Đảm bảo người phê duyệt là nhân viên đang hoạt động (cho INSERT)
DELIMITER //
CREATE TRIGGER trg_ValidateApproverStatus_Insert BEFORE INSERT ON AssetRequest FOR EACH ROW
BEGIN
    DECLARE approver_status VARCHAR(50);
    IF NEW.approver_id IS NOT NULL THEN
        SELECT status INTO approver_status FROM Employee WHERE employee_id = NEW.approver_id;
        IF approver_status != 'Active' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nguoi phe duyet phai la nhan vien dang hoat dong.';
        END IF;
    END IF;
END //
DELIMITER ;

-- Trigger 6: Đảm bảo người phê duyệt là nhân viên đang hoạt động (cho UPDATE)
DELIMITER //
CREATE TRIGGER trg_ValidateApproverStatus_Update BEFORE UPDATE ON AssetRequest FOR EACH ROW
BEGIN
    DECLARE approver_status VARCHAR(50);
    IF NEW.approver_id IS NOT NULL AND (NEW.approver_id <> OLD.approver_id OR (NEW.approver_id IS NOT NULL AND OLD.approver_id IS NULL)) THEN
        SELECT status INTO approver_status FROM Employee WHERE employee_id = NEW.approver_id;
        IF approver_status != 'Active' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nguoi phe duyet phai la nhan vien dang hoat dong.';
        END IF;
    END IF;
END //
DELIMITER ;