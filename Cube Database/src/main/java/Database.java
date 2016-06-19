package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
	
	DatabaseMtg mtg;
	Connection cube;
	Statement stat;
	
	public Database(){
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

	public boolean addCard(Card card, String setCode, int num){
		try {
			PreparedStatement ps = cube.prepareStatement("insert into cards (name, setCode, quantity) values (?, ?, ?);");
			ps.setString(1, card.name);
			ps.setString(2, setCode);
			ps.setInt(3, num);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public class DatabaseMtg {
		
		Connection database;
		Statement stat;
		
		public DatabaseMtg(){
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
		
		public ArrayList<Card> queryCards(String sql) {
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
							rs.getString("layout"));
					cards.add(card);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return cards;
		}
		
		public ArrayList<MtgSet> querySets(String sql) {
			ArrayList<MtgSet> sets = new ArrayList<MtgSet>();
			try {
				ResultSet rs = stat.executeQuery(sql);
				while (rs.next()) {
					MtgSet set = new MtgSet(
							rs.getString("name"),
							rs.getString("code"),
							rs.getString("mciCode"),
							rs.getString("release"),
							rs.getString("type"));
					sets.add(set);
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return sets;
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
	}

}
