package dao.main.interfaces;

import java.util.List;

import models.main.Employee;

public interface EmployeeDAO {
    void addEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployee(int employeeId);

    Employee getEmployeeById(int employeeId);

    List<Employee> getAllEmployees();

    Employee getEmployeeByUsernameAndPassword(String username, String password);
}
