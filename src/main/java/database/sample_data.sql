SET NAMES utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;
SET sql_mode = '';
USE asset_management;
-- Seed data for Department
INSERT INTO Department (department_id, department_name, head_employee_id) VALUES
(1, 'Phong Ke Toan', NULL),
(2, 'Phong Nhan Su', NULL),
(3, 'Phong IT', NULL),
(4, 'Phong Kinh Doanh', NULL),
(5, 'Phong Marketing', NULL),
(6, 'Phong Hanh Chinh', NULL),
(7, 'Phong Dao Tao', NULL),
(8, 'Phong Bao Ve', NULL),
(9, 'Phong Ky Thuat', NULL),
(10, 'Phong Du An', NULL),
(11, 'Phong Phap Che', NULL),
(12, 'Phong Quan He Khach Hang', NULL),
(13, 'Phong Chinh Sach', NULL),
(14, 'Phong Thiet Bi', NULL),
(15, 'Phong Van Thu', NULL),
(16, 'Phong Lap Trinh', NULL),
(17, 'Phong Thiet Ke', NULL),
(18, 'Phong Ho Tro', NULL),
(19, 'Phong Nghien Cuu', NULL),
(20, 'Phong Phat Trien', NULL),
(21, 'Phong Kiem Thu', NULL),
(22, 'Phong Giao Hang', NULL),
(23, 'Phong Bao Tri', NULL),
(24, 'Phong Kiem Toan', NULL),
(25, 'Phong Tu Van', NULL),
(26, 'Phong Chuyen Mon', NULL),
(27, 'Phong Doi Ngoai', NULL),
(28, 'Phong Truyen Thong', NULL),
(29, 'Phong Su Kien', NULL),
(30, 'Phong Hoach Dinh', NULL),
(31, 'Phong Quan Tri', NULL),
(32, 'Phong Kiem Dinh', NULL),
(33, 'Phong Giam Sat', NULL),
(34, 'Phong Phan Tich', NULL),
(35, 'Phong Lap Ke Hoach', NULL),
(36, 'Phong Quan Ly Du An', NULL),
(37, 'Phong Kiem Soat', NULL),
(38, 'Phong Phat Trien San Pham', NULL),
(39, 'Phong Kiem Nghiem', NULL),
(40, 'Phong Doi Tac', NULL),
(41, 'Phong Kinh Te', NULL),
(42, 'Phong Quan Ly Chat Luong', NULL),
(43, 'Phong Giao Dich', NULL),
(44, 'Phong Ho Tro Khach Hang', NULL),
(45, 'Phong Kiem Tra', NULL),
(46, 'Phong Phan Phoi', NULL),
(47, 'Phong Van Chuyen', NULL),
(48, 'Phong Bao Hanh', NULL),
(49, 'Phong Lap Rap', NULL),
(50, 'Phong Dieu Phoi', NULL);

