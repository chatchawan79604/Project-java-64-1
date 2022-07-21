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
import ku.cs.models.*;
import ku.cs.services.*;
import ku.cs.utility.DialogAlert;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AddProductController extends javax.swing.JFrame {

    @FXML private Circle userImage;
    @FXML private TextField nameProductTextField;
    @FXML private TextArea detailProductTextArea;
    @FXML private TextField costTextField;
    @FXML private TextField quantityTextField;
    @FXML private Label resultLabel;
    @FXML private ImageView imageProduct;
    @FXML private Label imagePathLabel;
    @FXML private ComboBox comboBox;
    @FXML private VBox vBoxAttribute;

    private DataBox dataBox;
    private User user;
    private String picturePath;
    private Shop shop;
    private PictureManager pictureManager;
    private String[] arrayInputAttribute;
    private ProductCategory productCategory;
    private String categorySelect;
    private ArrayList<AttributeController> attributeControllers;

    public void initialize() {
        System.out.println("initialize AddProductController");
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        user = (User) dataBox.getCurrentlyAccount();
        resultLabel.setText("");
        try {
            userImage.setFill(new ImagePattern(new Image(new FileInputStream(user.getPathPicture()))));
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }

        shop = dataBox.getCurrentlyShop();
        pictureManager = new PictureManager();
        comboBox.setItems(FXCollections.observableList(new ArrayList<>(dataBox.getCategory().keySet())));
        try {
            imageProduct.setImage(new Image (new FileInputStream("data" + File.separator + "images" + File.separator + "default" + File.separator + "default_product.png" )) );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        categorySelect = "ไม่มีหมวดหมู่";
        showProductFeatureFields(categorySelect);

    }

    // ปุ่มเลือกหมวดหมู่สินค้า
    public void selectCategoryComboBox(ActionEvent event){
        categorySelect = comboBox.getSelectionModel().getSelectedItem().toString();
        showProductFeatureFields(categorySelect);
    }

    // แสดงคุณลักษณะเพิ่มเติมให้กรอกข้อมูล
    public void showProductFeatureFields(String category){
        ArrayList<String> arrayListAttribute = dataBox.getCategory().get(category);
        arrayInputAttribute = new String[arrayListAttribute.size()];
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

    // ----------------------------------------------------------------------------------------------------------------------

    // ปุ่มเพิ่มสินค้า
    @FXML
    public void handleAddProduct(ActionEvent event) {
        String nameProduct = nameProductTextField.getText();
        String detail = detailProductTextArea.getText().replaceAll("\n","");
        String cost = costTextField.getText();
        String quantity = quantityTextField.getText();
        // check product
        int resultCheckProduct = shop.checkProduct(nameProduct, detail, cost, quantity);
        if(resultCheckProduct == 1){
            DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่ข้อมูลให้ครบทุกช่อง");
            return;
        }
        else if(resultCheckProduct == 2){
            DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่ราคาสินค้าให้ถูกต้อง");
            return;
        }
        else if(resultCheckProduct == 3){
            DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่จำนวนสินค้าให้ถูกต้อง");
            return;
        }
        else if(picturePath == null || picturePath.equals("/ku/cs/images/default/default_profile_picture.png")) {
            DialogAlert.showErrorDialogAlert("Alert","กรุณอัพโหลดรูปภาพ");
            return;
        }
        for(int i = 0 ;i < arrayInputAttribute.length; i++){
            arrayInputAttribute[i] = attributeControllers.get(i).getAttributeTextField();
        }
        if(resultCheckProduct == 0){
            ArrayList<String> attributeList = dataBox.getCategory().get(categorySelect);
            if(shop.checkAttributeProduct(arrayInputAttribute) == 1){
                DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่ข้อมูลให้ครบทุกช่อง");
                return;
            }
            picturePath = pictureManager.importProfileImage(picturePath, "product");
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

            nameProductTextField.setText("");
            detailProductTextArea.setText("");
            costTextField.setText("");
            quantityTextField.setText("");
            
            productCategory = createProductCategory.getProductCategory();

            Product productAdd = new Product(shop.getName(),nameProduct,detail,picturePath, Double.parseDouble(cost), Integer.parseInt(quantity), String.format("%010d", dataBox.getProductList().getSize()+1));
            productAdd.setProductCategory(productCategory);

            shop.addProduct(productAdd);
            dataBox.getProductList().addProduct(productAdd);
            // append สินค้าลงไฟล์ product_data.csv
            ProductsDataSource productsDataSource = new ProductsDataSource();
            productsDataSource.addData(productAdd);
            // สร้างโฟล์เดอร์สำหรับเก็บ review
            DataSource<ReviewList> reviewDataSource = new ReviewDataSource("data" + File.separator + "review", String.valueOf(productAdd.getSerialnumber()) + ".csv");
            productAdd.setReviewList(reviewDataSource.readData());

            try {
                com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//----------------------------------------------------------------------------------------------------------------------

    //ปุ่มเกี่ยวกับของร้าน
    @FXML
    public void handleBackToShopButton(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า my_shop ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
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

    @FXML
    public void handleUploadImageButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File fileImage = null;
        fileImage = fileChooser.showOpenDialog(null);
        try {
            if(fileImage == null) return;
            picturePath = fileImage.getPath().replace("\\","/");
            InputStream inputStream = new FileInputStream(picturePath);
            imageProduct.setImage(new Image(inputStream,200,200,false,false));
            this.imagePathLabel.setText(picturePath);
        } catch (FileNotFoundException e) {
            System.err.println("imageProduct failed");
        }
    }
    //------------------------------------------------------------------------------------------------------------------
}
