package dao.device;

import config.HibernateUtil;
import models.device.AssetRequest;
import models.main.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssetRequestDAOImplTest {
    private AssetRequestDAOImpl assetRequestDAO;
    private static MockedStatic<HibernateUtil> hibernateUtilMockedStatic;
    private SessionFactory sessionFactoryMock;
    private Session sessionMock;
    private Transaction transactionMock;
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
        assetRequestDAO = new AssetRequestDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionFactoryStaticHolder = sessionFactoryMock; // Assign to static holder
        sessionMock = mock(Session.class);
        transactionMock = mock(Transaction.class);
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
        Query<AssetRequest> queryMock = mock(Query.class);
        // Corrected HQL query to match implementation
        when(sessionMock.createQuery("FROM AssetRequest ar ORDER BY ar.requestId ASC", AssetRequest.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(requests);
        List<AssetRequest> result = assetRequestDAO.getAllAssetRequests(currentUser);
        assertEquals(2, result.size());
    }
}