package lebron.gui;

import javafx.application.Application;

/**
 * Separate entry point that starts {@link Main} via {@link Application#launch}.
 * A packaged (shadow) jar whose main class directly extends
 * {@code Application} can trip Java's module-path check for JavaFX runtime
 * components ("JavaFX runtime components are missing"); launching through a
 * plain class instead avoids that check entirely.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
