package application;

import javafx.beans.property.SimpleDoubleProperty;

public class Page{
	private String name;
	private String id;
	private SimpleDoubleProperty w, h;
//	private SimpleDoubleProperty dpiX, dpiY;
	private Unit unit;
	
	public static enum Unit{cm,mm,inches}; //,px};
	
	public Page(){
		this("A4",210.0, 297.0, Unit.mm);
	}
	
	
	public Page(String name, double width, double height, /*double dx, double dy,*/ Unit u){
		this.name = name;
		w = new SimpleDoubleProperty();
		h = new SimpleDoubleProperty();
	//	dpiX = new SimpleDoubleProperty();
	//	dpiY = new SimpleDoubleProperty();
		unit = u;
		
		converSet(w,width);
		converSet(h,height);
		
		id = String.valueOf(hashCode());
	}
	
	public void setName(String n){
		name = n;
		id = String.valueOf(hashCode());
	}
	
	public double getWidth(){ return w.doubleValue()/getConvert();}
	public double getHeight(){return h.doubleValue()/getConvert();}
	public Unit getUnit(){return unit;}
	public String getId(){return id;}
	
	private void converSet(SimpleDoubleProperty p, Double n){
		p.setValue(n*getConvert());
	}
	
	private double getConvert(){
		switch(unit){
		case mm: return 1;
		case inches: return 25.4;
		case cm: return 10;
		default: return 1;
		}
	}
	
	@Override
	public int hashCode(){
		String t = name+String.valueOf(w.doubleValue())+String.valueOf(h.doubleValue())+unit.toString();
		return t.hashCode();
	}
	
	public Page clone(){
		return clone("Temp");
	}
	
	public Page clone(String name){
		return  new Page(name, getWidth(), getHeight(), getUnit());
	}
	
	
}
