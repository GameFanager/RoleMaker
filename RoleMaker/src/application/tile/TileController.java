package application.tile;

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
}
