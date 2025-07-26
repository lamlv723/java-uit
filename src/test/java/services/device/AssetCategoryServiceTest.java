package services.device;

import dao.device.interfaces.AssetCategoryDAO;
import models.device.AssetCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetCategoryServiceTest {
    private AssetCategoryService assetCategoryService;
    private AssetCategoryDAO assetCategoryDAOMock;

    @BeforeEach
    void setUp() {
        assetCategoryDAOMock = Mockito.mock(AssetCategoryDAO.class);
        assetCategoryService = new AssetCategoryService();
        // Inject mock DAO
        java.lang.reflect.Field daoField;
        try {
            daoField = AssetCategoryService.class.getDeclaredField("assetCategoryDAO");
            daoField.setAccessible(true);
            daoField.set(assetCategoryService, assetCategoryDAOMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddAssetCategory() {
        AssetCategory category = new AssetCategory();
        assetCategoryService.addAssetCategory(category, "ADMIN");
        verify(assetCategoryDAOMock, times(1)).addAssetCategory(category);
    }

    @Test
    void testUpdateAssetCategory() {
        AssetCategory category = new AssetCategory();
        assetCategoryService.updateAssetCategory(category, "ADMIN");
        verify(assetCategoryDAOMock, times(1)).updateAssetCategory(category);
    }

    @Test
    void testDeleteAssetCategory() {
        assetCategoryService.deleteAssetCategory(1, "ADMIN");
        verify(assetCategoryDAOMock, times(1)).deleteAssetCategory(1);
    }

    @Test
    void testGetAssetCategoryById() {
        AssetCategory category = new AssetCategory();
        when(assetCategoryDAOMock.getAssetCategoryById(1)).thenReturn(category);
        AssetCategory result = assetCategoryService.getAssetCategoryById(1);
        assertEquals(category, result);
    }

    @Test
    void testGetAllAssetCategories() {
        List<AssetCategory> categories = Arrays.asList(new AssetCategory(), new AssetCategory());
        when(assetCategoryDAOMock.getAllAssetCategories()).thenReturn(categories);
        List<AssetCategory> result = assetCategoryService.getAllAssetCategories();
        assertEquals(2, result.size());
    }
}
