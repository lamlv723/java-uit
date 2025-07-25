package views.main;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controllers.main.EmployeeController;
import services.main.EmployeeService;
import models.main.Employee;
import views.main.components.EmployeeTable;

public class EmployeeManagementView extends JFrame {
    private EmployeeTable table;
    private EmployeeController employeeController;

    public EmployeeManagementView() {
        setTitle("Quản lý Nhân viên");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        employeeController = new EmployeeController(new EmployeeService());
        table = new EmployeeTable();
        loadDataToTable();
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        // Action for Add
        btnAdd.addActionListener(e -> {
            JTextField tfFirstName = new JTextField();
            JTextField tfLastName = new JTextField();
            JTextField tfEmail = new JTextField();
            JTextField tfPhone = new JTextField();
            JTextField tfDeptId = new JTextField();
            JTextField tfRole = new JTextField();
            JTextField tfUsername = new JTextField();
            JTextField tfPassword = new JTextField();
            Object[] message = {
                    "Tên:", tfFirstName,
                    "Họ:", tfLastName,
                    "Email:", tfEmail,
                    "Số điện thoại:", tfPhone,
                    "ID phòng ban:", tfDeptId,
                    "Vai trò:", tfRole,
                    "Tên đăng nhập:", tfUsername,
                    "Mật khẩu:", tfPassword
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Thêm Nhân viên", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                // Thu thập dữ liệu từ UI
                String firstName = tfFirstName.getText().trim();
                String lastName = tfLastName.getText().trim();
                String email = tfEmail.getText().trim();
                String phone = tfPhone.getText().trim();
                String deptIdStr = tfDeptId.getText().trim();
                String role = tfRole.getText().trim();
                String username = tfUsername.getText().trim();
                String password = tfPassword.getText().trim();
                // Gọi service xử lý nghiệp vụ, trả về lỗi nếu có
                String error = employeeController.getEmployeeService().addEmployeeFromInput(firstName, lastName, email,
                        phone, deptIdStr, role, username, password, "ADMIN");
                if (error == null) {
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Edit
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để sửa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            Employee emp = employeeController.getEmployeeById(id);
            if (emp == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTextField tfFirstName = new JTextField(emp.getFirstName());
            JTextField tfLastName = new JTextField(emp.getLastName());
            JTextField tfEmail = new JTextField(emp.getEmail());
            JTextField tfPhone = new JTextField(emp.getPhoneNumber());
            JTextField tfDeptId = new JTextField(
                    emp.getDepartmentId() != null ? String.valueOf(emp.getDepartmentId()) : "");
            JTextField tfRole = new JTextField(emp.getRole());
            JTextField tfUsername = new JTextField(emp.getUsername());
            JTextField tfPassword = new JTextField(emp.getPassword());
            Object[] message = {
                    "Tên:", tfFirstName,
                    "Họ:", tfLastName,
                    "Email:", tfEmail,
                    "Số điện thoại:", tfPhone,
                    "ID phòng ban:", tfDeptId,
                    "Vai trò:", tfRole,
                    "Tên đăng nhập:", tfUsername,
                    "Mật khẩu:", tfPassword
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Sửa Nhân viên", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String firstName = tfFirstName.getText().trim();
                String lastName = tfLastName.getText().trim();
                String email = tfEmail.getText().trim();
                String phone = tfPhone.getText().trim();
                String deptIdStr = tfDeptId.getText().trim();
                String role = tfRole.getText().trim();
                String username = tfUsername.getText().trim();
                String password = tfPassword.getText().trim();
                if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !username.isEmpty()
                        && !password.isEmpty()) {
                    emp.setFirstName(firstName);
                    emp.setLastName(lastName);
                    emp.setEmail(email);
                    emp.setPhoneNumber(phone);
                    emp.setRole(role);
                    emp.setUsername(username);
                    emp.setPassword(password);
                    if (!deptIdStr.isEmpty()) {
                        try {
                            int deptId = Integer.parseInt(deptIdStr);
                            emp.setDepartmentId(deptId);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "ID phòng ban phải là số!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        emp.setDepartmentId(null);
                    }
                    employeeController.updateEmployee(emp, "ADMIN"); // TODO: lấy role thực tế nếu có
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Các trường bắt buộc không được để trống!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Delete
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                employeeController.deleteEmployee(id, "ADMIN"); // TODO: lấy role thực tế nếu có
                loadDataToTable();
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        List<Employee> list = employeeController.getAllEmployees();
        table.setEmployeeData(list);
    }
}
