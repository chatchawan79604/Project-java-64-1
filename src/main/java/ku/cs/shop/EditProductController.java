package ku.cs.shop;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import ku.cs.models.Product;
import ku.cs.models.ProductList;
import ku.cs.models.Shop;
import ku.cs.models.User;
import ku.cs.services.*;
import ku.cs.utility.DialogAlert;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class EditProductController {
    @FXML private Circle userImage;
    @FXML private TextField nameProductTextField;
    @FXML private TextArea detailProductTextArea;
    @FXML private TextField costTextField;
    @FXML private Label quantityLabel;
    @FXML private Label resultLabel;
    @FXML private ImageView imageProduct;
    @FXML private Label imagePath;
    @FXML private ComboBox comboBox;
    @FXML private VBox vBoxAttribute;

    private DataBox dataBox;
    private User user;
    private String picturePath;
    private Shop shop;
    private PictureManager pictureManager;
    private Product product;
    private DataSource<ProductList> productsDataSource;
    private String[] arrayInputAttribute;
    private String categorySelect;
    private ArrayList<AttributeController> attributeControllers;


    public void initialize() {
        System.out.println("initialize EditProductController");
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        user = (User) dataBox.getCurrentlyAccount();
        resultLabel.setText("");
        product = dataBox.getSelectedProduct();
        shop = dataBox.getCurrentlyShop();
        pictureManager = new PictureManager();
        picturePath = product.getPicturePath();
        try {
            userImage.setFill(new ImagePattern(new Image(new FileInputStream(user.getPathPicture()))));
            imageProduct.setImage(new Image(new FileInputStream(product.getPicturePath())));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        nameProductTextField.setText(product.getName());
        detailProductTextArea.setText(product.getDetail());
        costTextField.setText(String.format("%.2f", product.getCost()));
        quantityLabel.setText(Integer.toString(product.getQuantity()));

        comboBox.setItems(FXCollections.observableList(new ArrayList<>(dataBox.getCategory().keySet())));
        if(product.getProductCategory() != null){
            categorySelect = product.getProductCategory().getCategory();
        }
        comboBox.setPromptText(categorySelect);
        arrayInputAttribute = new String[dataBox.getCategory().get(categorySelect).size()];
        for(int i=0; i<dataBox.getCategory().get(categorySelect).size(); i++){
            arrayInputAttribute[i] = product.getProductCategory().getAttributeProductListByIndex(i).toString();
        }
        showProductFeatureFields(categorySelect);
    }

    // ปุ่มเลือกหมวดหมู่สินค้า
    public void selectCategoryComboBox(ActionEvent event){
        categorySelect = comboBox.getSelectionModel().getSelectedItem().toString();
        arrayInputAttribute = new String[dataBox.getCategory().get(categorySelect).size()];
        showProductFeatureFields(categorySelect);
    }

    // แสดงคุณลักษณะเพิ่มเติม
    public void showProductFeatureFields(String category){
        ArrayList<String> arrayListAttribute = dataBox.getCategory().get(category);
        attributeControllers = new ArrayList<>();
        vBoxAttribute.getChildren().removeAll(vBoxAttribute.getChildren());
        for(int i=0; i<arrayListAttribute.size(); i++){
            String attribute = arrayListAttribute.get(i);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ku/cs/product_feature_fields.fxml"));

            try {
                HBox hBox = fxmlLoader.load();
                AttributeController attributeController = fxmlLoader.getController();
                attributeController.setAttribute(attribute, i, arrayInputAttribute);
                attributeControllers.add(attributeController);
                vBoxAttribute.getChildren().add(hBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ปุ่มแก้ไขภาพสินค้า
    @FXML
    public void handleUploadImageButton(ActionEvent event) {
        PictureManager pictureManager = new PictureManager();
        FileChooser fileChooser = new FileChooser();
        File fileImage = null;
        fileImage = fileChooser.showOpenDialog(null);
        String imagePath = fileImage.getPath().replace("\\","/");
        this.imagePath.setText(imagePath);

        if(pictureManager.checkImageType(imagePath)) {
            try {
                picturePath = fileImage.getPath().replace("\\","/");
                InputStream inputStream = new FileInputStream(picturePath);
                imageProduct.setImage(new Image(inputStream, 200, 200, false, false));
            } catch (FileNotFoundException e) {
                System.err.println("imageProduct failed");
            }
        }
    }

    // ปุ่มแก้ไขสินค้า
    @FXML
    public void handleUpdateProduct(ActionEvent e) {
        String nameProduct = nameProductTextField.getText();
        String detail = (detailProductTextArea.getText()).replaceAll("\n",  "");
        String cost = costTextField.getText();
        String quantity = quantityLabel.getText();

        // check product
        int resultCheckProduct = shop.checkProduct(nameProduct, detail, cost, quantity) ;
        if(resultCheckProduct == 1){
            DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่ข้อมูลให้ครบทุกช่อง");
        }
        else if(resultCheckProduct == 2){
            DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่ราคาสินค้าให้ถูกต้อง");
        }
        else if(resultCheckProduct == 3){
            DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่จำนวนสินค้าให้ถูกต้อง");
        }
        for(int i = 0 ;i < arrayInputAttribute.length; i++){
            arrayInputAttribute[i] = attributeControllers.get(i).getAttributeTextField();
        }
        // ตรวจสอบว่าถ้าผู้ขายเลือกหมวดหมู่สินค้าจะสร้างคลาส ProductCategory เพื่อเก็บคุณลักษณะของสินค้า
        if(categorySelect != null){
            ArrayList<String> attributeList = dataBox.getCategory().get(categorySelect);
            if(shop.checkAttributeProduct(arrayInputAttribute) == 1){
                DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่ข้อมูลให้ครบทุกช่อง");
                return;
            }
            CreateProductCategory createProductCategory = new CreateProductCategory(categorySelect);
            for(int i=0; i<dataBox.getCategory().get(categorySelect).size(); i++){
                String inputData = arrayInputAttribute[i];
                String attribute = attributeList.get(i);
                try {
                    createProductCategory.addAttribute(attribute,inputData);
                } catch (NumberFormatException exception) {
                    DialogAlert.showErrorDialogAlert("Alert","กรุณาตววจสอบความถูกต้องของข้อมูลคุณลักษณะสินค้า");
                    return;
                }
            }
            product.setProductCategory(createProductCategory.getProductCategory());
        }
        if(resultCheckProduct == 0){
            product.setProductName(nameProduct);
            product.setDetail(detail);
            product.setCost(Double.parseDouble(cost));
            product.setQuantity(Integer.parseInt(quantity));

            // แก้ไขรูปสินค้า แล้วเขียนลงไฟล์ product_data.csv
            if(picturePath != product.getPicturePath()){
                picturePath = pictureManager.importProfileImage(picturePath, "product");
            }
            productsDataSource = new ProductsDataSource();
            productsDataSource.writeData(dataBox.getProductList());
            ProductsDataSource productsDataSource = new ProductsDataSource("data", "product_data.csv");
            productsDataSource.writeData(dataBox.getProductList());

            try {
                com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        ProductsDataSource productsDataSource = new ProductsDataSource("data","product_data.csv");
        productsDataSource.writeData(dataBox.getProductList());
    }
//----------------------------------------------------------------------------------------------------------------------

    //แถบเมนูที่เกี่ยวกับร้านค้า
    @FXML
    public void handleBackToShopButton(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addProductButton(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("add_product", dataBox);
        } catch (IOException e) {
            e.printStackTrace(        );
            e.printStackTrace();
        }
    }

    @FXML
    void allProductOfStoreButton(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void orderListOfStoreButton(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("order_list", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//----------------------------------------------------------------------------------------------------------------------

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

    public void handleMyProfile(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("my_profile", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------
}
