package ku.cs.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.Product;
import ku.cs.models.Review;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ReportedProductPreviewController {
    @FXML private ImageView imageProduct;
    @FXML private Label nameProductLabel;
    @FXML private Label costLabel;
    @FXML private Label detailProductLabel;

    public void setPreview(Product product){
        try {
            imageProduct.setImage(new Image(new FileInputStream(product.getPicturePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        nameProductLabel.setText(product.getName());
        costLabel.setText(String.format("%.2f บาท", product.getCost()));
        detailProductLabel.setText(product.getDetail());
    }
}
