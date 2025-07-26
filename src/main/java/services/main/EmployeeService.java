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
}
