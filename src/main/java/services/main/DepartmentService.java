
package services.main;

import java.util.List;
import dao.main.DepartmentDAOImpl;
import dao.main.interfaces.DepartmentDAO;
import models.main.Department;

public class DepartmentService {
    private DepartmentDAO departmentDAO;

    public DepartmentService() {
        this.departmentDAO = new DepartmentDAOImpl();
    }

    public void addDepartment(Department department, String currentUserRole) {
        // TODO: Add role-based validation if needed
        departmentDAO.addDepartment(department);
    }

    public void updateDepartment(Department department, String currentUserRole) {
        // TODO: Add role-based validation if needed
        departmentDAO.updateDepartment(department);
    }

    public void deleteDepartment(int departmentId, String currentUserRole) {
        // TODO: Add role-based validation if needed
        departmentDAO.deleteDepartment(departmentId);
    }

    public Department getDepartmentById(int departmentId) {
        return departmentDAO.getDepartmentById(departmentId);
    }

    public List<Department> getAllDepartments() {
        return departmentDAO.getAllDepartments();
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm phòng ban từ dữ liệu đầu vào dạng
     * String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addDepartmentFromInput(String name, String headIdStr, String currentUserRole) {
        if (name == null || name.isEmpty()) {
            return "Tên phòng ban không được để trống!";
        }
        Integer headId = null;
        if (headIdStr != null && !headIdStr.isEmpty()) {
            try {
                headId = Integer.parseInt(headIdStr);
            } catch (NumberFormatException ex) {
                return "ID trưởng phòng phải là số!";
            }
        }
        Department dept = new Department();
        dept.setDepartmentName(name);
        if (headId != null) {
            models.main.Employee head = new models.main.Employee();
            head.setEmployeeId(headId);
            dept.setHeadEmployee(head);
        }
        try {
            addDepartment(dept, currentUserRole);
        } catch (Exception ex) {
            return "Lỗi khi thêm phòng ban: " + ex.getMessage();
        }
        return null;
    }
}
