package application;

public class MapController {

	private Thread guiThread; 
	private GUI gui;
	
	
	public MapController(String[] args){
		GUI.setArgs(args);
	}
	
	public void displayGUI(){
		gui = new GUI();
		guiThread= new Thread(gui);
		gui.run();
	}
	
	
}
