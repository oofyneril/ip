package peggy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static final String SAVE_PATH = "data/peggy.txt";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/MainWindow.fxml"));
        AnchorPane root = loader.load();

        MainWindow controller = loader.getController();
        controller.setPeggy(new Peggy(SAVE_PATH));

        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                MainApp.class.getResource("/view/style.css").toExternalForm()
        );

        stage.setTitle("Peggy");
        stage.setScene(scene);
        stage.show();
    }
}
