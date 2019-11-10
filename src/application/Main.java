package application;
	
import java.io.IOException;

import application.scene.entry.GameScene;
import application.scene.gameLocal.GameLocal;
import javafx.application.Application;
import javafx.stage.Stage;




public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		System.out.println("Start");
		GameScene gameScene = new GameScene(primaryStage);
		GameLocal gamelocal = new GameLocal(primaryStage);
		gameScene.setGameLocal(gamelocal);
		gameScene.start();
	}
	
	public static void main(String[] args) {
		System.out.println("Main");
		StaticText.setTextEN();
		launch(args);
	}
}
