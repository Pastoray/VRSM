package pkg.transactions;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Payment {

    private double amount;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private PaymentType type;


    public Payment() {}


    public Payment(double amount, PaymentType type) {
        this.amount = amount;
        this.type = type;
        this.date = LocalDate.now();
    }


    public double get_amount() {
        return amount;
    }

    public void set_amount(double amount) {
        this.amount = amount;
    }

    public LocalDate get_date() {
        return date;
    }

    public void set_date(LocalDate date) {
        this.date = date;
    }

    public PaymentType get_type() {
        return type;
    }

    public void set_type(PaymentType type) {
        this.type = type;
    }
}
