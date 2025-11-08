package pkg.transactions;

import java.time.LocalDate;
import java.util.Optional;

import pkg.users.Customer;
import pkg.vehicules.Vehicule;

import java.util.ArrayList;
import java.util.List;

enum SaleType
{
    ONE_TIME,
    CREDIT
}

enum SaleStatus
{
    PENDING,
    COMPLETED,
    CANCELLED
}

public class Sale
{
    private static long g_id = 0;
    private long id;
    private LocalDate date;
    private double total_amount;
    private SaleType type;
    private Optional<Discount> discount;
    private Vehicule vehicle;
    private Customer customer;
    private SaleStatus status;
    private List<Payment> payments;

    public Sale(Vehicule vehicle, Customer customer, SaleType type, Optional<Discount> discount)
    {
        this.id = genID();
        this.date = LocalDate.now();
        this.vehicle = vehicle;
        this.customer = customer;
        this.type = type;
        this.discount = discount;
        this.total_amount = 0.0;
        this.status = SaleStatus.PENDING;
        this.payments = new ArrayList<>();
    }

    public void add_payment(Payment payment)
    {
        if (this.status != SaleStatus.PENDING)
        {
            System.out.println("Sale " + this.id + " is no longer pending...");
            return;
        }

        if (this.type == SaleType.ONE_TIME)
        {
            if (this.total_amount < payment.get_amount())
            {
                System.out.println("Payment failed; Insufficient payment amount. The full sale price must be paid in a single transaction.");
                return;
            }
        }
        else if (this.type == SaleType.CREDIT)
        {
            this.payments.add(payment);
            double total_payments = this.payments.stream().mapToDouble(Payment::get_amount).sum();

            if (total_payments >= this.total_amount)
            {
                this.status = SaleStatus.COMPLETED;
                System.out.println("Sale " + this.id + " finalized as: " + this.status);
            }
        }
    }

    public void cancel_sale()
    {
        if (this.status == SaleStatus.PENDING)
            this.status = SaleStatus.CANCELLED;
    }

    public static long genID()
    {
        return Sale.g_id++;
    }

    public long get_id() {
        return id;
    }

    public LocalDate get_date() {
        return date;
    }

    public double get_total_amount() {
        return total_amount;
    }

    public SaleType get_type() {
        return type;
    }

    public Optional<Discount> get_discount() {
        return discount;
    }

    public Vehicule get_vehicle() {
        return vehicle;
    }

    public Customer get_customer() {
        return customer;
    }

    public void set_discount(Optional<Discount> discount) {
        this.discount = discount;
    }

    public void update_total_amount(double new_amount) {
        this.total_amount = new_amount;
    }
}
