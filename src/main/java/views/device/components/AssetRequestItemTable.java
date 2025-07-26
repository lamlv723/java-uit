package views.device.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.device.AssetRequestItem;

public class AssetRequestItemTable extends JTable {
    private DefaultTableModel model;

    public AssetRequestItemTable() {
        super();
        model = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Request ID", "Asset ID", "Borrow Date",
                "Return Date", "Condition Borrow", "Condition Return" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setAssetRequestItemData(Object[][] data) {
        model.setDataVector(data, new String[] { "ID", "Request ID", "Asset ID", "Borrow Date", "Return Date",
                "Condition Borrow", "Condition Return" });
    }

    // Thêm phương thức tiện lợi để nhận List<AssetRequestItem>
    public void setAssetRequestItemData(List<AssetRequestItem> items) {
        Object[][] data = new Object[items.size()][7];
        for (int i = 0; i < items.size(); i++) {
            AssetRequestItem item = items.get(i);
            data[i][0] = item.getRequestItemId();
            data[i][1] = item.getAssetRequest() != null ? item.getAssetRequest().getRequestId() : "";
            data[i][2] = item.getAsset() != null ? item.getAsset().getAssetId() : "";
            data[i][3] = item.getBorrowDate();
            data[i][4] = item.getReturnDate();
            data[i][5] = item.getConditionOnBorrow();
            data[i][6] = item.getConditionOnReturn();
        }
        setAssetRequestItemData(data);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
