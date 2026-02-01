package peggy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import peggy.ui.MainWindow;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/MainWindow.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("Peggy");
        stage.setScene(scene);

        MainWindow controller = loader.getController();
        controller.setPeggy(new Peggy("data/peggy.txt"));

        stage.show();
    }
}