-- Seed data for Employee
INSERT INTO Employee (employee_id, first_name, last_name, email, phone_number, department_id, role, username, password, status) VALUES
(1, 'Nguyen', 'Van A', 'nguyenvana1@email.com', '0900000001', 1, 'Admin', 'nguyenvana1', 'matkhau1', 'Active'),
(2, 'Tran', 'Thi B', 'tranthib2@email.com', '0900000002', 2, 'Manager', 'tranthib2', 'matkhau2', 'Active'),
(3, 'Le', 'Van C', 'levanc3@email.com', '0900000003', 3, 'Staff', 'levanc3', 'matkhau3', 'Active'),
(4, 'Pham', 'Thi D', 'phamthid4@email.com', '0900000004', 4, 'Staff', 'phamthid4', 'matkhau4', 'Active'),
(5, 'Hoang', 'Van E', 'hoangvane5@email.com', '0900000005', 5, 'Manager', 'hoangvane5', 'matkhau5', 'Active'),
(6, 'Vu', 'Thi F', 'vuthif6@email.com', '0900000006', 6, 'Staff', 'vuthif6', 'matkhau6', 'Active'),
(7, 'Do', 'Van G', 'dovang7@email.com', '0900000007', 7, 'Staff', 'dovang7', 'matkhau7', 'Active'),
(8, 'Bui', 'Thi H', 'buithih8@email.com', '0900000008', 8, 'Manager', 'buithih8', 'matkhau8', 'Active'),
(9, 'Dang', 'Van I', 'dangvani9@email.com', '0900000009', 9, 'Staff', 'dangvani9', 'matkhau9', 'Active'),
(10, 'Ngo', 'Thi K', 'ngothik10@email.com', '0900000010', 10, 'Staff', 'ngothik10', 'matkhau10', 'Active'),
(11, 'Nguyen', 'Van L', 'nguyenvanl11@email.com', '0900000011', 11, 'Staff', 'nguyenvanl11', 'matkhau11', 'Active'),
(12, 'Tran', 'Thi M', 'tranthim12@email.com', '0900000012', 12, 'Manager', 'tranthim12', 'matkhau12', 'Active'),
(13, 'Le', 'Van N', 'levann13@email.com', '0900000013', 13, 'Staff', 'levann13', 'matkhau13', 'Active'),
(14, 'Pham', 'Thi O', 'phamthio14@email.com', '0900000014', 14, 'Staff', 'phamthio14', 'matkhau14', 'Active'),
(15, 'Hoang', 'Van P', 'hoangvanp15@email.com', '0900000015', 15, 'Manager', 'hoangvanp15', 'matkhau15', 'Active'),
(16, 'Vu', 'Thi Q', 'vuthiq16@email.com', '0900000016', 16, 'Staff', 'vuthiq16', 'matkhau16', 'Active'),
(17, 'Do', 'Van R', 'dovanr17@email.com', '0900000017', 17, 'Staff', 'dovanr17', 'matkhau17', 'Active'),
(18, 'Bui', 'Thi S', 'buithis18@email.com', '0900000018', 18, 'Manager', 'buithis18', 'matkhau18', 'Active'),
(19, 'Dang', 'Van T', 'dangvant19@email.com', '0900000019', 19, 'Staff', 'dangvant19', 'matkhau19', 'Active'),
(20, 'Ngo', 'Thi U', 'ngothiu20@email.com', '0900000020', 20, 'Staff', 'ngothiu20', 'matkhau20', 'Active'),
(21, 'Nguyen', 'Van V', 'nguyenvanv21@email.com', '0900000021', 21, 'Staff', 'nguyenvanv21', 'matkhau21', 'Active'),
(22, 'Tran', 'Thi X', 'tranthix22@email.com', '0900000022', 22, 'Manager', 'tranthix22', 'matkhau22', 'Active'),
(23, 'Le', 'Van Y', 'levany23@email.com', '0900000023', 23, 'Staff', 'levany23', 'matkhau23', 'Active'),
(24, 'Pham', 'Thi Z', 'phamthiz24@email.com', '0900000024', 24, 'Staff', 'phamthiz24', 'matkhau24', 'Active'),
(25, 'Hoang', 'Van AA', 'hoangvanaa25@email.com', '0900000025', 25, 'Manager', 'hoangvanaa25', 'matkhau25', 'Active'),
(26, 'Vu', 'Thi AB', 'vuthiab26@email.com', '0900000026', 26, 'Staff', 'vuthiab26', 'matkhau26', 'Active'),
(27, 'Do', 'Van AC', 'dovanac27@email.com', '0900000027', 27, 'Staff', 'dovanac27', 'matkhau27', 'Active'),
(28, 'Bui', 'Thi AD', 'buithiad28@email.com', '0900000028', 28, 'Manager', 'buithiad28', 'matkhau28', 'Active'),
(29, 'Dang', 'Van AE', 'dangvanae29@email.com', '0900000029', 29, 'Staff', 'dangvanae29', 'matkhau29', 'Active'),
(30, 'Ngo', 'Thi AF', 'ngothiaf30@email.com', '0900000030', 30, 'Staff', 'ngothiaf30', 'matkhau30', 'Active'),
(31, 'Nguyen', 'Van AG', 'nguyenvanag31@email.com', '0900000031', 31, 'Staff', 'nguyenvanag31', 'matkhau31', 'Active'),
(32, 'Tran', 'Thi AH', 'tranthiah32@email.com', '0900000032', 32, 'Manager', 'tranthiah32', 'matkhau32', 'Active'),
(33, 'Le', 'Van AI', 'levanai33@email.com', '0900000033', 33, 'Staff', 'levanai33', 'matkhau33', 'Active'),
(34, 'Pham', 'Thi AJ', 'phamthiaj34@email.com', '0900000034', 34, 'Staff', 'phamthiaj34', 'matkhau34', 'Active'),
(35, 'Hoang', 'Van AK', 'hoangvanak35@email.com', '0900000035', 35, 'Manager', 'hoangvanak35', 'matkhau35', 'Active'),
(36, 'Vu', 'Thi AL', 'vuthial36@email.com', '0900000036', 36, 'Staff', 'vuthial36', 'matkhau36', 'Active'),
(37, 'Do', 'Van AM', 'dovanam37@email.com', '0900000037', 37, 'Staff', 'dovanam37', 'matkhau37', 'Active'),
(38, 'Bui', 'Thi AN', 'buithian38@email.com', '0900000038', 38, 'Manager', 'buithian38', 'matkhau38', 'Active'),
(39, 'Dang', 'Van AO', 'dangvanao39@email.com', '0900000039', 39, 'Staff', 'dangvanao39', 'matkhau39', 'Active'),
(40, 'Ngo', 'Thi AP', 'ngothiap40@email.com', '0900000040', 40, 'Staff', 'ngothiap40', 'matkhau40', 'Active'),
(41, 'Nguyen', 'Van AQ', 'nguyenvanaq41@email.com', '0900000041', 41, 'Staff', 'nguyenvanaq41', 'matkhau41', 'Active'),
(42, 'Tran', 'Thi AR', 'tranthiar42@email.com', '0900000042', 42, 'Manager', 'tranthiar42', 'matkhau42', 'Active'),
(43, 'Le', 'Van AS', 'levanas43@email.com', '0900000043', 43, 'Staff', 'levanas43', 'matkhau43', 'Active'),
(44, 'Pham', 'Thi AT', 'phamthiat44@email.com', '0900000044', 44, 'Staff', 'phamthiat44', 'matkhau44', 'Active'),
(45, 'Hoang', 'Van AU', 'hoangvanau45@email.com', '0900000045', 45, 'Manager', 'hoangvanau45', 'matkhau45', 'Active'),
(46, 'Vu', 'Thi AV', 'vuthiav46@email.com', '0900000046', 46, 'Staff', 'vuthiav46', 'matkhau46', 'Active'),
(47, 'Do', 'Van AW', 'dovanaw47@email.com', '0900000047', 47, 'Staff', 'dovanaw47', 'matkhau47', 'Active'),
(48, 'Bui', 'Thi AX', 'buithiax48@email.com', '0900000048', 48, 'Manager', 'buithiax48', 'matkhau48', 'Active'),
(49, 'Dang', 'Van AY', 'dangvanay49@email.com', '0900000049', 49, 'Staff', 'dangvanay49', 'matkhau49', 'Active'),
(50, 'Ngo', 'Thi AZ', 'ngothiaz50@email.com', '0900000050', 50, 'Staff', 'ngothiaz50', 'matkhau50', 'Active');

