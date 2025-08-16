package services.device;

import dao.device.AssetDAOImpl;
import dao.device.AssetRequestDAOImpl;
import dao.device.AssetRequestItemDAOImpl;
import dao.device.interfaces.AssetRequestDAO;
import models.device.Asset;
import models.device.AssetRequest;
import models.device.AssetRequestItem;
// Đảm bảo import này luôn tồn tại
import static constants.AppConstants.*;
import java.util.List;

public class AssetRequestService {
    private AssetRequestDAO assetRequestDAO;

    public AssetRequestService() {
        this.assetRequestDAO = new AssetRequestDAOImpl();
    }

    public void addAssetRequest(AssetRequest request, String currentUserRole) {
        assetRequestDAO.addAssetRequest(request);
    }

    public void updateAssetRequest(AssetRequest request, String currentUserRole) {
        assetRequestDAO.updateAssetRequest(request);
    }

    public void deleteAssetRequest(int requestId, String currentUserRole) {
        AssetRequestItemDAOImpl itemDAO = new AssetRequestItemDAOImpl();
        itemDAO.deleteItemsByRequestId(requestId);
        assetRequestDAO.deleteAssetRequest(requestId);
    }

    public AssetRequest getAssetRequestById(int requestId) {
        return assetRequestDAO.getAssetRequestById(requestId);
    }

    public List<AssetRequest> getAllAssetRequests() {
        return assetRequestDAO.getAllAssetRequests();
    }

    public String addAssetRequestFromInput(String title, String desc, String currentUserRole) {
        if (title == null || title.isEmpty()) {
            return "Tiêu đề không được để trống!";
        }
        AssetRequest req = new AssetRequest();
        req.setRequestType(title);
        req.setStatus(desc);
        try {
            addAssetRequest(req, currentUserRole);
        } catch (Exception ex) {
            return "Lỗi khi thêm yêu cầu: " + ex.getMessage();
        }
        return null;
    }

    public String createRequestWithItems(int employeeId, String requestType, List<Integer> assetIds, String currentUserRole) {
        try {
            AssetRequest request = new AssetRequest();
            models.main.Employee employee = new models.main.Employee();
            employee.setEmployeeId(employeeId);
            request.setEmployee(employee);
            request.setRequestType(requestType);
            request.setRequestDate(new java.util.Date());
            request.setStatus(REQUEST_PENDING); // SỬ DỤNG HẰNG SỐ

            assetRequestDAO.addAssetRequest(request);

            AssetRequestItemDAOImpl itemDAO = new AssetRequestItemDAOImpl();
            for (Integer assetId : assetIds) {
                AssetRequestItem item = new AssetRequestItem();
                item.setAssetRequest(request);
                models.device.Asset asset = new models.device.Asset();
                asset.setAssetId(assetId);
                item.setAsset(asset);
                itemDAO.addAssetRequestItem(item);
            }
        } catch (Exception ex) {
            return "Lỗi khi tạo yêu cầu: " + ex.getMessage();
        }
        return null;
    }

     public String updateRequestWithItems(int requestId, int employeeId, String requestType, List<Integer> newAssetIds) {
        try {
            AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
            if (request == null) {
                return "Không tìm thấy yêu cầu để cập nhật.";
            }
            if (!REQUEST_PENDING.equals(request.getStatus())) {
                return "Chỉ có thể sửa các yêu cầu đang ở trạng thái 'Pending'.";
            }
            AssetRequestItemDAOImpl itemDAO = new AssetRequestItemDAOImpl();
            itemDAO.deleteItemsByRequestId(requestId);
            for (Integer assetId : newAssetIds) {
                AssetRequestItem newItem = new AssetRequestItem();
                newItem.setAssetRequest(request);
                models.device.Asset asset = new models.device.Asset();
                asset.setAssetId(assetId);
                newItem.setAsset(asset);
                itemDAO.addAssetRequestItem(newItem);
            }
            models.main.Employee employee = new models.main.Employee();
            employee.setEmployeeId(employeeId);
            request.setEmployee(employee);
            request.setRequestType(requestType);
            assetRequestDAO.updateAssetRequest(request);
        } catch (Exception ex) {
            return "Lỗi khi cập nhật yêu cầu: " + ex.getMessage();
        }
        return null;
    }


