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
    }

    public void setAssetData(Object[][] data) {
        model.setDataVector(data, new String[] { "ID", "Tên tài sản", "Loại", "Nhà cung cấp", "Tình trạng" });
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
            data[i][4] = a.getStatus();
        }
        setAssetData(data);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
