package main.gen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class GeneratorMtg {

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
					+ "name varchar(50) primary key not NULL,"
					+ "manaCost varchar(10),"
					+ "cmc varchar(10),"
					+ "colors varchar(10),"
					+ "colorIdentity varchar(10),"
					+ "types varchar(50),"
					+ "subtypes varchar(50),"
					+ "power varchar (3),"
					+ "toughness varchar (3),"
					+ "text varchar (300),"
					+ "layout varchar (20),"
					+ "frame varchar (20));");
			System.out.println("Created card table ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			time = System.currentTimeMillis();
			stat.executeUpdate("drop table if exists contents;");
			stat.executeUpdate("create table if not exists contents("
					+ "cardId varchar (40) primary key not null,"
					+ "cardName varchar(50) ,"
					+ "setName varchar(50),"
					+ "rarity varchar (1),"
					+ "artist varchar (100),"
					+ "flavor varchar (200),"
					+ "number varchar (5));");
			System.out.println("Created contents table ("+ (System.currentTimeMillis() - time)/1000 +")");
			
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
				prep.setString(2, (set.code != null) ? set.code : set.magicCardsInfoCode.toUpperCase());
				prep.setString(3, (set.magicCardsInfoCode != null) ? set.magicCardsInfoCode : set.code.toLowerCase());
				prep.setString(4, set.releaseDate);
				prep.setString(5, set.type);
				prep.addBatch();
			}
			prep.executeBatch();
			database.commit();
			System.out.println("Finished sets ("+ (System.currentTimeMillis() - time)/1000 +")");

			//add the cards
			prep = database.prepareStatement("insert or ignore into cards values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			PreparedStatement prep2 = database.prepareStatement("insert into contents values (?, ?, ?, ?, ?, ?, ?);");
			database.setAutoCommit(false);
			time = System.currentTimeMillis();
			for (MtgSet set : mtg.data.values()){
				int counter = 0;
				
				//System.out.println("Type 'new' or 'old' for " + set.name + " :");
				//Scanner sc = new Scanner(System.in);
				//String style = sc.nextLine();
				
				for (Card card : set.cards){
					
					String colors = "";
					if(card.colors != null){
						for (String s : card.colors){
							switch(s){
							case "White":
								colors += "W";
								break;
							case "Blue":
								colors += "U";
								break;
							case "Black":
								colors += "B";
								break;
							case "Red":
								colors += "R";
								break;
							case "Green":
								colors += "G";
								break;
							}
						}
					}
					
					prep.setString(1, card.name);
					prep.setString(2, card.manaCost);
					prep.setString(3, String.valueOf(card.cmc));
					prep.setString(4, (card.colors != null) ? colors : "");
					prep.setString(5, (card.colorIdentity != null) ? String.join(",", card.colorIdentity) : "");
					prep.setString(6, (card.types != null) ? String.join(",", card.types) : "");
					prep.setString(7, (card.subtypes != null) ? String.join(",", card.subtypes) : "");
					prep.setString(8, card.power);
					prep.setString(9, card.toughness);
					prep.setString(10, card.text);
					prep.setString(11, card.layout);	
					prep.setString(12, "new");	
					prep.addBatch();
					
					counter++;
					prep2.setString(1, card.id);
					prep2.setString(2, card.name);
					prep2.setString(3, (set.code != null) ? set.code : set.magicCardsInfoCode);
					prep2.setString(4, card.rarity);
					prep2.setString(5, card.artist);
					prep2.setString(6, card.flavor);
					prep2.setString(7, (card.number != null) ? card.number : String.valueOf(counter));
					prep2.addBatch();
				}
			}
			prep.executeBatch();
			prep2.executeBatch();
			database.commit();
			System.out.println("Finished cards ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
