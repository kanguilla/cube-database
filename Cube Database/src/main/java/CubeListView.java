package main.java;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CubeListView extends Scene{
	
	Database database;
	ObservableList<CardEntry> data = FXCollections.observableArrayList();
	
	Label title;
	TableView<CardEntry> table = new TableView<CardEntry>();

	public CubeListView(Database dc){
		super(new Group());
		this.database = dc;
        table.setEditable(false);
        
   		data.addAll(database.getCubeCards("select * from cards;"));
   		
        Label title = new Label("Cards");
        title.setFont(new Font("Arial", 20));
        
        TextField textField = new TextField ();
        textField.setPromptText("Search");
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
        	data = FXCollections.observableArrayList();
        	for (CardEntry c : database.getCubeCards("select * from cards where name like \"%" + newValue + "%\";")){
        		data.add(c);
        	}
        	table.setItems(data);
        });
        
        TableColumn<CardEntry, String> nameCol = new TableColumn<CardEntry, String>("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().card.name));
 
        
        TableColumn<CardEntry, String> setCol = new TableColumn<CardEntry, String>("Set");
        setCol.setMinWidth(100);
        setCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().set));
        
        TableColumn<CardEntry, Number> numCol = new TableColumn<CardEntry, Number>("Count");
        numCol.setMinWidth(100);
        numCol.setCellValueFactory(c-> c.getValue().getQuantityProperty());
        
        TableColumn<CardEntry, String> colorCol = new TableColumn<CardEntry, String>("Color");
        colorCol.setMinWidth(100);
        colorCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().card.getColorString()));
        
        colorCol.setCellFactory(column -> {
            return new TableCell<CardEntry, String>(){
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
        table.getColumns().addAll(nameCol, setCol, numCol, colorCol);
        
        table.setRowFactory( tv -> {
			TableRow<CardEntry> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					new CardDialog(row.getItem().card, database).show();
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
}
