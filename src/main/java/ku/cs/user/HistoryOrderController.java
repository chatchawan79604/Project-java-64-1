package ku.cs.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.DataBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class HistoryOrderController {

    private DataBox dataBox;
    private User user;
    @FXML private GridPane grid;
    @FXML private Circle miniImage;


    public void initialize() throws FileNotFoundException {
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        user = (User) dataBox.getCurrentlyAccount();
        try {
            miniImage.setFill(new ImagePattern(new Image(new FileInputStream(user.getPathPicture()))));
        } catch (FileNotFoundException e) {
            System.err.println("โหลดรูปภาพไม่สำเร็จ");
        }
        showHistoryOrderProducts();
    }

    //I want get product order , เดี่๋ยวทำต่อ ขาดออเดอร์
    public void showHistoryOrderProducts(){
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();

        int row = 1, column = 0;
        //แสดงสินค้าทั้งหมดโดยที่สินค้าล่าสุดขึ้นก่อน
        try {
            ArrayList<Order> orderArrayList = new ArrayList<>();
            orderArrayList = dataBox.getOrderList().orderListOfUser(user.getUsername());
            for (Order i : orderArrayList){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/history_order_product.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                HistoryOrderProductController historyOrderProductController = fxmlLoader.getController();
                historyOrderProductController.setDataBox(dataBox);
                historyOrderProductController.setProduct(i);

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

    @FXML
    public void handleMainPageButton(ActionEvent event){
        try {
            com.github.saacsos.FXRouter.goTo("marketplace", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMyProfile(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("my_profile",dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleShop(ActionEvent event) {
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

    @FXML
    public void handleHistory(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("history_order", dataBox);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า my_profile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}