package main.java;
import javafx.application.*;
import javafx.stage.Stage;

public class Launcher extends Application{
    
	DatabaseMtg dm = new DatabaseMtg();
	DatabaseCube dc = new DatabaseCube();
	
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        stage.setScene(new CardListView(dm));
        stage.show();
    	
        Stage cubeStage = new Stage();
        cubeStage.setScene(new CubeListView(dm, dc));
        cubeStage.show();
    }
}
