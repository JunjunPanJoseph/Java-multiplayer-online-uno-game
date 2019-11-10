package game.client;

import org.json.JSONObject;

public class ClientPlayer {
	private int id; 
	private String name;
	public ClientPlayer() {
		this.id = -1;
	}
	public ClientPlayer(JSONObject obj) {
		setPlayer(obj);
	}
	private boolean setPlayer(JSONObject obj) {
		try {
			int tmp_id = obj.getInt("id");
			String tmp_name = obj.getString("name");
			
			id = tmp_id;
			name = tmp_name;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.append("id", this.id);
		json.append("name", this.name);
		return json;
	}
	
}
