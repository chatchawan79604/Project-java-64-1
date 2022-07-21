package ku.cs.models;

import ku.cs.services.BlacklistDataSource;
import ku.cs.services.DataSource;

import java.time.LocalDateTime;

public class BlacklistAccount {
    private String username;
    private String shopName;
    private int loginAttempt;

    public BlacklistAccount(String username, String shopName, int loginAttempt) {
        this.username = username;
        this.shopName = shopName;
        this.loginAttempt = loginAttempt;
    }

    public BlacklistAccount(String username, String shopName) {
        this(username,shopName, 0);
    }

    public String getUsername() {
        return username;
    }

    public int getLoginAttempt() {
        return loginAttempt;
    }

    public String getShopName() {
        return shopName;
    }

    public void attemptToLogin(){
        loginAttempt++;
    }

    @Override
    public String toString() {
        return username + "," + shopName + "," + loginAttempt;
    }
}
