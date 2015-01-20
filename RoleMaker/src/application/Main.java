package application;


public class Main {
	public static MapController mc;
	public static void main(String[] args) {
		mc = new MapController(args);
		mc.displayGUI();
	}
}
