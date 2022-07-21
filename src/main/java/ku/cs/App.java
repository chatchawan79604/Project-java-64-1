package ku.cs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.github.saacsos.FXRouter;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    Parent root;
    public double x = 0,y = 0;

    @Override
    public void start(Stage stage) throws IOException {
        //stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image(new FileInputStream("data/images/logo/MiniLogo.png")));
        FXRouter.bind(this, stage, "Lazadeekaisa", 1360, 768);
        configRoute();

        FXRouter.setAnimationType("fade");
        FXRouter.goTo("login");
    }

    private static void configRoute() {
        String packageStr = "ku/cs/";
        FXRouter.when("login", packageStr+ "login.fxml");
        FXRouter.when("signup", packageStr+"signup.fxml");
        FXRouter.when("about_us", packageStr+"about_us.fxml");
        FXRouter.when("marketplace", packageStr+"marketplace.fxml");
        FXRouter.when("create_shop",packageStr+"create_shop.fxml");
        FXRouter.when("my_profile",packageStr+"my_profile.fxml");
        FXRouter.when("change_password",packageStr+"change_password.fxml");
        FXRouter.when("chang_password_admin",packageStr+"chang_password_admin.fxml");
        FXRouter.when("my_shop",packageStr+"my_shop.fxml");
        FXRouter.when("product",packageStr+"product.fxml");
        FXRouter.when("product_details",packageStr+"product_details.fxml");
        FXRouter.when("history_order",packageStr+"history_order.fxml");
        FXRouter.when("history_order_product",packageStr+"history_order_product.fxml");
        FXRouter.when("store_page",packageStr+"store_page.fxml");
        FXRouter.when("add_product",packageStr+"add_product.fxml");
        FXRouter.when("order_list",packageStr+"order_list.fxml");
        FXRouter.when("history_order",packageStr+"history_order.fxml");
        FXRouter.when("home_admin",packageStr+"home_admin.fxml");
        FXRouter.when("user_in_home_admin",packageStr+"user_in_home_admin.fxml");
        FXRouter.when("review",packageStr+"review.fxml");
        FXRouter.when("edit_product",packageStr+"edit_product.fxml");
        FXRouter.when("admin_reported_review_panel",packageStr+"admin_reported_review_panel.fxml");
        FXRouter.when("reported_review",packageStr+"reported_review.fxml");
        FXRouter.when("reported_review_detail",packageStr+"reported_review_detail.fxml");
        FXRouter.when("admin_reported_product_panel",packageStr+"admin_reported_product_panel.fxml");
        FXRouter.when("reported_product_detail",packageStr+"reported_product_detail.fxml");
        FXRouter.when("reported_product_detail",packageStr+"reported_product_detail.fxml");
        FXRouter.when("admin_blacklist_account",packageStr+"admin_blacklist_account.fxml");
        FXRouter.when("how_to_use",packageStr+"how_to_use.fxml");
    }

    public static void setRoot(String fxml) throws IOException {

        Parent root = loadFXML(fxml);
        scene.setFill(Color.TRANSPARENT);
        scene.setRoot(root);

    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}