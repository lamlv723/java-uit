package views.main;

import controllers.main.EmployeeController;
import models.main.Employee;
import services.main.EmployeeService;
import views.common.BaseManagementFrame;
import views.main.components.EmployeeTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Quản lý Nhân viên - refactor dùng BaseManagementFrame
 */
public class EmployeeManagementView extends BaseManagementFrame {
    private final EmployeeController employeeController;
    private final EmployeeTable employeeTable;

    public EmployeeManagementView() {
        super("Quản lý Nhân viên", "Quản lý Nhân viên", "user", 1000, 680,
                Color.decode("#373B44"), Color.decode("#4286f4"));
        employeeController = new EmployeeController(new EmployeeService());
        employeeTable = (EmployeeTable) this.table;
        loadData();
    }

    @Override
    protected JTable createTable() {
        return new EmployeeTable();
    }

    @Override
    protected void loadData() {
        Employee currentUser = controllers.user.UserSession.getInstance().getLoggedInEmployee();
        List<Employee> list = employeeController.getAllEmployees(currentUser);
        employeeTable.setEmployeeData(list);
    }

    @Override
    protected void onAdd() {
        EmployeeFormDialog d = new EmployeeFormDialog(this, employeeController, null);
        d.setVisible(true);
        if (d.isSaved())
            loadData();
    }

    @Override
    protected void onEdit(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        Employee emp = employeeController.getEmployeeById(id);
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        EmployeeFormDialog d = new EmployeeFormDialog(this, employeeController, emp);
        d.setVisible(true);
        if (d.isSaved())
            loadData();
    }

    @Override
    protected void onDelete(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            models.main.Employee user = controllers.user.UserSession.getInstance().getLoggedInEmployee();
            try {
                employeeController.deleteEmployee(id, user);
                loadData();
            } catch (Exception ex) {
                utils.UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                        "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        }
    }
}
