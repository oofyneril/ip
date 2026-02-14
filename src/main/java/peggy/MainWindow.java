package peggy;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Objects;

public class MainWindow {

    private static final Duration BOT_TYPING_TIME = Duration.millis(1010);
    private static final Duration EXIT_DELAY = Duration.millis(600);

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

        // If blank: no user bubble, but still show bot typing then error reply
        if (input == null || input.trim().isBlank()) {
            String response = cleanForGui(peggy.getResponse(input));
            userInput.clear();
            showBotTypingThenReply(response, false);
            return;
        }

        // User bubble
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImg));

        boolean isBye = peggy.isExitCommand(input);
        String response = cleanForGui(peggy.getResponse(input));

        userInput.clear();

        // Show typing animation before showing the bot reply
        showBotTypingThenReply(response, isBye);
    }

    private void showBotTypingThenReply(String response, boolean exitAfter) {
        // Disable input while "typing" (prevents spamming multiple sends)
        userInput.setDisable(true);
        sendButton.setDisable(true);

        // Create a temporary typing bubble
        DialogBox typing = DialogBox.getBotDialog("...", botImg);
        typing.getStyleClass().add("typing-bubble");
        dialogContainer.getChildren().add(typing);

        // Animate dots: ".", "..", "..."
        Timeline dots = new Timeline(
                new KeyFrame(Duration.ZERO, e -> typing.setText(".")),
                new KeyFrame(Duration.millis(200), e -> typing.setText("..")),
                new KeyFrame(Duration.millis(400), e -> typing.setText("...")),
                new KeyFrame(Duration.millis(600), e -> typing.setText(".")),
                new KeyFrame(Duration.millis(800), e -> typing.setText("..")),
                new KeyFrame(Duration.millis(1000), e -> typing.setText("..."))
        );
        dots.setCycleCount(Animation.INDEFINITE);
        dots.play();

        // After a short delay, replace typing bubble with real reply
        PauseTransition wait = new PauseTransition(BOT_TYPING_TIME);
        wait.setOnFinished(e -> {
            dots.stop();
            dialogContainer.getChildren().remove(typing);
            dialogContainer.getChildren().add(DialogBox.getBotDialog(response, botImg));

            if (exitAfter) {
                PauseTransition delay = new PauseTransition(EXIT_DELAY);
                delay.setOnFinished(ev -> Platform.exit());
                delay.play();
                return;
            }

            userInput.setDisable(false);
            sendButton.setDisable(false);
            userInput.requestFocus();
        });
        wait.play();
    }

    private static String cleanForGui(String s) {
        if (s == null) return "";
        return s.replaceAll("(?m)^-+\\s*$\\R?", "").trim();
    }
}
