package pkg.users;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME")
    private String first_name;

    @Column(name = "LAST_NAME")
    private String last_name;


    @Column(name = "LOGIN", unique = true, nullable = false)
    private String login;

    @Column(name = "PASSWORD_HASH")
    private String password;

    public User()
    {

    }


    public User(String first_name, String last_name, String login, String password)
    {

        this.first_name = first_name;
        this.last_name = last_name;
        this.login = login;
        this.password = password;
    }




    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }





    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
