package dao.main;

import dao.main.interfaces.DepartmentDAO;
import models.main.Department;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class DepartmentDAOImpl implements DepartmentDAO {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentDAOImpl.class);

    @Override
    public void addDepartment(Department department) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(department);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error adding department: {}", e.getMessage(), e);
        }
    }

    @Override
    public void updateDepartment(Department department) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(department);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error updating department: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteDepartment(int departmentId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Department dept = session.get(Department.class, departmentId);
            if (dept != null) {
                session.delete(dept);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error deleting department with id {}: {}", departmentId, e.getMessage(), e);
        }
    }

    @Override
    public Department getDepartmentById(int departmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Department.class, departmentId);
        } catch (Exception e) {
            logger.error("Error getting department by id {}: {}", departmentId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Department> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Department> query = session.createQuery("FROM Department", Department.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all departments: {}", e.getMessage(), e);
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public Department findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Department> query = session.createQuery("FROM Department WHERE departmentName = :name",
                    Department.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding department by name {}: {}", name, e.getMessage(), e);
            return null;
        }
    }
}
