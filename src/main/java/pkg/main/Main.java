package pkg.main;

import pkg.users.Admin;
import pkg.users.CustomerType;
import pkg.users.Customer;
import pkg.users.Seller;
import pkg.users.User;
import pkg.users.UserDAO;
import pkg.users.StockManager; // Assuming StockManager is also a role
import pkg.util.HibernateUtil; // Used for potential system shutdown/cleanup
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Optional;

/**
 * Main application console entry point. Handles user role selection,
 * login, and account creation (Sign Up), persisting data using UserDAO.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        System.out.println("--- Welcome to the VRSM Console System ---");
        System.out.println("Initializing Database Connection...");

        // Ensure Hibernate is initialized (optional, but good practice for cleanup)
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("Database connection ready.");
            selectRole();
        } catch (Exception e) {
            System.err.println("FATAL: Could not initialize database. Application cannot run.");
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            // HibernateUtil.shutdown(); // Uncomment if you need explicit shutdown
        }
    }

    private static void selectRole() {
        String role = null;

        while (role == null) {
            System.out.println("\n--- ROLE SELECTION ---");
            System.out.println("1. Customer");
            System.out.println("2. Seller");
            System.out.println("3. Admin");
            System.out.println("4. StockManager");
            System.out.println("5. Exit Application");
            System.out.print("Please select your role (1-5): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": role = "Customer"; break;
                case "2": role = "Seller"; break;
                case "3": role = "Admin"; break;
                case "4": role = "StockManager"; break;
                case "5": System.out.println("Exiting VRSM..."); return;
                default: System.out.println("Invalid choice. Please enter a number from 1 to 5.");
            }
        }
        loginSignupMenu(role);
    }

    private static void loginSignupMenu(String role) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- " + role.toUpperCase() + " ACTIONS ---");
            System.out.println("1. Log In");
            System.out.println("2. Create Account (Sign Up)");
            System.out.println("3. Back to Role Selection");
            System.out.print("Select an action (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": handleLogin(role); break;
                case "2": handleSignup(role); break;
                case "3": back = true; break;
                default: System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        selectRole();
    }

    /**
     * Attempts to authenticate a user against the database.
     */
    private static void handleLogin(String role) {
        System.out.println("\n[LOGIN] Enter credentials for " + role + ".");
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("LOGIC: Checking database for " + role + " (" + login + ")...");

        Optional<User> userOpt = userDAO.findByLogin(login);

        if (userOpt.isEmpty()) {
            System.out.println("STATUS: Login failed. User not found.");
            return;
        }

        User user = userOpt.get();

        // 1. Check if the retrieved user's type matches the requested role
        // This prevents a Customer from logging in as an Admin even if they share a login
        String actualRole = user.getClass().getSimpleName();
        if (!actualRole.equals(role)) {
            System.out.println("STATUS: Login failed. User found but role mismatch (Logged in as " + actualRole + ").");
            return;
        }

        // 2. Simple password check (In a real app, use hashing like BCrypt)
        if (user.getPassword().equals(password)) {
            System.out.println("STATUS: Authentication successful! Welcome, " + user.getFirstName() + " (" + actualRole + ")!");
            // TODO: Launch the appropriate User Menu (e.g., CustomerMenu.start(user))
            System.out.println("Current User ID: " + user.getId());
            // For now, we return to the menu
        } else {
            System.out.println("STATUS: Login failed. Incorrect password.");
        }
    }

    /**
     * Handles account creation based on the selected role.
     * Uses specific forms for each role instead of reflection.
     */
    private static void handleSignup(String role) {
        System.out.println("\n--- SIGNUP: Creating a new " + role.toUpperCase() + " account ---");

        System.out.print("First Name: "); String fn = scanner.nextLine();
        System.out.print("Last Name: "); String ln = scanner.nextLine();
        System.out.print("Desired Login: "); String log = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();

        try {
            User newUser = null;

            switch (role) {
                case "Customer":
                    newUser = signupCustomer(fn, ln, log, pass);
                    break;
                case "Seller":
                    newUser = signupSeller(fn, ln, log, pass);
                    break;
                case "Admin":
                    newUser = signupAdmin(fn, ln, log, pass);
                    break;
                case "StockManager":
                    newUser = signupStockManager(fn, ln, log, pass);
                    break;
                default:
                    System.err.println("Invalid role selected for signup.");
                    return;
            }

            if (newUser != null) {
                userDAO.save(newUser);
                System.out.println("\nSUCCESS: " + role + " account created and saved to DB! Login: " + newUser.getLogin());
            }

        } catch (IllegalArgumentException e) {
            System.err.println("\nSIGNUP ERROR: Invalid input for Customer Type or other field.");
        } catch (Exception e) {
            System.err.println("\nSIGNUP ERROR: An unexpected database error occurred.");
            e.printStackTrace();
        }
    }

    // --- Specific Role Signup Methods ---

    private static User signupCustomer(String fn, String ln, String log, String pass) {
        System.out.print("Address: "); String address = scanner.nextLine();
        System.out.print("Phone: "); String phone = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();

        System.out.print("Customer Type (INDIVIDUAL/CORPORATE): ");
        String typeInput = scanner.nextLine().toUpperCase();

        // This relies on the CustomerType enum having INDIVIDUAL and CORPORATE values.
        CustomerType type = CustomerType.valueOf(typeInput);

        return new Customer(fn, ln, log, pass, address, phone, email, type);
    }

    private static User signupSeller(String fn, String ln, String log, String pass) {
        System.out.print("Employee ID: ");
        // Read string input for the employee ID, as the constructor likely expects a String
        String employeeId = scanner.nextLine();

        return new Seller(fn, ln, log, pass, 0, 0);
    }

    private static User signupAdmin(String fn, String ln, String log, String pass) {
        // Admins only require base User fields for creation
        return new Admin(fn, ln, log, pass);
    }

    private static User signupStockManager(String fn, String ln, String log, String pass) {
        System.out.print("Assigned Warehouse Location (e.g., 'Warehouse_A_NY'): ");
        String warehouseLocation = scanner.nextLine();

        return new StockManager(fn, ln, log, pass, warehouseLocation);
    }
}
