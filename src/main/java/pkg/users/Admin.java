package pkg.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ADMINS")
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String first_name, String last_name, String login, String password) {
        super(first_name, last_name, login, password);
    }

    public void resetPassword(User user, String new_password)
    {
        user.setPassword(new_password);
        System.out.println("Admin " + getId() + " requested password reset for user " + user.getLogin());
    }

    public void deactivateUserAccount(User user)
    {
        System.out.println("Admin " + getId() + " requested account deactivation for user " + user.getLogin());
    }
}
