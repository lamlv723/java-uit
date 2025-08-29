package views.user;

import controllers.user.UserSession;
import dao.main.EmployeeDAOImpl;
import models.main.Employee;
import views.main.MainView;
import views.common.CustomComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import com.formdev.flatlaf.FlatClientProperties;


public class LoginView extends JFrame {
    private CustomComponent.SearchField usernameField;
    private CustomComponent.PasswordFieldWithReveal passwordField;
    private CustomComponent.PrimaryButton loginButton;
    private JLabel statusLabel;
    
    public LoginView() {
        setTitle("Asset Management");
        setSize(400, 470);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Font cho toàn bộ
        Font FontThuong = new Font("Segoe UI", Font.PLAIN, 14);
        Font FontDam = new Font("Segoe UI", Font.BOLD, 14);
        UIManager.put("Title.font", FontThuong);
        UIManager.put("Label.font", FontThuong);
        UIManager.put("TextField.font", FontThuong);
        UIManager.put("PasswordField.font", FontThuong);
        
        //Panel Login
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(250, 250, 250));
        root.setBorder(new EmptyBorder(0, 0, 0, 0));

        //Panel Card bo góc
        JPanel card = new JPanel(new BorderLayout(0, 18));
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(32, 32, 32, 32));
        
        // Header: Avatar + Tiêu đề
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        header.setOpaque(false);
        // header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        
        int AVATAR_SIZE = 56;
        java.net.URL url = LoginView.class.getResource("/icons/user.png");
        JLabel avatar;
        if (url != null) {
            // tạo icon đã scale
            ImageIcon raw = new ImageIcon(url);
            Image img = raw.getImage().getScaledInstance(AVATAR_SIZE, AVATAR_SIZE, Image.SCALE_SMOOTH);
            avatar = new JLabel(new ImageIcon(img), SwingConstants.CENTER);
        } else {
            // fallback emoji
            avatar = new JLabel("\uD83D\uDC64", SwingConstants.CENTER);
            avatar.setFont(avatar.getFont().deriveFont(40f));
        }
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(avatar);
        
        JLabel title = new JLabel("Đăng nhập", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        JPanel headerWrap = new JPanel();
        headerWrap.setOpaque(false);
        headerWrap.setLayout(new BoxLayout(headerWrap, BoxLayout.Y_AXIS));
        headerWrap.add(header);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerWrap.add(title);
        
        //Form 2 hàng Username, Password
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Label Tên đăng nhập
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setBorder(new EmptyBorder(0, 2, 6, 2));
        form.add(usernameLabel, gbc);
        
        // field Tên đăng nhập
        gbc.gridy++;
        usernameField = new CustomComponent.SearchField("Nhập tên đăng nhập");
        usernameField.setColumns(22);
        usernameField.setPreferredSize(new Dimension(330, 48));
        form.add(usernameField, gbc);
        java.net.URL userIco = LoginView.class.getResource("/icons/user.svg");
        if (userIco != null)
            usernameField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new ImageIcon(userIco));
        
        // Label và field cho Mật khẩu
        gbc.gridy++;
        gbc.insets = new Insets(12, 0, 10, 0);
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setBorder(new EmptyBorder(0, 2, 6, 2));
        form.add(passwordLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        passwordField = new CustomComponent.PasswordFieldWithReveal("Nhập mật khẩu");
        passwordField.setColumns(22);
        passwordField.setPreferredSize(new Dimension(330, 48));
        form.add(passwordField, gbc);
        java.net.URL lockIco = LoginView.class.getResource("/icons/lock.svg");
        if (lockIco != null)
            passwordField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new ImageIcon(lockIco));
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");
        passwordField.putClientProperty("JComponent.roundRect", true); // bo góc
        usernameField.putClientProperty("JComponent.roundRect", true); // bo góc


        // Footer: Nút Đăng nhập
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        loginButton = new CustomComponent.PrimaryButton("Đăng nhập");
        loginButton.setPreferredSize(new Dimension(340, 40));
        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(loginButton.getFont().deriveFont(Font.BOLD, 14f));
        footer.add(loginButton, BorderLayout.CENTER);
        footer.setBorder(new EmptyBorder(12, 0, 0, 0));

        // Label trạng thái màu đỏ bên dưới nút
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(206, 33, 39));
        statusLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ghép header, form vào card
        card.add(headerWrap, BorderLayout.NORTH);
        card.add(form, BorderLayout.CENTER);
        
        // Gom footer + status vào panel dọc
        JPanel bottomWrap = new JPanel();
        bottomWrap.setOpaque(false);
        bottomWrap.setLayout(new BoxLayout(bottomWrap, BoxLayout.Y_AXIS));
        bottomWrap.add(footer);
        bottomWrap.add(statusLabel);
        card.add(bottomWrap, BorderLayout.SOUTH);
        
        root.add(card,BorderLayout.CENTER);
        setContentPane(root);
                        
        // Đặt nút Đăng nhập làm nút mặc định khi nhấn Enter
        this.getRootPane().setDefaultButton(loginButton);
        
        // Action Listener cho nút Đăng nhập
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                // Kiểm tra rỗng để báo sớm
                if (username.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
                    statusLabel.setVisible(true);
                    statusLabel.revalidate();
                    statusLabel.repaint();
                    return;
                }

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
                    statusLabel.setVisible(true);
                    statusLabel.revalidate();
                    statusLabel.repaint();
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