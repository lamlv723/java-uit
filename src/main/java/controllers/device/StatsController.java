package controllers.device;

import services.device.AssetService;

public class StatsController {
    private final AssetService assetService;

    public StatsController(AssetService assetService) {
        this.assetService = assetService;
    }

    public long totalAssets() {
        return assetService.countTotalAssets();
    }

    public long availableAssets() {
        return assetService.countAvailableAssets();
    }

    public long inUseAssets() {
        return assetService.countInUseAssets();
    }
}