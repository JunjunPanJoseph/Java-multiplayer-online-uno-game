package application.scene.entry;

import application.scene.gameLocal.GameLocal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
/**
 * 
 * @author ASUS
 *
 */
public class GameSceneController {
	private GameScene gameScene;
	private GameLocal gameLocal;
	public GameSceneController(GameScene gameScene){
		this.gameScene = gameScene;
	}
    @FXML
    private Button gameStartLocal;
    @FXML
    private Button gameStartOnline;
    @FXML
    private Button setting;
    @FXML
    private Button exit;

    
    @FXML
    public void handleGameStartLocal(ActionEvent event) {
    	System.out.println("Start game");
    	gameLocal.start();
    }
    @FXML
    public void handleGameStartOnline(ActionEvent event) {
    	System.out.println("Start game online");
    }
    @FXML
    public void handleExit(ActionEvent event) {
    	gameScene.close();
    }
    @FXML
    public void handleSetting(ActionEvent event) {
    	//Setting
    }
    @FXML
    public void handleKeyPress(KeyEvent event) {
    	System.out.println("Key Press: "+event.getText());
        switch (event.getCode()) {
        case ENTER:
        	this.handleGameStartLocal(null);
		default:
			break;
        }
    }
	public void setGameLocal(GameLocal gameLocal) {
		this.gameLocal = gameLocal;
	}

}
