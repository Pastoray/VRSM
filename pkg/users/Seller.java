package pkg.users;

import pkg.vehicules.Vehicule;
import pkg.transactions.Sale;

import java.util.List;
import java.util.ArrayList;

public class Seller extends User
{
    private int monthly_sales_quota;
    private double commission_rate;
    private List<Vehicule> vehicules;
    private List<Sale> sales_closed;

    Seller(String first_name, String last_name, String login, String password, int quota, double commission)
    {
        super(first_name, last_name, login, password);
        this.monthly_sales_quota = quota;
        this.commission_rate = commission;
        this.vehicules = new ArrayList<>();
        this.sales_closed = new ArrayList<>();
    }

    public void assign_vehicle(Vehicule vehicle_asg) {
        this.vehicules.add(vehicle_asg);

    }

    public void record_sale(Sale sale) {
        this.sales_closed.add(sale);
    }

    public int get_monthly_sales_quota()
    {
        return monthly_sales_quota;
    }
    public double get_commission_rate()
    {
        return commission_rate;
    }
    public List<Vehicule> get_active_vehicle_assignments()
    {
        return vehicules;
    }
    public List<Sale> get_sales_closed()
    {
        return sales_closed;
    }

}
