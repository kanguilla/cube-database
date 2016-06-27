package main.test;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.CardListView;
import main.java.Database;
import main.java.ViewController;

public class CardListTest extends Application{
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
        views.show();
    }
}