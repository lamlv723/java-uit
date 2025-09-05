package controllers.device;

import models.main.Employee;
import services.device.AssetService;

import java.util.List;

import models.device.Asset;

public class AssetController {
    private final AssetService assetService;

    public AssetController() {
        this.assetService = new AssetService();
    }

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
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

    public List<Asset> getAllAvailableAssets() {
        return assetService.getAllAvailableAssets();
    }

    public List<Asset> getBorrowedAssetsByEmployeeId(int employeeId) {
        return assetService.getBorrowedAssetsByEmployeeId(employeeId);
    }

    public void updateAsset(Asset asset, Employee currentUser) {
        assetService.updateAsset(asset, currentUser);
    }

    public void deleteAsset(Asset asset, Employee currentUser) {
        assetService.deleteAsset(asset, currentUser);
    }
}
