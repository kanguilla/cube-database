package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

	Connection mtg, cube;
	Statement cubeStatement, mtgStatement;

	public Database() {
		try {
			Class.forName("org.sqlite.JDBC");
			cube = DriverManager.getConnection("jdbc:sqlite:cube.db");
			mtg = DriverManager.getConnection("jdbc:sqlite:mtg.db");
			cubeStatement = cube.createStatement();
			mtgStatement = mtg.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<CardEntry> getCubeCards(String sql) {
		ArrayList<CardEntry> cards = new ArrayList<CardEntry>();
		try {
			ResultSet rs = cubeStatement.executeQuery(sql);
			while (rs.next()) {
				Card card = getMtgCards("select * from cards where name=\"" + rs.getString("name") + "\";").get(0);
				CardEntry cardEntry = new CardEntry(card, rs.getString("setCode"), rs.getInt("quantity"));
				cards.add(cardEntry);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cards;
	}

	public boolean addToCube(Card card, String setCode, int num) {
		try {
			PreparedStatement ps = cube
					.prepareStatement("insert into cards (name, setCode, quantity) values (?, ?, ?);");
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

	public ArrayList<Card> getMtgCards(String sql) {
		ArrayList<Card> cards = new ArrayList<Card>();
		try {
			ResultSet rs = mtgStatement.executeQuery(sql);
			while (rs.next()) {
				Card card = new Card(rs.getString("name"), rs.getString("manaCost"), rs.getString("cmc"),
						rs.getString("colors"), rs.getString("colorIdentity"), rs.getString("types"),
						rs.getString("subtypes"), rs.getString("power"), rs.getString("toughness"),
						rs.getString("text"), rs.getString("layout"));
				cards.add(card);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return cards;
	}

	public ArrayList<MtgSet> getSets(String sql) {
		ArrayList<MtgSet> sets = new ArrayList<MtgSet>();
		try {
			ResultSet rs = mtgStatement.executeQuery(sql);
			while (rs.next()) {
				MtgSet set = new MtgSet(rs.getString("name"), rs.getString("code"), rs.getString("mciCode"),
						rs.getString("release"), rs.getString("type"));
				sets.add(set);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return sets;
	}

	//Generic queries
	public ResultSet queryCube(String sql) {
		try {
			ResultSet rs = cubeStatement.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public ResultSet queryMtg(String sql) {
		try {
			ResultSet rs = mtgStatement.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getMostRecentSet(Card item) {
		ResultSet rs = queryMtg("select setName from contents where name=\"" +item.name+ "\";");
		try {
			return rs.getString("setName");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
