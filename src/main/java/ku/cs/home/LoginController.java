package ku.cs.home;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ku.cs.models.AccountList;
import ku.cs.models.Admin;
import ku.cs.models.BlacklistAccountList;
import ku.cs.models.User;
import ku.cs.services.AccountDataSource;
import ku.cs.services.BlacklistDataSource;
import ku.cs.services.DataBox;
import ku.cs.services.DataSource;
import ku.cs.utility.DialogAlert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.SystemColor.window;

public class LoginController {
    private DataBox dataBox;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Button exitButton;
    @FXML private ImageView miniLogoImageView;
    @FXML private ImageView logoImageView;
    @FXML private ComboBox categoryComboBox;

    private ArrayList<String> theme = new ArrayList<String>();

    @FXML
    public void initialize() {
        System.out.println("initialize sign login page");
        try {
            miniLogoImageView.setImage(new Image(new FileInputStream("data/images/logo/MiniLogo.png")));
            logoImageView.setImage(new Image(new FileInputStream("data/images/logo/LoGo00.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        if(dataBox == null){
            dataBox = new DataBox();
        }
        theme.add("Normal");
        theme.add("Halloween");
        theme.add("Pastel");
        categoryComboBox.setItems(FXCollections.observableList(theme));
    }

    @FXML
    public void handleLogin() {
        try{
            dataBox.setCurrentlyAccount(dataBox.getAccountList().login(usernameTextField.getText(),passwordTextField.getText()));
            if (dataBox.getCurrentlyAccount() != null && dataBox.getBlacklistAccountList().getBlacklistAccountByUsername(dataBox.getCurrentlyAccount().getUsername()) != null){
                dataBox.getBlacklistAccountList().getBlacklistAccountByUsername(dataBox.getCurrentlyAccount().getUsername()).attemptToLogin();
                DataSource<BlacklistAccountList> blacklistAccountListDataSource = new BlacklistDataSource();
                blacklistAccountListDataSource.writeData(dataBox.getBlacklistAccountList());
                throw new RuntimeException();
            }
        } catch (RuntimeException e){
            DialogAlert.showErrorDialogAlert("Alert","บัญชีดังกล่าวถูกระงับการใช้งานแล้ว");
            return;
        }
        if(dataBox.getCurrentlyAccount() instanceof Admin){
            //เปลี่ยนหน้าไปที่ AdminPanel
            try {
                com.github.saacsos.FXRouter.goTo("home_admin", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(dataBox.getCurrentlyAccount() instanceof User){
            //set time in user
            dataBox.getAccountList().replaceAccountData(dataBox.getCurrentlyAccount());
            DataSource<AccountList> accountDataSource = new AccountDataSource("data","account_data.csv");
            accountDataSource.writeData(dataBox.getAccountList());

            try {
                com.github.saacsos.FXRouter.goTo("marketplace", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            usernameTextField.clear();
            passwordTextField.clear();
            DialogAlert.showErrorDialogAlert("Alert","กรุณาตรวจสอบ Username และ Password ให้ถูกต้อง");
        }
    }

    @FXML
    public void handleGoToSignUp(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("signup", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // เลือกหมวดหมู่ธีม
    @FXML
    void selectCategoryComboBox(ActionEvent event) {
        String categorySelect = categoryComboBox.getSelectionModel().getSelectedItem().toString();
        if(categorySelect.equals("Normal")){
            try{
                com.github.saacsos.FXRouter.setPathCss("/ku/cs/style/normal.css");
                com.github.saacsos.FXRouter.goTo("login", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(categorySelect.equals("Halloween")){
            try{
                com.github.saacsos.FXRouter.setPathCss("/ku/cs/style/halloween.css");
                com.github.saacsos.FXRouter.goTo("login", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(categorySelect.equals("Pastel")){
            try{
                com.github.saacsos.FXRouter.setPathCss("/ku/cs/style/pastel.css");
                com.github.saacsos.FXRouter.goTo("login", dataBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleGoToAboutUs(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("about_us", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCloseProgram(ActionEvent actionEvent){
        com.github.saacsos.FXRouter.closeStage();
    }
    @FXML
    void handleGoToHowToUse(ActionEvent event) {
        try {
            com.github.saacsos.FXRouter.goTo("how_to_use", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
