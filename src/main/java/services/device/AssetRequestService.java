package services.device;

import java.util.List;

import models.device.AssetRequest;

public interface AssetRequestService {
    void addAssetRequest(AssetRequest request, String currentUserRole);

    void updateAssetRequest(AssetRequest request, String currentUserRole);

    void deleteAssetRequest(int requestId, String currentUserRole);

    AssetRequest getAssetRequestById(int requestId);

    List<AssetRequest> getAllAssetRequests();
}
