package application.tile;

import application.base.GUI;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class TileGUI extends GUI{
	
	@FXML
	private FlowPane flowTiles;
	
	private SimpleStringProperty tileSelected;
	private TileDragged draggEvent;
	
	@Override
	protected void init() {
		tileSelected = new SimpleStringProperty();
		tileSelected.addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				System.out.println(newValue);
			}
			
		});
	}
	
	
	public TileGUI() throws Exception{
		super("TileGUI.fxml");
	}
	
	@Override
	protected void create() {
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				displayTiles();
			}
			
		});
		
	}

	private void displayTiles(){
		draggEvent = new TileDragged();
		for (int i=0; i<100; i++){
			Image im = new Image("file:Resources/Koala.jpg");
			ImageView iv = new ImageView(im);
			flowTiles.getChildren().add(iv);
			iv.setOnDragDetected(draggEvent);
		}
	}
	
	private class TileDragged implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent me) {
			if (me.getSource().getClass() != ImageView.class) return;
			tileSelected.set(((ImageView)me.getSource()).getId());
		}
		
	}

	@Override
	protected Node getDroppedItem() {
			Polygon p = new Polygon();
			p.getPoints().addAll(new Double[]{
					5.0,0.0,
					10.0,2.5,
					10.0,7.5,
					5.0,10.0,
					0.0,7.5,
					0.0,2.5		
			});
		return p;
	}

	
	
	
}
