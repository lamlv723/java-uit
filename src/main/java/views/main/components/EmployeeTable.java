package views.main.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import models.main.Employee;

public class EmployeeTable extends JTable {
    private DefaultTableModel model;

    public EmployeeTable() {
        super();
        model = new DefaultTableModel(new Object[][] {},
                new String[] { "ID", "Tên", "Họ", "Email", "SĐT", "Chức vụ", "Phòng ban" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setEmployeeData(Object[][] data) {
        model.setDataVector(data, new String[] { "ID", "Tên", "Họ", "Email", "SĐT", "Chức vụ", "Phòng ban" });
    }

    public void setEmployeeData(List<Employee> employees) {
        Object[][] data = new Object[employees.size()][7];
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            data[i][0] = e.getEmployeeId();
            data[i][1] = e.getFirstName();
            data[i][2] = e.getLastName();
            data[i][3] = e.getEmail();
            data[i][4] = e.getPhoneNumber();
            data[i][5] = e.getRole();
            data[i][6] = e.getDepartment() != null ? e.getDepartment().getDepartmentName() : "";
        }
        setEmployeeData(data);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
