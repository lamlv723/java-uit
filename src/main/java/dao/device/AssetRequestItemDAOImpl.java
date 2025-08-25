package dao.device;

import config.HibernateUtil;
import dao.device.interfaces.AssetRequestItemDAO;
import models.device.AssetRequestItem;
import models.main.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AssetRequestItemDAOImpl implements AssetRequestItemDAO {
    private static final Logger logger = LoggerFactory.getLogger(AssetRequestItemDAOImpl.class);

    @Override
    public void addAssetRequestItem(AssetRequestItem item) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(item);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error adding asset request item: {}", e.getMessage(), e);
        }
    }

    @Override
    public void updateAssetRequestItem(AssetRequestItem item) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(item);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error updating asset request item: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteAssetRequestItem(int requestItemId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            AssetRequestItem item = session.get(AssetRequestItem.class, requestItemId);
            if (item != null) {
                session.delete(item);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error deleting asset request item with id {}: {}", requestItemId, e.getMessage(), e);
        }
    }

    @Override
    public AssetRequestItem getAssetRequestItemById(int requestItemId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AssetRequestItem.class, requestItemId);
        } catch (Exception e) {
            logger.error("Error getting asset request item by id {}: {}", requestItemId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<AssetRequestItem> getAllAssetRequestItems() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AssetRequestItem> query = session.createQuery("FROM AssetRequestItem", AssetRequestItem.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all asset request items: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<AssetRequestItem> getAllBorrowedAssetRequestItems(Employee currentUser) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (currentUser == null) {
                return new java.util.ArrayList<>();
            }

            String role = currentUser.getRole();
            String baseQuery = "FROM AssetRequestItem i WHERE i.assetRequest.status NOT LIKE 'Rejected'";

            if ("Admin".equalsIgnoreCase(role)) {
                // Admin thấy tất cả
                Query<AssetRequestItem> query = session.createQuery(baseQuery, AssetRequestItem.class);
                return query.getResultList();
            } else if ("Manager".equalsIgnoreCase(role)) {
                // Manager thấy của nhân viên trong phòng ban
                String hql = baseQuery + " AND i.assetRequest.employee.department.departmentId = :deptId";
                Query<AssetRequestItem> query = session.createQuery(hql, AssetRequestItem.class);
                query.setParameter("deptId", currentUser.getDepartmentId());
                return query.getResultList();
            } else {
                // Staff chỉ thấy của chính mình
                String hql = baseQuery + " AND i.assetRequest.employee.employeeId = :empId";
                Query<AssetRequestItem> query = session.createQuery(hql, AssetRequestItem.class);
                query.setParameter("empId", currentUser.getEmployeeId());
                return query.getResultList();
            }
        } catch (Exception e) {
            logger.error("Error getting filtered list of borrowed asset request items: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public AssetRequestItem findActiveBorrowItemByAssetId(int assetId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AssetRequestItem> query = session.createQuery(
                    "FROM AssetRequestItem i WHERE i.asset.assetId = :assetId AND i.returnDate IS NULL AND i.assetRequest.requestType = 'borrow' AND i.assetRequest.status = 'Approved'",
                    AssetRequestItem.class);
            query.setParameter("assetId", assetId);
            query.setMaxResults(1);
            return query.uniqueResultOptional().orElse(null);
        } catch (Exception e) {
            logger.error("Error finding active borrow item for asset id {}: {}", assetId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<AssetRequestItem> getAssetRequestItemsByRequestId(int requestId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AssetRequestItem> query = session.createQuery(
                    "FROM AssetRequestItem WHERE assetRequest.requestId = :requestId", AssetRequestItem.class);
            query.setParameter("requestId", requestId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting asset request items by request id {}: {}", requestId, e.getMessage(), e);
            return new java.util.ArrayList<>();
        }
    }
}