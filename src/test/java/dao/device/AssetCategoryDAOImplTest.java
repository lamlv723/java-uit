package dao.device;

import models.device.AssetCategory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import config.HibernateUtil;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetCategoryDAOImplTest {
    private static MockedStatic<HibernateUtil> hibernateUtilMockedStatic;
    private AssetCategoryDAOImpl assetCategoryDAO;
    private SessionFactory sessionFactoryMock;
    private org.hibernate.Session sessionMock;
    private org.hibernate.Transaction transactionMock;

    @BeforeAll
    static void beforeAll() {
        hibernateUtilMockedStatic = Mockito.mockStatic(HibernateUtil.class);
        // Stub getSessionFactory for all tests
        hibernateUtilMockedStatic.when(HibernateUtil::getSessionFactory)
                .thenAnswer(invocation -> sessionFactoryStaticHolder);
    }

    @AfterAll
    static void afterAll() {
        hibernateUtilMockedStatic.close();
    }

    // Static holder for sessionFactoryMock to be used in static lambda
    private static SessionFactory sessionFactoryStaticHolder;

    @BeforeEach
    void setUp() {
        assetCategoryDAO = new AssetCategoryDAOImpl();
        sessionFactoryMock = mock(SessionFactory.class);
        sessionFactoryStaticHolder = sessionFactoryMock;
        sessionMock = mock(org.hibernate.Session.class);
        transactionMock = mock(org.hibernate.Transaction.class);

        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.beginTransaction()).thenReturn(transactionMock);
    }

    @Test
    void testAddAssetCategory() {
        AssetCategory category = new AssetCategory();
        assetCategoryDAO.addAssetCategory(category);
        verify(sessionMock, times(1)).save(category);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testUpdateAssetCategory() {
        AssetCategory category = new AssetCategory();
        assetCategoryDAO.updateAssetCategory(category);
        verify(sessionMock, times(1)).update(category);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testDeleteAssetCategory() {
        AssetCategory category = new AssetCategory();
        when(sessionMock.get(AssetCategory.class, 1)).thenReturn(category);
        assetCategoryDAO.deleteAssetCategory(1);
        verify(sessionMock, times(1)).delete(category);
        verify(transactionMock, times(1)).commit();
    }

    @Test
    void testGetAssetCategoryById() {
        AssetCategory category = new AssetCategory();
        when(sessionMock.get(AssetCategory.class, 1)).thenReturn(category);
        AssetCategory result = assetCategoryDAO.getAssetCategoryById(1);
        assertEquals(category, result);
    }

    @Test
    void testGetAllAssetCategories() {
        List<AssetCategory> categories = Arrays.asList(new AssetCategory(), new AssetCategory());
        org.hibernate.query.Query<AssetCategory> queryMock = mock(org.hibernate.query.Query.class);
        when(sessionMock.createQuery("FROM AssetCategory", AssetCategory.class)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(categories);
        List<AssetCategory> result = assetCategoryDAO.getAllAssetCategories();
        assertEquals(2, result.size());
    }
}
