package application.tile;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import application.Main;
import application.base.Controller;
import application.tile.TileGUI;

public class TileController extends Controller{

	private TileGUI gui;
	
	public TileController(){
		
	}
	
	@Override
	public void makeGUI(){
		try{
			gui = new TileGUI();
			showGui();
			
			double h = gui.getHeight();
			Rectangle2D sb = Screen.getPrimary().getBounds();
			
			gui.setX(50);
			gui.setY((sb.getHeight()-h)/5);
			
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
