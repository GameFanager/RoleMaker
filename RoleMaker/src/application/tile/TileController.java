package application.tile;

import application.tile.GUI;

public class TileController {

	private Thread guiThread; 
	private GUI gui;
	
	public TileController(String[] args){
		GUI.setArgs(args);
	}
	
	public void displayGUI(){
		gui = new GUI();
		guiThread= new Thread(gui);
		guiThread.start();
		System.out.println("Tiles Made");
	}	
}
