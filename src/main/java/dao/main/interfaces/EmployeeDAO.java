package dao.main.interfaces;

import java.util.List;

import models.main.Employee;

public interface EmployeeDAO {
    void addEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployee(int employeeId);

    Employee getEmployeeById(int employeeId);

    // Pure persistence queries (no role/current user decisions)
    List<Employee> getAll();

    List<Employee> getByDepartmentId(int departmentId);

    List<Employee> getEmployeesByRole(String role);

    Employee getEmployeeByUsernameAndPassword(String username, String password);
}
