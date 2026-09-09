package lebron.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lebron.LeBron;

/**
 * Controller for the main chat window: a scrollable column of dialog
 * bubbles, plus a text field and button for entering commands.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/User.png"));
    private final Image leBronImage = new Image(getClass().getResourceAsStream("/images/LeBron.png"));

    private LeBron leBron;

    /** Auto-scrolls the dialog area to the bottom whenever it grows. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the {@link LeBron} instance this window sends commands to. */
    public void setLeBron(LeBron leBron) {
        this.leBron = leBron;
    }

    /**
     * Reads the text typed into {@link #userInput}, runs it through
     * {@link LeBron#getResponse(String)}, and appends a user bubble and a
     * LeBron reply bubble to {@link #dialogContainer}. Closes the app after
     * a "bye", the same way the console version does.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        String response = leBron.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getLeBronDialog(response, leBronImage));
        userInput.clear();

        if (leBron.isExit(input)) {
            Platform.exit();
        }
    }
}
