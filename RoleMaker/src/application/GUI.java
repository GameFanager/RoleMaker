package application;

import java.util.HashMap;

import application.Tile.Type;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class GUI extends Application implements Runnable{
	private static String[] args;
	
	@FXML
	private ScrollPane scrollV, scrollH, scrollCanvas;
	@FXML
	private AnchorPane rulerV, rulerH; 
	@FXML
	private AnchorPane canvas;
	
	private Stage stage;
	
	private SimpleBooleanProperty rulerVisible;
	private static final int rulerThickness = 30;

	private SimpleDoubleProperty zoomLevel, xSize, ySize;

	public boolean isShowing(){return stage.isShowing();}
	public ReadOnlyBooleanProperty showingProperty(){return stage.showingProperty();}
	
	private boolean scroll = true;
	private boolean zoom = false;
	private static final Double zoomAmount = 0.1;
	private Double[] points = { 
		5.0,0.0,
		10.0,2.5,
		10.0,7.5,
		5.0,10.0,
		0.0,7.5,
		0.0,2.5
	};
	private PolyList grid; 
	
	private static Shape s;
	private static Color mainColor = Color.DARKGRAY;
	private static Color clickedColor = Color.DARKBLUE;
	
	public void setShape(Tile.Type s){
		if (s==Type.HEX){
			points = new Double[]{
					5.0,0.0,
					10.0,2.5,
					10.0,7.5,
					5.0,10.0,
					0.0,7.5,
					0.0,2.5		
			};
			grid.setJig(true);
			grid.setShiftOnEven(true);
		}else{
			points = new Double[]{
					0.0,0.0,
					10.0,0.0,
					10.0,10.0,
					0.0,10.0
			};
			grid.setJig(false);
		}
	}
	
	
	public static void setArgs(String[] arg){
		args = arg;
	}
	
	private Polygon getPoly(){
		Polygon p = new Polygon();
		p.getPoints().addAll(points);
		p.setFill(Color.TRANSPARENT);
		p.setStroke(mainColor);
		p.setStrokeWidth(1.0);
		return p;
	}
	
	public GUI(){
		xSize = new SimpleDoubleProperty(0);
		ySize = new SimpleDoubleProperty(0);
		
		grid = new PolyList();
		//grid.setOffSet(10.0, 10.0);
		grid.setBufLeft(7.5);
		grid.setBufTop(7.5);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		setShape(Type.HEX);
//		setShape(Type.SQUARE);
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
		
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
//				for (int i=0; i<98; i++){
//					for (int j=0; j<100; j++){
						
						for (int i=0; i<5; i++){
							for (int j=0; j<5; j++){
						Polygon p = getPoly();
						//canvas.getChildren().add(p);
						grid.add(i, j, p);
					}
				}
				canvas.getChildren().addAll(grid.getAll());
			}
			
		});

		Line l = new Line();
		l.setStartX(0);
		l.setEndX(10);
		canvas.getChildren().add(l);
		
		Line l2 = new Line();
		l2.setStartY(0);
		l2.setEndY(10);
		canvas.getChildren().add(l2);
		
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {

				
			}
			
		});
		
		canvas.setOnScroll(new EventHandler<ScrollEvent>(){

			@Override
			public void handle(ScrollEvent event) {
				if (!scroll ){
					if (zoom){
						double m = zoomAmount;
						if (event.getDeltaY()<0){
							m*=-1;
						}
						zoomLevel.set(zoomLevel.get()+m);
					}
					event.consume();
					return;
				}
			//	System.out.println("Scrolling");
			}
			
		});

	}

	public void setRulerVisiblity(boolean vis){rulerVisible.setValue(vis);}
		
	
	private void setupRulers(){
		rulerVisible = new SimpleBooleanProperty(true);
		rulerVisible.addListener(new rulerVisChange());

		
		canvas.widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> num,
					Number oldValue, Number newValue) {
				if (newValue.doubleValue()>oldValue.doubleValue()){
					//add
				}else if(newValue.doubleValue()<oldValue.doubleValue()){
					//delete
				}
			}
		});
		
		canvas.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> num,
					Number oldValue, Number newValue) {
				if (newValue.doubleValue()>oldValue.doubleValue()){
					//add
				}else if(newValue.doubleValue()<oldValue.doubleValue()){
					//delete
				}
			}
		});

		
		rulerH.prefWidthProperty().bind(canvas.prefWidthProperty());
		rulerH.minWidthProperty().bind(canvas.minWidthProperty());
		rulerH.maxWidthProperty().bind(canvas.maxWidthProperty());
		
		rulerV.prefHeightProperty().bind(canvas.prefHeightProperty());
		rulerV.minHeightProperty().bind(canvas.minHeightProperty());
		rulerV.maxHeightProperty().bind(canvas.maxHeightProperty());

		scrollV.vvalueProperty().bindBidirectional(scrollCanvas.vvalueProperty());
		scrollH.hvalueProperty().bindBidirectional(scrollCanvas.hvalueProperty());

		scrollV.vmaxProperty().bind(scrollCanvas.vmaxProperty());
		scrollH.hmaxProperty().bind(scrollCanvas.hmaxProperty());
		
		canvas.setPrefSize(1000, 1000);
		
		
		for (int i=0; i<100; i++){
			Polygon p = getPoly();
			p.setTranslateX(i*20);
			rulerH.getChildren().add(p);
		}
		
		for (int i=0; i<100; i++){
			Polygon p = getPoly();
			p.setTranslateY(i*20);
			rulerV.getChildren().add(p);
		}
		
		Line l = new Line();
		l.setStartY(990);
		l.setEndY(990);
		l.setStartX(0);
		l.setStartX(100);
		rulerV.getChildren().add(l);
		
		
		scrollCanvas.requestFocus();
		
		scrollCanvas.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent e) {
				KeyCode c = e.getCode();
				if (c==KeyCode.CONTROL){
					scroll=false;
					zoom=true;
				}else if(c==KeyCode.SHIFT){
				}
			}
			
		});
		
		scrollCanvas.setOnKeyReleased(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent e) {
				KeyCode c = e.getCode();
				if (c==KeyCode.CONTROL){
					scroll=true;
					zoom=false;
				}else if(c==KeyCode.SHIFT){
				//	System.out.println("no side scroll");
				}
			}
			
		});
		
		
	}
	
	private void setupZoom(){
		zoomLevel = new SimpleDoubleProperty(1);
		zoomLevel.addListener(new zoomChange());
		canvas.scaleXProperty().bind(zoomLevel);
		canvas.scaleYProperty().bind(zoomLevel);
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
				AnchorPane.setLeftAnchor(scrollCanvas,(double) rulerThickness);
				AnchorPane.setTopAnchor(scrollCanvas,(double) rulerThickness);
			}else{
				AnchorPane.setLeftAnchor(scrollCanvas, 0.0);
				AnchorPane.setTopAnchor(scrollCanvas,0.0);
			}
		}
	}

	private class zoomChange implements ChangeListener<Number>{
		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			if (newValue.doubleValue()<=0){
				zoomLevel.set(zoomAmount);
			}else{
				Double pW = canvas.getWidth();
				Double pH = canvas.getHeight();
				
				canvas.setTranslateX(((pW*newValue.doubleValue())-pW)*0.5);
				canvas.setTranslateY(((pH*newValue.doubleValue())-pH)*0.5);
			}
		}
	}
	
