
package dao.device.interfaces;

import models.device.AssetRequest;
import models.main.Employee;

import java.util.List;

public interface AssetRequestDAO {
    void addAssetRequest(AssetRequest request);

    void updateAssetRequest(AssetRequest request);

    void deleteAssetRequest(int requestId);

    AssetRequest getAssetRequestById(int requestId);

    // Pure persistence queries
    List<AssetRequest> getAll();

    List<AssetRequest> getByDepartmentId(int departmentId);

    List<AssetRequest> getByEmployeeId(int employeeId);

    // Workflow methods kept for now (can be moved to Service in a later refactor)
    String createRequestWithItems(int employeeId, String requestType, java.util.List<Integer> assetIds);

    String updateRequestWithItems(int requestId, java.util.List<Integer> assetIds);

    String approveBorrowRequest(int requestId, Employee approver);

    String approveReturnRequest(int requestId, Employee approver);

    String rejectRequest(int requestId, Employee approver);
}
