package ku.cs.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ku.cs.models.Account;
import ku.cs.models.BlacklistAccount;
import ku.cs.models.User;
import ku.cs.models.AccountList;
import ku.cs.services.AccountDataSource;
import ku.cs.services.CategoryDataSource;
import ku.cs.services.DataBox;
import ku.cs.services.DataSource;
import ku.cs.utility.DialogAlert;

import java.io.IOException;
import java.util.ArrayList;

public class HomeAdminController {

    @FXML private Label accountNameLabel;
    @FXML private GridPane accountGrid;
    @FXML private GridPane reportedProductGrid;
    @FXML private GridPane reportedReviewGrid;
    @FXML private GridPane blacklistGrid;
    @FXML private Tab logTimeTab;
    @FXML private Tab changePasswordTab;
    @FXML private Tab ReportedProductPanelTab;
    @FXML private Tab ReportedReviewPanelTab;
    @FXML private Tab BlacklistAccountTab;
    @FXML private TabPane tabPane;
    private DataBox dataBox;


    public void initialize(){
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        dataBox.updateReportedReviewList();
        accountNameLabel.setText((dataBox.getCurrentlyAccount()).getAccountName());
        AccountList accountList = dataBox.getAccountList();
        accountList.sortLastFirstAccountList();
        showUser(accountList.getAccountArrayList());
    }

    @FXML
    public void handleLogout(ActionEvent event) throws IOException{
        dataBox.setCurrentlyAccount(null);
        com.github.saacsos.FXRouter.goTo("login", dataBox);
    }

