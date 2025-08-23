
package services.main;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.main.EmployeeDAOImpl;
import dao.main.interfaces.EmployeeDAO;
import models.main.Employee;

public class EmployeeService {
    private EmployeeDAO employeeDAO;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAOImpl();
    }

    public void addEmployee(Employee employee, String currentUserRole) {
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to add an employee.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        employeeDAO.addEmployee(employee);
    }

    public void updateEmployee(Employee employee, String currentUserRole) {
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to update an employee.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        employeeDAO.updateEmployee(employee);
    }

    public void deleteEmployee(int employeeId, String currentUserRole) {
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to delete employee with id " + employeeId + ".";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        employeeDAO.deleteEmployee(employeeId);
    }

    public Employee getEmployeeById(int employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }

    public List<Employee> getAllEmployees(Employee currentUser) {
        return employeeDAO.getAllEmployees(currentUser);
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm nhân viên từ dữ liệu đầu vào dạng
     * String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addEmployeeFromInput(String firstName, String lastName, String email, String phone, String deptIdStr,
            String role, String username, String password, String currentUserRole) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            return "Các trường bắt buộc không được để trống!";
        }
        Integer deptId = null;
        if (deptIdStr != null && !deptIdStr.isEmpty()) {
            try {
                deptId = Integer.parseInt(deptIdStr);
            } catch (NumberFormatException ex) {
                return "ID phòng ban phải là số!";
            }
        }
        Employee emp = new Employee();
        emp.setFirstName(firstName);
        emp.setLastName(lastName);
        emp.setEmail(email);
        emp.setPhoneNumber(phone);
        emp.setRole(role);
        emp.setUsername(username);
        emp.setPassword(password);
        emp.setDepartmentId(deptId);

        // If error, let View layer catch and display to user
        addEmployee(emp, currentUserRole);

        return null;
    }
}
