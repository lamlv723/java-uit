package services.main;

import dao.main.interfaces.DepartmentDAO;
import models.main.Department;
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
        try {
            daoField = DepartmentService.class.getDeclaredField("departmentDAO");
            daoField.setAccessible(true);
            daoField.set(departmentService, departmentDAOMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddDepartment() {
        Department department = new Department();
        departmentService.addDepartment(department, "ADMIN");
        verify(departmentDAOMock, times(1)).addDepartment(department);
    }

    @Test
    void testUpdateDepartment() {
        Department department = new Department();
        departmentService.updateDepartment(department, "ADMIN");
        verify(departmentDAOMock, times(1)).updateDepartment(department);
    }

    @Test
    void testDeleteDepartment() {
        departmentService.deleteDepartment(1, "ADMIN");
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
    void testGetAllDepartments() {
        List<Department> departments = Arrays.asList(new Department(), new Department());
        when(departmentDAOMock.getAllDepartments()).thenReturn(departments);
        List<Department> result = departmentService.getAllDepartments();
        assertEquals(2, result.size());
    }
}
