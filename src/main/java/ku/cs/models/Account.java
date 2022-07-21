package ku.cs.models;

import java.time.LocalDateTime;

public class Account {
    protected String accountType;
    private String accountName;
    private String username;
    private String password;
    private LocalDateTime timeLogin;

    public Account(String accountType, String accountName, String username, String password) {
        this.accountType = accountType;
        this.username = username;
        this.password = password;
        this.accountName = accountName;
        timeLogin = LocalDateTime.now();
    }

    //set time
    public void setTimeLogin(LocalDateTime timeLogin) {
        this.timeLogin = timeLogin;
    }
    public LocalDateTime getTimeLogin() { return timeLogin; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public int changePassword(String oldPassword, String newPassword, String confirmNewPassword){
        if(oldPassword.equals(getPassword())){
            if(newPassword.equals(confirmNewPassword)){
                setPassword(newPassword);
                return 0;
            }
            else{
                return  1;
            }
        }
        return 2;
    }
}
