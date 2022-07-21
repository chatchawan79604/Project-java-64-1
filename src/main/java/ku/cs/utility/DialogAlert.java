package ku.cs.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import ku.cs.marketplace.ReportDialogController;

import java.util.ArrayList;
import java.util.Optional;

public class DialogAlert {
    public static void showInformationDialogAlert(String title, String header, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static void showInformationDialogAlert(String title, String text){
        showInformationDialogAlert(title,null,text);
    }

    public static void showErrorDialogAlert(String title, String header, String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static void showErrorDialogAlert(String title, String text){
        showErrorDialogAlert(title, null,text);
    }

    public static boolean showConfirmationDialogAlert(String title, String header,String text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        ButtonType confirmButton = new ButtonType("ยืนยัน");
        ButtonType cancelButton = new ButtonType("ยกเลิก");
        alert.getButtonTypes().setAll(confirmButton,cancelButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == confirmButton){
            return true;
        } else {
            return false;
        }
    }

    public static boolean showConfirmationDialogAlert(String title, String text){
        return showConfirmationDialogAlert(title,null,text);
    }

    public static String showChoiceDialogAlert(String title, String text, ArrayList<String> choices){
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0),choices);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(text);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        }
        return null;
    }

    public static String showInputDialogAlert(String title, String header,String text){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(text);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public static String showInputDialogAlert(String title, String text){
        return showInputDialogAlert(title,null,text);
    }
}
