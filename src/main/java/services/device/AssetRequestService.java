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

import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
            logger.warn("Authorization Error: User {} attempted to update a request they don't own.", currentUser.getUsername());
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
            String errorMessage = "Authorization Error: User " + currentUser.getUsername() + " attempted to delete a request they don't own.";
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

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // 1. Tạo đối tượng AssetRequest
            AssetRequest request = new AssetRequest();
            Employee employee = session.get(Employee.class, employeeId);
            if (employee == null) {
                return "Không tìm thấy nhân viên với ID: " + employeeId;
            }
            request.setEmployee(employee);
            request.setRequestType(requestType);
            request.setRequestDate(Date.from(Instant.now()));
            request.setStatus("Pending");

            // 2. Lưu AssetRequest để Hibernate gán ID
            session.save(request);

            // 3. Tạo và lưu các AssetRequestItem
            for (Integer assetId : assetIds) {
                Asset asset = session.get(Asset.class, assetId);
                if (asset == null) {
                    // Nếu một asset không tồn tại, hủy toàn bộ giao dịch
                    transaction.rollback();
                    return "Không tìm thấy tài sản với ID: " + assetId;
                }
                // Logic kiểm tra nếu tài sản không có sẵn (quan trọng)
                 if ("borrow".equalsIgnoreCase(requestType)) {
                    if (!"Available".equalsIgnoreCase(asset.getStatus())) {
                        transaction.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId + ") không có sẵn để mượn.";
                    }
                } else if ("return".equalsIgnoreCase(requestType)) {
                    if (!"Borrowed".equalsIgnoreCase(asset.getStatus())) {
                        transaction.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId + ") không ở trạng thái 'Borrowed' để có thể trả.";
                    }
                }

                AssetRequestItem item = new AssetRequestItem();
                item.setAssetRequest(request);
                item.setAsset(asset);
                session.save(item);
            }

            // 4. Hoàn tất giao dịch
            transaction.commit();
            return null; // Thành công

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return "Đã xảy ra lỗi khi tạo yêu cầu: " + e.getMessage();
        }
    }

    public String rejectRequest(int requestId, Employee currentUser) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            AssetRequest request = session.get(AssetRequest.class, requestId);
            if (request == null) return "Không tìm thấy yêu cầu.";
            if (!"Pending".equals(request.getStatus())) return "Chỉ có thể từ chối yêu cầu ở trạng thái 'Pending'.";

            // === AUTHORIZATION LOGIC ===
            boolean canApprove = false;
            String currentUserRole = currentUser.getRole();
            Employee requestEmployee = request.getEmployee();

            if ("Admin".equalsIgnoreCase(currentUserRole)) {
                canApprove = true;
            } else if ("Manager".equalsIgnoreCase(currentUserRole)) {
                // Manager có thể duyệt yêu cầu của nhân viên trong phòng ban của mình
                boolean isSameDepartment = currentUser.getDepartmentId().equals(requestEmployee.getDepartmentId());
                if (isSameDepartment) {
                    canApprove = true;
                }
            }

            if (!canApprove) {
                logger.warn("Authorization Error: User {} attempted to approve a request they are not allowed to.", currentUser.getUsername());
                throw new SecurityException("Bạn không có quyền duyệt yêu cầu này.");
            }
            // === END - AUTHORIZATION LOGIC ===

            request.setStatus("Rejected");
            request.setApprover(currentUser);
            request.setRejectedDate(Date.from(Instant.now()));
            session.update(request);

            transaction.commit();
            return null; // Thành công
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return "Lỗi khi từ chối yêu cầu: " + e.getMessage();
        }
    }

    /**
     * Cập nhật trạng thái tài sản thành 'Borrowed' và set borrow_date.
     */
    public String approveBorrowRequest(int requestId, Employee currentUser) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            AssetRequest request = session.get(AssetRequest.class, requestId);

            if (request == null) return "Không tìm thấy yêu cầu.";
            if (!"borrow".equals(request.getRequestType())) return "Đây không phải là yêu cầu mượn.";
            if (!"Pending".equals(request.getStatus())) return "Chỉ có thể hoàn tất yêu cầu đang 'Pending'.";

            // === AUTHORIZATION LOGIC ===
            boolean canApprove = false;
            String currentUserRole = currentUser.getRole();
            Employee requestEmployee = request.getEmployee();

            if ("Admin".equalsIgnoreCase(currentUserRole)) {
                canApprove = true;
            } else if ("Manager".equalsIgnoreCase(currentUserRole)) {
                // Manager có thể duyệt yêu cầu của nhân viên trong phòng ban của mình
                boolean isSameDepartment = currentUser.getDepartmentId().equals(requestEmployee.getDepartmentId());
                if (isSameDepartment) {
                    canApprove = true;
                }
            }

            if (!canApprove) {
                logger.warn("Authorization Error: User {} attempted to approve a request they are not allowed to.", currentUser.getUsername());
                throw new SecurityException("Bạn không có quyền duyệt yêu cầu này.");
            }

            List<AssetRequestItem> items = assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);
            for (AssetRequestItem item : items) {
                Asset asset = item.getAsset();
                asset.setStatus("Borrowed"); // Cập nhật trạng thái tài sản
                item.setBorrowDate(Date.from(Instant.now())); // Set ngày mượn
                session.update(asset);
                session.update(item);
            }
            // === END - AUTHORIZATION LOGIC ===

            request.setStatus("Approved");
            request.setApprover(currentUser);
            request.setApprovalDate(Date.from(Instant.now()));
            session.update(request);

            transaction.commit();
            return null; // Thành công
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "Lỗi khi hoàn tất yêu cầu mượn: " + e.getMessage();
        }
    }

    /**
     * Hoàn tất yêu cầu TRẢ: Cập nhật trạng thái tài sản thành 'Available' và set return_date.
     */
    public String approveReturnRequest(int requestId, Employee currentUser) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            AssetRequest request = session.get(AssetRequest.class, requestId);

            if (request == null) return "Không tìm thấy yêu cầu.";
            if (!"return".equals(request.getRequestType())) return "Đây không phải là yêu cầu trả.";
            if (!"Pending".equals(request.getStatus())) return "Chỉ có thể hoàn tất yêu cầu đang 'Pending'.";

            // === AUTHORIZATION LOGIC ===
            boolean canApprove = false;
            String currentUserRole = currentUser.getRole();
            Employee requestEmployee = request.getEmployee();

            if ("Admin".equalsIgnoreCase(currentUserRole)) {
                canApprove = true;
            } else if ("Manager".equalsIgnoreCase(currentUserRole)) {
                // Manager có thể duyệt yêu cầu của nhân viên trong phòng ban của mình
                boolean isSameDepartment = currentUser.getDepartmentId().equals(requestEmployee.getDepartmentId());
                if (isSameDepartment) {
                    canApprove = true;
                }
            }

            if (!canApprove) {
                logger.warn("Authorization Error: User {} attempted to approve a request they are not allowed to.", currentUser.getUsername());
                throw new SecurityException("Bạn không có quyền duyệt yêu cầu này.");
            }
            // === END - AUTHORIZATION LOGIC ===

            List<AssetRequestItem> tempItemsToReturn = assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);
            if (tempItemsToReturn.isEmpty()) {
                return "Yêu cầu trả không có tài sản nào.";
            }

            for (AssetRequestItem tempReturnItem : tempItemsToReturn) {
                int assetId = tempReturnItem.getAsset().getAssetId();

                // Tìm lại record mượn gốc đang hoạt động
                AssetRequestItem originalBorrowItem = assetRequestItemDAO.findActiveBorrowItemByAssetId(assetId);

                if (originalBorrowItem == null) {
                    transaction.rollback();
                    return "Lỗi logic: Không tìm thấy bản ghi mượn đang hoạt động cho tài sản ID: " + assetId;
                }

                Date returnDate = Date.from(Instant.now());

                // Cập nhật record mượn gốc
                originalBorrowItem.setReturnDate(returnDate);
                session.update(originalBorrowItem);

                // Sao chép ngày mượn và gán ngày trả để cửa sổ chi tiết hiển thị đầy đủ
                tempReturnItem.setBorrowDate(originalBorrowItem.getBorrowDate());
                tempReturnItem.setReturnDate(returnDate);
                session.update(tempReturnItem);

                // Cập nhật trạng thái tài sản
                Asset asset = originalBorrowItem.getAsset();
                asset.setStatus("Available");
                session.update(asset);
            }

            // Cập nhật trạng thái của yêu cầu trả thành "Approved"
            request.setStatus("Approved");
            request.setApprover(currentUser);
            request.setApprovalDate(Date.from(Instant.now()));
            session.update(request);

            transaction.commit();
            return null; // Thành công
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "Lỗi khi hoàn tất yêu cầu trả: " + e.getMessage();
        }
    }

    public String updateRequestWithItems(int requestId, List<Integer> assetIds, Employee currentUser) {
        if (assetIds == null || assetIds.isEmpty()) {
            return "Phải chọn ít nhất một tài sản.";
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            AssetRequest request = session.get(AssetRequest.class, requestId);
            if (request == null) {
                return "Không tìm thấy yêu cầu để cập nhật.";
            }

            // Prevent changing approved request
            if (!"Pending".equalsIgnoreCase(request.getStatus())) {
                logger.warn("User {} attempted to update a non-pending request (ID: {}) with status: {}",
                        currentUser.getUsername(), requestId, request.getStatus());
                throw new SecurityException("Chỉ có thể sửa các yêu cầu đang ở trạng thái 'Pending'.");
            }

            // === LOGIC CHECK OWNERSHIP ===
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
                logger.warn("Authorization Error: User {} attempted to update a request they don't own.", currentUser.getUsername());
                throw new SecurityException("Bạn không có quyền sửa yêu cầu này.");
            }
            // === LOGIC CHECK OWNERSHIP - END ===

            if (request == null) {
                return "Không tìm thấy yêu cầu để cập nhật.";
            }
            String requestType = request.getRequestType();

            // 1. Xóa các item cũ
            Query<?> deleteQuery = session.createQuery("DELETE FROM AssetRequestItem WHERE assetRequest.requestId = :requestId");
            deleteQuery.setParameter("requestId", requestId);
            deleteQuery.executeUpdate();

            // 2. Tạo và lưu các AssetRequestItem mới
            for (Integer assetId : assetIds) {
                Asset asset = session.get(Asset.class, assetId);
                if (asset == null) {
                    transaction.rollback();
                    return "Không tìm thấy tài sản với ID: " + assetId;
                }

                if ("borrow".equalsIgnoreCase(requestType)) {
                    if (!"Available".equalsIgnoreCase(asset.getStatus())) {
                        transaction.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId + ") không có sẵn để mượn.";
                    }
                } else if ("return".equalsIgnoreCase(requestType)) {
                    if (!"Borrowed".equalsIgnoreCase(asset.getStatus())) {
                        transaction.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId + ") không ở trạng thái 'Borrowed' để có thể trả.";
                    }
                }

                AssetRequestItem item = new AssetRequestItem();
                item.setAssetRequest(request);
                item.setAsset(asset);
                session.save(item);
            }

            // 3. Hoàn tất giao dịch
            transaction.commit();
            return null; // Thành công

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return "Đã xảy ra lỗi khi cập nhật yêu cầu: " + e.getMessage();
        }
    }

}