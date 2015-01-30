package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.sun.glass.ui.Application;

import application.map.MapController;
import application.tile.TileController;


public class Main {
	public static MapController mc;
	public static TileController tc;
	
	public static void main(String[] args) {
		mc = new MapController(args);
		tc = new TileController(args);
		mc.displayGUI();
	}

}
