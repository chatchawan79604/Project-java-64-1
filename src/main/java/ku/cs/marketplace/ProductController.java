package ku.cs.marketplace;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ku.cs.models.Product;
import ku.cs.services.DataBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProductController {
    @FXML private ImageView productImage;
    @FXML private Label productNameLabel;
    @FXML private Label priceLabel;

    private Product product;
    private DataBox dataBox;

    public void setProduct(Product product){

        this.product = product;
        try {
            productImage.setImage(new Image(new FileInputStream(product.getPicturePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        productNameLabel.setText(product.getName());
        priceLabel.setText(String.format("฿ %.2f",product.getCost()));
    }

    public void setData(DataBox data) {
        this.dataBox = data;
    }

    @FXML
    public void clickProduct(MouseEvent event) {
        dataBox.setSelectedProduct(product);
        try {
            com.github.saacsos.FXRouter.goTo("product_details", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}