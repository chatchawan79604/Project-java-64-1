package ku.cs.models;

import ku.cs.services.OrderDataSource;
import ku.cs.services.PictureManager;

import java.time.LocalDateTime;


public class User extends Account{
    private String imagePath;
    private Shop shop;
    private OrderList orderList = new OrderList();

    //register propose constructor
    public User(String accountType, String username, String password, String accountName, String imagePath) {
        super(accountType, accountName, username, password);
        this.imagePath = imagePath;
    }

    //read-data purpose constructor
    public User(String accountType, String username, String password, String accountName, String imagePath, Shop shop, LocalDateTime time) {
        super(accountType, accountName, username, password);
        super.setTimeLogin(time);
        this.imagePath = imagePath;
        this.shop = shop;
    }

    public void addOrder(Order order){
        orderList.addOrder(order);
    }

    //getter
    public String getPathPicture() {
        return imagePath;
    }
    public Shop getShop() {
        return shop;
    }

    //setter
    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void createShop(String name){
        this.shop = new Shop(name);
        this.accountType = "Seller";
    }

    public void changeProfileImage(String imagePath) {
        PictureManager pictureManager = new PictureManager();
        String newProfileImagePath = pictureManager.importProfileImage(imagePath, "user_profile_images");
        if(newProfileImagePath == null){
            System.err.println("เปลี่ยนรูปภาพไม่สำเร็จ");
            return;
        }
        this.imagePath = newProfileImagePath;
    }

    public void buyProduct(Shop shop, Product product, int quantity){
        if(product.getQuantity() >= quantity){
            OrderDataSource orderDataSource = new OrderDataSource();
            OrderList orderList = orderDataSource.readData();
            shop.sellProduct(product.getName(),quantity);
            System.out.println("ซื้อสำเร็จ");
            Order order = new Order(product.getName(),quantity,product.getCost()*quantity,getUsername(),shop.getName(),product.getSerialnumber());
            orderList.addOrder(order);
            orderDataSource.writeData(orderList);
            this.addOrder(order);
        }
        else{
            System.err.println("ซื้อขายไม่สำเร็จ โปรดตรวจสอบจำนวนสินค้า");
        }
    }

    @Override
    public String toString() {
        return accountType + "," + getUsername() + "," + getPassword() + "," + getAccountName() + "," + getShop() + "," +
                getPathPicture() + "," + getTimeLogin();
    }
}
