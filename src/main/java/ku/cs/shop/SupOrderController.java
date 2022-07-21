package ku.cs.shop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.OrderList;
import ku.cs.models.Product;
import ku.cs.models.User;
import ku.cs.models.Order;
import ku.cs.services.DataBox;
import ku.cs.services.DataSource;
import ku.cs.services.OrderDataSource;
import ku.cs.utility.DialogAlert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SupOrderController {

    @FXML private Label productNameLabel;
    @FXML private Label orderQuantity;
    @FXML private Label productPrice;
    @FXML private Label myShipping;
    @FXML private Label trackingNumber;
    @FXML private ImageView productImage;
    @FXML private TextField trackingTextField;
    @FXML private Button saveTracking;

    private Product product;
    private Order order;
    private DataBox dataBox;


    public void setProduct(Order order){
        if(order.getStatus().equals("จัดส่งแล้ว")){
            trackingTextField.setManaged(false);
            saveTracking.setVisible(false);
        }
        this.order = order;
        productNameLabel.setText(order.getProduct());
        product = dataBox.getProductList().getProductBySerialNumber(order.getSerialNumber());
        String totalCost = "0";
        totalCost = String.format("%.2f",order.getPrice());

        productPrice.setText(totalCost);
        orderQuantity.setText(String.valueOf(order.getQuantity()));
        myShipping.setText(order.getStatus());

        if(order.getTrackingNumber().equals("null")){
            trackingNumber.setManaged(false);
        }else {
            trackingNumber.setText(order.getTrackingNumber());
        }
        try {
            productImage.setImage(new Image(new FileInputStream(product.getPicturePath())));
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    //  เพิ่มหมายเลขพัศดุ
    @FXML void trackingButton(ActionEvent event) {
        String trackingNumber;
        trackingNumber = trackingTextField.getText();

        if(trackingTextField.getText().equals("")) {
            DialogAlert.showErrorDialogAlert("Alert","กรุณากรอกเลขพัสดุ");
        }
        else if(!trackingTextField.getText().matches("[A-Za-z0-9]*")) {
            DialogAlert.showErrorDialogAlert("Alert","เลขพัสดุต้องประกอบไปด้วยตัวอักษร A-Z, a-z และ 0-9 เท่านั้น");
        }
        else {
            order.setTrackingNumber(trackingNumber);
            order.changeStatus();
            dataBox.getOrderList().addTracking(order);
            DataSource<OrderList> orderDataSource = new OrderDataSource();
            orderDataSource.writeData(dataBox.getOrderList());
            DialogAlert.showInformationDialogAlert("Alert","เพิ่มเลขพัสดุสำเร็จ");

            try {
                com.github.saacsos.FXRouter.goTo("order_list", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setData(DataBox dataBox) {
        this.dataBox = dataBox;
    }
}
