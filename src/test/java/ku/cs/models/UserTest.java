package ku.cs.models;

import ku.cs.services.AccountDataSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testShopOfUser(){
        AccountDataSource accountDataSource = new AccountDataSource("data","account_data.csv");
        AccountList accountList = new AccountList();
        accountList = accountDataSource.readData();
        Account account = accountList.login("123","123");
        assertTrue(account instanceof User);
        assertNotNull(((User)account).getShop());
    }

    @Test
    void testBanUser(){
        AccountDataSource accountDataSource = new AccountDataSource("data","account_data.csv");
        AccountList accountList;
        accountList = accountDataSource.readData();

        Account account = accountList.login("admin","admin");
        ((Admin)account).banUser((User) accountList.getAccountByUsername("arceus1121"));
        account = accountList.login("arceus1121","11234");
        accountDataSource.writeData(accountList);
        assertNull(account);
    }
}