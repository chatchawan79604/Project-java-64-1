package ku.cs.services;

import ku.cs.models.Shop;
import ku.cs.models.ShopList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ShopDataSource implements DataSource<ShopList> {
    private String directory;
    private String filename;

    public ShopDataSource(String directory,String filename){
        this.directory = directory;
        this.filename = filename;
        makeFileIfNotExist(directory, filename);
    }

    public ShopDataSource() {
        this("data","shop_data.csv");
    }

    @Override
    public ShopList readData() {
        ShopList shopList = new ShopList();
        File file = new File(directory + File.separator + filename);
        FileReader fileReader = null;
        BufferedReader buffer = null;
        try {
            fileReader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = null;
        try{
            while ((line = buffer.readLine()) != null){
                String[] data = line.split(",");
                Shop shop = new Shop(data[0].trim());
                shop.setLowValue(Integer.parseInt(data[1].trim()));
                shopList.addShop(shop);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error File not found.");
        } catch (IOException e) {
            System.err.println("Error reading from file.");
        } finally {
            try {
                buffer.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return shopList;
    }

    @Override
    public void writeData(ShopList shopList) {
        Path file = Paths.get(directory+File.separator+filename);
        try {
            BufferedWriter bufferedWriter = Files.newBufferedWriter(file, StandardCharsets.UTF_8);
            for (int index = 0;index < shopList.size();index++){
                bufferedWriter.write(
                        shopList.getIndexOfShop(index).getName() + ","
                        + shopList.getIndexOfShop(index).getLowValue());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e){
            System.err.println("Fail to write " + file.toString());
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