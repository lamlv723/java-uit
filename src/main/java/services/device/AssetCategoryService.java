package services.device;

import java.util.List;

import models.device.AssetCategory;

public interface AssetCategoryService {
    void addAssetCategory(AssetCategory category, String currentUserRole);

    void updateAssetCategory(AssetCategory category, String currentUserRole);

    void deleteAssetCategory(int categoryId, String currentUserRole);

    AssetCategory getAssetCategoryById(int categoryId);

    List<AssetCategory> getAllAssetCategories();
}
