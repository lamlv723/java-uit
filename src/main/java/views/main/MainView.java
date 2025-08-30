package views.main;

import controllers.user.UserSession;
import views.user.ChangePasswordView;
import views.user.LoginView;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;
import utils.UITheme;
import ui.IconName;
import ui.AppIcons;

public class MainView extends JFrame {
    public MainView() {
        setTitle("Enterprise Asset Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800); // per design
        setLocationRelativeTo(null);
        // Frame/taskbar icon set (consistent with LoginView building icon)
        setIconImages(AppIcons.windowIconImages(IconName.BUILDING));

        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);

        /************************ MENU BAR ************************/
        JMenuItem mnuAssetCategory = menuItem("Danh mục tài sản");
        JMenuItem mnuAsset = menuItem("Tài sản");
        JMenuItem mnuAssetRequest = menuItem("Yêu cầu tài sản");
        JMenuItem mnuAssetRequestItem = menuItem("Chi tiết yêu cầu");
        JMenuItem mnuVendor = menuItem("Nhà cung cấp");
        JMenuItem mnuDepartment = menuItem("Phòng ban");
        JMenuItem mnuEmployee = menuItem("Nhân viên");
        JMenuItem mnuMyAssets = menuItem("Tài sản của tôi");
        JMenuItem mnuChangePassword = menuItem("Đổi mật khẩu");
        JMenuItem mnuLogout = menuItem("Đăng xuất");

        // ======= Top Menu Bar =======
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.PRIMARY_BLUE));

        // Left side: Thiết bị + Chính
        JPanel leftMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        leftMenu.setOpaque(false);

        // Button "Thiết bị" (with icon)
        JButton deviceBtn = menuButton("Thiết bị ▾");
        deviceBtn.setIcon(IconName.LAPTOP.icon(16));
        deviceBtn.setIconTextGap(6);
        JPopupMenu deviceMenu = new JPopupMenu();
        deviceMenu.add(mnuAssetCategory);
        deviceMenu.add(mnuAsset);
        deviceMenu.add(mnuAssetRequest);
        deviceMenu.add(mnuAssetRequestItem);
        deviceMenu.add(mnuVendor);
        deviceBtn.addActionListener(e -> deviceMenu.show(deviceBtn, 0, deviceBtn.getHeight()));
        leftMenu.add(deviceBtn);

        // Button "Chính" (with icon)
        JButton mainBtn = menuButton("Chính ▾");
        mainBtn.setIcon(IconName.COGS.icon(16));
        mainBtn.setIconTextGap(6);
        JPopupMenu mainMenu = new JPopupMenu();
        mainMenu.add(mnuDepartment);
        mainMenu.add(mnuEmployee);
        mainBtn.addActionListener(e -> mainMenu.show(mainBtn, 0, mainBtn.getHeight()));
        leftMenu.add(mainBtn);

        topBar.add(leftMenu, BorderLayout.WEST);

        // Right side: User menu
        String displayName = "Người dùng";
        if (UserSession.getInstance().getLoggedInEmployee() != null) {
            displayName = UserSession.getInstance().getLoggedInEmployee().getFirstName() + " " +
                          UserSession.getInstance().getLoggedInEmployee().getLastName();
        }
        JButton userBtn = menuButton(displayName + " ▾");
        userBtn.setIcon(IconName.USER.icon(16));
        userBtn.setIconTextGap(6);
        JPopupMenu userMenu = new JPopupMenu();
        userMenu.add(mnuMyAssets);
        userMenu.add(mnuChangePassword);
        userMenu.addSeparator();
        userMenu.add(mnuLogout);
        userBtn.addActionListener(e -> userMenu.show(userBtn, 0, userBtn.getHeight()));
        topBar.add(userBtn, BorderLayout.EAST);

        topBar.setPreferredSize(new Dimension(1, 56));
        root.add(topBar, BorderLayout.NORTH);

        /************************ CENTER WELCOME ************************/
        // Gradient background panel
        JPanel center = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                // từ #EFF6FF (blue-50) sang #E0E7FF (indigo-100)
                Color startColor = new Color(239, 246, 255);
                Color endColor   = new Color(224, 231, 255);
                GradientPaint gp = new GradientPaint(0, 0, startColor, w, h, endColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        center.setLayout(new GridBagLayout());

        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // KPI Icon
        JLabel kpiIcon = new JLabel(IconName.CHART_LINE.icon(64));
        kpiIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(kpiIcon);

    JLabel welcome = new JLabel("Chào mừng đến với Hệ thống Quản lý Tài Sản");
        welcome.setFont(UITheme.fontBold(24));
        welcome.setForeground(UITheme.GRAY_700);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(Box.createVerticalStrut(12));
    welcomePanel.add(welcome);
    welcomePanel.add(Box.createVerticalStrut(16)); // extra space after main title

    JLabel subtitle = new JLabel("Enterprise Asset Management System");
        subtitle.setFont(UITheme.fontRegular(16));
        subtitle.setForeground(UITheme.GRAY_600);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    welcomePanel.add(subtitle);
    welcomePanel.add(Box.createVerticalStrut(20)); // space before hint line

        JLabel hint = new JLabel("Chọn chức năng từ menu để bắt đầu làm việc");
        hint.setFont(UITheme.fontRegular(13));
        hint.setForeground(UITheme.GRAY_500);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
    welcomePanel.add(hint);

        center.add(welcomePanel);
        root.add(center, BorderLayout.CENTER);

        /************************ ACTIONS ************************/
        mnuAssetCategory.addActionListener(e -> new views.device.AssetCategoryManagementView().setVisible(true));
        mnuAsset.addActionListener(e -> new views.device.AssetManagementView().setVisible(true));
        mnuAssetRequest.addActionListener(e -> new views.device.AssetRequestManagementView().setVisible(true));
        mnuAssetRequestItem.addActionListener(e -> new views.device.AssetRequestItemManagementView().setVisible(true));
        mnuVendor.addActionListener(e -> new views.device.VendorManagementView().setVisible(true));
        mnuDepartment.addActionListener(e -> new views.main.DepartmentManagementView().setVisible(true));
        mnuEmployee.addActionListener(e -> new views.main.EmployeeManagementView().setVisible(true));
        mnuChangePassword.addActionListener(e -> new ChangePasswordView().setVisible(true));
        mnuMyAssets.addActionListener(e -> new views.device.MyAssetsView().setVisible(true));
        mnuLogout.addActionListener(e -> {
            UserSession.getInstance().clearSession();
            dispose();
            new LoginView().setVisible(true);
        });
    }

    /**
     * Create a flat styled menu button for top bar popups with hover effect.
     */
    private JButton menuButton(String text){
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        b.setBackground(Color.WHITE);
        b.setFont(UITheme.fontRegular(13));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e){ b.setBackground(new Color(235, 240, 250)); }
            public void mouseExited(java.awt.event.MouseEvent e){ b.setBackground(Color.WHITE); }
        });
        return b;
    }

    /**
     * Create a styled JMenuItem with hover effect.
     */
    private JMenuItem menuItem(String text){
        JMenuItem item = new JMenuItem(text);
        item.setFont(UITheme.fontRegular(13));
        item.setOpaque(true);
        item.setBackground(Color.WHITE);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(235,240,250));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(Color.WHITE);
            }
        });
        return item;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
    }
}
