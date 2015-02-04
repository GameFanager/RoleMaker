package application.base;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public abstract class GUI extends Stage{

	private String id;
	
	public void setID(String ID){id= ID;}
	public String getID(){return id;}
	
	abstract protected void init();
	
	abstract protected void create();
	
	abstract protected Node getDroppedItem();
	
	public GUI() throws Exception{
		this("GUI.fxml");
	}
	
	public GUI(String fxml) throws Exception{
		init();
		start(fxml);
		create();
	}
	
	private void start(String fxml) throws Exception{
		Region root = null;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
		fxmlLoader.setController(this);
		root = (Region) fxmlLoader.load();
		Scene scene = new Scene(root);
		this.setScene(scene);
		
		this.focusedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (newValue){
					
				}
			}
			
		});
		
	}
	
	
}
