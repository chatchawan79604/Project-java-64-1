package ku.cs.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.Product;
import ku.cs.models.Report;
import ku.cs.services.DataBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReportedProductController {
    private DataBox dataBox;
    private Report<Product> report;
    @FXML private Label categoryLabel;
    @FXML private Label detailLabel;
    @FXML private Label timeLabel;
    @FXML private Label reporterUsernameLabel;

    public void setReport(Report<Product> report, DataBox dataBox){
        this.dataBox = dataBox;
        this.report = report;
        categoryLabel.setText(report.getCategory());
        detailLabel.setText(report.getDetail());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("วันที่ d MMMM yyyy เวลา HH:mm:ss",new Locale("th","TH"));
        timeLabel.setText(report.getTime().format(timeFormatter));
        reporterUsernameLabel.setText(report.getReporter());
    }

    @FXML
    void handleCheckDetail(ActionEvent event) {
        try {
            dataBox.setProductReport(report);
            com.github.saacsos.FXRouter.goTo("reported_product_detail",dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
