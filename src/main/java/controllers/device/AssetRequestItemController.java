package controllers.device;

import services.device.AssetRequestItemService;

import java.util.List;

import models.device.AssetRequestItem;

public class AssetRequestItemController {
    private AssetRequestItemService assetRequestItemService;

    public AssetRequestItemController(AssetRequestItemService assetRequestItemService) {
        this.assetRequestItemService = assetRequestItemService;
    }

    public AssetRequestItemService getAssetRequestItemService() {
        return assetRequestItemService;
    }

    public void addAssetRequestItem(AssetRequestItem item, String currentUserRole) {
        assetRequestItemService.addAssetRequestItem(item, currentUserRole);
    }

    public void updateAssetRequestItem(AssetRequestItem item, String currentUserRole) {
        assetRequestItemService.updateAssetRequestItem(item, currentUserRole);
    }

    public void deleteAssetRequestItem(int requestItemId, String currentUserRole) {
        assetRequestItemService.deleteAssetRequestItem(requestItemId, currentUserRole);
    }

    public AssetRequestItem getAssetRequestItemById(int requestItemId) {
        return assetRequestItemService.getAssetRequestItemById(requestItemId);
    }

    public List<AssetRequestItem> getAllAssetRequestItems() {
        return assetRequestItemService.getAllAssetRequestItems();
    }

    public List<AssetRequestItem> getAssetRequestItemsByRequestId(int requestId) {
        return assetRequestItemService.getAssetRequestItemsByRequestId(requestId);
    }
}
