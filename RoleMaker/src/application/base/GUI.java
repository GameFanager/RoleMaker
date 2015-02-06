package application.base;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class GUI extends Stage{

	public static enum TYPE{DISPLAY, UTIL, NONE};
	
	private final String id;
	
	public String getID(){return id;}
	
	abstract public TYPE getType();
	
	abstract protected void init();
	
	abstract protected void create();
	
	abstract public Node getDroppedItem();
	
	abstract protected void focusChanged(boolean isFocused);
	
	public GUI() throws Exception{
		this("GUI.fxml");
	}
	
	public GUI(String fxml) throws Exception{
		init();
		start(fxml);
		create();
		id = String.valueOf(this.hashCode());
		Main.addGUI(this);
		
		this.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent arg0) {
				removeGUI();
			}
		});
		
		this.getScene().setOnDragOver(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {
				focusRequest();
			}
			
		});
		
		this.focusedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue){
					Main.changeFocusedGUI(getID());
					
				}
				focusChanged(newValue);
			}
			
		});
	}
	
	private void focusRequest(){
		this.requestFocus();
	}
	
	
	private void removeGUI(){
		Main.removeGUI(this);
	}
	
	private void start(String fxml) throws Exception{
		Region root = null;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
		fxmlLoader.setController(this);
		root = (Region) fxmlLoader.load();
		Scene scene = new Scene(root);
		this.setScene(scene);
	}
	
	
	
	
	
}
