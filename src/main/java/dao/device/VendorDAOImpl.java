package dao.device;

import models.device.Vendor;
import dao.device.interfaces.VendorDAO;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class VendorDAOImpl implements VendorDAO {
    @Override
    public void addVendor(Vendor vendor) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(vendor);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void updateVendor(Vendor vendor) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(vendor);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void deleteVendor(int vendorId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Vendor v = session.get(Vendor.class, vendorId);
            if (v != null)
                session.delete(v);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Vendor getVendorById(int vendorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Vendor.class, vendorId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Vendor> getAllVendors() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Vendor> query = session.createQuery("FROM Vendor", Vendor.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
