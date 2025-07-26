package views.main;

import dao.main.DepartmentDAOImpl;
import dao.main.interfaces.DepartmentDAO;
import models.main.Department;
import models.main.Employee;
import views.main.components.DepartmentTable;
import java.util.List;

import javax.swing.*;
import java.awt.*;

public class DepartmentManagementView extends JFrame {
    public DepartmentManagementView() {
        setTitle("Quản lý Phòng ban");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DepartmentTable table = new DepartmentTable();
        loadDataToTable(table);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable(DepartmentTable table) {
        DepartmentDAO dao = new DepartmentDAOImpl();
        List<Department> list = dao.getAllDepartments();
        Object[][] data = new Object[list.size()][3];
        for (int i = 0; i < list.size(); i++) {
            Department d = list.get(i);
            data[i][0] = d.getDepartmentId();
            data[i][1] = d.getDepartmentName();
            Employee head = d.getHeadEmployee();
            data[i][2] = (head != null) ? head.getEmployeeId() : "";
        }
        table.setDepartmentData(data);
    }
}
