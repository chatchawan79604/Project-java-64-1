package ku.cs.marketplace;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.Product;
import ku.cs.services.DataBox;
import ku.cs.models.ProductList;
import ku.cs.models.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class StorePageController {


    @FXML private Circle userImage;
    @FXML private TextField minimumPriceTextField;
    @FXML private TextField maximumPriceTextField;
    @FXML private GridPane grid;
    @FXML private Label nameShopLabel;
    @FXML private ComboBox categoryComboBox;

    private User user;
    private Product product;
    private ProductList products;
    private ProductList productsInShop;
    private ProductList productsShow;
    private DataBox dataBox;

    @FXML
    public void initialize(){

        System.out.println("initialize ProductDetailsController");

        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        user = (User) dataBox.getCurrentlyAccount();
        product = dataBox.getSelectedProduct();
        products = dataBox.getProductList();

        try {
            userImage.setFill(new ImagePattern(new Image(new FileInputStream(user.getPathPicture()))));
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        nameShopLabel.setText(product.getShop());
        productsInShop = products.getProductsInShop(product.getShop());
        productsShow = productsInShop.copyProductList();
        ArrayList<String> categoryStringArrayList = (new ArrayList<>(dataBox.getCategory().keySet()));
        categoryStringArrayList.add("ทั้งหมด");
        categoryComboBox.setItems(FXCollections.observableList(categoryStringArrayList));
        showProducts(productsShow);
    }

    public void showProducts(ProductList products){
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();

        int row = 1, column = 0;
        //แสดงสินค้าทั้งหมดโดยที่สินค้าล่าสุดขึ้นก่อน
        try {
            for(int i=0; i<products.getSize(); i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/product.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ProductController productController = fxmlLoader.getController();
                productController.setData(dataBox); // pass data
                productController.setProduct(products.getProduct(i));

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

    // แถบเมนูจัดเรียงสินค้า(แถบล่าง)
    // ปุ่มสินค้าทั้งหมด
    @FXML
    public void allProductButton(ActionEvent event) {
        productsShow = productsInShop.copyProductList();
        showProducts(productsShow);
    }
    // ปุ่มค้าหาสินค้าตามช่วงราคา
    @FXML
    public void searchRangeCostButton(ActionEvent event) {
        double max = productsInShop.maxCostProduct(), min = 0;
        if(!maximumPriceTextField.getText().equals("")){
            max = Double.parseDouble(maximumPriceTextField.getText());
        }
        if(!minimumPriceTextField.getText().equals("")){
            min = Double.parseDouble(minimumPriceTextField.getText());
        }
        productsShow = productsInShop.sortRangeCostProduct(max >=0 ? max : 0, min >=0 ? min : 0);
        showProducts(productsShow);
        minimumPriceTextField.clear();
        maximumPriceTextField.clear();
    }
    // ปุ่มเรียงจากสินค้าราคาน้อยไปมาก
    @FXML
    public void ascendingButton(ActionEvent event) {
        productsShow = productsShow.sortProductsAscending();
        showProducts(productsShow);
    }
    // ปุ่มเรียงจากสินค้าราคามากไปน้อย
    @FXML
    public void descendingButton(ActionEvent event) {
        productsShow = productsShow.sortProductsDescending();
        showProducts(productsShow);
    }
    // เลือกหมวดหมู่สินค้า
    @FXML
    void selectCategoryComboBox(ActionEvent event) {
        String categorySelect = categoryComboBox.getSelectionModel().getSelectedItem().toString();
        productsShow = productsInShop.copyProductList();
        if(categorySelect.equals("ทั้งหมด")){
            showProducts(productsShow);
            return;
        }
        productsShow = productsShow.sortProductsByCategory(categorySelect);
        showProducts(productsShow);
    }

    // -------------------------------------------------------------------------

    // Menu Bar
    // ปุ่มย้อนกลับ
    @FXML
    public void handleBack(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("product_details");
        } catch (IOException e) {
            e.printStackTrace();
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
    // -------------------------------------------------------------------------
}
