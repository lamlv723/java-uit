package services.main;

import dao.main.interfaces.EmployeeDAO;
import models.main.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {
    private EmployeeService employeeService;
    private EmployeeDAO employeeDAOMock;

    @BeforeEach
    void setUp() {
        employeeDAOMock = Mockito.mock(EmployeeDAO.class);
        employeeService = new EmployeeService();
        // Inject mock DAO
        java.lang.reflect.Field daoField;
        try {
            daoField = EmployeeService.class.getDeclaredField("employeeDAO");
            daoField.setAccessible(true);
            daoField.set(employeeService, employeeDAOMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddEmployee() {
        Employee employee = new Employee();
        employeeService.addEmployee(employee, "ADMIN");
        verify(employeeDAOMock, times(1)).addEmployee(employee);
    }

    @Test
    void testUpdateEmployee() {
        Employee employee = new Employee();
        employeeService.updateEmployee(employee, "ADMIN");
        verify(employeeDAOMock, times(1)).updateEmployee(employee);
    }

    @Test
    void testDeleteEmployee() {
        employeeService.deleteEmployee(1, "ADMIN");
        verify(employeeDAOMock, times(1)).deleteEmployee(1);
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        when(employeeDAOMock.getEmployeeById(1)).thenReturn(employee);
        Employee result = employeeService.getEmployeeById(1);
        assertEquals(employee, result);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeDAOMock.getAllEmployees()).thenReturn(employees);
        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(2, result.size());
    }

    @Test
    void testAddEmployeeFromInput_success() {
        // Trường hợp hợp lệ
        String error = employeeService.addEmployeeFromInput(
                "A", "B", "a@b.com", "0123", "1", "USER", "user", "pass", "ADMIN");
        assertNull(error);
        verify(employeeDAOMock, times(1)).addEmployee(any(Employee.class));
    }

    @Test
    void testAddEmployeeFromInput_missingRequired() {
        // Thiếu trường bắt buộc
        String error = employeeService.addEmployeeFromInput(
                "", "B", "a@b.com", "0123", "1", "USER", "user", "pass", "ADMIN");
        assertEquals("Các trường bắt buộc không được để trống!", error);
        verify(employeeDAOMock, never()).addEmployee(any(Employee.class));
    }

    @Test
    void testAddEmployeeFromInput_invalidDeptId() {
        // deptId không phải số
        String error = employeeService.addEmployeeFromInput(
                "A", "B", "a@b.com", "0123", "abc", "USER", "user", "pass", "ADMIN");
        assertEquals("ID phòng ban phải là số!", error);
        verify(employeeDAOMock, never()).addEmployee(any(Employee.class));
    }

}
