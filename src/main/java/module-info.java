module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens ku.cs to javafx.fxml;
    exports ku.cs;
    exports ku.cs.home;
    opens ku.cs.home to javafx.fxml;
    exports ku.cs.marketplace;
    opens ku.cs.marketplace to javafx.fxml;
    exports ku.cs.models;
    opens ku.cs.models to javafx.fxml;
    exports ku.cs.shop;
    opens ku.cs.shop to javafx.fxml;
    exports ku.cs.services;
    opens ku.cs.services to javafx.fxml;
    exports ku.cs.utility;
    opens ku.cs.utility to javafx.fxml;
    exports ku.cs.admin;
    opens ku.cs.admin to javafx.fxml;
    exports ku.cs.user;
    opens ku.cs.user to javafx.fxml;
    exports ku.cs.attribute;
    opens ku.cs.attribute to javafx.fxml;
}
