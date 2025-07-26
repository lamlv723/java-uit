package controllers.device;

import models.Asset;
import services.AssetService;
import java.util.List;

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
