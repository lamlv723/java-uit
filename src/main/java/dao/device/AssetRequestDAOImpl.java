package dao.device;

import models.device.AssetRequest;
import config.HibernateUtil;
import dao.device.interfaces.AssetRequestDAO;

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
    public List<AssetRequest> getAllAssetRequests() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AssetRequest> query = session.createQuery("FROM AssetRequest", AssetRequest.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all asset requests: {}", e.getMessage(), e);
            return null;
        }
    }
}
