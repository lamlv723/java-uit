package controllers.device;

import models.main.Employee;
import services.device.AssetService;

import java.util.List;

import models.device.Asset;

public class AssetController {
    private AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    public AssetService getAssetService() {
        return assetService;
    }

    public void addAsset(Asset asset, Employee currentUser) {
        assetService.addAsset(asset, currentUser);
    }

    public Asset getAssetById(int id) {
        return assetService.getAssetById(id);
    }

    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    public void updateAsset(Asset asset, Employee currentUser) {
        assetService.updateAsset(asset, currentUser);
    }

    public void deleteAsset(Asset asset, Employee currentUser) {
        assetService.deleteAsset(asset, currentUser);
    }
}
