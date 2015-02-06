package application.map;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import application.Main;
import application.base.Controller;
import application.base.GUI;
import application.map.MapGUI;

public class MapController extends Controller{

	private MapGUI gui;

	
	public MapController(){

	}
	
	@Override
	public void makeGUI(){
		try{
			gui = new MapGUI();
			showGui();

//			gui.getScene().setOnDragOver(new EventHandler<DragEvent>(){
//
//				@Override
//				public void handle(DragEvent event) {
//					gui.requestFocus();
//				}
//				
//			});
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	@Override
	public void hideGui() {
		gui.hide();		
	}

	@Override
	public void showGui() {
		gui.show();
		
	}

	@Override
	public boolean isGuiShowing() {
		return gui.isShowing();
	}
	
}

