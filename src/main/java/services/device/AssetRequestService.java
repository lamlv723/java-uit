
package services.device;

import dao.device.AssetRequestDAOImpl;
import dao.device.interfaces.AssetRequestDAO;
import dao.device.interfaces.AssetRequestItemDAO;
import dao.device.AssetRequestItemDAOImpl;
import models.device.Asset;
import models.device.AssetRequest;
import models.device.AssetRequestItem;
import models.main.Employee;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AssetRequestService {
    private AssetRequestDAO assetRequestDAO;
    private AssetRequestItemDAO assetRequestItemDAO;

    public AssetRequestService() {
        this.assetRequestDAO = new AssetRequestDAOImpl();
        this.assetRequestItemDAO = new AssetRequestItemDAOImpl();
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

    public List<AssetRequest> getAllAvailableAssets() {
        List<AssetRequest> allAssets = assetRequestDAO.getAllAssetRequests();
        if (allAssets == null) {
            return new java.util.ArrayList<>();
        }
        return allAssets.stream()
                .filter(asset -> "Available".equalsIgnoreCase(asset.getStatus()))
                .collect(Collectors.toList());
    }

    public String createRequestWithItems(int employeeId, String requestType, List<Integer> assetIds, String currentUserRole) {
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
                 if (!"Available".equalsIgnoreCase(asset.getStatus())) {
                    transaction.rollback();
                    return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId + ") không có sẵn để mượn.";
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


    public String approveRequest(int requestId, int approverId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            AssetRequest request = session.get(AssetRequest.class, requestId);
            if (request == null) return "Không tìm thấy yêu cầu.";
            if (!"Pending".equals(request.getStatus())) return "Chỉ có thể duyệt yêu cầu ở trạng thái 'Pending'.";

            Employee approver = session.get(Employee.class, approverId);
            if (approver == null) return "Không tìm thấy người duyệt.";

            // Logic kiểm tra tài sản vẫn còn đó, nhưng không thay đổi trạng thái hay ngày tháng
            List<AssetRequestItem> items = assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);

            if ("borrow".equals(request.getRequestType())) {
                for (AssetRequestItem item : items) {
                    Asset asset = item.getAsset();
                    if (!"Available".equalsIgnoreCase(asset.getStatus())) {
                        transaction.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' không có sẵn để mượn.";
                    }
                }
            } else if ("return".equals(request.getRequestType())) {
                for (AssetRequestItem item : items) {
                    Asset asset = item.getAsset();
                     if (!"Borrowed".equalsIgnoreCase(asset.getStatus())) {
                        transaction.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' không ở trạng thái 'Borrowed' để trả.";
                    }
                }
            }

            request.setStatus("Approved");
            request.setApprover(approver);
            request.setApprovalDate(Date.from(Instant.now()));
            session.update(request);

            transaction.commit();
            return null; // Thành công
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace(); // In lỗi ra console để debug
            return "Lỗi khi duyệt yêu cầu: " + e.getMessage();
        }
    }

    public String rejectRequest(int requestId, int approverId) {
        // ... (phương thức này không đổi)
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            AssetRequest request = session.get(AssetRequest.class, requestId);
            if (request == null) return "Không tìm thấy yêu cầu.";
            if (!"Pending".equals(request.getStatus())) return "Chỉ có thể từ chối yêu cầu ở trạng thái 'Pending'.";

            Employee approver = session.get(Employee.class, approverId);
            if (approver == null) return "Không tìm thấy người duyệt.";

            request.setStatus("Rejected");
            request.setApprover(approver);
            request.setApprovalDate(Date.from(Instant.now()));
            session.update(request);

            transaction.commit();
            return null; // Thành công
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return "Lỗi khi từ chối yêu cầu: " + e.getMessage();
        }
    }

    /**
     * Hoàn tất yêu cầu MƯỢN: Cập nhật trạng thái tài sản thành 'Borrowed' và set borrow_date.
     */
    public String completeBorrowRequest(int requestId) {
         Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            AssetRequest request = session.get(AssetRequest.class, requestId);

            if (request == null) return "Không tìm thấy yêu cầu.";
            if (!"borrow".equals(request.getRequestType())) return "Đây không phải là yêu cầu mượn.";
            if (!"Approved".equals(request.getStatus())) return "Chỉ có thể hoàn tất yêu cầu đã được 'Approved'.";
            
            List<AssetRequestItem> items = assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);
            for (AssetRequestItem item : items) {
                Asset asset = item.getAsset();
                asset.setStatus("Borrowed"); // Cập nhật trạng thái tài sản
                item.setBorrowDate(Date.from(Instant.now())); // Set ngày mượn
                session.update(asset);
                session.update(item);
            }

            request.setStatus("Completed");
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
    public String completeReturnRequest(int requestId) {
         Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            AssetRequest request = session.get(AssetRequest.class, requestId);

            if (request == null) return "Không tìm thấy yêu cầu.";
            if (!"return".equals(request.getRequestType())) return "Đây không phải là yêu cầu trả.";
            if (!"Approved".equals(request.getStatus())) return "Chỉ có thể hoàn tất yêu cầu đã được 'Approved'.";
            
            List<AssetRequestItem> items = assetRequestItemDAO.getAssetRequestItemsByRequestId(requestId);
            for (AssetRequestItem item : items) {
                Asset asset = item.getAsset();
                asset.setStatus("Available"); // Cập nhật trạng thái tài sản
                item.setReturnDate(Date.from(Instant.now())); // Set ngày trả
                session.update(asset);
                session.update(item);
            }

            request.setStatus("Completed");
            session.update(request);
            
            transaction.commit();
            return null; // Thành công
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return "Lỗi khi hoàn tất yêu cầu trả: " + e.getMessage();
        }
    }

}
