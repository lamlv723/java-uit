package dao.main.interfaces;

import java.util.List;

import models.main.Department;
import models.main.Employee;

public interface DepartmentDAO {
    void addDepartment(Department department);

    void updateDepartment(Department department);

    void deleteDepartment(int departmentId);

    Department getDepartmentById(int departmentId);

    Department findByName(String name);

    List<Department> getAllDepartments(Employee currentUser);
}
