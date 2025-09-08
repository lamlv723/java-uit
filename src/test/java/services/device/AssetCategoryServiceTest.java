package services.device;

import dao.device.interfaces.AssetCategoryDAO;
import models.device.AssetCategory;
import models.main.Employee;
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
        category.setCategoryName("New Category");
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        when(assetCategoryDAOMock.findByName("New Category")).thenReturn(null);

        assetCategoryService.addAssetCategory(category, currentUser);
        verify(assetCategoryDAOMock, times(1)).addAssetCategory(category);
    }
    
    @Test
    void testAddAssetCategory_throwsOnDuplicateName() {
        AssetCategory category = new AssetCategory();
        category.setCategoryName("Duplicate");
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        when(assetCategoryDAOMock.findByName("Duplicate")).thenReturn(new AssetCategory());

        assertThrows(IllegalStateException.class, () -> {
            assetCategoryService.addAssetCategory(category, currentUser);
        });
        verify(assetCategoryDAOMock, never()).addAssetCategory(category);
    }

    @Test
    void testUpdateAssetCategory() {
        AssetCategory category = new AssetCategory();
        category.setCategoryId(1);
        category.setCategoryName("Updated");
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        when(assetCategoryDAOMock.findByName("Updated")).thenReturn(null);

        assetCategoryService.updateAssetCategory(category, currentUser);
        verify(assetCategoryDAOMock, times(1)).updateAssetCategory(category);
    }

    @Test
    void testUpdateAssetCategory_throwsOnDuplicateName() {
        AssetCategory otherCategory = new AssetCategory();
        otherCategory.setCategoryId(2);
        otherCategory.setCategoryName("Existing");

        AssetCategory categoryToUpdate = new AssetCategory();
        categoryToUpdate.setCategoryId(1);
        categoryToUpdate.setCategoryName("Existing");
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");

        when(assetCategoryDAOMock.findByName("Existing")).thenReturn(otherCategory);
        
        assertThrows(IllegalStateException.class, () -> {
            assetCategoryService.updateAssetCategory(categoryToUpdate, currentUser);
        });
        verify(assetCategoryDAOMock, never()).updateAssetCategory(categoryToUpdate);
    }

    @Test
    void testDeleteAssetCategory() {
        Employee currentUser = new Employee();
        currentUser.setRole("Admin");
        assetCategoryService.deleteAssetCategory(1, currentUser);
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