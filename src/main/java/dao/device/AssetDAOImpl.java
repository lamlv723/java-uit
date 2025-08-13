package dao.device;

import models.device.Asset;
import models.device.AssetCategory;
import models.device.Vendor;
import config.HibernateUtil;
import dao.device.interfaces.AssetDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class AssetDAOImpl implements AssetDAO {
    private static final Logger logger = LoggerFactory.getLogger(AssetDAOImpl.class);

    @Override
    public void save(Asset asset) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Load lại category từ DB (nếu có)
            if (asset.getCategory() != null && asset.getCategory().getCategoryId() != null) {
                AssetCategory attachedCategory = session.get(AssetCategory.class, asset.getCategory().getCategoryId());
                if (attachedCategory == null) {
                    throw new IllegalArgumentException("AssetCategory ID không tồn tại trong DB.");
                }
                asset.setCategory(attachedCategory);
            } else {
                throw new IllegalArgumentException("Thiếu AssetCategory ID.");
            }

            // Tương tự nếu bạn có Vendor
            if (asset.getVendor() != null && asset.getVendor().getVendorId() != null) {
                Vendor attachedVendor = session.get(Vendor.class, asset.getVendor().getVendorId());
                if (attachedVendor != null) {
                    asset.setVendor(attachedVendor);
                }
            }

            session.save(asset);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("❌ Lỗi khi thêm tài sản: {}", e.getMessage(), e);
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }

        }
    }

    @Override
    public Asset getByAssetTag(String assetTag) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Asset> query = session.createQuery(
                    "FROM Asset WHERE assetTag = :assetTag", Asset.class);
            query.setParameter("assetTag", assetTag);
            return query.uniqueResult();
        }
    }

    @Override
    public Asset getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Asset.class, id);
        } catch (Exception e) {
            logger.error("❌ Error getting asset by id {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Asset getByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Asset.class, name);
        } catch (Exception e) {
            logger.error("❌ Error getting asset by name {}: {}", name, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Asset> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Asset> query = session.createQuery("FROM Asset ORDER BY assetTag ASC", Asset.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("❌ Error getting all assets: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void update(Asset asset) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(asset);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("❌ Error updating asset: {}", e.getMessage(), e);
        } finally {
            if (session != null)
                session.close();
        }
    }

    @Override
    public void delete(Asset asset) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.delete(asset);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("❌ Error deleting asset: {}", e.getMessage(), e);
        } finally {
            if (session != null)
                session.close();
        }
    }
}
