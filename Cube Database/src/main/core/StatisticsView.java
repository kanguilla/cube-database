package main.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class StatisticsView extends DynamicScene{
	
	Filter filter = new Filter("");
	Database db;
	PieChart setChart = new PieChart();
	
	public StatisticsView(Database db) {
		super(new Group());
		this.db = db;
		TabPane tabPane = new TabPane();
		
		filter.setSelection("select setCode, COUNT(*) as 'num' from cards where name like \"%\" ");
    	filter.setOrdering(" group by setCode order by num desc");
		TextField searchField = new TextField ();
        searchField.setPromptText("Search");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        	filter = new Filter(newValue);
        	filter.setSelection("select setCode, COUNT(*) as 'num' from cards where name like \"%\" ");
        	filter.setOrdering(" group by setCode order by num desc");
        	update();
        });
		
		Tab setTab = new Tab("Set Distribution");
		ArrayList<PieChart.Data> data = new ArrayList<PieChart.Data>();
		ResultSet rs = db.queryCube(filter.toSQL());
		try {
			while (rs.next()){
				data.add(new PieChart.Data(rs.getString("setCode"), rs.getInt("num")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setChart.setData(FXCollections.observableArrayList(data));
		setTab.setContent(setChart);
		
		Tab cmcTab = new Tab("CMC Distribution");
		
		
		
		
		
		tabPane.getTabs().addAll(setTab);
		
		
		final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(searchField, tabPane);
 
        ((Group) getRoot()).getChildren().addAll(vbox);
	}

	
	
	@Override
	public void update() {
		ArrayList<PieChart.Data> data = new ArrayList<PieChart.Data>();
		ResultSet rs = db.queryCube(filter.toSQL());
		try {
			while (rs.next()){
				data.add(new PieChart.Data(rs.getString("setCode"), rs.getInt("num")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setChart.setData(FXCollections.observableArrayList(data));
	}
}
