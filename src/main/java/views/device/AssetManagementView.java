package views.device;

import controllers.device.AssetController;
import models.device.Asset;
import models.device.AssetCategory;
import models.device.Vendor;
import services.device.AssetCategoryService;
import services.device.VendorService;
import views.device.components.AssetTable;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class AssetManagementView extends JFrame {
    private final AssetController assetController;
    private final AssetTable table;

    public AssetManagementView() {
        setTitle("Quản lý tài sản");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        assetController = new AssetController(new services.device.AssetService());
        table = new AssetTable();
        loadDataToTable();
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnBorrowReturn = new JButton("Mượn/Trả");

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnBorrowReturn);

        // ✅ Sự kiện nút Thêm
        btnAdd.addActionListener(e -> {
            JTextField tfName = new JTextField();
            JTextField tfSerial = new JTextField();
            JTextField tfPrice = new JTextField();
            JTextField tfAssetTag = new JTextField();

            AssetCategoryService categoryService = new AssetCategoryService();
            List<AssetCategory> categoryList = categoryService.getAllAssetCategories();
            final JComboBox<AssetCategory> categoryComboBox = new JComboBox<>(categoryList.toArray(new AssetCategory[0]));

            VendorService vendorService = new VendorService();
            List<Vendor> vendorList = vendorService.getAllVendors();
            final JComboBox<Vendor> vendorComboBox = new JComboBox<>(vendorList.toArray(new Vendor[0]));

            JDateChooser dcPurchaseDate = new JDateChooser();
            dcPurchaseDate.setDateFormatString("yyyy-MM-dd");
            JDateChooser dcWarrantyDate = new JDateChooser();
            dcWarrantyDate.setDateFormatString("yyyy-MM-dd");

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Mã tài sản:")); panel.add(tfAssetTag);
            panel.add(new JLabel("Tên tài sản:")); panel.add(tfName);
            panel.add(new JLabel("Serial number:")); panel.add(tfSerial);
            panel.add(new JLabel("Loại tài sản (Category Name):")); panel.add(categoryComboBox);
            panel.add(new JLabel("Nhà cung cấp (Vendor Name):"));  panel.add(vendorComboBox);
            panel.add(new JLabel("Ngày mua:")); panel.add(dcPurchaseDate);
            panel.add(new JLabel("Giá mua:")); panel.add(tfPrice);
            panel.add(new JLabel("Ngày hết bảo hành:")); panel.add(dcWarrantyDate);

            int result = JOptionPane.showConfirmDialog(this, panel, "Thêm tài sản mới", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String assetTag = tfAssetTag.getText().trim();
                    String name = tfName.getText().trim();
                    String serial = tfSerial.getText().trim();
                    BigDecimal price = new BigDecimal(tfPrice.getText().trim());
                    Date purchaseDate = dcPurchaseDate.getDate();
                    Date warrantyDate = dcWarrantyDate.getDate();

                    if (assetTag.isEmpty() || name.isEmpty() || serial.isEmpty() ||purchaseDate == null || warrantyDate == null) {
                        throw new Exception("Vui lòng điền đầy đủ thông tin!");
                    }

                    AssetCategory selectedCategory = (AssetCategory) categoryComboBox.getSelectedItem();
                    Vendor selectedVendor = (Vendor) vendorComboBox.getSelectedItem();
                    Asset asset = new Asset();
                    asset.setAssetTag(assetTag);
                    asset.setAssetName(name);
                    asset.setSerialNumber(serial);
                    asset.setCategory(selectedCategory);
                    asset.setVendor(selectedVendor);
                    asset.setPurchaseDate(purchaseDate);
                    asset.setPurchasePrice(price);
                    asset.setWarrantyExpiryDate(warrantyDate);
                    asset.setStatus("Available");

                    assetController.addAsset(asset);
                    loadDataToTable();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm tài sản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ✅ Sự kiện nút Sửa
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài sản để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String assetTag = (String) table.getModel().getValueAt(row, 0);
            Asset asset = assetController.getAssetByAssetTag(assetTag);
            if (asset == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextField tfName = new JTextField(asset.getAssetName());
            JTextField tfDesc = new JTextField(asset.getDescription());

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Tên tài sản:")); panel.add(tfName);
            panel.add(new JLabel("Mô tả:")); panel.add(tfDesc);

            int option = JOptionPane.showConfirmDialog(this, panel, "Sửa Tài sản", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = tfName.getText().trim();
                String desc = tfDesc.getText().trim();
                if (!name.isEmpty()) {
                    asset.setAssetName(name);
                    asset.setDescription(desc);
                    assetController.updateAsset(asset);
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Tên tài sản không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ✅ Sự kiện nút Xóa
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài sản để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String assetTag = (String) table.getModel().getValueAt(row, 0);
            Asset asset = assetController.getAssetByAssetTag(assetTag);
            if (asset == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài sản này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                assetController.deleteAsset(asset);
                loadDataToTable();
            }
        });

        // ✅ Sự kiện Mượn/Trả
        btnBorrowReturn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài sản để mượn/trả!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String assetTag = (String) table.getModel().getValueAt(row, 0);
            Asset asset = assetController.getAssetByAssetTag(assetTag);
            if (asset == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài sản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String currentStatus = asset.getStatus();
            String newStatus = "Borrowed".equalsIgnoreCase(currentStatus) ? "Available" : "Borrowed";
            asset.setStatus(newStatus);
            assetController.updateAsset(asset);
            loadDataToTable();
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        List<Asset> list = assetController.getAllAssets();
        table.setAssetData(list);
    }
}
