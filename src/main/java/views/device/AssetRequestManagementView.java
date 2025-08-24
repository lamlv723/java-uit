package views.device;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import controllers.device.AssetRequestController;
import controllers.user.UserSession;
import dao.device.AssetRequestItemDAOImpl;
import models.device.Asset;
import models.main.Employee;
import services.device.AssetRequestService;
import models.device.AssetRequest;
import models.device.AssetRequestItem;
import services.device.AssetService;
import services.main.EmployeeService;
import views.device.components.AssetRequestTable;
import utils.UIUtils;

public class AssetRequestManagementView extends JFrame {
    private AssetRequestTable table;
    private AssetRequestController assetRequestController;

    public AssetRequestManagementView() {
        setTitle("Quản lý Yêu cầu Tài sản");
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
        JButton btnViewDetails = new JButton("Xem chi tiết");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnApprove);
        panelButtons.add(btnReject);
        panelButtons.add(btnViewDetails);

        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        String currentUserRole = currentUser.getRole();

        // Hiển thị các nút duyệt cho Admin và Manager
        if ("Staff".equalsIgnoreCase(currentUserRole)) {
            btnApprove.setVisible(false);
            btnReject.setVisible(false);
        }

        btnAdd.addActionListener(e -> {
            // Panel chính cho dialog
            JComponent employeeComponent;
            if ("Admin".equals(currentUserRole)) {
                JComboBox<String> employeeComboBox = new JComboBox<>();
                EmployeeService employeeService = new EmployeeService();
                List<Employee> employees = employeeService.getAllEmployees(currentUser);
                if (employees != null) {
                    for (Employee emp : employees) {
                        employeeComboBox
                                .addItem(emp.getEmployeeId() + ": " + emp.getFirstName() + " " + emp.getLastName());
                    }
                }
                employeeComponent = employeeComboBox;
            } else {
                JTextField employeeField = new JTextField(currentUser.getFirstName() + " " + currentUser.getLastName());
                employeeField.setEditable(false);
                employeeComponent = employeeField;
            }

            AssetService assetService = new AssetService();
            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> assetList = new JList<>(listModel);
            assetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane assetScrollPane = new JScrollPane(assetList);
            assetScrollPane.setPreferredSize(new Dimension(250, 100));

            JComboBox<String> requestTypeComboBox = new JComboBox<>(new String[] { "borrow", "return" });
            
            Runnable updateAssetList = () -> {
                String selectedType = (String) requestTypeComboBox.getSelectedItem();
                listModel.clear();

                int employeeIdToFilter;

                // Xác định employeeId dựa trên vai trò và lựa chọn
                if (employeeComponent instanceof JComboBox) { // Admin
                    String selectedEmployee = (String) ((JComboBox<?>) employeeComponent).getSelectedItem();
                    if (selectedEmployee == null) return; // Nếu chưa chọn ai thì không làm gì
                    employeeIdToFilter = Integer.parseInt(selectedEmployee.split(":")[0]);
                } else { // Nhân viên thường
                    employeeIdToFilter = currentUser.getEmployeeId();
                }

                List<Asset> assetsToShow;
                if ("borrow".equals(selectedType)) {
                    // Yêu cầu mượn: luôn hiển thị tài sản có sẵn
                    assetsToShow = assetService.getAllAvailableAssets();
                } else { // "return"
                    // Yêu cầu trả: hiển thị tài sản mà nhân viên được chọn đang mượn
                    assetsToShow = assetService.getBorrowedAssetsByEmployeeId(employeeIdToFilter);
                }

                if (assetsToShow != null) {
                    for (Asset asset : assetsToShow) {
                        listModel.addElement(asset.getAssetId() + ": " + asset.getAssetName());
                    }
                }
            };

            requestTypeComboBox.addActionListener(event -> updateAssetList.run());
            if (employeeComponent instanceof JComboBox) {
                ((JComboBox<?>) employeeComponent).addActionListener(event -> updateAssetList.run());
            }

            updateAssetList.run();

            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Nhân viên:"));
            panel.add(employeeComponent);
            panel.add(new JLabel("Loại yêu cầu:"));
            panel.add(requestTypeComboBox);
            panel.add(new JLabel("Chọn tài sản:"));
            panel.add(assetScrollPane);
            // Hiển thị dialog
            int option = JOptionPane.showConfirmDialog(this, panel, "Tạo Yêu cầu mới", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            // Xử lý kết quả
            if (option == JOptionPane.OK_OPTION) {
                int employeeId;
                if ("Admin".equals(currentUserRole)) {
                    String selectedEmployee = (String) ((JComboBox<?>) employeeComponent).getSelectedItem();
                    employeeId = Integer.parseInt(selectedEmployee.split(":")[0]);
                } else {
                    employeeId = currentUser.getEmployeeId();
                }

                String requestType = (String) requestTypeComboBox.getSelectedItem();
                List<String> selectedAssets = assetList.getSelectedValuesList();

                if (selectedAssets.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một tài sản!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<Integer> assetIds = new ArrayList<>();
                for (String assetStr : selectedAssets) {
                    assetIds.add(Integer.parseInt(assetStr.split(":")[0]));
                }

                // Gọi service để tạo request
                String error = assetRequestController.getAssetRequestService().createRequestWithItems(employeeId,
                        requestType, assetIds);

                if (error == null) {
                    JOptionPane.showMessageDialog(this, "Tạo yêu cầu thành công!");
                    loadDataToTable(); // Tải lại bảng
                    if (AssetRequestItemManagementView.generalInstance != null) {
                        AssetRequestItemManagementView.generalInstance.refreshData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Delete
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để xóa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa yêu cầu này?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String error = assetRequestController.getAssetRequestService().deleteAssetRequest(id, currentUser);
                    if (error == null) {
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    String detailedMessage = "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage();
                    UIUtils.showErrorDialog(this, detailedMessage, "Lỗi Hệ Thống");
                    ex.printStackTrace();
                }
            }
        });

        btnApprove.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để duyệt!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer requestId = (Integer) table.getValueAt(row, 0);

            String error;
            AssetRequest selectedRequest = assetRequestController.getAssetRequestById(requestId);
            try {
                if ("borrow".equals(selectedRequest.getRequestType())) {
                    error = assetRequestController.getAssetRequestService().approveBorrowRequest(requestId, currentUser);
                } else {
                    error = assetRequestController.getAssetRequestService().approveReturnRequest(requestId, currentUser);
                }

                if (error == null) {
                    JOptionPane.showMessageDialog(this, "Đã duyệt yêu cầu thành công!");
                    loadDataToTable();
                    // Đồng bộ hóa các cửa sổ khác nếu cần
                    if (AssetRequestItemManagementView.generalInstance != null) {
                        AssetRequestItemManagementView.generalInstance.refreshData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                String detailedMessage = "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage();
                UIUtils.showErrorDialog(this, detailedMessage, "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        });

        btnReject.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để từ chối!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer requestId = (Integer) table.getValueAt(row, 0);

            try {
                String error = assetRequestController.getAssetRequestService().rejectRequest(requestId, currentUser);

                if (error == null) {
                    JOptionPane.showMessageDialog(this, "Đã từ chối yêu cầu thành công!");
                    loadDataToTable();
                    // Đồng bộ hóa các cửa sổ khác nếu cần
                    if (AssetRequestItemManagementView.generalInstance != null) {
                        AssetRequestItemManagementView.generalInstance.refreshData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                String detailedMessage = "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage();
                UIUtils.showErrorDialog(this, detailedMessage, "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        });

        btnViewDetails.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để xem chi tiết.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer requestId = (Integer) table.getValueAt(selectedRow, 0);
            AssetRequestItemManagementView detailsView = new AssetRequestItemManagementView(requestId);
            detailsView.setVisible(true);
        });

        // Action for Edit
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để sửa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            AssetRequest req = assetRequestController.getAssetRequestById(id);

            if (req == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy yêu cầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!"Pending".equalsIgnoreCase(req.getStatus())) {
                JOptionPane.showMessageDialog(this, "Chỉ có thể sửa các yêu cầu đang ở trạng thái 'Pending'.", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JTextField employeeField = new JTextField(req.getEmployee().getFirstName() + " " + req.getEmployee().getLastName());
            employeeField.setEditable(false);

            AssetService assetService = new AssetService();
            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> assetList = new JList<>(listModel);
            assetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane assetScrollPane = new JScrollPane(assetList);
            assetScrollPane.setPreferredSize(new Dimension(250, 100));

            JComboBox<String> requestTypeComboBox = new JComboBox<>(new String[] { "borrow", "return" });
            requestTypeComboBox.setSelectedItem(req.getRequestType());
            requestTypeComboBox.setEnabled(false); // Không cho sửa loại yêu cầu

            Runnable updateAssetList = () -> {
                String selectedType = (String) requestTypeComboBox.getSelectedItem();
                listModel.clear();
                int employeeIdToFilter = req.getEmployee().getEmployeeId();
                List<Asset> assetsToShow;

                if ("borrow".equals(selectedType)) {
                    assetsToShow = assetService.getAllAvailableAssets();
                } else {
                    assetsToShow = assetService.getBorrowedAssetsByEmployeeId(employeeIdToFilter);
                }

                if (assetsToShow != null) {
                    for (Asset asset : assetsToShow) {
                        listModel.addElement(asset.getAssetId() + ": " + asset.getAssetName());
                    }
                }
            };

            updateAssetList.run(); // Chạy lần đầu để tải danh sách

            // Lấy và chọn sẵn các tài sản đã có trong yêu cầu
            List<AssetRequestItem> currentItems = new AssetRequestItemDAOImpl().getAssetRequestItemsByRequestId(id);
            List<Integer> currentAssetIds = currentItems.stream()
                                                        .map(item -> item.getAsset().getAssetId())
                                                        .collect(Collectors.toList());
            
            SwingUtilities.invokeLater(() -> {
                ListSelectionModel selectionModel = assetList.getSelectionModel();
                selectionModel.clearSelection();
                for (int i = 0; i < listModel.size(); i++) {
                    int assetId = Integer.parseInt(listModel.getElementAt(i).split(":")[0]);
                    if (currentAssetIds.contains(assetId)) {
                        selectionModel.addSelectionInterval(i, i);
                    }
                }
            });

            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Nhân viên:"));
            panel.add(employeeField);
            panel.add(new JLabel("Loại yêu cầu:"));
            panel.add(requestTypeComboBox);
            panel.add(new JLabel("Chọn tài sản:"));
            panel.add(assetScrollPane);

            int option = JOptionPane.showConfirmDialog(this, panel, "Sửa Yêu cầu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                List<String> selectedAssets = assetList.getSelectedValuesList();

                if (selectedAssets.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<Integer> assetIds = new ArrayList<>();
                for (String assetStr : selectedAssets) {
                    assetIds.add(Integer.parseInt(assetStr.split(":")[0]));
                }

                try {
                    // Gọi phương thức UPDATE
                    String error = assetRequestController.updateRequestWithItems(id, assetIds, currentUser);

                    if (error == null) {
                        JOptionPane.showMessageDialog(this, "Cập nhật yêu cầu thành công!");
                        loadDataToTable();
                        if (AssetRequestItemManagementView.generalInstance != null) {
                            AssetRequestItemManagementView.generalInstance.refreshData();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    String detailedMessage = "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage();
                    UIUtils.showErrorDialog(this, detailedMessage, "Lỗi Hệ Thống");
                    ex.printStackTrace();
                }
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
        List<AssetRequest> list = assetRequestController.getAllAssetRequests(currentUser);
        table.setAssetRequestData(list);
    }

    
}