-- Cập nhật head_employee_id cho Department
UPDATE Department SET head_employee_id = 1 WHERE department_id = 1;
UPDATE Department SET head_employee_id = 2 WHERE department_id = 2;
UPDATE Department SET head_employee_id = 3 WHERE department_id = 3;
UPDATE Department SET head_employee_id = 4 WHERE department_id = 4;
UPDATE Department SET head_employee_id = 5 WHERE department_id = 5;
UPDATE Department SET head_employee_id = 6 WHERE department_id = 6;
UPDATE Department SET head_employee_id = 7 WHERE department_id = 7;
UPDATE Department SET head_employee_id = 8 WHERE department_id = 8;
UPDATE Department SET head_employee_id = 9 WHERE department_id = 9;
UPDATE Department SET head_employee_id = 10 WHERE department_id = 10;
UPDATE Department SET head_employee_id = 11 WHERE department_id = 11;
UPDATE Department SET head_employee_id = 12 WHERE department_id = 12;
UPDATE Department SET head_employee_id = 13 WHERE department_id = 13;
UPDATE Department SET head_employee_id = 14 WHERE department_id = 14;
UPDATE Department SET head_employee_id = 15 WHERE department_id = 15;
UPDATE Department SET head_employee_id = 16 WHERE department_id = 16;
UPDATE Department SET head_employee_id = 17 WHERE department_id = 17;
UPDATE Department SET head_employee_id = 18 WHERE department_id = 18;
UPDATE Department SET head_employee_id = 19 WHERE department_id = 19;
UPDATE Department SET head_employee_id = 20 WHERE department_id = 20;
UPDATE Department SET head_employee_id = 21 WHERE department_id = 21;
UPDATE Department SET head_employee_id = 22 WHERE department_id = 22;
UPDATE Department SET head_employee_id = 23 WHERE department_id = 23;
UPDATE Department SET head_employee_id = 24 WHERE department_id = 24;
UPDATE Department SET head_employee_id = 25 WHERE department_id = 25;
UPDATE Department SET head_employee_id = 26 WHERE department_id = 26;
UPDATE Department SET head_employee_id = 27 WHERE department_id = 27;
UPDATE Department SET head_employee_id = 28 WHERE department_id = 28;
UPDATE Department SET head_employee_id = 29 WHERE department_id = 29;
UPDATE Department SET head_employee_id = 30 WHERE department_id = 30;
UPDATE Department SET head_employee_id = 31 WHERE department_id = 31;
UPDATE Department SET head_employee_id = 32 WHERE department_id = 32;
UPDATE Department SET head_employee_id = 33 WHERE department_id = 33;
UPDATE Department SET head_employee_id = 34 WHERE department_id = 34;
UPDATE Department SET head_employee_id = 35 WHERE department_id = 35;
UPDATE Department SET head_employee_id = 36 WHERE department_id = 36;
UPDATE Department SET head_employee_id = 37 WHERE department_id = 37;
UPDATE Department SET head_employee_id = 38 WHERE department_id = 38;
UPDATE Department SET head_employee_id = 39 WHERE department_id = 39;
UPDATE Department SET head_employee_id = 40 WHERE department_id = 40;
UPDATE Department SET head_employee_id = 41 WHERE department_id = 41;
UPDATE Department SET head_employee_id = 42 WHERE department_id = 42;
UPDATE Department SET head_employee_id = 43 WHERE department_id = 43;
UPDATE Department SET head_employee_id = 44 WHERE department_id = 44;
UPDATE Department SET head_employee_id = 45 WHERE department_id = 45;
UPDATE Department SET head_employee_id = 46 WHERE department_id = 46;
UPDATE Department SET head_employee_id = 47 WHERE department_id = 47;
UPDATE Department SET head_employee_id = 48 WHERE department_id = 48;
UPDATE Department SET head_employee_id = 49 WHERE department_id = 49;
UPDATE Department SET head_employee_id = 50 WHERE department_id = 50;
-- Seed data for AssetCategory
INSERT INTO AssetCategory (category_id, category_name, description) VALUES
(1, 'May tinh xach tay', 'Danh muc may tinh xach tay'),
(2, 'May chieu', 'Danh muc may chieu'),
(3, 'Ban', 'Danh muc ban'),
(4, 'Ghe', 'Danh muc ghe'),
(5, 'May in', 'Danh muc may in'),
(6, 'May scan', 'Danh muc may scan'),
(7, 'May tinh de ban', 'Danh muc may tinh de ban'),
(8, 'Man hinh', 'Danh muc man hinh'),
(9, 'Ban phim', 'Danh muc ban phim'),
(10, 'Chuot', 'Danh muc chuot'),
(11, 'Router', 'Danh muc router'),
(12, 'Switch', 'Danh muc switch'),
(13, 'May fax', 'Danh muc may fax'),
(14, 'May photo', 'Danh muc may photo'),
(15, 'May dieu hoa', 'Danh muc may dieu hoa'),
(16, 'May loc nuoc', 'Danh muc may loc nuoc'),
(17, 'Tu lanh', 'Danh muc tu lanh'),
(18, 'Quat', 'Danh muc quat'),
(19, 'Den', 'Danh muc den'),
(20, 'Camera', 'Danh muc camera'),
(21, 'May pos', 'Danh muc may pos'),
(22, 'May tinh bang', 'Danh muc may tinh bang'),
(23, 'May chi chieu', 'Danh muc may chi chieu'),
(24, 'May hut bui', 'Danh muc may hut bui'),
(25, 'May say', 'Danh muc may say'),
(26, 'May hut am', 'Danh muc may hut am'),
(27, 'May loc khong khi', 'Danh muc may loc khong khi'),
(28, 'May in mau', 'Danh muc may in mau'),
(29, 'May in laser', 'Danh muc may in laser'),
(30, 'May in kim', 'Danh muc may in kim'),
(31, 'May in phun', 'Danh muc may in phun'),
(32, 'May in 3d', 'Danh muc may in 3d'),
(33, 'May in nhiet', 'Danh muc may in nhiet'),
(34, 'May in tem', 'Danh muc may in tem'),
(35, 'May in the', 'Danh muc may in the'),
(36, 'May in hoa don', 'Danh muc may in hoa don'),
(37, 'May in van tay', 'Danh muc may in van tay'),
(38, 'May in ma vach', 'Danh muc may in ma vach'),
(39, 'May in sieu toc', 'Danh muc may in sieu toc'),
(40, 'May in khac', 'Danh muc may in khac'),
(41, 'May in mini', 'Danh muc may in mini'),
(42, 'May in di dong', 'Danh muc may in di dong'),
(43, 'May in wifi', 'Danh muc may in wifi'),
(44, 'May in bluetooth', 'Danh muc may in bluetooth'),
(45, 'May in usb', 'Danh muc may in usb'),
(46, 'May in lan', 'Danh muc may in lan'),
(47, 'May in wifi lan', 'Danh muc may in wifi lan'),
(48, 'May in bluetooth lan', 'Danh muc may in bluetooth lan'),
(49, 'May in usb lan', 'Danh muc may in usb lan'),
(50, 'May in khong day', 'Danh muc may in khong day');

