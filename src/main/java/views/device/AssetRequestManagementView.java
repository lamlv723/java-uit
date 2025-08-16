package views.device;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.BiFunction;
import controllers.device.AssetRequestController;
import dao.device.AssetRequestItemDAOImpl;
import services.device.AssetRequestService;
import models.device.AssetRequest;
import models.device.AssetRequestItem;
import views.device.components.AssetRequestTable;

public class AssetRequestManagementView extends JFrame {
    private AssetRequestTable table;
    private AssetRequestController assetRequestController;
    private java.util.Map<Integer, AssetRequestItemManagementView> openDetailViews = new java.util.HashMap<>();

    public AssetRequestManagementView() {
        setTitle("Quản lý Yêu cầu Tài sản (QLYCTS)");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        assetRequestController = new AssetRequestController(new AssetRequestService());
        table = new AssetRequestTable();
        loadDataToTable();
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JButton btnComplete = new JButton("Complete");
        JButton btnViewDetails = new JButton("Xem chi tiết");

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnApprove);
        panelButtons.add(btnReject);
        panelButtons.add(btnComplete);
        panelButtons.add(btnViewDetails);

        btnAdd.addActionListener(e -> {
            String userRole = "ADMIN";
            JComboBox<String> employeeComboBox = new JComboBox<>();
            services.main.EmployeeService employeeService = new services.main.EmployeeService();
            List<models.main.Employee> employees = employeeService.getAllEmployees();
            if (employees != null) {
                for (models.main.Employee emp : employees) {
                    employeeComboBox.addItem(emp.getEmployeeId() + ": " + emp.getFirstName() + " " + emp.getLastName());
                }
            }
            if (!"ADMIN".equals(userRole)) {
                employeeComboBox.setEnabled(false);
            }
            services.device.AssetService assetService = new services.device.AssetService();
            List<models.device.Asset> availableAssets = assetService.getAllAvailableAssets();
            DefaultListModel<String> listModel = new DefaultListModel<>();
            if (availableAssets != null) {
                for (models.device.Asset asset : availableAssets) {
                    listModel.addElement(asset.getAssetId() + ": " + asset.getAssetName());
                }
            }
            JList<String> assetList = new JList<>(listModel);
            assetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane assetScrollPane = new JScrollPane(assetList);
            assetScrollPane.setPreferredSize(new Dimension(250, 100));
            JComboBox<String> typeComboBox = new JComboBox<>(new String[] { "borrow", "return" });
            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Nhân viên:"));
            panel.add(employeeComboBox);
            panel.add(new JLabel("Loại yêu cầu:"));
            panel.add(typeComboBox);
            panel.add(new JLabel("Chọn tài sản:"));
            panel.add(assetScrollPane);
            int option = JOptionPane.showConfirmDialog(this, panel, "Tạo Yêu cầu mới", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String selectedEmployee = (String) employeeComboBox.getSelectedItem();
                int employeeId = Integer.parseInt(selectedEmployee.split(":")[0]);
                String requestType = (String) typeComboBox.getSelectedItem();
                List<String> selectedAssets = assetList.getSelectedValuesList();
                if (selectedAssets.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                java.util.List<Integer> assetIds = new java.util.ArrayList<>();
                for (String assetStr : selectedAssets) {
                    assetIds.add(Integer.parseInt(assetStr.split(":")[0]));
                }
                String error = assetRequestController.getAssetRequestService().createRequestWithItems(employeeId, requestType, assetIds, "ADMIN");
                if (error == null) {
                    loadDataToTable();
                    // --- THÊM LOGIC ĐỒNG BỘ CHO CỬA SỔ "TỔNG" ---
                    if (AssetRequestItemManagementView.generalInstance != null) {
                        AssetRequestItemManagementView.generalInstance.refreshData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer requestId = (Integer) table.getValueAt(row, 0);
            AssetRequest reqToEdit = assetRequestController.getAssetRequestById(requestId);

            if (reqToEdit == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy yêu cầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Chỉ cho phép sửa yêu cầu đang ở trạng thái "Pending"
            if (!"Pending".equals(reqToEdit.getStatus())) {
                 JOptionPane.showMessageDialog(this, "Chỉ có thể sửa các yêu cầu đang ở trạng thái 'Pending'.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // --- Bắt đầu xây dựng giao diện giống hệt nút "Thêm" ---
            
            // 1. Employee ComboBox và chọn sẵn nhân viên cũ
            JComboBox<String> employeeComboBox = new JComboBox<>();
            services.main.EmployeeService employeeService = new services.main.EmployeeService();
            List<models.main.Employee> employees = employeeService.getAllEmployees();
            String currentEmployeeStr = "";
            for (models.main.Employee emp : employees) {
                String itemStr = emp.getEmployeeId() + ": " + emp.getFirstName() + " " + emp.getLastName();
                employeeComboBox.addItem(itemStr);
                if (emp.getEmployeeId().equals(reqToEdit.getEmployee().getEmployeeId())) {
                    currentEmployeeStr = itemStr;
                }
            }
            employeeComboBox.setSelectedItem(currentEmployeeStr);

            // 2. Asset List và chọn sẵn các tài sản cũ
            services.device.AssetService assetService = new services.device.AssetService();
            List<models.device.Asset> availableAssets = assetService.getAllAvailableAssets();
            DefaultListModel<String> listModel = new DefaultListModel<>();
            
            AssetRequestItemDAOImpl itemDAO = new AssetRequestItemDAOImpl();
            List<AssetRequestItem> currentItems = itemDAO.getItemsByRequestId(requestId);
            java.util.Set<Integer> currentAssetIds = new java.util.HashSet<>();
            for(AssetRequestItem item : currentItems) {
                currentAssetIds.add(item.getAsset().getAssetId());
                // Thêm cả những tài sản đã được mượn (bởi chính request này) vào danh sách
                listModel.addElement(item.getAsset().getAssetId() + ": " + item.getAsset().getAssetName());
            }

            for (models.device.Asset asset : availableAssets) {
                // Chỉ thêm các tài sản available mà chưa có trong danh sách
                if (!currentAssetIds.contains(asset.getAssetId())) {
                    listModel.addElement(asset.getAssetId() + ": " + asset.getAssetName());
                }
            }
            
            JList<String> assetList = new JList<>(listModel);
            assetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            
            // Tô màu chọn sẵn các tài sản cũ
            List<Integer> indicesToSelect = new java.util.ArrayList<>();
            for (int i = 0; i < listModel.size(); i++) {
                int assetId = Integer.parseInt(listModel.getElementAt(i).split(":")[0]);
                if(currentAssetIds.contains(assetId)){
                    indicesToSelect.add(i);
                }
            }
            int[] selectedIndices = indicesToSelect.stream().mapToInt(i -> i).toArray();
            assetList.setSelectedIndices(selectedIndices);
            
            JScrollPane assetScrollPane = new JScrollPane(assetList);
            assetScrollPane.setPreferredSize(new Dimension(250, 100));

            // 3. Request Type ComboBox và chọn sẵn loại yêu cầu cũ
            JComboBox<String> typeComboBox = new JComboBox<>(new String[] { "borrow", "return" });
            typeComboBox.setSelectedItem(reqToEdit.getRequestType());

            // --- Hiển thị form ---
            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Nhân viên:"));
            panel.add(employeeComboBox);
            panel.add(new JLabel("Loại yêu cầu:"));
            panel.add(typeComboBox);
            panel.add(new JLabel("Chọn tài sản:"));
            panel.add(assetScrollPane);

            int option = JOptionPane.showConfirmDialog(this, panel, "Sửa Yêu cầu ID: " + requestId, JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                // Lấy thông tin đã cập nhật từ form
                String selectedEmployee = (String) employeeComboBox.getSelectedItem();
                int newEmployeeId = Integer.parseInt(selectedEmployee.split(":")[0]);
                String newRequestType = (String) typeComboBox.getSelectedItem();
                List<String> newSelectedAssets = assetList.getSelectedValuesList();

                if (newSelectedAssets.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Yêu cầu phải có ít nhất một tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.util.List<Integer> newAssetIds = new java.util.ArrayList<>();
                for (String assetStr : newSelectedAssets) {
                    newAssetIds.add(Integer.parseInt(assetStr.split(":")[0]));
                }

                // Gọi service để cập nhật
                String error = assetRequestController.getAssetRequestService().updateRequestWithItems(requestId, newEmployeeId, newRequestType, newAssetIds);
                if (error == null) {
                    loadDataToTable();
                    // Đồng bộ hóa các cửa sổ khác nếu cần
                    if(openDetailViews.containsKey(requestId)){
                        openDetailViews.get(requestId).refreshData();
                    }
                    if(AssetRequestItemManagementView.generalInstance != null){
                        AssetRequestItemManagementView.generalInstance.refreshData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa yêu cầu này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                assetRequestController.deleteAssetRequest(id, "ADMIN");
                loadDataToTable();
            }
        });
        
        btnApprove.addActionListener(e -> handleRequestAction( "duyệt", (requestId, approverId) -> assetRequestController.getAssetRequestService().approveRequest(requestId, approverId) ));
        btnReject.addActionListener(e -> handleRequestAction( "từ chối", (requestId, approverId) -> assetRequestController.getAssetRequestService().rejectRequest(requestId, approverId) ));
        btnComplete.addActionListener(e -> handleRequestAction( "hoàn tất", (requestId) -> assetRequestController.getAssetRequestService().completeBorrowRequest(requestId) ));

        btnViewDetails.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để xem chi tiết!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer requestId = (Integer) table.getValueAt(row, 0);
            if (openDetailViews.containsKey(requestId)) {
                openDetailViews.get(requestId).toFront();
                return;
            }
            AssetRequestItemManagementView detailsView = new AssetRequestItemManagementView(requestId);
            openDetailViews.put(requestId, detailsView);
            detailsView.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    openDetailViews.remove(requestId);
                }
            });
            detailsView.setVisible(true);
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void handleRequestAction(String actionName, BiFunction<Integer, Integer, String> serviceMethod) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để " + actionName + "!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer id = (Integer) table.getValueAt(row, 0);
        String error = serviceMethod.apply(id, 1);
        if (error == null) {
            JOptionPane.showMessageDialog(this, "Đã " + actionName + " yêu cầu thành công!");
            loadDataToTable();
            // --- SỬA LỖI: Đồng bộ hóa cả cửa sổ chi tiết cụ thể và cửa sổ "tổng" ---
            if (openDetailViews.containsKey(id)) {
                openDetailViews.get(id).refreshData();
            }
            if (AssetRequestItemManagementView.generalInstance != null) {
                AssetRequestItemManagementView.generalInstance.refreshData();
            }
        } else {
            JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRequestAction(String actionName, Function<Integer, String> serviceMethod) {
        handleRequestAction(actionName, (requestId, approverId) -> serviceMethod.apply(requestId));
    }

    private void loadDataToTable() {
        List<AssetRequest> list = assetRequestController.getAllAssetRequests();
        table.setAssetRequestData(list);
    }
}