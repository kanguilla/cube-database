package main.java;
import java.util.Optional;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class ArchListView extends Scene{
	
	Database database;
	ObservableList<Archetype> data = FXCollections.observableArrayList();
	
	Label title;
	TableView<Archetype> table = new TableView<Archetype>();

	public ArchListView(Database dc){
		super(new Group());
		this.database = dc;
        table.setEditable(false);
        
   		data.addAll(database.getArchetypes());
   		
        Label title = new Label("Archetypes");
        title.setFont(new Font("Arial", 20));
        
        Button add = new Button("Add");
        add.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	TextInputDialog dialog = new TextInputDialog("New Archetype");
		    	dialog.setHeaderText("Enter archetype name");
		    	Optional<String> result = dialog.showAndWait();
		    	if (result.isPresent()){
		    		System.out.println("New Archetype: " + result.get());
		    		database.createArchetype(new Archetype(result.get()));
		    	}
		    	data.setAll(database.getArchetypes());
		    	table.setItems(data);
		    }
		});
        Button remove = new Button("Remove");
        remove.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        ChoiceDialog dialog = new ChoiceDialog(database.getArchetypes().get(0), database.getArchetypes());
		        dialog.setHeaderText("Choose an archetype");
		        Optional<Object> result = dialog.showAndWait();
		        if (result.isPresent()){
		        	System.out.println("Removing Archetype: " + ((Archetype) result.get()).name);
		    		database.destroyArchetype(new Archetype(result.get().toString()));
		        }
		        data.setAll(database.getArchetypes());
		    	table.setItems(data);
		    }
        });
        
        
        TableColumn<Archetype, String> nameCol = new TableColumn<Archetype, String>("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().name));
        
        TableColumn<Archetype, Number> numCol = new TableColumn<Archetype, Number>("Size");
        numCol.setMinWidth(100);
        numCol.setCellValueFactory(c-> new SimpleIntegerProperty(database.getArchetypeSize(c.getValue())));
        
        table.setItems(data);
        table.getColumns().addAll(nameCol, numCol);
        
        table.setRowFactory( tv -> {
			TableRow<Archetype> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					
				}
			});
            return row ;
        });
 
        final GridPane g = new GridPane();
        g.setPadding(new Insets(10, 0, 0, 10));
        g.add(title, 0, 0, 2, 1);
        g.add(add, 0, 1);
        g.add(remove, 1, 1);
        g.add(table, 0 ,2, 2, 1);
 
        ((Group) getRoot()).getChildren().addAll(g);
    }
}
