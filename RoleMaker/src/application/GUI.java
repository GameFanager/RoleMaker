package application;

import application.Tile.Shape;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class GUI extends Application implements Runnable{
	private static String[] args;
	
	@FXML
	private ScrollPane scrollV, scrollH, scrollCanvas;
	@FXML
	private AnchorPane rulerV, rulerH; 
	@FXML
	private AnchorPane anchorCanvas;
	
	private Stage stage;
	
	private SimpleBooleanProperty rulerVisible;
	private static final int rulerThickness = 30;
	private SimpleIntegerProperty xSize, ySize;
	private SimpleDoubleProperty zoomLevel;
	
	private static Double[] points = { 
		5.0,0.0,
		10.0,2.5,
		10.0,7.5,
		5.0,10.0,
		0.0,7.5,
		0.0,2.5
	};
	
	public static void setShape(Tile.Shape s){
		if (s==Shape.HEX){
			points = new Double[]{
					5.0,0.0,
					10.0,2.5,
					10.0,7.5,
					5.0,10.0,
					0.0,7.5,
					0.0,2.5		
			};
		}else{
			points = new Double[]{
					0.0,0.0,
					10.0,0.0,
					10.0,10.0,
					0.0,10.0
			};
		}
	}
	
	
	public static void setArgs(String[] arg){
		args = arg;
	}
	
	private Polygon getPoly(){
		Polygon p = new Polygon();
		p.getPoints().addAll(points);
		p.setFill(null);
		p.setStroke(Color.DARKGRAY);
//		p.setStrokeWidth(1/zoomLevel.doubleValue());
		
		return p;
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		Region root = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUI.fxml"));
			fxmlLoader.setController(this);
			root = (Region) fxmlLoader.load();
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
		setupRulers();
		setupZoom();
		
		Polygon poly = new Polygon();
		poly.getPoints().addAll(points);
		poly.setScaleX(5);
		poly.setScaleY(5);
		poly.setTranslateX(100);
		poly.setTranslateY(100);
		anchorCanvas.getChildren().add(poly);
		
		Polygon poly2 = new Polygon();
		poly2.getPoints().addAll(new Double[]{
			0.0,0.0,
			10.0,0.0,
			10.0,10.0,
			0.0,10.0
		});
		poly2.setScaleX(5);
		poly2.setScaleY(5);
		poly2.setTranslateX(160);
		poly2.setTranslateY(100);
		anchorCanvas.getChildren().add(poly2);
		
	}

	private void setupRulers(){
		rulerVisible = new SimpleBooleanProperty();
		scrollH.visibleProperty().bindBidirectional(rulerVisible);
		scrollV.visibleProperty().bindBidirectional(rulerVisible);
		rulerVisible.addListener(new rulerVisChange());
	}
	
	private void setupZoom(){
		zoomLevel = new SimpleDoubleProperty(1);
		zoomLevel.addListener(new zoomChange());
		anchorCanvas.scaleXProperty().bind(zoomLevel);
		anchorCanvas.scaleYProperty().bind(zoomLevel);
	}
	
	
	@Override
	public void run() {
		launch(args);
	}
	
	
	private class rulerVisChange implements ChangeListener<Boolean>{
		@Override
		public void changed(ObservableValue<? extends Boolean> arg0,
				Boolean oldV, Boolean newV) {
			if (newV){
				AnchorPane.setLeftAnchor(scrollCanvas, 0.0);
				AnchorPane.setTopAnchor(scrollCanvas,0.0);
			}else{
				AnchorPane.setLeftAnchor(scrollCanvas,(double) rulerThickness);
				AnchorPane.setTopAnchor(scrollCanvas,(double) rulerThickness);
			}
			
		}
	}

	private class zoomChange implements ChangeListener<Number>{
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			if (newValue.doubleValue()<1){
				zoomLevel.set(1);
			}else{
				Double pW = anchorCanvas.getScene().getWidth();
				Double pH = anchorCanvas.getScene().getHeight();
			}
		}
	}
}
