package pkg.users;

public abstract class User
{
    private static long g_id = 0;
    private long id;
    private String first_name;
    private String last_name;
    private String login;
    private String password;

    public User(String first_name, String last_name, String login, String password)
    {
        this.id = genID();
        this.first_name = first_name;
        this.last_name = last_name;
        this.login = login;
        this.password = password;
    }

    public static long genID()
    {
        return User.g_id++;
    }

	public long get_id() {
		return id;
	}

	public String get_first_name() {
		return first_name;
	}

	public String get_last_name() {
		return last_name;
	}

	public String get_login() {
		return login;
	}

	public String get_password() {
		return password;
	}

	public void set_id(User caller, long id) {
	    if (!(caller instanceof Admin))
			return;
		this.id = id;
	}

	public void set_first_name(User caller, String first_name) {
        if (!(caller instanceof Admin))
            return;
		this.first_name = first_name;
	}

	public void set_last_name(User caller, String last_name) {
    	if (!(caller instanceof Admin))
            return;
		this.last_name = last_name;
	}

	public void set_login(User caller, String login) {
        if (!(caller instanceof Admin))
            return;
		this.login = login;
	}

	public void set_password(User caller, String password) {
        if (!(caller instanceof Admin))
            return;
		this.password = password;
	}

}
