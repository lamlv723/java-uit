
package views.device.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.device.AssetCategory;

public class AssetCategoryTable extends JTable {
    private DefaultTableModel model;

    public AssetCategoryTable() {
        super();
        model = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Tên danh mục", "Mô tả" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setAssetCategoryData(Object[][] data) {
        model.setDataVector(data, new String[] { "ID", "Tên danh mục", "Mô tả" });
    }

    // Nhận List<AssetCategory>
    public void setAssetCategoryData(List<AssetCategory> categories) {
        Object[][] data = new Object[categories.size()][3];
        for (int i = 0; i < categories.size(); i++) {
            AssetCategory c = categories.get(i);
            data[i][0] = c.getCategoryId();
            data[i][1] = c.getCategoryName();
            data[i][2] = c.getDescription();
        }
        setAssetCategoryData(data);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
