package application.tile;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;

import application.Main;
import application.base.GUI;
import application.base.ShapeDropable;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class TileGUI extends GUI{
	
	@FXML
	private FlowPane flowTiles;
	@FXML
	private ToggleButton tbtnPaintbrush;
	
	
	private SimpleBooleanProperty paint;
	
	private SimpleStringProperty tileSelected;
	private TileDragged dragStarted;
	private TileDropped dragEnded;
	private Node selected;
	
	private static Double[] basePoints = new Double[]{
			5.0,0.0,
			10.0,2.5,
			10.0,7.5,
			5.0,10.0,
			0.0,7.5,
			0.0,2.5		
	};
	
	private static Double[] displayPoints;
	
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
		
		displayPoints = new Double[basePoints.length];
		for (int i=0; i<basePoints.length; i++){
			displayPoints[i] = basePoints[i]*4;
		}
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
		
		paint = new SimpleBooleanProperty();
		paint.bind(tbtnPaintbrush.pressedProperty());
		
		paint.addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {

					
			}
		});
	}

	
	private void displayTiles(){
		dragStarted = new TileDragged();
		dragEnded = new TileDropped();
		for(int r=0; r<256; r+=25){
			for(int g=0; g<256; g+=25){
				for(int b=0; b<256; b+=25){
					Polygon p = getDisplayPoly();
					p.setStroke(Color.TRANSPARENT);
					p.setFill(Color.rgb(r,g,b));
					p.setOnDragDetected(dragStarted);
					p.setOnDragDone(dragEnded);
					p.setPickOnBounds(false);
					flowTiles.getChildren().add(p);
				}
			}
		}
	}

	private class TileDragged implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent me) {
			tileSelected.set(((Node)me.getSource()).getId());
			Dragboard db = ((Node)me.getSource()).startDragAndDrop(TransferMode.ANY);
			ClipboardContent con = new ClipboardContent();
			con.putString(getID());
			db.setContent(con);
			selected =  (Node) me.getSource();
			me.consume();
			System.out.println("Dragging");
		}
		
	}
	
	private class TileDropped implements EventHandler<DragEvent>{
		@Override
		public void handle(DragEvent de) {
			System.out.println(de.getAcceptedTransferMode());
//			if (de.getAcceptedTransferMode().compareTo(TransferMode.COPY)){
			dropTile();
//			}
		}
	}
	
	private void dropTile(){
		System.out.println("Dropped");
		GUI g = Main.getFocusedGUI();
		if (!ShapeDropable.class.isAssignableFrom(g.getClass())) {System.out.println(g.getClass());return;}
		ShapeDropable sd = (ShapeDropable) g;
		sd.shapeDropped(getDroppedItem());
	}
	
	@Override
	public Node getDroppedItem() {
		if (Polygon.class.isAssignableFrom(selected.getClass())){
			Polygon d = (Polygon) selected;
			Polygon p = getPoly();
			p.setStroke(d.getStroke());
			p.setFill(d.getFill());
			p.setStrokeWidth(d.getStrokeWidth());
			return p;
		}else{
			return selected;
		}
	}
	
	private Polygon getBasePoly(){
		Polygon p = new Polygon();
		p.setFill(Color.TRANSPARENT);
		p.setStroke(Color.BLACK);
		return p;
	}
	
	private Polygon getDisplayPoly(){
		Polygon p = getBasePoly();
		p.getPoints().addAll(displayPoints);
		return p;
	}

	private Polygon getPoly(){
		Polygon p = getBasePoly();
		p.getPoints().addAll(basePoints);
		return p;
	}
	
	@Override
	public TYPE getType() {
		return TYPE.UTIL;
	}


	@Override
	protected void focusChanged(boolean isFocused) {
		// TODO Auto-generated method stub
		//do nothing, don't really care....
	}

	
	
}
