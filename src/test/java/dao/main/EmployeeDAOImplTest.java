package dao.main;

import models.main.Employee;
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
    void testGetAll() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        @SuppressWarnings("unchecked")
        org.hibernate.query.Query<Employee> queryMock = (org.hibernate.query.Query<Employee>) mock(
                org.hibernate.query.Query.class);
        when(sessionMock.createQuery("FROM Employee", Employee.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(employees);
        List<Employee> result = employeeDAO.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void testGetByDepartmentId() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        @SuppressWarnings("unchecked")
        org.hibernate.query.Query<Employee> queryMock = (org.hibernate.query.Query<Employee>) mock(
                org.hibernate.query.Query.class);
        when(sessionMock.createQuery("FROM Employee WHERE department.departmentId = :deptId", Employee.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("deptId"), anyInt())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(employees);
        List<Employee> result = employeeDAO.getByDepartmentId(10);
        assertEquals(2, result.size());
    }

    @Test
    void testGetEmployeeById_Query() {
        Employee e = new Employee();
        when(sessionMock.get(Employee.class, 777)).thenReturn(e);
        Employee result = employeeDAO.getEmployeeById(777);
        assertEquals(e, result);
    }
}
