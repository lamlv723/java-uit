package views.main;

import dao.main.EmployeeDAOImpl;
import dao.main.interfaces.EmployeeDAO;
import models.main.Employee;
import views.main.components.EmployeeTable;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class EmployeeManagementView extends JFrame {
    public EmployeeManagementView() {
        setTitle("Quản lý Nhân viên");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        EmployeeTable table = new EmployeeTable();
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

    private void loadDataToTable(EmployeeTable table) {
        EmployeeDAO dao = new EmployeeDAOImpl();
        List<Employee> list = dao.getAllEmployees();
        table.setEmployeeData(list);
    }
}
