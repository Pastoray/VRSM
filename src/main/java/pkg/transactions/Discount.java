package pkg.transactions;

import java.time.LocalDate;
import java.time.Period;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Discount {
    private static final Period DEFAULT_DISCOUNT_DURATION = Period.ofMonths(3);

    @Column(name = "discount_amount")
    private long amount;

    @Column(name = "discount_type")
    private DiscountType type;

    @Column(name = "discount_applicable_to")
    private ApplicableTo applicable_to;

    @Column(name = "discount_start_date")
    private LocalDate start_date;

    @Column(name = "discount_end_date")
    private LocalDate end_date;


    public Discount() {}


    public Discount(long amount, DiscountType type, ApplicableTo applicable_to,
                    LocalDate start_date, Period duration) {
        this.amount = amount;
        this.type = type;
        this.applicable_to = applicable_to;
        this.start_date = start_date;
        this.end_date = start_date.plus(duration);
    }


    public Discount(long amount, DiscountType type, ApplicableTo applicable_to, LocalDate start_date) {
        this(amount, type, applicable_to, start_date, DEFAULT_DISCOUNT_DURATION);
    }


    public long get_amount() {
        return amount;
    }

    public DiscountType get_type() {
        return type;
    }

    public ApplicableTo get_applicable_to() {
        return applicable_to;
    }

    public LocalDate get_start_date() {
        return start_date;
    }

    public LocalDate get_end_date() {
        return end_date;
    }


    public boolean is_active() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(start_date) && !now.isAfter(end_date);
    }
}
