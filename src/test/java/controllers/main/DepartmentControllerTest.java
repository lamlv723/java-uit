package controllers.main;

import models.main.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.main.DepartmentService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentControllerTest {
    private DepartmentController departmentController;
    private DepartmentService departmentServiceMock;

    @BeforeEach
    void setUp() {
        departmentServiceMock = mock(DepartmentService.class);
        departmentController = new DepartmentController(departmentServiceMock);
    }

    @Test
    void testAddDepartment() {
        Department department = new Department();
        departmentController.addDepartment(department, "ADMIN");
        verify(departmentServiceMock, times(1)).addDepartment(department, "ADMIN");
    }

    @Test
    void testUpdateDepartment() {
        Department department = new Department();
        departmentController.updateDepartment(department, "ADMIN");
        verify(departmentServiceMock, times(1)).updateDepartment(department, "ADMIN");
    }

    @Test
    void testDeleteDepartment() {
        departmentController.deleteDepartment(1, "ADMIN");
        verify(departmentServiceMock, times(1)).deleteDepartment(1, "ADMIN");
    }

    @Test
    void testGetDepartmentById() {
        Department department = new Department();
        when(departmentServiceMock.getDepartmentById(1)).thenReturn(department);
        Department result = departmentController.getDepartmentById(1);
        assertEquals(department, result);
    }

    @Test
    void testGetAllDepartments() {
        List<Department> departments = Arrays.asList(new Department(), new Department());
        when(departmentServiceMock.getAllDepartments()).thenReturn(departments);
        List<Department> result = departmentController.getAllDepartments();
        assertEquals(2, result.size());
    }
}
