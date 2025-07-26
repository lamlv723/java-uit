package services.device;

import dao.device.AssetCategoryDAOImpl;
import dao.device.interfaces.AssetCategoryDAO;
import models.device.AssetCategory;
import java.util.List;

public class AssetCategoryService {
    private AssetCategoryDAO assetCategoryDAO;

    public AssetCategoryService() {
        this.assetCategoryDAO = new AssetCategoryDAOImpl();
    }

    public void addAssetCategory(AssetCategory category, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetCategoryDAO.addAssetCategory(category);
    }

    public void updateAssetCategory(AssetCategory category, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetCategoryDAO.updateAssetCategory(category);
    }

    public void deleteAssetCategory(int categoryId, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetCategoryDAO.deleteAssetCategory(categoryId);
    }

    public AssetCategory getAssetCategoryById(int categoryId) {
        return assetCategoryDAO.getAssetCategoryById(categoryId);
    }

    public List<AssetCategory> getAllAssetCategories() {
        return assetCategoryDAO.getAllAssetCategories();
    }
}
