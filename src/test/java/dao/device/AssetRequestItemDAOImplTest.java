package dao.device;

import models.device.AssetRequestItem;
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

class AssetRequestItemDAOImplTest {
    private static MockedStatic<HibernateUtil> hibernateUtilMockedStatic;
    private AssetRequestItemDAOImpl assetRequestItemDAO;
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
        assetRequestItemDAO = new AssetRequestItemDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionFactoryStaticHolder = sessionFactoryMock;
        sessionMock = mock(org.hibernate.Session.class);
        transactionMock = mock(org.hibernate.Transaction.class);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
    }

    @Test
    void testAddAssetRequestItem() {
        AssetRequestItem item = new AssetRequestItem();
        assetRequestItemDAO.addAssetRequestItem(item);
        verify(sessionMock, times(1)).save(item);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testUpdateAssetRequestItem() {
        AssetRequestItem item = new AssetRequestItem();
        assetRequestItemDAO.updateAssetRequestItem(item);
        verify(sessionMock, times(1)).update(item);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testDeleteAssetRequestItem() {
        AssetRequestItem item = new AssetRequestItem();
        when(sessionMock.get(AssetRequestItem.class, 1)).thenReturn(item);
        assetRequestItemDAO.deleteAssetRequestItem(1);
        verify(sessionMock, times(1)).delete(item);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testGetAssetRequestItemById() {
        AssetRequestItem item = new AssetRequestItem();
        when(sessionMock.get(AssetRequestItem.class, 1)).thenReturn(item);
        AssetRequestItem result = assetRequestItemDAO.getAssetRequestItemById(1);
        assertEquals(item, result);
    }

    @Test
    void testGetAllAssetRequestItems() {
        List<AssetRequestItem> items = Arrays.asList(new AssetRequestItem(), new AssetRequestItem());
        org.hibernate.query.Query queryMock = mock(org.hibernate.query.Query.class);
        when(sessionMock.createQuery("FROM AssetRequestItem", AssetRequestItem.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(items);
        List<AssetRequestItem> result = assetRequestItemDAO.getAllAssetRequestItems();
        assertEquals(2, result.size());
    }
}
