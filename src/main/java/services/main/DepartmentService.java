package services.main;

import java.util.List;

import models.main.Department;

public interface DepartmentService {
    void addDepartment(Department department, String currentUserRole);

    void updateDepartment(Department department, String currentUserRole);

    void deleteDepartment(int departmentId, String currentUserRole);

    Department getDepartmentById(int departmentId);

    List<Department> getAllDepartments();
}
