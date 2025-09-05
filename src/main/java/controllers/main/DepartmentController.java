package controllers.main;

import models.main.Employee;
import services.main.DepartmentService;

import java.util.List;

import models.main.Department;

public class DepartmentController {
    private final DepartmentService departmentService;

    // No-arg constructor to hide Service from Views
    public DepartmentController() {
        this.departmentService = new DepartmentService();
    }

    // DI/testing constructor
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void addDepartment(Department department, Employee currentUser) {
        departmentService.addDepartment(department, currentUser);
    }

    public void updateDepartment(Department department, Employee currentUser) {
        departmentService.updateDepartment(department, currentUser);
    }

    public void deleteDepartment(int departmentId, Employee currentUser) {
        departmentService.deleteDepartment(departmentId, currentUser);
    }

    public Department getDepartmentById(int departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    public List<Department> getAllDepartments(Employee currentUser) {
        return departmentService.getAllDepartments(currentUser);
    }
}
