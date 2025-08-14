package views.device.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.device.Asset;

public class AssetTable extends JTable {
    private DefaultTableModel model;
    private List<Asset> assetList; // <-- Thêm dòng này

    public AssetTable() {
        super();
        model = new DefaultTableModel(new Object[][] {},
                new String[] {"Mã tài sản", "Tên tài sản", "Loại", "Nhà cung cấp", "Tình trạng" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setAssetData(Object[][] data) {
        model.setDataVector(data, new String[] {"Mã tài sản", "Tên tài sản", "Loại", "Nhà cung cấp", "Tình trạng" });
    }

    public void setAssetData(List<Asset> assets) {
        this.assetList = assets; // <-- Gán danh sách gốc
        Object[][] data = new Object[assets.size()][5]; // <-- Chỉ 4 cột hiển thị
        for (int i = 0; i < assets.size(); i++) {
            Asset a = assets.get(i);
            data[i][0] = a.getAssetTag();
            data[i][1] = a.getAssetName();
            data[i][2] = a.getCategory() != null ? a.getCategory().getCategoryName() : "";
            data[i][3] = a.getVendor() != null ? a.getVendor().getVendorName() : "";
            data[i][4] = a.getStatus();
        }
        setAssetData(data);
    }

    public Asset getAssetAt(int rowIndex) {
        if (assetList != null && rowIndex >= 0 && rowIndex < assetList.size()) {
            return assetList.get(rowIndex);
        }
        return null;
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
