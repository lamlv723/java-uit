
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

    public void addEmployee(Employee employee, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to add an employee.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        employeeDAO.addEmployee(employee);
    }

    public void updateEmployee(Employee employee, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to update an employee.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }

        Employee employeeBeforeUpdate = employeeDAO.getEmployeeById(employee.getEmployeeId());

        if (employeeBeforeUpdate != null && "Admin".equalsIgnoreCase(employeeBeforeUpdate.getRole())) {
            // Kiểm tra xem vai trò có bị thay đổi từ Admin sang vai trò khác không
            if (!"Admin".equalsIgnoreCase(employee.getRole())) {
                List<Employee> admins = employeeDAO.getEmployeesByRole("Admin");
                // Nếu chỉ có một admin (chính là người sắp bị đổi vai trò), thì không cho phép
                if (admins != null && admins.size() <= 1) {
                    throw new SecurityException("Không thể thay đổi vai trò của quản trị viên cuối cùng. Hệ thống phải có ít nhất một quản trị viên.");
                }
            }
        }

        employeeDAO.updateEmployee(employee);
    }

    public void deleteEmployee(int employeeId, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to delete employee with id " + employeeId + ".";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }

        Employee employeeToDelete = employeeDAO.getEmployeeById(employeeId);
        if (employeeToDelete != null && "Admin".equalsIgnoreCase(employeeToDelete.getRole())) {
            List<Employee> admins = employeeDAO.getEmployeesByRole("Admin");
            if (admins != null && admins.size() <= 1) {
                throw new SecurityException("Không thể xóa quản trị viên cuối cùng. Hệ thống phải có ít nhất một quản trị viên.");
            }
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
            String role, String username, String password, Employee currentUser) {
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
        addEmployee(emp, currentUser);

        return null;
    }

    public String changePassword(Employee currentUser, String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return "Mật khẩu không được để trống.";
        }

        // Check if the old password is correct
        if (!currentUser.getPassword().equals(oldPassword)) {
            return "Mật khẩu cũ không chính xác.";
        }

        currentUser.setPassword(newPassword);

        // Persist the change to the database
        try {
            employeeDAO.updateEmployee(currentUser);
        } catch (Exception e) {
            logger.error("Error changing password for user {}: {}", currentUser.getUsername(), e.getMessage(), e);
            return "Đã xảy ra lỗi khi cập nhật mật khẩu.";
        }

        return null; // Return null on success
    }

    public List<Employee> getEmployeesByRole(String role) {
        return employeeDAO.getEmployeesByRole(role);
    }
}
