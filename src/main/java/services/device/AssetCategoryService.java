
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

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm AssetCategory từ dữ liệu đầu vào dạng
     * String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addAssetCategoryFromInput(String name, String desc, String currentUserRole) {
        if (name == null || name.isEmpty()) {
            return "Tên danh mục không được để trống!";
        }
        AssetCategory cat = new AssetCategory();
        cat.setCategoryName(name);
        cat.setDescription(desc);
        try {
            addAssetCategory(cat, currentUserRole);
        } catch (Exception ex) {
            return "Lỗi khi thêm danh mục: " + ex.getMessage();
        }
        return null;
    }
}
