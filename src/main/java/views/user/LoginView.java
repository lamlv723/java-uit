package views.user;

import controllers.user.UserSession;
import dao.main.EmployeeDAOImpl;
import models.main.Employee;
import views.main.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginView() {
        setTitle("Đăng nhập");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label và field cho Tên đăng nhập
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        // Label và field cho Mật khẩu
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        // Nút Đăng nhập
        loginButton = new JButton("Đăng nhập");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        // Label hiển thị trạng thái
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);

        add(panel);

        // Action Listener cho nút Đăng nhập
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();
                Employee employee = employeeDAO.getEmployeeByUsernameAndPassword(username, password);

                if (employee != null) {
                    // Đăng nhập thành công
                    UserSession.getInstance().setLoggedInEmployee(employee);
                    JOptionPane.showMessageDialog(LoginView.this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    // Mở MainView và đóng LoginView
                    new MainView().setVisible(true);
                    dispose();
                } else {
                    // Đăng nhập thất bại
                    statusLabel.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}