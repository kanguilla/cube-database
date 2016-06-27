package main.java;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewController {
	
	ArrayList<Stage> stages = new ArrayList<Stage>();
	
	public ViewController(){
		
		
	}
	
	public void addView(Scene s){
		Stage stage = new Stage();
		stage.setScene(s);
		stage.sizeToScene();
		stages.add(stage);
	}
	
	public void show(){
		for (Stage s : stages){
			s.show();
		}
	}
	
	public void updateAll(){
		for (Stage s : stages){
			((DynamicScene)  (s.getScene())).update();
			s.sizeToScene();
		}
	}
}
