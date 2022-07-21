package ku.cs.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import ku.cs.models.AccountList;
import ku.cs.services.AccountDataSource;
import ku.cs.services.DataSource;
import ku.cs.utility.DialogAlert;
import ku.cs.services.PictureManager;
import ku.cs.models.User;
import ku.cs.services.DataBox;

import java.io.*;

public class MyProfileController {
    @FXML private Label accountNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label shopNameLabel;
    @FXML private Circle miniImage;
    @FXML private Circle userImage;
    private DataBox dataBox;

    public void initialize(){
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        try {
            miniImage.setFill(new ImagePattern(new Image(new FileInputStream(((User) dataBox.getCurrentlyAccount()).getPathPicture()))));
            userImage.setFill(new ImagePattern(new Image(new FileInputStream(((User) dataBox.getCurrentlyAccount()).getPathPicture()))));
        } catch (FileNotFoundException e) {
            System.err.println("โหลดรูปภาพไม่สำเร็จ");
        }
        accountNameLabel.setText((dataBox.getCurrentlyAccount()).getAccountName());
        usernameLabel.setText((dataBox.getCurrentlyAccount()).getUsername());
        if(((User) dataBox.getCurrentlyAccount()).getShop() == null){
            shopNameLabel.setText("ไม่มีร้านค้า");
        }
        else{shopNameLabel.setText(((User) dataBox.getCurrentlyAccount()).getShop().getName());}
    }

    @FXML
    public void handleChangePassword(ActionEvent event) throws IOException {
        com.github.saacsos.FXRouter.goTo("change_password", dataBox);
    }

    @FXML
    public void handleChangeProfilePicture(ActionEvent event) {
        PictureManager pictureManager = new PictureManager();
        FileChooser fileChooser = new FileChooser();
        File fileImage = null;
        fileImage = fileChooser.showOpenDialog(null);
        String imagePath = fileImage.getPath().replace("\\","/");
        if(pictureManager.checkImageType(imagePath)){
            ((User) dataBox.getCurrentlyAccount()).changeProfileImage(imagePath);
            try {
                miniImage.setFill(new ImagePattern(new Image(new FileInputStream(((User) dataBox.getCurrentlyAccount()).getPathPicture()))));
                userImage.setFill(new ImagePattern(new Image(new FileInputStream(((User) dataBox.getCurrentlyAccount()).getPathPicture()))));
            } catch (FileNotFoundException e) {
                System.err.println("โหลดรูปภาพไม่สำเร็จ");
            }
            DataSource<AccountList> accountDataSource = new AccountDataSource("data","account_data.csv");
            dataBox.getAccountList().replaceAccountData(dataBox.getCurrentlyAccount());
            accountDataSource.writeData(dataBox.getAccountList());
            return;
        }
        DialogAlert.showErrorDialogAlert("Alert","ไม่สามารถเปลี่ยนเป็นรูปภาพดังกล่าวได้");
        return;
    }

    @FXML
    public void handleLogout(ActionEvent event) throws IOException{
        dataBox.setCurrentlyAccount(null);
        com.github.saacsos.FXRouter.goTo("login", dataBox);
    }

    @FXML
    public void handleMainPageButton(ActionEvent event){
        try {
            com.github.saacsos.FXRouter.goTo("marketplace", dataBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleShop(ActionEvent event) {
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

}
