package ku.cs.marketplace;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CategoryDetailController {
    @FXML private Label attributeLabel;
    @FXML private Label dataAttributeLabel;

    public void setDataCategoryDetail(String attribute, String dataAttribute){
        attributeLabel.setText(attribute);
        dataAttributeLabel.setText(dataAttribute);
    }
}
