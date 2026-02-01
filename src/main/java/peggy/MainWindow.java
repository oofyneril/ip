package peggy.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import peggy.Peggy;

public class MainWindow {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;

    private Peggy peggy;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setPeggy(Peggy peggy) {
        this.peggy = peggy;
        dialogContainer.getChildren().add(new Text(peggy.getWelcomeMessage()));
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = peggy.getResponse(input);

        dialogContainer.getChildren().addAll(
                new Text("You: " + input),
                new Text("Peggy: " + response)
        );

        userInput.clear();

        if (peggy.isExitCommand(input)) {
            Platform.exit();
        }
    }
}
