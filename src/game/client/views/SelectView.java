package game.client.views;

import game.client.ClientPlayer;
import game.client.ClientView;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SelectView extends ClientGroupView{
	private ClientPlayer currPlayer;
	public SelectView(ClientView view, Stage stage, StartView currView) {
		super(view, stage);
		//Player 
		currPlayer = new ClientPlayer();
        TextField playerName = new TextField("Enter your name: ");
        playerName.layoutXProperty().bind(getStageWidthProperty().divide(4).subtract(playerName.widthProperty().divide(2)));
        playerName.layoutYProperty().bind(getStageHeightProperty().divide(4));
        // Button
        Button button0 = new Button("New game room");
        button0.layoutXProperty().bind(getStageWidthProperty().divide(3).subtract(button0.widthProperty().divide(2)));
        button0.layoutYProperty().bind(getStageHeightProperty().divide(4));
        button0.prefWidthProperty().bind(getStageWidthProperty().multiply(0.2));
        button0.prefHeightProperty().bind(getStageHeightProperty().multiply(0.5));
        button0.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currPlayer.setName(playerName.getText());
				getModel().send_newGameRoom(currPlayer);
			}
        	
        });
        TextField roomNumber = new TextField();
        roomNumber.layoutXProperty().bind(getStageWidthProperty().divide(4).multiply(3).subtract(roomNumber.widthProperty().divide(2)));
        roomNumber.layoutYProperty().bind(getStageHeightProperty().divide(4));
        
        Button button1 = new Button("Join game room");
        button1.layoutXProperty().bind(getStageWidthProperty().divide(3).multiply(2).subtract(button1.widthProperty().divide(2)));
        button1.layoutYProperty().bind(getStageHeightProperty().divide(4));
        button1.prefWidthProperty().bind(getStageWidthProperty().multiply(0.2));
        button1.prefHeightProperty().bind(getStageHeightProperty().multiply(0.5));
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					currPlayer.setName(playerName.getText());
					int roomNo = Integer.parseInt(roomNumber.getText());
					getModel().send_joinGameRoom(currPlayer, roomNo);
				} catch (NumberFormatException e) {
					errorMsg("Invalid input! must be a number");
				}
			}
        	
        });
        Button button2 = new Button("Back");
        button2.layoutXProperty().bind(getStageWidthProperty().divide(5).subtract(button2.widthProperty().divide(2)));
        button2.layoutYProperty().bind(getStageHeightProperty().divide(2));
        button2.prefWidthProperty().bind(getStageWidthProperty().multiply(0.05));
        button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currView.start();
			}
        	
        });
        getRoot().getChildren().addAll(button0, button1, button2, playerName, roomNumber);
	}

}
