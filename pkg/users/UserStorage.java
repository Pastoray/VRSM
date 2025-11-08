package pkg.users;

import java.util.HashMap;
import java.util.Map;

public class UserStorage
{
    // In memory storage (not persistent)
    private static final Map<String, User> userMap = new HashMap<>();

    public static void add_user(User user)
    {
        userMap.put(user.get_login(), user);
    }

    public static User get_user_by_login(String login)
    {
        return userMap.get(login);
    }

    public static boolean user_exists(String login)
    {
        return userMap.containsKey(login);
    }
}
