package dao.device;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import config.HibernateUtil;
import models.device.Asset;
import dao.device.interfaces.AssetDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class AssetDAOImpl implements AssetDAO {
    private static final Logger logger = LoggerFactory.getLogger(AssetDAOImpl.class);

    @Override
    public void save(Asset asset) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(asset);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error("Error saving asset: {}", e.getMessage(), e);
        }
    }

    @Override
    public Asset getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Asset.class, id);
        } catch (Exception e) {
            logger.error("Error getting asset by id {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Asset> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Asset> query = session.createQuery("from Asset", Asset.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error getting all assets: {}", e.getMessage(), e);
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public void update(Asset asset) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(asset);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error("Error updating asset: {}", e.getMessage(), e);
        }
    }

    @Override
    public void delete(Asset asset) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(asset);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error("Error deleting asset: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<Asset> getAllAvailableAssets() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Asset> query = session.createQuery("FROM Asset WHERE status = 'Available'", Asset.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error getting all available assets: {}", e.getMessage(), e);
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public List<Asset> getBorrowedAssetsByEmployeeId(int employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Asset> query = session.createQuery(
                    "SELECT i.asset FROM AssetRequestItem i " +
                            "WHERE i.assetRequest.employee.employeeId = :employeeId " +
                            "AND i.assetRequest.requestType = 'borrow' " +
                            "AND i.assetRequest.status = 'Approved' " +
                            "AND i.returnDate IS NULL",
                    Asset.class);
            query.setParameter("employeeId", employeeId);
            return query.list();
        } catch (Exception e) {
            logger.error("Error getting borrowed assets by employee id {}: {}", employeeId, e.getMessage(), e);
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public long countAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = session.createQuery("SELECT COUNT(a) FROM Asset a", Long.class);
            return q.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting all assets: {}", e.getMessage(), e);
            return 0L;
        }
    }

    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> q = session.createQuery("SELECT COUNT(a) FROM Asset a WHERE a.status = :st", Long.class);
            q.setParameter("st", status);
            return q.uniqueResult();
        } catch (Exception e) {
            logger.error("Error counting assets by status {}: {}", status, e.getMessage(), e);
            return 0L;
        }
    }

    @Override
    public Asset findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Asset> query = session.createQuery("FROM Asset WHERE assetName = :name", Asset.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding asset by name {}: {}", name, e.getMessage(), e);
            return null;
        }
    }
}