//	private class SideScroll implements EventHandler<ScrollEvent>{
//
//		@Override
//		public void handle(ScrollEvent e) {
//			System.out.println("Sidescrolling");
//			Double d = e.getDeltaY();
//			scrollCanvas.setHvalue(scrollCanvas.getHvalue()+d);
//			e.consume();
//			
//		}
//		
//	}
	
	private static Shape getShape(){return s;}
	private static void setShape(Shape n){s = n;}
	
	class PolyList extends HashMap<String,Shape>{
		
		private static final long serialVersionUID = 1L;
		private SimpleDoubleProperty xOff, yOff;
		private boolean	jig = false;
		private int line = 0;
		private double w, h, bufL, bufT;
		private ShapeClicked clicked = new ShapeClicked();
		
		public PolyList(){ this(0.0,0.0, false);}
		public PolyList(double x, double y){ this(x,y,false);}
		public PolyList(boolean jig){this(0.0,0.0,jig);}
		
		public PolyList(double x, double y, boolean jig){
			xOff = new SimpleDoubleProperty(x);
			yOff = new SimpleDoubleProperty(y);
			w = 10;
			h = 10;
			setJig(jig);
			bufL = 0.0;
			bufT = 0.0;
		}
		
		public void setShiftOnEven(boolean even){
			if (even){
				line = 0;
			}else{
				line = 1;
			}
		}
		
		public boolean isShiftOnEven(){return line==0;}
		
		public void addOffSet(double x,double y){
			xOff.set(xOff.get()+x);
			yOff.set(yOff.get()+y);
		}
		public void setOffSet(Double x, Double y){
			xOff.set(x);
			yOff.set(y);
		}
		
		public void setYoffSet(Double n){yOff.set(n);}
		public void setXoffSet(Double n){xOff.set(n);}
		
		public double getYoffSet(){return yOff.get();}
		public double getXoffSet(){return xOff.get();}
		
		public ReadOnlyDoubleProperty xOffset(){return ReadOnlyDoubleProperty.readOnlyDoubleProperty(xOff);}
		public ReadOnlyDoubleProperty yOffset(){return ReadOnlyDoubleProperty.readOnlyDoubleProperty(yOff);}
		
		public void setBufLeft(Double l){bufL = l;}
		public void setBufTop(Double t){bufT = t;}
		
		public void setJig(boolean jig){
			this.jig = jig;
			if (jig) {h=7.5;}else{h=10;}
			}
		public boolean isJigged(){return jig;}
		
		private String genName(double x, double y){
			return (x+xOff.get())+"/"+(y+yOff.get());}
		private Shape translate(double x, double y, Shape s){
			double shift = 0.0;
			if (jig && (y%2==line)){
				shift = 0.5;
			}
			s.setTranslateX(w*(x+xOff.get()+shift)+bufL);
			s.setTranslateY(h*(y+yOff.get())+bufT);
			return s;
		}
		public void add(double x, double y, Shape s){s.setId(genName(x,y));this.put(genName(x,y), translate(x,y,s)); s.setOnMouseClicked(clicked);}
		public Shape get(double x,double y){ return this.get(genName(x,y));}
		public void remove(double x, double y){Shape s = this.get(x, y); this.remove(s);}
		public void replace(double x, double y, Shape s){ this.remove(x, y); this.add(x, y, s);}
		
		public ObservableList<Shape> getAll(){
			ObservableList<Shape> l = FXCollections.observableArrayList();
			String[] set = this.keySet().toArray(new String[this.keySet().size()]);
			for(String s:set){
				l.add(this.get(s));
			}
			return l;
		}
		
	}
	
	private static class ShapeClicked implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent a) {
			if (a.getSource().getClass()!=Polygon.class){System.out.println(a.getSource().getClass()); return;}
			
			Shape s = getShape();
			if (s!=null){
				s.setStroke(mainColor);
			}
			
			s = (Shape) a.getSource();
			s.setStroke(clickedColor);
			s.toFront();
			setShape(s);
		}
		
	}
}
