package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Generator {

	public static String name = "mtg.db";
	
	public static void main(String[] args) {
		System.out.println("Creating mtg database...\n");
		try {
			double time = System.currentTimeMillis();
			MTG mtg = new MTG();
			System.out.println("Loaded mtg data ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			time = System.currentTimeMillis();
			Class.forName("org.sqlite.JDBC");
			Connection database = DriverManager.getConnection("jdbc:sqlite:mtg.db");
			Statement stat = database.createStatement();
			System.out.println("Connected to database ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			time = System.currentTimeMillis();
			stat.executeUpdate("drop table if exists cards;");
			stat.executeUpdate("create table if not exists cards("
					+ "id varchar(40) primary key not NULL,"
					+ "name varchar(50) ,"
					+ "manaCost varchar(10),"
					+ "cmc varchar(10),"
					+ "colors varchar(10),"
					+ "colorIdentity varchar(10),"
					+ "types varchar(50),"
					+ "subtypes varchar(50),"
					+ "power varchar (3),"
					+ "toughness varchar (3),"
					+ "text varchar (400),"
					+ "rarity varchar (1),"
					+ "artist varchar (100),"
					+ "flavor varchar (200),"
					+ "number varchar (5),"
					+ "layout varchar (20));");
			System.out.println("Created card table ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			time = System.currentTimeMillis();
			stat.executeUpdate("drop table if exists sets;");
			stat.executeUpdate("create table if not exists sets("
					+ "name varchar(50)primary key not NULL,"
					+ "code varchar(4),"
					+ "mciCode varchar(10),"
					+ "release varchar(15),"
					+ "type varchar(15));");
			System.out.println("Created set table ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			
			//add the sets
			time = System.currentTimeMillis();
			PreparedStatement prep = database.prepareStatement("insert into sets values (?, ?, ?, ?, ?);");
			database.setAutoCommit(false);
			for (MtgSet set : mtg.data.values()){
				prep.setString(1, set.name);
				prep.setString(2, set.code);
				prep.setString(3, set.magicCardsInfoCode);
				prep.setString(4, set.releaseDate);
				prep.setString(5, set.type);
				prep.addBatch();
			}
			prep.executeBatch();
			database.commit();
			System.out.println("Finished sets ("+ (System.currentTimeMillis() - time)/1000 +")");

			//add the cards
			prep = database.prepareStatement("insert into cards values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			database.setAutoCommit(false);
			time = System.currentTimeMillis();
			for (MtgSet set : mtg.data.values()){
				
				for (Card card : set.cards){
				
					prep.setString(1, card.id);
					prep.setString(2, card.name);
					prep.setString(3, card.manaCost);
					prep.setString(4, String.valueOf(card.cmc));
					prep.setString(5, (card.colors != null) ? String.join(",", card.colors) : "");
					prep.setString(6, (card.colorIdentity != null) ? String.join(",", card.colorIdentity) : "");
					prep.setString(7, (card.types != null) ? String.join(",", card.types) : "");
					prep.setString(8, (card.subtypes != null) ? String.join(",", card.subtypes) : "");
					prep.setString(9, card.power);
					prep.setString(10, card.toughness);
					prep.setString(11, card.text);
					prep.setString(12, card.rarity);
					prep.setString(13, card.artist);
					prep.setString(14, card.flavor);
					prep.setString(15, (card.number != null) ? card.number : "0");
					prep.setString(16, card.layout);
					prep.addBatch();
				}
			}
			prep.executeBatch();
			database.commit();
			System.out.println("Finished cards ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
