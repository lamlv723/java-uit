package services.device;

import dao.device.AssetRequestDAOImpl;
import dao.device.interfaces.AssetRequestDAO;
import dao.device.interfaces.AssetRequestItemDAO;
import dao.device.AssetRequestItemDAOImpl;
import models.device.Asset;
import models.device.AssetRequest;
import models.device.AssetRequestItem;
import models.main.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AssetRequestService {
    private AssetRequestDAO assetRequestDAO;
    private AssetRequestItemDAO assetRequestItemDAO;
    private static final Logger logger = LoggerFactory.getLogger(AssetRequestService.class);

    public AssetRequestService() {
        this.assetRequestDAO = new AssetRequestDAOImpl();
        this.assetRequestItemDAO = new AssetRequestItemDAOImpl();
    }

    public void addAssetRequest(AssetRequest request, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        if (currentUserRole == null || currentUserRole.isEmpty()) {
            logger.warn("Authorization Error: Unauthenticated user attempted to add an asset request.");
            throw new SecurityException("Bạn phải đăng nhập để thực hiện hành động này.");
        }
        assetRequestDAO.addAssetRequest(request);
    }

    public void updateAssetRequest(AssetRequest request, Employee currentUser) {
        if (request == null) {
            throw new IllegalArgumentException("Yêu cầu không được để trống.");
        }

        // Lấy bản ghi mới nhất từ DB để kiểm tra
        AssetRequest existingRequest = assetRequestDAO.getAssetRequestById(request.getRequestId());
        if (existingRequest == null) {
            throw new RuntimeException("Không tìm thấy yêu cầu để cập nhật.");
        }

        if (!"Pending".equalsIgnoreCase(existingRequest.getStatus())) {
            throw new SecurityException("Chỉ có thể sửa các yêu cầu đang ở trạng thái 'Pending'.");
        }

        boolean canUpdate = false;
        if ("Admin".equalsIgnoreCase(currentUser.getRole())) {
            canUpdate = true;
        } else {
            boolean isOwner = currentUser.getEmployeeId().equals(existingRequest.getEmployee().getEmployeeId());
            if (isOwner) {
                canUpdate = true;
            }
        }

        if (!canUpdate) {
            logger.warn("Authorization Error: User {} attempted to update a request they don't own.",
                    currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền sửa yêu cầu này.");
        }

        assetRequestDAO.updateAssetRequest(request);
    }

    public String deleteAssetRequest(int requestId, Employee currentUser) {
        String currentUserRole = currentUser.getRole();
        AssetRequest requestToDelete = assetRequestDAO.getAssetRequestById(requestId);

        if (requestToDelete == null) {
            return "Không tìm thấy yêu cầu để xóa.";
        }

        // Chỉ được xóa khi ở trạng thái "Pending"
        if (!"Pending".equalsIgnoreCase(requestToDelete.getStatus())) {
            logger.warn("User {} attempted to delete a non-pending request (ID: {}) with status: {}",
                    currentUser.getUsername(), requestId, requestToDelete.getStatus());
            throw new SecurityException("Chỉ có thể xóa các yêu cầu đang ở trạng thái 'Pending'.");
        }

        boolean canDelete = false;
        if ("Admin".equalsIgnoreCase(currentUserRole)) {
            canDelete = true;
        } else {
            boolean isOwner = currentUser.getEmployeeId().equals(requestToDelete.getEmployee().getEmployeeId());
            if (isOwner) {
                canDelete = true;
            }
        }

        if (!canDelete) {
            String errorMessage = "Authorization Error: User " + currentUser.getUsername()
                    + " attempted to delete a request they don't own.";
            logger.warn(errorMessage);
            throw new SecurityException("Bạn không có quyền xóa yêu cầu này.");
        }

        try {
            // Lấy danh sách các item liên quan đến yêu cầu
            List<AssetRequestItem> itemsToDelete = assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);

            // Xóa từng item chi tiết trong yêu cầu
            for (AssetRequestItem item : itemsToDelete) {
                assetRequestItemDAO.deleteAssetRequestItem(item.getRequestItemId());
            }

            // Xóa yêu cầu chính
            assetRequestDAO.deleteAssetRequest(requestId);

            return null; // Trả về null nếu thành công
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi xóa yêu cầu: " + e.getMessage();
        }
    }

    public AssetRequest getAssetRequestById(int requestId) {
        return assetRequestDAO.getAssetRequestById(requestId);
    }

    public List<AssetRequest> getAllAssetRequests(Employee currentUser) {
        return assetRequestDAO.getAllAssetRequests(currentUser);
    }

    public List<AssetRequestItem> getItemsByRequestId(int requestId) {
        return assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);
    }

    public List<AssetRequestItem> getItemsReferencingAsset(int assetId) {
        java.util.List<AssetRequestItem> result = new java.util.ArrayList<>();
        try {
            AssetRequestItem active = assetRequestItemDAO.findActiveBorrowItemByAssetId(assetId);
            if (active != null)
                result.add(active);
        } catch (Exception ignored) {
        }
        return result; // at most one element currently
    }

    public String addAssetRequestFromInput(String title, String desc, Employee currentUser) {
        if (title == null || title.isEmpty()) {
            return "Tiêu đề không được để trống!";
        }
        AssetRequest req = new AssetRequest();
        req.setRequestType(title); // Dùng requestType làm tiêu đề
        req.setStatus(desc); // Dùng status làm mô tả
        try {
            addAssetRequest(req, currentUser);
        } catch (Exception ex) {
            return "Lỗi khi thêm yêu cầu: " + ex.getMessage();
        }
        return null;
    }

    public List<AssetRequest> getAllAvailableAssets(Employee currentUser) {
        List<AssetRequest> allAssets = assetRequestDAO.getAllAssetRequests(currentUser);
        if (allAssets == null) {
            return new java.util.ArrayList<>();
        }
        return allAssets.stream()
                .filter(asset -> "Available".equalsIgnoreCase(asset.getStatus()))
                .collect(Collectors.toList());
    }

    public String createRequestWithItems(int employeeId, String requestType, List<Integer> assetIds) {
        if (assetIds == null || assetIds.isEmpty()) {
            return "Phải chọn ít nhất một tài sản.";
        }
        // Ủy quyền xử lý DB xuống DAO
        return assetRequestDAO.createRequestWithItems(employeeId, requestType, assetIds);
    }

    public String rejectRequest(int requestId, Employee currentUser) {
        // Lấy request để kiểm tra quyền
        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null)
            return "Không tìm thấy yêu cầu.";
        if (!"Pending".equals(request.getStatus()))
            return "Chỉ có thể từ chối yêu cầu ở trạng thái 'Pending'.";

        boolean canApprove = false;
        String currentUserRole = currentUser.getRole();
        Employee requestEmployee = request.getEmployee();

        if ("Admin".equalsIgnoreCase(currentUserRole)) {
            canApprove = true;
        } else if ("Manager".equalsIgnoreCase(currentUserRole)) {
            boolean isSameDepartment = currentUser.getDepartmentId().equals(requestEmployee.getDepartmentId());
            if (isSameDepartment)
                canApprove = true;
        }

        if (!canApprove) {
            logger.warn("Authorization Error: User {} attempted to approve a request they are not allowed to.",
                    currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền duyệt yêu cầu này.");
        }

        return assetRequestDAO.rejectRequest(requestId, currentUser);
    }

    /**
     * Cập nhật trạng thái tài sản thành 'Borrowed' và set borrow_date.
     */
    public String approveBorrowRequest(int requestId, Employee currentUser) {
        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null)
            return "Không tìm thấy yêu cầu.";
        if (!"borrow".equals(request.getRequestType()))
            return "Đây không phải là yêu cầu mượn.";
        if (!"Pending".equals(request.getStatus()))
            return "Chỉ có thể hoàn tất yêu cầu đang 'Pending'.";

        boolean canApprove = false;
        String currentUserRole = currentUser.getRole();
        Employee requestEmployee = request.getEmployee();
        if ("Admin".equalsIgnoreCase(currentUserRole)) {
            canApprove = true;
        } else if ("Manager".equalsIgnoreCase(currentUserRole)) {
            boolean isSameDepartment = currentUser.getDepartmentId().equals(requestEmployee.getDepartmentId());
            if (isSameDepartment)
                canApprove = true;
        }
        if (!canApprove) {
            logger.warn("Authorization Error: User {} attempted to approve a request they are not allowed to.",
                    currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền duyệt yêu cầu này.");
        }

        // Ủy quyền xử lý dữ liệu xuống DAO
        return assetRequestDAO.approveBorrowRequest(requestId, currentUser);
    }

    /**
     * Hoàn tất yêu cầu TRẢ: Cập nhật trạng thái tài sản thành 'Available' và set
     * return_date.
     */
    public String approveReturnRequest(int requestId, Employee currentUser) {
        AssetRequest requestToReturn = assetRequestDAO.getAssetRequestById(requestId);
        if (requestToReturn == null)
            return "Không tìm thấy yêu cầu.";
        if (!"return".equals(requestToReturn.getRequestType()))
            return "Đây không phải là yêu cầu trả.";
        if (!"Pending".equals(requestToReturn.getStatus()))
            return "Chỉ có thể hoàn tất yêu cầu đang 'Pending'.";

        boolean canApprove = false;
        String currentUserRole = currentUser.getRole();
        Employee requestEmployee = requestToReturn.getEmployee();
        if ("Admin".equalsIgnoreCase(currentUserRole)) {
            canApprove = true;
        } else if ("Manager".equalsIgnoreCase(currentUserRole)) {
            boolean isSameDepartment = currentUser.getDepartmentId().equals(requestEmployee.getDepartmentId());
            if (isSameDepartment)
                canApprove = true;
        }
        if (!canApprove) {
            logger.warn("Authorization Error: User {} attempted to approve a request they are not allowed to.",
                    currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền duyệt yêu cầu này.");
        }

        return assetRequestDAO.approveReturnRequest(requestId, currentUser);
    }

    // ================= Unit-test friendly core logic helpers =================
    /**
     * Core logic for approving a borrow request (updates items + request) without
     * starting/committing transaction.
     * Provided to allow isolated unit tests without a live DB.
     */
    protected void applyBorrowApprovalCore(List<AssetRequestItem> items, AssetRequest request, Employee approver,
            org.hibernate.Session session) {
        Date now = Date.from(Instant.now());
        for (AssetRequestItem item : items) {
            Asset asset = item.getAsset();
            asset.setStatus("Borrowed");
            item.setBorrowDate(now);
            if (session != null) {
                session.update(asset);
                session.update(item);
            }
        }
        request.setStatus("Approved");
        request.setApprover(approver);
        request.setApprovalDate(now);
        if (session != null)
            session.update(request);
    }

    /**
     * Core logic for rejecting a request, used for unit tests.
     */
    protected void applyRejectCore(AssetRequest request, Employee approver) {
        request.setStatus("Rejected");
        request.setApprover(approver);
        request.setRejectedDate(Date.from(Instant.now()));
    }

    public String updateRequestWithItems(int requestId, List<Integer> assetIds, Employee currentUser) {
        if (assetIds == null || assetIds.isEmpty()) {
            return "Phải chọn ít nhất một tài sản.";
        }

        AssetRequest request = assetRequestDAO.getAssetRequestById(requestId);
        if (request == null) {
            return "Không tìm thấy yêu cầu để cập nhật.";
        }

        if (!"Pending".equalsIgnoreCase(request.getStatus())) {
            logger.warn("User {} attempted to update a non-pending request (ID: {}) with status: {}",
                    currentUser.getUsername(), requestId, request.getStatus());
            throw new SecurityException("Chỉ có thể sửa các yêu cầu đang ở trạng thái 'Pending'.");
        }

        boolean canUpdate = false;
        if ("Admin".equalsIgnoreCase(currentUser.getRole())) {
            canUpdate = true;
        } else {
            boolean isOwner = currentUser.getEmployeeId().equals(request.getEmployee().getEmployeeId());
            if (isOwner) {
                canUpdate = true;
            }
        }

        if (!canUpdate) {
            logger.warn("Authorization Error: User {} attempted to update a request they don't own.",
                    currentUser.getUsername());
            throw new SecurityException("Bạn không có quyền sửa yêu cầu này.");
        }

        return assetRequestDAO.updateRequestWithItems(requestId, assetIds);
    }

}