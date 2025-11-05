package common;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author vova
 */
@Setter
@Getter
public final class User {
    private static User instance = null;
    private String name;
    private String password;
    private String about;
    private String ip;
    private String idEmployee;
    private String fio;
    private String department;
    private String rights;

    private User() {
    }

    public static synchronized User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }

}
