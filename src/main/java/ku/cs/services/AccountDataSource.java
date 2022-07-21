package ku.cs.services;

import ku.cs.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class AccountDataSource implements DataSource<AccountList>{
    private String directory;
    private String filename;

    public AccountDataSource(String directory,String filename){
        this.directory = directory;
        this.filename = filename;
        makeFileIfNotExist(directory, filename);
    }
    public AccountDataSource(){
        this("data","account_data.csv");
    }
    @Override
    public AccountList readData() {
        File file = new File(directory + File.separator + filename);
        FileReader fileReader = null;
        BufferedReader buffer = null;
        DataSource<ShopList> shopDataSource = new ShopDataSource("data","shop_data.csv");
        ShopList shopList = shopDataSource.readData();

        try {
            fileReader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AccountList accountList = new AccountList();
        String line = null;
        try{
            while ((line = buffer.readLine()) != null){
                String[] data = line.split(",");
                if(data[0].equals("Buyer") || data[0].equals("Seller")){
                    User user = new User(data[0], data[1], data[2], data[3], data[5], shopList.getShop(data[4]),LocalDateTime.parse(data[6]));
                    accountList.addAccount(user);
                }
                else if(data[0].equals("Admin")){
                    Admin admin = new Admin(data[0],data[1],data[2],data[3],LocalDateTime.parse(data[6]));
                    accountList.addAccount(admin);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(file +" not found.");
        } catch (IOException e) {
            System.err.println("Error reading from "+file);
        } finally {
            try {
                buffer.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return accountList;
    }

    @Override
    public void writeData(AccountList accountList) {
        Path file;
        file = Paths.get(directory + File.separator + filename);
        try {
            BufferedWriter bufferedWriter = Files.newBufferedWriter(file, StandardCharsets.UTF_8);
            for (int index = 0; index < accountList.getSize(); index++){
                if (accountList.getAccountOfIndex(index) instanceof User){
                    User user =  (User) accountList.getAccountOfIndex(index);
                    bufferedWriter.write(user.toString());
                }
                else if(accountList.getAccountOfIndex(index) instanceof Admin){
                    Admin admin =  (Admin) accountList.getAccountOfIndex(index);
                    bufferedWriter.write(
                            admin.getAccountType() + ","
                            + admin.getUsername() + ","
                            + admin.getPassword() + ","
                            + admin.getAccountName() +
                            ",null,null,"
                            + (admin.getTimeLogin() == null ? null : admin.getTimeLogin().toString()));
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e){
            System.err.println("Fail to write " + file);
        }

    }

    @Override
    public void makeFileIfNotExist(String directory, String targetFile) {
        File file = new File(directory);
        if(!file.exists()){
            file.mkdir();
        }
        String path = directory + File.separator + targetFile;
        file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
