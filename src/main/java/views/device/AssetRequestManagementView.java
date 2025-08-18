package views.device;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import controllers.device.AssetRequestController;
import controllers.user.UserSession;
import models.device.Asset;
import models.main.Employee;
import services.device.AssetRequestService;
import models.device.AssetRequest;
import services.device.AssetService;
import services.main.EmployeeService;
import views.device.components.AssetRequestTable;

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

        String currentUserRole = UserSession.getInstance().getCurrentUserRole();
        if (!"Admin".equals(currentUserRole)) {
            // User chỉ được Thêm, Xem chi tiết, và Hoàn tất yêu cầu của chính mình
            btnDelete.setVisible(false);
            btnApprove.setVisible(false);
            btnReject.setVisible(false);
        }

        btnAdd.addActionListener(e -> {
            // Panel chính cho dialog
            JComponent employeeComponent;
            if ("Admin".equals(currentUserRole)) {
                JComboBox<String> employeeComboBox = new JComboBox<>();
                EmployeeService employeeService = new services.main.EmployeeService();
                List<Employee> employees = employeeService.getAllEmployees();
                if (employees != null) {
                    for (Employee emp : employees) {
                        employeeComboBox
                                .addItem(emp.getEmployeeId() + ": " + emp.getFirstName() + " " + emp.getLastName());
                    }
                }
                employeeComponent = employeeComboBox;
            } else {
                Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
                JTextField employeeField = new JTextField(currentUser.getFirstName() + " " + currentUser.getLastName());
                employeeField.setEditable(false);
                employeeComponent = employeeField;
            }

            AssetService assetService = new AssetService();
            List<Asset> availableAssets = assetService.getAllAvailableAssets();
            DefaultListModel<String> listModel = new DefaultListModel<>();
            if (availableAssets != null) {
                for (Asset asset : availableAssets) {
                    listModel.addElement(asset.getAssetId() + ": " + asset.getAssetName());
                }
            }
            JList<String> assetList = new JList<>(listModel);
            assetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane assetScrollPane = new JScrollPane(assetList);
            assetScrollPane.setPreferredSize(new Dimension(250, 100));

            JComboBox<String> typeComboBox = new JComboBox<>(new String[] { "borrow", "return" });

            Runnable updateAssetList = () -> {
                String selectedType = (String) typeComboBox.getSelectedItem();
                listModel.clear();

                // int employeeId;
                // if ("Admin".equals(currentUserRole)) {
                //     String selectedEmployee = (String) employeeComponent.getSelectedItem();
                //     if (selectedEmployee == null)
                //         return;
                //     employeeId = Integer.parseInt(selectedEmployee.split(":")[0]);
                // } else {
                //     employeeId = UserSession.getInstance().getLoggedInEmployee().getEmployeeId();
                // }

                List<Asset> assetsToShow;
                if ("borrow".equals(selectedType)) {
                    assetsToShow = assetService.getAllAvailableAssets();
                } else { // "return"
                    assetsToShow = assetService.getBorrowedAssetsByEmployee(UserSession.getInstance().getLoggedInEmployee().getEmployeeId());
                }

                if (assetsToShow != null) {
                    for (Asset asset : assetsToShow) {
                        listModel.addElement(asset.getAssetId() + ": " + asset.getAssetName());
                    }
                }
            };

            typeComboBox.addActionListener(event -> updateAssetList.run());
            // if ("Admin".equals(currentUserRole)) {
            //     employeeComponent.addActionListener(event -> updateAssetList.run());
            // }

            updateAssetList.run();

            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Nhân viên:"));
            panel.add(employeeComponent);
            panel.add(new JLabel("Loại yêu cầu:"));
            panel.add(typeComboBox);
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
                    employeeId = UserSession.getInstance().getLoggedInEmployee().getEmployeeId();
                }

                String requestType = (String) typeComboBox.getSelectedItem();
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
                        requestType, assetIds, currentUserRole);

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
            JTextField tfTitle = new JTextField(req.getRequestType());
            JTextField tfDesc = new JTextField(req.getStatus());
            Object[] message = {
                    "Tiêu đề yêu cầu:", tfTitle,
                    "Mô tả:", tfDesc
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Sửa Yêu cầu", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String title = tfTitle.getText().trim();
                String desc = tfDesc.getText().trim();
                if (!title.isEmpty()) {
                    req.setRequestType(title); // Dùng requestType làm tiêu đề
                    req.setStatus(desc); // Dùng status làm mô tả
                    assetRequestController.updateAssetRequest(req, "ADMIN"); // TODO: lấy role thực tế nếu có
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Tiêu đề không được để trống!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
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
                assetRequestController.deleteAssetRequest(id, "ADMIN"); // TODO: lấy role thực tế nếu có
                loadDataToTable();
            }
        });

        btnApprove.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để duyệt!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer requestId = (Integer) table.getValueAt(row, 0);
            int approverId = UserSession.getInstance().getLoggedInEmployee().getEmployeeId();

            AssetRequest selectedRequest = assetRequestController.getAssetRequestById(requestId);
            String error = assetRequestController.getAssetRequestService().approveRequest(requestId, approverId);

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
        });


        btnReject.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để từ chối!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer requestId = (Integer) table.getValueAt(row, 0);
            int approverId = UserSession.getInstance().getLoggedInEmployee().getEmployeeId();

            String error = assetRequestController.getAssetRequestService().rejectRequest(requestId, approverId);

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
        });

        btnComplete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một yêu cầu để hoàn tất!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);

            // Logic kiểm tra quyền cho nút Complete
            AssetRequest selectedReq = assetRequestController.getAssetRequestById(id);
            int currentUserId = UserSession.getInstance().getLoggedInEmployee().getEmployeeId();
            if (!"Admin".equals(currentUserRole) && selectedReq.getEmployee().getEmployeeId() != currentUserId) {
                JOptionPane.showMessageDialog(this, "Bạn chỉ có thể hoàn tất yêu cầu do chính bạn tạo.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String error;
            if ("borrow".equals(selectedReq.getRequestType())) {
                error = assetRequestController.getAssetRequestService().completeBorrowRequest(id);
            } else {
                error = assetRequestController.getAssetRequestService().completeReturnRequest(id);
            }
            if (error == null) {
                JOptionPane.showMessageDialog(this, "Đã hoàn tất yêu cầu thành công!");
                loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        List<AssetRequest> list = assetRequestController.getAllAssetRequests();
        table.setAssetRequestData(list);
    }
}
