
package dao.device;

import models.device.AssetRequestItem;
import config.HibernateUtil;
import dao.device.interfaces.AssetRequestItemDAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class AssetRequestItemDAOImpl implements AssetRequestItemDAO {
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public AssetRequestItem getAssetRequestItemById(int requestItemId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AssetRequestItem.class, requestItemId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<AssetRequestItem> getAllAssetRequestItems() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AssetRequestItem> query = session.createQuery("FROM AssetRequestItem", AssetRequestItem.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
