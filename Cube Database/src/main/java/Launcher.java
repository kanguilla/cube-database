package main.java;
import javafx.application.*;
import javafx.stage.Stage;

public class Launcher extends Application{
    
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
    	new CardListView(new DatabaseMtg());
    }
}
