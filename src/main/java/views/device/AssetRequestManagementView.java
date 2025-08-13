package views.device;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controllers.device.AssetRequestController;
import services.device.AssetRequestService;
import models.device.AssetRequest;
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
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        // Action for Add
        btnAdd.addActionListener(e -> {
            JTextField tfTitle = new JTextField();
            JTextField tfDesc = new JTextField();
            Object[] message = {
                    "Tiêu đề yêu cầu:", tfTitle,
                    "Mô tả:", tfDesc
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Thêm Yêu cầu", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String title = tfTitle.getText().trim();
                String desc = tfDesc.getText().trim();
                // Gọi service xử lý nghiệp vụ, trả về lỗi nếu có
                String error = assetRequestController.getAssetRequestService().addAssetRequestFromInput(title, desc,
                        "ADMIN");
                if (error == null) {
                    loadDataToTable();
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

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        List<AssetRequest> list = assetRequestController.getAllAssetRequests();
        table.setAssetRequestData(list);
    }
}
