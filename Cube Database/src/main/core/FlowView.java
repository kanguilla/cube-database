package main.core;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class FlowView extends Parent{
	
	private FlowPane flowPane = new FlowPane();
	private ArrayList<Card> local;
	private Button btn_Next = new Button();
	private Button btn_Prev = new Button();
	private int index = 0;
	private static int MAX_SIZE = 4;
	private CardListView parent;
	private int resultSize;
	
	
	public FlowView(CardListView parent){
		this.parent = parent;
		local = new ArrayList<Card>();
		HBox layout = new HBox();
		layout.setPadding(new Insets(5, 5, 5, 5));
		flowPane.setHgap(0);
		flowPane.setVgap(0);

		btn_Next.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	if (index + MAX_SIZE <= resultSize)index += MAX_SIZE;
		    	refresh();
		    }
		});
		btn_Prev.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	if (index - MAX_SIZE >= 0)index -= MAX_SIZE;
		    	refresh();
		    }
		});
		
		layout.getChildren().addAll(btn_Prev, flowPane, btn_Next);
		this.getChildren().add(layout);
	}
	
	public void update(){
		index = 0;
		resultSize = parent.getCards().length;
		refresh();
	}

	public void refresh(){
		
		flowPane.getChildren().clear();
		local.clear();
		for (int i = index; (i < MAX_SIZE + index && i < resultSize); i++){
			local.add(parent.getCards()[i]);
		}
		for (Card c : local){
			flowPane.getChildren().add(new CardImage(c));
		}
		btn_Next.setMinHeight(flowPane.getHeight());
		btn_Prev.setMinHeight(flowPane.getHeight());
	}
	
	public class CardImage extends ImageView{
		public CardImage(Card c){
			super();
			CardImage me = this;
			this.setImage(parent.getDatabase().loadImage(c));
			this.setFitWidth(200);
			this.setPreserveRatio(true);
			this.setSmooth(true);
			this.setCache(true);  

			this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			     @Override
			     public void handle(MouseEvent event) {
			         System.out.println("Tile pressed " + c.name);
			         new CardDialog(c, parent.getDatabase()).show();
			         event.consume();
			     }
			});
			
			this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			     @Override
			     public void handle(MouseEvent event) {

			     }
			});
			this.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			     @Override
			     public void handle(MouseEvent event) {

			     }
			});
		}
	}
}
