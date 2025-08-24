
package services.device;

import dao.device.AssetDAOImpl;
import models.device.Asset;
import models.main.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AssetService {
    private AssetDAOImpl assetDAO = new AssetDAOImpl();
    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);

    public void addAsset(Asset asset, Employee currentUser) { // Thay đổi ở đây
        if (!"Admin".equalsIgnoreCase(currentUser.getRole())) { // Lấy role từ currentUser
            logger.warn("Authorization Error: User {} attempted to add an asset.", currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        assetDAO.save(asset);
    }

    public Asset getAssetById(int id) {
        return assetDAO.getById(id);
    }

    public List<Asset> getAllAssets() {
        return assetDAO.getAll();
    }

    public void updateAsset(Asset asset, Employee currentUser) { // Thay đổi ở đây
        if (!"Admin".equalsIgnoreCase(currentUser.getRole())) {
            logger.warn("Authorization Error: User {} attempted to update an asset.", currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        assetDAO.update(asset);
    }

    public void deleteAsset(Asset asset, Employee currentUser) { // Thay đổi ở đây
        if (!"Admin".equalsIgnoreCase(currentUser.getRole())) {
            logger.warn("Authorization Error: User {} attempted to delete an asset.", currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        assetDAO.delete(asset);
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm Asset từ dữ liệu đầu vào dạng String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addAssetFromInput(String name, String desc, Employee currentUser) {
        if (name == null || name.isEmpty()) {
            return "Tên tài sản không được để trống!";
        }
        Asset asset = new Asset();
        asset.setName(name);
        asset.setDescription(desc);

        addAsset(asset, currentUser);

        return null;
    }

    public List<Asset> getAllAvailableAssets() {
        return assetDAO.getAllAvailableAssets();
    }

    public List<Asset> getBorrowedAssetsByEmployeeId(int employeeId) {
        return assetDAO.getBorrowedAssetsByEmployeeId(employeeId);
    }
}
