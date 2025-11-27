package pkg.users;

import jakarta.persistence.*;

@Entity
@Table(name = "CUSTOMERS")
public class Customer extends User
{
    @Column(name = "BALANCE", nullable = false)
    private double balance = 0.0;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;


    @Column(name = "CUSTOMER_TYPE")
    @Enumerated(EnumType.STRING)
    private CustomerType type;

    public Customer() {
        super();
    }

    public Customer(String first_name, String last_name, String login, String password, String address, String phone, String email, CustomerType type)
    {
        super(first_name, last_name, login, password);
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }
}
