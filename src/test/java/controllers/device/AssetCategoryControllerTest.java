package controllers.device;

import models.device.AssetCategory;
import models.main.Employee;
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
        Employee currentUser = new Employee();
        assetCategoryController.addAssetCategory(category, currentUser);
        verify(assetCategoryServiceMock, times(1)).addAssetCategory(category, currentUser);
    }

    @Test
    void testUpdateAssetCategory() {
        AssetCategory category = new AssetCategory();
        Employee currentUser = new Employee();
        assetCategoryController.updateAssetCategory(category, currentUser);
        verify(assetCategoryServiceMock, times(1)).updateAssetCategory(category, currentUser);
    }

    @Test
    void testDeleteAssetCategory() {
        Employee currentUser = new Employee();
        assetCategoryController.deleteAssetCategory(1, currentUser);
        verify(assetCategoryServiceMock, times(1)).deleteAssetCategory(1, currentUser);
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
