package game.client.viewNodes;

import java.lang.management.ManagementFactory;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ClientErrorMsgNode implements ViewNode{
	private	Pane errorMsgPane;
	private Text errorTextContent;
	private Group root;
	
	private static int nCreate;
	public ClientErrorMsgNode(Stage stage, Group root){
		nCreate++;
		this.root = root;
        errorMsgPane = new Pane();
        errorMsgPane.layoutYProperty().bind(stage.heightProperty().divide(4));
        
        Rectangle errorMsgBackground = new Rectangle();
        errorMsgBackground.widthProperty().bind(stage.widthProperty());
        errorMsgBackground.heightProperty().bind(stage.heightProperty().divide(2));
        errorMsgBackground.setFill(Color.MEDIUMPURPLE);
        Text errorTextTitle = new Text();
        errorTextTitle.layoutXProperty().bind(errorMsgBackground.widthProperty().divide(2));
        errorTextTitle.layoutYProperty().bind(errorMsgBackground.heightProperty().divide(5));
        errorTextTitle.setText("Error");
        errorTextTitle.setTextAlignment(TextAlignment.CENTER);
        errorTextContent = new Text();
        errorTextContent.layoutXProperty().bind(errorMsgBackground.widthProperty().divide(2).subtract(errorTextTitle.strokeWidthProperty().divide(2)));
        errorTextContent.layoutYProperty().bind(errorMsgBackground.heightProperty().divide(2));
        errorTextContent.setText("default msg");
        errorTextContent.setTextAlignment(TextAlignment.CENTER);
        
        Button ok = new Button("OK");
        ok.layoutXProperty().bind(errorMsgBackground.widthProperty().divide(2).subtract(ok.widthProperty().divide(2)));
        ok.layoutYProperty().bind(errorMsgBackground.heightProperty().multiply(4).divide(5));
        ok.prefWidthProperty().bind(errorMsgBackground.widthProperty().multiply(.1));
        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				root.getChildren().remove(errorMsgPane);
			}
        });
        errorMsgPane.getChildren().addAll(errorMsgBackground, errorTextTitle, errorTextContent, ok);
        
	}
	@Override
	public Node getNode() {
		return errorMsgPane;
	}
	
	public void disPlayMsg(String str) {
		errorTextContent.setText(str);
		root.getChildren().add(errorMsgPane);
		System.out.println("Root hashcode: " + root.hashCode());
		errorTextContent.toFront();
	}
	
}
