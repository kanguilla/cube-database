package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseCube {
	
	DatabaseMtg mtg;
	Connection cube;
	Statement stat;
	
	public DatabaseCube(){
		try {
			Class.forName("org.sqlite.JDBC");
			cube = DriverManager.getConnection("jdbc:sqlite:cube.db");
			mtg = new DatabaseMtg();
			stat = cube.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<CardEntry> queryCards(String sql) {
		ArrayList<CardEntry> cards = new ArrayList<CardEntry>();
		try {
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				Card card = mtg.queryCards("select * from cards where name=\"" +rs.getString("name")+ "\";").get(0);
				CardEntry cardEntry = new CardEntry(
						card,
						rs.getString("setCode"),
						rs.getInt("quantity"));
				cards.add(cardEntry);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cards;
	}
	
	public ResultSet query(String sql) {
		try {
			ResultSet rs = stat.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getNames(){
		ArrayList<String> cardNames = new ArrayList<String>();
		try {
     		ResultSet rs = query("select name from cards;");
				while(rs.next()){
					cardNames.add(rs.getString("name"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return cardNames;
	}
}
