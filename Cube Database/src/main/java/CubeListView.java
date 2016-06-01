package main.java;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CubeListView extends Scene{
	
	DatabaseMtg connection = new DatabaseMtg();
	DatabaseCube cube;
	ObservableList<CardEntry> data = FXCollections.observableArrayList();
	
	Label title;
	TableView<Card> table = new TableView<Card>();

	public CubeListView(DatabaseMtg dm, DatabaseCube dc){
		super(new Group());
		this.connection = dm;
		this.cube = dc;
     	
     	for (String s : dc.getNames()){
     		data.add(connection.queryCards("select * from cards where name like \"%" + s + "%\";").get(0));
     	}

        Label title = new Label("Cards");
        title.setFont(new Font("Arial", 20));
        
        TextField textField = new TextField ();
        textField.setPromptText("Search");
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
        	data = FXCollections.observableArrayList();
        	for (Card c : connection.queryCards("select * from cards where name like \"%" + newValue + "%\";")){
        		data.add(c);
        		
        	}
        	table.setItems(data);
        });
        
        table.setEditable(false);
        
        TableColumn<CardEntry, String> nameCol = new TableColumn<CardEntry, String>("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().card.name));
 
        
        TableColumn<CardEntry, String> setCol = new TableColumn<CardEntry, String>("Set");
        setCol.setMinWidth(100);
        setCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().set));
        
        TableColumn<CardEntry, Integer> numCol = new TableColumn<CardEntry, Integer>("Count");
        numCol.setMinWidth(100);
        numCol.setCellValueFactory();
        
        TableColumn<Card, String> colorCol = new TableColumn<Card, String>("Color");
        colorCol.setMinWidth(100);
        colorCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getColorString()));
        
        colorCol.setCellFactory(column -> {
            return new TableCell<Card, String>(){
            	@Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    TableRow<Card> tr = getTableRow();
                    
                    if (item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                    	setText(item);
                        if (item.contains("W")) {
                        	tr.setStyle("-fx-background-color: #ffffcc");
                        }else if (item.contains("U")) {
                        	tr.setStyle("-fx-background-color: #66ccff");
                        }else if (item.contains("B")) {
                        	tr.setStyle("-fx-background-color: #b3b3b3");
                        }else if (item.contains("R")) {
                        	tr.setStyle("-fx-background-color: #ff6666");
                        }else if (item.contains("G")) {
                        	tr.setStyle("-fx-background-color: #85e085");
                        } else {
                        	tr.setStyle("-fx-background-color: #c2c2a3");
                        }
                    }
                }
            };
        });
        
        
        table.setItems(data);
        table.getColumns().addAll(nameCol, costCol, ptCol, colorCol);
        
        table.setRowFactory( tv -> {
			TableRow<Card> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					new CardDialog(row.getItem(), connection).show();
				}
			});
            return row ;
        });
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(title, textField, table);
 
        ((Group) getRoot()).getChildren().addAll(vbox);
    }
	
	public class CardEntry{
		Card card;
		String set;
		int quantity = 0;
		public CardEntry(Card c, String set, int quantity){
			this.card = c;
			this.set = set;
			this.quantity = quantity;
		}
		
		public SimpleIntegerProperty getQuantityProperty(){
			SimpleIntegerProperty si = new SimpleIntegerProperty();
			si.set(this.quantity);
			return si;
		}
	}
}
