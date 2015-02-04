package application;

import java.util.Observable;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import application.base.GUI;
import application.map.MapController;
import application.tile.TileController;


public class Main extends Application{
	public static MapController mc;
	public static TileController tc;
	
	private static ObservableList<GUI> guis;
	private static SimpleStringProperty focusedGUI;
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		guis = FXCollections.observableArrayList();
		focusedGUI = new SimpleStringProperty();
		
		mc = new MapController();
		
		tc = new TileController();
	
		mc.makeGUI();
		tc.makeGUI();
	}
	
	public static void addGUI(GUI g){
		guis.add(g);
		System.out.println(guis.hashCode());
	}
	
	public static void removeGUI(GUI g){
		guis.remove(g);
	}
	
	public static void changeFocusedGUI(String id){
		focusedGUI.set(id);
	}

}
