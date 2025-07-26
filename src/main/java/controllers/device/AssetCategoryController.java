package controllers.device;

import services.device.AssetCategoryService;

import java.util.List;

import models.device.AssetCategory;

public class AssetCategoryController {
    private AssetCategoryService assetCategoryService;

    public AssetCategoryController(AssetCategoryService assetCategoryService) {
        this.assetCategoryService = assetCategoryService;
    }

    public void addAssetCategory(AssetCategory category, String currentUserRole) {
        assetCategoryService.addAssetCategory(category, currentUserRole);
    }

    public void updateAssetCategory(AssetCategory category, String currentUserRole) {
        assetCategoryService.updateAssetCategory(category, currentUserRole);
    }

    public void deleteAssetCategory(int categoryId, String currentUserRole) {
        assetCategoryService.deleteAssetCategory(categoryId, currentUserRole);
    }

    public AssetCategory getAssetCategoryById(int categoryId) {
        return assetCategoryService.getAssetCategoryById(categoryId);
    }

    public List<AssetCategory> getAllAssetCategories() {
        return assetCategoryService.getAllAssetCategories();
    }
}