INSERT INTO Vendor (vendor_id, vendor_name, contact_person, phone_number, email, address) VALUES
(1, 'Cong ty TNHH May Tinh A', 'Nguyen Van A', '0911000001', 'vendor1@email.com', '123 Duong 1, Quan 1, TP HCM'),
(2, 'Cong ty TNHH May Tinh B', 'Tran Thi B', '0911000002', 'vendor2@email.com', '124 Duong 2, Quan 2, TP HCM'),
(3, 'Cong ty TNHH May Tinh C', 'Le Van C', '0911000003', 'vendor3@email.com', '125 Duong 3, Quan 3, TP HCM'),
(4, 'Cong ty TNHH May Tinh D', 'Pham Thi D', '0911000004', 'vendor4@email.com', '126 Duong 4, Quan 4, TP HCM'),
(5, 'Cong ty TNHH May Tinh E', 'Hoang Van E', '0911000005', 'vendor5@email.com', '127 Duong 5, Quan 5, TP HCM'),
(6, 'Cong ty TNHH May Tinh F', 'Vu Thi F', '0911000006', 'vendor6@email.com', '128 Duong 6, Quan 6, TP HCM'),
(7, 'Cong ty TNHH May Tinh G', 'Do Van G', '0911000007', 'vendor7@email.com', '129 Duong 7, Quan 7, TP HCM'),
(8, 'Cong ty TNHH May Tinh H', 'Bui Thi H', '0911000008', 'vendor8@email.com', '130 Duong 8, Quan 8, TP HCM'),
(9, 'Cong ty TNHH May Tinh I', 'Dang Van I', '0911000009', 'vendor9@email.com', '131 Duong 9, Quan 9, TP HCM'),
(10, 'Cong ty TNHH May Tinh K', 'Ngo Thi K', '0911000010', 'vendor10@email.com', '132 Duong 10, Quan 10, TP HCM'),
(11, 'Cong ty TNHH May Tinh L', 'Nguyen Van L', '0911000011', 'vendor11@email.com', '133 Duong 11, Quan 11, TP HCM'),
(12, 'Cong ty TNHH May Tinh M', 'Tran Thi M', '0911000012', 'vendor12@email.com', '134 Duong 12, Quan 12, TP HCM'),
(13, 'Cong ty TNHH May Tinh N', 'Le Van N', '0911000013', 'vendor13@email.com', '135 Duong 13, Quan 1, TP HCM'),
(14, 'Cong ty TNHH May Tinh O', 'Pham Thi O', '0911000014', 'vendor14@email.com', '136 Duong 14, Quan 2, TP HCM'),
(15, 'Cong ty TNHH May Tinh P', 'Hoang Van P', '0911000015', 'vendor15@email.com', '137 Duong 15, Quan 3, TP HCM'),
(16, 'Cong ty TNHH May Tinh Q', 'Vu Thi Q', '0911000016', 'vendor16@email.com', '138 Duong 16, Quan 4, TP HCM'),
(17, 'Cong ty TNHH May Tinh R', 'Do Van R', '0911000017', 'vendor17@email.com', '139 Duong 17, Quan 5, TP HCM'),
(18, 'Cong ty TNHH May Tinh S', 'Bui Thi S', '0911000018', 'vendor18@email.com', '140 Duong 18, Quan 6, TP HCM'),
(19, 'Cong ty TNHH May Tinh T', 'Dang Van T', '0911000019', 'vendor19@email.com', '141 Duong 19, Quan 7, TP HCM'),
(20, 'Cong ty TNHH May Tinh U', 'Ngo Thi U', '0911000020', 'vendor20@email.com', '142 Duong 20, Quan 8, TP HCM'),
(21, 'Cong ty TNHH May Tinh V', 'Nguyen Van V', '0911000021', 'vendor21@email.com', '143 Duong 21, Quan 9, TP HCM'),
(22, 'Cong ty TNHH May Tinh X', 'Tran Thi X', '0911000022', 'vendor22@email.com', '144 Duong 22, Quan 10, TP HCM'),
(23, 'Cong ty TNHH May Tinh Y', 'Le Van Y', '0911000023', 'vendor23@email.com', '145 Duong 23, Quan 11, TP HCM'),
(24, 'Cong ty TNHH May Tinh Z', 'Pham Thi Z', '0911000024', 'vendor24@email.com', '146 Duong 24, Quan 12, TP HCM'),
(25, 'Cong ty TNHH May Tinh AA', 'Hoang Van AA', '0911000025', 'vendor25@email.com', '147 Duong 25, Quan 1, TP HCM'),
(26, 'Cong ty TNHH May Tinh AB', 'Vu Thi AB', '0911000026', 'vendor26@email.com', '148 Duong 26, Quan 2, TP HCM'),
(27, 'Cong ty TNHH May Tinh AC', 'Do Van AC', '0911000027', 'vendor27@email.com', '149 Duong 27, Quan 3, TP HCM'),
(28, 'Cong ty TNHH May Tinh AD', 'Bui Thi AD', '0911000028', 'vendor28@email.com', '150 Duong 28, Quan 4, TP HCM'),
(29, 'Cong ty TNHH May Tinh AE', 'Dang Van AE', '0911000029', 'vendor29@email.com', '151 Duong 29, Quan 5, TP HCM'),
(30, 'Cong ty TNHH May Tinh AF', 'Ngo Thi AF', '0911000030', 'vendor30@email.com', '152 Duong 30, Quan 6, TP HCM'),
(31, 'Cong ty TNHH May Tinh AG', 'Nguyen Van AG', '0911000031', 'vendor31@email.com', '153 Duong 31, Quan 7, TP HCM'),
(32, 'Cong ty TNHH May Tinh AH', 'Tran Thi AH', '0911000032', 'vendor32@email.com', '154 Duong 32, Quan 8, TP HCM'),
(33, 'Cong ty TNHH May Tinh AI', 'Le Van AI', '0911000033', 'vendor33@email.com', '155 Duong 33, Quan 9, TP HCM'),
(34, 'Cong ty TNHH May Tinh AJ', 'Pham Thi AJ', '0911000034', 'vendor34@email.com', '156 Duong 34, Quan 10, TP HCM'),
(35, 'Cong ty TNHH May Tinh AK', 'Hoang Van AK', '0911000035', 'vendor35@email.com', '157 Duong 35, Quan 11, TP HCM'),
(36, 'Cong ty TNHH May Tinh AL', 'Vu Thi AL', '0911000036', 'vendor36@email.com', '158 Duong 36, Quan 12, TP HCM'),
(37, 'Cong ty TNHH May Tinh AM', 'Do Van AM', '0911000037', 'vendor37@email.com', '159 Duong 37, Quan 1, TP HCM'),
(38, 'Cong ty TNHH May Tinh AN', 'Bui Thi AN', '0911000038', 'vendor38@email.com', '160 Duong 38, Quan 2, TP HCM'),
(39, 'Cong ty TNHH May Tinh AO', 'Dang Van AO', '0911000039', 'vendor39@email.com', '161 Duong 39, Quan 3, TP HCM'),
(40, 'Cong ty TNHH May Tinh AP', 'Ngo Thi AP', '0911000040', 'vendor40@email.com', '162 Duong 40, Quan 4, TP HCM'),
(41, 'Cong ty TNHH May Tinh AQ', 'Nguyen Van AQ', '0911000041', 'vendor41@email.com', '163 Duong 41, Quan 5, TP HCM'),
(42, 'Cong ty TNHH May Tinh AR', 'Tran Thi AR', '0911000042', 'vendor42@email.com', '164 Duong 42, Quan 6, TP HCM'),
(43, 'Cong ty TNHH May Tinh AS', 'Le Van AS', '0911000043', 'vendor43@email.com', '165 Duong 43, Quan 7, TP HCM'),
(44, 'Cong ty TNHH May Tinh AT', 'Pham Thi AT', '0911000044', 'vendor44@email.com', '166 Duong 44, Quan 8, TP HCM'),
(45, 'Cong ty TNHH May Tinh AU', 'Hoang Van AU', '0911000045', 'vendor45@email.com', '167 Duong 45, Quan 9, TP HCM'),
(46, 'Cong ty TNHH May Tinh AV', 'Vu Thi AV', '0911000046', 'vendor46@email.com', '168 Duong 46, Quan 10, TP HCM'),
(47, 'Cong ty TNHH May Tinh AW', 'Do Van AW', '0911000047', 'vendor47@email.com', '169 Duong 47, Quan 11, TP HCM'),
(48, 'Cong ty TNHH May Tinh AX', 'Bui Thi AX', '0911000048', 'vendor48@email.com', '170 Duong 48, Quan 12, TP HCM'),
(49, 'Cong ty TNHH May Tinh AY', 'Dang Van AY', '0911000049', 'vendor49@email.com', '171 Duong 49, Quan 1, TP HCM'),
(50, 'Cong ty TNHH May Tinh AZ', 'Ngo Thi AZ', '0911000050', 'vendor50@email.com', '172 Duong 50, Quan 2, TP HCM');

