package dao.device.interfaces;

import models.device.AssetRequestItem;
import models.main.Employee;

import java.util.List;

public interface AssetRequestItemDAO {
    void addAssetRequestItem(AssetRequestItem item);

    void updateAssetRequestItem(AssetRequestItem item);

    void deleteAssetRequestItem(int requestItemId);

    AssetRequestItem getAssetRequestItemById(int requestItemId);

    List<AssetRequestItem> getAllAssetRequestItems();

    List<AssetRequestItem> getAssetRequestItemsByRequestId(int requestId);

    AssetRequestItem findActiveBorrowItemByAssetId(int assetId);

    List<AssetRequestItem> getFilteredRequestItems(Employee currentUser);
}
