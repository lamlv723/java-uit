package views.main;

import javax.swing.*;
import java.awt.*;

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

        // Action mở các view quản lý
        mnuAssetCategory.addActionListener(e -> new views.device.AssetCategoryManagementView().setVisible(true));
        mnuAsset.addActionListener(e -> new views.device.AssetManagementView().setVisible(true));
        mnuAssetRequest.addActionListener(e -> new views.device.AssetRequestManagementView().setVisible(true));
        mnuAssetRequestItem.addActionListener(e -> new views.device.AssetRequestItemManagementView().setVisible(true));
        mnuVendor.addActionListener(e -> new views.device.VendorManagementView().setVisible(true));
        mnuDepartment.addActionListener(e -> new views.main.DepartmentManagementView().setVisible(true));
        mnuEmployee.addActionListener(e -> new views.main.EmployeeManagementView().setVisible(true));

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
