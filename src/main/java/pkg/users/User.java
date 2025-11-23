package pkg.users;

import jakarta.persistence.*;

/**
 * Abstract base class for all User types (e.g., Admin, Customer).
 * Uses Joined Inheritance Strategy.
 */
@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.JOINED) // Defines how the subclasses map to tables
public abstract class User
{
    // 1. FIX: Use JPA annotations for ID management
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Use Long for auto-generated IDs

    @Column(name = "FIRST_NAME")
    private String first_name;

    @Column(name = "LAST_NAME")
    private String last_name;

    // The login must be unique and non-null
    @Column(name = "LOGIN", unique = true, nullable = false)
    private String login;

    @Column(name = "PASSWORD_HASH")
    private String password;

    // 2. MANDATORY FIX: Add no-argument constructor for Hibernate
    /**
     * Required by Hibernate/JPA for instantiation when loading data from the DB.
     */
    public User()
    {
        // Must be empty!
    }

    // Parameterized constructor for object creation (manual ID generation removed)
    public User(String first_name, String last_name, String login, String password)
    {
        // REMOVED: this.id = genID(); -- Let the DB/Hibernate handle the ID
        this.first_name = first_name;
        this.last_name = last_name;
        this.login = login;
        this.password = password;
    }

    // 3. REMOVED: static long g_id and genID() are no longer needed.
    // --- Getters ---

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    // --- Setters (Simplified for JPA Compliance) ---
    // The security logic (if (!(caller instanceof Admin))) must be moved to UserDAO or a UserService.
    // Hibernate REQUIRES simple setters/field access to populate the object from the database.

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
