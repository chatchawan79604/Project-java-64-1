package ku.cs.models;

import ku.cs.services.BlacklistDataSource;
import ku.cs.services.DataSource;

import java.time.LocalDateTime;

public class Admin extends Account{

    //constructor
    public Admin(String accountType, String username, String password, String accountName, LocalDateTime time){
        super(accountType, accountName, username, password);
        super.setTimeLogin(time);
    }

    //method สำหรับระงับการใช้งานผู้ใช้งาน
    public void banUser(User user){
        DataSource<BlacklistAccountList> blacklistAccountListDataSource = new BlacklistDataSource();
        BlacklistAccountList blacklistAccountList = blacklistAccountListDataSource.readData();
        blacklistAccountList.addBlacklistAccount(new BlacklistAccount(user.getUsername(),user.getShop()==null?null:user.getShop().getName()));
        blacklistAccountListDataSource.writeData(blacklistAccountList);
    }

    public void unbanUser(User user){
        DataSource<BlacklistAccountList> blacklistAccountListDataSource = new BlacklistDataSource();
        BlacklistAccountList blacklistAccountList = blacklistAccountListDataSource.readData();
        blacklistAccountList.removeBlacklistAccount(user.getUsername());
        blacklistAccountListDataSource.writeData(blacklistAccountList);
    }

}