package pkg.users;

enum CustomerType
{
    INDIVIDUAL,
    COMPANY
}

public class Customer extends User
{
    private String address;
    private String phone;
    private String email;
    private CustomerType type;

    public Customer(String first_name, String last_name, String login, String password, String address, String phone, String email, CustomerType type)
    {
        super(first_name, last_name, login, password);
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }

    public String get_address() {
        return address;
    }

    public String get_phone() {
        return phone;
    }

    public String get_email() {
        return email;
    }

    public CustomerType get_type() {
        return type;
    }
}
