package controllers.main;

import models.main.Employee;
import services.main.DepartmentService;

import java.util.List;

import models.main.Department;

public class DepartmentController {
    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public DepartmentService getDepartmentService() {
        return departmentService;
    }

    public void addDepartment(Department department, String currentUserRole) {
        departmentService.addDepartment(department, currentUserRole);
    }

    public void updateDepartment(Department department, String currentUserRole) {
        departmentService.updateDepartment(department, currentUserRole);
    }

    public void deleteDepartment(int departmentId, String currentUserRole) {
        departmentService.deleteDepartment(departmentId, currentUserRole);
    }

    public Department getDepartmentById(int departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    public List<Department> getAllDepartments(Employee currentUser) {
        return departmentService.getAllDepartments(currentUser);
    }
}
