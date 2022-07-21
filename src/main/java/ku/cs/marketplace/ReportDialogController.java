package ku.cs.marketplace;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class ReportDialogController {
    @FXML private TextArea detailTextArea;
    @FXML private ComboBox typeComboBox;

    public void setChoice() {
        String[] choices = { "เนื้อหาไม่เหมาะสม","เนื้อหาล่อแหลม","หลอกลวง/สินค้าปลอม","เนื้อหาอันตรายมีความรุนแรง"};
        typeComboBox.setItems(FXCollections.observableArrayList(choices));
    }

    public ComboBox getTypeComboBox() {
        return typeComboBox;
    }

    public TextArea getDetailTextArea() {
        return detailTextArea;
    }
}
