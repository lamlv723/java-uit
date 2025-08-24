package dao.device;

import models.device.AssetRequest;
import models.main.Employee;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import config.HibernateUtil;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetRequestDAOImplTest {
    private AssetRequestDAOImpl assetRequestDAO;
    private SessionFactory sessionFactoryMock;
    private org.hibernate.Session sessionMock;
    private org.hibernate.Transaction transactionMock;

    @BeforeEach
    void setUp() {
        assetRequestDAO = new AssetRequestDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionMock = mock(org.hibernate.Session.class);
        transactionMock = mock(org.hibernate.Transaction.class);
        HibernateUtil.setSessionFactory(sessionFactoryMock);
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
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        List<AssetRequest> requests = Arrays.asList(new AssetRequest(), new AssetRequest());
        org.hibernate.query.Query queryMock = mock(org.hibernate.query.Query.class);
        when(sessionMock.createQuery("FROM AssetRequest", AssetRequest.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(requests);
        List<AssetRequest> result = assetRequestDAO.getAllAssetRequests(currentUser);
        assertEquals(2, result.size());
    }
}
