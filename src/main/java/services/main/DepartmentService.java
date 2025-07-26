package services.main;

import java.util.List;
import dao.main.DepartmentDAOImpl;
import dao.main.interfaces.DepartmentDAO;
import models.main.Department;

public class DepartmentService {
    private DepartmentDAO departmentDAO;

    public DepartmentService() {
        this.departmentDAO = new DepartmentDAOImpl();
    }

    public void addDepartment(Department department, String currentUserRole) {
        // TODO: Add role-based validation if needed
        departmentDAO.addDepartment(department);
    }

    public void updateDepartment(Department department, String currentUserRole) {
        // TODO: Add role-based validation if needed
        departmentDAO.updateDepartment(department);
    }

    public void deleteDepartment(int departmentId, String currentUserRole) {
        // TODO: Add role-based validation if needed
        departmentDAO.deleteDepartment(departmentId);
    }

    public Department getDepartmentById(int departmentId) {
        return departmentDAO.getDepartmentById(departmentId);
    }

    public List<Department> getAllDepartments() {
        return departmentDAO.getAllDepartments();
    }
}
