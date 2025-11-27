package pkg.users;

import jakarta.persistence.*;
import pkg.vehicules.Vehicule;
import pkg.transactions.Sale;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "SELLERS")
public class Seller extends User
{
    @Column(name = "MONTHLY_SALES_QUOTA")
    private int monthly_sales_quota;

    @Column(name = "COMMISSION_RATE")
    private double commission_rate;

    @OneToMany(mappedBy = "assignedSeller", fetch = FetchType.LAZY)
    private List<Vehicule> assignedVehicules = new ArrayList<>();



    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> salesClosed = new ArrayList<>();

    public Seller()
    {
        super();
    }


    public Seller(String first_name, String last_name, String login, String password, int quota, double commission)
    {
        super(first_name, last_name, login, password);
        this.monthly_sales_quota = quota;
        this.commission_rate = commission;
        this.assignedVehicules = new ArrayList<>();
        this.salesClosed = new ArrayList<>();
    }





    public void assignVehicle(Vehicule vehicleAsg) {
        this.assignedVehicules.add(vehicleAsg);
        vehicleAsg.setAssignedSeller(this);
    }

    public void recordSale(Sale sale) {
        this.salesClosed.add(sale);
        sale.set_seller(this);

    }



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
