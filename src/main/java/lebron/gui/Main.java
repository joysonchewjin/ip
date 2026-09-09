package lebron.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lebron.LeBron;

/**
 * JavaFX entry point: loads the main window's FXML, wires it to a
 * {@link LeBron} instance, and shows the stage.
 */
public class Main extends Application {
    private final LeBron leBron = new LeBron();

    /** Loads MainWindow.fxml, injects {@link #leBron} into its controller, and shows the window. */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("LeBron");
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setLeBron(leBron);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
