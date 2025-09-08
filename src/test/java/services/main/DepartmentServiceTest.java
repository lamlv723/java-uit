package services.main;

import dao.main.interfaces.DepartmentDAO;
import models.main.Department;
import models.main.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {
    private DepartmentService departmentService;
    private DepartmentDAO departmentDAOMock;

    @BeforeEach
    void setUp() {
        departmentDAOMock = Mockito.mock(DepartmentDAO.class);
        departmentService = new DepartmentService();
        // Inject mock DAO
        java.lang.reflect.Field daoField;
        DepartmentService real = new DepartmentService(departmentDAOMock,
                new EmployeeService(Mockito.mock(dao.main.interfaces.EmployeeDAO.class)));
        departmentService = real;
        try {
            daoField = DepartmentService.class.getDeclaredField("departmentDAO");
            daoField.setAccessible(true);
            daoField.set(departmentService, departmentDAOMock);

            EmployeeService employeeServiceMock = Mockito.mock(EmployeeService.class);
            java.lang.reflect.Field serviceField = DepartmentService.class.getDeclaredField("employeeService");
            serviceField.setAccessible(true);
            serviceField.set(departmentService, employeeServiceMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddDepartment() {
        Department department = new Department();
        department.setDepartmentName("New Dept");
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        when(departmentDAOMock.findByName("New Dept")).thenReturn(null);

        departmentService.addDepartment(department, currentUser);
        verify(departmentDAOMock, times(1)).addDepartment(department);
    }

    @Test
    void testAddDepartment_throwsOnDuplicateName() {
        Department department = new Department();
        department.setDepartmentName("Duplicate");
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        when(departmentDAOMock.findByName("Duplicate")).thenReturn(new Department());

        assertThrows(IllegalStateException.class, () -> {
            departmentService.addDepartment(department, currentUser);
        });
        verify(departmentDAOMock, never()).addDepartment(department);
    }

    @Test
    void testUpdateDepartment() {
        Department department = new Department();
        department.setDepartmentId(1);
        department.setDepartmentName("Updated");
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        when(departmentDAOMock.findByName("Updated")).thenReturn(null);

        departmentService.updateDepartment(department, currentUser);
        verify(departmentDAOMock, times(1)).updateDepartment(department);
    }

    @Test
    void testUpdateDepartment_throwsOnDuplicateName() {
        Department otherDept = new Department();
        otherDept.setDepartmentId(2);
        otherDept.setDepartmentName("Existing");

        Department departmentToUpdate = new Department();
        departmentToUpdate.setDepartmentId(1);
        departmentToUpdate.setDepartmentName("Existing");
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        when(departmentDAOMock.findByName("Existing")).thenReturn(otherDept);

        assertThrows(IllegalStateException.class, () -> {
            departmentService.updateDepartment(departmentToUpdate, currentUser);
        });
        verify(departmentDAOMock, never()).updateDepartment(departmentToUpdate);
    }

    @Test
    void testDeleteDepartment() {
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");
        departmentService.deleteDepartment(1, currentUser);
        verify(departmentDAOMock, times(1)).deleteDepartment(1);
    }

    @Test
    void testGetDepartmentById() {
        Department department = new Department();
        when(departmentDAOMock.getDepartmentById(1)).thenReturn(department);
        Department result = departmentService.getDepartmentById(1);
        assertEquals(department, result);
    }

    @Test
    void testGetAllDepartments_Admin() {
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        List<Department> departments = Arrays.asList(new Department(), new Department());
        when(departmentDAOMock.getAll()).thenReturn(departments);
        List<Department> result = departmentService.getAllDepartments(currentUser);
        assertEquals(2, result.size());
        verify(departmentDAOMock, times(1)).getAll();
    }
}