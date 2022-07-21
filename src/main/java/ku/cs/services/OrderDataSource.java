package ku.cs.services;

import ku.cs.models.Order;
import ku.cs.models.OrderList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderDataSource implements DataSource<OrderList>{
    private String directoryName;
    private String fileName;

    public OrderDataSource(){
        this("data","order_data.csv");
    }

    public OrderDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        makeFileIfNotExist(directoryName, fileName);

    }

    @Override
    public OrderList readData() {
        OrderList orderList = new OrderList();
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
                Order order = new Order(data[1],data[0],data[2], LocalDateTime.parse(data[3], DateTimeFormatter.ISO_DATE_TIME),Integer.parseInt(data[4]),Double.parseDouble(data[5]),data[6],data[7],data[8]);
                orderList.addOrder(order);

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

        return orderList;
    }

    @Override
    public void writeData(OrderList orderList) {
        String path = directoryName + File.separator + fileName;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file,StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);

            buffer.write(orderList.toCsv());
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
        File file = new File(directoryName);
        if(!file.exists()){
            file.mkdir();
        }
        String path = directoryName + File.separator + fileName;
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
