
package services.main;

import java.util.List;
import dao.main.EmployeeDAOImpl;
import dao.main.interfaces.EmployeeDAO;
import models.main.Employee;

public class EmployeeService {
    private EmployeeDAO employeeDAO;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAOImpl();
    }

    public void addEmployee(Employee employee, String currentUserRole) {
        // TODO: Add role-based validation if needed
        employeeDAO.addEmployee(employee);
    }

    public void updateEmployee(Employee employee, String currentUserRole) {
        // TODO: Add role-based validation if needed
        employeeDAO.updateEmployee(employee);
    }

    public void deleteEmployee(int employeeId, String currentUserRole) {
        // TODO: Add role-based validation if needed
        employeeDAO.deleteEmployee(employeeId);
    }

    public Employee getEmployeeById(int employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
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
        try {
            addEmployee(emp, currentUserRole);
        } catch (Exception ex) {
            return "Lỗi khi thêm nhân viên: " + ex.getMessage();
        }
        return null;
    }
}
