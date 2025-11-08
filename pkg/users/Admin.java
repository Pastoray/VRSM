package pkg.users;

public class Admin extends User
{
    public Admin(String first_name, String last_name, String login, String password)
    {
        super(first_name, last_name, login, password);
    }

    public void reset_password(User user, String new_password)
    {
        user.set_password(this, new_password);
        System.out.println("Admin " + get_id() + " reset password for user " + user.get_login());
    }

    public void deactivate_user_account(User user)
    {
        System.out.println("Admin " + get_id() + " deactivated account for user " + user.get_login());
    }
}
