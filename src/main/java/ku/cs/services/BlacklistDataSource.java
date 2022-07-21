package ku.cs.services;

import ku.cs.models.BlacklistAccount;
import ku.cs.models.BlacklistAccountList;
import ku.cs.models.Order;
import ku.cs.models.OrderList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BlacklistDataSource implements DataSource<BlacklistAccountList>{
    private String directoryName;
    private String fileName;

    public BlacklistDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        makeFileIfNotExist(directoryName, fileName);
    }

    public BlacklistDataSource() {
        this("data","blacklist_account.csv");
    }

    @Override
    public BlacklistAccountList readData() {
        BlacklistAccountList blacklistAccounts = new BlacklistAccountList();
        String path = directoryName + File.separator + fileName;
        File file =  new File(path);

        FileReader reader = null;
        BufferedReader buffer = null;

        try{
            reader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);
            String line = null;
            while ((line = buffer.readLine()) != null){
                String[] data = line.split(",");                                                        // ไว้เปรียบเทียบเวลา
                BlacklistAccount blacklistAccount = new BlacklistAccount(data[0],data[1],Integer.parseInt(data[2]));
                blacklistAccounts.addBlacklistAccount(blacklistAccount);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error " + fileName + " not found.");
        } catch (IOException e) {
            System.err.println("Error reading from " + fileName);
        } finally {
            try {
                if (buffer != null)
                    buffer.close();
            } catch (IOException e) {
                System.err.println("Error closing " + fileName);
            }
        }

        return blacklistAccounts;
    }

    @Override
    public void writeData(BlacklistAccountList blacklistAccountList) {
        String path = directoryName + File.separator + fileName;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);

            buffer.write(blacklistAccountList.toCsv());
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
