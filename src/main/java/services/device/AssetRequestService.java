package services.device;

import dao.device.AssetRequestDAOImpl;
import dao.device.interfaces.AssetRequestDAO;
import dao.device.interfaces.AssetRequestItemDAO;
import dao.device.AssetRequestItemDAOImpl;
import dao.device.interfaces.AssetDAO;
import dao.device.AssetDAOImpl;
import dao.main.interfaces.EmployeeDAO;
import dao.main.EmployeeDAOImpl;
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
    private final AssetRequestDAO assetRequestDAO;
    private final AssetRequestItemDAO assetRequestItemDAO;
    // Additional DAOs for business validations
    private final AssetDAO assetDAO;
    private final EmployeeDAO employeeDAO;
    private static final Logger logger = LoggerFactory.getLogger(AssetRequestService.class);

    public AssetRequestService(AssetRequestDAO assetRequestDAO, AssetRequestItemDAO assetRequestItemDAO) {
        this.assetRequestDAO = assetRequestDAO;
        this.assetRequestItemDAO = assetRequestItemDAO;
        this.assetDAO = new AssetDAOImpl();
        this.employeeDAO = new EmployeeDAOImpl();
    }

    public AssetRequestService(AssetRequestDAO assetRequestDAO, AssetRequestItemDAO assetRequestItemDAO,
            AssetDAO assetDAO, EmployeeDAO employeeDAO) {
        this.assetRequestDAO = assetRequestDAO;
        this.assetRequestItemDAO = assetRequestItemDAO;
        this.assetDAO = assetDAO;
        this.employeeDAO = employeeDAO;
    }

    // Backward-compatible default wiring
    public AssetRequestService() {
        this(new AssetRequestDAOImpl(), new AssetRequestItemDAOImpl(), new AssetDAOImpl(), new EmployeeDAOImpl());
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
            List<AssetRequestItem> itemsToDelete = assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);

            for (AssetRequestItem item : itemsToDelete) {
                assetRequestItemDAO.deleteAssetRequestItem(item.getRequestItemId());
            }

            assetRequestDAO.deleteAssetRequest(requestId);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi xóa yêu cầu: " + e.getMessage();
        }
    }

    public AssetRequest getAssetRequestById(int requestId) {
        return assetRequestDAO.getAssetRequestById(requestId);
    }

    public List<AssetRequest> getAllAssetRequests(Employee currentUser) {
        if (currentUser == null) {
            return new java.util.ArrayList<>();
        }
        String role = currentUser.getRole();
        if ("Admin".equalsIgnoreCase(role)) {
            return assetRequestDAO.getAll();
        } else if ("Manager".equalsIgnoreCase(role)) {
            Integer deptId = currentUser.getDepartmentId();
            if (deptId == null)
                return new java.util.ArrayList<>();
            return assetRequestDAO.getByDepartmentId(deptId);
        } else {
            return assetRequestDAO.getByEmployeeId(currentUser.getEmployeeId());
        }
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
        return result;
    }

    public String addAssetRequestFromInput(String title, String desc, Employee currentUser) {
        if (title == null || title.isEmpty()) {
            return "Tiêu đề không được để trống!";
        }
        AssetRequest req = new AssetRequest();
        req.setRequestType(title);
        req.setStatus(desc);
        try {
            addAssetRequest(req, currentUser);
        } catch (Exception ex) {
            return "Lỗi khi thêm yêu cầu: " + ex.getMessage();
        }
        return null;
    }

    public List<AssetRequest> getAllAvailableAssets(Employee currentUser) {
        List<AssetRequest> allAssets = getAllAssetRequests(currentUser);
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

        String type = requestType == null ? "" : requestType.trim().toLowerCase();
        if (!"borrow".equals(type) && !"return".equals(type)) {
            return "Loại yêu cầu không hợp lệ. Chỉ hỗ trợ 'borrow' hoặc 'return'.";
        }

        Employee employee = employeeDAO.getEmployeeById(employeeId);
        if (employee == null) {
            return "Không tìm thấy nhân viên với ID: " + employeeId;
        }

        for (Integer assetId : assetIds) {
            Asset asset = assetDAO.getById(assetId);
            if (asset == null) {
                return "Không tìm thấy tài sản với ID: " + assetId;
            }
            if ("borrow".equals(type)) {
                if (!"Available".equalsIgnoreCase(asset.getStatus())) {
                    return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId + ") không có sẵn để mượn.";
                }
            } else { // return
                if (!"Borrowed".equalsIgnoreCase(asset.getStatus())) {
                    return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId
                            + ") không ở trạng thái 'Borrowed' để có thể trả.";
                }
            }
        }

        return assetRequestDAO.createRequestWithItems(employeeId, type, assetIds);
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

        List<AssetRequestItem> items = assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);
        for (AssetRequestItem item : items) {
            int assetId = item.getAsset().getAssetId();
            Asset fresh = assetDAO.getById(assetId);
            if (fresh == null || !"Available".equalsIgnoreCase(fresh.getStatus())) {
                String assetName = (fresh != null ? fresh.getAssetName() : ("#" + assetId));
                return "Không thể duyệt: Tài sản '" + assetName + "' đã được mượn hoặc không có sẵn.";
            }
        }

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

        List<AssetRequestItem> tempItemsToReturn = assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);
        if (tempItemsToReturn == null || tempItemsToReturn.isEmpty()) {
            return "Yêu cầu trả không có tài sản nào.";
        }
        for (AssetRequestItem tempReturnItem : tempItemsToReturn) {
            int assetId = tempReturnItem.getAsset().getAssetId();
            AssetRequestItem originalBorrowItem = assetRequestItemDAO.findActiveBorrowItemByAssetId(assetId);
            if (originalBorrowItem == null) {
                return "Lỗi logic: Không tìm thấy bản ghi mượn đang hoạt động cho tài sản ID: " + assetId;
            }
        }

        return assetRequestDAO.approveReturnRequest(requestId, currentUser);
    }

    // Unit-test helpers
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

        String requestType = request.getRequestType();
        String type = requestType == null ? "" : requestType.trim().toLowerCase();
        for (Integer assetId : assetIds) {
            Asset asset = assetDAO.getById(assetId);
            if (asset == null) {
                return "Không tìm thấy tài sản với ID: " + assetId;
            }
            if ("borrow".equals(type)) {
                if (!"Available".equalsIgnoreCase(asset.getStatus())) {
                    return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId + ") không có sẵn để mượn.";
                }
            } else if ("return".equals(type)) {
                if (!"Borrowed".equalsIgnoreCase(asset.getStatus())) {
                    return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId
                            + ") không ở trạng thái 'Borrowed' để có thể trả.";
                }
            }
        }

        return assetRequestDAO.updateRequestWithItems(requestId, assetIds);
    }

}