-- Seed data for Asset
INSERT INTO Asset (asset_id, asset_name, description, serial_number, purchase_date, purchase_price, warranty_expiry_date, status, category_id, vendor_id) VALUES
-- 50 assets, chia đều các category và vendor, trạng thái Available
(1, 'May tinh xach tay 1', 'May tinh xach tay cho phong 1', 'SN00001', '2023-01-01', 15000000, '2026-01-01', 'Available', 1, 1),
(2, 'May tinh xach tay 2', 'May tinh xach tay cho phong 2', 'SN00002', '2023-01-02', 15000000, '2026-01-02', 'Available', 1, 2),
(3, 'May chieu 1', 'May chieu cho phong 3', 'SN00003', '2023-01-03', 8000000, '2026-01-03', 'Available', 2, 3),
(4, 'May chieu 2', 'May chieu cho phong 4', 'SN00004', '2023-01-04', 8000000, '2026-01-04', 'Available', 2, 4),
(5, 'Ban 1', 'Ban cho phong 5', 'SN00005', '2023-01-05', 2000000, '2026-01-05', 'Available', 3, 5),
(6, 'Ban 2', 'Ban cho phong 6', 'SN00006', '2023-01-06', 2000000, '2026-01-06', 'Available', 3, 6),
(7, 'Ghe 1', 'Ghe cho phong 7', 'SN00007', '2023-01-07', 1000000, '2026-01-07', 'Available', 4, 7),
(8, 'Ghe 2', 'Ghe cho phong 8', 'SN00008', '2023-01-08', 1000000, '2026-01-08', 'Available', 4, 8),
(9, 'May in 1', 'May in cho phong 9', 'SN00009', '2023-01-09', 5000000, '2026-01-09', 'Available', 5, 9),
(10, 'May in 2', 'May in cho phong 10', 'SN00010', '2023-01-10', 5000000, '2026-01-10', 'Available', 5, 10),
(11, 'May scan 1', 'May scan cho phong 11', 'SN00011', '2023-01-11', 4000000, '2026-01-11', 'Available', 6, 11),
(12, 'May scan 2', 'May scan cho phong 12', 'SN00012', '2023-01-12', 4000000, '2026-01-12', 'Available', 6, 12),
(13, 'May tinh de ban 1', 'May tinh de ban cho phong 13', 'SN00013', '2023-01-13', 12000000, '2026-01-13', 'Available', 7, 13),
(14, 'May tinh de ban 2', 'May tinh de ban cho phong 14', 'SN00014', '2023-01-14', 12000000, '2026-01-14', 'Available', 7, 14),
(15, 'Man hinh 1', 'Man hinh cho phong 15', 'SN00015', '2023-01-15', 3000000, '2026-01-15', 'Available', 8, 15),
(16, 'Man hinh 2', 'Man hinh cho phong 16', 'SN00016', '2023-01-16', 3000000, '2026-01-16', 'Available', 8, 16),
(17, 'Ban phim 1', 'Ban phim cho phong 17', 'SN00017', '2023-01-17', 500000, '2026-01-17', 'Available', 9, 17),
(18, 'Ban phim 2', 'Ban phim cho phong 18', 'SN00018', '2023-01-18', 500000, '2026-01-18', 'Available', 9, 18),
(19, 'Chuot 1', 'Chuot cho phong 19', 'SN00019', '2023-01-19', 300000, '2026-01-19', 'Available', 10, 19),
(20, 'Chuot 2', 'Chuot cho phong 20', 'SN00020', '2023-01-20', 300000, '2026-01-20', 'Available', 10, 20),
(21, 'Router 1', 'Router cho phong 21', 'SN00021', '2023-01-21', 1000000, '2026-01-21', 'Available', 11, 21),
(22, 'Router 2', 'Router cho phong 22', 'SN00022', '2023-01-22', 1000000, '2026-01-22', 'Available', 11, 22),
(23, 'Switch 1', 'Switch cho phong 23', 'SN00023', '2023-01-23', 2000000, '2026-01-23', 'Available', 12, 23),
(24, 'Switch 2', 'Switch cho phong 24', 'SN00024', '2023-01-24', 2000000, '2026-01-24', 'Available', 12, 24),
(25, 'May fax 1', 'May fax cho phong 25', 'SN00025', '2023-01-25', 2500000, '2026-01-25', 'Available', 13, 25),
(26, 'May fax 2', 'May fax cho phong 26', 'SN00026', '2023-01-26', 2500000, '2026-01-26', 'Available', 13, 26),
(27, 'May photo 1', 'May photo cho phong 27', 'SN00027', '2023-01-27', 7000000, '2026-01-27', 'Available', 14, 27),
(28, 'May photo 2', 'May photo cho phong 28', 'SN00028', '2023-01-28', 7000000, '2026-01-28', 'Available', 14, 28),
(29, 'May dieu hoa 1', 'May dieu hoa cho phong 29', 'SN00029', '2023-01-29', 9000000, '2026-01-29', 'Available', 15, 29),
(30, 'May dieu hoa 2', 'May dieu hoa cho phong 30', 'SN00030', '2023-01-30', 9000000, '2026-01-30', 'Available', 15, 30),
(31, 'May loc nuoc 1', 'May loc nuoc cho phong 31', 'SN00031', '2023-01-31', 3500000, '2026-01-31', 'Available', 16, 31),
(32, 'May loc nuoc 2', 'May loc nuoc cho phong 32', 'SN00032', '2023-02-01', 3500000, '2026-02-01', 'Available', 16, 32),
(33, 'Tu lanh 1', 'Tu lanh cho phong 33', 'SN00033', '2023-02-02', 6000000, '2026-02-02', 'Available', 17, 33),
(34, 'Tu lanh 2', 'Tu lanh cho phong 34', 'SN00034', '2023-02-03', 6000000, '2026-02-03', 'Available', 17, 34),
(35, 'Quat 1', 'Quat cho phong 35', 'SN00035', '2023-02-04', 800000, '2026-02-04', 'Available', 18, 35),
(36, 'Quat 2', 'Quat cho phong 36', 'SN00036', '2023-02-05', 800000, '2026-02-05', 'Available', 18, 36),
(37, 'Den 1', 'Den cho phong 37', 'SN00037', '2023-02-06', 200000, '2026-02-06', 'Available', 19, 37),
(38, 'Den 2', 'Den cho phong 38', 'SN00038', '2023-02-07', 200000, '2026-02-07', 'Available', 19, 38),
(39, 'Camera 1', 'Camera cho phong 39', 'SN00039', '2023-02-08', 3000000, '2026-02-08', 'Available', 20, 39),
(40, 'Camera 2', 'Camera cho phong 40', 'SN00040', '2023-02-09', 3000000, '2026-02-09', 'Available', 20, 40),
(41, 'May pos 1', 'May pos cho phong 41', 'SN00041', '2023-02-10', 4000000, '2026-02-10', 'Available', 21, 41),
(42, 'May pos 2', 'May pos cho phong 42', 'SN00042', '2023-02-11', 4000000, '2026-02-11', 'Available', 21, 42),
(43, 'May tinh bang 1', 'May tinh bang cho phong 43', 'SN00043', '2023-02-12', 7000000, '2026-02-12', 'Available', 22, 43),
(44, 'May tinh bang 2', 'May tinh bang cho phong 44', 'SN00044', '2023-02-13', 7000000, '2026-02-13', 'Available', 22, 44),
(45, 'May chi chieu 1', 'May chi chieu cho phong 45', 'SN00045', '2023-02-14', 8000000, '2026-02-14', 'Available', 23, 45),
(46, 'May chi chieu 2', 'May chi chieu cho phong 46', 'SN00046', '2023-02-15', 8000000, '2026-02-15', 'Available', 23, 46),
(47, 'May hut bui 1', 'May hut bui cho phong 47', 'SN00047', '2023-02-16', 2500000, '2026-02-16', 'Available', 24, 47),
(48, 'May hut bui 2', 'May hut bui cho phong 48', 'SN00048', '2023-02-17', 2500000, '2026-02-17', 'Available', 24, 48),
(49, 'May say 1', 'May say cho phong 49', 'SN00049', '2023-02-18', 1500000, '2026-02-18', 'Available', 25, 49),
(50, 'May say 2', 'May say cho phong 50', 'SN00050', '2023-02-19', 1500000, '2026-02-19', 'Available', 25, 50);

