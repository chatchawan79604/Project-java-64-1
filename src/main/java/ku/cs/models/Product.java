package ku.cs.models;

import ku.cs.services.PictureManager;

import java.time.LocalDateTime;

public class Product {
    private String shop;
    private String name;
    private String detail;
    private String picturePath;
    private double cost;
    private int quantity;
    private LocalDateTime timeAdded;
    private String serialnumber;
    private ReviewList reviewList;
    private ProductCategory productCategory;

    // Constructor รับข้อมูลของสินค้าที่สร้าง ได้แก่ ชื่อสินค้า รายละเอียดสินค้า รูปสินค้า ราคาสินค้า จำนวน และ serialnumber(กำหนดให้อัตโนมัติ)
    public Product(String shop,String name, String detail, String picturePath, double cost, int quantity, String serialnumber){
        this.shop = shop;
        this.name = name;
        this.detail = detail;
        this.picturePath = picturePath;
        this.cost = cost;
        this.quantity = quantity;
        this.timeAdded = LocalDateTime.now();
        this.serialnumber = serialnumber;

    }
    // ไว้ใช้ตอนอ่านข้อมูลจาก csv เพราะต้องการใช้เวลาตอนเพิ่มสินค้าอันเดิม
    public Product(String shop,String name, String detail, String picturePath, double cost, int quantity, LocalDateTime dateTime, String serialnumber){
        this(shop, name, detail, picturePath, cost, quantity, serialnumber);
        this.timeAdded = dateTime;
    }

    // ไว้ใช้ตอนอ่านข้อมูลจาก csv เมื่อสินค้ามี หมวดหมู่
    public Product(String shop, String name, String detail, String picturePath, double cost, int quantity, LocalDateTime dateTime, String serialnumber, ProductCategory productCategory){
        this(shop, name, detail, picturePath, cost, quantity,dateTime, serialnumber);
        this.productCategory = productCategory;
    }

    public String getShop() {
        return shop;
    }

    public String getName() {
        return name;
    }
    public String getDetail() {
        return detail;
    }
    public String getPicturePath() {
        return picturePath;
    }
    public double getCost() {
        return cost;
    }
    public int getQuantity() {
        return quantity;
    }
    public LocalDateTime getTimeAdded() {
        return timeAdded;
    }
    public String getSerialnumber(){
        return serialnumber;
    }
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductName(String productName) {
        this.name = productName;
    }
    public void setDetail(String detail){
        this.detail = detail;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String toCsv(){
        String result = "";
        result +=  getName() + "\n" + getDetail() + "\n" + getShop() + ","
                + getPicturePath() + "," + getCost() + "," + getQuantity()
                + "," + getTimeAdded() + "," + getSerialnumber();
        if(productCategory!=null){
            result += productCategory.toCsv();
        }

        return result;
    }

    public String toString(){
        return  getName() + "\n" + getDetail() + "\n" + getShop() + ","
                + getPicturePath() + "," + getCost() + "," + getQuantity()
                + "," + getTimeAdded() + "," + getSerialnumber();
    }

    // ใช้ตรวจสอบว่าสินค้านี้เป็นของร้านค้าชื่อนี้หรือไม่
    public boolean isInShop(String nameShop){
        return shop.equals(nameShop);
    }

    public void setReviewList(ReviewList reviewList) {
        this.reviewList = reviewList;
    }

    public ReviewList getReviewList() {
        return reviewList;
    }

    public void editProductImage(String imagePath){
        PictureManager pictureManager = new PictureManager();
        String newProductImagePath = pictureManager.importProfileImage(imagePath,"product");
        if(newProductImagePath == null){
            System.err.println("เปลี่ยนรูปภาพไม่สำเร็จ");
            return;
        }
        this.picturePath = newProductImagePath;
    }
}