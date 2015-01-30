package application.tile;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class GUI implements Runnable{
	private static String[] args;

	private Stage stage;
	
	public boolean isShowing(){return stage.isShowing();}
	public ReadOnlyBooleanProperty showingProperty(){return stage.showingProperty();}	
	
	@Override
	public void run() {
		try {
			start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
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
	}

	public static void setArgs(String[] arg){
		args = arg;
	}
	
}
