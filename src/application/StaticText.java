package application;

public class StaticText {
	public static String gameTitle;
	public static String entry_gameStartLocal;
	public static String entry_gameStartOnline;
	public static String entry_setting;
	public static String entry_exit;
	public StaticText(String lang){
		switch (lang) {
		case "cn":
			setTextCN();
			break;
		case "en":
			setTextEN();
			break;
		}
	}
	public static void setTextEN() {
		gameTitle = "UNO Online";
		
		entry_gameStartLocal = "Local game";
		entry_gameStartOnline = "Online game";
		entry_setting = "Setting";
		entry_exit = "Exit";
	}

	public static void setTextCN() {
		gameTitle = "UNO Online";
		
		entry_gameStartLocal = "单机游戏";
		entry_gameStartOnline = "多人联机";
		entry_setting = "游戏设置";
		entry_exit = "退出游戏";
	}
	
}
