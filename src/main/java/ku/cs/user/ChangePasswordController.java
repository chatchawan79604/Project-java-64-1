package ku.cs.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.AccountList;
import ku.cs.models.User;
import ku.cs.services.AccountDataSource;
import ku.cs.services.DataBox;
import ku.cs.services.DataSource;
import ku.cs.utility.DialogAlert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChangePasswordController {
    private DataBox dataBox;
    @FXML private Circle userImage;
    @FXML private Circle miniImage;
    @FXML private PasswordField oldPasswordPasswordField;
    @FXML private PasswordField newPasswordPasswordField;
    @FXML private PasswordField confirmNewPasswordPasswordField;

    public void initialize(){
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        try {
            miniImage.setFill(new ImagePattern(new Image(new FileInputStream(((User) dataBox.getCurrentlyAccount()).getPathPicture()))));
            userImage.setFill(new ImagePattern(new Image(new FileInputStream(((User) dataBox.getCurrentlyAccount()).getPathPicture()))));
        } catch (FileNotFoundException e) {
            System.err.println("โหลดรูปภาพไม่สำเร็จ");
        }
    }

    @FXML
    void handleChangePassword(ActionEvent event) {
        if(!newPasswordPasswordField.getText().matches("[A-Za-z0-9]*")){
            DialogAlert.showErrorDialogAlert("Error","รหัสผ่าน ให้ใช้ตัวอักษรภาษาอังกฤษพิมพ์เล็ก อักษรภาษาอังกฤษพิมพ์ใหญ่ และตัวเลขเท่านั้น");
        }
        else if (newPasswordPasswordField.getText().length() > 14 ||newPasswordPasswordField.getText().length() < 4){
            DialogAlert.showErrorDialogAlert("Error","รหัสผ่านใหม่ ต้องมีความยาวตั้งแต่ 4 ถึง 14 ตัวอักษร");
        }
        else if((dataBox.getCurrentlyAccount()).changePassword(oldPasswordPasswordField.getText(),newPasswordPasswordField.getText(),confirmNewPasswordPasswordField.getText()) == 0){
            dataBox.getAccountList().replaceAccountData(dataBox.getCurrentlyAccount());
            DataSource<AccountList> accountDataSource = new AccountDataSource("data","account_data.csv");
            accountDataSource.writeData(dataBox.getAccountList());
            oldPasswordPasswordField.clear();
            newPasswordPasswordField.clear();
            confirmNewPasswordPasswordField.clear();
            DialogAlert.showInformationDialogAlert("Alert","เปลี่ยนรหัสผ่านสำเร็จ");
            try {
                com.github.saacsos.FXRouter.goTo("my_profile", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if((dataBox.getCurrentlyAccount()).changePassword(oldPasswordPasswordField.getText(),newPasswordPasswordField.getText(),confirmNewPasswordPasswordField.getText()) == 1){
            DialogAlert.showErrorDialogAlert("Alert","รหัสผ่านใหม่ และยืนยันรหัสผ่านใหม่ไม่เหมือนกัน");
        }else if((dataBox.getCurrentlyAccount()).changePassword(oldPasswordPasswordField.getText(),newPasswordPasswordField.getText(),confirmNewPasswordPasswordField.getText()) == 2){
            DialogAlert.showErrorDialogAlert("Alert","รหัสผ่านเดิมไม่ถูกต้อง");
        }
    }

    @FXML
    void handleBack(ActionEvent event) throws IOException {
        com.github.saacsos.FXRouter.goTo("my_profile", dataBox);
    }

    @FXML
    public void handleShopButton(ActionEvent event) {
        if(((User) dataBox.getCurrentlyAccount()).getShop() == null){
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

    @FXML
    public void handleHistory(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("history_order", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMyProfile(ActionEvent event){
        try {
            com.github.saacsos.FXRouter.goTo("my_profile", dataBox);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

