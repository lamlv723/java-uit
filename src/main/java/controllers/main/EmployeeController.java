package controllers.main;

import services.main.EmployeeService;

import java.util.List;

import models.main.Employee;

public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController() {
        this.employeeService = new EmployeeService();
    }

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
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

    public List<Employee> getEmployeesByRole(String role) {
        return employeeService.getEmployeesByRole(role);
    }

    public String changePassword(Employee currentUser, String oldPass, String newPass) {
        return employeeService.changePassword(currentUser, oldPass, newPass);
    }
}