-- Seed data for AssetRequest
INSERT INTO AssetRequest (request_id, employee_id, request_type, request_date, status, approver_id, approval_date, rejected_date, expected_return_date) VALUES
-- 50 requests, chia đều các employee, loại borrow và return, trạng thái Pending/Approved/Completed
-- Đã bổ sung giá trị cho các trường approval_date, expected_return_date để tránh NULL nếu có thể
(1, 1, 'borrow', '2025-07-01 08:00:00', 'Pending', 2, '2025-07-01 09:00:00',NULL, '2025-07-10'),
(2, 2, 'borrow', '2025-07-02 09:00:00', 'Approved', 1, '2025-07-02 10:00:00',NULL, '2025-07-12'),
(3, 3, 'borrow', '2025-07-03 10:00:00', 'Approved', 4, '2025-07-03 11:00:00', NULL,'2025-07-13'),
(4, 4, 'return', '2025-07-04 11:00:00', 'Pending', 3, '2025-07-04 12:00:00',NULL, '2025-07-14'),
(5, 5, 'return', '2025-07-05 12:00:00', 'Approved', 6, '2025-07-05 13:00:00',NULL, '2025-07-15'),
(6, 6, 'borrow', '2025-07-06 13:00:00', 'Pending', 5, '2025-07-06 14:00:00', NULL,'2025-07-16'),
(7, 7, 'borrow', '2025-07-07 14:00:00', 'Approved', 8, '2025-07-07 15:00:00', NULL,'2025-07-17'),
(8, 8, 'borrow', '2025-07-08 15:00:00', 'Approved', 7, '2025-07-08 16:00:00', NULL,'2025-07-18'),
(9, 9, 'return', '2025-07-09 16:00:00', 'Pending', 10, '2025-07-09 17:00:00', NULL,'2025-07-19'),
(10, 10, 'return', '2025-07-10 17:00:00', 'Approved', 9, '2025-07-10 18:00:00', NULL,'2025-07-20'),
(11, 11, 'borrow', '2025-07-11 08:00:00', 'Pending', 12, NULL, NULL,'2025-07-21'),
(12, 12, 'borrow', '2025-07-12 09:00:00', 'Approved', 11, '2025-07-12 10:00:00',NULL, '2025-07-22'),
(13, 13, 'borrow', '2025-07-13 10:00:00', 'Approved', 14, '2025-07-13 11:00:00', NULL,'2025-07-23'),
(14, 14, 'return', '2025-07-14 11:00:00', 'Pending', 13, NULL,NULL, NULL),
(15, 15, 'return', '2025-07-15 12:00:00', 'Approved', 16, '2025-07-15 13:00:00',NULL, NULL),
(16, 16, 'borrow', '2025-07-16 13:00:00', 'Pending', 15, NULL, NULL,'2025-07-26'),
(17, 17, 'borrow', '2025-07-17 14:00:00', 'Approved', 18, '2025-07-17 15:00:00',NULL, '2025-07-27'),
(18, 18, 'borrow', '2025-07-18 15:00:00', 'Approved', 17, '2025-07-18 16:00:00', NULL,'2025-07-28'),
(19, 19, 'return', '2025-07-19 16:00:00', 'Pending', 20, NULL, NULL,NULL),
(20, 20, 'return', '2025-07-20 17:00:00', 'Approved', 19, '2025-07-20 18:00:00',NULL, NULL),
(21, 21, 'borrow', '2025-07-21 08:00:00', 'Pending', 22, NULL, NULL,'2025-07-31'),
(22, 22, 'borrow', '2025-07-22 09:00:00', 'Approved', 21, '2025-07-22 10:00:00', NULL,'2025-08-01'),
(23, 23, 'borrow', '2025-07-23 10:00:00', 'Approved', 24, '2025-07-23 11:00:00',NULL, '2025-08-02'),
(24, 24, 'return', '2025-07-24 11:00:00', 'Pending', 23, NULL, NULL,NULL),
(25, 25, 'return', '2025-07-25 12:00:00', 'Approved', 26, '2025-07-25 13:00:00', NULL,NULL),
(26, 26, 'borrow', '2025-07-26 13:00:00', 'Pending', 25, NULL, NULL,'2025-08-05'),
(27, 27, 'borrow', '2025-07-27 14:00:00', 'Approved', 28, '2025-07-27 15:00:00', NULL,'2025-08-06'),
(28, 28, 'borrow', '2025-07-28 15:00:00', 'Approved', 27, '2025-07-28 16:00:00', NULL,'2025-08-07'),
(29, 29, 'return', '2025-07-29 16:00:00', 'Pending', 30, NULL, NULL,NULL),
(30, 30, 'return', '2025-07-30 17:00:00', 'Approved', 29, '2025-07-30 18:00:00', NULL,NULL),
(31, 31, 'borrow', '2025-07-31 08:00:00', 'Pending', 32, NULL, NULL,'2025-08-10'),
(32, 32, 'borrow', '2025-08-01 09:00:00', 'Approved', 31, '2025-08-01 10:00:00',NULL, '2025-08-11'),
(33, 33, 'borrow', '2025-08-02 10:00:00', 'Approved', 34, '2025-08-02 11:00:00', NULL,'2025-08-12'),
(34, 34, 'return', '2025-08-03 11:00:00', 'Pending', 33, NULL, NULL,NULL),
(35, 35, 'return', '2025-08-04 12:00:00', 'Approved', 36, '2025-08-04 13:00:00',NULL, NULL),
(36, 36, 'borrow', '2025-08-05 13:00:00', 'Pending', 35, NULL, NULL,'2025-08-15'),
(37, 37, 'borrow', '2025-08-06 14:00:00', 'Approved', 38, '2025-08-06 15:00:00',NULL, '2025-08-16'),
(38, 38, 'borrow', '2025-08-07 15:00:00', 'Approved', 37, '2025-08-07 16:00:00', NULL,'2025-08-17'),
(39, 39, 'return', '2025-08-08 16:00:00', 'Pending', 40, NULL,NULL, NULL),
(40, 40, 'return', '2025-08-09 17:00:00', 'Approved', 39, '2025-08-09 18:00:00',NULL, NULL),
(41, 41, 'borrow', '2025-08-10 08:00:00', 'Pending', 42, NULL,NULL, '2025-08-20'),
(42, 42, 'borrow', '2025-08-11 09:00:00', 'Approved', 41, '2025-08-11 10:00:00', NULL,'2025-08-21'),
(43, 43, 'borrow', '2025-08-12 10:00:00', 'Approved', 44, '2025-08-12 11:00:00', NULL,'2025-08-22'),
(44, 44, 'return', '2025-08-13 11:00:00', 'Pending', 43, NULL, NULL,NULL),
(45, 45, 'return', '2025-08-14 12:00:00', 'Approved', 46, '2025-08-14 13:00:00', NULL,NULL),
(46, 46, 'borrow', '2025-08-15 13:00:00', 'Pending', 45, NULL, NULL,'2025-08-25'),
(47, 47, 'borrow', '2025-08-16 14:00:00', 'Approved', 48, '2025-08-16 15:00:00',NULL, '2025-08-26'),
(48, 48, 'borrow', '2025-08-17 15:00:00', 'Approved', 47, '2025-08-17 16:00:00', NULL,'2025-08-27'),
(49, 49, 'return', '2025-08-18 16:00:00', 'Pending', 50, NULL, NULL,NULL),
(50, 50, 'return', '2025-08-19 17:00:00', 'Approved', 49, '2025-08-19 18:00:00', NULL,NULL);

