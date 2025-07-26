
package dao.device;

import models.device.Vendor;
import dao.device.interfaces.VendorDAO;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class VendorDAOImpl implements VendorDAO {
    private static final Logger logger = LoggerFactory.getLogger(VendorDAOImpl.class);

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
            logger.error("Error adding vendor: {}", e.getMessage(), e);
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
            logger.error("Error updating vendor: {}", e.getMessage(), e);
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
            logger.error("Error deleting vendor with id {}: {}", vendorId, e.getMessage(), e);
        }
    }

    @Override
    public Vendor getVendorById(int vendorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Vendor.class, vendorId);
        } catch (Exception e) {
            logger.error("Error getting vendor by id {}: {}", vendorId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Vendor> getAllVendors() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Vendor> query = session.createQuery("FROM Vendor", Vendor.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all vendors: {}", e.getMessage(), e);
            return null;
        }
    }
}
