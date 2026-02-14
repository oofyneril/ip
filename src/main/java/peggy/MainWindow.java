package peggy;

import java.util.Objects;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainWindow {

    private static final Duration IDLE_TIMEOUT = Duration.minutes(3); // change this
    private static final Duration CLOSE_DELAY = Duration.seconds(2);

    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private Peggy peggy;

    private final Image userImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/user.png")));
    private final Image botImg  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bot.png")));

    private final PauseTransition idleTimer = new PauseTransition(IDLE_TIMEOUT);

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        idleTimer.setOnFinished(e -> handleTimeout());

        // Reset timeout on user activity in the input area
        userInput.addEventFilter(KeyEvent.ANY, e -> resetIdleTimer());
        userInput.addEventFilter(MouseEvent.ANY, e -> resetIdleTimer());

        // If you want: also reset when clicking Send (extra safety)
        sendButton.addEventFilter(MouseEvent.ANY, e -> resetIdleTimer());
    }

    public void setPeggy(Peggy peggy) {
        this.peggy = peggy;

        dialogContainer.getChildren().add(
                DialogBox.getBotDialog(cleanForGui(peggy.getWelcomeMessage()), botImg)
        );

        resetIdleTimer(); // start counting once app is ready
    }

    @FXML
    private void handleUserInput() {
        resetIdleTimer();

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
            idleTimer.stop();
            userInput.setDisable(true);
            sendButton.setDisable(true);

            PauseTransition delay = new PauseTransition(Duration.millis(600));
            delay.setOnFinished(e -> Platform.exit());
            delay.play();
        }
    }

    private void resetIdleTimer() {
        // If peggy isn't set yet, don't start the timer
        if (peggy == null) {
            return;
        }
        idleTimer.stop();
        idleTimer.setDuration(IDLE_TIMEOUT);
        idleTimer.playFromStart();
    }

    private void handleTimeout() {
        // Prevent repeated firing
        idleTimer.stop();

        dialogContainer.getChildren().add(
                DialogBox.getBotDialog("Session timed out due to inactivity. Closing…", botImg)
        );

        userInput.setDisable(true);
        sendButton.setDisable(true);

        PauseTransition closeDelay = new PauseTransition(CLOSE_DELAY);
        closeDelay.setOnFinished(e -> Platform.exit());
        closeDelay.play();
    }

    private static String cleanForGui(String s) {
        if (s == null) {
            return "";
        }
        return s.replaceAll("(?m)^-+\\s*$\\R?", "").trim();
    }
}
