package services.main;

import java.util.List;

import models.main.Employee;

public interface EmployeeService {
    void addEmployee(Employee employee, String currentUserRole);

    void updateEmployee(Employee employee, String currentUserRole);

    void deleteEmployee(int employeeId, String currentUserRole);

    Employee getEmployeeById(int employeeId);

    List<Employee> getAllEmployees();
}
