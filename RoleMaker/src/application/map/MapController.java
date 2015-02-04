package application.map;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import application.Main;
import application.base.Controller;
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
			Main.addGUI(gui);
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
	
	private class MapShapeDropped implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent me) {
			
			//gui.shapeDropped(dropped);
			
		}
	}
	
	
}

