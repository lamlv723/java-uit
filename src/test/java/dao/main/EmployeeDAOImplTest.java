package dao.main;

import models.main.Employee;
import models.main.Department;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import config.HibernateUtil;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeDAOImplTest {
    private EmployeeDAOImpl employeeDAO;
    private SessionFactory sessionFactoryMock;
    private org.hibernate.Session sessionMock;
    private org.hibernate.Transaction transactionMock;

    @BeforeEach
    void setUp() {
        employeeDAO = new EmployeeDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionMock = mock(org.hibernate.Session.class);
        transactionMock = mock(org.hibernate.Transaction.class);
        HibernateUtil.setSessionFactory(sessionFactoryMock);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
    }

    @Test
    void testAddEmployee() {
        Employee employee = new Employee();
        employeeDAO.addEmployee(employee);
        verify(sessionMock, times(1)).save(employee);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testUpdateEmployee() {
        Employee employee = new Employee();
        employeeDAO.updateEmployee(employee);
        verify(sessionMock, times(1)).update(employee);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testDeleteEmployee() {
        Employee employee = new Employee();
        when(sessionMock.get(Employee.class, 1)).thenReturn(employee);
        employeeDAO.deleteEmployee(1);
        verify(sessionMock, times(1)).delete(employee);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        when(sessionMock.get(Employee.class, 1)).thenReturn(employee);
        Employee result = employeeDAO.getEmployeeById(1);
        assertEquals(employee, result);
    }

    @Test
    void testGetAllEmployeesAdmin() {
        // Mock data
        Employee currentUser = new Employee();
        currentUser.setRole("ADMIN");

        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        org.hibernate.query.Query<Employee> queryMock = mock(org.hibernate.query.Query.class);
        when(sessionMock.createQuery("FROM Employee", Employee.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(employees);
        List<Employee> result = employeeDAO.getAllEmployees(currentUser);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllEmployeesManager() {
        // Mock data
        Department managerDept = new Department();
        managerDept.setDepartmentId(10);

        Employee currentUser = new Employee();
        currentUser.setRole("MANAGER");
        currentUser.setDepartment(managerDept);

        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        org.hibernate.query.Query<Employee> queryMock = mock(org.hibernate.query.Query.class);

        when(sessionMock.createQuery("FROM Employee WHERE department.departmentId = :deptId", Employee.class)).thenReturn(queryMock);
        when(queryMock.setParameter(eq("deptId"), anyInt())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(employees);

        List<Employee> result = employeeDAO.getAllEmployees(currentUser);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllEmployeesStaff() {
        // Mock data
        Employee currentUser = new Employee();
        currentUser.setRole("Staff");
        currentUser.setEmployeeId(777);

        List<Employee> employees = Arrays.asList(currentUser);
        org.hibernate.query.Query<Employee> queryMock = mock(org.hibernate.query.Query.class);

        when(sessionMock.createQuery("FROM Employee WHERE employeeId = :empId", Employee.class)).thenReturn(queryMock);
        when(queryMock.setParameter(eq("empId"), anyInt())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(employees);

        List<Employee> result = employeeDAO.getAllEmployees(currentUser);
        assertEquals(1, result.size());
    }
}
