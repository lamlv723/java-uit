package services.device;

import java.util.List;

import models.device.AssetRequestItem;

public interface AssetRequestItemService {
    void addAssetRequestItem(AssetRequestItem item, String currentUserRole);

    void updateAssetRequestItem(AssetRequestItem item, String currentUserRole);

    void deleteAssetRequestItem(int requestItemId, String currentUserRole);

    AssetRequestItem getAssetRequestItemById(int requestItemId);

    List<AssetRequestItem> getAllAssetRequestItems();
}
