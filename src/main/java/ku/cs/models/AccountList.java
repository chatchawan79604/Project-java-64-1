package ku.cs.models;

import ku.cs.models.Account;
import ku.cs.models.User;
import ku.cs.services.AccountDataSource;
import ku.cs.services.DataSource;
import ku.cs.utility.DialogAlert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AccountList {
    private ArrayList<Account> accountArrayList;
    public AccountList(){
        accountArrayList = new ArrayList<>();
    }

    public void addAccount(Account user) {
        accountArrayList.add(user);
        sortLastFirstAccountList();
    }

    public ArrayList<Account> getAccountArrayList() {
        return accountArrayList;
    }

    public Account getAccountByUsername(String username){
        for (Account account : accountArrayList) {
            if (username.equals(account.getUsername())){
                return account;
            }
        }
        return null;
    }

    public void replaceAccountData(Account newAccount){
        Account account = getAccountByUsername(newAccount.getUsername());
        if(account != null){
             account = newAccount;
        }
    }

    public void createAccount(String username, String password, String accountName, String picturePath) {
        if(getAccountByUsername(username) == null){
            addAccount(new User("Buyer", username, password, accountName, picturePath));
            DataSource<AccountList> accountListDataSource = new AccountDataSource();
            accountListDataSource.writeData(this);
        }
        else throw new RuntimeException();
    }

    public Account login(String username, String password) {
        Account account = getAccountByUsername(username);
        if (account != null && password.equals(account.getPassword())) {
            account.setTimeLogin(LocalDateTime.now());
            return account;
        }
        return null;
    }

    public int getSize(){
        return accountArrayList.size();
    }

    public Account getAccountOfIndex(int index){
        return accountArrayList.get(index);
    }

    public void sortLastFirstAccountList(){
        // มีการเปลี่ยนแปลงข้อมูล Account (type ArrayList)
        Collections.sort(accountArrayList, new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                if(o1.getTimeLogin().isBefore(o2.getTimeLogin())) {return 1;}
                if(o1.getTimeLogin().isAfter(o2.getTimeLogin())) {return -1;}
                return 0;
            }
        });
    }

    public User getUserByShop(String shopName){
        for (Account account:accountArrayList) {
            if(account instanceof User){
                if(((User) account).getShop() != null){
                    if(((User) account).getShop().getName().equals(shopName)){
                        return (User)account;
                    }
                }
            }
        }
        return null;
    }
}
