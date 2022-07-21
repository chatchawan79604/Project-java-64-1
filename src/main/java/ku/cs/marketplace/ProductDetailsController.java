package ku.cs.marketplace;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.models.Report;
import ku.cs.models.ReportList;
import ku.cs.services.*;
import ku.cs.utility.DialogAlert;
import ku.cs.utility.Number;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ProductDetailsController {

    @FXML private ImageView imageProduct;
    @FXML private Label nameProductLabel;
    @FXML private Label costLabel;
    @FXML private Label detailProductLabel;
    @FXML private Label quantityLabel;
    @FXML private Circle userImage;
    @FXML private TextField quantityTextField;
    @FXML private Button nameShop;
    @FXML private Label totalCostLabel;
    @FXML private Label categoryLabel;
    @FXML private VBox vBoxCategory;


    // ส่วนของ review
    @FXML private TextArea reviewTextArea;
    @FXML private VBox vBox;
    @FXML private Button star1;
    @FXML private Button star2;
    @FXML private Button star3;
    @FXML private Button star4;
    @FXML private Button star5;
    @FXML private Label numberOfReviewer;
    @FXML private Button starAvg1;
    @FXML private Button starAvg2;
    @FXML private Button starAvg3;
    @FXML private Button starAvg4;
    @FXML private Button starAvg5;
    @FXML private Label averageRating;
    @FXML private Label avgStar5Label;
    @FXML private Label avgStar4Label;
    @FXML private Label avgStar3Label;
    @FXML private Label avgStar2Label;
    @FXML private Label avgStar1Label;
    @FXML private ProgressBar ratingBarStar5;
    @FXML private ProgressBar ratingBarStar4;
    @FXML private ProgressBar ratingBarStar3;
    @FXML private ProgressBar ratingBarStar2;
    @FXML private ProgressBar ratingBarStar1;

    private int rating;
    private ReviewList reviewList;


    private DataBox dataBox;
    private User user;
    private Product product;

    @FXML
    public void initialize() {
        System.out.println("initialize ProductDetailsController");
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        user = (User) dataBox.getCurrentlyAccount();
        product = dataBox.getSelectedProduct();
        reviewList = product.getReviewList();

        try {
            userImage.setFill(new ImagePattern(new Image(new FileInputStream(user.getPathPicture()))));
        } catch (FileNotFoundException e) {
            System.err.println("อ่านไฟล์รูปภาพไม่ถูกต้อง");
        }
        quantityTextField.setText("1");
        totalCostLabel.setText(String.format("%.2f บาท", product.getCost()));
        showProductDetail();

        // review
        ratingBarStar1.setStyle("-fx-accent : #ffea00;");
        ratingBarStar2.setStyle("-fx-accent : #ffea00;");
        ratingBarStar3.setStyle("-fx-accent : #ffea00;");
        ratingBarStar4.setStyle("-fx-accent : #ffea00;");
        ratingBarStar5.setStyle("-fx-accent : #ffea00;");
        showReview();
        showRatingStar(rating);
        showRatingAverageStar((int)reviewList.averageRating());
        averageRating.setText(String.format("%.1f", reviewList.averageRating()));
        numberOfReviewer.setText(String.valueOf( " Review " + "(" + reviewList.getSize()) + ")" );

    }

    public void showProductDetail(){
        try {
            imageProduct.setImage(new Image(new FileInputStream(product.getPicturePath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        nameProductLabel.setText(product.getName());
        costLabel.setText(String.format("%.2f", product.getCost()));
        detailProductLabel.setText(product.getDetail());
        quantityLabel.setText(String.valueOf(product.getQuantity()));
        nameShop.setText(product.getShop());
        categoryLabel.setText(product.getProductCategory().getCategory());

        // show category
        ArrayList<String> arrayListAttribute = dataBox.getCategory().get(product.getProductCategory().getCategory());
        for(int i=0; i<product.getProductCategory().getSizeAttributeProducts(); i++){
            String attribute = arrayListAttribute.get(i);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ku/cs/category_detail_in_product_detail.fxml"));

            try {
                HBox hBox = fxmlLoader.load();
                CategoryDetailController categoryDetailController = fxmlLoader.getController();
                categoryDetailController.setDataCategoryDetail(attribute, product.getProductCategory().getAttributeProductListByIndex(i).toString());
                vBoxCategory.getChildren().add(hBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Menu Bar
    @FXML
    public void handleBackToShopButton(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("marketplace");
        } catch (IOException e) {
            e.printStackTrace();
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

    @FXML
    public void handleMyProfile(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("my_profile", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    // -------------------------------------------------------------------------

    // แถบสั่งซื้อ
    // ปุ่มลดจำนวนสินค้าที่ต้องการซื้อ
    @FXML
    public void handleMinusButton(ActionEvent event) {
        if(quantityTextField.getText().isEmpty() || !Number.checkIntegerNumber(quantityTextField.getText())){
            quantityTextField.setText("1");
        }
        int quantity = Integer.parseInt(quantityTextField.getText());
        quantity --;
        if(quantity <= 0){
            quantity = 1;
        }
        quantityTextField.setText(String.format("%d", quantity));
        totalCostLabel.setText(String.format("%.2f บาท",quantity*product.getCost()));
    }
    // ปุ่มเพิ่มจำนวนสินค้าที่ต้องการซื้อ
    @FXML
    public void handlePlusButton(ActionEvent event) {
        if(quantityTextField.getText().isEmpty() || !Number.checkIntegerNumber(quantityTextField.getText())){
            quantityTextField.setText("1");
        }
        int quantity = Integer.parseInt(quantityTextField.getText());
        if(quantity +1 <= product.getQuantity() ){
            quantity ++;
        }
        else {
            DialogAlert.showErrorDialogAlert("Alert","จำนวนสินค้าที่เพิ่มมากว่าจำนวนสินค้าที่มี");
        }
        quantityTextField.setText(String.format("%d", quantity));
        totalCostLabel.setText(String.valueOf(String.format("%.2f บาท",quantity*product.getCost())));
    }
    // ในช่องกรอกจำนวนสินค้าที่ต้องการซื้อ OnAction Enter ได้
    @FXML
    public void handleQuantityTextField(ActionEvent event){
        if(quantityTextField.getText().isEmpty() || !Number.checkIntegerNumber(quantityTextField.getText())){
            quantityTextField.setText("1");
            return;
        }
        int quantity = Integer.parseInt(quantityTextField.getText());
        if(quantity > product.getQuantity() ){
            DialogAlert.showErrorDialogAlert("Alert","จำนวนสินค้าที่เพิ่มมากว่าจำนวนสินค้าที่มี");
            quantity = product.getQuantity();
        }
        else if(quantity <= 0){
            DialogAlert.showErrorDialogAlert("Alert","จำนวนสินค้าไม่ถูกต้อง");
            quantity = 1;
        }
        quantityTextField.setText(String.format("%d", quantity));
        totalCostLabel.setText(String.format("%.2f บาท",quantity*product.getCost()));
    }
    // กดซื้อสินค้า
    @FXML
    public void handlePlaceAnOrderButton(ActionEvent event) {
        int quantity;
        if(quantityTextField.getText().isEmpty() || !Number.checkIntegerNumber(quantityTextField.getText())){
            DialogAlert.showErrorDialogAlert("Alert","จำนวนสินค้าไม่ถูกต้อง");
            quantityTextField.setText("1");
            return;
        }
        quantity = Integer.parseInt(quantityTextField.getText());
        if(quantity <= 0){
            DialogAlert.showErrorDialogAlert("Alert","จำนวนสินค้าไม่ถูกต้อง");
            quantity = 1;
        }
        else if(quantity > product.getQuantity() ){
            DialogAlert.showErrorDialogAlert("Alert","จำนวนสินค้าที่เพิ่มมากว่าจำนวนสินค้าที่มี");
            quantity = product.getQuantity();
        }
        else if(quantity >= 1 && quantity <= product.getQuantity()) {
            if (DialogAlert.showConfirmationDialogAlert("ยืนยันคำสั่งซื้อ", "คุณต้องการซื้อง\t: " + product.getName()+"\nจำนวน\t\t: "
                    + quantity + "\nเป็นเงิน\t\t: " + String.format("%.2f",product.getCost() * quantity) + " บาท\nใช่หรือไม่ ?")) {
                dataBox.setCurrentlyShop(dataBox.getShopList().getShop(product.getShop()));
                dataBox.getCurrentlyShop().setProducts(dataBox.getProductList().getProductsInShop(dataBox.getCurrentlyShop().getName()));
                ((User) dataBox.getCurrentlyAccount()).buyProduct(dataBox.getCurrentlyShop(), dataBox.getSelectedProduct(), quantity);
                DataSource<ProductList> productsDataSource = new ProductsDataSource();
                productsDataSource.writeData(dataBox.getProductList());
                dataBox.updateOrderList();
                dataBox.setCurrentlyShop(null);
                try {
                    com.github.saacsos.FXRouter.goTo("history_order", dataBox);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        quantityTextField.setText(String.format("%d", quantity));
    }
    // -------------------------------------------------------------------------

    // ปุ่มไปหน้าร้านค้า ข้อ 17.5
    @FXML
    public void handleNameShopButton(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("store_page", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // -------------------------------------------------------------------------

    // ส่วนของ review
    // ปุ่มรีวิว
    @FXML
    public void reviewButton(ActionEvent event){
        if(rating == 0){
            DialogAlert.showErrorDialogAlert("Error","กรุณาเลือกระดับความพอใจ/ดาว!");
            return;
        }
        if(reviewList.usernameReviewerIsInList(user.getUsername())){
            DialogAlert.showErrorDialogAlert("Error","ส่งได้เพียงครั้งเดียวเท่านั้น!");
            return;
        }
        if(reviewTextArea.getLength() > 60){
            DialogAlert.showErrorDialogAlert("Error","ความยาวของรีวิวต้องไม่เกิน 60 ตัวอักษร!");
            return;
        }
        String comment = reviewTextArea.getText().replace('\n',' ');
        Review review = new Review(rating, comment, user.getAccountName(), user.getUsername());

        reviewList.addReviewList(review);
        reviewList.sortLastReviewList();
        ReviewDataSource reviewDataSource = new ReviewDataSource("data" + File.separator + "review", String.valueOf(product.getSerialnumber()) + ".csv");
        reviewDataSource.addData(review);

        reviewTextArea.setText("");
        rating = 0;
        showRatingStar(rating);
        showReview();
        showRatingAverageStar((int)reviewList.averageRating());
        averageRating.setText(String.format("%.1f", reviewList.averageRating()));
    }

    // ปุ่มยกเลิกรีวิว
    @FXML
    public void cancelButton(ActionEvent event){
        reviewTextArea.setText("");
        rating = 0;
        showRatingStar(rating);
    }

    public void showReview(){
        numberOfReviewer.setText(String.valueOf( " Review " + "(" + reviewList.getSize()) + ")" );
        vBox.getChildren().removeAll(vBox.getChildren());

        for(int i=0; i<reviewList.getSize(); i++){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ku/cs/review.fxml"));

            try {
                AnchorPane anchorPane = fxmlLoader.load();
                ReviewController reviewController = fxmlLoader.getController();
                reviewController.setReview(reviewList.getByIndex(i),dataBox.getCurrentlyAccount().getUsername());
                vBox.getChildren().add(anchorPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void starButton1(ActionEvent event) {
        rating = 1;
        showRatingStar(rating);
    }
    @FXML
    void starButton2(ActionEvent event) {
        rating = 2;
        showRatingStar(rating);
    }
    @FXML
    void starButton3(ActionEvent event) {
        rating = 3;
        showRatingStar(rating);
    }
    @FXML
    void starButton4(ActionEvent event) {
        rating = 4;
        showRatingStar(rating);
    }
    @FXML
    void starButton5(ActionEvent event) {
        rating = 5;
        showRatingStar(rating);
    }

    private void showRatingStar(int rating){
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

    private void showRatingAverageStar(int rating){
        avgStar1Label.setText(String.valueOf(reviewList.getNumStar(1)));
        avgStar2Label.setText(String.valueOf(reviewList.getNumStar(2)));
        avgStar3Label.setText(String.valueOf(reviewList.getNumStar(3)));
        avgStar4Label.setText(String.valueOf(reviewList.getNumStar(4)));
        avgStar5Label.setText(String.valueOf(reviewList.getNumStar(5)));

        ratingBarStar1.setProgress(reviewList.getAverageOfEachStar(1));
        ratingBarStar2.setProgress(reviewList.getAverageOfEachStar(2));
        ratingBarStar3.setProgress(reviewList.getAverageOfEachStar(3));
        ratingBarStar4.setProgress(reviewList.getAverageOfEachStar(4));
        ratingBarStar5.setProgress(reviewList.getAverageOfEachStar(5));

        starAvg1.setStyle("-fx-base: #c4c4c4");
        starAvg2.setStyle("-fx-base: #c4c4c4");
        starAvg3.setStyle("-fx-base: #c4c4c4");
        starAvg4.setStyle("-fx-base: #c4c4c4");
        starAvg5.setStyle("-fx-base: #c4c4c4");
        switch (rating) {
            case 5:
                starAvg5.setStyle("-fx-base: #ffea00");
            case 4:
                starAvg4.setStyle("-fx-base: #ffea00");
            case 3:
                starAvg3.setStyle("-fx-base: #ffea00");
            case 2:
                starAvg2.setStyle("-fx-base: #ffea00");
            case 1:
                starAvg1.setStyle("-fx-base: #ffea00");
        }
    }

    // -------------------------------------------------------------------------
    public void handleReportProduct(){
        if(DialogAlert.showConfirmationDialogAlert("รายงานสินค้า","คุณต้องการรายงานสินค้านี้ใช่หรือไม่ ?")){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(DialogAlert.class.getResource("/ku/cs/report_dialog.fxml"));
                DialogPane alert = fxmlLoader.load();

                ReportDialogController reportDialogController = fxmlLoader.getController();
                reportDialogController.setChoice();

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(alert);
                dialog.setTitle("รายงานสินค้า");
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
                    DataSource<ReportList<Product>> reportListDataSource = new ProductReportDataSource("data","reported_product.csv");
                    Report<Product> report = new Report<>(category,detail,this.product,dataBox.getCurrentlyAccount().getUsername());
                    dataBox.getProductReports().addReport(report);
                    reportListDataSource.writeData(dataBox.getProductReports());
                    DialogAlert.showInformationDialogAlert("รายงานรีวิว","รายงานสำเร็จแล้ว!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
