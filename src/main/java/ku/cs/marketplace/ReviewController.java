package ku.cs.marketplace;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import ku.cs.models.Product;
import ku.cs.models.Review;
import ku.cs.models.Report;
import ku.cs.models.ReportList;
import ku.cs.services.DataSource;
import ku.cs.services.ProductReportDataSource;
import ku.cs.services.ReviewReportDataSource;
import ku.cs.utility.DialogAlert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public class ReviewController {
    private Review review;
    private String currentlyAccountUsername;
    @FXML private Label nameReviewerLabel;
    @FXML private Label commentLabel;
    @FXML private Label timeLabel;
    @FXML private Button reportButton;
    @FXML private Button star1;
    @FXML private Button star2;
    @FXML private Button star3;
    @FXML private Button star4;
    @FXML private Button star5;

    public void setReview(Review review, String currentlyAccountUsername){
        this.review = review;
        this.currentlyAccountUsername = currentlyAccountUsername;
        nameReviewerLabel.setText(review.getNameReviewer());
        commentLabel.setText(review.getComment());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("วันที่ d MMMM yyyy เวลา HH:mm:ss",new Locale("th","TH"));
        timeLabel.setText(review.getTime().format(timeFormatter));
        showRatingStar(review.getRating());
    }

    private void showRatingStar(int rating) {
        star1.setStyle("-fx-base: #c4c4c4");
        star2.setStyle("-fx-base: #c4c4c4");
        star3.setStyle("-fx-base: #c4c4c4");
        star4.setStyle("-fx-base: #c4c4c4");
        star5.setStyle("-fx-base: #c4c4c4");
        switch (rating) {
            case 5:
                star5.setStyle("-fx-base: #ffea00");
            case 4:
                star4.setStyle("-fx-base: #ffea00");
            case 3:
                star3.setStyle("-fx-base: #ffea00");
            case 2:
                star2.setStyle("-fx-base: #ffea00");
            case 1:
                star1.setStyle("-fx-base: #ffea00");
        }
    }

    public void handleReportReview(javafx.event.ActionEvent actionEvent) {
        if(DialogAlert.showConfirmationDialogAlert("รายงานรีวิว","คุณต้องการรายงานรีวิวนี้ใช่หรือไม่ ?")){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(DialogAlert.class.getResource("/ku/cs/report_dialog.fxml"));
                DialogPane alert = fxmlLoader.load();

                ReportDialogController reportDialogController = fxmlLoader.getController();
                reportDialogController.setChoice();

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(alert);
                dialog.setTitle("รายงานรีวิว");
                Optional<ButtonType> confirmButton = dialog.showAndWait();
                if (confirmButton.isPresent() && confirmButton.get() == ButtonType.OK){
                    if(reportDialogController.getTypeComboBox().getSelectionModel().getSelectedItem() == null){
                        DialogAlert.showErrorDialogAlert("Error","กรุณาเลือกหมวดหมู่ที่ต้องการจะรายงาน");
                        return;
                    }
                    String category = reportDialogController.getTypeComboBox().getSelectionModel().getSelectedItem().toString();
                    String detail = reportDialogController.getDetailTextArea().getText().replace('\n',' ');
                    if (category == null || detail == null){
                        return;
                    }
                    DataSource<ReportList<Review>> reportListDataSource = new ReviewReportDataSource("data","reported_review.csv");
                    ReportList<Review> reportedReviews = reportListDataSource.readData();
                    Report<Review> report = new Report<>(category,detail,this.review,currentlyAccountUsername);
                    reportedReviews.addReport(report);
                    reportListDataSource.writeData(reportedReviews);
                    DialogAlert.showInformationDialogAlert("รายงานรีวิว","รายงานสำเร็จแล้ว!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hiddenButton(){
        reportButton.setVisible(false);
    }
}
