package ku.cs.admin;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.User;

import javafx.scene.control.Label;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserInHomeAdmin {


    @FXML private Label accountNameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label shopLabel;
    @FXML private Label timeLabel;
    @FXML private ImageView userImageImageView;

    public void setUser(User user){

        try {
            userImageImageView.setImage(new Image(new FileInputStream(user.getPathPicture())));
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }

        accountNameLabel.setText(user.getAccountName());
        usernameLabel.setText(user.getUsername());
        if(user.getShop()==null){
            shopLabel.setText("ไม่มีร้านค้า");
        }else{
            shopLabel.setText(user.getShop().getName());
        }
        if(user.getTimeLogin()==null){
            timeLabel.setText("ไม่เคยเข้าสู่ระบบ");
        }else{
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("วันที่ d MMMM yyyy เวลา HH:mm:ss",new Locale("th","TH"));
            String formattedDate = user.getTimeLogin().format(timeFormatter);
            timeLabel.setText(formattedDate);
        }


    }
}
