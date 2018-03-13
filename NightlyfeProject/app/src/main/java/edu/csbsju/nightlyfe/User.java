package edu.csbsju.nightlyfe;

/**
 * Created by dannyfritz3 on 3/13/18.
 * Class for getting and setting values for a user
 */

public class User {
    private String username, name, password;
    private int type;

    public String getUsername() {
        return this.username;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public int getType() {
        return this.type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(int type) {
        this.type = type;
    }
}
