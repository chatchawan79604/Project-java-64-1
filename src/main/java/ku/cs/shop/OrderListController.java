package ku.cs.shop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.DataBox;
import ku.cs.models.OrderList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OrderListController {

    @FXML private Circle userImage;
    @FXML private GridPane grid;

    private User user;
    private Shop shop;
    private DataBox dataBox;
    private OrderList orderList;
    private Order order;


    public void initialize()throws FileNotFoundException {

        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        user = (User) dataBox.getCurrentlyAccount();
        shop = dataBox.getCurrentlyShop();
        orderList = dataBox.getOrderList();
        shop = user.getShop();
        shop.setOrderList(orderList.orderListInShop(shop.getName()));
        showSupOrder(shop);
        try {
            userImage.setFill(new ImagePattern(new Image(new FileInputStream(user.getPathPicture()))));
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }

    }
    public User getUser() {
        return user;
    }

    //แสดงออเดอร์ทั้งหมดโดยที่ออเดอร์ล่าสุดขึ้นก่อน
    public void showSupOrder(Shop shop){
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();
        int row = 1, column = 0;
        try {
            for (int i=0; i< shop.getSizeOrderList(); i++){
                Order order = shop.getOrderOfIndex(i);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/sup_order.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                SupOrderController supOrderController = fxmlLoader.getController();
                supOrderController.setData(dataBox);
                supOrderController.setProduct(shop.getOrderOfIndex(i));

                if(column == 1){
                    column = 0;
                    row++;
                }
                //เพิ่มสินค้าลงตาราง grid
                grid.add(anchorPane, column++, row);
                //ระยะห่างระหว่าง anchorPane
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    // Menubar
    @FXML public void handleMyShopButton(ActionEvent event) throws IOException {
    try {
        com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
    } catch (IOException e) {
        e.printStackTrace();
        }
    }

    @FXML
    void handleHistory(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("history_order", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleMyProfile(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("my_profile", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleShopButton(ActionEvent event) {
        if(user.getShop() == null){
            try {
                com.github.saacsos.FXRouter.goTo("create_shop", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//----------------------------------------------------------------------------------------------------------------------

    //แถบเมนูที่เกี่ยวกับร้านค้า
    @FXML
    public void handleBackToShopButton(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า my_shop ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    // สถานะการจัดส่งทั้งหมด
    @FXML
    void allStatusButton(ActionEvent event) {
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();
        int row = 1, column = 0;
        //แสดงออเดอร์ทั้งหมดโดยที่ออเดอร์ล่าสุดขึ้นก่อน
        try {

            for (int i=0; i< shop.getSizeOrderList(); i++){
                Order order = shop.getOrderOfIndex(i);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/sup_order.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                SupOrderController supOrderController = fxmlLoader.getController();
                supOrderController.setData(dataBox);
                supOrderController.setProduct(shop.getOrderOfIndex(i));


                if(column == 1){
                    column = 0;
                    row++;
                }
                //เพิ่มสินค้าลงตาราง grid
                grid.add(anchorPane, column++, row);
                //ระยะห่างระหว่าง anchorPane
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    // จัดส่งแล้ว
    @FXML
    public void shippedButton(ActionEvent event) {
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();
        int row = 1, column = 0;
        //แสดงออเดอร์ทั้งหมดโดยที่ออเดอร์ล่าสุดขึ้นก่อน
        try {

            for (int i=0; i< shop.getSizeOrderList(); i++){
                Order order = shop.getOrderOfIndex(i);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/sup_order.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                if (order.getStatus().equals("จัดส่งแล้ว")) {
                    SupOrderController supOrderController = fxmlLoader.getController();
                    supOrderController.setData(dataBox);
                    supOrderController.setProduct(shop.getOrderOfIndex(i));


                    if(column == 1){
                        column = 0;
                        row++;
                    }
                    //เพิ่มสินค้าลงตาราง grid
                    grid.add(anchorPane, column++, row);
                    //ระยะห่างระหว่าง anchorPane
                    GridPane.setMargin(anchorPane, new Insets(10));
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    // ยังไม่จัดส่ง
    @FXML
    void waitingForShipmentButton(ActionEvent event) {
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();
        int row = 1, column = 0;
        //แสดงออเดอร์ทั้งหมดโดยที่ออเดอร์ล่าสุดขึ้นก่อน
        try {
            for (int i = 0; i < shop.getSizeOrderList(); i++) {
                Order order = shop.getOrderOfIndex(i);


                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/sup_order.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                if (order.getStatus().equals("ยังไม่จัดส่ง")) {

                    SupOrderController supOrderController = fxmlLoader.getController();
                    supOrderController.setData(dataBox);
                    supOrderController.setProduct(shop.getOrderOfIndex(i));


                    if (column == 1) {
                        column = 0;
                        row++;
                    }
                    //เพิ่มสินค้าลงตาราง grid
                    grid.add(anchorPane, column++, row);
                    //ระยะห่างระหว่าง anchorPane
                    GridPane.setMargin(anchorPane, new Insets(10));
                } else {
                    continue;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
