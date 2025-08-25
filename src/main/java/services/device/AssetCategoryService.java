
package services.device;

import dao.device.AssetCategoryDAOImpl;
import dao.device.interfaces.AssetCategoryDAO;
import models.device.AssetCategory;
import java.util.List;

import models.main.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetCategoryService {
    private AssetCategoryDAO assetCategoryDAO;
    private static final Logger logger = LoggerFactory.getLogger(AssetCategoryService.class);

    public AssetCategoryService() {
        this.assetCategoryDAO = new AssetCategoryDAOImpl();
    }

    public void addAssetCategory(AssetCategory category, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to add an asset category.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        assetCategoryDAO.addAssetCategory(category);
    }

    public void updateAssetCategory(AssetCategory category, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to update an asset category.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        assetCategoryDAO.updateAssetCategory(category);
    }

    public void deleteAssetCategory(int categoryId, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (!"Admin".equalsIgnoreCase(currentUserRole)) {
            String errorMessage = "Authorization Error: User with role " + currentUserRole + " attempted to delete asset category with id " + categoryId + ".";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
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
    public String addAssetCategoryFromInput(String name, String desc, Employee currentUser) {
        if (name == null || name.isEmpty()) {
            return "Tên danh mục không được để trống!";
        }
        AssetCategory cat = new AssetCategory();
        cat.setCategoryName(name);
        cat.setDescription(desc);

        // Error will propagate and be caught in view layer
        addAssetCategory(cat, currentUser);

        return null;
    }
}
