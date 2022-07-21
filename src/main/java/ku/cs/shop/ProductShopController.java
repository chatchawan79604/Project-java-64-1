package ku.cs.shop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ku.cs.models.Product;
import ku.cs.models.ProductList;
import ku.cs.services.DataBox;
import ku.cs.services.DataSource;
import ku.cs.services.ProductsDataSource;
import ku.cs.utility.DialogAlert;
import ku.cs.utility.Number;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProductShopController {

    @FXML private ImageView productImage;
    @FXML private Label priceLabel;
    @FXML private Label productNameLabel;
    @FXML private Label quantityProduct;
    @FXML private TextField increaseQuantityTextField;

    private Product product;
    private DataBox dataBox;
    private DataSource<ProductList> productsDataSource;

    public void setProduct(Product product, int lowValue){

        this.product = product;
        try {
            productImage.setImage(new Image(new FileInputStream(product.getPicturePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        productNameLabel.setText(product.getName());
        priceLabel.setText(String.format("%.2f",product.getCost()));
        increaseQuantityTextField.setText("");

        if(product.getQuantity() <= lowValue){
            quantityProduct.setStyle("-fx-text-fill: #ff0000");
        }

        quantityProduct.setText(String.valueOf(product.getQuantity()));
    }

    public void setDataPasser(DataBox dataBox){
        this.dataBox = dataBox;
    }

    // ปุ่มเพิ่มจำนวนสินค้า
    @FXML
    public void handleIncreaseQuantityProduct(ActionEvent event) {
        if (!Number.checkIntegerNumber(increaseQuantityTextField.getText())){
            DialogAlert.showErrorDialogAlert("Error","กรุณาใส่ข้อมูลให้ถูกต้อง");
            increaseQuantityTextField.setText("");
            return;
        }
        int quantity = Integer.parseInt(increaseQuantityTextField.getText());
        if(quantity <= 0){
            DialogAlert.showErrorDialogAlert("Alert","จำนวนสินค้าไม่ถูกต้อง");
            increaseQuantityTextField.setText("");
        }
        else{
            increaseQuantityTextField.setText("");
            quantityProduct.setText(String.valueOf(quantity));
            product.setQuantity(product.getQuantity() + quantity);
            // แก้ไขจำนวนสินค้า แล้วเขียนลงไฟล์
            productsDataSource = new ProductsDataSource();
            productsDataSource.writeData(dataBox.getProductList());

            try {
                com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void clickProduct(MouseEvent event) {
        dataBox.setSelectedProduct(product);
        try {
            com.github.saacsos.FXRouter.goTo("edit_product", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
