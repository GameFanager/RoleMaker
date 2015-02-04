package application.map;

import java.util.HashMap;

import application.base.GUI;
import application.base.ShapeDropable;
import application.map.Page.Unit;
import application.tile.Tile;
import application.tile.Tile.Type;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class MapGUI extends GUI implements  ShapeDropable{
	@FXML
	private ScrollPane scrollV, scrollH, scrollCanvas;
	@FXML
	private AnchorPane rulerV, rulerH; 
	@FXML
	private AnchorPane canvas, anchorMain;
	@FXML
	private Label lblCPos, lblSPos;
	
	
	private SimpleBooleanProperty rulerVisible;
	private static final int rulerThickness = 30;

	private SimpleDoubleProperty zoomLevel;

	private boolean scroll = true;
	private boolean zoom = false;
	private static final Double zoomAmount = 0.1;
	private Double[] points;
	private PolyList grid, map; 
	
	private Shape shapeSelected;
	private SimpleStringProperty shapeHoveredXY;
	private Page page;
	
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
			System.out.println("shape is not hex");
			points = new Double[]{
					0.0,0.0,
					10.0,0.0,
					10.0,10.0,
					0.0,10.0
			};
			grid.setJig(false);
		}
		
		for (double d:points){
			System.out.print(d+",");
		}
		
	}
	
	private Polygon getPoly(){
		Polygon p = new Poly(points);
		return p;
	}
	
	@Override
	protected void init() {
		shapeHoveredXY = new SimpleStringProperty();
		grid = new PolyList();
		grid.setClickedListener(new GridShapeClicked());
		grid.setHoverListener(new GridShapeHovered());
		page = new Page();
		
	}
	
	public MapGUI() throws Exception{
		super("MapGUI.fxml");
	}
	
	@Override
	protected void create(){
		setShape(Type.HEX);
//		setShape(Type.SQUARE);
		setupRulers();
		setupZoom();
		setRulerVisiblity(false);
		
		
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				grid.fill(page, 40,62);
				canvas.getChildren().add(grid.getGroup());
				map = grid.clone();
				canvas.getChildren().add(map.getGroup());
			}
			
		});

		canvas.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {

				Polygon p = getPoly();
				p.setFill(Color.ALICEBLUE);
				
				shapeDropped(p);
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
		
		canvas.setPrefSize(page.getWidth(Unit.pt), page.getHeight(Unit.pt));
		
		scrollCanvas.requestFocus();

		System.out.println((scrollCanvas.getWidth()-canvas.getWidth())/2);
		
		this.widthProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
			//	canvas.setTranslateX(canvas.getTranslateX()+(oldValue.doubleValue()-newValue.doubleValue())/2);
			}
			
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
			//	canvas.setTranslateY(canvas.getTranslateY()+(oldValue.doubleValue()-newValue.doubleValue())/2);
			}
			
		});
	}
	
	private void setupZoom(){
		zoomLevel = new SimpleDoubleProperty(1);
		zoomLevel.addListener(new zoomChange());
		canvas.scaleXProperty().bind(zoomLevel);
		canvas.scaleYProperty().bind(zoomLevel);
		rulerH.scaleXProperty().bind(zoomLevel);
		rulerV.scaleYProperty().bind(zoomLevel);
	}
		
	private class rulerVisChange implements ChangeListener<Boolean>{
		@Override
		public void changed(ObservableValue<? extends Boolean> arg0,
				Boolean oldV, Boolean newV) {
			if (newV){
				AnchorPane.setLeftAnchor(scrollCanvas,(double) rulerThickness);
				AnchorPane.setTopAnchor(scrollCanvas,(double) rulerThickness);
				lblCPos.setTranslateX(rulerThickness);
				lblSPos.setTranslateX(rulerThickness);
			}else{
				AnchorPane.setLeftAnchor(scrollCanvas, 0.0);
				AnchorPane.setTopAnchor(scrollCanvas,0.0);
				lblCPos.setTranslateX(0);
				lblSPos.setTranslateX(0);
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
				
				Double dW = ((pW*newValue.doubleValue())-pW)*0.5;
				Double dH = ((pH*newValue.doubleValue())-pH)*0.5;
				
				canvas.setTranslateX(dW);
				canvas.setTranslateY(dH);
			}
		}
	}
	
	private  Shape getShapeSelected(){return shapeSelected;}
	private  void setShapeSelected(Shape n){
		shapeSelected = n; 
		String id = "NA";
		if (n!=null)	id = n.getId();
		lblSPos.setText("Selected: "+ id);
	}
	
	private void setShapeHoveredXY(String s){
		shapeHoveredXY.setValue(s);
		lblCPos.setText(" Mouse over: "+s);
	}
	
	private double[] breakTileName(String s){
		double x = Double.valueOf(s.substring(0, s.indexOf("/")));
		double y = Double.valueOf(s.substring(s.indexOf("/")+1));
		return new double[] {x,y};
	}
	
		
	
	private String createTileName(Double x, Double y){return (x)+"/"+(y);}
	
	class PolyList extends HashMap<String,Shape>{
		
		private Pane g;
		private static final long serialVersionUID = 1L;
		private boolean	jig = false;
		private int line = 0;
		private SimpleDoubleProperty w, h, bufL, bufT, scale;

		private EventHandler<MouseEvent> clicked;
		private EventHandler<MouseEvent> hover;
		public PolyList(){ this(false);}
		
		public PolyList(boolean jig){
			w = new SimpleDoubleProperty(10.0);
			h = new SimpleDoubleProperty(10.0);
			setJig(jig);
			bufL = new SimpleDoubleProperty(0.0);
			bufT = new SimpleDoubleProperty(0.0);
			scale = new SimpleDoubleProperty(1);
			g = new Pane();
			
			g.translateXProperty().bind(bufL);
			g.translateYProperty().bind(bufT);
			g.scaleXProperty().bind(scale);
			g.scaleYProperty().bind(scale);
		}
		
		public boolean fill(Page page, int x, int y){
			if (x<1 || y<1) return false;
			if (page==null) page = new Page();
			
			//we add one to simulate another row to make sure that there is a border.
			double nx =page.getWidth(Unit.pt)/((x+1)*w.get());
			double ny = page.getHeight(Unit.pt)/((y+1)*h.get());
			double s;
			for(int i=0; i<x; i++){
				for (int j=0; j<y; j++){
					if (!(i==0 && j%2!=line && jig)){
						Polygon p = getPoly();
						this.add(i, j, p);
					}
				}
			}
			
			if (nx<=ny){
				s=nx;
			}else{
				s=ny;
			}
			
			scale.set(s);
			
			nx = (s*x*w.get());
			ny = (s*y*h.get());
			if (jig){
				nx += s*h.get()*0.5;
			}
			bufferLeftProperty().set((page.getWidth(Unit.pt)-nx)/2);
			
			if ((h.get()*s)<=(page.getHeight(Unit.pt)-ny)/2){
				bufferTopProperty().set(h.get()*s);
			}else{
				bufferTopProperty().set((page.getHeight(Unit.pt)-ny)/2);
			}
			return true;
		}
		
		public Pane getGroup(){
			return g;
		}
		
		public double getScale(){
			return scale.get();
		}
		
		public void setShiftOnEven(boolean even){
			if (even){
				line = 0;
			}else{
				line = 1;
			}
		}
		
		public boolean isShiftOnEven(){return line==0;}
		
		public double getBufferTop(){return bufT.get();}
		public double getBufferLeft(){return bufL.get();}
		
		public void setBufLeft(Double l){bufL.setValue(l);}
		public void setBufTop(Double t){bufT.setValue(t);}
		
		public void setClickedListener(EventHandler<MouseEvent> clicked){this.clicked= clicked;}
		public void setHoverListener(EventHandler<MouseEvent> hover){this.hover = hover;}
		
		public SimpleDoubleProperty bufferLeftProperty(){return bufL;}
		public SimpleDoubleProperty bufferTopProperty(){return bufT;}
		public SimpleDoubleProperty shapeHeightProperty(){return h;}
		public SimpleDoubleProperty shapeWidthProperty(){return w;}
		public SimpleDoubleProperty scaleProperty(){ return scale;}
		
		public void setJig(boolean jig){
			this.jig = jig;
			if (jig) {h.setValue(7.5);}else{h.setValue(10.0);}
		}
		public boolean isJigged(){return jig;}
		
		private String genName(double x, double y){return createTileName(x,y);}
			//return (x)+"/"+(y);}
		
		public Shape getFromId(String s){
			return this.get(s);
		}
		
		private Shape translate(double x, double y, Shape s){
			double shift = 0.0;
			if (jig && (y%2==line)){
				shift = 0.5;
			}
			s.setTranslateX(w.get()*(x+shift));
			s.setTranslateY(h.get()*(y));
			return s;
		}
		
		public boolean add(double x, double y, Shape s){
			s.setId(genName(x,y));
			translate(x,y,s);
			this.put(genName(x,y), s);
			s.setOnMouseClicked(clicked);
			s.setOnMouseEntered(hover);
			g.getChildren().add(s);
			return true;
		}
		
		public Shape get(double x,double y){ return this.get(genName(x,y));}
		public void remove(double x, double y){Shape s = this.get(x, y); s.setOnMouseClicked(null); s.setOnMouseEntered(null);this.remove(s);}
		public void replace(double x, double y, Shape s){ this.remove(x, y); this.add(x, y, s);}
		public boolean existsAt(double x, double y){return this.containsKey(genName(x,y));}
		
		
		public ObservableList<Shape> getAll(){
			ObservableList<Shape> l = FXCollections.observableArrayList();
			String[] set = this.keySet().toArray(new String[this.keySet().size()]);
			for(String s:set){
				l.add(this.get(s));
			}
			return l;
		}
		
		public PolyList clone(){
			PolyList c = new PolyList();
			c.bufferLeftProperty().bindBidirectional(this.bufferLeftProperty());
			c.bufferTopProperty().bindBidirectional(this.bufferTopProperty());
			c.shapeHeightProperty().bindBidirectional(this.shapeHeightProperty());
			c.shapeWidthProperty().bindBidirectional(this.shapeWidthProperty());
			c.scaleProperty().bindBidirectional(this.scaleProperty());
			
			c.setJig(this.isJigged());
			c.setShiftOnEven(this.isShiftOnEven());
			return c;
		}
		
		public String toString(){
			return "leftB: "+bufferLeftProperty().doubleValue() +" topB: "+bufferTopProperty().doubleValue() + " shapeH: "+shapeHeightProperty().doubleValue() + " shapeW: "+shapeWidthProperty().doubleValue() +
					" scale: "+scaleProperty().doubleValue() + " jigged: "+ this.isJigged()+ " shifted: " + isShiftOnEven() +" size: "+this.size();
		}
		
		
		
	}
	private class GridShapeClicked implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent a) {
			if (!Shape.class.isAssignableFrom(a.getSource().getClass())){System.out.println(Shape.class.isAssignableFrom(a.getSource().getClass())); return;}
			Shape s = getShapeSelected();
			if (s!=null){
				s.setStroke(Poly.getMainBorder());
				s.setFill(Poly.getMainFill());
			}

			if (s==a.getSource()){setShapeSelected(null); return;}
			
			s = (Shape) a.getSource();
			s.setStroke(Poly.getClickedBorder());
			s.setFill(Poly.getClickedFill());
			s.toFront();
			setShapeSelected(s);
		}
	}
	
	private class GridShapeHovered implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent a) {
			if (!Shape.class.isAssignableFrom(a.getSource().getClass())){System.out.println(a.getSource().getClass()); return;}
			setShapeHoveredXY(((Shape) a.getSource()).getId());
		}
	}

	@Override
	public void shapeDropped(Shape dropped) {
		double[] xy = breakTileName(shapeHoveredXY.get());
		if (!map.existsAt(xy[0], xy[1])){
		map.add(xy[0],xy[1], dropped);
		System.out.println(shapeHoveredXY.get());
		System.out.println("Map: "+map.toString());
		System.out.println("Grid: "+grid.toString());
		canvas.requestLayout();
		}
	}

	@Override
	protected Node getDroppedItem() {
		return shapeSelected;
	}





}