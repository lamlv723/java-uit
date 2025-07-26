package views.device.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.device.Vendor;

public class VendorTable extends JTable {
    private DefaultTableModel model;

    public VendorTable() {
        super();
        model = new DefaultTableModel(new Object[][] {},
                new String[] { "ID", "Tên nhà cung cấp", "Địa chỉ", "Số điện thoại", "Email" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setVendorData(Object[][] data) {
        model.setDataVector(data, new String[] { "ID", "Tên nhà cung cấp", "Địa chỉ", "Số điện thoại", "Email" });
    }

    public void setVendorData(List<Vendor> vendors) {
        Object[][] data = new Object[vendors.size()][5];
        for (int i = 0; i < vendors.size(); i++) {
            Vendor v = vendors.get(i);
            data[i][0] = v.getVendorId();
            data[i][1] = v.getVendorName();
            data[i][2] = v.getAddress();
            data[i][3] = v.getPhoneNumber();
            data[i][4] = v.getEmail();
        }
        setVendorData(data);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
