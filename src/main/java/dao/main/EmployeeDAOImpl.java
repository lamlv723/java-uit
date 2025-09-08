package dao.main;

import models.main.Employee;
import dao.main.interfaces.EmployeeDAO;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAOImpl.class);

    @Override
    public void addEmployee(Employee employee) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error adding employee: {}", e.getMessage(), e);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error updating employee: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteEmployee(int employeeId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Employee emp = session.get(Employee.class, employeeId);
            if (emp != null)
                session.delete(emp);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error deleting employee with id {}: {}", employeeId, e.getMessage(), e);
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Employee.class, employeeId);
        } catch (Exception e) {
            logger.error("Error getting employee by id {}: {}", employeeId, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Employee> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery("FROM Employee", Employee.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all employees: {}", e.getMessage(), e);
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public List<Employee> getByDepartmentId(int departmentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery("FROM Employee WHERE department.departmentId = :deptId",
                    Employee.class);
            query.setParameter("deptId", departmentId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting employees by department id {}: {}", departmentId, e.getMessage(), e);
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public Employee getEmployeeByUsernameAndPassword(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery(
                    "FROM Employee WHERE username = :username AND password = :password",
                    Employee.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error getting employee by username and password: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Employee> getEmployeesByRole(String role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery("FROM Employee WHERE role = :role", Employee.class);
            query.setParameter("role", role);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting employees by role '{}': {}", role, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
