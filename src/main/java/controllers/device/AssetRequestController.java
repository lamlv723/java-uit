package controllers.device;

import models.main.Employee;
import services.device.AssetRequestService;

import java.util.List;

import models.device.AssetRequest;
import models.device.AssetRequestItem;

public class AssetRequestController {
    private final AssetRequestService assetRequestService;

    public AssetRequestController() {
        this.assetRequestService = new AssetRequestService();
    }

    public AssetRequestController(AssetRequestService assetRequestService) {
        this.assetRequestService = assetRequestService;
    }

    public void addAssetRequest(AssetRequest request, Employee currentUser) {
        assetRequestService.addAssetRequest(request, currentUser);
    }

    public void updateAssetRequest(AssetRequest request, Employee currentUser) {
        assetRequestService.updateAssetRequest(request, currentUser);
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

    public List<AssetRequestItem> getItemsByRequestId(int requestId) {
        return assetRequestService.getItemsByRequestId(requestId);
    }

    // ===== Additional pass-through methods (hide service from Views) =====
    public String createRequestWithItems(int employeeId, String requestType, List<Integer> assetIds) {
        return assetRequestService.createRequestWithItems(employeeId, requestType, assetIds);
    }

    public String approveBorrowRequest(int requestId, Employee currentUser) {
        return assetRequestService.approveBorrowRequest(requestId, currentUser);
    }

    public String approveReturnRequest(int requestId, Employee currentUser) {
        return assetRequestService.approveReturnRequest(requestId, currentUser);
    }

    public String rejectRequest(int requestId, Employee currentUser) {
        return assetRequestService.rejectRequest(requestId, currentUser);
    }

    public List<AssetRequestItem> getActiveBorrowItemsByAssetId(int assetId) {
        return assetRequestService.getItemsReferencingAsset(assetId);
    }
}