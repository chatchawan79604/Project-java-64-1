package ku.cs.services;

import ku.cs.models.AccountList;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class AccountDataSourceTest {
    @Test
    void testMakeFileIfNotExist(){
        String directory = "unit-test";
        String filename = "account_data.csv";

        DataSource<AccountList> dataSource;
        dataSource = new AccountDataSource(directory, filename);

        File file = new File(directory+File.separator+filename);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    @Test
    void testWriteReadFile(){
        String directory = "unit-test";
        String filename = "account_data.csv";

        DataSource<AccountList> dataSource;
        dataSource = new AccountDataSource(directory, filename);

        AccountList accountList = dataSource.readData();
    }
}