    public void showUser(ArrayList<Account> userList){
        accountGrid.getColumnConstraints().clear();
        accountGrid.getRowConstraints().clear();
        accountGrid.getChildren().clear();

        int row = 1, column = 0;

        try {
            for(Account account : userList){
                if(!(account.getAccountType().equalsIgnoreCase("admin"))){
                    User user = (User)account;
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/ku/cs/user_in_home_admin.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    UserInHomeAdmin userInHomeAdmin = fxmlLoader.getController();
                    userInHomeAdmin.setUser(user);

                    if(column == 1){
                        column = 0;
                        row++;
                    }
                    //เพิ่มสินค้าลงตาราง grid
                    accountGrid.add(anchorPane, column++, row);
                    //ระยะห่างระหว่าง anchorPane
                    GridPane.setMargin(anchorPane, new Insets(10));
                }

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handleAdminHome(ActionEvent actionEvent) {
        tabPane.getSelectionModel().select(logTimeTab);
    }

    // change password
    @FXML private TextField password;
    @FXML private TextField newPassword;
    @FXML private TextField confirmNewPassword;

    @FXML
    public void handleChangePassword(ActionEvent event) {
        tabPane.getSelectionModel().select(changePasswordTab);
    }

    @FXML
    void setPassword(ActionEvent event) {
        if(!newPassword.getText().matches("[A-Za-z0-9]*")){
            DialogAlert.showErrorDialogAlert("Error","รหัสผ่านให้ใช้ตัวอักษรภาษาอังกฤษพิมพ์เล็ก อักษรภาษาอังกฤษพิมพ์ใหญ่ และตัวเลขเท่านั้น");
        }
        else if (newPassword.getText().length() > 14 ||newPassword.getText().length() < 4){
            DialogAlert.showErrorDialogAlert("Error","รหัสผ่านใหม่ ต้องมีความยาวตั้งแต่ 4 ถึง 14 ตัวอักษร");
        }
        else if(dataBox.getCurrentlyAccount().changePassword(password.getText(),newPassword.getText(), confirmNewPassword.getText()) == 0){
            dataBox.getAccountList().replaceAccountData(dataBox.getCurrentlyAccount());
            DataSource<AccountList> accountDataSource = new AccountDataSource("data","account_data.csv");
            accountDataSource.writeData(dataBox.getAccountList());
            password.clear();
            newPassword.clear();
            confirmNewPassword.clear();
            DialogAlert.showInformationDialogAlert("Alert","เปลี่ยนรหัสผ่านสำเร็จ");
        }
        else if(dataBox.getCurrentlyAccount().changePassword(password.getText(),newPassword.getText(), confirmNewPassword.getText()) == 1){
            DialogAlert.showErrorDialogAlert("Alert","รหัสผ่านใหม่ และยืนยันรหัสผ่านใหม่ไม่เหมือนกัน");
        }
        else if(dataBox.getCurrentlyAccount().changePassword(password.getText(),newPassword.getText(), confirmNewPassword.getText()) == 2){
            DialogAlert.showErrorDialogAlert("Alert","รหัสผ่านเดิมไม่ถูกต้อง");
        }
    }

    //----------------------------------------------------------

    // createCategoryTab
    @FXML private Tab createCategoryTab;
    @FXML private CheckBox checkBoxModel;
    @FXML private CheckBox checkBoxCapacity;
    @FXML private CheckBox checkBoxColor;
    @FXML private CheckBox checkBoxHeight;
    @FXML private CheckBox checkBoxLength;
    @FXML private CheckBox checkBoxWidth;
    @FXML private CheckBox checkBoxDiameter;
    @FXML private CheckBox checkBoxSize;
    @FXML private CheckBox checkBoxWeight;
    @FXML private TextField categoryTextField;
    @FXML private ListView<String> categoryListView;

    private ArrayList<String> arrayList ;

    private void setDefaultButton(){
        arrayList = new ArrayList<>();
        categoryTextField.setText("");
        checkBoxModel.setSelected(false);
        checkBoxCapacity.setSelected(false);
        checkBoxColor.setSelected(false);
        checkBoxHeight.setSelected(false);
        checkBoxLength.setSelected(false);
        checkBoxWidth.setSelected(false);
        checkBoxDiameter.setSelected(false);
        checkBoxSize.setSelected(false);
        checkBoxWeight.setSelected(false);
        categoryListView.getItems().clear();
        categoryListView.refresh();
        for(String key:dataBox.getCategory().keySet()){
            categoryListView.getItems().add(key + " " + dataBox.getCategory().get(key).toString());
        }
        categoryListView.refresh();
    }

    @FXML
    void handleCreateCategory(ActionEvent event){
        tabPane.getSelectionModel().select(createCategoryTab);
        setDefaultButton();
    }

    @FXML
    void handleAddCategoryButton(ActionEvent event) {
        if(checkBoxModel.isSelected()) arrayList.add("ชื่อรุ่น");
        if(checkBoxCapacity.isSelected()) arrayList.add("ความจุ");
        if(checkBoxColor.isSelected()) arrayList.add("สี");
        if(checkBoxHeight.isSelected()) arrayList.add("ความสูง(ซม.)");
        if(checkBoxLength.isSelected()) arrayList.add("ความยาว(ซม.)");
        if(checkBoxWidth.isSelected()) arrayList.add("ความกว้าง(ซม.)");
        if(checkBoxDiameter.isSelected()) arrayList.add("เส้นผ่านศูนย์กลาง(ซม.)");
        if(checkBoxSize.isSelected()) arrayList.add("ไซต์");
        if(checkBoxWeight.isSelected()) arrayList.add("น้ำหนัก(กรัม)");

        if(arrayList.size() <= 0) {
            DialogAlert.showErrorDialogAlert("Alert","กรุณาเลือกคุณลักษณะของหมวดหมู่สินค้า");
            arrayList.clear();
            return;
        }
        if(categoryTextField.getText().isEmpty()) {
            DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่ชื่อหมวดหมู่สินค้า");
            arrayList.clear();
            return;
        }
        if(!categoryTextField.getText().matches("[ก-ฮะ-์]*")){
            DialogAlert.showErrorDialogAlert("Alert","ชื่อ Category ต้องเป็นตัวอักษร สระ และวรรณยุกต์ภาษาไทยเท่านั้น");
            arrayList.clear();
            return;
        }
        if( (dataBox.getCategory().keySet()).contains(categoryTextField.getText()) ){
            DialogAlert.showErrorDialogAlert("Alert","หมวดหมู่นี้มีอยู่แล้ว กรุณาใส่ชื่อหมวดหมู่ใหม่");
            arrayList.clear();
            return;
        }
        CategoryDataSource categoryDataSource = new CategoryDataSource();
        categoryDataSource.addData(categoryTextField.getText(),arrayList);
        dataBox.getCategory().put(categoryTextField.getText(), arrayList);
        setDefaultButton();
        arrayList.clear();
        DialogAlert.showInformationDialogAlert("Succeed","เพิ่มหมวดหมู่สำเร็จ");
    }

    //----------------------------------------------------------

    //Report of Product Panel
    public void handleReportedProductPanel(ActionEvent event){
        tabPane.getSelectionModel().select(ReportedProductPanelTab);
        showReportedProduct();
    }

    public void showReportedProduct(){
        reportedProductGrid.getColumnConstraints().clear();
        reportedProductGrid.getRowConstraints().clear();
        reportedProductGrid.getChildren().clear();

        int row = 1, column = 0;

        try {
            for(int index = 0;index < dataBox.getProductReports().getSize();index++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/reported_product.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ReportedProductController reportedProductController = fxmlLoader.getController();
                reportedProductController.setReport(dataBox.getProductReports().getReportsOfIndex(index), dataBox);
                reportedProductGrid.add(anchorPane, column, row++);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //================================

    //Report of Review Panel
    public void handleReportedReviewPanel(ActionEvent actionEvent) {
        tabPane.getSelectionModel().select(ReportedReviewPanelTab);
        showReportedReview();
    }

    public void showReportedReview(){
        reportedReviewGrid.getColumnConstraints().clear();
        reportedReviewGrid.getRowConstraints().clear();
        reportedReviewGrid.getChildren().clear();

        int row = 1, column = 0;

        try {
            for(int index = 0;index < dataBox.getReviewReports().getSize();index++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/reported_review.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ReportedReviewController reportedReviewController = fxmlLoader.getController();
                reportedReviewController.setReport(dataBox.getReviewReports().getReportsOfIndex(index),this.dataBox);

                reportedReviewGrid.add(anchorPane, column, row++);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    //================================

    //Report of Review Panel
    public void handleCheckBlacklist(ActionEvent event){
        tabPane.getSelectionModel().select(BlacklistAccountTab);
        showBlacklistAccount();
    }

    public void showBlacklistAccount(){
        blacklistGrid.getColumnConstraints().clear();
        blacklistGrid.getRowConstraints().clear();
        blacklistGrid.getChildren().clear();

        int row = 1, column = 0;
        //แสดงสินค้าทั้งหมดโดยที่สินค้าล่าสุดขึ้นก่อน

        try {
            for(int index = 0 ; index < dataBox.getBlacklistAccountList().getSizeOfBlacklistAccount() ; index++){
                BlacklistAccount blacklistAccount = dataBox.getBlacklistAccountList().getBlacklistAccountOfIndex(index);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/admin_blacklist_account.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                AdminBlacklistAccountController adminBlacklistAccountController = fxmlLoader.getController();
                adminBlacklistAccountController.setBlacklistAccount(blacklistAccount,dataBox);

                //เพิ่มสินค้าลงตาราง grid
                blacklistGrid.add(anchorPane, 0, row++);
                //ระยะห่างระหว่าง anchorPane
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
