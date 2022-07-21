package ku.cs.models;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Order {
    private String shop;
    private String buyer;
    private String product;
    private LocalDateTime time;
    private int quantity;
    private String status;
    private String trackingNumber;
    private String serialNumber;
    private double price;

    //ใช้ตอนสร้าง Order time เป็น now และกำหนด serialNumber อัตโนมัติ
    public Order(String product,int quantity,double price,String buyer,String shop,String serialNumber){
        this.shop = shop;
        this.buyer = buyer;
        this.quantity = quantity;
        this.product = product;
        this.time = LocalDateTime.now();
        this.status = "ยังไม่จัดส่ง";
        this.serialNumber = serialNumber;
        this.price = price;
    }

    //ใช้ตอนอ่านไฟล์ Order
    public Order(String shop, String buyer, String product, LocalDateTime time, int quantity, double price, String status, String trackingNumber, String serialNumber) {
        this.shop = shop;
        this.buyer = buyer;
        this.product = product;
        this.time = time;
        this.quantity = quantity;
        this.status = status;
        this.trackingNumber = trackingNumber;
        this.serialNumber = serialNumber;
        this.price = price;
    }

    public String getBuyerUsername() {
        return buyer;
    }

    public String getProduct() {
        return product;
    }

    public String getShop() {
        return shop;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void changeStatus(){
        if(status.equals("ยังไม่จัดส่ง")){
            status = "จัดส่งแล้ว";
        }
    }

    public String getStatus() {
        return status;
    }

    public String getTrackingNumber() {
        return "" + trackingNumber;
    }

    @Override
    public String toString() {
        return buyer + "," + shop + "," + product + "," + time + "," + quantity + ","+ price + "," + status + "," + trackingNumber + "," + serialNumber;
    }


    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public double getPrice() {
        return price;
    }

}
