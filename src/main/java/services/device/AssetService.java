
package services.device;

import dao.device.AssetDAOImpl;
import models.device.Asset;

import java.util.List;

public class AssetService {
    private AssetDAOImpl assetDAO = new AssetDAOImpl();

    public void addAsset(Asset asset) {
        assetDAO.save(asset);
    }

    public Asset getAssetById(int id) {
        return assetDAO.getById(id);
    }

    public List<Asset> getAllAssets() {
        return assetDAO.getAll();
    }

    public void updateAsset(Asset asset) {
        assetDAO.update(asset);
    }

    public void deleteAsset(Asset asset) {
        assetDAO.delete(asset);
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm Asset từ dữ liệu đầu vào dạng String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addAssetFromInput(String name, String desc) {
        if (name == null || name.isEmpty()) {
            return "Tên tài sản không được để trống!";
        }
        Asset asset = new Asset();
        asset.setName(name);
        asset.setDescription(desc);
        try {
            addAsset(asset);
        } catch (Exception ex) {
            return "Lỗi khi thêm tài sản: " + ex.getMessage();
        }
        return null;
    }
}
