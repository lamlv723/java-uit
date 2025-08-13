package controllers.device;

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

    public void addAsset(Asset asset) {
        assetService.addAsset(asset);
    }

    public Asset getAssetById(int id) {
        return assetService.getAssetById(id);
    }

    public Asset getAssetByName(String name) {
        return assetService.getAssetByName(name);
    }

    public Asset getAssetByAssetTag(String assetTag) {
        return assetService.getAssetByAssetTag(assetTag);
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

    public void updateAssetStatus(int assetId, String status) {
    Asset asset = assetService.getAssetById(assetId);
    if (asset != null) {
        asset.setStatus(status);
        assetService.updateAsset(asset);
    }
    }

}
