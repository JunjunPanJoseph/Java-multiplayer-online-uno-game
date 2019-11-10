package game.client;

import java.io.IOException;

import game.client.viewNodes.ClientErrorMsgNode;
import game.client.views.StartView;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientView {
	private static int nCreate = 0;
    private Stage stage;
    private Scene scene;
    private ClientModel model;
    
    private Group root;
    private ClientErrorMsgNode errorMsg;
    private Text logArea;
    public ClientView(Stage stage) throws IOException {
    	this.stage = stage; 
        root = new Group();
        // Log area
        logArea = new Text("logs: ");
        logArea.setLayoutX(0);
        logArea.setLayoutY(50);
        // General error msg
        errorMsg = new ClientErrorMsgNode(stage, root);
        scene = new Scene(root);
        new StartView(this, stage).start();
        nCreate++;
    }
    public void errorMsg(String msg) {
    	System.out.println("ClientView: " + nCreate);
    	errorMsg.disPlayMsg(msg);
    }
    public void addRoot(Group root) {
    	this.root.getChildren().clear();
    	this.root.getChildren().add(root);
        root.getChildren().add(logArea);
    }
    public ClientModel getModel() {
    	return model;
    }
    public void setModel(ClientModel model) {
    	this.model = model;
    }
    public void log(String str) {
    	logArea.setText(logArea.getText() + "\n" + str);
    }
    public void start() {
        stage.setTitle("UNO Online client");
        stage.setScene(scene);
        stage.show();
    }
	public Stage getStage() {
		return stage;
	}
    
}
