package pkg.main;

import pkg.users.*;
import pkg.vehicules.Vehicule;
import pkg.users.UserDAO;
import pkg.util.HibernateUtil;
import pkg.vehicules.VehiculeDAO;
import pkg.transactions.Sale;
import pkg.transactions.SaleDAO;
import pkg.vehicules.FuelType;
import pkg.vehicules.MotorcycleType;
import pkg.vehicules.Car;
import pkg.vehicules.Truck;
import pkg.vehicules.Motorcycle;
import pkg.transactions.Discount;
import pkg.transactions.DiscountType;
import pkg.transactions.ApplicableTo;
import pkg.transactions.Payment;
import pkg.transactions.PaymentType;
import pkg.transactions.SaleStatus;
import pkg.transactions.SaleType;
import java.time.LocalDate;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        System.out.println("--- Welcome to the VRSM Console System ---");
        System.out.println("Initializing Database Connection...");

        System.out.print("Would you like the (Console/GUI) application: ");

        String option = scanner.nextLine();
        if (Objects.equals(option.toUpperCase(), "GUI"))
        {
            pkg.main.VRSMGUI.launch();
        }
        else if (Objects.equals(option.toUpperCase(), "CONSOLE"))
        {
            try {
                HibernateUtil.getSessionFactory();
                System.out.println("Database connection ready.\n");
                mainMenu();
            } catch (Exception e) {
                System.err.println("Error: Could not initialize database. Application cannot run.");
                e.printStackTrace();
            } finally {
                scanner.close();

            }
        }
        else
        {
            System.out.println("Unknown option, please choose 'Console' or 'GUI' next time.");
        }
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Display Entries");
            System.out.println("2. Login / Register");
            System.out.println("3. Exit Application");
            System.out.print("Choose an option (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayEntriesMenu();
                    break;
                case "2":
                    loginOrRegisterMenu();
                    break;
                case "3":
                    System.out.println("Exiting VRSM...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private static void displayEntriesMenu() {
        System.out.println("\n--- DISPLAY ENTRIES ---");
        System.out.println("Which table would you like to view?");
        System.out.println("1. Vehicules");
        System.out.println("2. Customers");
        System.out.println("3. Sellers");
        System.out.println("4. Sales");
        System.out.println("5. Back to Main Menu");
        System.out.print("Select (1-5): ");

        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1":
                    displayVehicules();
                    break;
                case "2":
                    displayCustomers();
                    break;
                case "3":
                    displaySellers();
                    break;
                case "4":
                    displaySales();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private static void displayVehicules() {
        VehiculeDAO vehiculeDAO = new VehiculeDAO();
        List<Vehicule> vehicules = vehiculeDAO.findAll();
        if (vehicules.isEmpty()) {
            System.out.println("No vehicles in showroom.");
        } else {
            System.out.println("\n--- ALL VEHICLES ---");
            for (Vehicule v : vehicules) {

                System.out.printf("ID: %d | Type: %s | %s %s (%d) | Price: $%.2f | Available: %s%n",
                    v.getId(),
                    v.getClass().getSimpleName(),
                    v.getBrand(),
                    v.getModel(),
                    v.getYear(),
                    v.getPrice(),
                    v.isAvailable()
                );
            }
        }
    }

    private static void displayCustomers() {
        System.out.println("\n[DISPLAY] Customers");
        List<Customer> customers = userDAO.findAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer c : customers) {
                System.out.printf("ID: %d | Name: %s %s | Login: %s | Type: %s%n",
                        c.getId(), c.getFirstName(), c.getLastName(), c.getLogin(), c.getType());
            }
        }
    }

    private static void displaySellers() {
        System.out.println("\n[DISPLAY] Sellers");
        List<Seller> sellers = userDAO.findAllSellers();
        if (sellers.isEmpty()) {
            System.out.println("No sellers found.");
        } else {
            for (Seller s : sellers) {
                System.out.printf("ID: %d | Name: %s %s | Login: %s | Quota: %d%n",
                        s.getId(), s.getFirstName(), s.getLastName(), s.getLogin(), s.getMonthlySalesQuota());
            }
        }
    }

    private static void displaySales() {
        SaleDAO saleDAO = new SaleDAO();
        List<Sale> sales = saleDAO.findAll();

        if (sales.isEmpty()) {
            System.out.println("\nNo sales recorded yet.");
            return;
        }

        System.out.println("\n--- ALL SALES ---");
        System.out.printf("%-6s %-12s %-15s %-20s %-12s %-12s %-10s%n",
                "ID", "Date", "Customer", "Vehicle", "Total", "Type", "Status");

        for (Sale s : sales) {

            String customerName = (s.get_customer() != null)
                    ? s.get_customer().getFirstName() + " " + s.get_customer().getLastName()
                    : "Unknown";
            String vehicleInfo = (s.get_vehicle() != null)
                    ? s.get_vehicle().getBrand() + " " + s.get_vehicle().getModel()
                    : "Unknown Vehicle";

            System.out.printf("%-6d %-12s %-15s %-20s $%-11.2f %-12s %-10s%n",
                    s.get_id(),
                    s.get_date() != null ? s.get_date().toString() : "N/A",
                    customerName,
                    vehicleInfo,
                    s.get_total_amount(),
                    s.get_type() != null ? s.get_type().name() : "N/A",
                    s.get_status() != null ? s.get_status().name() : "N/A"
            );
        }
    }

    private static void loginOrRegisterMenu() {
        System.out.println("\n--- LOGIN / REGISTER ---");
        System.out.println("1. Log In");
        System.out.println("2. Create Account (Sign Up)");
        System.out.println("3. Back to Main Menu");
        System.out.print("Select (1-3): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                selectRoleForAuth("login");
                break;
            case "2":
                selectRoleForAuth("signup");
                break;
            case "3":
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void selectRoleForAuth(String action) {
        System.out.println("\n--- SELECT YOUR ROLE ---");
        System.out.println("1. Customer");
        System.out.println("2. Seller");
        System.out.println("3. Admin");
        System.out.println("4. StockManager");
        System.out.println("5. Back");
        System.out.print("Choose role (1-5): ");

        String choice = scanner.nextLine().trim();

        String role = switch (choice) {
            case "1" -> "Customer";
            case "2" -> "Seller";
            case "3" -> "Admin";
            case "4" -> "StockManager";
            case "5" -> null;
            default -> {
                System.out.println("Invalid role selection.");
                yield null;
            }
        };

        if (role == null) return;

        if ("login".equals(action)) {
            handleLogin(role);
        } else {
            handleSignup(role);
        }
    }



    private static void handleLogin(String role) {
        System.out.println("\n[LOGIN] Enter credentials for " + role + ".");
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> userOpt = userDAO.findByLogin(login);
        if (userOpt.isEmpty()) {
            System.out.println("Login failed. User not found.");
            return;
        }

        User user = userOpt.get();
        String actualRole = user.getClass().getSimpleName();

        if (!actualRole.equals(role)) {
            System.out.println("Login failed. Role mismatch (found: " + actualRole + ").");
            return;
        }

        if (user.getPassword().equals(password)) {
            System.out.println("Login successful! Welcome, " + user.getFirstName() + " (" + actualRole + ")!");
            switch (actualRole) {
                case "Customer" -> customerMenu((Customer) user);
                case "Seller" -> sellerMenu((Seller) user);
                case "StockManager" -> stockManagerMenu((StockManager) user);
                case "Admin" -> adminMenu((Admin) user);
                default -> System.out.println("Unsupported role.");
            }
        } else {
            System.out.println("Incorrect password.");
        }
    }

    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. View My Info");
            System.out.println("2. View My Purchases");
            System.out.println("3. Deposit Funds");
            System.out.println("4. Buy Vehicle");
            System.out.println("5. Logout");
            System.out.print("Choose option (1-5): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> displayCustomerInfo(customer);
                case "2" -> displayCustomerPurchases(customer);
                case "3" -> depositFunds(customer);
                case "4" -> buyVehicle(customer);
                case "5" -> { System.out.println("Logging out..."); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void depositFunds(Customer customer) {
        System.out.print("\nEnter deposit amount: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
            customer.deposit(amount);
            userDAO.save(customer);
            System.out.printf("$%.2f deposited. New balance: $%.2f%n", amount, customer.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private static void buyVehicle(Customer customer) {
        System.out.println("\n--- BUY VEHICLE ---");


        VehiculeDAO vehiculeDAO = new VehiculeDAO();
        List<Vehicule> availableVehicles = vehiculeDAO.findAllAvailable();

        if (availableVehicles.isEmpty()) {
            System.out.println("No vehicles available for sale.");
            return;
        }

        System.out.println("\n--- AVAILABLE VEHICLES ---");
        for (int i = 0; i < availableVehicles.size(); i++) {
            Vehicule v = availableVehicles.get(i);
            System.out.printf("%d. %s %s (%s) - $%.2f%n",
                    i + 1, v.getBrand(), v.getModel(), v.getClass().getSimpleName(), v.getPrice());
        }


        System.out.print("Select vehicle (1-" + availableVehicles.size() + "): ");
        Vehicule selectedVehicle;
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice < 0 || choice >= availableVehicles.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            selectedVehicle = availableVehicles.get(choice);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
            return;
        }


        double price = selectedVehicle.getPrice();
        if (customer.getBalance() < price) {
            System.out.printf("Insufficient funds! Vehicle costs $%.2f, your balance: $%.2f%n", price, customer.getBalance());
            System.out.println("Tip: Use 'Deposit Funds' to add money.");
            return;
        }


        System.out.printf("\nBuy %s %s for $%.2f? (y/n): ",
                selectedVehicle.getBrand(), selectedVehicle.getModel(), price);
        if (!scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            System.out.println("Purchase cancelled.");
            return;
        }


        customer.setBalance(customer.getBalance() - price);
        userDAO.save(customer);


        Sale sale = new Sale(selectedVehicle, customer, SaleType.ONE_TIME, null);
        sale.update_total_amount(price);


        Payment payment = new Payment(price, PaymentType.CASH);
        sale.add_payment(payment);


        UserDAO userDAO = new UserDAO();
        List<Seller> sellers = userDAO.findAllSellers();
        if (!sellers.isEmpty()) {
            sale.set_seller(sellers.get(0));
        }


        SaleDAO saleDAO = new SaleDAO();
        saleDAO.save(sale);


        selectedVehicle.setAvailable(false);
        vehiculeDAO.saveOrUpdate(selectedVehicle);

        System.out.println("Purchase successful! Vehicle is now yours.");
        System.out.printf("Remaining balance: $%.2f%n", customer.getBalance());
    }

    private static void sellerMenu(Seller seller) {
        while (true) {
            System.out.println("\n--- SELLER MENU ---");
            System.out.println("1. View My Info");
            System.out.println("2. View Assigned Vehicles");
            System.out.println("3. Record New Sale");
            System.out.println("4. Logout");
            System.out.print("Choose option (1-4): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> displaySellerInfo(seller);
                case "2" -> displayAssignedVehicles(seller);
                case "3" -> recordNewSale(seller);
                case "4" -> { System.out.println("Logging out..."); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void stockManagerMenu(StockManager manager) {
        while (true) {
            System.out.println("\n--- STOCK MANAGER MENU ---");
            System.out.println("1. View All Vehicles");
            System.out.println("2. Add New Vehicle");
            System.out.println("3. Assign Vehicle to Seller");
            System.out.println("4. Logout");
            System.out.print("Choose option (1-4): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> displayVehicules();
                case "2" -> addNewVehicle(manager);
                case "3" -> assignVehicleToSeller(manager);
                case "4" -> { System.out.println("Logging out..."); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void assignVehicleToSeller(StockManager manager) {
        System.out.println("\n--- ASSIGN VEHICLE TO SELLER ---");


        VehiculeDAO vehiculeDAO = new VehiculeDAO();
        List<Vehicule> unassignedVehicles = vehiculeDAO.findAll().stream()
                .filter(v -> v.getAssignedSeller() == null || v.getAssignedSeller().getId() == null)
                .toList();

        if (unassignedVehicles.isEmpty()) {
            System.out.println("No unassigned vehicles available.");
            return;
        }

        System.out.println("\n--- UNASSIGNED VEHICLES ---");
        for (int i = 0; i < unassignedVehicles.size(); i++) {
            Vehicule v = unassignedVehicles.get(i);
            System.out.printf("%d. %s %s (%s) - $%.2f%n",
                    i + 1, v.getBrand(), v.getModel(), v.getClass().getSimpleName(), v.getPrice());
        }

        System.out.print("Select vehicle (1-" + unassignedVehicles.size() + "): ");
        Vehicule selectedVehicle;
        try {
            int vChoice = Integer.parseInt(scanner.nextLine()) - 1;
            if (vChoice < 0 || vChoice >= unassignedVehicles.size()) {
                System.out.println("Invalid vehicle selection.");
                return;
            }
            selectedVehicle = unassignedVehicles.get(vChoice);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
            return;
        }


        UserDAO userDAO = new UserDAO();
        List<Seller> sellers = userDAO.findAllSellers();
        if (sellers.isEmpty()) {
            System.out.println("No sellers available.");
            return;
        }

        System.out.println("\n--- SELLERS ---");
        for (int i = 0; i < sellers.size(); i++) {
            Seller s = sellers.get(i);
            System.out.printf("%d. %s %s (Quota: %d)%n",
                    i + 1, s.getFirstName(), s.getLastName(), s.getMonthlySalesQuota());
        }

        System.out.print("Select seller (1-" + sellers.size() + "): ");
        Seller selectedSeller;
        try {
            int sChoice = Integer.parseInt(scanner.nextLine()) - 1;
            if (sChoice < 0 || sChoice >= sellers.size()) {
                System.out.println("Invalid seller selection.");
                return;
            }
            selectedSeller = sellers.get(sChoice);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
            return;
        }


        selectedVehicle.setAssignedSeller(selectedSeller);
        vehiculeDAO.saveOrUpdate(selectedVehicle);

        System.out.println("Vehicle " + selectedVehicle.getBrand() + " " + selectedVehicle.getModel() +
                " (ID: " + selectedVehicle.getId() + ") assigned to " +
                selectedSeller.getFirstName() + " " + selectedSeller.getLastName());
    }

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View All Users");
            System.out.println("2. Delete User Account");
            System.out.println("3. Logout");
            System.out.print("Choose option (1-3): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> displayAllUsers();
                case "2" -> deleteUserAccount();
                case "3" -> { System.out.println("Logging out..."); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void deleteUserAccount() {
        System.out.println("\n--- DELETE USER ACCOUNT ---");
        System.out.print("Enter login of user to delete: ");
        String login = scanner.nextLine().trim();

        if (login.isEmpty()) {
            System.out.println("Login cannot be empty.");
            return;
        }

        Optional<User> userOpt = userDAO.findByLogin(login);
        if (userOpt.isEmpty()) {
            System.out.println("User not found.");
            return;
        }

        User user = userOpt.get();
        if (user instanceof Customer customer) {
            SaleDAO saleDAO = new SaleDAO();
            if (!saleDAO.findByCustomer(customer).isEmpty()) {
                System.out.println("Cannot delete customer with existing sales.");
                return;
            }
        }
        String role = user.getClass().getSimpleName();




        System.out.printf("Delete %s account: %s %s (Login: %s)? (y/n): ",
                role, user.getFirstName(), user.getLastName(), user.getLogin());
        if (!scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            System.out.println("Deletion cancelled.");
            return;
        }


        userDAO.delete(user);
        System.out.println("User account deleted successfully.");
    }

    private static void displayCustomerInfo(Customer c) {
        System.out.println("\n--- MY INFO ---");
        System.out.println("Name: " + c.getFirstName() + " " + c.getLastName());
        System.out.println("Email: " + c.getEmail());
        System.out.println("Phone: " + c.getPhone());
        System.out.printf("Balance: $%.2f%n", c.getBalance());
    }

    private static void displayCustomerPurchases(Customer customer) {
        SaleDAO saleDAO = new SaleDAO();
        List<Sale> sales = saleDAO.findByCustomer(customer);

        if (sales.isEmpty()) {
            System.out.println("\nYou haven't made any purchases yet.");
            return;
        }

        System.out.println("\n--- MY PURCHASE HISTORY ---");
        System.out.printf("%-6s %-12s %-20s %-12s %-12s%n",
                "ID", "Date", "Vehicle", "Total", "Status");

        for (Sale s : sales) {
            String vehicleInfo = (s.get_vehicle() != null)
                    ? s.get_vehicle().getBrand() + " " + s.get_vehicle().getModel()
                    : "Unknown Vehicle";

            System.out.printf("%-6d %-12s %-20s $%-11.2f %-12s%n",
                    s.get_id(),
                    s.get_date() != null ? s.get_date().toString() : "N/A",
                    vehicleInfo,
                    s.get_total_amount(),
                    s.get_status() != null ? s.get_status().name() : "N/A"
            );
        }
    }


    private static void displaySellerInfo(Seller s) {
        System.out.println("\n--- MY INFO ---");
        System.out.println("Name: " + s.getFirstName() + " " + s.getLastName());
        System.out.println("Monthly Quota: " + s.getMonthlySalesQuota());
        System.out.println("Commission Rate: " + (s.getCommissionRate() * 100) + "%");
    }

    private static void displayAssignedVehicles(Seller seller) {
        VehiculeDAO vehiculeDAO = new VehiculeDAO();
        List<Vehicule> vehicles = vehiculeDAO.findBySeller(seller);

        if (vehicles.isEmpty()) {
            System.out.println("\nYou have no vehicles assigned to you.");
            return;
        }

        System.out.println("\n--- MY ASSIGNED VEHICLES ---");
        System.out.printf("%-6s %-15s %-20s %-12s %-10s%n",
                "ID", "Type", "Vehicle", "Price", "Available");

        for (Vehicule v : vehicles) {
            String vehicleInfo = v.getBrand() + " " + v.getModel();
            String typeName = v.getClass().getSimpleName();

            System.out.printf("%-6d %-15s %-20s $%-11.2f %-10s%n",
                    v.getId(),
                    typeName,
                    vehicleInfo,
                    v.getPrice(),
                    v.isAvailable() ? "Yes" : "No"
            );
        }
    }

    private static void recordNewSale(Seller seller) {
        System.out.println("\n--- RECORD NEW SALE ---");


        Customer customer = selectCustomer();
        if (customer == null) return;


        Vehicule vehicle = selectAvailableVehicle(seller);
        if (vehicle == null) return;


        System.out.println("\nSale type:");
        System.out.println("1. One-time (cash)");
        System.out.println("2. Credit (installments)");
        System.out.print("Choose (1-2): ");
        SaleType saleType;
        switch (scanner.nextLine().trim())
        {
            case "1":
                saleType = SaleType.ONE_TIME;
                break;

            case "2":
                saleType = SaleType.CREDIT;
                break;

            default:
                System.out.println("Invalid sale type.");
                return;
        };


        Discount discount = createDiscount();


        Sale sale = new Sale(vehicle, customer, saleType, discount);
        double basePrice = vehicle.getPrice();
        double finalPrice = basePrice;


        if (discount != null && discount.is_active()) {
            if (discount.get_type() == DiscountType.FIXED_AMOUNT) {
                finalPrice = Math.max(0, basePrice - discount.get_amount());
            } else if (discount.get_type() == DiscountType.PERCENTAGE) {
                finalPrice = basePrice * (1 - (discount.get_amount() / 100.0));
            }
        }

        sale.update_total_amount(finalPrice);


        if (saleType == SaleType.ONE_TIME) {
            System.out.printf("\nEnter payment amount (total: $%.2f): ", finalPrice);
            double paymentAmount = Double.parseDouble(scanner.nextLine());
            Payment payment = new Payment(paymentAmount, PaymentType.CASH);
            sale.add_payment(payment);
        } else {
            System.out.println("\nCredit sale created. Payments will be added later.");

        }


        sale.set_seller(seller);
        SaleDAO saleDAO = new SaleDAO();
        saleDAO.save(sale);


        if (sale.get_status() == SaleStatus.COMPLETED) {
            vehicle.setAvailable(false);
            VehiculeDAO vehiculeDAO = new VehiculeDAO();
            vehiculeDAO.saveOrUpdate(vehicle);
        }

        System.out.println("Sale recorded successfully! Sale ID: " + sale.get_id());
    }

    private static void addNewVehicle(StockManager manager) {
        System.out.println("\n--- ADD NEW VEHICLE ---");
        System.out.println("Select vehicle type:");
        System.out.println("1. Car");
        System.out.println("2. Truck");
        System.out.println("3. Motorcycle");
        System.out.print("Enter choice (1-3): ");

        String choice = scanner.nextLine().trim();
        Vehicule newVehicle = null;

        System.out.print("Brand: ");
        String brand = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Production Year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Fuel Type (GASOLINE/DIESEL/KEROSENE): ");
        FuelType fuelType = FuelType.valueOf(scanner.nextLine().trim().toUpperCase());

        switch (choice) {
            case "1":
                System.out.print("Number of Doors: ");
                int doors = Integer.parseInt(scanner.nextLine());
                System.out.print("Trunk Capacity (litres): ");
                float trunk = Float.parseFloat(scanner.nextLine());
                System.out.print("Has Air Conditioning? (true/false): ");
                boolean ac = Boolean.parseBoolean(scanner.nextLine());
                newVehicle = new Car(brand, model, year, price, fuelType, doors, trunk, ac);
                break;

            case "2":
                System.out.print("Payload Capacity (kg): ");
                float payload = Float.parseFloat(scanner.nextLine());
                System.out.print("Number of Axles: ");
                int axles = Integer.parseInt(scanner.nextLine());
                newVehicle = new Truck(brand, model, year, price, fuelType, payload, axles);
                break;

            case "3":
                System.out.print("Engine Capacity (cc): ");
                int engine = Integer.parseInt(scanner.nextLine());
                System.out.print("Motorcycle Type (SPORT_BIKE/CRUISER/TOURING/STANDARD/DIRT_BIKE): ");
                MotorcycleType motoType = MotorcycleType.valueOf(scanner.nextLine().trim().toUpperCase());
                newVehicle = new Motorcycle(brand, model, year, price, fuelType, engine, motoType);
                break;

            default:
                System.out.println("Invalid vehicle type.");
                return;
        }


        newVehicle.setAvailable(true);


        VehiculeDAO vehiculeDAO = new VehiculeDAO();
        vehiculeDAO.saveOrUpdate(newVehicle);

        System.out.println("Vehicle added successfully! ID: " + newVehicle.getId());
    }


    private static void displayAllUsers() {
        System.out.println("\n--- ALL USERS ---");
        displayCustomers();
        displaySellers();

    }

    private static void handleSignup(String role) {
        System.out.println("\n--- SIGNING UP AS " + role.toUpperCase() + " ---");
        System.out.print("First Name: "); String fn = scanner.nextLine();
        System.out.print("Last Name: "); String ln = scanner.nextLine();
        System.out.print("Desired Login: "); String log = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();

        try {
            User newUser = switch (role) {
                case "Customer" -> signupCustomer(fn, ln, log, pass);
                case "Seller" -> signupSeller(fn, ln, log, pass);
                case "Admin" -> signupAdmin(fn, ln, log, pass);
                case "StockManager" -> signupStockManager(fn, ln, log, pass);
                default -> throw new IllegalArgumentException("Unknown role");
            };

            userDAO.save(newUser);
            System.out.println("Account created! Login: " + newUser.getLogin());
        } catch (Exception e) {
            System.err.println("Signup failed: " + e.getMessage());
            if (e instanceof IllegalArgumentException) {
                System.out.println("Hint: For Customer, type must be INDIVIDUAL or CORPORATE.");
            }
        }
    }

    private static User signupCustomer(String fn, String ln, String log, String pass) {
        System.out.print("Address: "); String address = scanner.nextLine();
        System.out.print("Phone: "); String phone = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Customer Type (INDIVIDUAL/CORPORATE): ");
        CustomerType type = CustomerType.valueOf(scanner.nextLine().trim().toUpperCase());
        return new Customer(fn, ln, log, pass, address, phone, email, type);
    }

    private static User signupSeller(String fn, String ln, String log, String pass) {

        return new Seller(fn, ln, log, pass, 10, 0.05);
    }

    private static User signupAdmin(String fn, String ln, String log, String pass) {
        return new Admin(fn, ln, log, pass);
    }

    private static User signupStockManager(String fn, String ln, String log, String pass) {
        System.out.print("Assigned Warehouse Location: ");
        String warehouse = scanner.nextLine();
        return new StockManager(fn, ln, log, pass, warehouse);
    }


    private static Customer selectCustomer() {
        System.out.print("\nEnter customer login: ");
        String login = scanner.nextLine();
        Optional<User> userOpt = userDAO.findByLogin(login);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Customer)) {
            System.out.println("Customer not found.");
            return null;
        }
        return (Customer) userOpt.get();
    }


    private static Vehicule selectAvailableVehicle(Seller seller) {
        VehiculeDAO vehiculeDAO = new VehiculeDAO();
        List<Vehicule> availableVehicles = vehiculeDAO.findBySeller(seller).stream()
                .filter(Vehicule::isAvailable)
                .toList();

        if (availableVehicles.isEmpty()) {
            System.out.println("You have no available vehicles assigned.");
            return null;
        }

        System.out.println("\n--- AVAILABLE VEHICLES ---");
        for (int i = 0; i < availableVehicles.size(); i++) {
            Vehicule v = availableVehicles.get(i);
            System.out.printf("%d. %s %s (%s) - $%.2f%n",
                    i + 1, v.getBrand(), v.getModel(), v.getClass().getSimpleName(), v.getPrice());
        }

        System.out.print("Select vehicle (1-" + availableVehicles.size() + "): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < availableVehicles.size()) {
                return availableVehicles.get(choice);
            } else {
                System.out.println("Invalid selection.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
            return null;
        }
    }


    private static Discount createDiscount() {
        System.out.print("\nApply discount? (y/n): ");
        if (!scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            return null;
        }

        System.out.print("Discount amount: ");
        long amount = Long.parseLong(scanner.nextLine());
        System.out.print("Discount type (FIXED_AMOUNT/PERCENTAGE): ");
        DiscountType type = DiscountType.valueOf(scanner.nextLine().trim().toUpperCase());
        System.out.print("Applicable to (ALL/CARS/TRUCKS/MOTORCYCLES): ");
        ApplicableTo applicable = ApplicableTo.valueOf(scanner.nextLine().trim().toUpperCase());

        return new Discount(amount, type, applicable, LocalDate.now());
    }
}
