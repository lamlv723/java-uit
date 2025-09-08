
package services.main;

import java.util.List;
import java.util.stream.Collectors;

import models.main.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.main.DepartmentDAOImpl;
import dao.main.interfaces.DepartmentDAO;
import models.main.Department;

public class DepartmentService {
    private final DepartmentDAO departmentDAO;
    private final EmployeeService employeeService;

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    public DepartmentService(DepartmentDAO departmentDAO, EmployeeService employeeService) {
        this.departmentDAO = departmentDAO;
        this.employeeService = employeeService;
    }

    // Backward-compatible default wiring
    public DepartmentService() {
        this(new DepartmentDAOImpl(), new EmployeeService());
    }

    public void addDepartment(Department department, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole
                    + " attempted to add a department.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }

        if (departmentDAO.findByName(department.getDepartmentName()) != null) {
            throw new IllegalStateException("Tên phòng ban đã tồn tại.");
        }

        departmentDAO.addDepartment(department);

        // Assign head if provided
        Employee head = department.getHeadEmployee();
        if (head != null) {
            promoteAndAssignEmployeeToDepartment(head, department, currentUser);
        }
    }

    public void updateDepartment(Department department, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole
                    + " attempted to update a department.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }

        Department existing = departmentDAO.findByName(department.getDepartmentName());
        if (existing != null && !existing.getDepartmentId().equals(department.getDepartmentId())) {
            throw new IllegalStateException("Tên phòng ban đã tồn tại.");
        }

        departmentDAO.updateDepartment(department);

        // Assign head if provided
        Employee head = department.getHeadEmployee();
        if (head != null) {
            promoteAndAssignEmployeeToDepartment(head, department, currentUser);
        }
    }

    private void promoteAndAssignEmployeeToDepartment(Employee employee, Department department, Employee currentUser) {
        Employee employeeToUpdate = employeeService.getEmployeeById(employee.getEmployeeId());
        if (employeeToUpdate != null) {
            employeeToUpdate.setRole("Manager");
            employeeToUpdate.setDepartment(department);
            employeeService.updateEmployee(employeeToUpdate, currentUser);
            logger.info("Promoted and assigned employee {} to department {}", employeeToUpdate.getUsername(),
                    department.getDepartmentName());
        }
    }

    public void deleteDepartment(int departmentId, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole
                    + " attempted to delete department with id " + departmentId + ".";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        departmentDAO.deleteDepartment(departmentId);
    }

    public Department getDepartmentById(int departmentId) {
        return departmentDAO.getDepartmentById(departmentId);
    }

    public List<Department> getAllDepartments(Employee currentUser) {
        if (currentUser == null) {
            return java.util.Collections.emptyList();
        }
        String role = currentUser.getRole();
        if ("Admin".equalsIgnoreCase(role)) {
            return departmentDAO.getAll();
        }
        Integer deptId = currentUser.getDepartmentId();
        if (deptId == null)
            return java.util.Collections.emptyList();
        // Manager/Staff: only their own department
        List<Department> all = departmentDAO.getAll();
        if (all == null)
            return java.util.Collections.emptyList();
        return all.stream()
                .filter(d -> java.util.Objects.equals(d.getDepartmentId(), deptId))
                .collect(Collectors.toList());
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm phòng ban từ dữ liệu đầu vào dạng
     * String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addDepartmentFromInput(String name, String headIdStr, Employee currentUser) {

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

        // If error, let View layer catch and display to user
        addDepartment(dept, currentUser);

        return null;
    }
}
