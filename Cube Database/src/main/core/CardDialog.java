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

public class CardDialog extends Stage{

	Database database;
	
	public CardDialog(Card card, Database database) {
		super();
		this.database = database;
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		Text title = new Text(card.getName());
		TabPane tabPane = new TabPane();
		
		System.out.println("Creating a dialog with " + card.name);
		
		try {
			ResultSet rs = database.queryMtg("select setName, mciCode, number from contents join sets on contents.setName=sets.code where cardName=\"" + card.name + "\";");
			while(rs.next()){
			    String setCode = rs.getString("setName");	
			    String mciCode = rs.getString("mciCode");	
			    String number = rs.getString("number");
				tabPane.getTabs().add(new CardTab(card, setCode, mciCode, number));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		TextField numberField = new TextField("1");
		tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {

		    }
		}); 
		ChoiceBox<Archetype> cb = new ChoiceBox<Archetype>(FXCollections.observableArrayList(database.getArchetypes()));
		Button btnAdd = new Button("Add this edition");
		btnAdd.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	try{
			        database.addToCube(card, ((CardTab) tabPane.getSelectionModel().getSelectedItem()).code, Integer.parseInt(numberField.getText()));
			        database.addToArchetype(card, cb.getValue());
		    	}catch (NumberFormatException nfe){
		    		System.out.println("Wrong number format");
		    	}
		    	
		    }
		});
		
		cb.setTooltip(new Tooltip("(Optional) Select an archetype"));
		cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number value, Number newValue) {
				
			}
		});

		GridPane layout = new GridPane();
		layout.add(title, 0, 0);
		layout.add(tabPane, 0, 1);
		layout.add(btnAdd, 0, 2);
		layout.add(cb, 0, 3);
		layout.add(numberField, 0, 4);
		
		Scene dialogScene = new Scene(layout, 300, 520);
		setScene(dialogScene);
		
	}
	
	
	class CardTab extends Tab{
		
		private String code;
		
		public CardTab(Card card, String setCode, String mciCode, String number){
			super(setCode);
			System.out.println("Tab created: " + card.name + " " + setCode);
			
			this.code = setCode;
			setClosable(false);
			
			GridPane g = new GridPane();
			g.add(new ImageView(database.loadImage(mciCode, number)), 0, 0);
			setContent(g);
		}
	}

}
