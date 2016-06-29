package main.core;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CubeListView extends DynamicScene{
	
	Database database;
	ObservableList<CardEntry> data = FXCollections.observableArrayList();
	
	MenuBar menu;
	Label title;
	
	TableView<CardEntry> table = new TableView<CardEntry>();

	public CubeListView(Database dc){
		super(new Group());
		this.database = dc;
        table.setEditable(false);
        
   		data.addAll(database.getCubeCards("select * from cards;"));
   		
   		menu = new MenuBar();
   		Menu m_cube = new Menu("Cube");
   		MenuItem m_new = new Menu("New");
   		m_new.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	TextInputDialog dialog = new TextInputDialog("New Cube");
		    	dialog.setHeaderText("Enter cube name");
		    	Optional<String> result = dialog.showAndWait();
		    	if (result.isPresent()){
		    		database.setSource(result.get());
		    	}
            }
        });   
   		m_cube.getItems().add(m_new);
   		menu.getMenus().add(m_cube);
   		
        title = new Label("Cube");
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
					new CardEntryDialog(row.getItem(), database).show();
				}else if (event.getButton() == MouseButton.SECONDARY){
					
					
					ContextMenu menu = new ContextMenu();
					
					MenuItem menuInspect = new MenuItem("Inspect");
					menuInspect.setOnAction(new EventHandler<ActionEvent>() {
					    @Override
					    public void handle(ActionEvent event) {
					    	new CardDialog(row.getItem().card, database).show();
					    }
					});
					
					MenuItem menuRemove = new MenuItem("Remove");
					menuRemove.setOnAction(new EventHandler<ActionEvent>() {
					    @Override
					    public void handle(ActionEvent event) {
					    	if (row.getItem() != null){
					    		database.removeFromCube(row.getItem().card, row.getItem().set);
					    	}
					    }
					});
					
					menu.getItems().addAll(menuInspect, menuRemove);
					menu.show(row, event.getScreenX(), event.getScreenY());
				}
			});
            return row ;
        });
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(menu, title, textField, table);
 
        ((Group) getRoot()).getChildren().addAll(vbox);
    }

	@Override
	public void update() {
		data.setAll(database.getCubeCards("select * from cards;"));
		table.setItems(data);
	}
}
