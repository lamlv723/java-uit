
package dao.device;

import models.device.AssetRequest;
import java.util.List;

public interface AssetRequestDAO {
    void addAssetRequest(AssetRequest request);

    void updateAssetRequest(AssetRequest request);

    void deleteAssetRequest(int requestId);

    AssetRequest getAssetRequestById(int requestId);

    List<AssetRequest> getAllAssetRequests();
}
