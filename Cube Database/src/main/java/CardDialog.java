package main.java;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CardDialog extends Stage{

	public CardDialog(Card card, Database database) {
		super();
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);
		VBox layout = new VBox();
		
		Text title = new Text(card.getName());
		TabPane tabPane = new TabPane();
		
		try {
			ResultSet rs = database.queryMtg("select setName, mciCode, number from contents join sets on sets.code=contents.setName where cardName=\"" + card.name + "\";");
			while(rs.next()){
				String mciCode = rs.getString("mciCode");
			    String number = rs.getString("number");
			    String setName = rs.getString("setName");
				System.out.println("   " + card.name + "...");
				
				CardTab tab = new CardTab(card, setName, mciCode, number);
				tab.setClosable(false);
				tabPane.getTabs().add(tab);
				if(tab.isSelected()){
					GridPane g = new GridPane();
					g.add(new ImageView(loadImage(tab.mciCode, tab.number)), 0, 0);
					Button btnAdd = new Button("Add this edition");
					btnAdd.setOnAction(new EventHandler<ActionEvent>() {
					    @Override public void handle(ActionEvent e) {
					        database.addToCube(tab.c, tab.s, 1);
					    }
					});
					g.add(btnAdd, 0, 1);
					tab.setContent(g);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
		    	CardTab tab = (CardTab) tabPane.getTabs().get(newValue.intValue());
		    	if (tab.getContent() == null){
		    		GridPane g = new GridPane();
					g.add(new ImageView(loadImage(tab.mciCode, tab.number)), 0, 0);
					Button btnAdd = new Button("Add this edition");
					btnAdd.setOnAction(new EventHandler<ActionEvent>() {
					    @Override public void handle(ActionEvent e) {
					        database.addToCube(tab.c, tab.s, 1);
					    }
					});
					g.add(btnAdd, 0, 1);
					tab.setContent(g);
		        }
		    }
		}); 
		
		layout.getChildren().addAll(title, tabPane);
		
		Scene dialogScene = new Scene(layout, 300, 520);
		setScene(dialogScene);

		
		
	}
	
	public Image loadImage(String mciCode, String number){
		String imageUrl = "http://magiccards.info/scans/en/" +mciCode+ "/" + number + ".jpg";
		URLConnection hc;
		try {
			hc = new URL(imageUrl).openConnection();
			hc.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
			hc.connect();
			return new Image(hc.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	class CardTab extends Tab{
		
		String mciCode, number, s;
		Card c;
		
		public CardTab(Card c, String s, String mciCode, String number){
			super(s);
			this.s = s;
			this.c = c;
			this.mciCode = mciCode;
			this.number = number;
		}
	}

}
