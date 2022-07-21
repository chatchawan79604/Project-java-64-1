package ku.cs.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ku.cs.models.Admin;
import ku.cs.models.Product;
import ku.cs.models.ProductList;
import ku.cs.models.User;
import ku.cs.models.ReportList;
import ku.cs.services.DataBox;
import ku.cs.services.DataSource;
import ku.cs.services.ProductReportDataSource;
import ku.cs.services.ProductsDataSource;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReportedProductDetailController {
    private DataBox dataBox;
    @FXML private GridPane grid;
    @FXML private Label categoryLabel;
    @FXML private Label detailLabel;
    @FXML private Label reporterLabel;
    @FXML private Label timeLabel;


    public void initialize(){
        this.dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        categoryLabel.setText(dataBox.getProductReport().getCategory());
        detailLabel.setText(dataBox.getProductReport().getDetail());
        reporterLabel.setText(dataBox.getProductReport().getReporter());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("วันที่ d MMMM yyyy เวลา HH:mm:ss",new Locale("th","TH"));
        timeLabel.setText(dataBox.getProductReport().getTime().format(timeFormatter));
        showPreview();
    }

    private void showPreview() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ku/cs/reported_product_preview.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            ReportedProductPreviewController reportedProductPreviewController = fxmlLoader.getController();
            reportedProductPreviewController.setPreview(dataBox.getProductReport().getReportedObject());

            grid.add(anchorPane,0,0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleBanSeller(ActionEvent event) {
        String shopName = dataBox.getProductReport().getReportedObject().getShop();
        User user = dataBox.getAccountList().getUserByShop(shopName);
        ((Admin)dataBox.getCurrentlyAccount()).banUser(user);
        dataBox.updateBlacklistAccount();
        handleDeleteReport(event);
    }

    @FXML
    public void handleDeleteReport(ActionEvent event) {
        DataSource<ReportList<Product>> reportListDataSource = new ProductReportDataSource("data", "reported_product.csv");
        dataBox.getProductReports().removeReportIfContain(dataBox.getProductReport());
        reportListDataSource.writeData(dataBox.getProductReports());
        handleBack(event);
    }

    @FXML
    public void handleBack(ActionEvent event){
        try {
            dataBox.setProductReport(null);
            com.github.saacsos.FXRouter.goTo("home_admin",dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
