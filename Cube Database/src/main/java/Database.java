package main.java;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.scene.image.Image;
import main.java.CardDialog.CardTab;

public class Database {

	Connection mtg, cube;
	Statement cubeStatement, mtgStatement;
	ViewController vc;
	
	
	public Database(ViewController vc) {
		this.vc = vc;
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
			PreparedStatement ps = cube.prepareStatement("insert into cards (name, setCode, quantity) values (?, ?, ?);");
			ps.setString(1, card.name);
			ps.setString(2, setCode);
			ps.setInt(3, num);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		vc.updateAll();
		return true;
	}
	
	public boolean addToArchetype(Card c, Archetype a){
		try {
			PreparedStatement ps = cube.prepareStatement("insert into archMembers (archName, cardName) values (?, ?);");
			ps.setString(1, a.name);
			ps.setString(2, c.name);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		vc.updateAll();
		return true;
	}
	
	public ArrayList<Card> getCards(Archetype a){
		ArrayList<Card> cards = new ArrayList<Card>();
		try {
			ResultSet rs = cubeStatement.executeQuery("select cardName from archMembers where name=\"" + a.name + "\";");
			while (rs.next()) {
				Card card = getMtgCards("select * from cards where name=\"" + rs.getString("name") + "\";").get(0);
				cards.add(card);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cards;
	}
	
	public int getArchetypeSize(Archetype a){
		try {
			ResultSet rs = cubeStatement.executeQuery("SELECT COUNT(*) FROM archMembers where archName=\"" + a.name + "\";");
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public ArrayList<Archetype> getArchetypes() {
		ArrayList<Archetype> archetypes = new ArrayList<Archetype>();
		try {
			ResultSet rs = cubeStatement.executeQuery("select * from archetypes");
			while (rs.next()) {
				Archetype archetype = new Archetype(rs.getString("name"));
				archetypes.add(archetype);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return archetypes;
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
		ResultSet rs = queryMtg("select setName from contents where cardName=\"" +item.name+ "\";");
		try {
			return rs.getString("setName");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean createArchetype(Archetype a){
		try {
			PreparedStatement ps = cube.prepareStatement("insert or ignore into archetypes (name) values (?);");
			ps.setString(1, a.name);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		vc.updateAll();
		return true;
	}
	
	public boolean destroyArchetype(Archetype a){
		try {
			cubeStatement.execute("delete from archetypes where name=\"" + a.name + "\";");
			cubeStatement.execute("delete from archMembers where archName=\"" + a.name + "\";");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		vc.updateAll();
		return true;
	}
	
	public boolean removeFromCube(Card c, String setCode){
		try {
			cubeStatement.execute("delete from cards where name=\"" + c.name + "\" and setCode=\"" + setCode + "\";");
			cubeStatement.execute("delete from archMembers where cardName=\"" + c.name + "\";");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		vc.updateAll();
		return true;
	}
	
	public boolean removeFromCube(Card c, String setCode, int num){
		
			try {

				if (queryCube("select quantity from cards where name=\"" + c.name + "\" and setCode=\"" + setCode + "\";").getInt("quantity") - num <= 0){
					removeFromCube(c, setCode);
				}else{
				cubeStatement.executeUpdate("update cards set quantity where name=\"" + c.name + "\" and setCode=\"" + setCode + "\";");
				//cubeStatement.execute("delete from archMembers where archName=\"" + a.name + "\";");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			vc.updateAll();
			return true;
	}
	
	
	public Image loadImage(Card card){
		try {
			ResultSet rs = queryMtg("select setName, number from contents join sets on sets.code=contents.setName where cardName=\"" + card.name + "\";");
			return loadImage(rs.getString("mciCode"), rs.getString("number"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Image loadImage(Card card, String setCode){
		try {
			System.out.println(card.name + " from " + setCode);
			ResultSet rs = queryMtg("select mciCode, number from contents join sets on sets.code=contents.setName where cardName=\"" + card.name + "\" and code=\""+ setCode + "\";");
			String imageUrl = "http://magiccards.info/scans/en/" +rs.getString("mciCode")+ "/" + rs.getString("number") + ".jpg";
			URLConnection hc;
			try {
				hc = new URL(imageUrl).openConnection();
				hc.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
				hc.connect();
				return new Image(hc.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Image loadImage(String mciCode, String number){
		String imageUrl = "http://magiccards.info/scans/en/" +mciCode+ "/" + number + ".jpg";
		URLConnection hc;
		try {
			hc = new URL(imageUrl).openConnection();
			hc.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
			hc.connect();
			return new Image(hc.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
