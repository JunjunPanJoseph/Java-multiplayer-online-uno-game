package game.client.views;

import game.client.ClientModel;
import game.client.ClientView;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Group;
import javafx.stage.Stage;

public abstract class ClientGroupView {
	private Group root;
	private Stage stage;
	private ClientView view;
	public ClientGroupView(ClientView view, Stage stage){
		this.view = view;
		this.stage = stage;
		this.root = new Group();
	}
	public void setModel(ClientModel model) {
		view.setModel(model);
	}
	public ClientModel getModel() {
		return view.getModel();
	}
	public void log(String str) {
		view.log(str);
	}
	public void errorMsg(String msg) {
		view.errorMsg(msg);
	}
	public ReadOnlyDoubleProperty getStageWidthProperty() {
		return stage.widthProperty();
	}
	public ReadOnlyDoubleProperty getStageHeightProperty() {
		return stage.heightProperty();
	}
	public Group getRoot() {
		return root;
	}
	public void start() {
		view.addRoot(getRoot());
	}
}
