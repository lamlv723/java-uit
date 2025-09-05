package dao.device;

import models.device.AssetRequest;
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
            return null;
        }
    }
}
