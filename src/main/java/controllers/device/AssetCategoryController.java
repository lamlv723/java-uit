package controllers.device;

import models.main.Employee;
import services.device.AssetCategoryService;

import java.util.List;

import models.device.AssetCategory;

public class AssetCategoryController {
    private final AssetCategoryService assetCategoryService;

    public AssetCategoryController() {
        this.assetCategoryService = new AssetCategoryService();
    }

    public AssetCategoryController(AssetCategoryService assetCategoryService) {
        this.assetCategoryService = assetCategoryService;
    }

    public void addAssetCategory(AssetCategory category, Employee currentUser) {
        assetCategoryService.addAssetCategory(category, currentUser);
    }

    public void updateAssetCategory(AssetCategory category, Employee currentUser) {
        assetCategoryService.updateAssetCategory(category, currentUser);
    }

    public void deleteAssetCategory(int categoryId, Employee currentUser) {
        assetCategoryService.deleteAssetCategory(categoryId, currentUser);
    }

    public AssetCategory getAssetCategoryById(int categoryId) {
        return assetCategoryService.getAssetCategoryById(categoryId);
    }

    public List<AssetCategory> getAllAssetCategories() {
        return assetCategoryService.getAllAssetCategories();
    }
}
