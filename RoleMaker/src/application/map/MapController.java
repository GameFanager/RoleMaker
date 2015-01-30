package application.map;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import application.Main;
import application.map.GUI;

public class MapController{

	private Thread guiThread; 
	private GUI gui;
	
	public MapController(String[] args){
		GUI.setArgs(args);
	}
	
	public void displayGUI(){
		gui = new GUI();
		guiThread= new Thread(gui);
		guiThread.start();
		try {
			gui.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	
}

