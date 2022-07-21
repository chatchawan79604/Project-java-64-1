package ku.cs.services;

import ku.cs.models.Product;
import ku.cs.models.ProductCategory;
import ku.cs.models.Report;
import ku.cs.models.ReportList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductReportDataSource implements DataSource<ReportList<Product>>{
    private String directoryName;
    private String fileName;

    public ProductReportDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        makeFileIfNotExist(directoryName, fileName);
    }

    @Override
    public ReportList<Product> readData() {
        ReportList<Product> reportedProducts = new ReportList<>();

        CategoryDataSource categoryDataSource = new CategoryDataSource();
        HashMap<String, ArrayList> mapCategory = categoryDataSource.readData();

        Product reportedProduct;

        FileReader reader = null;
        BufferedReader buffer = null;
        String line = null;
        try {
            reader = new FileReader(directoryName + File.separator + fileName, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);
            while((line = buffer.readLine()) != null){
                String category = line;
                String detail = buffer.readLine();
                line = buffer.readLine();
                String productName = line;
                line = buffer.readLine();
                String productDetail = line;
                line = buffer.readLine();
                String[] data = line.split(",");
                String shopName = data[0];
                String picturePath = data[1];
                double cost = Double.parseDouble(data[2]);
                int quantity = Integer.parseInt(data[3]);
                //Parse String to LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(data[4], DateTimeFormatter.ISO_DATE_TIME);
                String  serialnumber = data[5];
                // set category
                if(data.length > 6){
                    CreateProductCategory createProductCategory = new CreateProductCategory(data[6]);
                    String attribute;

                    ArrayList<String> arrayListCategory = mapCategory.get(data[6]);
                    for(int j=0; j< arrayListCategory.size(); j++){
                        attribute = arrayListCategory.get(j);
                        createProductCategory.addAttribute(attribute,data[7+j]);
                    }
                    ProductCategory productCategory = createProductCategory.getProductCategory();
                    reportedProduct = new Product(shopName, productName, productDetail, picturePath,cost, quantity, dateTime, serialnumber, productCategory);
                }
                else{
                    reportedProduct = new Product(shopName, productName, productDetail, picturePath,cost, quantity, dateTime, serialnumber);
                }
                line = buffer.readLine();
                data = line.split(",");
                String reporter = data[0];
                LocalDateTime time = LocalDateTime.parse(data[1], DateTimeFormatter.ISO_DATE_TIME);
                Report<Product> productReport = new Report<>(category,detail,reportedProduct,reporter,time);
                reportedProducts.addReport(productReport);
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reportedProducts;
    }

    @Override
    public void writeData(ReportList<Product> productReportList) {
        String path = directoryName + File.separator + fileName;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file,StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);

            buffer.write(productReportList.toCsv());
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
