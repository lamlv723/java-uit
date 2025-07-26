
package services.device;

import dao.device.AssetRequestDAOImpl;
import dao.device.interfaces.AssetRequestDAO;
import models.device.AssetRequest;
import java.util.List;

public class AssetRequestService {
    private AssetRequestDAO assetRequestDAO;

    public AssetRequestService() {
        this.assetRequestDAO = new AssetRequestDAOImpl();
    }

    public void addAssetRequest(AssetRequest request, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestDAO.addAssetRequest(request);
    }

    public void updateAssetRequest(AssetRequest request, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestDAO.updateAssetRequest(request);
    }

    public void deleteAssetRequest(int requestId, String currentUserRole) {
        // TODO: Add role-based logic if needed
        assetRequestDAO.deleteAssetRequest(requestId);
    }

    public AssetRequest getAssetRequestById(int requestId) {
        return assetRequestDAO.getAssetRequestById(requestId);
    }

    public List<AssetRequest> getAllAssetRequests() {
        return assetRequestDAO.getAllAssetRequests();
    }

    /**
     * Xử lý toàn bộ logic nghiệp vụ khi thêm AssetRequest từ dữ liệu đầu vào dạng
     * String.
     * Trả về null nếu thành công, trả về thông báo lỗi nếu có lỗi.
     */
    public String addAssetRequestFromInput(String title, String desc, String currentUserRole) {
        if (title == null || title.isEmpty()) {
            return "Tiêu đề không được để trống!";
        }
        AssetRequest req = new AssetRequest();
        req.setRequestType(title); // Dùng requestType làm tiêu đề
        req.setStatus(desc); // Dùng status làm mô tả
        try {
            addAssetRequest(req, currentUserRole);
        } catch (Exception ex) {
            return "Lỗi khi thêm yêu cầu: " + ex.getMessage();
        }
        return null;
    }
}
