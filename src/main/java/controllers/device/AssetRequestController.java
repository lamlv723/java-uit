package controllers.device;

import models.main.Employee;
import services.device.AssetRequestService;

import java.util.List;

import models.device.AssetRequest;

public class AssetRequestController {
    private AssetRequestService assetRequestService;

    public AssetRequestController(AssetRequestService assetRequestService) {
        this.assetRequestService = assetRequestService;
    }

    public AssetRequestService getAssetRequestService() {
        return assetRequestService;
    }

    public void addAssetRequest(AssetRequest request, String currentUserRole) {
        assetRequestService.addAssetRequest(request, currentUserRole);
    }

    public void updateAssetRequest(AssetRequest request, String currentUserRole) {
        assetRequestService.updateAssetRequest(request, currentUserRole);
    }

    public String deleteAssetRequest(int requestId, Employee currentUser) {
        return assetRequestService.deleteAssetRequest(requestId, currentUser);
    }

    public AssetRequest getAssetRequestById(int requestId) {
        return assetRequestService.getAssetRequestById(requestId);
    }

    public List<AssetRequest> getAllAssetRequests(Employee currentUser) {
        return assetRequestService.getAllAssetRequests(currentUser);
    }

    public String updateRequestWithItems(int requestId, List<Integer> assetIds, Employee currentUser) {
        return assetRequestService.updateRequestWithItems(requestId, assetIds, currentUser);
    }
}