package pkg.users;

import jakarta.persistence.*;
import pkg.vehicules.Vehicule;
import java.time.LocalDate;

@Entity
@Table(name = "STOCK_MANAGERS")
public class StockManager extends User {

    @Column(name = "WAREHOUSE_LOCATION")
    private String warehouse_location;


    @Column(name = "LAST_INVENTORY_CHECK_DATE")
    private LocalDate last_inventory_check_date;

    public StockManager() {
        super();
    }

    public StockManager(String first_name, String last_name, String login, String password, String location) {
        super(first_name, last_name, login, password);
        this.warehouse_location = location;

        this.last_inventory_check_date = LocalDate.now();
    }



    public void logVehicleArrival(Vehicule vehicle) {

        System.out.println("StockManager " + getId() + " logged vehicle arrival at " + this.warehouse_location);
    }

    public void performInventoryCheck() {

        this.setLastInventoryCheckDate(LocalDate.now());
        System.out.println("Inventory check completed for warehouse at " + this.warehouse_location);
    }



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
