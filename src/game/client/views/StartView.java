package game.client.views;

import game.client.ClientView;
import game.client.SyncClient;
import game.client.states.ClientSelectState;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class StartView extends ClientGroupView {
	public StartView(ClientView view, Stage stage) {
		super(view, stage);
		StartView currView = this;
        // Button
        Button button0 = new Button("Start game");
        button0.layoutXProperty().bind(getStageWidthProperty().divide(2).subtract(button0.widthProperty().divide(2)));
        button0.layoutYProperty().bind(getStageHeightProperty().multiply(4).divide(8));
        button0.prefWidthProperty().bind(getStageWidthProperty().multiply(.1));
        button0.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Connect to server
				SyncClient syncClient = new SyncClient();
				if (syncClient.connectToServer()) {
					syncClient.getModel().setView(view);
					setModel(syncClient.getModel());
					
					SelectView selectView = new SelectView(view, stage, currView);
					ClientSelectState newState = new ClientSelectState(syncClient.getModel());
					newState.setSelectView(selectView);
					syncClient.getModel().setState(newState);
					selectView.start();
				} else {
					errorMsg("Connect server failed!");
				}
				
			}
        	
        });
        Button button1 = new Button("Setting");
        button1.layoutXProperty().bind(getStageWidthProperty().divide(2).subtract(button0.widthProperty().divide(2)));
        button1.layoutYProperty().bind(getStageHeightProperty().multiply(5).divide(8));
        button1.prefWidthProperty().bind(getStageWidthProperty().multiply(.1));
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				errorMsg("Not finished");
			}
        	
        });
        
        Button button2 = new Button("Exit");
        button2.layoutXProperty().bind(getStageWidthProperty().divide(2).subtract(button0.widthProperty().divide(2)));
        button2.layoutYProperty().bind(getStageHeightProperty().multiply(6).divide(8));
        button2.prefWidthProperty().bind(getStageWidthProperty().multiply(.1));
        button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.close();
			}
        });
        getRoot().getChildren().addAll(button0, button1, button2);
	}

}
