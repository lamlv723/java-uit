package views.user;

import controllers.user.UserSession;
import models.main.Employee;
import services.user.AuthenticationService;
import views.main.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import ui.AppIcons;
import ui.IconName;
import com.formdev.flatlaf.FlatLightLaf;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel statusLabel;

    public LoginView() {
        setTitle("Đăng nhập");
        setSize(300, 450);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Bo góc cửa sổ
        setUndecorated(true);
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        add(root);

        JLabel userIcon = new JLabel(IconName.USER.icon(64));
        userIcon.setHorizontalAlignment(SwingConstants.CENTER);
        userIcon.setBorder(BorderFactory.createEmptyBorder(20, 0, 4, 0));

        JLabel title = new JLabel("Đăng nhập", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(40, 70, 130));
        title.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(title);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.add(userIcon, BorderLayout.CENTER);
        header.add(titlePanel, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        root.add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Tên đăng nhập:"), gbc);

        gbc.gridy = 1;
        usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        JPanel userFieldPanel = buildInputWithIcon(usernameField, IconName.USER.icon(18));
        centerPanel.add(userFieldPanel, gbc);

        gbc.gridy = 2;
        centerPanel.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridy = 3;
        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        JPanel passFieldPanel = buildInputWithIcon(passwordField, IconName.LOCK.icon(18));
        centerPanel.add(passFieldPanel, gbc);

        gbc.gridy = 4;
        loginButton = new JButton("Đăng nhập", IconName.SIGN_IN_ALT.icon(18));
        loginButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        loginButton.setIconTextGap(6);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(24, 119, 242));
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(220, 44));
        centerPanel.add(loginButton, gbc);

        gbc.gridy = 5;
        cancelButton = new JButton("Hủy bỏ");
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(239, 68, 68));
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(220, 44));
        centerPanel.add(cancelButton, gbc);

        gbc.gridy = 6;
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(200, 40, 40));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        centerPanel.add(statusLabel, gbc);

        getRootPane().setDefaultButton(loginButton);

        setIconImages(AppIcons.windowIconImages(IconName.BUILDING));

        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            AuthenticationService authService = new AuthenticationService();
            Employee employee = authService.authenticate(username, password);

            if (employee != null) {
                UserSession.getInstance().setLoggedInEmployee(employee);
                JOptionPane.showMessageDialog(LoginView.this, "Đăng nhập thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                new MainView().setVisible(true);
                dispose();
            } else {
                statusLabel.setText(" ");
                JOptionPane.showMessageDialog(LoginView.this,
                        "Tên đăng nhập hoặc mật khẩu không đúng hoặc tài khoản bị khóa.",
                        "Lỗi đăng nhập",
                        JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocusInWindow();
            }
        });

        cancelButton.addActionListener((ActionEvent e) -> {
            // Dispose the window and exit the application
            dispose();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        UIManager.put("Button.arc", 15);
        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 15);

        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }

    private JPanel buildInputWithIcon(JComponent field, Icon icon) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 220)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 4));
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(260, 40));
        return panel;
    }
}
