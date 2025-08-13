package controllers.device;

import models.device.AssetRequestItem;
import services.device.AssetRequestService;
import models.device.AssetRequest;
import models.main.Employee;
import exceptions.ValidationException;
import exceptions.NotFoundException;

import java.util.Date;
import java.util.List;

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

    public void deleteAssetRequest(int requestId, String currentUserRole) {
        assetRequestService.deleteAssetRequest(requestId, currentUserRole);
    }

    public AssetRequest getAssetRequestById(int requestId) {
        return assetRequestService.getAssetRequestById(requestId);
    }

    public List<AssetRequest> getAllAssetRequests() {
        return assetRequestService.getAllAssetRequests();
    }

}