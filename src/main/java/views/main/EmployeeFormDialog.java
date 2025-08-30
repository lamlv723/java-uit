package views.main;

import controllers.main.EmployeeController;
import controllers.user.UserSession;
import models.main.Employee;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/** Dialog thêm / sửa Nhân viên dùng BaseFormDialog */
public class EmployeeFormDialog extends views.common.BaseFormDialog {
    private final EmployeeController controller; private final Employee employee; // null => add
    private JTextField tfFirstName, tfLastName, tfEmail, tfPhone, tfDeptId, tfRole, tfUsername, tfPassword;

    public EmployeeFormDialog(Frame owner, EmployeeController controller, Employee employee){
        super(owner, 520, 460, "user", employee == null ? "Thêm Nhân viên" : "Chỉnh sửa Nhân viên");
        this.controller = controller;
        this.employee = employee;
        initForm();
        buildUI();
    }

    private void initForm(){ tfFirstName=new JTextField(); tfLastName=new JTextField(); tfEmail=new JTextField(); tfPhone=new JTextField(); tfDeptId=new JTextField(); tfRole=new JTextField(); tfUsername=new JTextField(); tfPassword=new JTextField(); if(employee!=null){ tfFirstName.setText(employee.getFirstName()); tfLastName.setText(employee.getLastName()); tfEmail.setText(employee.getEmail()); tfPhone.setText(employee.getPhoneNumber()); if(employee.getDepartmentId()!=null) tfDeptId.setText(String.valueOf(employee.getDepartmentId())); tfRole.setText(employee.getRole()); tfUsername.setText(employee.getUsername()); tfPassword.setText(employee.getPassword()); } }

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
        addField(p, gbc, "ID phòng ban", tfDeptId);
        addField(p, gbc, "Vai trò *", tfRole);
        addField(p, gbc, "Tên đăng nhập *", tfUsername);
        addField(p, gbc, "Mật khẩu *", tfPassword);
        return p;
    }
    private void addField(JPanel p, GridBagConstraints gbc, String label, JComponent input){ gbc.gridwidth=1; gbc.gridx=0; p.add(new JLabel(label),gbc); gbc.gridx=1; p.add(input,gbc); gbc.gridy++; }

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
        String deptStr = tfDeptId.getText().trim();
        if (!deptStr.isEmpty()) {
            try {
                e.setDepartmentId(Integer.parseInt(deptStr));
            } catch (NumberFormatException ex) {
                UIUtils.showErrorDialog(this, "ID phòng ban phải là số", "Lỗi");
                return;
            }
        } else {
            e.setDepartmentId(null);
        }
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
