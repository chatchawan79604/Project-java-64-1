package ku.cs.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.AccountDataSource;
import ku.cs.services.DataBox;
import ku.cs.services.DataSource;
import ku.cs.services.ShopDataSource;
import ku.cs.utility.DialogAlert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CreateMyShopController {

    @FXML private Label resultLabelShop;
    @FXML private TextField myNameShopField;
    @FXML private Circle miniImage;
    private DataBox dataBox;

    public void initialize() throws FileNotFoundException {
        dataBox = (DataBox) com.github.saacsos.FXRouter.getData();
        try {
            miniImage.setFill(new ImagePattern(new Image(new FileInputStream(((User) dataBox.getCurrentlyAccount()).getPathPicture()))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        resultLabelShop.setText("");
    }

    @FXML
    public void handleCreateShop() throws IOException {

        if((myNameShopField.getText()).equals("")){
            myNameShopField.clear();
            DialogAlert.showErrorDialogAlert("Alert","กรุณาใส่ชื่อร้านค้า");
        }
        else if(!myNameShopField.getText().matches("[ก-ฮA-Za-zะ-์ ]*")){
            DialogAlert.showErrorDialogAlert("Alert","ชื่อร้านต้องประกอบไปด้วย ก-ฮ, A-Z, a-z สระภาษาไทย วรรณยุกต์ภาษาไทย และเว้นวรรคเท่านั้น");
        }
        else if(dataBox.getShopList().isExist(myNameShopField.getText())){
            DialogAlert.showErrorDialogAlert("Alert","ชื่อร้านดังกล่าวมีอยู่ในระบบแล้ว กรุณาตั้งชื่อร้านใหม่");
        }else{
            ((User) dataBox.getCurrentlyAccount()).createShop(myNameShopField.getText());
            dataBox.getAccountList().replaceAccountData(dataBox.getCurrentlyAccount());
            DataSource<AccountList> accountDataSource = new AccountDataSource("data","account_data.csv");
            accountDataSource.writeData(dataBox.getAccountList());
            dataBox.getShopList().addShop(new Shop(myNameShopField.getText()));
            ShopDataSource shopDataSource = new ShopDataSource("data","shop_data.csv");
            shopDataSource.writeData(dataBox.getShopList());
            com.github.saacsos.FXRouter.goTo("my_shop", dataBox);
        }
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

    public void handleMyProfile(ActionEvent event) throws IOException{
        try {
            com.github.saacsos.FXRouter.goTo("my_profile", dataBox);
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


}
