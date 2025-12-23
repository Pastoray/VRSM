package pkg.main;

import pkg.users.*;
import pkg.vehicules.*;
import pkg.util.HibernateUtil;
import pkg.users.UserDAO;
import pkg.vehicules.VehiculeDAO;
import pkg.transactions.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VRSMGUI {

    private JFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel cards;

    private final UserDAO userDAO = new UserDAO();
    private final VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private final SaleDAO saleDAO = new SaleDAO();

    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            try {
                HibernateUtil.getSessionFactory();
                new VRSMGUI().createAndShowGUI();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "FATAL: Could not initialize database.\n" + e.getMessage(),
                        "Startup Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private void createAndShowGUI() {
        mainFrame = new JFrame("Vehicle Sales & Rental Management");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(new MainMenuPanel(), "MAIN");
        cards.add(new DisplayMenuPanel(), "DISPLAY_MENU");
        cards.add(new LoginRegisterPanel(), "LOGIN_REGISTER");

        mainFrame.add(cards);
        mainFrame.setVisible(true);
    }

    private class MainMenuPanel extends JPanel {
        public MainMenuPanel() {
            setLayout(new BorderLayout());
            JLabel title = new JLabel("VRSM System", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 24));
            add(title, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

            JButton displayBtn = new JButton("Display Entries");
            JButton loginBtn = new JButton("Login / Register");
            JButton exitBtn = new JButton("Exit");

            displayBtn.addActionListener(e -> cardLayout.show(cards, "DISPLAY_MENU"));
            loginBtn.addActionListener(e -> cardLayout.show(cards, "LOGIN_REGISTER"));
            exitBtn.addActionListener(e -> System.exit(0));

            buttonPanel.add(displayBtn);
            buttonPanel.add(loginBtn);
            buttonPanel.add(exitBtn);
            add(buttonPanel, BorderLayout.CENTER);
        }
    }

    private class DisplayMenuPanel extends JPanel {
        public DisplayMenuPanel() {
            setLayout(new BorderLayout());
            JLabel title = new JLabel("Display Entries", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 18));
            add(title, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

            buttonPanel.add(createButton("Vehicles", () -> showDisplayTable("Vehicles", displayVehicles())));
            buttonPanel.add(createButton("Customers", () -> showDisplayTable("Customers", displayCustomers())));
            buttonPanel.add(createButton("Sellers", () -> showDisplayTable("Sellers", displaySellers())));
            buttonPanel.add(createButton("Sales", () -> showDisplayTable("Sales", displaySales())));
            buttonPanel.add(createButton("Back", () -> cardLayout.show(cards, "MAIN")));

            add(buttonPanel, BorderLayout.CENTER);
        }

        private JButton createButton(String text, Runnable action) {
            JButton btn = new JButton(text);
            btn.addActionListener(e -> action.run());
            return btn;
        }
    }

    private class LoginRegisterPanel extends JPanel {
        public LoginRegisterPanel() {
            setLayout(new BorderLayout());
            JLabel title = new JLabel("Login / Register", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 18));
            add(title, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));

            buttonPanel.add(createButton("Log In", () -> showRoleSelection("login")));
            buttonPanel.add(createButton("Register", () -> showRoleSelection("signup")));
            buttonPanel.add(createButton("Back", () -> cardLayout.show(cards, "MAIN")));

            add(buttonPanel, BorderLayout.CENTER);
        }

        private JButton createButton(String text, Runnable action) {
            JButton btn = new JButton(text);
            btn.addActionListener(e -> action.run());
            return btn;
        }
    }

    private void showRoleSelection(String action) {
        String[] roles = {"Customer", "Seller", "Admin", "StockManager"};
        String role = (String) JOptionPane.showInputDialog(
                mainFrame, "Select your role:", "Role",
                JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]
        );
        if (role != null) {
            if ("login".equals(action)) handleLogin(role);
            else handleSignup(role);
        }
    }

    private void handleLogin(String role) {
        JTextField loginField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Login:")); panel.add(loginField);
        panel.add(new JLabel("Password:")); panel.add(passField);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Login as " + role, JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        String login = loginField.getText().trim();
        String password = new String(passField.getPassword());

        Optional<User> userOpt = userDAO.findByLogin(login);
        if (userOpt.isEmpty()) {
            showError("User not found.");
            return;
        }

        User user = userOpt.get();
        String actualRole = user.getClass().getSimpleName();
        if (!actualRole.equals(role)) {
            showError("Role mismatch! Found: " + actualRole);
            return;
        }
        if (!user.getPassword().equals(password)) {
            showError("Incorrect password.");
            return;
        }

        showMessage("Welcome, " + user.getFirstName() + "!");
        switch (actualRole) {
            case "Customer" -> showCustomerMenu((Customer) user);
            case "Seller" -> showSellerMenu((Seller) user);
            case "StockManager" -> showStockManagerMenu((StockManager) user);
            case "Admin" -> showAdminMenu((Admin) user);
        }
    }

    private void handleSignup(String role) {
        JTextField fn = new JTextField(), ln = new JTextField(), login = new JTextField();
        JPasswordField pass = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("First Name:")); panel.add(fn);
        panel.add(new JLabel("Last Name:")); panel.add(ln);
        panel.add(new JLabel("Login:")); panel.add(login);
        panel.add(new JLabel("Password:")); panel.add(pass);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Sign Up as " + role, JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            User newUser;
            switch (role) {
                case "Customer" -> {
                    String typeStr = JOptionPane.showInputDialog(mainFrame, "Customer Type (INDIVIDUAL/CORPORATE):", "INDIVIDUAL");
                    if (typeStr == null) return;
                    CustomerType type = CustomerType.valueOf(typeStr.trim().toUpperCase());
                    newUser = new Customer(fn.getText(), ln.getText(), login.getText(), new String(pass.getPassword()),
                            "N/A", "N/A", "N/A", type);
                }
                case "Seller" -> newUser = new Seller(fn.getText(), ln.getText(), login.getText(), new String(pass.getPassword()), 10, 0.05);
                case "Admin" -> newUser = new Admin(fn.getText(), ln.getText(), login.getText(), new String(pass.getPassword()));
                case "StockManager" -> {
                    String wh = JOptionPane.showInputDialog(mainFrame, "Warehouse Location:", "Main Warehouse");
                    if (wh == null) return;
                    newUser = new StockManager(fn.getText(), ln.getText(), login.getText(), new String(pass.getPassword()), wh);
                }
                default -> throw new IllegalArgumentException("Unknown role");
            }
            userDAO.save(newUser);
            showMessage("Account created!");
        } catch (Exception e) {
            showError("Signup failed: " + e.getMessage());
        }
    }

    private void showCustomerMenu(Customer customer) {
        String[] options = {"View My Info", "View My Purchases", "Deposit Funds", "Buy Vehicle", "Logout"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(mainFrame, "Customer Menu", "Choose",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == null || "Logout".equals(choice)) break;
            switch (choice) {
                case "View My Info" -> showCustomerInfo(customer);
                case "View My Purchases" -> showCustomerPurchases(customer);
                case "Deposit Funds" -> depositFunds(customer);
                case "Buy Vehicle" -> buyVehicle(customer);
            }
        }
    }

    private void depositFunds(Customer customer) {
        String input = JOptionPane.showInputDialog(mainFrame, "Deposit amount ($):", "0.00");
        if (input == null) return;
        try {
            double amount = Double.parseDouble(input);
            if (amount <= 0) throw new NumberFormatException();
            customer.deposit(amount);
            userDAO.save(customer);
            showMessage(String.format("$%.2f deposited.\nNew balance: $%.2f", amount, customer.getBalance()));
        } catch (NumberFormatException e) {
            showError("Invalid amount.");
        }
    }

    private void buyVehicle(Customer customer) {
        List<Vehicule> available = vehiculeDAO.findAllAvailable();
        if (available.isEmpty()) {
            showMessage("No vehicles available.");
            return;
        }

        String[] opts = available.stream()
                .map(v -> String.format("%s %s (%s) - $%.2f", v.getBrand(), v.getModel(), v.getClass().getSimpleName(), v.getPrice()))
                .toArray(String[]::new);

        String sel = (String) JOptionPane.showInputDialog(mainFrame, "Select vehicle:", "Buy Vehicle",
                JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
        if (sel == null) return;

        Vehicule v = available.get(java.util.Arrays.asList(opts).indexOf(sel));
        if (customer.getBalance() < v.getPrice()) {
            showError(String.format("Insufficient funds!\nNeed: $%.2f\nHave: $%.2f", v.getPrice(), customer.getBalance()));
            return;
        }

        if (confirm("Buy " + v.getBrand() + " " + v.getModel() + " for $" + String.format("%.2f", v.getPrice()) + "?")) {
            customer.setBalance(customer.getBalance() - v.getPrice());
            userDAO.save(customer);

            Sale sale = new Sale(v, customer, SaleType.ONE_TIME, null);
            sale.update_total_amount(v.getPrice());
            sale.add_payment(new Payment(v.getPrice(), PaymentType.CASH));

            List<Seller> sellers = userDAO.findAllSellers();
            if (!sellers.isEmpty()) sale.set_seller(sellers.get(0));

            saleDAO.save(sale);
            v.setAvailable(false);
            vehiculeDAO.saveOrUpdate(v);

            showMessage("Purchase successful!\nRemaining balance: $" + String.format("%.2f", customer.getBalance()));
        }
    }

    private void showSellerMenu(Seller seller) {
        String[] options = {"View My Info", "View Assigned Vehicles", "Record New Sale", "Logout"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(mainFrame, "Seller Menu", "Choose",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == null || "Logout".equals(choice)) break;
            switch (choice) {
                case "View My Info" -> showSellerInfo(seller);
                case "View Assigned Vehicles" -> showAssignedVehicles(seller);
                case "Record New Sale" -> recordNewSale(seller);
            }
        }
    }

    private void recordNewSale(Seller seller) {
        List<Customer> customers = userDAO.findAllCustomers();
        if (customers.isEmpty()) {
            showError("No customers found.");
            return;
        }
        String[] custOpts = customers.stream()
                .map(c -> c.getFirstName() + " " + c.getLastName() + " (" + c.getLogin() + ")")
                .toArray(String[]::new);
        String custSel = (String) JOptionPane.showInputDialog(mainFrame, "Select customer:", "Record Sale",
                JOptionPane.QUESTION_MESSAGE, null, custOpts, custOpts[0]);
        if (custSel == null) return;
        Customer customer = customers.get(java.util.Arrays.asList(custOpts).indexOf(custSel));

        List<Vehicule> myVehicles = vehiculeDAO.findBySeller(seller).stream()
                .filter(Vehicule::isAvailable)
                .collect(Collectors.toList());
        if (myVehicles.isEmpty()) {
            showError("You have no available vehicles assigned.");
            return;
        }
        String[] vehOpts = myVehicles.stream()
                .map(v -> v.getBrand() + " " + v.getModel() + " ($" + String.format("%.2f", v.getPrice()) + ")")
                .toArray(String[]::new);
        String vehSel = (String) JOptionPane.showInputDialog(mainFrame, "Select vehicle:", "Record Sale",
                JOptionPane.QUESTION_MESSAGE, null, vehOpts, vehOpts[0]);
        if (vehSel == null) return;
        Vehicule vehicle = myVehicles.get(java.util.Arrays.asList(vehOpts).indexOf(vehSel));

        String[] typeOpts = {"ONE_TIME", "CREDIT"};
        String typeSel = (String) JOptionPane.showInputDialog(mainFrame, "Sale type:", "Record Sale",
                JOptionPane.QUESTION_MESSAGE, null, typeOpts, typeOpts[0]);
        if (typeSel == null) return;
        SaleType saleType = SaleType.valueOf(typeSel);

        Discount discount = null;
        if (confirm("Apply discount?")) {
            String amtStr = JOptionPane.showInputDialog(mainFrame, "Discount amount:", "0");
            if (amtStr == null) return;
            long amount = Long.parseLong(amtStr);
            String[] discTypeOpts = {"FIXED_AMOUNT", "PERCENTAGE"};
            String discTypeSel = (String) JOptionPane.showInputDialog(mainFrame, "Discount type:", "Discount",
                    JOptionPane.QUESTION_MESSAGE, null, discTypeOpts, discTypeOpts[0]);
            if (discTypeSel == null) return;
            DiscountType discType = DiscountType.valueOf(discTypeSel);
            String[] appOpts = {"ALL", "CARS", "TRUCKS", "MOTORCYCLES"};
            String appSel = (String) JOptionPane.showInputDialog(mainFrame, "Applicable to:", "Discount",
                    JOptionPane.QUESTION_MESSAGE, null, appOpts, appOpts[0]);
            if (appSel == null) return;
            ApplicableTo applicable = ApplicableTo.valueOf(appSel);
            discount = new Discount(amount, discType, applicable, LocalDate.now());
        }

        double basePrice = vehicle.getPrice();
        double finalPrice = basePrice;
        if (discount != null && discount.is_active()) {
            if (discount.get_type() == DiscountType.FIXED_AMOUNT) {
                finalPrice = Math.max(0, basePrice - discount.get_amount());
            } else {
                finalPrice = basePrice * (1 - (discount.get_amount() / 100.0));
            }
        }

        Sale sale = new Sale(vehicle, customer, saleType, discount);
        sale.update_total_amount(finalPrice);
        sale.set_seller(seller);

        if (saleType == SaleType.ONE_TIME) {
            String payStr = JOptionPane.showInputDialog(mainFrame, "Payment amount ($):", String.format("%.2f", finalPrice));
            if (payStr == null) return;
            double paymentAmount = Double.parseDouble(payStr);
            sale.add_payment(new Payment(paymentAmount, PaymentType.CASH));
        }

        saleDAO.save(sale);
        if (sale.get_status() == SaleStatus.COMPLETED) {
            vehicle.setAvailable(false);
            vehiculeDAO.saveOrUpdate(vehicle);
        }

        showMessage("Sale recorded! ID: " + sale.get_id());
    }

    private void showStockManagerMenu(StockManager manager) {
        String[] options = {"View All Vehicles", "Add New Vehicle", "Assign Vehicle to Seller", "Logout"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(mainFrame, "Stock Manager Menu", "Choose",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == null || "Logout".equals(choice)) break;
            switch (choice) {
                case "View All Vehicles" -> showDisplayTable("Vehicles", displayVehicles());
                case "Add New Vehicle" -> addNewVehicle(manager);
                case "Assign Vehicle to Seller" -> assignVehicleToSeller(manager);
            }
        }
    }

    private void addNewVehicle(StockManager manager) {
        String[] types = {"Car", "Truck", "Motorcycle"};
        String type = (String) JOptionPane.showInputDialog(mainFrame, "Vehicle type:", "Add Vehicle",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        if (type == null) return;

        JTextField brand = new JTextField(), model = new JTextField(), year = new JTextField(), price = new JTextField();
        String[] fuelOpts = {"GASOLINE", "DIESEL", "KEROSENE"};
        String fuelStr = (String) JOptionPane.showInputDialog(mainFrame, "Fuel type:", "Add Vehicle",
                JOptionPane.QUESTION_MESSAGE, null, fuelOpts, fuelOpts[0]);
        if (fuelStr == null) return;
        FuelType fuelType = FuelType.valueOf(fuelStr);

        Vehicule vehicle = null;
        try {
            switch (type) {
                case "Car" -> {
                    String doorsStr = JOptionPane.showInputDialog(mainFrame, "Number of doors:", "4");
                    String trunkStr = JOptionPane.showInputDialog(mainFrame, "Trunk capacity (L):", "500");
                    String acStr = JOptionPane.showInputDialog(mainFrame, "Has AC? (true/false):", "true");
                    vehicle = new Car(brand.getText(), model.getText(), Integer.parseInt(year.getText()),
                            Double.parseDouble(price.getText()), fuelType,
                            Integer.parseInt(doorsStr), Float.parseFloat(trunkStr), Boolean.parseBoolean(acStr));
                }
                case "Truck" -> {
                    String payloadStr = JOptionPane.showInputDialog(mainFrame, "Payload (kg):", "10000");
                    String axlesStr = JOptionPane.showInputDialog(mainFrame, "Number of axles:", "2");
                    vehicle = new Truck(brand.getText(), model.getText(), Integer.parseInt(year.getText()),
                            Double.parseDouble(price.getText()), fuelType,
                            Float.parseFloat(payloadStr), Integer.parseInt(axlesStr));
                }
                case "Motorcycle" -> {
                    String engineStr = JOptionPane.showInputDialog(mainFrame, "Engine (cc):", "600");
                    String[] motoTypes = {"SPORT_BIKE", "CRUISER", "TOURING", "STANDARD", "DIRT_BIKE"};
                    String motoTypeStr = (String) JOptionPane.showInputDialog(mainFrame, "Type:", "Add Motorcycle",
                            JOptionPane.QUESTION_MESSAGE, null, motoTypes, motoTypes[0]);
                    if (motoTypeStr == null) return;
                    MotorcycleType motoType = MotorcycleType.valueOf(motoTypeStr);
                    vehicle = new Motorcycle(brand.getText(), model.getText(), Integer.parseInt(year.getText()),
                            Double.parseDouble(price.getText()), fuelType,
                            Integer.parseInt(engineStr), motoType);
                }
            }
        } catch (Exception e) {
            showError("Invalid input: " + e.getMessage());
            return;
        }

        if (vehicle != null) {
            vehicle.setAvailable(true);
            vehiculeDAO.saveOrUpdate(vehicle);
            showMessage("Vehicle added! ID: " + vehicle.getId());
        }
    }

    private void assignVehicleToSeller(StockManager manager) {
        List<Vehicule> unassigned = vehiculeDAO.findAll().stream()
                .filter(v -> v.getAssignedSeller() == null)
                .collect(Collectors.toList());
        if (unassigned.isEmpty()) {
            showMessage("No unassigned vehicles.");
            return;
        }

        String[] vehOpts = unassigned.stream()
                .map(v -> v.getBrand() + " " + v.getModel() + " (" + v.getClass().getSimpleName() + ")")
                .toArray(String[]::new);
        String vehSel = (String) JOptionPane.showInputDialog(mainFrame, "Select vehicle:", "Assign Vehicle",
                JOptionPane.QUESTION_MESSAGE, null, vehOpts, vehOpts[0]);
        if (vehSel == null) return;
        Vehicule vehicle = unassigned.get(java.util.Arrays.asList(vehOpts).indexOf(vehSel));

        List<Seller> sellers = userDAO.findAllSellers();
        if (sellers.isEmpty()) {
            showError("No sellers available.");
            return;
        }
        String[] sellerOpts = sellers.stream()
                .map(s -> s.getFirstName() + " " + s.getLastName())
                .toArray(String[]::new);
        String sellerSel = (String) JOptionPane.showInputDialog(mainFrame, "Select seller:", "Assign Vehicle",
                JOptionPane.QUESTION_MESSAGE, null, sellerOpts, sellerOpts[0]);
        if (sellerSel == null) return;
        Seller seller = sellers.get(java.util.Arrays.asList(sellerOpts).indexOf(sellerSel));

        vehicle.setAssignedSeller(seller);
        vehiculeDAO.saveOrUpdate(vehicle);
        showMessage("Vehicle assigned to " + seller.getFirstName() + " " + seller.getLastName());
    }

    private void showAdminMenu(Admin admin) {
        String[] options = {"View All Users", "Delete User Account", "Logout"};
        while (true) {
            String choice = (String) JOptionPane.showInputDialog(mainFrame, "Admin Menu", "Choose",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == null || "Logout".equals(choice)) break;
            switch (choice) {
                case "View All Users" -> {
                    showDisplayTable("Customers", displayCustomers());
                    showDisplayTable("Sellers", displaySellers());
                }
                case "Delete User Account" -> deleteUserAccount();
            }
        }
    }

    private void deleteUserAccount() {
        String login = JOptionPane.showInputDialog(mainFrame, "Enter login to delete:", "");
        if (login == null || login.trim().isEmpty()) return;

        Optional<User> userOpt = userDAO.findByLogin(login);
        if (userOpt.isEmpty()) {
            showError("User not found.");
            return;
        }

        User user = userOpt.get();
        if (user instanceof Customer customer && !saleDAO.findByCustomer(customer).isEmpty()) {
            showError("Cannot delete customer with sales.");
            return;
        }

        if (confirm("Delete " + user.getClass().getSimpleName() + " " + user.getFirstName() + " " + user.getLastName() + "?")) {
            userDAO.delete(user);
            showMessage("User deleted.");
        }
    }

    private void showCustomerInfo(Customer c) {
        String info = String.format(
                "Name: %s %s\nEmail: %s\nPhone: %s\nBalance: $%.2f",
                c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhone(), c.getBalance()
        );
        showMessage(info, "My Info");
    }

    private void showCustomerPurchases(Customer customer) {
        List<Sale> sales = saleDAO.findByCustomer(customer);
        if (sales.isEmpty()) {
            showMessage("No purchases yet.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Sale s : sales) {
            String vehicle = (s.get_vehicle() != null) ? s.get_vehicle().getBrand() + " " + s.get_vehicle().getModel() : "Unknown";
            sb.append(String.format("ID: %d | Date: %s | Vehicle: %s | Total: $%.2f | Status: %s\n",
                    s.get_id(),
                    s.get_date() != null ? s.get_date().toString() : "N/A",
                    vehicle,
                    s.get_total_amount(),
                    s.get_status() != null ? s.get_status().name() : "N/A"
            ));
        }
        showMessage(sb.toString(), "My Purchases");
    }

    private void showSellerInfo(Seller s) {
        String info = String.format(
                "Name: %s %s\nMonthly Quota: %d\nCommission Rate: %.1f%%",
                s.getFirstName(), s.getLastName(), s.getMonthlySalesQuota(), s.getCommissionRate() * 100
        );
        showMessage(info, "My Info");
    }

    private void showAssignedVehicles(Seller seller) {
        List<Vehicule> vehicles = vehiculeDAO.findBySeller(seller);
        if (vehicles.isEmpty()) {
            showMessage("No vehicles assigned.");
            return;
        }
        Object[][] data = vehicles.stream()
                .map(v -> new Object[]{
                        v.getId(),
                        v.getClass().getSimpleName(),
                        v.getBrand() + " " + v.getModel(),
                        v.getPrice(),
                        v.isAvailable() ? "Yes" : "No"
                })
                .toArray(Object[][]::new);
        showDisplayTable("My Vehicles", data, new String[]{"ID", "Type", "Vehicle", "Price", "Available"});
    }

    private void showDisplayTable(String title, Object[][] data) {
        String[] headers;
        if ("Vehicles".equals(title)) headers = new String[]{"ID", "Type", "Brand", "Model", "Year", "Price", "Available"};
        else if ("Customers".equals(title)) headers = new String[]{"ID", "Name", "Login", "Type"};
        else if ("Sellers".equals(title)) headers = new String[]{"ID", "Name", "Login", "Quota"};
        else if ("Sales".equals(title)) headers = new String[]{"ID", "Date", "Customer", "Vehicle", "Total", "Type", "Status"};
        else headers = new String[0];
        showDisplayTable(title, data, headers);
    }

    private void showDisplayTable(String title, Object[][] data, String[] headers) {
        JDialog dialog = new JDialog(mainFrame, title, true);
        dialog.setSize(700, 400);
        dialog.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(data, headers);
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton close = new JButton("Close");
        close.addActionListener(e -> dialog.dispose());
        dialog.add(close, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void showMessage(String msg) { showMessage(msg, "Info"); }
    private void showMessage(String msg, String title) {
        JOptionPane.showMessageDialog(mainFrame, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(mainFrame, msg, "Error", JOptionPane.WARNING_MESSAGE);
    }

    private boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(mainFrame, msg, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private Object[][] displayVehicles() {
        return vehiculeDAO.findAll().stream()
                .map(v -> new Object[]{v.getId(), v.getClass().getSimpleName(), v.getBrand(), v.getModel(),
                        v.getYear(), v.getPrice(), v.isAvailable()})
                .toArray(Object[][]::new);
    }

    private Object[][] displayCustomers() {
        return userDAO.findAllCustomers().stream()
                .map(c -> new Object[]{c.getId(), c.getFirstName() + " " + c.getLastName(), c.getLogin(), c.getType()})
                .toArray(Object[][]::new);
    }

    private Object[][] displaySellers() {
        return userDAO.findAllSellers().stream()
                .map(s -> new Object[]{s.getId(), s.getFirstName() + " " + s.getLastName(), s.getLogin(), s.getMonthlySalesQuota()})
                .toArray(Object[][]::new);
    }

    private Object[][] displaySales() {
        return saleDAO.findAll().stream()
                .map(s -> {
                    String cust = (s.get_customer() != null) ? s.get_customer().getFirstName() + " " + s.get_customer().getLastName() : "Unknown";
                    String veh = (s.get_vehicle() != null) ? s.get_vehicle().getBrand() + " " + s.get_vehicle().getModel() : "Unknown";
                    return new Object[]{s.get_id(),
                            s.get_date() != null ? s.get_date().toString() : "N/A",
                            cust, veh, s.get_total_amount(),
                            s.get_type() != null ? s.get_type().name() : "N/A",
                            s.get_status() != null ? s.get_status().name() : "N/A"};
                })
                .toArray(Object[][]::new);
    }
}