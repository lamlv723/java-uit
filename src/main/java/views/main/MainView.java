package views.main;

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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainView().setVisible(true);
        });
    }
}