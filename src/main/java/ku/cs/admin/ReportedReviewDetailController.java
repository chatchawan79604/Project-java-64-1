package ku.cs.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ku.cs.marketplace.ReviewController;
import ku.cs.models.Admin;
import ku.cs.models.Review;
import ku.cs.models.ReviewList;
import ku.cs.models.User;
import ku.cs.models.ReportList;
import ku.cs.services.DataBox;
import ku.cs.services.DataSource;
import ku.cs.services.ReviewDataSource;
import ku.cs.services.ReviewReportDataSource;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReportedReviewDetailController {
    private DataBox dataBox;
    @FXML private GridPane grid;
    @FXML private Label categoryLabel;
    @FXML private Label detailLabel;
    @FXML private Label reporterLabel;
    @FXML private Label timeLabel;


    public void initialize(){
        this.dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        categoryLabel.setText(dataBox.getReviewReport().getCategory());
        detailLabel.setText(dataBox.getReviewReport().getDetail());
        reporterLabel.setText(dataBox.getReviewReport().getReporter());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("วันที่ d MMMM yyyy เวลา HH:mm:ss",new Locale("th","TH"));
        timeLabel.setText(dataBox.getReviewReport().getTime().format(timeFormatter));
        showPreview();
    }

    private void showPreview() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ku/cs/review.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            ReviewController reviewController = fxmlLoader.getController();
            reviewController.setReview(dataBox.getReviewReport().getReportedObject(),dataBox.getReviewReport().getReporter());
            reviewController.hiddenButton();
            grid.add(anchorPane,0,0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBanReviewer(ActionEvent event) {
        User user = (User)dataBox.getAccountList().getAccountByUsername(dataBox.getReviewReport().getReportedObject().getUsernameReviewer());
        ((Admin)dataBox.getCurrentlyAccount()).banUser(user);
        dataBox.updateBlacklistAccount();
        handleDeleteReview(event);
    }

    @FXML
    void handleDeleteReview(ActionEvent event) {
        for(int serial = 1; serial < dataBox.getProductList().getSize(); serial++){
            String serialNumber = String.format("%010d",serial);
            System.out.println(serialNumber);
            if(dataBox.getProductList().getProductBySerialNumber(serialNumber).getReviewList().removeReviewIfContain(dataBox.getReviewReport().getReportedObject())){
                DataSource<ReviewList> reviewDataSource = new ReviewDataSource("data/review",serialNumber+".csv");
                reviewDataSource.writeData(dataBox.getProductList().getProductBySerialNumber(serialNumber).getReviewList());
                break;
            }
        }
        handleDeleteReport(event);
    }

    @FXML
    void handleDeleteReport(ActionEvent event) {
        DataSource<ReportList<Review>> reviewDataSource = new ReviewReportDataSource("data","reported_review.csv");
        dataBox.getReviewReports().removeReportIfContain(dataBox.getReviewReport());
        reviewDataSource.writeData(dataBox.getReviewReports());
        try {
            dataBox.setReviewReport(null);
            com.github.saacsos.FXRouter.goTo("home_admin", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
