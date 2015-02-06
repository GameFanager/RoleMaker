package application;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.stage.Stage;
import application.base.GUI;
import application.base.GUI.TYPE;
import application.map.MapController;
import application.tile.TileController;


public class Main extends Application{
	public static MapController mc;
	public static TileController tc;
	
	private static ObservableMap<String, GUI> guis;
	private static SimpleStringProperty focusedGUI;
	private static SimpleStringProperty preGUI;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		guis = FXCollections.observableHashMap();
		focusedGUI = new SimpleStringProperty();
		preGUI = new SimpleStringProperty();
		
		focusedGUI.addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				preGUI.setValue(oldValue);
			}
		});
		tc = new TileController();
		tc.makeGUI();
		
		mc = new MapController();
		mc.makeGUI();
		
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener(){

			@Override
			public void eventDispatched(AWTEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}, AWTEvent.MOUSE_EVENT_MASK);
		
	}
	
	public static void addGUI(GUI g){
		System.out.println(g.getID());
		guis.put(g.getID(),g);
	}
	
	public static void removeGUI(GUI g){
		guis.put(g.getID(), null);

		boolean found = false;
		String[] gs = guis.keySet().toArray(new String[guis.keySet().size()]);
		for (String s:gs){
			GUI temp = guis.get(s);
			if (temp!=null && temp.getType()==TYPE.DISPLAY){
				found = true;
				break;
			}
		}
		if (!found) System.exit(0);
	}
	
	public static void changeFocusedGUI(String id){
		focusedGUI.set(id);
	}
	
	public static GUI getFocusedGUI(){return guis.get(focusedGUI.get());}

	public static GUI getPreviousGUI(){return guis.get(preGUI.get());}
	
}
