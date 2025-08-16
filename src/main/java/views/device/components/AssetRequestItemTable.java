package views.device.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.device.AssetRequestItem;

public class AssetRequestItemTable extends JTable {
    private DefaultTableModel model;

    public AssetRequestItemTable() {
        super();
        model = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Request ID", "Asset ID", "Tên tài sản", "Borrow Date",
                "Return Date", "Condition Borrow", "Condition Return" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    // PHƯƠNG THỨC CŨ - Chúng ta sẽ không dùng trực tiếp setDataVector nữa
    public void setAssetRequestItemData(Object[][] data) {
        model.setDataVector(data, new String[] { "ID", "Request ID", "Asset ID", "Tên tài sản", "Borrow Date", "Return Date",
                "Condition Borrow", "Condition Return" });
    }

    // --- PHẦN SỬA LỖI ---
    // Sửa lại phương thức này để cập nhật bảng một cách triệt để hơn
    public void setAssetRequestItemData(List<AssetRequestItem> items) {
        // Xóa tất cả các dòng hiện có
        model.setRowCount(0);

        // Thêm lại từng dòng với dữ liệu mới
        for (AssetRequestItem item : items) {
            model.addRow(new Object[]{
                item.getRequestItemId(),
                item.getAssetRequest() != null ? item.getAssetRequest().getRequestId() : "",
                item.getAsset() != null ? item.getAsset().getAssetId() : "",
                item.getAsset() != null ? item.getAsset().getAssetName() : "N/A",
                item.getBorrowDate(),
                item.getReturnDate(),
                item.getConditionOnBorrow(),
                item.getConditionOnReturn()
            });
        }
    }

    public DefaultTableModel getModel() {
        return model;
    }
}