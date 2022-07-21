package ku.cs.shop;

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
import javafx.scene.control.TextField;
import ku.cs.services.DataBox;
import ku.cs.models.ProductList;
import ku.cs.services.DataSource;
import ku.cs.services.ShopDataSource;
import ku.cs.utility.DialogAlert;
import ku.cs.utility.Number;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyShopController {
    @FXML private Circle userImage;
    @FXML private GridPane grid;
    @FXML private TextField lowValueTextField;

    private DataBox dataBox;
    private User user;
    private ProductList products;
    private Shop shop;
    private int lowValue;

    public void initialize() {
        System.out.println("initialize myShopController");
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        user = (User) dataBox.getCurrentlyAccount();
        products = dataBox.getProductList();
        shop = dataBox.getShopList().getShop(user.getShop().getName());
        dataBox.setCurrentlyShop(shop);

        try {
            userImage.setFill(new ImagePattern(new Image(new FileInputStream(user.getPathPicture()))));
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }

        lowValueTextField.setText(String.valueOf(shop.getLowValue()));
        lowValue = shop.getLowValue();
        showProducts(shop);
    }

    public void showProducts(Shop shop){
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();

        int row = 1, column = 0;
        //แสดงสินค้าทั้งหมดโดยที่สินค้าล่าสุดขึ้นก่อน
        try {
            for(int i=0; i<shop.getSizeOfProductList(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/product_shop.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ProductShopController productShopController = fxmlLoader.getController();
                productShopController.setProduct(shop.getProductOfIndex(i), lowValue);
                productShopController.setDataPasser(dataBox); // pass data

                if(column == 5){
                    column = 0;
                    row++;
                }
                //เพิ่มสินค้าลงตาราง grid
                grid.add(anchorPane, column++, row);
                //ระยะห่างระหว่าง anchorPane
                GridPane.setMargin(anchorPane, new Insets(35));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
//----------------------------------------------------------------------------------------------------------------------

    //แถบเมนูที่เกี่ยวกับร้านค้า
    @FXML
    public void addProductButton(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("add_product", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void allProductOfStoreButton(ActionEvent event) {
        initialize();
    }

    // set Low value
    @FXML
    public void setLowValueButton(ActionEvent event) {
        if (!Number.checkIntegerNumber(lowValueTextField.getText())){
            DialogAlert.showErrorDialogAlert("Error","กรุณาใส่ข้อมูลให้ถูกต้อง");
            lowValueTextField.setText(lowValue+"");
            return;
        }
        if (Integer.parseInt(lowValueTextField.getText()) < 0){
            DialogAlert.showErrorDialogAlert("Error","กรุณาใส่ข้อมูลให้ถูกต้อง");
            lowValueTextField.setText(lowValue+"");
            return;
        }
        int lowValue = Integer.parseInt(lowValueTextField.getText());
        if(lowValue >=0 ){
            shop.setLowValue(lowValue);
            DataSource<ShopList> shopDataSource = new ShopDataSource("data","shop_data.csv");
            shopDataSource.writeData(dataBox.getShopList());
        }
        else{
            shop.setLowValue(0);
            lowValueTextField.setText("0");
        }
        initialize();

    }
    @FXML
    public void orderListOfStoreButton(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("order_list", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // -------------------------------------------------------------------------

    // Menubar
    @FXML
    public void handleShopButton(ActionEvent event) {
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
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMyProfile(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("my_profile", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // ปุ่มกลับหน้ากลัก (marketplace)
    @FXML
    public void backToMainPageButton(ActionEvent event){
        try {
            com.github.saacsos.FXRouter.goTo("marketplace", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------
}