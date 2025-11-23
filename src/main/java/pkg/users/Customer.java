package pkg.users;

import jakarta.persistence.*;

/**
 * Concrete entity representing a standard Customer user.
 * Mapped to the CUSTOMERS table, joining with the USERS base table.
 */
@Entity
@Table(name = "CUSTOMERS")
public class Customer extends User
{
    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone; // Using 'phone' as per your latest definition

    @Column(name = "EMAIL")
    private String email;

    // Use EnumType.STRING to save the enum name (INDIVIDUAL or COMPANY)
    @Column(name = "CUSTOMER_TYPE")
    @Enumerated(EnumType.STRING)
    private CustomerType type;

    // MANDATORY FIX: Add no-argument constructor for Hibernate/JPA
    /**
     * Required by Hibernate/JPA for instantiation when loading data from the DB.
     */
    public Customer() {
        super();
    }

    /**
     * Parameterized constructor for object creation.
     */
    public Customer(String first_name, String last_name, String login, String password, String address, String phone, String email, CustomerType type)
    {
        super(first_name, last_name, login, password);
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }

    // --- Getters and Setters (Standard Java/JPA naming convention applied) ---

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
}
