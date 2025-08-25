
package views.device;

import javax.swing.*;
import java.awt.*;

import controllers.user.UserSession;
import models.main.Employee;
import views.device.components.AssetCategoryTable;
import controllers.device.AssetCategoryController;
import services.device.AssetCategoryService;
import models.device.AssetCategory;
import java.util.List;

public class AssetCategoryManagementView extends JFrame {
    private AssetCategoryTable table;
    private AssetCategoryController assetCategoryController;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;

    public AssetCategoryManagementView() {
        setTitle("Quản lý Danh mục Tài sản");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        assetCategoryController = new AssetCategoryController(new AssetCategoryService());
        table = new AssetCategoryTable();
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
            Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
            JTextField tfName = new JTextField();
            JTextField tfDesc = new JTextField();
            Object[] message = {
                    "Tên danh mục:", tfName,
                    "Mô tả:", tfDesc
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Thêm Danh mục", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = tfName.getText().trim();
                String desc = tfDesc.getText().trim();
                // Gọi service xử lý nghiệp vụ, trả về lỗi nếu có
                try {
                    String error = assetCategoryController.getAssetCategoryService().addAssetCategoryFromInput(name, desc,
                            currentUser);
                    if (error == null) {
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi không mong muốn.", "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Action for Edit
        btnEdit.addActionListener(e -> {
            Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một danh mục để sửa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            AssetCategory cat = assetCategoryController.getAssetCategoryById(id);
            if (cat == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTextField tfName = new JTextField(cat.getCategoryName());
            JTextField tfDesc = new JTextField(cat.getDescription());
            Object[] message = {
                    "Tên danh mục:", tfName,
                    "Mô tả:", tfDesc
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Sửa Danh mục", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = tfName.getText().trim();
                String desc = tfDesc.getText().trim();
                if (!name.isEmpty()) {
                    cat.setCategoryName(name);
                    cat.setDescription(desc);
                    try {
                        assetCategoryController.updateAssetCategory(cat, currentUser);
                        loadDataToTable();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi không mong muốn.", "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Tên danh mục không được để trống!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Delete
        btnDelete.addActionListener(e -> {
            Employee currentUser = UserSession.getInstance().getLoggedInEmployee();
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một danh mục để xóa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa danh mục này?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    assetCategoryController.deleteAssetCategory(id, currentUser);
                    loadDataToTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi không mong muốn.", "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        List<AssetCategory> list = assetCategoryController.getAllAssetCategories();
        table.setAssetCategoryData(list);
    }

    private void applyRoles() {
        String role = UserSession.getInstance().getCurrentUserRole();
        // Only Admin can manage asset categories
        boolean isAdmin = "Admin".equalsIgnoreCase(role);

        btnAdd.setVisible(isAdmin);
        btnEdit.setVisible(isAdmin);
        btnDelete.setVisible(isAdmin);
    }
}
