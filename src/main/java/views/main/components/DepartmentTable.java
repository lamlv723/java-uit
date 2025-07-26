package views.main.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.main.Department;

public class DepartmentTable extends JTable {
    private DefaultTableModel model;

    public DepartmentTable() {
        super();
        model = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Tên phòng ban", "Trưởng phòng" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setDepartmentData(Object[][] data) {
        model.setDataVector(data, new String[] { "ID", "Tên phòng ban", "Trưởng phòng" });
    }

    // Thêm phương thức tiện lợi để nhận List<Department>
    public void setDepartmentData(List<Department> departments) {
        Object[][] data = new Object[departments.size()][3];
        for (int i = 0; i < departments.size(); i++) {
            Department d = departments.get(i);
            data[i][0] = d.getDepartmentId();
            data[i][1] = d.getDepartmentName();
            data[i][2] = d.getHeadEmployee() != null
                    ? (d.getHeadEmployee().getFirstName() + " " + d.getHeadEmployee().getLastName())
                    : "";
        }
        setDepartmentData(data);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
