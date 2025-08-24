
package views.device;

import controllers.device.AssetController;
import controllers.user.UserSession;
import models.device.Asset;
import models.main.Employee;
import utils.UIUtils;
import views.device.components.AssetTable;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class AssetManagementView extends JFrame {
    private final AssetController assetController;
    private final AssetTable table;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;

    public AssetManagementView() {
        setTitle("Quản lý Tài sản");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        assetController = new AssetController(new services.device.AssetService());
        table = new AssetTable();
        loadDataToTable();
        JScrollPane scrollPane = new JScrollPane(table);

        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        applyRoles();

        // Action for Add
        btnAdd.addActionListener(e -> {
            JTextField tfName = new JTextField();
            JTextField tfDesc = new JTextField();
            Object[] message = {
                    "Tên tài sản:", tfName,
                    "Mô tả:", tfDesc
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Thêm Tài sản", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = tfName.getText().trim();
                String desc = tfDesc.getText().trim();

                Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
                // Gọi service xử lý nghiệp vụ, trả về lỗi nếu có
                try {
                    String error = assetController.getAssetService().addAssetFromInput(name, desc, currentUser);
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

        // Action for Edit
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài sản để sửa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            Asset asset = assetController.getAssetById(id);
            if (asset == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTextField tfName = new JTextField(asset.getName());
            JTextField tfDesc = new JTextField(asset.getDescription());
            Object[] message = {
                    "Tên tài sản:", tfName,
                    "Mô tả:", tfDesc
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Sửa Tài sản", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = tfName.getText().trim();
                String desc = tfDesc.getText().trim();
                if (!name.isEmpty()) {
                    asset.setName(name);
                    asset.setDescription(desc);
                    Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
                    try {
                        assetController.updateAsset(asset, currentUser);
                        loadDataToTable();
                    } catch (Exception ex) {
                        String detailedMessage = "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage();
                        UIUtils.showErrorDialog(this, detailedMessage, "Lỗi Hệ Thống");
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Tên tài sản không được để trống!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Delete
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài sản để xóa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            Asset asset = assetController.getAssetById(id);
            if (asset == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài sản này?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
                try {
                    assetController.deleteAsset(asset, currentUser);
                    loadDataToTable();
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
        List<Asset> list = assetController.getAllAssets();
        table.setAssetData(list);
    }

    private void applyRoles() {
        String role = UserSession.getInstance().getCurrentUserRole();
        // Only Admin can manage assets
        boolean isAdmin = "Admin".equalsIgnoreCase(role);

        btnAdd.setVisible(isAdmin);
        btnEdit.setVisible(isAdmin);
        btnDelete.setVisible(isAdmin);
    }
}
