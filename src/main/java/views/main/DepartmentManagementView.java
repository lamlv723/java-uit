package views.main;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controllers.main.DepartmentController;
import services.main.DepartmentService;
import models.main.Department;
import models.main.Employee;
import views.main.components.DepartmentTable;

public class DepartmentManagementView extends JFrame {
    private DepartmentTable table;
    private DepartmentController departmentController;

    public DepartmentManagementView() {
        setTitle("Quản lý Phòng ban");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        departmentController = new DepartmentController(new DepartmentService());
        table = new DepartmentTable();
        loadDataToTable();
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        // Action for Add
        btnAdd.addActionListener(e -> {
            JTextField tfName = new JTextField();
            JTextField tfHeadId = new JTextField();
            Object[] message = {
                    "Tên phòng ban:", tfName,
                    "ID trưởng phòng (có thể để trống):", tfHeadId
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Thêm Phòng ban", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = tfName.getText().trim();
                String headIdStr = tfHeadId.getText().trim();
                // Gọi service xử lý nghiệp vụ, trả về lỗi nếu có
                String error = departmentController.getDepartmentService().addDepartmentFromInput(name, headIdStr,
                        "ADMIN");
                if (error == null) {
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Edit
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng ban để sửa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            Department dept = departmentController.getDepartmentById(id);
            if (dept == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phòng ban!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTextField tfName = new JTextField(dept.getDepartmentName());
            JTextField tfHeadId = new JTextField(
                    dept.getHeadEmployee() != null ? String.valueOf(dept.getHeadEmployee().getEmployeeId()) : "");
            Object[] message = {
                    "Tên phòng ban:", tfName,
                    "ID trưởng phòng (có thể để trống):", tfHeadId
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Sửa Phòng ban", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = tfName.getText().trim();
                String headIdStr = tfHeadId.getText().trim();
                if (!name.isEmpty()) {
                    dept.setDepartmentName(name);
                    if (!headIdStr.isEmpty()) {
                        try {
                            int headId = Integer.parseInt(headIdStr);
                            Employee head = new Employee();
                            head.setEmployeeId(headId);
                            dept.setHeadEmployee(head);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "ID trưởng phòng phải là số!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        dept.setHeadEmployee(null);
                    }
                    departmentController.updateDepartment(dept, "ADMIN"); // TODO: lấy role thực tế nếu có
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Tên phòng ban không được để trống!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for Delete
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng ban để xóa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer id = (Integer) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phòng ban này?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                departmentController.deleteDepartment(id, "ADMIN"); // TODO: lấy role thực tế nếu có
                loadDataToTable();
            }
        });

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadDataToTable() {
        List<Department> list = departmentController.getAllDepartments();
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
