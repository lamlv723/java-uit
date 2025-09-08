package dao.main.interfaces;

import java.util.List;

import models.main.Department;

public interface DepartmentDAO {
    void addDepartment(Department department);

    void updateDepartment(Department department);

    void deleteDepartment(int departmentId);

    Department getDepartmentById(int departmentId);

    // Pure queries
    List<Department> getAll();

    Department findByName(String name);
}
