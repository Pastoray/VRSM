package pkg.users;

import jakarta.persistence.*;
import pkg.vehicules.Vehicule;
import java.time.LocalDate;

/**
 * Concrete entity representing a Stock Manager user.
 * Mapped to the STOCK_MANAGERS table, joining with the USERS base table.
 */
@Entity
@Table(name = "STOCK_MANAGERS")
public class StockManager extends User {

    @Column(name = "WAREHOUSE_LOCATION")
    private String warehouse_location;

    // LocalDate is mapped natively by Hibernate
    @Column(name = "LAST_INVENTORY_CHECK_DATE")
    private LocalDate last_inventory_check_date;

    /**
     * MANDATORY: No-argument constructor for Hibernate/JPA.
     */
    public StockManager() {
        super();
    }

    /**
     * Parameterized constructor for object creation.
     */
    public StockManager(String first_name, String last_name, String login, String password, String location) {
        super(first_name, last_name, login, password);
        this.warehouse_location = location;
        // Initialization for a newly created Java object
        this.last_inventory_check_date = LocalDate.now();
    }

    // --- Business Methods (kept simple for demonstration) ---

    public void logVehicleArrival(Vehicule vehicle) {
        // Note: In a real application, this should use the DAO to update the vehicle's state
        System.out.println("StockManager " + getId() + " logged vehicle arrival at " + this.warehouse_location);
    }

    public void performInventoryCheck() {
        // Note: Use a setter here to ensure property change is tracked if entity is managed
        this.setLastInventoryCheckDate(LocalDate.now());
        System.out.println("Inventory check completed for warehouse at " + this.warehouse_location);
    }

    // --- Getters and Setters (Standard JPA naming convention applied) ---

    public String getWarehouseLocation() {
        return warehouse_location;
    }

    public void setWarehouseLocation(String warehouse_location) {
        this.warehouse_location = warehouse_location;
    }

    public LocalDate getLastInventoryCheckDate() {
        return last_inventory_check_date;
    }

    public void setLastInventoryCheckDate(LocalDate last_inventory_check_date) {
        this.last_inventory_check_date = last_inventory_check_date;
    }
}
