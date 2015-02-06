package application.map;

import java.util.HashMap;

import com.sun.glass.ui.Window;
import com.sun.javafx.collections.ObservableMapWrapper;

import application.base.GUI;
import application.base.ShapeDropable;
import application.map.Page.Unit;
import application.tile.Tile;
import application.tile.Tile.Type;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

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
	private Shape shapeHovered;
	private Shape shapeDragHovered;
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
			points = new Double[]{
					0.0,0.0,
					10.0,0.0,
					10.0,10.0,
					0.0,10.0
			};
			grid.setJig(false);
		}
	}
	
	private Polygon getPoly(){
		Polygon p = new Poly(points);
		return p;
	}
	
	@Override
	public TYPE getType(){return TYPE.DISPLAY;}
	
	@Override
	protected void init() {
		grid = new PolyList();
		grid.setClickedHandler(new GridShapeClicked());
		grid.setHoverHandler(new GridShapeHovered());
		grid.setDragHandler(new GridShapeDragEntered(), new GridShapeDragExit());
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
				map.getGroup().setPickOnBounds(false);
				
				canvas.getChildren().add(map.getGroup());
				
			}
			
		});

		canvas.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				MouseButton mb = arg0.getButton();
				
				if (mb==MouseButton.SECONDARY){
					Polygon p = getPoly();
					p.setFill(Color.ALICEBLUE);
					shapeDropped(p);
				}else if(mb==MouseButton.MIDDLE){
					System.out.println("Middle");
					deleteSelectedShape();
				}
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

		canvas.setOnDragOver(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
				event.acceptTransferModes(TransferMode.ANY);
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
	
	private  void setShapeSelected(Shape s){
		shapeSelected = s; 
		String id = "N/A";
		if (s!=null)	id = s.getId();
		lblSPos.setText("Selected: "+ id);
	}
	
	private  Shape getShapeSelected(){return shapeSelected;}
	
	private void setShapeHovered(Shape s){
		shapeHovered = s;
		String txt = "N/A";
		if (s!=null) txt = s.getId();
		lblCPos.setText(" Mouse over: "+txt);
	}
	private Shape getShapeHovered(){return shapeHovered;}
	
	private void setShapeDragHovered(Shape s){
		shapeDragHovered = s;
	}
	
	private Shape getShapeDragHovered(){ return shapeDragHovered;}
	
	
	
	private Double[] breakTileName(String s){
		if (!(s==null || s.equals(""))){
			try{
				double x = Double.valueOf(s.substring(0, s.indexOf("/")));
				double y = Double.valueOf(s.substring(s.indexOf("/")+1));
				return new Double[] {x,y};
			}catch(Exception e){
				System.out.println("Not a polyList grid id");
			}
		}
		return null;
	}
	
	private String createTileName(Double x, Double y){return (x)+"/"+(y);}
	
	class PolyList extends HashMap<String,Shape>{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2135008558033062520L;
		private Pane g;
		private boolean	jig = false;
		private int line = 0;
		private SimpleDoubleProperty w, h, bufL, bufT, scale;

		private EventHandler<MouseEvent> clicked;
		private EventHandler<MouseEvent> hover;
		private EventHandler<DragEvent> dragEnter;
		private EventHandler<DragEvent> dragExit;
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
			
			g.setPickOnBounds(true);
			
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
		
		public void setClickedHandler(EventHandler<MouseEvent> clicked){this.clicked= clicked;}
		public void setHoverHandler(EventHandler<MouseEvent> hover){this.hover = hover;}
		public void setDragHandler(EventHandler<DragEvent> dragEnter, EventHandler<DragEvent> dragExit){this.dragEnter = dragEnter; this.dragExit = dragExit;}
		
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
			setHandlers(s);
			g.getChildren().add(s);
			return true;
		}
		
		private void setHandlers(Shape s){
			s.setOnMouseClicked(clicked);
			s.setOnMouseEntered(hover);
			s.setOnDragEntered(dragEnter);
			s.setOnDragExited(dragExit);
		}
		
		private void clearHandlers(Shape s){
			s.setOnMouseClicked(null);
			s.setOnMouseEntered(null);
			s.setOnDragEntered(null);
			s.setOnDragExited(null);
		}
		
		public Shape get(double x,double y){ return this.get(genName(x,y));}
		public void removeAt(double x, double y){
			Shape s = this.get(x, y); 
			if (s==null) return;
			clearHandlers(s);
			this.remove(s.getId()); 
			s.setStroke(Color.TRANSPARENT);
			s.setFill(Color.TRANSPARENT);
			s=null;
		}
		
		public void replace(double x, double y, Shape s){ this.removeAt(x, y); this.add(x, y, s);}
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
			if (a.getButton()==MouseButton.MIDDLE) return;
			if (!Shape.class.isAssignableFrom(a.getSource().getClass())){System.out.println(Shape.class.isAssignableFrom(a.getSource().getClass())); return;}
			Shape s = getShapeSelected();
			deselectShape(s);
			if (s==a.getSource()){setShapeSelected(null); return;}
			s = (Shape) a.getSource();
			selectShape(s);
		}
	}
	
	private class GridShapeDragEntered implements EventHandler<DragEvent>{
		@Override
		public void handle(DragEvent a) {
			if(!Shape.class.isAssignableFrom(a.getSource().getClass())) return;
			Shape s = (Shape)a.getSource();
			setShapeDragHovered(s);
		}
	}

	private class GridShapeDragExit implements EventHandler<DragEvent>{
		@Override
		public void handle(DragEvent arg0) {
			//setShapeDragHovered(null);
		}
	}
	
	private class GridShapeHovered implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent a) {
			if (!Shape.class.isAssignableFrom(a.getSource().getClass())){System.out.println(a.getSource().getClass()); return;}
			setShapeHovered((Shape) a.getSource());
		}
	}
	
	public void deselectShape(Shape s){
		if (s!=null){
			s.setStroke(Poly.getMainBorder());
			s.setFill(Poly.getMainFill());
		}
	}
	
	public void selectShape(Shape s){
		s.setStroke(Poly.getClickedBorder());
		s.setFill(Poly.getClickedFill());
		s.toFront();
		setShapeSelected(s);
	}

	@Override
	public void shapeDropped(Node dropped) {
		if (dropped==null)return;
		if(!Shape.class.isAssignableFrom(dropped.getClass())){System.out.println(dropped.getClass()); return;}

		Double[] xy = getDropLocationCords();

		
		if (xy==null) xy = new Double[]{0.0,0.0};
		
		dropped.setMouseTransparent(true);
		map.add(xy[0],xy[1], (Shape)dropped);
		canvas.requestLayout();
	}

	private Double[] getHoveredCords(){
		Shape drag = getShapeDragHovered();
		if (drag==null) return null;
		return breakTileName(drag.getId());
	}
	
	private Double[] getSelectedCords(){
		Shape selected = getShapeSelected();
		if (selected == null) return null;
		return breakTileName(selected.getId());
	}
	
	private Double[] getDropLocationCords(){
		Double[] xy; 
		xy = getHoveredCords();
		if (xy!=null)return xy;
		xy = getSelectedCords();
		return xy;
	}
	
	
	@Override
	public Node getDroppedItem() {
		return getShapeSelected();
	}

	private void deleteSelectedShape(){
		Shape s = getShapeSelected();
		if(s == null) return;
		Double[] xy = breakTileName(s.getId());
		map.removeAt(xy[0],xy[1]);
		canvas.requestLayout();
	}

	@Override
	protected void focusChanged(boolean isFocused) {
	}

}
