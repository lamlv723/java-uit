
package views.device;

import javax.swing.*;
import java.awt.*;
import views.device.components.AssetCategoryTable;
import controllers.device.AssetCategoryController;
import services.device.AssetCategoryService;
import models.device.AssetCategory;
import java.util.List;

public class AssetCategoryManagementView extends JFrame {
    private AssetCategoryTable table;
    private AssetCategoryController assetCategoryController;

    public AssetCategoryManagementView() {
        setTitle("Quản lý Danh mục Tài sản");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        assetCategoryController = new AssetCategoryController(new AssetCategoryService());
        table = new AssetCategoryTable();
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
                if (!name.isEmpty()) {
                    AssetCategory cat = new AssetCategory();
                    cat.setCategoryName(name);
                    cat.setDescription(desc);
                    assetCategoryController.addAssetCategory(cat, "ADMIN"); // TODO: lấy role thực tế nếu có
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Tên danh mục không được để trống!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Edit
        btnEdit.addActionListener(e -> {
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
                    assetCategoryController.updateAssetCategory(cat, "ADMIN"); // TODO: lấy role thực tế nếu có
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Tên danh mục không được để trống!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Delete
        btnDelete.addActionListener(e -> {
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
                assetCategoryController.deleteAssetCategory(id, "ADMIN"); // TODO: lấy role thực tế nếu có
                loadDataToTable();
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        List<AssetCategory> list = assetCategoryController.getAllAssetCategories();
        table.setAssetCategoryData(list);
    }
}
