package pkg.users;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pkg.util.HibernateUtil;
import pkg.vehicules.VehiculeDAO;

import java.util.List;
import java.util.Optional;

public class UserDAO {

    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving user: " + (user != null ? user.getLogin() : "null"));
            e.printStackTrace();
        }
    }

    public Optional<User> findByLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User u WHERE u.login = :login", User.class);
            query.setParameter("login", login);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Error finding user by login: " + login);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean existsByLogin(String login) {
        return findByLogin(login).isPresent();
    }



    public List<Customer> findAllCustomers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error fetching all customers");
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Seller> findAllSellers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Seller> query = session.createQuery("FROM Seller", Seller.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error fetching all sellers");
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Admin> findAllAdmins() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Admin> query = session.createQuery("FROM Admin", Admin.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error fetching all admins");
            e.printStackTrace();
            return List.of();
        }
    }

    public List<StockManager> findAllStockManagers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<StockManager> query = session.createQuery("FROM StockManager", StockManager.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error fetching all stock managers");
            e.printStackTrace();
            return List.of();
        }
    }

    public void delete(User user) {
        if (user == null || user.getId() == null) {
            System.err.println("Cannot delete null or unsaved user.");
            return;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();


            if (user instanceof Seller seller) {
                VehiculeDAO vehiculeDAO = new VehiculeDAO();
                vehiculeDAO.unassignSeller(seller);
            }


            User managed = session.merge(user);
            session.remove(managed);
            transaction.commit();
            System.out.println("Deleted user: " + user.getLogin());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
