package controllers.device;

import models.device.AssetCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.device.AssetCategoryService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetCategoryControllerTest {
    private AssetCategoryController assetCategoryController;
    private AssetCategoryService assetCategoryServiceMock;

    @BeforeEach
    void setUp() {
        assetCategoryServiceMock = mock(AssetCategoryService.class);
        assetCategoryController = new AssetCategoryController(assetCategoryServiceMock);
    }

    @Test
    void testAddAssetCategory() {
        AssetCategory category = new AssetCategory();
        assetCategoryController.addAssetCategory(category, "ADMIN");
        verify(assetCategoryServiceMock, times(1)).addAssetCategory(category, "ADMIN");
    }

    @Test
    void testUpdateAssetCategory() {
        AssetCategory category = new AssetCategory();
        assetCategoryController.updateAssetCategory(category, "ADMIN");
        verify(assetCategoryServiceMock, times(1)).updateAssetCategory(category, "ADMIN");
    }

    @Test
    void testDeleteAssetCategory() {
        assetCategoryController.deleteAssetCategory(1, "ADMIN");
        verify(assetCategoryServiceMock, times(1)).deleteAssetCategory(1, "ADMIN");
    }

    @Test
    void testGetAssetCategoryById() {
        AssetCategory category = new AssetCategory();
        when(assetCategoryServiceMock.getAssetCategoryById(1)).thenReturn(category);
        AssetCategory result = assetCategoryController.getAssetCategoryById(1);
        assertEquals(category, result);
    }

    @Test
    void testGetAllAssetCategories() {
        List<AssetCategory> categories = Arrays.asList(new AssetCategory(), new AssetCategory());
        when(assetCategoryServiceMock.getAllAssetCategories()).thenReturn(categories);
        List<AssetCategory> result = assetCategoryController.getAllAssetCategories();
        assertEquals(2, result.size());
    }
}
