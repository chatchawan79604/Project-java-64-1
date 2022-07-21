package ku.cs.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import ku.cs.services.DataBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AboutUsController {
    private DataBox dataBox;
    @FXML private Circle imagePound;
    @FXML private Circle imageJik;
    @FXML private Circle imageGame;
    @FXML private Circle imageView;

    public void initialize(){
        try {
            imagePound.setFill(new ImagePattern(new Image( new FileInputStream("data/images/about_us/pound.jpg"))));
            imageJik.setFill(new ImagePattern(new Image(new FileInputStream("data/images/about_us/jik.jpg"))));
            imageGame.setFill(new ImagePattern(new Image(new FileInputStream("data/images/about_us/game.png"))));
            imageView.setFill(new ImagePattern(new Image(new FileInputStream("data/images/about_us/view.jpg"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
    public void handleCloseProgram(ActionEvent actionEvent){
        com.github.saacsos.FXRouter.closeStage();
    }

}
