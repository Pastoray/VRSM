package pkg.main;

import pkg.users.UserStorage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Scanner;
import pkg.users.User;

public class Main
{
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args)
    {
        System.out.println("--- Welcome to the VRSM Console System ---");
        select_role();
    }

    private static void select_role()
    {
        String role = null;

        while (role == null)
        {
            System.out.println("\n--- ROLE SELECTION ---");
            System.out.println("1. Customer");
            System.out.println("2. Seller");
            System.out.println("3. Admin");
            System.out.println("4. Exit Application");
            System.out.print("Please select your role (1-4): ");

            String choice = scanner.nextLine().trim();

            switch (choice)
            {
                case "1":
                    role = "Customer";
                    break;
                case "2":
                    role = "Seller";
                    break;
                case "3":
                    role = "Admin";
                    break;
                case "4":
                    System.out.println("Exiting VRSM...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 4.");
            }
        }
        login_signup_menu(role);
    }

    private static void login_signup_menu(String role)
    {
        boolean back = false;

        while (!back)
        {
            System.out.println("\n--- " + role.toUpperCase() + " ACTIONS ---");
            System.out.println("1. Log In");
            System.out.println("2. Create Account (Sign Up)");
            System.out.println("3. Back to Role Selection");
            System.out.print("Select an action (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice)
            {
                case "1":
                    handle_login(role);
                    break;
                case "2":
                    handle_signup(role);
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        select_role();
    }

    private static void handle_login(String role)
    {
        System.out.println("\n[LOGIN] Enter credentials for " + role + ".");
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("LOGIC: Checking database for " + role + " (" + login + ")...");
        System.out.println("STATUS: Authentication successful! (Placeholder)");
    }

    private static void handle_signup(String role)
    {
        System.out.println("\n--- SIGNUP: Creating a new " + role.toUpperCase() + " account ---");

        try
        {
            String className = "pkg.users." + role;
            Class<?> userClass = Class.forName(className);

            Constructor<?> constructor = get_largest_constructor(userClass);
            if (constructor == null)
            {
                System.err.println("Error: Could not find a suitable constructor for " + role + ".");
                return;
            }

            Parameter[] parameters = constructor.getParameters();
            Object[] arguments = new Object[parameters.length];

            System.out.println("\nPlease enter the required information:");

            for (int i = 0; i < parameters.length; i++)
            {
                Parameter p = parameters[i];

                String fieldName = p.getName().replace('_', ' ');
                System.out.print(fieldName + " (" + p.getType().getSimpleName() + "): ");

                arguments[i] = read_and_cast_input(p.getType());
            }

            User newUser = (User) constructor.newInstance(arguments);

            UserStorage.add_user(newUser);
            System.out.println("\nSUCCESS: " + role + " account created and stored! Login: " + newUser.get_login());

        }
        catch (Exception e)
        {
            System.err.println("\n❌ SIGNUP ERROR: An unexpected error occurred during creation.");
            System.err.println("Detail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Constructor<?> get_largest_constructor(Class<?> cls)
    {
        Constructor<?> largest = null;
        int maxParams = -1;
        for (Constructor<?> c : cls.getDeclaredConstructors())
        {
            if (c.getParameterCount() > maxParams)
            {
                maxParams = c.getParameterCount();
                largest = c;
            }
        }
        return largest;
    }

    private static Object read_and_cast_input(Class<?> targetType)
    {
        String input = scanner.nextLine().trim();

        if (targetType == String.class)
        {
            return input;
        }
        else if (targetType == int.class)
        {
            try
            {
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e)
            {
                System.err.println("Invalid input. Defaulting to 0.");
                return null;
            }
        }
        else if (targetType == double.class)
        {
            try
            {
                return Double.parseDouble(input);
            }
            catch (NumberFormatException e)
            {
                System.err.println("Invalid input. Defaulting to 0.0.");
                return null;
            }
        }

        System.err.println("Warning: Unsupported parameter type " + targetType.getSimpleName() + ". Defaulting to null.");
        return null;
    }
}
