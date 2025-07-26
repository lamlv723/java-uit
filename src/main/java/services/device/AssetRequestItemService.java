package services.device;

import dao.device.AssetRequestItemDAOImpl;
import dao.device.interfaces.AssetRequestItemDAO;
import models.device.AssetRequestItem;
import java.util.List;

public class AssetRequestItemService {
    private AssetRequestItemDAO assetRequestItemDAO;

    public AssetRequestItemService() {
        this.assetRequestItemDAO = new AssetRequestItemDAOImpl();
    }

    public void addAssetRequestItem(AssetRequestItem item, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestItemDAO.addAssetRequestItem(item);
    }

    public void updateAssetRequestItem(AssetRequestItem item, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestItemDAO.updateAssetRequestItem(item);
    }

    public void deleteAssetRequestItem(int id, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestItemDAO.deleteAssetRequestItem(id);
    }

    public AssetRequestItem getAssetRequestItemById(int id) {
        return assetRequestItemDAO.getAssetRequestItemById(id);
    }

    public List<AssetRequestItem> getAllAssetRequestItems() {
        return assetRequestItemDAO.getAllAssetRequestItems();
    }
}
