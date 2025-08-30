package views.device.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.device.AssetRequestItem;

public class AssetRequestItemTable extends JTable {
    private DefaultTableModel model;
    private boolean showRequestId = false; // quyết định có hiển thị cột Request ID hay không

    public AssetRequestItemTable() {
        this(false);
    }

    public AssetRequestItemTable(boolean showRequestId) {
        super();
        this.showRequestId = showRequestId;
        String[] columns = showRequestId
                ? new String[] { "ID", "Request ID", "Asset ID", "Asset Name", "Borrow Date", "Return Date" }
                : new String[] { "ID", "Asset ID", "Asset Name", "Borrow Date", "Return Date" };
        model = new DefaultTableModel(new Object[][] {}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setAssetRequestItemData(Object[][] data) {
        String[] columns = showRequestId
                ? new String[] { "ID", "Request ID", "Asset ID", "Asset Name", "Borrow Date", "Return Date" }
                : new String[] { "ID", "Asset ID", "Asset Name", "Borrow Date", "Return Date" };
        model.setDataVector(data, columns);
    }

    public void setAssetRequestItemData(List<AssetRequestItem> items) {
        Object[][] data;
        if (showRequestId) {
            data = new Object[items.size()][6];
            for (int i = 0; i < items.size(); i++) {
                AssetRequestItem item = items.get(i);
                data[i][0] = item.getRequestItemId();
                data[i][1] = item.getAssetRequest() != null ? item.getAssetRequest().getRequestId() : "";
                data[i][2] = item.getAsset() != null ? item.getAsset().getAssetId() : "";
                data[i][3] = item.getAsset() != null ? item.getAsset().getAssetName() : "";
                data[i][4] = item.getBorrowDate();
                data[i][5] = item.getReturnDate();
            }
        } else {
            data = new Object[items.size()][5];
            for (int i = 0; i < items.size(); i++) {
                AssetRequestItem item = items.get(i);
                data[i][0] = item.getRequestItemId();
                data[i][1] = item.getAsset() != null ? item.getAsset().getAssetId() : "";
                data[i][2] = item.getAsset() != null ? item.getAsset().getAssetName() : "";
                data[i][3] = item.getBorrowDate();
                data[i][4] = item.getReturnDate();
            }
        }
        setAssetRequestItemData(data);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
