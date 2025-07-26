package services.device;

import dao.device.AssetDAOImpl;
import models.device.Asset;

import java.util.List;

public class AssetService {
    private AssetDAOImpl assetDAO = new AssetDAOImpl();

    public void addAsset(Asset asset) {
        assetDAO.save(asset);
    }

    public Asset getAssetById(int id) {
        return assetDAO.getById(id);
    }

    public List<Asset> getAllAssets() {
        return assetDAO.getAll();
    }

    public void updateAsset(Asset asset) {
        assetDAO.update(asset);
    }

    public void deleteAsset(Asset asset) {
        assetDAO.delete(asset);
    }
}
