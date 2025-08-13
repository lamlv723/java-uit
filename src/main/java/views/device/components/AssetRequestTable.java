package views.device.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.device.AssetRequest;

public class AssetRequestTable extends JTable {
    private DefaultTableModel model;

    public AssetRequestTable() {
        super();
        model = new DefaultTableModel(new Object[][] {},
                new String[] {"Mã tài sản", "Nhân viên", "Ngày yêu cầu", "Ngày trả", "Trạng thái" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setAssetRequestData(Object[][] data) {
        model.setDataVector(data, new String[] {"Mã tài sản", "Nhân viên", "Ngày yêu cầu", "Ngày trả", "Trạng thái" });
    }

    public void setAssetRequestData(List<AssetRequest> requests) {
        Object[][] data = new Object[requests.size()][5];
        for (int i = 0; i < requests.size(); i++) {
            AssetRequest r = requests.get(i);
            data[i][0] = r.getAsset().getAssetTag();
            data[i][1] = r.getEmployee() != null
                    ? (r.getEmployee().getFirstName() + " " + r.getEmployee().getLastName())
                    : "";
            data[i][2] = r.getRequestDate();
            data[i][3] = r.getReturnDate();
            data[i][4] = r.getStatus();
        }
        setAssetRequestData(data);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
