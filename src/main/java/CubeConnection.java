package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class CubeConnection {
	
	Connection database;
	Statement stat;
	String prototype = "select * from cards ";
	private Set<String> args = new TreeSet<String>();
	String ordering = "order by name asc";
	String textFilter = "";
	
	public CubeConnection(String address){
		try {
			Class.forName("org.sqlite.JDBC");
			database = DriverManager.getConnection("jdbc:sqlite:"+address);
			stat = database.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<oldCard> allCards(){
		return searchCards("select * from cards order by name asc;");
	}

	public void addColorFilter(String color){
		args.add(" color like \"%"+color+"%\" ");
	}
	
	public void removeColorFilter(String color){
		args.remove(" color like \"%"+color+"%\" ");
	}
	
	public void setTextFilter(String text) {
		textFilter = (" name like \"%"+text+"%\" ");
	}
	
	public ArrayList<oldCard> searchCards(String sqlQueryString){
		ArrayList<oldCard> cards = new ArrayList<oldCard>();
		try{
			ResultSet rs = stat.executeQuery(sqlQueryString);
			while (rs.next()) {
				oldCard card = new oldCard(
						rs.getString("name"),
						rs.getString("manaCost"),
						rs.getString("color"),
						rs.getString("types"),
						rs.getString("subtypes"),
						rs.getString("text"),
						rs.getString("flavor"),
						rs.getString("power"),
						rs.getString("toughness"));
				cards.add(card);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cards;
	}
	
	public ArrayList<oldCard> query() {
		ArrayList<oldCard> cards = new ArrayList<oldCard>();
		try{
			//Construct the query
			String sql = prototype;
			int counter = 0;
			for (String s : args){
				System.out.println("Arg# :" + s);
				if(counter>0){
					sql += "and ";
				}else{
					sql += "where ";
				}
				sql += s;
				counter++;
			}
			if(textFilter.length() > 0){
				if(counter == 0){
					sql += "where "+ textFilter;
				}else{
					sql += "and "+ textFilter;
				}
			}
			sql += (ordering + ";");
			System.out.println("Search as \"" + sql + "\"");
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				oldCard card = new oldCard(
						rs.getString("name"),
						rs.getString("manaCost"),
						rs.getString("color"),
						rs.getString("types"),
						rs.getString("subtypes"),
						rs.getString("text"),
						rs.getString("flavor"),
						rs.getString("power"),
						rs.getString("toughness"));
				cards.add(card);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cards;
	}
}
