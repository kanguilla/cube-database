package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MtgDatabase {
	
	Connection database;
	Statement stat;
	
	public MtgDatabase(){
		try {
			Class.forName("org.sqlite.JDBC");
			database = DriverManager.getConnection("jdbc:sqlite:mtg.db");
			stat = database.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Card> query(String sql) {
		ArrayList<Card> cards = new ArrayList<Card>();
		try {
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				Card card = new Card(
						rs.getString("name"),
						rs.getString("manaCost"),
						rs.getString("cmc"),
						rs.getString("colors"),
						rs.getString("colorIdentity"),
						rs.getString("types"),
						rs.getString("subtypes"),
						rs.getString("power"),
						rs.getString("toughness"),
						rs.getString("text"),
						rs.getString("rarity"),
						rs.getString("artist"),
						rs.getString("flavor"),
						rs.getString("id"),
						rs.getString("number"),
						rs.getString("layout"),
						rs.getString("mset"));
				cards.add(card);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cards;
	}
}
