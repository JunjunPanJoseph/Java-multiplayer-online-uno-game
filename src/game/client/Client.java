package game.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setWidth(1080);
		primaryStage.setHeight(720);
		ClientView clientView = new ClientView(primaryStage);
		clientView.start();
	}
	
	public static void main(String[] args) {
		System.out.println("Main");
		launch(args);
	}
}