
package dao.device;

import models.device.AssetRequestItem;
import config.HibernateUtil;
import dao.device.interfaces.AssetRequestItemDAO;

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

    public List<AssetRequestItem> getItemsByRequestId(int requestId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AssetRequestItem> query = session.createQuery("FROM AssetRequestItem WHERE request_id = :requestId", AssetRequestItem.class);
            query.setParameter("requestId", requestId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting items by request id {}: {}", requestId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void deleteItemsByRequestId(int requestId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            // Sử dụng HQL (Hibernate Query Language) để xóa tất cả các item có requestId tương ứng
            Query query = session.createQuery("DELETE FROM AssetRequestItem WHERE request_id = :requestId");
            query.setParameter("requestId", requestId);
            query.executeUpdate(); // Thực thi lệnh xóa
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Error deleting asset request items by request id {}: {}", requestId, e.getMessage(), e);
        }
    }
}
