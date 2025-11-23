package pkg.transactions;

import java.time.LocalDate;
import java.time.Period;

enum DiscountType
{
    FIXED_AMOUNT,
    PERCENTAGE
}

enum ApplicableTo
{
    CARS,
    TRUCKS,
    MOTORCYCLES,
    ALL
}

class Discount
{
    private static long g_id = 0;
    private long id;
    private long amount;
    private DiscountType type;
    private ApplicableTo applicable_to;
    private LocalDate start_date;
    private LocalDate end_date;

    private static final Period DEFAULT_DISCOUNT_DURATION = Period.ofMonths(3);

    public Discount(long amount, DiscountType type, ApplicableTo applicable_to, LocalDate start_date, LocalDate end_date)
    {
        this.id = genID();
        this.amount = amount;
        this.type = type;
        this.applicable_to = applicable_to;
        this.start_date = LocalDate.now();
        this.end_date = this.start_date.plus(DEFAULT_DISCOUNT_DURATION);
    }

    public Discount(long amount, DiscountType type, ApplicableTo applicable_to, LocalDate start_date, LocalDate end_date, Period duration)
    {
        this.id = genID();
        this.amount = amount;
        this.type = type;
        this.applicable_to = applicable_to;
        this.start_date = LocalDate.now();
        this.end_date = this.start_date.plus(duration);
    }

    private static long genID()
    {
        return Discount.g_id++;
    }

    public long get_id() {
        return this.id;
    }

    public long get_amount() {
        return this.amount;
    }

    public DiscountType get_type() {
        return this.type;
    }

    public ApplicableTo get_applicable_to() {
        return this.applicable_to;
    }

    public LocalDate getStartDate() {
            return this.start_date;
        }

    public LocalDate getEndDate() {
        return this.end_date;
    }
}
