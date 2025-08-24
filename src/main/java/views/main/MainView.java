package views.main;

import controllers.user.UserSession;
import views.user.ChangePasswordView;
import views.user.LoginView;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public MainView() {
        setTitle("Enterprise Asset Management System (Java Swing, Hibernate, Maven, MySQL)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        /************************ MENU BAR ************************/
        // Menu quản lý CRUD
        JMenuBar menuBar = new JMenuBar();
        JMenu menuDevice = new JMenu("Thiết bị");
        JMenuItem mnuAssetCategory = new JMenuItem("Danh mục tài sản");
        JMenuItem mnuAsset = new JMenuItem("Tài sản");
        JMenuItem mnuAssetRequest = new JMenuItem("Yêu cầu tài sản");
        JMenuItem mnuAssetRequestItem = new JMenuItem("Chi tiết yêu cầu");
        JMenuItem mnuVendor = new JMenuItem("Nhà cung cấp");
        menuDevice.add(mnuAssetCategory);
        menuDevice.add(mnuAsset);
        menuDevice.add(mnuAssetRequest);
        menuDevice.add(mnuAssetRequestItem);
        menuDevice.add(mnuVendor);

        JMenu menuMain = new JMenu("Chính");
        JMenuItem mnuDepartment = new JMenuItem("Phòng ban");
        JMenuItem mnuEmployee = new JMenuItem("Nhân viên");
        menuMain.add(mnuDepartment);
        menuMain.add(mnuEmployee);

        menuBar.add(menuDevice);
        menuBar.add(menuMain);
        setJMenuBar(menuBar);

        // PERSONAL PROFILE
        // Tạo menu cho người dùng hiện tại
        JMenu menuUser = new JMenu();
        if (UserSession.getInstance().getLoggedInEmployee() != null) {
            String firstName = UserSession.getInstance().getLoggedInEmployee().getFirstName();
            String lastName = UserSession.getInstance().getLoggedInEmployee().getLastName();
            String fullName = String.join(" ", firstName, lastName);
            menuUser.setText(fullName);
        }

        JMenuItem mnuMyAssets = new JMenuItem("Tài sản của tôi");
        JMenuItem mnuChangePassword = new JMenuItem("Đổi mật khẩu");
        JMenuItem mnuLogout = new JMenuItem("Đăng xuất");

        // Thêm các menu con vào menu người dùng
        menuUser.add(mnuMyAssets);
        menuUser.add(mnuChangePassword);
        menuUser.add(mnuLogout);

        // Thêm menu người dùng vào menu bar và căn lề phải
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuUser);
        setJMenuBar(menuBar);


        /************************ ACTIONS ************************/
        // Action mở các view quản lý
        mnuAssetCategory.addActionListener(e -> new views.device.AssetCategoryManagementView().setVisible(true));
        mnuAsset.addActionListener(e -> new views.device.AssetManagementView().setVisible(true));
        mnuAssetRequest.addActionListener(e -> new views.device.AssetRequestManagementView().setVisible(true));
        mnuAssetRequestItem.addActionListener(e -> new views.device.AssetRequestItemManagementView().setVisible(true));
        mnuVendor.addActionListener(e -> new views.device.VendorManagementView().setVisible(true));
        mnuDepartment.addActionListener(e -> new views.main.DepartmentManagementView().setVisible(true));
        mnuEmployee.addActionListener(e -> new views.main.EmployeeManagementView().setVisible(true));

        // Action cho nút "Đổi mật khẩu"
        mnuChangePassword.addActionListener(e -> {
            new ChangePasswordView().setVisible(true);
        });

        // Action cho nút "Tài sản của tôi"
        mnuMyAssets.addActionListener(e -> {
            // Tạm thời hiển thị thông báo, sẽ thay bằng việc mở cửa sổ mới ở bước sau
            JOptionPane.showMessageDialog(this, "Chức năng xem tài sản của tôi sẽ được triển khai!");
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel(
                "Welcome to Enterprise Asset Management System (Java Swing, Hibernate, Maven, MySQL) App!",
                SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        add(panel);

        // Action cho nút Đăng xuất
        mnuLogout.addActionListener(e -> {
            // Xóa phiên làm việc hiện tại
            UserSession.getInstance().clearSession();

            // Đóng MainView và mở lại LoginView
            dispose();
            new LoginView().setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainView().setVisible(true);
        });
    }
}
