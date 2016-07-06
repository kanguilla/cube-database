package main.core;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class FlowView extends FlowPane{
	
	private ArrayList<Image> data;
	
	public FlowView(){
		data = new ArrayList<Image>();
	}
	
	public void setContent(ArrayList<Image> data){
		this.getChildren().clear();
		this.data = data;
		for (Image i : data){
			this.getChildren().add(new ImageNode(i));
		}
		
	}
	
	public class ImageNode extends ImageView{
		public ImageNode(Image i){
			super();
			this.setImage(i);
			this.setFitWidth(200);
			this.setPreserveRatio(true);
			this.setSmooth(true);
			this.setCache(true);  
		}
	}
}
