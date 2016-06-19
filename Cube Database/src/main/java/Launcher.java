package main.java;
import javafx.application.*;
import javafx.stage.Stage;

public class Launcher extends Application{
    
	Database dc = new Database();
	
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        stage.setScene(new CardListView(dc));
        stage.show();
    	
        Stage cubeStage = new Stage();
        cubeStage.setScene(new CubeListView(dc));
        cubeStage.show();
    }
}
