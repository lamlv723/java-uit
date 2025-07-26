package dao.device;

import models.device.AssetCategory;
import dao.device.interfaces.AssetCategoryDAO;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class AssetCategoryDAOImpl implements AssetCategoryDAO {
    private static final Logger logger = LoggerFactory.getLogger(AssetCategoryDAOImpl.class);

    @Override
    public void addAssetCategory(AssetCategory category) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(category);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error adding asset category: {}", e.getMessage(), e);
        }
    }

    @Override
    public void updateAssetCategory(AssetCategory category) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(category);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error updating asset category: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteAssetCategory(int categoryId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            AssetCategory cat = session.get(AssetCategory.class, categoryId);
            if (cat != null)
                session.delete(cat);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error deleting asset category with id {}: {}", categoryId, e.getMessage(), e);
        }
    }

    @Override
    public AssetCategory getAssetCategoryById(int categoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AssetCategory.class, categoryId);
        } catch (Exception e) {
            logger.error("Error getting asset category by id {}: {}", categoryId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<AssetCategory> getAllAssetCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AssetCategory> query = session.createQuery("FROM AssetCategory", AssetCategory.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all asset categories: {}", e.getMessage(), e);
            return null;
        }
    }
}
