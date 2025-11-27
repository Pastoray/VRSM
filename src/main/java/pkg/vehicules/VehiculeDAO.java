package pkg.vehicules;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pkg.util.HibernateUtil;
import pkg.users.Seller;

import java.util.List;
import java.util.Optional;

public class VehiculeDAO {

    public void saveOrUpdate(Vehicule vehicule) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(vehicule);
            transaction.commit();
            System.out.println("Saved/Updated vehicle: " + vehicule.getBrand() + " " + vehicule.getModel());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error saving vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Optional<Vehicule> findById(Long id) {
        if (id == null) return Optional.empty();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Vehicule v = session.get(Vehicule.class, id);
            return Optional.ofNullable(v);
        } catch (Exception e) {
            System.err.println("Error finding vehicle by ID: " + id);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(Vehicule vehicule) {
        if (vehicule == null) {
            System.err.println("Cannot delete null or unsaved vehicle.");
            return;
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Vehicule managed = session.merge(vehicule);
            session.remove(managed);
            transaction.commit();
            System.out.println("Deleted vehicle ID: " + vehicule.getId());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error deleting vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Vehicule> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Vehicule> query = session.createQuery("FROM Vehicule", Vehicule.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error loading all vehicles.");
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Vehicule> findAllAvailable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Vehicule> query = session.createQuery(
                "SELECT v FROM Vehicule v WHERE v.available = true", Vehicule.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error loading available vehicles.");
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Vehicule> findBySeller(Seller seller) {
        if (seller == null || seller.getId() == null) {
            return List.of();
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Vehicule> query = session.createQuery(
                "FROM Vehicule v WHERE v.assignedSeller = :seller", Vehicule.class);
            query.setParameter("seller", seller);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error loading vehicles for seller: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    public void unassignSeller(Seller seller) {
        if (seller == null || seller.getId() == null) return;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "UPDATE Vehicule v SET v.assignedSeller = null WHERE v.assignedSeller = :seller";
            session.createQuery(hql)
                   .setParameter("seller", seller)
                   .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error unassigning seller: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
