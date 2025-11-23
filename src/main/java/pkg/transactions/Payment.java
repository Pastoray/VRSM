package pkg.transactions;

import java.time.LocalDate;

enum PaymentType
{
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    BANK_TRANSFER,
    CHEQUE
}

class Payment
{
    private static long g_id = 0;
    private long id;
    private double amount;
   	private LocalDate date;
    private PaymentType type;

    public static long genID()
    {
        return Payment.g_id++;
    }

	public long get_id() {
		return id;
	}
	public void set_id(long id) {
		this.id = id;
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
