package ku.cs.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.Admin;
import ku.cs.models.BlacklistAccount;
import ku.cs.models.BlacklistAccountList;
import ku.cs.models.User;
import ku.cs.services.BlacklistDataSource;
import ku.cs.services.DataBox;
import ku.cs.services.DataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AdminBlacklistAccountController {
    private DataBox dataBox;
    @FXML private Label username;
    @FXML private Label loginAttemptLabel;

    public void setBlacklistAccount(BlacklistAccount blacklistAccount, DataBox dataBox){
        this.dataBox = dataBox;
        username.setText(blacklistAccount.getUsername());
        loginAttemptLabel.setText(blacklistAccount.getLoginAttempt() + "");
    }

    @FXML void handleUnbanAccount(ActionEvent event) {
        ((Admin) dataBox.getCurrentlyAccount()).unbanUser((User) dataBox.getAccountList().getAccountByUsername(username.getText()));
        dataBox.updateBlacklistAccount();
        try {
            com.github.saacsos.FXRouter.goTo("home_admin", dataBox);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
