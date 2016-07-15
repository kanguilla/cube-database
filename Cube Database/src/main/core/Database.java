package main.core;

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
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class Database {

	private Connection mtg, cube;
	private Statement cubeStatement, mtgStatement;
	private ViewController vc;
	
	private HashMap<String, Image> imageCache = new HashMap<String, Image>();
	
	
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
				Card card = new Card(rs.getString("name"), rs.getString("manaCost"), rs.getString("cmc"),
						rs.getString("colors"), rs.getString("colorIdentity"), rs.getString("types"),
						rs.getString("subtypes"), rs.getString("power"), rs.getString("toughness"),
						rs.getString("text"), rs.getString("layout"));
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
			
//			+ "name varchar(50),"
//					+ "manaCost varchar(10),"
//					+ "cmc varchar(10),"
//					+ "colors varchar(10),"
//					+ "colorIdentity varchar(10),"
//					+ "types varchar(50),"
//					+ "subtypes varchar(50),"
//					+ "power varchar (3),"
//					+ "toughness varchar (3),"
//					+ "text varchar (300),"
//					+ "layout varchar (20),"
//					+ "setCode varchar (10),"
//					+ "quantity int,"
			
			PreparedStatement ps = cube.prepareStatement("insert or ignore into cards values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			ps.setString(1, card.name);
			ps.setString(2, card.manaCost);
			ps.setString(3,  card.cmc);
			ps.setString(4, String.join(",",card.colors));
			ps.setString(5, String.join(",",card.colorIdentity));
			ps.setString(6, String.join(",", card.types));
			ps.setString(7, String.join(",", card.subtypes));
			ps.setString(8, card.power);
			ps.setString(9, card.toughness);
			ps.setString(10, card.text);
			ps.setString(11, card.layout);
			ps.setString(12, setCode);
			ps.setInt(13, num);
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
		try {
			ResultSet rs = queryMtg("select setName from contents where cardName='"+ item.name.replace("'", "''")+ "';");
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
		return loadImage(card, getMostRecentSet(card));
	}
	
	public Image loadImage(Card card, String setCode){
		System.out.println("Attempting image load: " + card.name + " from " + setCode);
		try {
			ResultSet rs = queryMtg("select mciCode, number from contents join sets on sets.code=contents.setName where cardName='" + card.name.replace("'", "''") + "' and code='"+ setCode + "';");
			String imageUrl = "http://magiccards.info/scans/en/" +rs.getString("mciCode")+ "/" + rs.getString("number") + ".jpg";
			rs.close();
			if (imageCache.containsKey(imageUrl)){
				System.out.println("  Image found in cache.");
				return imageCache.get(imageUrl);
			}else{	
				System.out.println("  No image found. Fetching...");
				return getImageFromURL(imageUrl);
			}
		} catch (SQLException e) {
			System.out.println("  Card does not exist.");
			return null;
		}
	}
	public Image loadImage(String mciCode, String number){
		
		Thread thread = new Thread(){
			@Override
			public void run(){
				System.out.println("This is a thread");
				String imageUrl = "http://magiccards.info/scans/en/" +mciCode+ "/" + number + ".jpg";
			}
		};
		//thread.start();
		return getImageFromURL("http://magiccards.info/scans/en/" +mciCode+ "/" + number + ".jpg");
	}
	public Image getImageFromURL(String imageUrl){
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
	
	public boolean setSource(String string) {
		try {
			cube = DriverManager.getConnection("jdbc:sqlite:"+string+".db");
			vc.updateAll();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
