package pkg.transactions;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pkg.util.HibernateUtil;
import pkg.users.Customer;

import java.util.List;
import java.util.Optional;

public class SaleDAO {

    public List<Sale> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Sale> query = session.createQuery("FROM Sale", Sale.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error loading sales: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public Optional<Sale> findById(Long id) {
        if (id == null) return Optional.empty();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Sale sale = session.get(Sale.class, id);
            return Optional.ofNullable(sale);
        } catch (Exception e) {
            System.err.println("Error finding sale: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void save(Sale sale) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(sale);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error saving sale: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Sale> findByCustomer(Customer customer) {
        if (customer == null || customer.getId() == null) {
            return List.of();
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Sale> query = session.createQuery(
                "FROM Sale s WHERE s.customer = :customer", Sale.class);
            query.setParameter("customer", customer);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error loading sales for customer: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
}
