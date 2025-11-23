package pkg.vehicules;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pkg.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for the VehiculeJ entity hierarchy (Car, Truck, etc.).
 * Handles CRUD operations and custom queries related to vehicles.
 */
public class VehiculeDAO {

    /**
     * Saves or updates a VehiculeJ entity.
     * @param vehicule The vehicle object to persist.
     */
    public void saveOrUpdate(Vehicule vehicule) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(vehicule); // Use merge for both save and update
            transaction.commit();
            System.out.println("Saved/Updated vehicle: " + vehicule.getBrand() + " " + vehicule.getModel());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error persisting vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Finds a VehiculeJ entity by its ID.
     * @param id The ID of the vehicle.
     * @return An Optional containing the VehiculeJ, or empty if not found.
     */
    public Optional<Vehicule> findById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Use get(Class, id) to retrieve a concrete type, or load the base type
            return Optional.ofNullable(session.get(Vehicule.class, id));
        } catch (Exception e) {
            System.err.println("Error finding vehicle by ID: " + id + ". " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Deletes a VehiculeJ entity.
     * @param vehicule The vehicle object to delete.
     */
    public void delete(Vehicule vehicule) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Need to merge first if the object is detached (common case)
            session.remove(session.merge(vehicule));
            transaction.commit();
            System.out.println("Deleted vehicle ID: " + vehicule.getId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting vehicle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all vehicles from the database.
     * @return A list of all VehiculeJ entities.
     */
    public List<Vehicule> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL (Hibernate Query Language) to select all entities of the base type
            Query<Vehicule> query = session.createQuery("FROM Vehicule", Vehicule.class);
            return query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving all vehicles: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // Example of a custom query
    /**
     * Finds all vehicles currently marked as available.
     * @return A list of available VehiculeJ entities.
     */
    public List<Vehicule> findAllAvailable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Vehicule> query = session.createQuery(
                "FROM Vehicule WHERE available = true",
                Vehicule.class
            );
            return query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving available vehicles: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
}
