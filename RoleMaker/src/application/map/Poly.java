package application.map;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Poly extends Polygon{
	
	private static Color mainBorder = Color.DARKGRAY;
	private static Color clickedBorder = Color.DARKBLUE;
	private static Color mainFill = Color.TRANSPARENT;
	private static Color clickedFill = Color.TRANSPARENT;
	

	public Poly(Double[] p){
		super();
		this.getPoints().addAll(p);
		this.setFill(mainFill);
		this.setStroke(mainBorder);
		this.setStrokeWidth(1.0);
	}

	public static Color getMainBorder() {
		return mainBorder;
	}
	public static Color getClickedBorder() {
		return clickedBorder;
	}
	public static Color getMainFill() {
		return mainFill;
	}
	public static Color getClickedFill() {
		return clickedFill;
	}
	public static void setMainBorder(Color mainBorder) {
		Poly.mainBorder = mainBorder;
	}
	public static void setClickedBorder(Color clickedBorder) {
		Poly.clickedBorder = clickedBorder;
	}
	public static void setMainFill(Color mainFill) {
		Poly.mainFill = mainFill;
	}
	public static void setClickedFill(Color clickedFill) {
		Poly.clickedFill = clickedFill;
	}
}
