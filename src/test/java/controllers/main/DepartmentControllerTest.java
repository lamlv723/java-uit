package controllers.main;

import models.main.Department;
import models.main.Employee;
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
        Employee currentUser = new Employee();

        departmentController.addDepartment(department, currentUser);
        verify(departmentServiceMock, times(1)).addDepartment(department, currentUser);
    }

    @Test
    void testUpdateDepartment() {
        Department department = new Department();
        Employee currentUser = new Employee();

        departmentController.updateDepartment(department, currentUser);
        verify(departmentServiceMock, times(1)).updateDepartment(department, currentUser);
    }

    @Test
    void testDeleteDepartment() {
        Employee currentUser = new Employee();
        departmentController.deleteDepartment(1, currentUser);
        verify(departmentServiceMock, times(1)).deleteDepartment(1, currentUser);
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
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        List<Department> departments = Arrays.asList(new Department(), new Department());
        when(departmentServiceMock.getAllDepartments(currentUser)).thenReturn(departments);
        List<Department> result = departmentController.getAllDepartments(currentUser);
        assertEquals(2, result.size());
    }
}
