package controllers.main;

import models.main.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.main.EmployeeService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {
    private EmployeeController employeeController;
    private EmployeeService employeeServiceMock;

    @BeforeEach
    void setUp() {
        employeeServiceMock = mock(EmployeeService.class);
        employeeController = new EmployeeController(employeeServiceMock);
    }

    @Test
    void testAddEmployee() {
        Employee employee = new Employee();
        employeeController.addEmployee(employee, "ADMIN");
        verify(employeeServiceMock, times(1)).addEmployee(employee, "ADMIN");
    }

    @Test
    void testUpdateEmployee() {
        Employee employee = new Employee();
        employeeController.updateEmployee(employee, "ADMIN");
        verify(employeeServiceMock, times(1)).updateEmployee(employee, "ADMIN");
    }

    @Test
    void testDeleteEmployee() {
        employeeController.deleteEmployee(1, "ADMIN");
        verify(employeeServiceMock, times(1)).deleteEmployee(1, "ADMIN");
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        when(employeeServiceMock.getEmployeeById(1)).thenReturn(employee);
        Employee result = employeeController.getEmployeeById(1);
        assertEquals(employee, result);
    }

    @Test
    void testGetAllEmployees() {
        // Mock current user
        Employee currentUser = new Employee();
        currentUser.setRole("ADMIN");

        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeServiceMock.getAllEmployees(currentUser)).thenReturn(employees);

        List<Employee> result = employeeController.getAllEmployees(currentUser);
        assertEquals(2, result.size());

        // Verify if service call (optional)
//        verify(employeeServiceMock).getAllEmployees(currentUser);
    }
}
