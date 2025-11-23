package pkg.users;

import jakarta.persistence.*;
import pkg.vehicules.Vehicule;
import pkg.transactions.Sale;
import java.util.List;
import java.util.ArrayList;

/**
 * Concrete entity representing a Seller user.
 * Mapped to the SELLERS table, joining with the USERS base table.
 */
@Entity
@Table(name = "SELLERS")
public class Seller extends User
{
    @Column(name = "MONTHLY_SALES_QUOTA")
    private int monthly_sales_quota;

    @Column(name = "COMMISSION_RATE")
    private double commission_rate;

    // Relationship 1: Vehicules assigned to this Seller
    // Assumes Vehicule has a mappedBy="assigned_seller" field
    @OneToMany(mappedBy = "assignedSeller", fetch = FetchType.LAZY)
    private List<Vehicule> assignedVehicules = new ArrayList<>();

    // Relationship 2: Sales closed by this Seller
    // Assumes Sale has a mappedBy="seller" field
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> salesClosed = new ArrayList<>();

    /**
     * MANDATORY: No-argument constructor for Hibernate/JPA.
     */
    public Seller()
    {
        super();
        // JPA initialization; lists are often best initialized here
        // or through the field initialization above.
    }

    // Parameterized constructor
    public Seller(String first_name, String last_name, String login, String password, int quota, double commission)
    {
        super(first_name, last_name, login, password);
        this.monthly_sales_quota = quota;
        this.commission_rate = commission;
        this.assignedVehicules = new ArrayList<>();
        this.salesClosed = new ArrayList<>();
    }

    // --- Business Methods (adjusting names to standard Java conventions) ---

    // Note: When adding to a @OneToMany, you should also update the
    // many-side (e.g., set vehicle.setAssignedSeller(this)).
    public void assignVehicle(Vehicule vehicleAsg) {
        this.assignedVehicules.add(vehicleAsg);
        // Optional: vehicleAsg.setAssignedSeller(this);
    }

    public void recordSale(Sale sale) {
        this.salesClosed.add(sale);
        // Optional: sale.setSeller(this);
    }

    // --- Getters and Setters ---

    public int getMonthlySalesQuota()
    {
        return monthly_sales_quota;
    }
    public void setMonthlySalesQuota(int monthly_sales_quota)
    {
        this.monthly_sales_quota = monthly_sales_quota;
    }

    public double getCommissionRate()
    {
        return commission_rate;
    }
    public void setCommissionRate(double commission_rate)
    {
        this.commission_rate = commission_rate;
    }

    public List<Vehicule> getAssignedVehicules()
    {
        return assignedVehicules;
    }
    public void setAssignedVehicules(List<Vehicule> assignedVehicules)
    {
        this.assignedVehicules = assignedVehicules;
    }

    public List<Sale> getSalesClosed()
    {
        return salesClosed;
    }
    public void setSalesClosed(List<Sale> salesClosed)
    {
        this.salesClosed = salesClosed;
    }
}
