package ku.cs.services;

import ku.cs.models.Product;
import ku.cs.models.ProductCategory;
import ku.cs.models.ProductList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductsDataSource implements DataSource<ProductList> {

    private String directoryName;
    private String fileName;

    public ProductsDataSource(){
        this("data", "product_data.csv");
    }

    public ProductsDataSource(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        makeFileIfNotExist(directoryName, fileName);
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

    // อ่านข้อมูลจากไฟล์ csv มาสร้างเป็น object Product แล้วเพิ่มใน ProductArrayList
    @Override
    public ProductList readData(){
        ProductList products = new ProductList();

        CategoryDataSource categoryDataSource = new CategoryDataSource();
        HashMap<String, ArrayList> mapCategory = categoryDataSource.readData();

        String path = directoryName + File.separator + fileName;
        File file =  new File(path);

        FileReader reader = null;
        BufferedReader buffer = null;
        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);

            String line = buffer.readLine(); //อ่านบรรทัดแรกทิ้ง
            line = buffer.readLine();
            line = buffer.readLine();
            while ( (line = buffer.readLine())!=null ){
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
                    Product product = new Product(shopName, productName, productDetail, picturePath,cost, quantity, dateTime, serialnumber, productCategory);
                    products.addProduct(product);
                }
                else{
                    Product product = new Product(shopName, productName, productDetail, picturePath,cost, quantity, dateTime, serialnumber);
                    products.addProduct(product);
                }

            }
            products.sortLastProducts();
        }
        catch (FileNotFoundException e){
            System.err.println("Cannot read file "+ path);
        }
        catch (IOException e) {
            System.err.println("Error reading form data");
        }
        finally {
            try{
                buffer.close();
                reader.close();
            } catch (IOException e){
                System.err.println("Error closing files");
            }
        }
        return products;
    }
    @Override
    public void writeData(ProductList productList) {
        String path = directoryName + File.separator + fileName;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file,StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);

            buffer.write("productNam\nproductDetail\nshopName,picturePath,cost,quantity,time,serial number\n");
            buffer.write(productList.toCsv());
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(buffer != null){
                    buffer.close();
                    writer.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addData(Product productAdd){
        String path = directoryName + File.separator + fileName;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8,true);
            buffer = new BufferedWriter(writer);

            buffer.write(productAdd.toCsv());
            buffer.newLine();
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
}
