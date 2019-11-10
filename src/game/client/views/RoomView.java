package game.client.views;

import game.client.ClientView;
import javafx.stage.Stage;

public class RoomView extends ClientGroupView {
	private SelectView selectView;
	public RoomView(ClientView view, Stage stage) {
		super(view, stage);
	}
	/**
	 * @param selectView the selectView to set
	 */
	public void setSelectView(SelectView selectView) {
		this.selectView = selectView;
	}
	
}
