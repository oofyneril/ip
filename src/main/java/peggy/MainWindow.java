package peggy;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class MainWindow {

    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private Peggy peggy;

    private final Image userImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/user.png")));
    private final Image botImg  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bot.png")));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setPeggy(Peggy peggy) {
        this.peggy = peggy;
        dialogContainer.getChildren().add(
                DialogBox.getBotDialog(cleanForGui(peggy.getWelcomeMessage()), botImg)
        );
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        // if blank, show Peggy error without creating an empty user bubble
        if (input == null || input.trim().isBlank()) {
            dialogContainer.getChildren().add(
                    DialogBox.getBotDialog(cleanForGui(peggy.getResponse(input)), botImg)
            );
            userInput.clear();
            return;
        }

        // user bubble
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImg));

        String response = cleanForGui(peggy.getResponse(input));
        dialogContainer.getChildren().add(DialogBox.getBotDialog(response, botImg));

        boolean isBye = peggy.isExitCommand(input);
        userInput.clear();

        if (isBye) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.millis(600));
            delay.setOnFinished(e -> Platform.exit());
            delay.play();
        }
    }

    private static String cleanForGui(String s) {
        if (s == null) return "";
        return s.replaceAll("(?m)^-+\\s*$\\R?", "").trim();
    }
}
