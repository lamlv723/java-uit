package dao.device;

import models.device.AssetRequest;
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

class AssetRequestDAOImplTest {
    private static MockedStatic<HibernateUtil> hibernateUtilMockedStatic;
    private AssetRequestDAOImpl assetRequestDAO;
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
        assetRequestDAO = new AssetRequestDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionMock = mock(org.hibernate.Session.class);
        transactionMock = mock(org.hibernate.Transaction.class);
        hibernateUtilMockedStatic.when(HibernateUtil::getSessionFactory).thenReturn(sessionFactoryMock);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
    }

    @Test
    void testAddAssetRequest() {
        AssetRequest request = new AssetRequest();
        assetRequestDAO.addAssetRequest(request);
        verify(sessionMock, times(1)).save(request);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testUpdateAssetRequest() {
        AssetRequest request = new AssetRequest();
        assetRequestDAO.updateAssetRequest(request);
        verify(sessionMock, times(1)).update(request);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testDeleteAssetRequest() {
        AssetRequest request = new AssetRequest();
        when(sessionMock.get(AssetRequest.class, 1)).thenReturn(request);
        assetRequestDAO.deleteAssetRequest(1);
        verify(sessionMock, times(1)).delete(request);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testGetAssetRequestById() {
        AssetRequest request = new AssetRequest();
        when(sessionMock.get(AssetRequest.class, 1)).thenReturn(request);
        AssetRequest result = assetRequestDAO.getAssetRequestById(1);
        assertEquals(request, result);
    }

    @Test
    void testGetAllAssetRequests() {
        List<AssetRequest> requests = Arrays.asList(new AssetRequest(), new AssetRequest());
        org.hibernate.query.Query queryMock = mock(org.hibernate.query.Query.class);
        when(sessionMock.createQuery("FROM AssetRequest", AssetRequest.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(requests);
        List<AssetRequest> result = assetRequestDAO.getAllAssetRequests();
        assertEquals(2, result.size());
    }
}
