package pkg.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Concrete entity representing an Admin user.
 * Mapped to the ADMINS table, joining with the USERS base table.
 */
@Entity
@Table(name = "ADMINS")
public class Admin extends User {

    // Admins don't require unique fields, but must provide constructors.

    /**
     * MANDATORY: No-argument constructor for Hibernate/JPA.
     */
    public Admin() {
        super();
    }

    /**
     * Parameterized constructor for object creation.
     */
    public Admin(String first_name, String last_name, String login, String password) {
        super(first_name, last_name, login, password);
    }

    // --- Business Methods (Action methods simplified for entity compliance) ---

    /**
     * Resets the password for another user.
     * Note: The actual database update (saving the user with the new password)
     * must be handled by the UserService/UserDAO layer.
     * @param user The user whose password is to be reset.
     * @param new_password The new password hash.
     */
    public void resetPassword(User user, String new_password)
    {
        // Sets the password on the target User object instance.
        // The DAO layer MUST be called afterwards to save this change to the database.
        user.setPassword(new_password);
        System.out.println("Admin " + getId() + " requested password reset for user " + user.getLogin());
    }

    /**
     * Deactivates a user account.
     * Note: The actual database logic (setting an 'isActive' or 'isDeactivated' field)
     * must be handled by the UserService/UserDAO layer, as the User class doesn't currently
     * have an 'active' state field. This method simply logs the intent.
     * @param user The user account to deactivate.
     */
    public void deactivateUserAccount(User user)
    {
        System.out.println("Admin " + getId() + " requested account deactivation for user " + user.getLogin());
    }
}
