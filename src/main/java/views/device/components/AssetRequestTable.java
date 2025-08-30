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
                new String[] { "ID", "Nhân viên", "Loại yêu cầu", "Ngày yêu cầu", "Trạng thái" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setAssetRequestData(Object[][] data) {
        model.setDataVector(data, new String[] { "ID", "Nhân viên", "Loại yêu cầu", "Ngày yêu cầu", "Trạng thái" });
    }

    public void setAssetRequestData(List<AssetRequest> requests) {
        Object[][] data = new Object[requests.size()][5];
        for (int i = 0; i < requests.size(); i++) {
            AssetRequest r = requests.get(i);
            data[i][0] = r.getRequestId();
            data[i][1] = r.getEmployee() != null
                    ? (r.getEmployee().getFirstName() + " " + r.getEmployee().getLastName())
                    : "";
            String type = r.getRequestType();
            if (type != null) {
                String lower = type.toLowerCase();
                if ("borrow".equals(lower))
                    type = "Borrow";
                else if ("return".equals(lower))
                    type = "Return"; // map hiển thị chuyên nghiệp
            }
            data[i][2] = type;
            data[i][3] = r.getRequestDate();
            data[i][4] = r.getStatus();
        }
        setAssetRequestData(data);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
