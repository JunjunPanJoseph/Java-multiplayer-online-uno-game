package application.scene.gameLocal.gameNode;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

public abstract class GameNode {
	private IntegerProperty height;
	private IntegerProperty width;
	public GameNode() {
		height = new SimpleIntegerProperty();
		width = new SimpleIntegerProperty();
	}
	public abstract Node getNode();
	public void bind(ObservableValue<? extends Number> height, ObservableValue<? extends Number> width) {
		this.height.bind(height);
		this.width.bind(width);
	}
	public void unbind() {
		this.height.unbind();
		this.width.unbind();
	}
	public IntegerProperty getHeight() {
		return height;
	}
	public IntegerProperty getWidth() {
		return width;
	}
}
