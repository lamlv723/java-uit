package views.main;

import controllers.user.UserSession;
import views.user.LoginView;

import javax.swing.*;
import java.awt.*;
import views.device.AssetRequestManagementView;
import views.device.AssetCategoryManagementView;
import views.device.AssetManagementView;
import views.device.VendorManagementView;
import views.main.DepartmentManagementView;
import views.main.EmployeeManagementView;

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
        JMenuItem mnuAsset = new JMenuItem("Quản lý tài sản");
        JMenuItem mnuAssetRequest = new JMenuItem("Yêu cầu tài sản");
        JMenuItem mnuVendor = new JMenuItem("Nhà cung cấp");
        menuDevice.add(mnuAssetCategory);
        menuDevice.add(mnuAsset);
        menuDevice.add(mnuAssetRequest);
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

        JMenuItem mnuSettings = new JMenuItem("Cài đặt");
        JMenuItem mnuLogout = new JMenuItem("Đăng xuất");

        // Thêm các menu con vào menu người dùng
        menuUser.add(mnuSettings);
        menuUser.add(mnuLogout);

        // Thêm menu người dùng vào menu bar và căn lề phải
        menuBar.add(Box.createHorizontalGlue()); // Đẩy menu sang phải
        menuBar.add(menuUser);
        setJMenuBar(menuBar);


        /************************ ACTIONS ************************/
        // Action mở các view quản lý
        mnuAssetCategory.addActionListener(e -> new AssetCategoryManagementView().setVisible(true));
        mnuAsset.addActionListener(e -> new AssetManagementView().setVisible(true));
        mnuAssetRequest.addActionListener(e -> new AssetRequestManagementView().setVisible(true));
        mnuVendor.addActionListener(e -> new VendorManagementView().setVisible(true));
        mnuDepartment.addActionListener(e -> new DepartmentManagementView().setVisible(true));
        mnuEmployee.addActionListener(e -> new EmployeeManagementView().setVisible(true));

        // Dòng gây lỗi đã được xóa
        // mnuAssetRequestItem.addActionListener(e -> new views.device.AssetRequestItemManagementView().setVisible(true));


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