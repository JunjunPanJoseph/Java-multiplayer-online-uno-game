package application.scene.gameLocal.gameNode;

import java.util.Random;

import application.unoBackend.Card;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class ClientCardPileNode extends GameNode{
	private Pane cardPile;
	private boolean randomLoc;
	private double x;
	private double y;
	public ClientCardPileNode(int nCards, double x, double y, boolean randomLoc) {
		this.x = x;
		this.y = y;
		this.randomLoc = randomLoc;
		cardPile = new Pane();
		if (!randomLoc)
			cardPile.getChildren().add(CardNode.getCardBack());
		cardPile.setMouseTransparent(true);
		cardPile.setLayoutX(getWidth().get() * x);
		cardPile.setLayoutY(getHeight().get() * y);
		ChangeListener<Number> cardPileX = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				cardPile.setLayoutX(getWidth().get() * x);
			}
	    };
	    ChangeListener<Number> cardPileY = new ChangeListener<Number>() {
	    	@Override
	    	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	    		cardPile.setLayoutY(getHeight().get() * y);
	    	}
	    };
	    getWidth().addListener(cardPileX);
	    getHeight().addListener(cardPileY);
	}
	@Override
	public Node getNode() {
		return cardPile;
	}
	public void setMouseTransparent(boolean val) {
		cardPile.setMouseTransparent(val);
	}
	public double[] topCardLoc() {
		double [] retList = new double[3];
		//[0]: x relevant to width 
		//[1]: y relevant to width 
		//[2]: degree of rotation
		retList[0] = x;
		retList[1] = y;
		retList[2] = 0;
		return retList;
	}
	public double[] nextCardLoc() {
		double [] retList = new double[3];
		//[0]: x relevant to width 
		//[1]: y relevant to width 
		//[2]: degree of rotation
		retList[0] = x;
		retList[1] = y;
		retList[2] = 0;
		if (randomLoc) {
			Random rand = new Random();
			retList[0] += (rand.nextDouble() - 0.5) / 20;
			retList[1] += (rand.nextDouble() - 0.5) / 20;
			retList[2] += (rand.nextDouble() - 0.5) * 30;
		}
		return retList;
	}
	public void addCard(Card card, double[] endLoc) {
		Node newCard = CardNode.getCardFront(card);
		if (endLoc == null) {
			endLoc = nextCardLoc();
		}
		cardPile.getChildren().add(newCard);
		newCard.setLayoutX(getWidth().get() * (endLoc[0] - x));
		newCard.setLayoutY(getHeight().get() * (endLoc[1] - y));
		newCard.setRotate(endLoc[2]);
		
	}

}
