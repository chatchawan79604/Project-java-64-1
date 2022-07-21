package ku.cs.shop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AttributeController {
    @FXML private Label attributeLabel;
    @FXML private TextField attributeTextField;

    public void setAttribute(String attribute, int index, String[] array){
        attributeLabel.setText(attribute);
        attributeTextField.setText(array[index]);
    }

    public String getAttributeTextField() {
        return attributeTextField.getText();
    }
}
