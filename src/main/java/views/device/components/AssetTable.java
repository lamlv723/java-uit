package views.device.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.device.Asset;

public class AssetTable extends JTable {
    private DefaultTableModel model;

    public AssetTable() {
        super();
        model = new DefaultTableModel(new Object[][] {},
                new String[] { "ID", "Tên tài sản", "Loại", "Nhà cung cấp", "Tình trạng" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
        // Giữ nguyên cấu trúc cột, tránh Swing tự tái tạo khi model thay đổi
        setAutoCreateColumnsFromModel(false);
    }

    public void setAssetData(Object[][] data) {
        // Không dùng setDataVector vì sẽ tạo lại cột và làm mất cell renderer đã gán.
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }

    // Nhận List<Asset>
    public void setAssetData(List<Asset> assets) {
        Object[][] data = new Object[assets.size()][5];
        for (int i = 0; i < assets.size(); i++) {
            Asset a = assets.get(i);
            data[i][0] = a.getAssetId();
            data[i][1] = a.getAssetName();
            data[i][2] = a.getCategory() != null ? a.getCategory().getCategoryName() : "";
            data[i][3] = a.getVendor() != null ? a.getVendor().getVendorName() : "";
            data[i][4] = mapStatusForDisplay(a.getStatus());
        }
        setAssetData(data);
    }

    private String mapStatusForDisplay(String raw) {
        if (raw == null) return "";
        String v = raw.trim();
        switch (v.toLowerCase()) {
            case "available":
                return "Available";
            case "borrowed":
            case "đang mượn":
                return "Borrowed";
            case "retired":
            case "ngưng sử dụng":
                return "Retired";
            default:
                return v; // fallback nếu DB phát sinh giá trị ngoài 3 trạng thái chuẩn
        }
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
