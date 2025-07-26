package dao.main;

import models.main.Department;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import config.HibernateUtil;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

class DepartmentDAOImplTest {
    private static MockedStatic<HibernateUtil> hibernateUtilMockedStatic;
    private DepartmentDAOImpl departmentDAO;
    private SessionFactory sessionFactoryMock;
    private org.hibernate.Session sessionMock;
    private org.hibernate.Transaction transactionMock;

    @BeforeAll
    static void beforeAll() {
        hibernateUtilMockedStatic = Mockito.mockStatic(HibernateUtil.class);
    }

    @AfterAll
    static void afterAll() {
        hibernateUtilMockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        departmentDAO = new DepartmentDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionMock = mock(org.hibernate.Session.class);
        transactionMock = mock(org.hibernate.Transaction.class);
        hibernateUtilMockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(sessionFactoryMock);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
    }

    @Test
    void testAddDepartment() {
        Department department = new Department();
        departmentDAO.addDepartment(department);
        verify(sessionMock, times(1)).save(department);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testUpdateDepartment() {
        Department department = new Department();
        departmentDAO.updateDepartment(department);
        verify(sessionMock, times(1)).update(department);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testDeleteDepartment() {
        Department department = new Department();
        when(sessionMock.get(Department.class, 1)).thenReturn(department);
        departmentDAO.deleteDepartment(1);
        verify(sessionMock, times(1)).delete(department);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testGetDepartmentById() {
        Department department = new Department();
        when(sessionMock.get(Department.class, 1)).thenReturn(department);
        Department result = departmentDAO.getDepartmentById(1);
        assertEquals(department, result);
    }

    @Test
    void testGetAllDepartments() {
        List<Department> departments = Arrays.asList(new Department(), new Department());
        org.hibernate.query.Query queryMock = mock(org.hibernate.query.Query.class);
        when(sessionMock.createQuery("FROM Department", Department.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(departments);
        List<Department> result = departmentDAO.getAllDepartments();
        assertEquals(2, result.size());
    }
}
