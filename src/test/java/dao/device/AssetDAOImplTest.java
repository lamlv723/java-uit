package dao.device;

import models.device.Asset;
import models.device.AssetCategory;

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
        assetDAO = new AssetDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionFactoryStaticHolder = sessionFactoryMock;
        sessionMock = mock(org.hibernate.Session.class);
        transactionMock = mock(org.hibernate.Transaction.class);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
    }

    @Test
    void testSave() {
        Asset asset = new Asset();

        // Thêm AssetCategory giả để tránh lỗi IllegalArgument
        AssetCategory mockCategory = new AssetCategory();
        mockCategory.setCategoryId(1); // ID giả định
        when(sessionMock.get(AssetCategory.class, 1)).thenReturn(mockCategory);
        asset.setCategory(mockCategory);

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

        // Mock đúng query với ORDER BY
        when(sessionMock.createQuery("FROM Asset ORDER BY assetTag ASC", Asset.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(assets);

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