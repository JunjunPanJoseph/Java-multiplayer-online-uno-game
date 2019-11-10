package application.scene.gameLocal.gameNode;


import org.json.JSONObject;

import application.client.ClientModelPlayer;
import application.client.ClientView;
import application.unoBackend.Card;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ClientPlayerNode extends GameNode{
	static int animationTicks = 200;
	static int playerSectorHalf = 60;
	private int nodeHeight = 150;
	private int nodeWidth = 200;
	private ClientView view;
	private ClientModelPlayer player;
	
	private Text nameText;
	private Rectangle background;
	
	private int degree;
	private Pane playerNode;
	private Pane hand;
	private ChangeListener<Number> locationListener;
	public double getDegree() {
		return degree;
	}
	public ClientPlayerNode(ClientModelPlayer player, ClientView view){
		
		this.view = view;
		this.player = player;
		playerNode = new Pane();
		background = new Rectangle();
		background.setHeight(nodeHeight);
		background.setWidth(nodeWidth);
		background.setFill(Color.BISQUE);
		playerNode.getChildren().add(background);
		nameText = new Text(player.getName());
		nameText.setFont(Font.font ("Verdana", 20));
		nameText.setFill(Color.WHITE);
		nameText.setStrokeWidth(1);
		nameText.setStroke(Color.BLACK);
		
		playerNode.getChildren().add(nameText);
		int deg = (360 - 2 * playerSectorHalf) / view.getNPlayer();
		degree = 90; 
		degree += player.getRelLoc(view.getClientPlayerId(), view.getNPlayer()) * deg + playerSectorHalf;
		degree %= 360;
		
		this.hand = new Pane();
		if (player.getId() == view.getClientPlayerId()) {
			System.out.println("This is curr player");
			nodeHeight = 150;
			nodeWidth = 450;
		}
		playerNode.getChildren().add(hand);
		
		
		locationListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				//System.out.println("Name: " + player.getName() + "  Ratio: " + relvRatio);
				if (player.getRelLoc(view.getClientPlayerId(), view.getNPlayer()) == 0) {
					//Current player
					playerNode.setLayoutX(getWidth().get() * 0.25);
					playerNode.setLayoutY(getHeight().get() * 0.75);
				} else {
					// x2/a2 + y2/b2 = 1  a, b: x, y intercepts
					double a = getWidth().get() / 2.1 - nodeWidth / 2 ;
					double b = getHeight().get() / 2.1 - nodeHeight / 2;
					double x;
					double y;
					double relvDegree = degree;
					if (relvDegree == 0) {
						y = 0;
						x = a;
					} else if (relvDegree == 180) {
						y = 0;
						x = -a;
					} else if (relvDegree == 90) {
						y = b;
						x = 0;
					} else if (relvDegree == 270) {
						y = -b;
						x = 0;
					} else {
						double tanA = Math.tan(Math.PI * relvDegree / 180);
						x = a * b / Math.sqrt(a * a * tanA * tanA + b * b);
						y = a * b / Math.sqrt(b * b * (1/tanA) * (1/tanA) + a * a);
						if (relvDegree > 180) {
							y *= -1;
						}
						if (relvDegree > 90 && relvDegree < 270) {
							x *= -1;
						}
					}
					playerNode.setLayoutX(x - nodeWidth/2 + getWidth().get() / 2);
					playerNode.setLayoutY(y - nodeHeight/2 + getHeight().get() / 2);
				}
			}
	    };
	    getWidth().addListener(locationListener);
	    getHeight().addListener(locationListener);
	}
	public Node getNode() {
		return playerNode;
	}
	private void addNewCardToHand(Pane cardBox, boolean playAnimation) {
		cardBox.setVisible(false);
		hand.getChildren().add(cardBox);
		//Should be playerd by client view: 
		//use a appropriate queue to make sure animation won't ovarlap
		if (playAnimation) {
			drawCardAnimation();
		}
	}
	public class locInterpolator extends Interpolator {
	    @Override
	    protected double curve(double t) {
	    	return t ;
	    }
	}
	public KeyFrame moveCard(Node cardNode, int ms, double[] endLoc) {
		KeyValue drawCardX = new KeyValue(cardNode.layoutXProperty(), endLoc[0], new locInterpolator());
		KeyValue drawCardY = new KeyValue(cardNode.layoutYProperty(), endLoc[1], new locInterpolator());
		KeyValue drawCardRotate = new KeyValue(cardNode.rotateProperty(), endLoc[2], new locInterpolator());
		KeyFrame drawCardKeyFrame = new KeyFrame(Duration.millis(ms), drawCardX, drawCardY, drawCardRotate);
		return drawCardKeyFrame;
	}
	public double[] getPlayerCardLoc(Node node) {
		double[] retVal = new double[3];
		Point2D playerNodeCoor = hand.localToParent(new Point2D(node.getLayoutX(),node.getLayoutY()));
		Point2D gamePaneCoor = playerNode.localToParent(playerNodeCoor);
		
		retVal[0] = gamePaneCoor.getX();
		retVal[1] = gamePaneCoor.getY();
		retVal[2] = node.getRotate();
		return retVal;
	}
	public void drawCardAnimation() {
		//Play animation
		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		int nCards = hand.getChildren().size();
		for (int i = 0; i < nCards; i++) {
			Node card = hand.getChildren().get(i);
			double ratio = (double) i / nCards;
			KeyValue keyValueX = new KeyValue(card.layoutXProperty(), nodeWidth * ratio, new locInterpolator());
			KeyFrame kf = new KeyFrame(Duration.millis(animationTicks), keyValueX);
			timeline.getKeyFrames().add(kf);
			if (!card.isVisible()) {
	    		//Play animation of draw card
	    		//after it is finished, make the card visible
				double [] cardLocation = view.getCardPile().topCardLoc();
	    		
				Node drawCardNode = CardNode.getCardBack();
	    		drawCardNode.setLayoutX(cardLocation[0] * getWidth().get());
	    		drawCardNode.setLayoutY(cardLocation[1] * getHeight().get());
	    		drawCardNode.setRotate(cardLocation[2]);
	    		view.getGamePane().getChildren().add(drawCardNode);
	    		
	    		
	    		Point2D playerNodeCoor = hand.localToParent(new Point2D(nodeWidth * ratio,card.getLayoutY()));
	    		Point2D gamePaneCoor = playerNode.localToParent(playerNodeCoor);
	    		
	    		cardLocation[0] = gamePaneCoor.getX();
	    		cardLocation[1] = gamePaneCoor.getY();
	    		cardLocation[2] = card.getRotate();
				EventHandler<ActionEvent> drawCardFinished = new EventHandler<ActionEvent>() {
		        	@Override
		            public void handle(ActionEvent t) {
		        		view.getGamePane().getChildren().remove(drawCardNode);
		        		card.setVisible(true);
		        		
		            }
		        };
				
		        KeyFrame drawCardKeyFrame = moveCard(drawCardNode, animationTicks, cardLocation);
				KeyFrame drawCardFinishedKeyFrame = new KeyFrame(Duration.millis(animationTicks), drawCardFinished);
				
				timeline.getKeyFrames().add(drawCardKeyFrame);
				timeline.getKeyFrames().add(drawCardFinishedKeyFrame);
			}
		}
		timeline.play();
	}
	public boolean useCardAnimation(JSONObject card) {
		int ithCard = card.getInt("id");
		Card cardObj = new Card(card.getString("color"), card.getString("value"));
		return useCardAnimation(ithCard, cardObj);
	}
	public boolean useCardAnimation(int ithCard, Card card) {
		System.out.println("ith card: "+ithCard);
		if (ithCard < 0 || ithCard >= hand.getChildren().size()) {
			return false;
		}
		
		Node handCard = hand.getChildren().get(ithCard);
		double [] startLoc = getPlayerCardLoc(handCard);
		double [] endLoc = view.getDiscardPile().nextCardLoc();
		hand.getChildren().remove(handCard);
		//Reset the handcard
		drawCardAnimation();
		//Use card
		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		
		Node drawCardNode;
		if (card == null) {
			drawCardNode = CardNode.getCardBack();
		} else {
			drawCardNode = CardNode.getCardFront(card);
		}
		drawCardNode.setLayoutX(startLoc[0]);
		drawCardNode.setLayoutY(startLoc[1]);
		drawCardNode.setRotate(startLoc[2]);
		System.out.println("degree: " + degree);
		endLoc[2] += 180-degree;
		view.getGamePane().getChildren().add(drawCardNode);
		
		EventHandler<ActionEvent> drawCardFinished = new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent t) {
        		//Do others
        		view.getGamePane().getChildren().remove(drawCardNode);
        		view.getDiscardPile().addCard(card, endLoc);
            }
        };
        double[] realEndloc = new double[3];
        realEndloc[0] = endLoc[0] * getWidth().get();
        realEndloc[1] = endLoc[1] * getHeight().get();
        realEndloc[2] = endLoc[2];
        
        KeyFrame drawCardKeyFrame = moveCard(drawCardNode, animationTicks, realEndloc);
		KeyFrame drawCardFinishedKeyFrame = new KeyFrame(Duration.millis(animationTicks), drawCardFinished);
		
		timeline.getKeyFrames().add(drawCardKeyFrame);
		timeline.getKeyFrames().add(drawCardFinishedKeyFrame);
		timeline.play();
		return true;
	}
	public void drawACard(Card newCard, boolean playAnimation) {
		Pane cardBox = new Pane();
		Node newCardNode;
		if (newCard == null) {
			newCardNode = CardNode.getCardBack();
		} else {
			newCardNode = CardNode.getCardFront(newCard);
			EventHandler<MouseEvent> moveEventHandler = new EventHandler<MouseEvent>() {
				private double originalY = cardBox.getLayoutY();
				private double movedY = cardBox.getLayoutY() - 50;
				@Override
				public void handle(MouseEvent event) {
					if (cardBox.getLayoutY() == originalY) {
						cardBox.setLayoutY(movedY);
					} else {
						cardBox.setLayoutY(originalY);
					}
				}
			};
			EventHandler<MouseEvent> clickEventHandler = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					//Let player select color if the card is black
					if (view.playerUseCard(hand.getChildren().indexOf(cardBox), null)) {
						useCardAnimation(hand.getChildren().indexOf(cardBox), newCard);
					}
				}
			};
			cardBox.setOnMouseEntered(moveEventHandler);
			cardBox.setOnMouseExited(moveEventHandler);
			cardBox.setOnMouseClicked(clickEventHandler);
		}
		cardBox.getChildren().add(newCardNode);
		addNewCardToHand(cardBox, playAnimation);
	}
	public void unSetCurrentPlayer() {
		background.setFill(Color.BISQUE);
		nameText.setFill(Color.WHITE);
	}
	public void setCurrentPlayer() {
		background.setFill(Color.RED);
		nameText.setFill(Color.RED);
	}
}
