package ku.cs.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import ku.cs.utility.DialogAlert;
import ku.cs.services.PictureManager;
import ku.cs.services.DataBox;

import java.io.*;

public class SignUpController {
    private DataBox dataBox;
    private String picturePath;
    private PictureManager pictureManager;
    @FXML private TextField accountNameTextField;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordPasswordField;
    @FXML private PasswordField confirmPasswordPasswordField;
    @FXML private ImageView profilePictureImageView;
    @FXML private ImageView miniLogoImageView;

    @FXML
    public void initialize() {
        System.out.println("initialize sign up page");
        pictureManager = new PictureManager();
        picturePath = "data/images/default/default_profile_picture.png";
        try {
            profilePictureImageView.setImage(new Image(new FileInputStream(picturePath),200,200,false,false));
            miniLogoImageView.setImage(new Image(new FileInputStream("data/images/logo/MiniLogo.png")));
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
    }

    @FXML
    public void handleBackToLogin(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("login", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUploadImageButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File fileImage = null;
        fileImage = fileChooser.showOpenDialog(null);
        picturePath = fileImage.getPath().replace("\\","/");
        try {
            InputStream inputStream = new FileInputStream(picturePath);
            profilePictureImageView.setImage(new Image(inputStream,200,200,false,false));
            picturePath = pictureManager.importProfileImage(picturePath, "user_profile_images");
            if(picturePath == null){
                picturePath = "data/images/default/default_profile_picture.png";
            }
        } catch (FileNotFoundException e) {
            DialogAlert.showErrorDialogAlert("Error","Alert","ไม่สามารถอัพโหลดรูปภาพดังกล่าวได้");
        }
    }

    @FXML
    public void handleSubmit(ActionEvent actionEvent){
        if(!accountNameTextField.getText().equals("") && !usernameTextField.getText().equals("") && !passwordPasswordField.getText().equals("") && !confirmPasswordPasswordField.getText().equals("")) {
            if(!accountNameTextField.getText().matches("[A-Za-z0-9 ]*")){
                DialogAlert.showErrorDialogAlert("Error","Account Name ให้ใช้ตัวอักษรภาษาอังกฤษพิมพ์เล็ก อักษรภาษาอังกฤษพิมพ์ใหญ่ เว้นวรรคเท่านั้น");
                return;
            }
            if(!usernameTextField.getText().matches("[A-Za-z0-9]*") || !passwordPasswordField.getText().matches("[A-Za-z0-9]*") || !confirmPasswordPasswordField.getText().matches("[A-Za-z0-9]*")){
                DialogAlert.showErrorDialogAlert("Error","Username และรหัสผ่าน ให้ใช้ตัวอักษรภาษาอังกฤษพิมพ์เล็ก อักษรภาษาอังกฤษพิมพ์ใหญ่ และตัวเลขเท่านั้น");
                return;
            }
            else if (accountNameTextField.getText().length() > 20 ||accountNameTextField.getText().length() < 4){
                DialogAlert.showErrorDialogAlert("Error","Account Name ต้องมีความยาวตั้งแต่ 4 ถึง 20 ตัวอักษร");
            }
            else if (usernameTextField.getText().length() > 16 ||usernameTextField.getText().length() < 4){
                DialogAlert.showErrorDialogAlert("Error","Username ต้องมีความยาวตั้งแต่ 4 ถึง 16 ตัวอักษร");
            }
            else if (passwordPasswordField.getText().length() > 14 ||passwordPasswordField.getText().length() < 4){
                DialogAlert.showErrorDialogAlert("Error","รหัสผ่าน ต้องมีความยาวตั้งแต่ 4 ถึง 14 ตัวอักษร");
            }

            else if (passwordPasswordField.getText().equals(confirmPasswordPasswordField.getText())) {
                try {
                    dataBox.getAccountList().createAccount(usernameTextField.getText(), passwordPasswordField.getText(), accountNameTextField.getText(), picturePath);
                    DialogAlert.showInformationDialogAlert("Alert","สมัครสำเร็จ!");
                    System.out.println(dataBox.getAccountList().getAccountByUsername(usernameTextField.getText()));
                    dataBox.setCurrentlyAccount(dataBox.getAccountList().login(usernameTextField.getText(),passwordPasswordField.getText()));
                    try {
                        com.github.saacsos.FXRouter.goTo("marketplace", dataBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (RuntimeException e){
                    DialogAlert.showErrorDialogAlert("Error","Username ดังกล่างถูกใช้ไปแล้ว");
                }
            } else DialogAlert.showErrorDialogAlert("Error","กรุณาใส่ Password และ Confirm Password ให้ตรงกัน");
        }
        else DialogAlert.showErrorDialogAlert("Error","กรุณาใส่ข้อมูลให้ครบทุกช่อง");
    }

    @FXML
    public void handleGoToAboutUs(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("about_us", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
