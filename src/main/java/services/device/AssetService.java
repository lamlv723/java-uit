package services.device;

import dao.device.AssetDAOImpl;
import models.device.Asset;
import models.main.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.device.interfaces.AssetDAO;

import java.util.List;

public class AssetService {
    private AssetDAO assetDAO = new AssetDAOImpl();
    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);

    public void addAsset(Asset asset, Employee currentUser) {
        if (!"Admin".equalsIgnoreCase(currentUser.getRole())) {
            logger.warn("Authorization Error: User {} attempted to add an asset.", currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        // Kiểm tra tên tài sản đã tồn tại chưa
        if (assetDAO.findByName(asset.getAssetName()) != null) {
            throw new IllegalStateException("Tên tài sản đã tồn tại.");
        }
        assetDAO.save(asset);
    }

    public Asset getAssetById(int id) {
        return assetDAO.getById(id);
    }

    public List<Asset> getAllAssets() {
        return assetDAO.getAll();
    }

    public void updateAsset(Asset asset, Employee currentUser) {
        if (!"Admin".equalsIgnoreCase(currentUser.getRole())) {
            logger.warn("Authorization Error: User {} attempted to update an asset.", currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        // Kiểm tra tên tài sản đã tồn tại và không phải là chính nó
        Asset existingAsset = assetDAO.findByName(asset.getAssetName());
        if (existingAsset != null && !existingAsset.getAssetId().equals(asset.getAssetId())) {
            throw new IllegalStateException("Tên tài sản đã tồn tại.");
        }
        assetDAO.update(asset);
    }

    public void deleteAsset(Asset asset, Employee currentUser) {
        if (!"Admin".equalsIgnoreCase(currentUser.getRole())) {
            logger.warn("Authorization Error: User {} attempted to delete an asset.", currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền thực hiện hành động này.");
        }
        if (asset != null && "Borrowed".equalsIgnoreCase(asset.getStatus())) {
            throw new IllegalStateException("Không thể xóa tài sản đang được sử dụng (Borrowed/In Use).");
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

    // ===== Statistics =====
    public long countTotalAssets() {
        return assetDAO.countAll();
    }

    public long countAvailableAssets() {
        return assetDAO.countByStatus("Available");
    }

    public long countInUseAssets() {
        // In legacy code status used 'Borrowed'; treat as In Use
        long borrowed = assetDAO.countByStatus("Borrowed");
        long inUse = assetDAO.countByStatus("In Use");
        return borrowed + inUse;
    }
}