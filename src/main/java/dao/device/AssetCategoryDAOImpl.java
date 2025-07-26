package dao.device;

import models.device.AssetCategory;
import dao.device.interfaces.AssetCategoryDAO;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class AssetCategoryDAOImpl implements AssetCategoryDAO {
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public AssetCategory getAssetCategoryById(int categoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AssetCategory.class, categoryId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<AssetCategory> getAllAssetCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AssetCategory> query = session.createQuery("FROM AssetCategory", AssetCategory.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
