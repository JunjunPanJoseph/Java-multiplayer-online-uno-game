package application.scene.gameLocal.gameNode;

import application.unoBackend.Card;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CardNode {
	public static int cardWidth = 100;
	public static int cardHeight = 150;
	public static int edgeWidth = 10;
	
	private static StackPane makeNewCard(Color rectangle, Color eclipse) {
		StackPane newCard = new StackPane();
		Rectangle outterRect = new Rectangle(cardWidth,cardHeight);
		outterRect.setArcHeight(edgeWidth / 2);
		outterRect.setArcWidth(edgeWidth / 2);
		outterRect.setFill(Color.WHITE);
		outterRect.setStroke(Color.BLACK);
		Rectangle innerRect = new Rectangle(cardWidth - edgeWidth, cardHeight - edgeWidth);
		innerRect.setArcHeight(edgeWidth / 2);
		innerRect.setArcWidth(edgeWidth / 2);
		innerRect.setFill(rectangle);
		Ellipse innerEllipse = new Ellipse();
		innerEllipse.setRadiusX((cardWidth - 2 * edgeWidth) / 2);
		innerEllipse.setRadiusY((cardHeight - edgeWidth) / 2);
		if (eclipse == null) {
			innerEllipse.setFill(Color.WHITE);
		} else {
			innerEllipse.setFill(eclipse);
		}
		innerEllipse.setRotate(20);
		
		ObservableList<Node> newCardChildren = newCard.getChildren();
		newCardChildren.add(outterRect);
		newCardChildren.add(innerRect);
		newCardChildren.add(innerEllipse);
		return newCard;
	}
	private static void cardAddCenter(StackPane cardNode, Node node) {
		cardNode.getChildren().add(node);
	}
	public static StackPane getCardFront(Card card) {
		if (card == null) {
			return getCardBack();
		}
		StackPane cardFront;
		switch(card.getColor()) {
		case RED:
			cardFront = makeNewCard(Color.RED, Color.WHITE);
			break;
		case GREEN:
			cardFront = makeNewCard(Color.GREEN, Color.WHITE);
			break;
		case BLUE:
			cardFront = makeNewCard(Color.BLUE, Color.WHITE);
			break;
		case YELLOW:
			cardFront = makeNewCard(Color.YELLOW, Color.WHITE);
			break;
		case BLACK:
			cardFront = makeNewCard(Color.BLACK, Color.WHITE);
			break;
		default:
			cardFront = new StackPane();
		}
		cardAddCenter(cardFront, new Text(card.getValue().toString()));
		return cardFront;
	}
	public static StackPane getCardBack() {
		StackPane cardBack = makeNewCard(Color.BLACK, Color.DARKRED);
		Text centerText =  new Text("UNO");
		cardAddCenter(cardBack,centerText);
		return cardBack;
	}
}
