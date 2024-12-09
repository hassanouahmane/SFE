package application;
 
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Main instance;
    private Users user = new Users();

       @Override
    public void start(Stage primaryStage) throws Exception {
    	   FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/login.fxml"));
           Parent root = loader.load();

        loginController loginController = loader.getController();
        
        Scene scene = new Scene(root, 800,601);
        
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("Consignation");
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void init() throws Exception {
        super.init();
        instance = this;
    }

	public Users getUser() {
		return this.user ;
	}
	 public static Main getInstance() {
			return instance;
	    }
}
