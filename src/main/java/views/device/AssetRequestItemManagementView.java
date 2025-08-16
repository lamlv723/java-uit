package views.device;

import controllers.device.AssetRequestItemController;
import services.device.AssetRequestItemService;
import models.device.AssetRequestItem;
import views.device.components.AssetRequestItemTable;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AssetRequestItemManagementView extends JFrame {
    private final AssetRequestItemController assetRequestItemController;
    private final AssetRequestItemTable table;
    private Integer requestId;

    // Biến static để giữ tham chiếu đến cửa sổ "tổng"
    public static AssetRequestItemManagementView generalInstance = null;

    public AssetRequestItemManagementView() {
        setTitle("Quản lý Chi tiết Yêu cầu Tài sản (QLCTYCTS)");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        assetRequestItemController = new AssetRequestItemController(new AssetRequestItemService());
        table = new AssetRequestItemTable();
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        btnAdd.addActionListener(e -> {
            JTextField tfAssetId = new JTextField();
            JTextField tfQuantity = new JTextField();
            Object[] message = { "ID Tài sản:", tfAssetId, "Số lượng:", tfQuantity };
            int option = JOptionPane.showConfirmDialog(this, message, "Thêm Chi tiết YC Tài sản", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String assetIdStr = tfAssetId.getText().trim();
                String quantityStr = tfQuantity.getText().trim();
                String error = assetRequestItemController.getAssetRequestItemService().addAssetRequestItemFromInput(assetIdStr, quantityStr, "ADMIN");
                if (error == null) {
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            AssetRequestItem item = assetRequestItemController.getAssetRequestItemById(id);
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTextField tfAssetId = new JTextField(String.valueOf(item.getAssetId()));
            JTextField tfQuantity = new JTextField(String.valueOf(item.getQuantity()));
            Object[] message = { "ID Tài sản:", tfAssetId, "Số lượng:", tfQuantity };
            int option = JOptionPane.showConfirmDialog(this, message, "Sửa Chi tiết YC Tài sản", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int assetId = Integer.parseInt(tfAssetId.getText().trim());
                    int quantity = Integer.parseInt(tfQuantity.getText().trim());
                    item.setAssetId(assetId);
                    item.setQuantity(quantity);
                    assetRequestItemController.updateAssetRequestItem(item, "ADMIN");
                    loadDataToTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa chi tiết này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                assetRequestItemController.deleteAssetRequestItem(id, "ADMIN");
                loadDataToTable();
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                if (requestId == null) {
                    generalInstance = AssetRequestItemManagementView.this;
                }
            }
            @Override
            public void windowClosed(WindowEvent e) {
                if (generalInstance == AssetRequestItemManagementView.this) {
                    generalInstance = null;
                }
            }
        });
        
        loadDataToTable();
    }

    public AssetRequestItemManagementView(Integer requestId) {
        this();
        this.requestId = requestId;
        setTitle("Chi tiết Yêu cầu ID: " + requestId);
        loadDataToTable();
    }

    private void loadDataToTable() {
        List<AssetRequestItem> list;
        if (this.requestId != null) {
            list = assetRequestItemController.getAssetRequestItemsByRequestId(this.requestId);
        } else {
            list = assetRequestItemController.getAllAssetRequestItems();
        }
        table.setAssetRequestItemData(list);
    }

    public void refreshData() {
        loadDataToTable();
    }
}