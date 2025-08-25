package controllers.device;

import models.main.Employee;
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

    public void addAssetRequestItem(AssetRequestItem item, Employee currentUser) {
        assetRequestItemService.addAssetRequestItem(item, currentUser);
    }

    public void updateAssetRequestItem(AssetRequestItem item, Employee currentUser) {
        assetRequestItemService.updateAssetRequestItem(item, currentUser);
    }

    public void deleteAssetRequestItem(int requestItemId, Employee currentUser) {
        assetRequestItemService.deleteAssetRequestItem(requestItemId, currentUser);
    }

    public AssetRequestItem getAssetRequestItemById(int requestItemId) {
        return assetRequestItemService.getAssetRequestItemById(requestItemId);
    }

    public List<AssetRequestItem> getAllAssetRequestItems() {
        return assetRequestItemService.getAllAssetRequestItems();
    }

    public List<AssetRequestItem> getFilteredRequestItems(Employee currentUser){
        return assetRequestItemService.getFilteredRequestItems(currentUser);
    }
}
