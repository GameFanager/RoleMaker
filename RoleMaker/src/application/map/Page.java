package application.map;

import javafx.beans.property.SimpleDoubleProperty;

public class Page{
	private String name;
	private String id;
	private SimpleDoubleProperty w, h; //stored in mm
//	private SimpleDoubleProperty dpiX, dpiY;
	private Unit unit;
	private Orientation or;
	
	public static enum Unit{cm,mm,inches, pt};
	public static enum Orientation{landscape, portrait };
	
	public Page(){
		this("A4",210.0, 297.0, Unit.mm, Orientation.portrait);
	}
	
	
	public Page(String name, double width, double height, /*double dx, double dy,*/ Unit u, Orientation ora){
		this.name = name;
		w = new SimpleDoubleProperty();
		h = new SimpleDoubleProperty();
	//	dpiX = new SimpleDoubleProperty();
	//	dpiY = new SimpleDoubleProperty();
		unit = u;
		
		converSet(w,width);
		converSet(h,height);
		or = ora;
		id = String.valueOf(hashCode());
		
		System.out.println("Page: W: "+getWidth(Unit.mm)+"mm, "+getWidth(Unit.pt)+" H: "+getHeight(Unit.mm)+"mm, "+getHeight(Unit.pt));
		
	}
	
	public void setName(String n){
		name = n;
		id = String.valueOf(hashCode());
	}
	public String getName(){return name;}
	
	public Orientation getOrientation(){return or;}
	public double getWidth(){return getWidth(getUnit());}
	public double getHeight(){return getHeight(getUnit());}
	public double getWidth(Unit u){ if (or==Orientation.portrait){return w.doubleValue()*getConvert(u);}else{return h.doubleValue()*getConvert(u);}}
	public double getHeight(Unit u){if (or==Orientation.portrait){return h.doubleValue()*getConvert(u);}else{return w.doubleValue()*getConvert(u);}}
	public Unit getUnit(){return unit;}
	public String getId(){return id;}
	
	public void setOrientation(Orientation ora){or = ora;}
	
	private void converSet(SimpleDoubleProperty p, Double n){
		p.setValue(n*getConvert());
	}
	
	private double getConvert(){
		return getConvert(getUnit());
	}
	
	private double getConvert(Unit unit){
		switch(unit){
		case mm: return 1;
		case inches: return 25.4;
		case cm: return 10;
		case pt: return 2.83464566929; // 1/((1/72)*25.4), pt = 1/72 inch.
		default: return 1;
		}
	}
	
	@Override
	public int hashCode(){
		String t = name+String.valueOf(w.doubleValue())+String.valueOf(h.doubleValue())+unit.toString()+or.toString();
		return t.hashCode();
	}
	
	public Page clone(){
		return clone("Temp");
	}
	
	public Page clone(String name){
		return  new Page(name, getWidth(), getHeight(), getUnit(), getOrientation());
	}
	
	
}
