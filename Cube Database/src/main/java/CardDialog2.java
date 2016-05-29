package main.java;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CardDialog2 extends Stage{

	public CardDialog2(Card card, DatabaseMtg connection) {
		super();
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);
		VBox layout = new VBox();
		
		Text title = new Text(card.getName());
		layout.getChildren().add(title);
		
		TabPane tabPane = new TabPane();
		
		
		try {
			ResultSet rs = connection.query("select setName, mciCode, number from contents join sets on sets.code=contents.setName where cardName=\"" + card.name + "\";");
			while(rs.next()){
				String mciCode = rs.getString("mciCode");
			    String number = rs.getString("number");
			    String setName = rs.getString("setName");
				System.out.println("   " + card.name + "...");
				String imageUrl = "http://magiccards.info/scans/en/" +mciCode+ "/" + number + ".jpg";
				URLConnection hc = new URL(imageUrl).openConnection();
				hc.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
				hc.connect();
				Image img = new Image(hc.getInputStream());
				
				Tab tab = new Tab(setName);
				tab.setContent(new ImageView(img));
				tab.setClosable(false);
				
				tabPane.getTabs().add(tab);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		layout.getChildren().add(tabPane);
		
		Scene dialogScene = new Scene(layout, 300, 480);
		setScene(dialogScene);

		
		
	}

}
