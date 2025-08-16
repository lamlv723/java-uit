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
        department_id INT PRIMARY KEY AUTO_INCREMENT,
        department_name VARCHAR(255) NOT NULL,
        head_employee_id INT
    );

CREATE TABLE
    Employee (
        employee_id INT PRIMARY KEY AUTO_INCREMENT,
        first_name VARCHAR(100) NOT NULL,
        last_name VARCHAR(100) NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        phone_number VARCHAR(20),
        department_id INT,
        role VARCHAR(50) NOT NULL,
        username VARCHAR(50) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        status VARCHAR(50) NOT NULL DEFAULT 'Active',
        FOREIGN KEY (department_id) REFERENCES Department (department_id)
    );

ALTER TABLE Department ADD CONSTRAINT FK_Departments_HeadEmployee FOREIGN KEY (head_employee_id) REFERENCES Employee (employee_id);

CREATE TABLE
    AssetCategory (
        category_id INT PRIMARY KEY AUTO_INCREMENT,
        category_name VARCHAR(100) NOT NULL UNIQUE,
        description TEXT
    );

CREATE TABLE
    Vendor (
        vendor_id INT PRIMARY KEY AUTO_INCREMENT,
        vendor_name VARCHAR(255) NOT NULL UNIQUE,
        contact_person VARCHAR(255),
        phone_number VARCHAR(20),
        email VARCHAR(255),
        address TEXT
    );

-- Bảng: Asset (Tài sản)
CREATE TABLE
    Asset (
        asset_id INT PRIMARY KEY AUTO_INCREMENT,
        asset_name VARCHAR(255) NOT NULL,
        description TEXT,
        serial_number VARCHAR(100) UNIQUE NOT NULL,
        purchase_date DATE,
        purchase_price DECIMAL(10, 2),
        warranty_expiry_date DATE,
        -- THÊM CỘT `status` THEO YÊU CẦU
        status VARCHAR(50) NOT NULL DEFAULT 'available',
        category_id INT NOT NULL,
        vendor_id INT,
        FOREIGN KEY (category_id) REFERENCES AssetCategory (category_id),
        FOREIGN KEY (vendor_id) REFERENCES Vendor (vendor_id)
    );

CREATE TABLE
    AssetRequest (
        request_id INT PRIMARY KEY AUTO_INCREMENT,
        employee_id INT NOT NULL,
        request_type VARCHAR(50) NOT NULL,
        request_date DATETIME NOT NULL,
        status VARCHAR(50) NOT NULL,
        approver_id INT,
        approval_date DATETIME,
        expected_return_date DATE,
        FOREIGN KEY (employee_id) REFERENCES Employee (employee_id),
        FOREIGN KEY (approver_id) REFERENCES Employee (employee_id)
    );

-- Bảng: AssetRequestItem (Chi tiết yêu cầu tài sản)
CREATE TABLE
    AssetRequestItem (
        request_item_id INT PRIMARY KEY AUTO_INCREMENT,
        request_id INT NOT NULL,
        asset_id INT NOT NULL,
        borrow_date DATETIME,
        return_date DATETIME,
        condition_on_borrow TEXT,
        condition_on_return TEXT,
        FOREIGN KEY (request_id) REFERENCES AssetRequest (request_id),
        FOREIGN KEY (asset_id) REFERENCES Asset (asset_id),
        -- THÊM CONSTRAINT `UNIQUE` THEO YÊU CẦU
        UNIQUE (request_id, asset_id)
    );

-- =================================================================
-- PHẦN TRIGGERS ĐÃ BỊ THIẾU TRƯỚC ĐÓ
-- =================================================================
--
DELIMITER //
CREATE TRIGGER trg_UpdateAssetStatusOnBorrow AFTER INSERT ON AssetRequestItem FOR EACH ROW
BEGIN
    DECLARE req_type VARCHAR(50);
    DECLARE req_status VARCHAR(50);
    SELECT request_type, status INTO req_type, req_status FROM AssetRequest WHERE request_id = NEW.request_id;
    -- Sửa 'Borrowed' thành 'borrowed' để khớp với giá trị mới của cột status
    IF req_type = 'borrow' AND req_status = 'Approved' AND NEW.borrow_date IS NOT NULL THEN
        UPDATE Asset SET status = 'borrowed' WHERE asset_id = NEW.asset_id AND status = 'available';
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_UpdateAssetStatusOnReturn AFTER UPDATE ON AssetRequestItem FOR EACH ROW
BEGIN
    DECLARE req_type VARCHAR(50);
    DECLARE req_status VARCHAR(50);
    SELECT request_type, status INTO req_type, req_status FROM AssetRequest WHERE request_id = NEW.request_id;
    -- Sửa 'Available' thành 'available' để khớp với giá trị mới của cột status
    IF req_type = 'return' AND req_status = 'Completed' AND NEW.return_date IS NOT NULL THEN
        UPDATE Asset SET status = 'available' WHERE asset_id = NEW.asset_id AND status = 'borrowed';
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
    IF req_type = 'borrow' AND asset_status != 'available' THEN
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
    IF req_type = 'return' AND asset_status != 'borrowed' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Khong the tra tai san khong dang duoc muon.';
    END IF;
END //
DELIMITER ;

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