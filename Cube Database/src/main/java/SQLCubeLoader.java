package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import main.java.Card;
import main.java.Deck;

public class SQLCubeLoader {
	public static void main(String[] args) {
		MainView frame = null;
		try {
			Class.forName("org.sqlite.JDBC");
			Connection database = DriverManager.getConnection("jdbc:sqlite:cube.db");
			Statement stat = database.createStatement();

			
			//get all the decks
			String sqlQueryString = "select * from decks order by name asc;";
			System.out.println();
			System.out.println(sqlQueryString);
			ArrayList<Deck> decks = new ArrayList<Deck>();
			ResultSet rs = stat.executeQuery(sqlQueryString);
			while (rs.next()) {
				System.out.print("name: " + rs.getString("name"));
				System.out.println("arch: " + rs.getString("archetype"));
				Deck deck = new Deck(rs.getString("name"), rs.getString("archetype"));
				decks.add(deck);
			}
			rs.close();

			
			//get all the players
			sqlQueryString = "select * from players order by name asc;";
			System.out.println();
			System.out.println(sqlQueryString);
			ArrayList<Player> players = new ArrayList<Player>();
			rs = stat.executeQuery(sqlQueryString);
			while (rs.next()) {
				System.out.print("name: " + rs.getString("name"));
				System.out.println("id:" + rs.getString("id"));
				Player player = new Player(rs.getString("name"), rs.getInt("id"));
				
				PreparedStatement prep;
				prep = database.prepareStatement("select * from playLog where playerId=?;");
				prep.setInt(1, player.getId());
				ResultSet wins = prep.executeQuery();
				while (wins.next()){
					player.addWin(wins.getString("deckName"));
				}
				players.add(player);
			}
			rs.close();
			
			
			//get all the cards
			sqlQueryString = "select * from cards order by name asc;";
			rs = stat.executeQuery(sqlQueryString);
			System.out.println();
			System.out.println(sqlQueryString);
			ArrayList<Card> cards = new ArrayList<Card>();
			int DISPLAY_LIMIT = 100;
			int count = 0;
			while (rs.next() && count < DISPLAY_LIMIT) {
				System.out.println("name: " + rs.getString("name"));			
				Card card = new Card(
						rs.getString("name"),
						rs.getString("manaCost"),
						rs.getString("colors"),
						rs.getString("types"),
						rs.getString("subtypes"),
						rs.getString("text"),
						rs.getString("flavor"),
						rs.getString("power"),
						rs.getString("toughness"));
				cards.add(card);
				count++;
			}
			rs.close(); // close the query result table
			Card[] cardArray = new Card[1];
			cardArray = cards.toArray(cardArray);

			//get all the archetypes
			sqlQueryString = "select * from archetypes order by name asc;";
			System.out.println();
			System.out.println(sqlQueryString);
			ArrayList<Archetype> archetypes = new ArrayList<Archetype>();
			rs = stat.executeQuery(sqlQueryString);
			while (rs.next()) {
				System.out.print("name: " + rs.getString("name"));
				System.out.println("color: " + rs.getString("colors"));
				Archetype archetype = new Archetype(rs.getString("name"), rs.getString("colors"));
				
				PreparedStatement prep;
				prep = database.prepareStatement("select * from archetypeMembers where archetypeName=?;");
				prep.setString(1, archetype.name);
				ResultSet members = prep.executeQuery();
				while (members.next()){
					archetype.addCard(members.getString("cardName"));
				}
				archetypes.add(archetype);
			}
			rs.close();
			
			frame = new MainView("MTG CUBE", database, decks, cards, players, archetypes);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		frame.setVisible(true);
	}

}
