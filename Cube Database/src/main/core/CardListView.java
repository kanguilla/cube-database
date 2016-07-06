package main.core;
import java.util.ArrayList;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class CardListView extends DynamicScene{
	
	
	//Data members
	private Database database;
	private ObservableList<Card> data = FXCollections.observableArrayList();
	private Filter filter = new Filter("");
	
	//UI Members
	private Label title;
	private TabPane tabPane = new TabPane();
	private TableView<Card> table = new TableView<Card>();
	private FlowView flow = new FlowView();
	
	private ImageView previewPane;
	
	
	public CardListView(Database dm){
    	super(new Group());
    	this.database = dm;
    	data.addAll(database.getMtgCards(filter.toSQL()));

    	
    	previewPane = new ImageView();
        title = new Label("Cards");
        title.setFont(new Font("Arial", 20));
        
        TextField searchField = new TextField ();
        searchField.setPromptText("Search");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        	filter = new Filter(newValue);
        	update();
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
                    
                    if (item == null || empty || tr.getItem() == null) {
                        setText(null);
                        tr.setStyle("");
                    } else if (table.getSelectionModel().isSelected(tr.getIndex())){
                    	tr.setStyle("-fx-background-color: #000000; -fx-foreground-color: #ffffff;");
                    } else {
                    	setText(item);
                        tr.setStyle(tr.getItem().getCSSStyle());
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
					new CardDialog(row.getItem(), database).show();
				}else if (event.getButton() == MouseButton.SECONDARY){
					
					
					ContextMenu menu = new ContextMenu();
					
					MenuItem menuInspect = new MenuItem("Inspect");
					menuInspect.setOnAction(new EventHandler<ActionEvent>() {
					    @Override
					    public void handle(ActionEvent event) {
					    	new CardDialog(row.getItem(), database).show();
					    }
					});
					
					MenuItem menuAdd = new MenuItem("Add");
					menuAdd.setOnAction(new EventHandler<ActionEvent>() {
					    @Override
					    public void handle(ActionEvent event) {
					    	database.addToCube(row.getItem(), database.getMostRecentSet(row.getItem()), 1);
					    }
					});
					
					menu.getItems().addAll(menuInspect, menuAdd);
					menu.show(row, event.getScreenX(), event.getScreenY());
				}
			});
            return row ;
        });
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        	if (newSelection != null){
        		setPreview(newSelection);
        	}
        });
        

        
        Tab listTab = new Tab("List");
        listTab.setClosable(false);
        listTab.setContent(table);
        tabPane.getTabs().add(listTab);
        Tab flowTab = new Tab("Images");
        flowTab.setClosable(false);
        flowTab.setContent(flow);
        tabPane.getTabs().add(flowTab);
        Tab imageTab = new Tab("Preview");
        imageTab.setClosable(false);
        imageTab.setContent(previewPane);
        tabPane.getTabs().add(imageTab);

        ArrayList<Image> images = new ArrayList<Image>();
        for (int i = 0; i < 20; i++){
        	images.add(database.loadImage(data.get(i)));
        }
        flow.setContent(images);
        
        final GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.add(title, 0, 0);
        layout.add(searchField, 0, 1);
        layout.add(tabPane, 0, 2);
        ((Group) getRoot()).getChildren().addAll(layout);
        
        table.getSelectionModel().select(0);
    }

	@Override
	public void update() {
		data.setAll(database.getMtgCards(filter.toSQL()));
		table.setItems(data);
		table.getSelectionModel().select(0);
	}
	
	public void setPreview(Card c){
		previewPane.imageProperty().set(database.loadImage(c));
	}
}
