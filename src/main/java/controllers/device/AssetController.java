package controllers.device;

import services.device.AssetService;

import java.util.List;

import models.device.Asset;

public class AssetController {
    private AssetService assetService = new AssetService();

    public void createAsset(Asset asset) {
        assetService.addAsset(asset);
    }

    public Asset getAsset(int id) {
        return assetService.getAssetById(id);
    }

    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    public void updateAsset(Asset asset) {
        assetService.updateAsset(asset);
    }

    public void deleteAsset(Asset asset) {
        assetService.deleteAsset(asset);
    }
}