    public String approveRequest(int requestId, int approverId) {
        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null) {
            return "Không tìm thấy yêu cầu.";
        }
        // SỬ DỤNG HẰNG SỐ
        if (!REQUEST_PENDING.equals(request.getStatus())) {
            return "Chỉ có thể duyệt các yêu cầu đang ở trạng thái 'Pending'.";
        }

        AssetRequestItemDAOImpl itemDAO = new AssetRequestItemDAOImpl();
        AssetDAOImpl assetDAO = new AssetDAOImpl();
        List<AssetRequestItem> items = itemDAO.getItemsByRequestId(requestId);

        if (REQUEST_TYPE_BORROW.equals(request.getRequestType())) {
            for (AssetRequestItem item : items) {
                Asset asset = assetDAO.getById(item.getAsset().getAssetId());
                // SỬ DỤNG HẰNG SỐ
                if (!ASSET_AVAILABLE.equals(asset.getStatus())) {
                    request.setStatus(REQUEST_REJECTED);
                    assetRequestDAO.updateAssetRequest(request);
                    return "Yêu cầu bị từ chối do có tài sản '" + asset.getAssetName() + "' không có sẵn.";
                }
            }
            for (AssetRequestItem item : items) {
                Asset asset = assetDAO.getById(item.getAsset().getAssetId());
                asset.setStatus(ASSET_BORROWED); // SỬ DỤNG HẰNG SỐ
                assetDAO.update(asset);
                item.setBorrowDate(new java.util.Date());
                itemDAO.updateAssetRequestItem(item);
            }
            request.setStatus(REQUEST_APPROVED); // SỬ DỤNG HẰNG SỐ
        } else if (REQUEST_TYPE_RETURN.equals(request.getRequestType())) {
            for (AssetRequestItem item : items) {
                Asset asset = assetDAO.getById(item.getAsset().getAssetId());
                asset.setStatus(ASSET_AVAILABLE); // SỬ DỤNG HẰNG SỐ
                assetDAO.update(asset);
                item.setReturnDate(new java.util.Date());
                itemDAO.updateAssetRequestItem(item);
            }
            request.setStatus(REQUEST_COMPLETED); // SỬ DỤNG HẰNG SỐ
        }

        models.main.Employee approver = new models.main.Employee();
        approver.setEmployeeId(approverId);
        request.setApprover(approver);
        request.setApprovalDate(new java.util.Date());
        assetRequestDAO.updateAssetRequest(request);
        return null;
    }

    public String rejectRequest(int requestId, int approverId) {
        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null) {
            return "Không tìm thấy yêu cầu.";
        }
        // SỬ DỤNG HẰNG SỐ
        if (!REQUEST_PENDING.equals(request.getStatus())) {
            return "Chỉ có thể từ chối các yêu cầu đang ở trạng thái 'Pending'.";
        }
        request.setStatus(REQUEST_REJECTED); // SỬ DỤNG HẰNG SỐ
        models.main.Employee approver = new models.main.Employee();
        approver.setEmployeeId(approverId);
        request.setApprover(approver);
        request.setApprovalDate(new java.util.Date());
        assetRequestDAO.updateAssetRequest(request);
        return null;
    }

    public String completeBorrowRequest(int requestId) {
        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null) {
            return "Không tìm thấy yêu cầu.";
        }

        // SỬ DỤNG HẰNG SỐ
        if (!REQUEST_TYPE_BORROW.equals(request.getRequestType())) {
            return "Chức năng này chỉ dành cho yêu cầu mượn tài sản.";
        }
        if (!REQUEST_APPROVED.equals(request.getStatus())) {
            return "Chỉ có thể hoàn tất yêu cầu đã được duyệt (Approved).";
        }

        AssetRequestItemDAOImpl itemDAO = new AssetRequestItemDAOImpl();
        List<AssetRequestItem> items = itemDAO.getItemsByRequestId(requestId);
        AssetDAOImpl assetDAO = new AssetDAOImpl();

        for (AssetRequestItem item : items) {
            item.setReturnDate(new java.util.Date());
            itemDAO.updateAssetRequestItem(item);
            Asset asset = assetDAO.getById(item.getAsset().getAssetId());
            if (asset != null) {
                asset.setStatus(ASSET_AVAILABLE); // SỬ DỤNG HẰNG SỐ
                assetDAO.update(asset);
            }
        }
        request.setStatus(REQUEST_COMPLETED); // SỬ DỤNG HẰNG SỐ
        assetRequestDAO.updateAssetRequest(request);
        
        return null;
    }
}