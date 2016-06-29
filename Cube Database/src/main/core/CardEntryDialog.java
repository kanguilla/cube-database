package main.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CardEntryDialog extends Stage{

	Database database;
	
	public CardEntryDialog(CardEntry cardEntry, Database database) {
		super();
		this.database = database;
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		Text title = new Text(cardEntry.card.getName());
		ImageView cardImage = new ImageView(database.loadImage(cardEntry.card, cardEntry.set));
		
		GridPane layout = new GridPane();
		layout.add(title, 0, 0);
		layout.add(cardImage, 0, 1);
		
		Scene dialogScene = new Scene(layout, 300, 520);
		setScene(dialogScene);
		
	}
	
	
	class CardTab extends Tab{
		
		private String code;
		
		public CardTab(Card card, String setCode){
			super(setCode);
			System.out.println("Tab created: " + card.name + " " + setCode);
			
			this.code = setCode;
			setClosable(false);
			
			GridPane g = new GridPane();
			g.add(new ImageView(database.loadImage(card, setCode)), 0, 0);
			setContent(g);
		}
	}

}
