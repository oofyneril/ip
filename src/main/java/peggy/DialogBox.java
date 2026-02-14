package peggy;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class DialogBox extends HBox {

    @FXML private Label dialog;
    @FXML private ImageView displayPicture;
    @FXML private Label nameLabel;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DialogBox.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            setMaxWidth(Double.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dialog.setText(text);
        dialog.setWrapText(true);
        dialog.setMaxWidth(320);
        dialog.getStyleClass().add("bubble");

        displayPicture.setImage(img);

        nameLabel.getStyleClass().add("name-label");
    }

    public void setText(String text) {
        dialog.setText(text);
    }


    private void flip() {
        ObservableList<Node> nodes = FXCollections.observableArrayList(getChildren());
        Collections.reverse(nodes);
        getChildren().setAll(nodes);
        setAlignment(Pos.TOP_LEFT);
    }

    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.nameLabel.setText("User");
        db.dialog.getStyleClass().add("user-bubble");
        db.setAlignment(Pos.TOP_RIGHT);
        return db;
    }

    public static DialogBox getBotDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.nameLabel.setText("Peggy");
        db.dialog.getStyleClass().add("bot-bubble");
        db.flip();
        db.setAlignment(Pos.TOP_LEFT);
        return db;
    }
}
