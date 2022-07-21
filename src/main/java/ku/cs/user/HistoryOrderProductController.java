package ku.cs.user;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.Order;
import ku.cs.services.DataBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HistoryOrderProductController {

    @FXML private ImageView productImage;
    @FXML private Label productNameLabel;
    @FXML private Label priceLabel;
    @FXML private Label NumProductLabel;
    @FXML private Label ShopLabel;
    @FXML private Label TrackingLabel;
    private DataBox dataBox;

    public void setProduct(Order order){
        try {
            productImage.setImage(new Image(new FileInputStream( ((dataBox.getProductList()).getProductBySerialNumber(order.getSerialNumber())).getPicturePath() )));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        productNameLabel.setText(order.getProduct());
        priceLabel.setText(String.format("%.2f", order.getPrice()));
        NumProductLabel.setText(String.format("%d",order.getQuantity()));
        ShopLabel.setText(order.getShop());
        if(order.getTrackingNumber().equals("null")){
            TrackingLabel.setText("สินค้ายังไม่ถูกจัดส่ง");
        }
        else {
            TrackingLabel.setText(order.getTrackingNumber());
        }

    }

    public void setDataBox(DataBox dataBox){
        this.dataBox = dataBox;
    }

}
