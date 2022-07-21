package ku.cs.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ku.cs.services.DataBox;

import java.io.IOException;

public class HowToUseController {

    private DataBox dataBox;
    public void initialize(){
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
}
