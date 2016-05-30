package main.java;
import javafx.application.*;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainView extends Application{
	
	DatabaseMtg connection = new DatabaseMtg();
	TableView<Card> table = new TableView<Card>();
    ObservableList<Card> data = FXCollections.observableArrayList();
   
    public static void main(String[] args) {
    
    	
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
    	
    	for (Card c : connection.queryCards("select * from cards;")){
    		data.add(c);
    	}
    	
    	
        Scene scene = new Scene(new Group());
        stage.setTitle("Hedron");
        stage.setWidth(450);
        stage.setHeight(500);
 
        Label label = new Label("Cards");
        label.setFont(new Font("Arial", 20));
 
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
        
        TableColumn<Card, String> nameCol = new TableColumn<Card, String>("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<Card, String>("name"));
 
        
        TableColumn<Card, String> costCol = new TableColumn<Card, String>("Cost");
        costCol.setMinWidth(100);
        costCol.setCellValueFactory(new PropertyValueFactory<Card, String>("cost"));
        
        TableColumn<Card, String> ptCol = new TableColumn<Card, String>("P/T");
        ptCol.setMinWidth(100);
        ptCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getPT()));
        
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
        vbox.getChildren().addAll(label, textField, table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    }
}