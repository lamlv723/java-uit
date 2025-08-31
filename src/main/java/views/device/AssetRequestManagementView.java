package views.device;

import controllers.device.AssetRequestController;
import controllers.user.UserSession;
import models.device.Asset;
import models.device.AssetRequest;
import models.device.AssetRequestItem;
import models.main.Employee;
import services.device.AssetRequestService;
import services.device.AssetService;
import services.main.EmployeeService;
import utils.UIUtils;
import views.common.BaseManagementFrame;
import views.device.components.AssetRequestTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Quản lý Yêu cầu Tài sản (kế thừa BaseManagementFrame + 3 nút phụ)
public class AssetRequestManagementView extends BaseManagementFrame {
    private final AssetRequestController controller;
    private final AssetRequestTable requestTable;

    private JButton btnApprove, btnReject, btnViewDetails;
    private JPanel extraButtonsPanel;

    public AssetRequestManagementView() {
        super("Quản lý Yêu cầu Tài sản", "Quản lý Yêu cầu Tài sản", "clipboard-list", 1000, 680,
                Color.decode("#373B44"), Color.decode("#4286f4"));
        controller = new AssetRequestController(new AssetRequestService());
        requestTable = (AssetRequestTable) this.table;
        reorganizeActionBarForRightButtons();
        buildExtraButtons();
        applyRoles();
        loadData();
    }

