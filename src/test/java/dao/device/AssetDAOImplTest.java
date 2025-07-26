package dao.device;

import models.device.Asset;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import config.HibernateUtil;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetDAOImplTest {
    private static MockedStatic<HibernateUtil> hibernateUtilMockedStatic;
    private AssetDAOImpl assetDAO;
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
        assetDAO = new AssetDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionMock = mock(org.hibernate.Session.class);
        transactionMock = mock(org.hibernate.Transaction.class);
        hibernateUtilMockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(sessionFactoryMock);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
    }

    @Test
    void testSave() {
        Asset asset = new Asset();
        assetDAO.save(asset);
        verify(sessionMock, times(1)).save(asset);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testGetById() {
        Asset asset = new Asset();
        when(sessionMock.get(Asset.class, 1)).thenReturn(asset);
        Asset result = assetDAO.getById(1);
        assertEquals(asset, result);
    }

    @Test
    void testGetAll() {
        List<Asset> assets = Arrays.asList(new Asset(), new Asset());
        org.hibernate.query.Query queryMock = mock(org.hibernate.query.Query.class);
        when(sessionMock.createQuery("from Asset", Asset.class)).thenReturn(queryMock);
        when(queryMock.list()).thenReturn(assets);
        List<Asset> result = assetDAO.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void testUpdate() {
        Asset asset = new Asset();
        assetDAO.update(asset);
        verify(sessionMock, times(1)).update(asset);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testDelete() {
        Asset asset = new Asset();
        assetDAO.delete(asset);
        verify(sessionMock, times(1)).delete(asset);
        verify(transactionMock, times(1)).commit();
    }
}
