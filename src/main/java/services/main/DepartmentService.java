
package services.main;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.main.DepartmentDAOImpl;
import dao.main.interfaces.DepartmentDAO;
import models.main.Department;

public class DepartmentService {
    private DepartmentDAO departmentDAO;
    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    public DepartmentService() {
        this.departmentDAO = new DepartmentDAOImpl();
    }

    public void addDepartment(Department department, String currentUserRole) {
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to add a department.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        departmentDAO.addDepartment(department);
    }

    public void updateDepartment(Department department, String currentUserRole) {
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to update a department.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        departmentDAO.updateDepartment(department);
    }

    public void deleteDepartment(int departmentId, String currentUserRole) {
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to delete department with id " + departmentId + ".";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
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
