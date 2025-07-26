package dao.device;

import models.device.AssetRequest;
import config.HibernateUtil;
import dao.device.interfaces.AssetRequestDAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class AssetRequestDAOImpl implements AssetRequestDAO {
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public AssetRequest getAssetRequestById(int requestId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AssetRequest.class, requestId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<AssetRequest> getAllAssetRequests() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<AssetRequest> query = session.createQuery("FROM AssetRequest", AssetRequest.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
