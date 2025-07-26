package dao.device;

import models.device.Vendor;
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

class VendorDAOImplTest {
    private static MockedStatic<HibernateUtil> hibernateUtilMockedStatic;
    private VendorDAOImpl vendorDAO;
    private SessionFactory sessionFactoryMock;
    private org.hibernate.Session sessionMock;
    private org.hibernate.Transaction transactionMock;

    // Static holder for sessionFactoryMock to be used in static lambda
    private static SessionFactory sessionFactoryStaticHolder;

    @BeforeAll
    static void beforeAll() {
        hibernateUtilMockedStatic = Mockito.mockStatic(HibernateUtil.class);
        hibernateUtilMockedStatic.when(HibernateUtil::getSessionFactory)
                .thenAnswer(invocation -> sessionFactoryStaticHolder);
    }

    @AfterAll
    static void afterAll() {
        hibernateUtilMockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        vendorDAO = new VendorDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionFactoryStaticHolder = sessionFactoryMock;
        sessionMock = mock(org.hibernate.Session.class);
        transactionMock = mock(org.hibernate.Transaction.class);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
    }

    @Test
    void testAddVendor() {
        Vendor vendor = new Vendor();
        vendorDAO.addVendor(vendor);
        verify(sessionMock, times(1)).save(vendor);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testUpdateVendor() {
        Vendor vendor = new Vendor();
        vendorDAO.updateVendor(vendor);
        verify(sessionMock, times(1)).update(vendor);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testDeleteVendor() {
        Vendor vendor = new Vendor();
        when(sessionMock.get(Vendor.class, 1)).thenReturn(vendor);
        vendorDAO.deleteVendor(1);
        verify(sessionMock, times(1)).delete(vendor);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testGetVendorById() {
        Vendor vendor = new Vendor();
        when(sessionMock.get(Vendor.class, 1)).thenReturn(vendor);
        Vendor result = vendorDAO.getVendorById(1);
        assertEquals(vendor, result);
    }

    @Test
    void testGetAllVendors() {
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor());
        org.hibernate.query.Query queryMock = mock(org.hibernate.query.Query.class);
        when(sessionMock.createQuery("FROM Vendor", Vendor.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(vendors);
        List<Vendor> result = vendorDAO.getAllVendors();
        assertEquals(2, result.size());
    }
}
