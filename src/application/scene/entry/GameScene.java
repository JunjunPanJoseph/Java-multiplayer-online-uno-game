package application.scene.entry;

import java.io.IOException;

import application.scene.gameLocal.GameLocal;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameScene {

    private Stage stage;
    private Scene scene;
    private GameSceneController controller;
    /**
	 * 
	 * @param stage
	 * @throws IOException
	 */
    public GameScene(Stage stage) throws IOException {
    	this.stage = stage; 
    	
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameMainView.fxml"));
        controller = new GameSceneController(this);
        loader.setController(controller);
        // load into a Parent node called root
        Parent root = loader.load();
        root.requestFocus();
        scene = new Scene(root);
        
    }
    /**
     * Start the scene.
     */
    public void start() {
        stage.setTitle("tempTitle");
        stage.setScene(scene);
        stage.show();
    }
	/**
	 * Exit the game.
	 */
    public void close() {
    	stage.close();
    }
	public void setGameLocal(GameLocal gamelocal) {
		controller.setGameLocal(gamelocal);
	}
}