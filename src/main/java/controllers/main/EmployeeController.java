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

    public void addEmployee(Employee employee, Employee currentUser) {
        employeeService.addEmployee(employee, currentUser);
    }

    public void updateEmployee(Employee employee, Employee currentUser) {
        employeeService.updateEmployee(employee, currentUser);
    }

    public void deleteEmployee(int employeeId, Employee currentUser) {
        employeeService.deleteEmployee(employeeId, currentUser);
    }

    public Employee getEmployeeById(int employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    public List<Employee> getAllEmployees(Employee currentUser) {
        return employeeService.getAllEmployees(currentUser);
    }
}
