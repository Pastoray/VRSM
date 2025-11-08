package pkg.users;

import pkg.vehicules.Vehicule;

import java.time.LocalDate;

public class StockManager extends User {
    private String warehouse_location;
    private LocalDate last_inventory_check_date;

    public StockManager(String first_name, String last_name, String login, String password, String location) {
        super(first_name, last_name, login, password);
        this.warehouse_location = location;
        this.last_inventory_check_date = LocalDate.now();
    }

    public void log_vehicle_arrival(Vehicule vehicle) {
        System.out.println("StockManager " + get_id() + " logged vehicle arrival at " + this.warehouse_location);
    }

    public void perform_inventory_check() {
        this.last_inventory_check_date = LocalDate.now();
        System.out.println("Inventory check completed for warehouse at " + this.warehouse_location);
    }

    public String get_warehouse_location() {
        return warehouse_location;
    }

    public LocalDate get_last_inventory_check_date() {
        return last_inventory_check_date;
    }
}
