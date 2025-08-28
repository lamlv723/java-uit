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

    private static final Dimension INPUT_SIZE = new Dimension(440, 44);
    private static final int RADIUS = 12;
    private static final Color INPUT_BG = Color.WHITE;
    private static final Color INPUT_BORDER = new Color(220, 225, 230);

    public ChangePasswordView() {
        setTitle("Đổi mật khẩu");
        setSize(520, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        Font FontThuong = new Font("Segoe UI", Font.PLAIN, 14);
        Font FontDam = new Font("Segoe UI", Font.BOLD, 14);
        UIManager.put("Title.font", FontThuong);
        UIManager.put("Label.font", FontThuong);
        UIManager.put("TextField.font", FontThuong);
        UIManager.put("PasswordField.font", FontThuong);
        UIManager.put("Button.font", FontDam);
        
        //Panel tiêu đề
        JPanel topPad = new JPanel();
        topPad.setOpaque(false);
        topPad.setBorder(BorderFactory.createEmptyBorder(16, 20, 0, 20));
        add(topPad, BorderLayout.NORTH);

        // Panel for input fields
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        // Mật khẩu cũ
        fieldsPanel.add(createLabeledField("Mật khẩu cũ:",
                oldPasswordField = new JPasswordField(), "/icons/lock.png"), gbc);
        
        // Mật khẩu mới
        gbc.gridy++;
        fieldsPanel.add(createLabeledField("Mật khẩu mới:", newPasswordField = new JPasswordField(), "/icons/key.png"), gbc);

        // Xác nhận mật khẩu mới
        gbc.gridy++;
        fieldsPanel.add(createLabeledField("Xác nhận mật khẩu mới:", confirmPasswordField = new JPasswordField(), "/icons/check.png"), gbc);

        add(fieldsPanel, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttonPanel.setOpaque(false);

        // Button confirm
        btnConfirm = new JButton("Xác nhận");
        btnConfirm.putClientProperty("JButton.buttonType", "roundRect");     // bo góc
        btnConfirm.setBackground(new Color(59, 130, 246));                    // xanh
        btnConfirm.setForeground(Color.WHITE);                                 // chữ trắng
        btnConfirm.setPreferredSize(new Dimension(140, 44));
        buttonPanel.add(btnConfirm);

        // Button cancel
        btnCancel = new JButton("Hủy");
        btnCancel.putClientProperty("JButton.buttonType", "roundRect");
        btnCancel.setBackground(new Color(107, 114, 128));                    // xám 
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(120, 44));
        buttonPanel.add(btnCancel);



        // Status Label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(199, 51, 56));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 14, 0));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(buttonPanel);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        south.add(statusLabel);

        add(south, BorderLayout.SOUTH);

        // Enter = xác nhận
        getRootPane().setDefaultButton(btnConfirm);

        // Add Action Listeners
        btnConfirm.addActionListener(e -> handleChangePassword());
        btnCancel.addActionListener(e -> dispose());
    }

    private JPanel createLabeledField(String labelText, JPasswordField field, String iconPath) {
        JPanel wrap = new JPanel(new BorderLayout(0, 6));
        wrap.setOpaque(false);

        JLabel label = new JLabel(labelText);
        wrap.add(label, BorderLayout.NORTH);

        JPanel container = new RoundedContainer(RADIUS, INPUT_BG, INPUT_BORDER);
        container.setLayout(new BorderLayout());
        container.setPreferredSize(INPUT_SIZE);

        // Icon trái
        JLabel icon = new JLabel();
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 12));
        java.net.URL url = ChangePasswordView.class.getResource(iconPath);
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            icon.setIcon(new ImageIcon(img));
        }
        container.add(icon, BorderLayout.WEST);

        // Field phải (bo góc bằng container, nên bỏ border mặc định)
        field.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 12));
        field.setOpaque(false);
        field.setCaretColor(Color.DARK_GRAY);
        container.add(field, BorderLayout.CENTER);

        wrap.add(container, BorderLayout.CENTER);
        return wrap;
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

    // ======= Vẽ container bo góc cho input =======
    static class RoundedContainer extends JPanel {
        private final int radius;
        private final Color bg;
        private final Color border;

        public RoundedContainer(int radius, Color bg, Color border) {
            this.radius = radius;
            this.bg = bg;
            this.border = border;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}