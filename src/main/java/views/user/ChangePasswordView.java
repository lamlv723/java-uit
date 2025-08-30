package views.user;

import controllers.user.UserSession;
import models.main.Employee;
import services.main.EmployeeService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ChangePasswordView extends JFrame {
    // Colors & sizing constants
    private static final Color COLOR_PRIMARY = new Color(59, 130, 246);
    private static final Color COLOR_TEXT_DARK = new Color(55, 65, 81);
    private static final Color COLOR_ERROR = new Color(220, 38, 38); // red-600
    private static final Color COLOR_BUTTON_SECONDARY = new Color(107, 114, 128);
    private static final String FONT_FAMILY = "Segoe UI";
    private static final int FIELD_SPACING = 8;

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton btnConfirm;
    private JButton btnCancel;
    private JLabel statusLabel;

    public ChangePasswordView() {
        configureFrame();
        JPanel container = buildContainer();
        container.add(buildHeader(), BorderLayout.NORTH);
        container.add(buildFieldsPanel(), BorderLayout.CENTER);
        container.add(buildStatusLabel(), BorderLayout.SOUTH);
        container.add(buildButtons(), BorderLayout.PAGE_END);
        wireEvents();
        installShortcuts();
    }

    // (old labeledField method removed after refactor)

    private JPanel fieldGroup(String labelText, JPasswordField field) {
        JPanel group = new JPanel();
        group.setOpaque(false);
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        label.setForeground(COLOR_TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        group.add(label);
        group.add(Box.createVerticalStrut(2)); // small internal gap label -> field
        group.add(field);

        // Constrain group width to field width while letting BoxLayout compute height
        int h = label.getPreferredSize().height + 2 + field.getPreferredSize().height;
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        return group;
    }

    private JPasswordField createPasswordField() {
        return new TinyPasswordField();
    }

    /**
     * Compact password field with fixed height (configurable via constant below).
     */
    private static class TinyPasswordField extends JPasswordField {
        private static final int FIXED_HEIGHT = 36; // desired height per request
        private static final int WIDTH = 300;
        private final Dimension fixed = new Dimension(WIDTH, FIXED_HEIGHT);

        TinyPasswordField() {
            // Font size suitable for 36px height
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(new LineBorder(new Color(209, 213, 219), 1, true));
            // Horizontal padding; vertical kept 0 as height enforced outside text metrics
            setMargin(new Insets(0, 8, 0, 8));
            putClientProperty("JComponent.minimumHeight", FIXED_HEIGHT); // FlatLaf hint
            // Force a basic UI to prevent FlatLaf from adding extra vertical paddings
            setUI(new javax.swing.plaf.basic.BasicPasswordFieldUI());
            setPreferredSize(fixed);
            setMinimumSize(fixed);
        }

        @Override
        public Dimension getPreferredSize() {
            return fixed;
        }

        @Override
        public Dimension getMinimumSize() {
            return fixed;
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, FIXED_HEIGHT);
        }

        @Override
        public Insets getInsets() {
            return new Insets(0, 8, 0, 8);
        }

        @Override
        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, y, w, FIXED_HEIGHT);
        }

        @Override
        public void doLayout() {
            super.doLayout();
            if (getHeight() != FIXED_HEIGHT)
                setSize(getWidth(), FIXED_HEIGHT);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setUI(new javax.swing.plaf.basic.BasicPasswordFieldUI());
        }
    }

    private JButton styledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    private void handleChangePassword() {
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        String validationError = validateInputs(oldPass, newPass, confirmPass);
        if (validationError != null) {
            showError(validationError);
            return;
        }

        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        if (currentUser == null) {
            showError("Không tìm thấy thông tin người dùng.");
            return;
        }

        String error = new EmployeeService().changePassword(currentUser, oldPass, newPass);
        if (error == null) {
            showSuccess("Đổi mật khẩu thành công!");
            dispose();
        } else
            showError(error);
    }

    private void showError(String message) {
        statusLabel.setForeground(COLOR_ERROR);
        statusLabel.setText(message);
        JOptionPane.showMessageDialog(this, message, "Thất bại", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        statusLabel.setForeground(COLOR_PRIMARY);
        statusLabel.setText(message);
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private String validateInputs(String oldPass, String newPass, String confirmPass) {
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty())
            return "Vui lòng nhập đầy đủ các trường.";
        if (newPass.length() < 6)
            return "Mật khẩu mới phải ít nhất 6 ký tự.";
        if (!newPass.equals(confirmPass))
            return "Xác nhận mật khẩu mới không khớp.";
        if (oldPass.equals(newPass))
            return "Mật khẩu mới phải khác mật khẩu cũ.";
        return null;
    }

    // ---------------- UI build helpers ----------------
    private void configureFrame() {
        setTitle("Đổi mật khẩu");
        setSize(480, 410);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private JPanel buildContainer() {
        JPanel container = new JPanel(new BorderLayout(16, 16));
        container.setBorder(new EmptyBorder(16, 24, 16, 24));
        container.setBackground(Color.WHITE);
        add(container);
        return container;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        JLabel icon = new JLabel(ui.IconFactory.get("key-solid", 28));
        icon.setForeground(COLOR_PRIMARY);
        icon.setBorder(new EmptyBorder(0, 0, 0, 12));
        JLabel title = new JLabel("Đổi mật khẩu");
        title.setFont(new Font(FONT_FAMILY, Font.BOLD, 18));
        title.setForeground(COLOR_TEXT_DARK);
        header.add(icon);
        header.add(title);
        return header;
    }

    private JPanel buildFieldsPanel() {
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setOpaque(false);

        oldPasswordField = createPasswordField();
        newPasswordField = createPasswordField();
        confirmPasswordField = createPasswordField();

        fieldsPanel.add(fieldGroup("Mật khẩu cũ:", oldPasswordField));
        fieldsPanel.add(Box.createVerticalStrut(FIELD_SPACING));
        fieldsPanel.add(fieldGroup("Mật khẩu mới:", newPasswordField));
        fieldsPanel.add(Box.createVerticalStrut(FIELD_SPACING));
        fieldsPanel.add(fieldGroup("Xác nhận mật khẩu mới:", confirmPasswordField));
        return fieldsPanel;
    }

    private JLabel buildStatusLabel() {
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        statusLabel.setForeground(COLOR_ERROR);
        return statusLabel;
    }

    private JPanel buildButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        buttonPanel.setOpaque(false);
        btnConfirm = styledButton("Xác nhận", COLOR_PRIMARY);
        btnCancel = styledButton("Hủy", COLOR_BUTTON_SECONDARY);
        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);
        return buttonPanel;
    }

    private void wireEvents() {
        btnConfirm.addActionListener(e -> handleChangePassword());
        btnCancel.addActionListener(e -> dispose());
    }

    private void installShortcuts() {
        // Enter triggers confirm, Esc triggers cancel
        JRootPane root = getRootPane();
        root.setDefaultButton(btnConfirm);
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        am.put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
    }
}