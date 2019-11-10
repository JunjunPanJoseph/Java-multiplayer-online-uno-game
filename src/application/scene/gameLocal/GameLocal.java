package application.scene.gameLocal;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class GameLocal {

    private Stage stage;
    private Scene scene;
    private GameLocalController controller;
    /**
	 * 
	 * @param stage
	 * @throws IOException
	 */
    public GameLocal(Stage stage) throws IOException {
    	this.stage = stage;
    	
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameView.fxml"));
        controller = new GameLocalController(this, stage);
        loader.setController(controller);
        // load into a Parent node called root
        Parent root = loader.load();
        root.requestFocus();
        scene = new Scene(root);
        root.requestFocus();
    }
    /**
     * Start the scene.
     */
    public void start() {
        //stage.setTitle("tempTitle");
    	controller.bootstrap();
        stage.setScene(scene);
        stage.show();
        
    }
}