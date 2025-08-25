package views.user;

import controllers.user.UserSession;
import models.main.Employee;
import services.main.EmployeeService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordView extends JFrame {
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton btnConfirm;
    private JButton btnCancel;
    private JLabel statusLabel;

    public ChangePasswordView() {
        setTitle("Đổi mật khẩu");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel for input fields
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Old Password
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Mật khẩu cũ:"), gbc);
        oldPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        fieldsPanel.add(oldPasswordField, gbc);

        // New Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldsPanel.add(new JLabel("Mật khẩu mới:"), gbc);
        newPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        fieldsPanel.add(newPasswordField, gbc);

        // Confirm New Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldsPanel.add(new JLabel("Xác nhận mật khẩu mới:"), gbc);
        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        fieldsPanel.add(confirmPasswordField, gbc);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnConfirm = new JButton("Xác nhận");
        btnCancel = new JButton("Hủy");
        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);

        // Status Label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);

        // Add panels to frame
        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        // Add Action Listeners
        btnConfirm.addActionListener(e -> handleChangePassword());
        btnCancel.addActionListener(e -> dispose());
    }

    private void handleChangePassword() {
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        if (!newPass.equals(confirmPass)) {
            statusLabel.setText("Mật khẩu mới không khớp. Vui lòng nhập lại.");
            return;
        }

        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        if (currentUser == null) {
            statusLabel.setText("Lỗi: Không tìm thấy thông tin người dùng.");
            return;
        }

        // Call Employee services
        EmployeeService employeeService = new EmployeeService();
        String error = employeeService.changePassword(currentUser, oldPass, newPass);

        if (error == null) {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // close window after succeed
        } else {
            // Failed, display error from service
            statusLabel.setText(error);
        }
    }
}