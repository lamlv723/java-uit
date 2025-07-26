package controllers.main;

import services.main.EmployeeService;

import java.util.List;

import models.main.Employee;

public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public void addEmployee(Employee employee, String currentUserRole) {
        employeeService.addEmployee(employee, currentUserRole);
    }

    public void updateEmployee(Employee employee, String currentUserRole) {
        employeeService.updateEmployee(employee, currentUserRole);
    }

    public void deleteEmployee(int employeeId, String currentUserRole) {
        employeeService.deleteEmployee(employeeId, currentUserRole);
    }

    public Employee getEmployeeById(int employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}
