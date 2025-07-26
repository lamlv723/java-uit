
package services.device;

import dao.device.AssetRequestDAOImpl;
import dao.device.interfaces.AssetRequestDAO;
import models.device.AssetRequest;
import java.util.List;

public class AssetRequestService {
    private AssetRequestDAO assetRequestDAO;

    public AssetRequestService() {
        this.assetRequestDAO = new AssetRequestDAOImpl();
    }

    public void addAssetRequest(AssetRequest request, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestDAO.addAssetRequest(request);
    }

    public void updateAssetRequest(AssetRequest request, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestDAO.updateAssetRequest(request);
    }

    public void deleteAssetRequest(int requestId, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestDAO.deleteAssetRequest(requestId);
    }

    public AssetRequest getAssetRequestById(int requestId) {
        return assetRequestDAO.getAssetRequestById(requestId);
    }

    public List<AssetRequest> getAllAssetRequests() {
        return assetRequestDAO.getAllAssetRequests();
    }
}
