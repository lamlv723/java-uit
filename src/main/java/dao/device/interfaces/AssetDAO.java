package dao.device.interfaces;

import models.device.Asset;
import java.util.List;

public interface AssetDAO {
    void save(Asset asset);

    Asset getById(int id);

    List<Asset> getAll();

    void update(Asset asset);

    void delete(Asset asset);

    Asset getByName(String name);

    Asset getByAssetTag(String assetTag);
}
