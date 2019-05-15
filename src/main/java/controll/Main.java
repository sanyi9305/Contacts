/**
 * <h1>Névjegyzék</h1>
 * A névjegyzék alkalmazásban eltudjuk menteni az ismerőseink nevét telefonszámát és e-mail címét.
 *
 * @author Bodnár Sándor
 * @version 1.0
 * @since 2019-05-15
 */
package controll;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Indító osztály melyben elindul a programunk.
 */
public class Main extends Application {

    /**
     * Belépési pont melyben indul a programunk.
     * @param args indításhoz argumentumok
     */
    public static void main(String[] args) {
        launch(args);

    }

    /**
     * Kezdőképernyő meghívása.
     * @param primaryStage kezdőképernyő
     * @throws Exception kivétel
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainscene.fxml"));
        primaryStage.setTitle("Névjegyzék");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

    }


}