    /**
     * Show only the creation dialog for Asset Request, without showing the
     * management frame.
     */
    public static void showCreateDialogOnly() {
        // Use a temporary controller for dialog logic
        AssetRequestController controller = new AssetRequestController(new AssetRequestService());
        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        String role = currentUser.getRole();
        JComponent employeeComponent;
        if ("Admin".equalsIgnoreCase(role)) {
            JComboBox<String> employeeComboBox = new JComboBox<>();
            EmployeeService employeeService = new EmployeeService();
            java.util.List<Employee> employees = employeeService.getAllEmployees(currentUser);
            if (employees != null) {
                for (Employee emp : employees) {
                    employeeComboBox.addItem(emp.getEmployeeId() + ": " + emp.getFullName());
                }
            }
            employeeComponent = employeeComboBox;
        } else {
            JTextField employeeField = new JTextField(currentUser.getFullName());
            employeeField.setEditable(false);
            employeeComponent = employeeField;
        }
        AssetService assetService = new AssetService();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> assetList = new JList<>(listModel);
        assetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane assetScrollPane = new JScrollPane(assetList);
        assetScrollPane.setPreferredSize(new Dimension(260, 110));
        JComboBox<RequestTypeOption> requestTypeComboBox = new JComboBox<>(new RequestTypeOption[] {
                new RequestTypeOption("borrow", "Borrow"),
                new RequestTypeOption("return", "Return") });
        Runnable updater = () -> {
            RequestTypeOption opt = (RequestTypeOption) requestTypeComboBox.getSelectedItem();
            if (opt == null)
                return;
            String type = opt.value;
            listModel.clear();
            int employeeId;
            if (employeeComponent instanceof JComboBox) {
                String se = (String) ((JComboBox<?>) employeeComponent).getSelectedItem();
                if (se == null)
                    return;
                employeeId = Integer.parseInt(se.split(":")[0]);
            } else
                employeeId = currentUser.getEmployeeId();
            java.util.List<Asset> assets = "borrow".equals(type) ? assetService.getAllAvailableAssets()
                    : assetService.getBorrowedAssetsByEmployeeId(employeeId);
            if (assets != null)
                for (Asset a : assets)
                    listModel.addElement(a.getAssetId() + ": " + a.getAssetName());
        };
        requestTypeComboBox.addActionListener(e -> updater.run());
        if (employeeComponent instanceof JComboBox)
            ((JComboBox<?>) employeeComponent).addActionListener(e -> updater.run());
        updater.run();
        JPanel panel = new AssetRequestManagementView().formPanel(
                new String[] { "Nhân viên:", "Loại yêu cầu:", "Chọn tài sản:" },
                new JComponent[] { employeeComponent, requestTypeComboBox, assetScrollPane });
        // Gợi ý chọn nhiều
        GridBagConstraints tipGbc = new GridBagConstraints();
        tipGbc.gridx = 0;
        tipGbc.gridy = 3;
        tipGbc.gridwidth = 2;
        tipGbc.insets = new Insets(4, 6, 2, 6);
        tipGbc.anchor = GridBagConstraints.WEST;
        JLabel tip = new JLabel("Giữ Ctrl (hoặc Shift) để chọn nhiều tài sản");
        tip.setFont(tip.getFont().deriveFont(Font.ITALIC, tip.getFont().getSize() - 1f));
        tip.setForeground(new Color(90, 90, 90));
        panel.add(tip, tipGbc);
        int opt = JOptionPane.showConfirmDialog(null, panel, "Tạo Yêu cầu mới", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (opt == JOptionPane.OK_OPTION) {
            int employeeId;
            if ("Admin".equalsIgnoreCase(role)) {
                String sel = (String) ((JComboBox<?>) employeeComponent).getSelectedItem();
                employeeId = Integer.parseInt(sel.split(":")[0]);
            } else
                employeeId = currentUser.getEmployeeId();
            java.util.List<String> selAssets = assetList.getSelectedValuesList();
            if (selAssets.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất một tài sản!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.util.List<Integer> assetIds = new java.util.ArrayList<>();
            for (String s : selAssets)
                assetIds.add(Integer.parseInt(s.split(":")[0]));
            RequestTypeOption selOpt = (RequestTypeOption) requestTypeComboBox.getSelectedItem();
            String error = controller.getAssetRequestService().createRequestWithItems(employeeId,
                    selOpt == null ? "borrow" : selOpt.value, assetIds);
            if (error == null) {
                JOptionPane.showMessageDialog(null, "Tạo yêu cầu thành công!");
                // Trigger dashboard refresh (stats + request table)
                Runnable r1 = (Runnable) UIManager.get("dashboard.refreshStats");
                if (r1 != null)
                    SwingUtilities.invokeLater(r1);
                Runnable r2 = (Runnable) UIManager.get("dashboard.reloadRequests");
                if (r2 != null)
                    SwingUtilities.invokeLater(r2);
            } else
                JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Show only the edit dialog for Asset Request, without showing the management
     * frame.
     */
    public static void showEditDialogOnly(int id) {
        AssetRequestController controller = new AssetRequestController(new AssetRequestService());
        AssetRequest req = controller.getAssetRequestById(id);
        if (req == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy yêu cầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!"Pending".equalsIgnoreCase(req.getStatus())) {
            JOptionPane.showMessageDialog(null, "Chỉ sửa yêu cầu 'Pending'", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextField employeeField = new JTextField(req.getEmployee().getFullName());
        employeeField.setEditable(false);
        AssetService assetService = new AssetService();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> assetList = new JList<>(listModel);
        assetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane assetScrollPane = new JScrollPane(assetList);
        assetScrollPane.setPreferredSize(new Dimension(260, 110));
        JComboBox<RequestTypeOption> typeBox = new JComboBox<>(new RequestTypeOption[] {
                new RequestTypeOption("borrow", "Borrow"),
                new RequestTypeOption("return", "Return") });
        // Chọn option hiện tại
        for (int i = 0; i < typeBox.getItemCount(); i++) {
            if (typeBox.getItemAt(i).value.equalsIgnoreCase(req.getRequestType())) {
                typeBox.setSelectedIndex(i);
                break;
            }
        }
        typeBox.setEnabled(false);
        Runnable updater = () -> {
            RequestTypeOption opt = (RequestTypeOption) typeBox.getSelectedItem();
            if (opt == null)
                return;
            String type = opt.value;
            listModel.clear();
            int empId = req.getEmployee().getEmployeeId();
            java.util.List<Asset> assets = "borrow".equals(type) ? assetService.getAllAvailableAssets()
                    : assetService.getBorrowedAssetsByEmployeeId(empId);
            if (assets != null)
                for (Asset a : assets)
                    listModel.addElement(a.getAssetId() + ": " + a.getAssetName());
        };
        updater.run();
        // Chọn sẵn tài sản đang có
        java.util.List<AssetRequestItem> currentItems = new dao.device.AssetRequestItemDAOImpl()
                .getAssetRequestItemsByRequestId(id);
        java.util.List<Integer> currentAssetIds = currentItems.stream().map(it -> it.getAsset().getAssetId())
                .collect(java.util.stream.Collectors.toList());
        SwingUtilities.invokeLater(() -> {
            ListSelectionModel m = assetList.getSelectionModel();
            m.clearSelection();
            for (int i = 0; i < listModel.size(); i++) {
                int aid = Integer.parseInt(listModel.get(i).split(":")[0]);
                if (currentAssetIds.contains(aid))
                    m.addSelectionInterval(i, i);
            }
        });
        JPanel panel = new AssetRequestManagementView().formPanel(
                new String[] { "Nhân viên:", "Loại yêu cầu:", "Chọn tài sản:" },
                new JComponent[] { employeeField, typeBox, assetScrollPane });
        GridBagConstraints tipGbc2 = new GridBagConstraints();
        tipGbc2.gridx = 0;
        tipGbc2.gridy = 3;
        tipGbc2.gridwidth = 2;
        tipGbc2.insets = new Insets(4, 6, 2, 6);
        tipGbc2.anchor = GridBagConstraints.WEST;
        JLabel tip2 = new JLabel("Giữ Ctrl (hoặc Shift) để chọn nhiều tài sản");
        tip2.setFont(tip2.getFont().deriveFont(Font.ITALIC, tip2.getFont().getSize() - 1f));
        tip2.setForeground(new Color(90, 90, 90));
        panel.add(tip2, tipGbc2);
        int opt = JOptionPane.showConfirmDialog(null, panel, "Sửa Yêu cầu", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (opt == JOptionPane.OK_OPTION) {
            java.util.List<String> selAssets = assetList.getSelectedValuesList();
            if (selAssets.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất một tài sản!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.util.List<Integer> assetIds = new java.util.ArrayList<>();
            for (String s : selAssets)
                assetIds.add(Integer.parseInt(s.split(":")[0]));
            try {
                String error = controller.updateRequestWithItems(id, assetIds,
                        UserSession.getInstance().getLoggedInEmployee());
                if (error == null) {
                    JOptionPane.showMessageDialog(null, "Cập nhật yêu cầu thành công!");
                    // Optionally trigger dashboard refresh
                    Runnable r = (Runnable) UIManager.get("dashboard.refreshStats");
                    if (r != null)
                        SwingUtilities.invokeLater(r);
                } else
                    JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                UIUtils.showErrorDialog(null, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                        "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        }
    }

    // Phân quyền hiển thị nút
    @Override
    protected void applyRoles() {
        String role = UserSession.getInstance().getCurrentUserRole();
        boolean isAdmin = "Admin".equalsIgnoreCase(role);
        boolean isManagerOrStaff = !isAdmin && ("Manager".equalsIgnoreCase(role) || "Staff".equalsIgnoreCase(role));

        // Admin: giữ nguyên 3 nút CRUD
        if (isAdmin) {
            btnAdd.setVisible(true);
            btnEdit.setVisible(true);
            btnDelete.setVisible(true);
        } else if (isManagerOrStaff) {
            // Cho phép tạo yêu cầu; Edit/Delete vẫn ẩn để tránh nhầm (service vẫn kiểm tra
            // bảo vệ)
            btnAdd.setVisible(true);
            btnEdit.setVisible(false);
            btnDelete.setVisible(false);
        } else {
            // Vai trò khác (null hoặc không xác định)
            btnAdd.setVisible(false);
            btnEdit.setVisible(false);
            btnDelete.setVisible(false);
        }
        // Nút duyệt / từ chối xử lý ở applyRoleVisibility()
        if (btnApprove != null) {
            applyRoleVisibility();
        }
    }

    private void reorganizeActionBarForRightButtons() {
        // Tách CRUD trái + nút phụ phải
        Component[] existing = actionBarPanel.getComponents();
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);
        for (Component c : existing) {
            leftPanel.add(c);
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
            }
        }
        actionBarPanel.removeAll();
        actionBarPanel.setLayout(new BorderLayout());
        actionBarPanel.add(leftPanel, BorderLayout.WEST);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0)); // padding trái
        extraButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        extraButtonsPanel.setOpaque(false);
        extraButtonsPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 8));
        actionBarPanel.add(extraButtonsPanel, BorderLayout.CENTER);
    }

    private void buildExtraButtons() {
        btnApprove = createGradientSmallButton("Duyệt", utils.UITheme.APPROVE_START, utils.UITheme.APPROVE_END);
        btnReject = createGradientSmallButton("Từ chối", utils.UITheme.REJECT_START, utils.UITheme.REJECT_END);
        btnViewDetails = createGradientSmallButton("Chi tiết", utils.UITheme.DETAIL_START, utils.UITheme.DETAIL_END);
        extraButtonsPanel.add(btnApprove);
        extraButtonsPanel.add(btnReject);
        extraButtonsPanel.add(btnViewDetails);
        wireExtraActions();
        applyRoleVisibility();
        actionBarPanel.revalidate();
        actionBarPanel.repaint();
    }

    private JButton createGradientSmallButton(String text, Color c1, Color c2) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), 0, c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }

            public void updateUI() {
                super.updateUI();
                setContentAreaFilled(false);
                setOpaque(false);
            }
        };
        b.setForeground(Color.WHITE);
        b.setFont(utils.UITheme.fontMedium(14));
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void applyRoleVisibility() {
        String role = UserSession.getInstance().getCurrentUserRole();
        boolean isStaff = "Staff".equalsIgnoreCase(role);
        btnApprove.setVisible(!isStaff);
        btnReject.setVisible(!isStaff);
    }

    @Override
    protected JTable createTable() {
        return new AssetRequestTable();
    }

    @Override
    protected void loadData() {
        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        List<AssetRequest> list = controller.getAllAssetRequests(currentUser);
        requestTable.setAssetRequestData(list);
    }

    @Override
    protected void onAdd() {
        openCreateDialog();
    }

    @Override
    protected void onEdit(int selectedRow) {
        openEditDialog(selectedRow);
    }

    @Override
    protected void onDelete(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa yêu cầu này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Employee user = UserSession.getInstance().getLoggedInEmployee();
            try {
                String error = controller.getAssetRequestService().deleteAssetRequest(id, user);
                if (error == null) {
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                        "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        }
    }

    private void wireExtraActions() {
        btnApprove.addActionListener(e -> approveSelected());
        btnReject.addActionListener(e -> rejectSelected());
        btnViewDetails.addActionListener(e -> viewDetailsSelected());
    }

    private void approveSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            warnSelect("duyệt");
            return;
        }
        Integer requestId = (Integer) table.getValueAt(row, 0);
        AssetRequest req = controller.getAssetRequestById(requestId);
        if (req == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy yêu cầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String error;
        Employee user = UserSession.getInstance().getLoggedInEmployee();
        try {
            if ("borrow".equals(req.getRequestType()))
                error = controller.getAssetRequestService().approveBorrowRequest(requestId, user);
            else
                error = controller.getAssetRequestService().approveReturnRequest(requestId, user);
            if (error == null) {
                JOptionPane.showMessageDialog(this, "Đã duyệt yêu cầu thành công!");
                reloadAndSync();
            } else
                JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                    "Lỗi Hệ Thống");
            ex.printStackTrace();
        }
    }

    private void rejectSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            warnSelect("từ chối");
            return;
        }
        Integer requestId = (Integer) table.getValueAt(row, 0);
        Employee user = UserSession.getInstance().getLoggedInEmployee();
        try {
            String error = controller.getAssetRequestService().rejectRequest(requestId, user);
            if (error == null) {
                JOptionPane.showMessageDialog(this, "Đã từ chối yêu cầu thành công!");
                reloadAndSync();
            } else
                JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                    "Lỗi Hệ Thống");
            ex.printStackTrace();
        }
    }

    private void viewDetailsSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            warnSelect("xem chi tiết");
            return;
        }
        Integer requestId = (Integer) table.getValueAt(row, 0);
        AssetRequestItemManagementView v = new AssetRequestItemManagementView(requestId);
        v.setVisible(true);
        triggerDashboardRequestReload();
    }

    private void reloadAndSync() {
        loadData();
        if (AssetRequestItemManagementView.generalInstance != null)
            AssetRequestItemManagementView.generalInstance.refreshData();
        triggerDashboardRefresh();
        triggerDashboardRequestReload();
    }

    private void triggerDashboardRequestReload() {
        Runnable r = (Runnable) UIManager.get("dashboard.reloadRequests");
        if (r != null)
            SwingUtilities.invokeLater(r);
    }


    private void openCreateDialog() {
        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        String role = currentUser.getRole();
        JComponent employeeComponent;
        if ("Admin".equalsIgnoreCase(role)) {
            JComboBox<String> employeeComboBox = new JComboBox<>();
            EmployeeService employeeService = new EmployeeService();
            List<Employee> employees = employeeService.getAllEmployees(currentUser);
            if (employees != null) {
                for (Employee emp : employees) {
                    employeeComboBox.addItem(emp.getEmployeeId() + ": " + emp.getFullName());
                }
            }
            employeeComponent = employeeComboBox;
        } else {
            JTextField employeeField = new JTextField(currentUser.getFullName());
            employeeField.setEditable(false);
            employeeComponent = employeeField;
        }
        AssetService assetService = new AssetService();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> assetList = new JList<>(listModel);
        assetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane assetScrollPane = new JScrollPane(assetList);
        assetScrollPane.setPreferredSize(new Dimension(260, 110));
        JComboBox<RequestTypeOption> requestTypeComboBox = new JComboBox<>(new RequestTypeOption[] {
                new RequestTypeOption("borrow", "Borrow"),
                new RequestTypeOption("return", "Return") });
        Runnable updater = () -> {
            RequestTypeOption opt = (RequestTypeOption) requestTypeComboBox.getSelectedItem();
            if (opt == null)
                return;
            String type = opt.value;
            listModel.clear();
            int employeeId;
            if (employeeComponent instanceof JComboBox) {
                String se = (String) ((JComboBox<?>) employeeComponent).getSelectedItem();
                if (se == null)
                    return;
                employeeId = Integer.parseInt(se.split(":")[0]);
            } else
                employeeId = currentUser.getEmployeeId();
            List<Asset> assets = "borrow".equals(type) ? assetService.getAllAvailableAssets()
                    : assetService.getBorrowedAssetsByEmployeeId(employeeId);
            if (assets != null)
                for (Asset a : assets)
                    listModel.addElement(a.getAssetId() + ": " + a.getAssetName());
        };
        requestTypeComboBox.addActionListener(e -> updater.run());
        if (employeeComponent instanceof JComboBox)
            ((JComboBox<?>) employeeComponent).addActionListener(e -> updater.run());
        updater.run();
        JPanel panel = formPanel(new String[] { "Nhân viên:", "Loại yêu cầu:", "Chọn tài sản:" },
                new JComponent[] { employeeComponent, requestTypeComboBox, assetScrollPane });
        // Gợi ý chọn nhiều
        GridBagConstraints tipGbc = new GridBagConstraints();
        tipGbc.gridx = 0;
        tipGbc.gridy = 3;
        tipGbc.gridwidth = 2;
        tipGbc.insets = new Insets(4, 6, 2, 6);
        tipGbc.anchor = GridBagConstraints.WEST;
        JLabel tip = new JLabel("Giữ Ctrl (hoặc Shift) để chọn nhiều tài sản");
        tip.setFont(tip.getFont().deriveFont(Font.ITALIC, tip.getFont().getSize() - 1f));
        tip.setForeground(new Color(90, 90, 90));
        panel.add(tip, tipGbc);
        // Always show as modal dialog, not attached to this frame
        int opt = JOptionPane.showConfirmDialog(null, panel, "Tạo Yêu cầu mới", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (opt == JOptionPane.OK_OPTION) {
            int employeeId;
            if ("Admin".equalsIgnoreCase(role)) {
                String sel = (String) ((JComboBox<?>) employeeComponent).getSelectedItem();
                employeeId = Integer.parseInt(sel.split(":")[0]);
            } else
                employeeId = currentUser.getEmployeeId();
            List<String> selAssets = assetList.getSelectedValuesList();
            if (selAssets.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất một tài sản!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Integer> assetIds = new ArrayList<>();
            for (String s : selAssets)
                assetIds.add(Integer.parseInt(s.split(":")[0]));
            RequestTypeOption selOpt = (RequestTypeOption) requestTypeComboBox.getSelectedItem();
            String error = controller.getAssetRequestService().createRequestWithItems(employeeId,
                    selOpt == null ? "borrow" : selOpt.value, assetIds);
            if (error == null) {
                JOptionPane.showMessageDialog(null, "Tạo yêu cầu thành công!");
                // Trigger dashboard refresh (stats + request table)
                Runnable r1 = (Runnable) UIManager.get("dashboard.refreshStats");
                if (r1 != null)
                    SwingUtilities.invokeLater(r1);
                Runnable r2 = (Runnable) UIManager.get("dashboard.reloadRequests");
                if (r2 != null)
                    SwingUtilities.invokeLater(r2);
            } else
                JOptionPane.showMessageDialog(null, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openEditDialog(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        AssetRequest req = controller.getAssetRequestById(id);
        if (req == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy yêu cầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!"Pending".equalsIgnoreCase(req.getStatus())) {
            JOptionPane.showMessageDialog(this, "Chỉ sửa yêu cầu 'Pending'", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextField employeeField = new JTextField(
                req.getEmployee().getFullName());
        employeeField.setEditable(false);
        AssetService assetService = new AssetService();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> assetList = new JList<>(listModel);
        assetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane assetScrollPane = new JScrollPane(assetList);
        assetScrollPane.setPreferredSize(new Dimension(260, 110));
        JComboBox<RequestTypeOption> typeBox = new JComboBox<>(new RequestTypeOption[] {
                new RequestTypeOption("borrow", "Borrow"),
                new RequestTypeOption("return", "Return") });
        // Chọn option hiện tại
        for (int i = 0; i < typeBox.getItemCount(); i++) {
            if (typeBox.getItemAt(i).value.equalsIgnoreCase(req.getRequestType())) {
                typeBox.setSelectedIndex(i);
                break;
            }
        }
        typeBox.setEnabled(false);
        Runnable updater = () -> {
            RequestTypeOption opt = (RequestTypeOption) typeBox.getSelectedItem();
            if (opt == null)
                return;
            String type = opt.value;
            listModel.clear();
            int empId = req.getEmployee().getEmployeeId();
            List<Asset> assets = "borrow".equals(type) ? assetService.getAllAvailableAssets()
                    : assetService.getBorrowedAssetsByEmployeeId(empId);
            if (assets != null)
                for (Asset a : assets)
                    listModel.addElement(a.getAssetId() + ": " + a.getAssetName());
        };
        updater.run();
        // Chọn sẵn tài sản đang có
        List<AssetRequestItem> currentItems = new dao.device.AssetRequestItemDAOImpl()
                .getAssetRequestItemsByRequestId(id);
        java.util.List<Integer> currentAssetIds = currentItems.stream().map(it -> it.getAsset().getAssetId())
                .collect(Collectors.toList());
        SwingUtilities.invokeLater(() -> {
            ListSelectionModel m = assetList.getSelectionModel();
            m.clearSelection();
            for (int i = 0; i < listModel.size(); i++) {
                int aid = Integer.parseInt(listModel.get(i).split(":")[0]);
                if (currentAssetIds.contains(aid))
                    m.addSelectionInterval(i, i);
            }
        });
        JPanel panel = formPanel(new String[] { "Nhân viên:", "Loại yêu cầu:", "Chọn tài sản:" },
                new JComponent[] { employeeField, typeBox, assetScrollPane });
        GridBagConstraints tipGbc2 = new GridBagConstraints();
        tipGbc2.gridx = 0;
        tipGbc2.gridy = 3;
        tipGbc2.gridwidth = 2;
        tipGbc2.insets = new Insets(4, 6, 2, 6);
        tipGbc2.anchor = GridBagConstraints.WEST;
        JLabel tip2 = new JLabel("Giữ Ctrl (hoặc Shift) để chọn nhiều tài sản");
        tip2.setFont(tip2.getFont().deriveFont(Font.ITALIC, tip2.getFont().getSize() - 1f));
        tip2.setForeground(new Color(90, 90, 90));
        panel.add(tip2, tipGbc2);
        int opt = JOptionPane.showConfirmDialog(this, panel, "Sửa Yêu cầu", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (opt == JOptionPane.OK_OPTION) {
            List<String> selAssets = assetList.getSelectedValuesList();
            if (selAssets.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một tài sản!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Integer> assetIds = new ArrayList<>();
            for (String s : selAssets)
                assetIds.add(Integer.parseInt(s.split(":")[0]));
            try {
                String error = controller.updateRequestWithItems(id, assetIds,
                        UserSession.getInstance().getLoggedInEmployee());
                if (error == null) {
                    JOptionPane.showMessageDialog(this, "Cập nhật yêu cầu thành công!");
                    reloadAndSync();
                } else
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                        "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        }
    }

    private JPanel formPanel(String[] labels, JComponent[] fields) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i;
            gbc.gridx = 0;
            gbc.weightx = 0;
            p.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            p.add(fields[i], gbc);
        }
        return p;
    }

    // ===== Helper class hiển thị nhãn đẹp nhưng giữ value thô =====
    private static final class RequestTypeOption {
        final String value; // "borrow" / "return"
        final String display; // "Borrow" / "Return"

        RequestTypeOption(String value, String display) {
            this.value = value;
            this.display = display;
        }

        public String toString() {
            return display;
        }
    }

    private void triggerDashboardRefresh() {
        Runnable r = (Runnable) UIManager.get("dashboard.refreshStats");
        if (r != null)
            SwingUtilities.invokeLater(r);
    }

    private void warnSelect(String action) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để " + action + "!", "Thông báo",
                JOptionPane.WARNING_MESSAGE);
    }
}