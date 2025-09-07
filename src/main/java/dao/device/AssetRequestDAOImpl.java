package dao.device;

import models.device.AssetRequest;
import models.device.AssetRequestItem;
import models.device.Asset;
import config.HibernateUtil;
import dao.device.interfaces.AssetRequestDAO;

import models.main.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class AssetRequestDAOImpl implements AssetRequestDAO {
    private static final Logger logger = LoggerFactory.getLogger(AssetRequestDAOImpl.class);

    @Override
    public void addAssetRequest(AssetRequest request) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(request);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error adding asset request: {}", e.getMessage(), e);
        }
    }

    @Override
    public void updateAssetRequest(AssetRequest request) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(request);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error updating asset request: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteAssetRequest(int requestId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            AssetRequest req = session.get(AssetRequest.class, requestId);
            if (req != null)
                session.delete(req);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error deleting asset request with id {}: {}", requestId, e.getMessage(), e);
        }
    }

    @Override
    public AssetRequest getAssetRequestById(int requestId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AssetRequest.class, requestId);
        } catch (Exception e) {
            logger.error("Error getting asset request by id {}: {}", requestId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<AssetRequest> getAllAssetRequests(Employee currentUser) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (currentUser == null) {
                return new java.util.ArrayList<>();
            }

            String role = currentUser.getRole();
        if ("Admin".equalsIgnoreCase(role)) {
            // Admin thấy tất cả, sắp xếp theo ID mới nhất
            Query<AssetRequest> query = session.createQuery("FROM AssetRequest ar ORDER BY ar.requestId ASC", AssetRequest.class);
            return query.getResultList();
        } else if ("Manager".equalsIgnoreCase(role)) {
            // Manager thấy yêu cầu của nhân viên trong phòng ban, sắp xếp theo ID mới nhất
            Query<AssetRequest> query = session.createQuery(
                    "FROM AssetRequest ar WHERE ar.employee.department.departmentId = :deptId ORDER BY ar.requestId ASC", AssetRequest.class);
            query.setParameter("deptId", currentUser.getDepartmentId());
            return query.getResultList();
        } else {
            // Staff chỉ thấy yêu cầu của chính mình, sắp xếp theo ID mới nhất
            Query<AssetRequest> query = session.createQuery(
                    "FROM AssetRequest ar WHERE ar.employee.employeeId = :empId ORDER BY ar.requestId ASC", AssetRequest.class);
            query.setParameter("empId", currentUser.getEmployeeId());
            return query.getResultList();
        }
        } catch (Exception e) {
            logger.error("Error getting filtered list of asset requests: {}", e.getMessage(), e);
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public String createRequestWithItems(int employeeId, String requestType, java.util.List<Integer> assetIds) {
        if (assetIds == null || assetIds.isEmpty()) {
            return "Phải chọn ít nhất một tài sản.";
        }
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Employee employee = session.get(Employee.class, employeeId);
            if (employee == null) {
                return "Không tìm thấy nhân viên với ID: " + employeeId;
            }

            AssetRequest request = new AssetRequest();
            request.setEmployee(employee);
            request.setRequestType(requestType);
            request.setRequestDate(java.util.Date.from(java.time.Instant.now()));
            request.setStatus("Pending");
            session.save(request);

            for (Integer assetId : assetIds) {
                Asset asset = session.get(Asset.class, assetId);
                if (asset == null) {
                    tx.rollback();
                    return "Không tìm thấy tài sản với ID: " + assetId;
                }
                if ("borrow".equalsIgnoreCase(requestType)) {
                    if (!"Available".equalsIgnoreCase(asset.getStatus())) {
                        tx.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId + ") không có sẵn để mượn.";
                    }
                } else if ("return".equalsIgnoreCase(requestType)) {
                    if (!"Borrowed".equalsIgnoreCase(asset.getStatus())) {
                        tx.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId
                                + ") không ở trạng thái 'Borrowed' để có thể trả.";
                    }
                }
                AssetRequestItem item = new AssetRequestItem();
                item.setAssetRequest(request);
                item.setAsset(asset);
                session.save(item);
            }

            tx.commit();
            return null;
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error creating request with items: {}", e.getMessage(), e);
            return "Đã xảy ra lỗi khi tạo yêu cầu: " + e.getMessage();
        }
    }

    @Override
    public String updateRequestWithItems(int requestId, java.util.List<Integer> assetIds) {
        if (assetIds == null || assetIds.isEmpty()) {
            return "Phải chọn ít nhất một tài sản.";
        }
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            AssetRequest request = session.get(AssetRequest.class, requestId);
            if (request == null) {
                return "Không tìm thấy yêu cầu để cập nhật.";
            }
            if (!"Pending".equalsIgnoreCase(request.getStatus())) {
                return "Chỉ có thể sửa các yêu cầu đang ở trạng thái 'Pending'.";
            }

            String requestType = request.getRequestType();

            Query<?> deleteQuery = session
                    .createQuery("DELETE FROM AssetRequestItem WHERE assetRequest.requestId = :rid");
            deleteQuery.setParameter("rid", requestId);
            deleteQuery.executeUpdate();

            for (Integer assetId : assetIds) {
                Asset asset = session.get(Asset.class, assetId);
                if (asset == null) {
                    tx.rollback();
                    return "Không tìm thấy tài sản với ID: " + assetId;
                }
                if ("borrow".equalsIgnoreCase(requestType)) {
                    if (!"Available".equalsIgnoreCase(asset.getStatus())) {
                        tx.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId + ") không có sẵn để mượn.";
                    }
                } else if ("return".equalsIgnoreCase(requestType)) {
                    if (!"Borrowed".equalsIgnoreCase(asset.getStatus())) {
                        tx.rollback();
                        return "Tài sản '" + asset.getAssetName() + "' (ID: " + assetId
                                + ") không ở trạng thái 'Borrowed' để có thể trả.";
                    }
                }
                AssetRequestItem item = new AssetRequestItem();
                item.setAssetRequest(request);
                item.setAsset(asset);
                session.save(item);
            }

            tx.commit();
            return null;
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error updating request with items: {}", e.getMessage(), e);
            return "Đã xảy ra lỗi khi cập nhật yêu cầu: " + e.getMessage();
        }
    }

    @Override
    public String approveBorrowRequest(int requestId, Employee approver) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            AssetRequest request = session.get(AssetRequest.class, requestId);
            if (request == null)
                return "Không tìm thấy yêu cầu.";
            if (!"borrow".equals(request.getRequestType()))
                return "Đây không phải là yêu cầu mượn.";
            if (!"Pending".equals(request.getStatus()))
                return "Chỉ có thể hoàn tất yêu cầu đang 'Pending'.";

            List<AssetRequestItem> items = session.createQuery(
                    "FROM AssetRequestItem WHERE assetRequest.requestId = :rid", AssetRequestItem.class)
                    .setParameter("rid", requestId)
                    .getResultList();
            for (AssetRequestItem item : items) {
                session.refresh(item.getAsset());
                if (!"Available".equalsIgnoreCase(item.getAsset().getStatus())) {
                    tx.rollback();
                    return "Không thể duyệt: Tài sản '" + item.getAsset().getAssetName()
                            + "' đã được mượn hoặc không có sẵn.";
                }
            }

            java.util.Date now = java.util.Date.from(java.time.Instant.now());
            for (AssetRequestItem item : items) {
                Asset asset = item.getAsset();
                asset.setStatus("Borrowed");
                item.setBorrowDate(now);
                session.update(asset);
                session.update(item);
            }
            request.setStatus("Approved");
            request.setApprover(approver);
            request.setApprovalDate(now);
            session.update(request);

            tx.commit();
            return null;
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error approving borrow request {}: {}", requestId, e.getMessage(), e);
            return "Lỗi khi hoàn tất yêu cầu mượn: " + e.getMessage();
        }
    }

    @Override
    public String approveReturnRequest(int requestId, Employee approver) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            AssetRequest requestToReturn = session.get(AssetRequest.class, requestId);
            if (requestToReturn == null)
                return "Không tìm thấy yêu cầu.";
            if (!"return".equals(requestToReturn.getRequestType()))
                return "Đây không phải là yêu cầu trả.";
            if (!"Pending".equals(requestToReturn.getStatus()))
                return "Chỉ có thể hoàn tất yêu cầu đang 'Pending'.";

            List<AssetRequestItem> tempItemsToReturn = session.createQuery(
                    "FROM AssetRequestItem WHERE assetRequest.requestId = :rid", AssetRequestItem.class)
                    .setParameter("rid", requestId)
                    .getResultList();
            if (tempItemsToReturn.isEmpty()) {
                return "Yêu cầu trả không có tài sản nào.";
            }

            for (AssetRequestItem tempReturnItem : tempItemsToReturn) {
                int assetId = tempReturnItem.getAsset().getAssetId();

                AssetRequestItem originalBorrowItem = session.createQuery(
                        "FROM AssetRequestItem i WHERE i.asset.assetId = :assetId AND i.returnDate IS NULL AND i.assetRequest.requestType = 'borrow' AND i.assetRequest.status = 'Approved'",
                        AssetRequestItem.class)
                        .setParameter("assetId", assetId)
                        .setMaxResults(1)
                        .uniqueResultOptional().orElse(null);

                if (originalBorrowItem == null) {
                    tx.rollback();
                    return "Lỗi logic: Không tìm thấy bản ghi mượn đang hoạt động cho tài sản ID: " + assetId;
                }

                java.util.Date returnDate = java.util.Date.from(java.time.Instant.now());
                originalBorrowItem.setReturnDate(returnDate);
                session.update(originalBorrowItem);

                Asset asset = originalBorrowItem.getAsset();
                asset.setStatus("Available");
                session.update(asset);

                session.delete(tempReturnItem);
            }

            session.delete(requestToReturn);

            tx.commit();
            return null;
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error approving return request {}: {}", requestId, e.getMessage(), e);
            return "Lỗi khi hoàn tất yêu cầu trả: " + e.getMessage();
        }
    }

    @Override
    public String rejectRequest(int requestId, Employee approver) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            AssetRequest request = session.get(AssetRequest.class, requestId);
            if (request == null)
                return "Không tìm thấy yêu cầu.";
            if (!"Pending".equals(request.getStatus()))
                return "Chỉ có thể từ chối yêu cầu ở trạng thái 'Pending'.";
            request.setStatus("Rejected");
            request.setApprover(approver);
            request.setRejectedDate(java.util.Date.from(java.time.Instant.now()));
            session.update(request);
            tx.commit();
            return null;
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error rejecting request {}: {}", requestId, e.getMessage(), e);
            return "Lỗi khi từ chối yêu cầu: " + e.getMessage();
        }
    }
}
