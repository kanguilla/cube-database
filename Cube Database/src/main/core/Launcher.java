package main.core;
import javafx.application.*;
import javafx.stage.Stage;

public class Launcher extends Application{
	ViewController views;
	Database dc;
	
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
    	ViewController views = new ViewController();
    	dc = new Database(views);
    	views.addView(new CardListView(dc));
        //views.addView(new CubeListView(dc));
        //views.addView(new ArchListView(dc));
        //views.addView(new StatisticsView(dc));
        views.show();
        views.updateAll();
    }
}