-- Seed data for AssetRequestItem

-- Insert các dòng borrow trước (có borrow_date khác NULL)
INSERT INTO AssetRequestItem (request_item_id, request_id, asset_id, borrow_date, return_date, condition_on_borrow, condition_on_return) VALUES
-- Đã bổ sung giá trị cho các trường return_date, condition_on_borrow, condition_on_return để tránh NULL nếu có thể
(2, 2, 2, '2025-07-02 11:00:00', '2025-07-12 11:00:00', 'Tot', 'Binh thuong'),
(3, 3, 3, '2025-07-03 12:00:00', '2025-07-13 12:00:00', 'Tot', 'Binh thuong'),
(7, 7, 7, '2025-07-07 16:00:00', '2025-07-17 16:00:00', 'Tot', 'Binh thuong'),
(8, 8, 8, '2025-07-08 17:00:00', '2025-07-18 17:00:00', 'Tot', 'Binh thuong'),
(12, 12, 12, '2025-07-12 11:00:00', '2025-07-22 11:00:00', 'Tot', 'Binh thuong'),
(13, 13, 13, '2025-07-13 12:00:00', '2025-07-23 12:00:00', 'Tot', 'Binh thuong'),
(17, 17, 17, '2025-07-17 16:00:00', '2025-07-27 16:00:00', 'Tot', 'Binh thuong'),
(18, 18, 18, '2025-07-18 17:00:00', '2025-07-28 17:00:00', 'Tot', 'Binh thuong'),
(22, 22, 22, '2025-07-22 11:00:00', '2025-08-01 11:00:00', 'Tot', 'Binh thuong'),
(23, 23, 23, '2025-07-23 12:00:00', '2025-08-02 12:00:00', 'Tot', 'Binh thuong'),
(27, 27, 27, '2025-07-27 16:00:00', '2025-08-06 16:00:00', 'Tot', 'Binh thuong'),
(28, 28, 28, '2025-07-28 17:00:00', '2025-08-07 17:00:00', 'Tot', 'Binh thuong'),
(32, 32, 32, '2025-08-01 11:00:00', '2025-08-11 11:00:00', 'Tot', 'Binh thuong'),
(33, 33, 33, '2025-08-02 12:00:00', '2025-08-12 12:00:00', 'Tot', 'Binh thuong'),
(37, 37, 37, '2025-08-06 16:00:00', '2025-08-16 16:00:00', 'Tot', 'Binh thuong'),
(38, 38, 38, '2025-08-07 17:00:00', '2025-08-17 17:00:00', 'Tot', 'Binh thuong'),
(42, 42, 42, '2025-08-11 11:00:00', '2025-08-21 11:00:00', 'Tot', 'Binh thuong'),
(43, 43, 43, '2025-08-12 12:00:00', '2025-08-22 12:00:00', 'Tot', 'Binh thuong'),
(47, 47, 47, '2025-08-16 16:00:00', '2025-08-26 16:00:00', 'Tot', 'Binh thuong'),
(48, 48, 48, '2025-08-17 17:00:00', '2025-08-27 17:00:00', 'Tot', 'Binh thuong');

