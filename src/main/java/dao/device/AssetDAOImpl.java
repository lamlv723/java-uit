package dao.device;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import config.HibernateUtil;
import models.device.Asset;
import dao.device.interfaces.AssetDAO;
import java.util.List;

public class AssetDAOImpl implements AssetDAO {
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
            e.printStackTrace();
        }
    }

    @Override
    public Asset getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Asset.class, id);
        }
    }

    @Override
    public List<Asset> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Asset> query = session.createQuery("from Asset", Asset.class);
            return query.list();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
