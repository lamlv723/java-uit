package views.main;

import controllers.main.DepartmentController;
import controllers.user.UserSession;
import models.main.Department;
import models.main.Employee;
import services.main.DepartmentService;
import utils.UIUtils;
import views.common.BaseManagementFrame;
import views.main.components.DepartmentTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Quản lý Phòng ban - refactor dùng BaseManagementFrame
 */
public class DepartmentManagementView extends BaseManagementFrame {
    private final DepartmentController departmentController;
    private final DepartmentTable departmentTable;

    public DepartmentManagementView() {
        super("Quản lý Phòng ban", "Quản lý Phòng ban", "cogs", 900, 600,
                Color.decode("#373B44"), Color.decode("#4286f4"));
        departmentController = new DepartmentController(new DepartmentService());
        departmentTable = (DepartmentTable) this.table;
        loadData();
    }

    @Override
    protected JTable createTable() {
        return new DepartmentTable();
    }

    @Override
    protected void loadData() {
        Employee current = UserSession.getInstance().getLoggedInEmployee();
        List<Department> list = departmentController.getAllDepartments(current);
        Object[][] data = new Object[list.size()][3];
        for (int i = 0; i < list.size(); i++) {
            Department d = list.get(i);
            data[i][0] = d.getDepartmentId();
            data[i][1] = d.getDepartmentName();
            Employee head = d.getHeadEmployee();
            data[i][2] = (head != null) ? head.getFullName() : "";
        }
        departmentTable.setDepartmentData(data);
    }

    @Override
    protected void onAdd() {
        DepartmentFormDialog d = new DepartmentFormDialog(this, departmentController, null);
        d.setVisible(true);
        if (d.isSaved())
            loadData();
    }

    @Override
    protected void onEdit(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        Department dept = departmentController.getDepartmentById(id);
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phòng ban!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DepartmentFormDialog d = new DepartmentFormDialog(this, departmentController, dept);
        d.setVisible(true);
        if (d.isSaved())
            loadData();
    }

    @Override
    protected void onDelete(int selectedRow) {
        Integer id = (Integer) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phòng ban này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Employee user = UserSession.getInstance().getLoggedInEmployee();
            try {
                departmentController.deleteDepartment(id, user);
                loadData();
            } catch (Exception ex) {
                UIUtils.showErrorDialog(this, "Đã xảy ra lỗi không mong muốn.\nChi tiết: " + ex.getMessage(),
                        "Lỗi Hệ Thống");
                ex.printStackTrace();
            }
        }
    }
}