-- Cập nhật head_employee_id cho Department để không còn NULL
UPDATE Department SET head_employee_id = 1 WHERE department_id = 1;
UPDATE Department SET head_employee_id = 2 WHERE department_id = 2;
UPDATE Department SET head_employee_id = 3 WHERE department_id = 3;
UPDATE Department SET head_employee_id = 4 WHERE department_id = 4;
UPDATE Department SET head_employee_id = 5 WHERE department_id = 5;
UPDATE Department SET head_employee_id = 6 WHERE department_id = 6;
UPDATE Department SET head_employee_id = 7 WHERE department_id = 7;
UPDATE Department SET head_employee_id = 8 WHERE department_id = 8;
UPDATE Department SET head_employee_id = 9 WHERE department_id = 9;
UPDATE Department SET head_employee_id = 10 WHERE department_id = 10;
UPDATE Department SET head_employee_id = 11 WHERE department_id = 11;
UPDATE Department SET head_employee_id = 12 WHERE department_id = 12;
UPDATE Department SET head_employee_id = 13 WHERE department_id = 13;
UPDATE Department SET head_employee_id = 14 WHERE department_id = 14;
UPDATE Department SET head_employee_id = 15 WHERE department_id = 15;
UPDATE Department SET head_employee_id = 16 WHERE department_id = 16;
UPDATE Department SET head_employee_id = 17 WHERE department_id = 17;
UPDATE Department SET head_employee_id = 18 WHERE department_id = 18;
UPDATE Department SET head_employee_id = 19 WHERE department_id = 19;
UPDATE Department SET head_employee_id = 20 WHERE department_id = 20;
UPDATE Department SET head_employee_id = 21 WHERE department_id = 21;
UPDATE Department SET head_employee_id = 22 WHERE department_id = 22;
UPDATE Department SET head_employee_id = 23 WHERE department_id = 23;
UPDATE Department SET head_employee_id = 24 WHERE department_id = 24;
UPDATE Department SET head_employee_id = 25 WHERE department_id = 25;
UPDATE Department SET head_employee_id = 26 WHERE department_id = 26;
UPDATE Department SET head_employee_id = 27 WHERE department_id = 27;
UPDATE Department SET head_employee_id = 28 WHERE department_id = 28;
UPDATE Department SET head_employee_id = 29 WHERE department_id = 29;
UPDATE Department SET head_employee_id = 30 WHERE department_id = 30;
UPDATE Department SET head_employee_id = 31 WHERE department_id = 31;
UPDATE Department SET head_employee_id = 32 WHERE department_id = 32;
UPDATE Department SET head_employee_id = 33 WHERE department_id = 33;
UPDATE Department SET head_employee_id = 34 WHERE department_id = 34;
UPDATE Department SET head_employee_id = 35 WHERE department_id = 35;
UPDATE Department SET head_employee_id = 36 WHERE department_id = 36;
UPDATE Department SET head_employee_id = 37 WHERE department_id = 37;
UPDATE Department SET head_employee_id = 38 WHERE department_id = 38;
UPDATE Department SET head_employee_id = 39 WHERE department_id = 39;
UPDATE Department SET head_employee_id = 40 WHERE department_id = 40;
UPDATE Department SET head_employee_id = 41 WHERE department_id = 41;
UPDATE Department SET head_employee_id = 42 WHERE department_id = 42;
UPDATE Department SET head_employee_id = 43 WHERE department_id = 43;
UPDATE Department SET head_employee_id = 44 WHERE department_id = 44;
UPDATE Department SET head_employee_id = 45 WHERE department_id = 45;
UPDATE Department SET head_employee_id = 46 WHERE department_id = 46;
UPDATE Department SET head_employee_id = 47 WHERE department_id = 47;
UPDATE Department SET head_employee_id = 48 WHERE department_id = 48;
UPDATE Department SET head_employee_id = 49 WHERE department_id = 49;
UPDATE Department SET head_employee_id = 50 WHERE department_id = 50;