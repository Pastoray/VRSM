package pkg.users;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pkg.util.HibernateUtil;

import java.util.Optional; // Import Optional

/**
 * Data Access Object (DAO) for User entity operations.
 * This replaces the static, in-memory UserStorage with persistent database access.
 */
public class UserDAO {

    /**
     * Saves or updates a User entity in the database.
     * @param user The User object to persist.
     */
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user); // Use merge for both save and update
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving user: " + user.getLogin());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a User by their unique login (username).
     * @param login The login string to search for.
     * @return An Optional containing the User object if found, or an empty Optional otherwise.
     */
    public Optional<User> findByLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL (Hibernate Query Language) query to find a user by their login
            Query<User> query = session.createQuery("FROM User u WHERE u.login = :login", User.class);
            query.setParameter("login", login);

            // uniqueResultOptional() automatically returns an Optional
            return query.uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Error finding user by login: " + login);
            e.printStackTrace();
            return Optional.empty(); // Return empty Optional on error
        }
    }

    /**
     * Checks if a user with the given login already exists.
     * @param login The login string to check.
     * @return True if the user exists, false otherwise.
     */
    public boolean existsByLogin(String login) {
        // Now checks if the Optional returned by findByLogin is present
        return findByLogin(login).isPresent();
    }
}
