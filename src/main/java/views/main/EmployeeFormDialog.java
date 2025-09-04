package views.main;

import controllers.main.EmployeeController;
import controllers.user.UserSession;
import models.main.Department;
import models.main.Employee;
import controllers.main.DepartmentController;
import utils.UIUtils;
import java.util.List;

import javax.swing.*;
import java.awt.*;

/** Dialog thêm / sửa Nhân viên dùng BaseFormDialog */
public class EmployeeFormDialog extends views.common.BaseFormDialog {
    private final EmployeeController controller;
    private final Employee employee; // null => add
    private JTextField tfFirstName, tfLastName, tfEmail, tfPhone, tfRole, tfUsername, tfPassword;
    private JComboBox<Department> cbDepartment;

    public EmployeeFormDialog(Frame owner, EmployeeController controller, Employee employee) {
        super(owner, 520, 460, "user", employee == null ? "Thêm Nhân viên" : "Chỉnh sửa Nhân viên");
        this.controller = controller;
        this.employee = employee;
        initForm();
        buildUI();
    }

    private void initForm() {
        tfFirstName = new JTextField();
        tfLastName = new JTextField();
        tfEmail = new JTextField();
        tfPhone = new JTextField();
        cbDepartment = new JComboBox<>();
        loadDepartmentsIntoComboBox();
        tfRole = new JTextField();
        tfUsername = new JTextField();
        tfPassword = new JTextField();
        if (employee != null) {
            tfFirstName.setText(employee.getFirstName());
            tfLastName.setText(employee.getLastName());
            tfEmail.setText(employee.getEmail());
            tfPhone.setText(employee.getPhoneNumber());
            if (employee.getDepartment() != null) {
                for (int i = 0; i < cbDepartment.getItemCount(); i++) {
                    Department dept = cbDepartment.getItemAt(i);
                    if (dept != null && dept.getDepartmentId().equals(employee.getDepartmentId())) {
                        cbDepartment.setSelectedIndex(i);
                        break;
                    }
                }
            }
            tfRole.setText(employee.getRole());
            tfUsername.setText(employee.getUsername());
            tfPassword.setText(employee.getPassword());
        }
    }

    protected JPanel buildFormPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        addField(p, gbc, "Tên *", tfFirstName);
        addField(p, gbc, "Họ *", tfLastName);
        addField(p, gbc, "Email *", tfEmail);
        addField(p, gbc, "Số điện thoại", tfPhone);
        addField(p, gbc, "Phòng ban", cbDepartment);
        addField(p, gbc, "Vai trò *", tfRole);
        addField(p, gbc, "Tên đăng nhập *", tfUsername);
        addField(p, gbc, "Mật khẩu *", tfPassword);
        return p;
    }

    // Method to load departments into the JComboBox
    private void loadDepartmentsIntoComboBox() {
        DepartmentController deptController = new DepartmentController();
        List<Department> departments = deptController
                .getAllDepartments(UserSession.getInstance().getLoggedInEmployee());

        // Add a null option for "No Department"
        cbDepartment.addItem(null);
        if (departments != null) {
            for (Department dept : departments) {
                cbDepartment.addItem(dept);
            }
        }

        // Custom renderer to display department's name
        cbDepartment.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Department) {
                    setText(((Department) value).getDepartmentName());
                } else {
                    setText("— Không thuộc phòng ban —");
                }
                return this;
            }
        });
    }

    private void addField(JPanel p, GridBagConstraints gbc, String label, JComponent input) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(input, gbc);
        gbc.gridy++;
    }

    protected void onSave() {
        if (tfFirstName.getText().trim().isEmpty() || tfLastName.getText().trim().isEmpty()
                || tfEmail.getText().trim().isEmpty() || tfRole.getText().trim().isEmpty()
                || tfUsername.getText().trim().isEmpty() || tfPassword.getText().trim().isEmpty()) {
            UIUtils.showErrorDialog(this, "Các trường bắt buộc không được trống", "Lỗi");
            return;
        }
        Employee e = employee == null ? new Employee() : employee;
        e.setFirstName(tfFirstName.getText().trim());
        e.setLastName(tfLastName.getText().trim());
        e.setEmail(tfEmail.getText().trim());
        e.setPhoneNumber(tfPhone.getText().trim());
        e.setRole(tfRole.getText().trim());
        e.setUsername(tfUsername.getText().trim());
        e.setPassword(tfPassword.getText().trim());

        // Get the selected Department object from the JComboBox
        Department selectedDept = (Department) cbDepartment.getSelectedItem();
        e.setDepartment(selectedDept);

        Employee user = UserSession.getInstance().getLoggedInEmployee();
        try {
            if (employee == null)
                controller.addEmployee(e, user);
            else
                controller.updateEmployee(e, user);
            saved = true;
            dispose();
        } catch (Exception ex) {
            UIUtils.showErrorDialog(this, "Không thể lưu nhân viên: " + ex.getMessage(), "Lỗi Hệ thống");
            ex.printStackTrace();
        }
    }
}
