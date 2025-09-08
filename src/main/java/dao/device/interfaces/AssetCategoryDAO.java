package dao.device.interfaces;

import models.device.AssetCategory;
import java.util.List;

public interface AssetCategoryDAO {
    void addAssetCategory(AssetCategory category);

    void updateAssetCategory(AssetCategory category);

    void deleteAssetCategory(int categoryId);

    AssetCategory getAssetCategoryById(int categoryId);

    List<AssetCategory> getAllAssetCategories();

    AssetCategory findByName(String name);
}
