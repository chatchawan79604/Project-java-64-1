package ku.cs.models;

import ku.cs.services.BlacklistDataSource;
import ku.cs.services.DataSource;

import java.util.ArrayList;

public class BlacklistAccountList {
    private ArrayList<BlacklistAccount> blacklistAccounts;

    public BlacklistAccountList(ArrayList<BlacklistAccount> blacklistAccounts) {
        this.blacklistAccounts = blacklistAccounts;
    }

    public BlacklistAccountList() {
        blacklistAccounts = new ArrayList<>();
    }

    public BlacklistAccount getBlacklistAccountByUsername(String username) {
        for(BlacklistAccount blacklistAccount : blacklistAccounts){
            if(blacklistAccount.getUsername().equals(username)){
                return blacklistAccount;
            }
        }
        return null;
    }

    public void addBlacklistAccount(BlacklistAccount blacklistAccount){
        blacklistAccounts.add(blacklistAccount);
    }

    public void removeBlacklistAccount(String username){
        for(int index = 0 ; index < blacklistAccounts.size();index++ ){
            if(blacklistAccounts.get(index).getUsername().equals(username)){
                blacklistAccounts.remove(index);
                return;
            }
        }
        return;
    }

    public ArrayList<String> getBlacklistShopName(){
        ArrayList<String> blacklistShopNameList = new ArrayList<>();
        for (BlacklistAccount blacklistAccount : blacklistAccounts){
            if(blacklistAccount.getShopName() != null){
                blacklistShopNameList.add(blacklistAccount.getShopName());
            }
        }
        return blacklistShopNameList;
    }

    public BlacklistAccount getBlacklistAccountOfIndex(int index){
        return blacklistAccounts.get(index);
    }

    public int getSizeOfBlacklistAccount(){
        return blacklistAccounts.size();
    }

    public String toCsv(){
        String text = "";
        for (BlacklistAccount blacklistAccount: blacklistAccounts) {
            text += blacklistAccount.toString() + "\n";
        }
        return  text;
    }
}
