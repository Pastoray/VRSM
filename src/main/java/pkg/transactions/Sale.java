package pkg.transactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import pkg.users.Customer;
import pkg.vehicules.Vehicule;
import pkg.users.Seller;

@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private double total_amount;
    private SaleStatus status;

    @ManyToOne
    private Vehicule vehicle;

    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private SaleType type;

    @Embedded
    private Discount discount;

    @ManyToOne
    private Seller seller;

    @ElementCollection
    @CollectionTable(name = "sale_payments", joinColumns = @JoinColumn(name = "sale_id"))
    private List<Payment> payments = new ArrayList<>();


    public Sale() {}


    public Sale(Vehicule vehicle, Customer customer, SaleType type, Discount discount) {
        this.date = LocalDate.now();
        this.vehicle = vehicle;
        this.customer = customer;
        this.type = type;
        this.discount = discount;
        this.total_amount = 0.0;
        this.status = SaleStatus.PENDING;
    }

    public void add_payment(Payment payment) {
        if (status != SaleStatus.PENDING) {
            System.out.println("Sale " + id + " is no longer pending.");
            return;
        }

        if (type == SaleType.ONE_TIME) {
            if (payment.get_amount() < total_amount) {
                System.out.println("Payment failed: full amount must be paid at once.");
                return;
            }
            payments.add(payment);
            status = SaleStatus.COMPLETED;
        } else if (type == SaleType.CREDIT) {
            payments.add(payment);
            double paid = payments.stream().mapToDouble(Payment::get_amount).sum();
            if (paid >= total_amount) {
                status = SaleStatus.COMPLETED;
            }
        }
        System.out.println("Sale " + id + " is now: " + status);
    }

    public void cancel_sale() {
        if (status == SaleStatus.PENDING) {
            status = SaleStatus.CANCELLED;
        }
    }


    public Long get_id() { return id; }
    public LocalDate get_date() { return date; }
    public double get_total_amount() { return total_amount; }
    public SaleStatus get_status() { return status; }
    public SaleType get_type() { return type; }
    public Discount get_discount() { return discount; }
    public Vehicule get_vehicle() { return vehicle; }
    public Customer get_customer() { return customer; }


    public void set_discount(Discount discount) { this.discount = discount; }
    public void update_total_amount(double amount) { this.total_amount = amount; }
    public void set_seller(Seller seller) {
        this.seller = seller;
    }

    public Seller get_seller() {
        return seller;
    }
}
