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
            return null;
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

    public List<Asset> getAllAvailable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Asset> query = session.createQuery("FROM Asset WHERE status = 'available'", Asset.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Error getting all available assets: {}", e.getMessage(), e);
            return null;
        }
    }
}
