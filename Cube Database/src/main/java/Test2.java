package main.java;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class Test2 {
	Gson gson = new Gson();
	JsonParser json = new JsonParser();
	public static void main(String[] args) throws Exception {
		
		Class.forName("org.sqlite.JDBC");
		Connection database = DriverManager.getConnection("jdbc:sqlite:cube.db");
		
		Test2 t = new Test2();

		Statement stat = database.createStatement();
		stat.executeUpdate("drop table if exists cards;");
		stat.executeUpdate("create table if not exists cards("
				+ "name varchar(50) primary key not NULL,"
				+ "manaCost varchar(10),"
				+ "cmc varchar(10),"
				+ "color varchar(10),"
				+ "types varchar(50),"
				+ "subtypes varchar(50),"
				+ "text varchar (400),"
				+ "flavor varchar (200),"
				+ "power varchar (3),"
				+ "toughness varchar (3));");
		
		ArrayList<String> cards = t.readCards("ALLCARDS.json");	
		t.commit(cards, database);
	}
	
	public ArrayList<String> readCards(String file){
		ArrayList<String> output = new ArrayList<String>();
		int counter = 0;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), Charset.forName("UTF-8")));
			int b = 0;
			int r;
			String s = "";
			br.read();
			while ((r = br.read()) != -1){
				char c = (char) r;
				s += c;
				if(c == '{'){b++;}
				if(c == '}'){
					b--;
					if(b ==  0){
						output.add(s.substring(1).replaceAll("\\p{Pd}", "-").replaceAll("Æ", "AE").replaceAll("•", "*"));
						counter++;
						s = "";
					};
				}	
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("Done (" + counter + ")");
		return output;
	}
	
	public Card fromString(String s){
		s = s.substring(1);
		int i = s.indexOf("\":");
		s = s.substring(i + 2);
		Card c = gson.fromJson(s, Card.class);
		return c;
	}
	
	public void commit(ArrayList<String> cards, Connection database) throws SQLException{
		PreparedStatement prep = database.prepareStatement("insert into cards values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		database.setAutoCommit(false);
		double time = System.currentTimeMillis();
		for (String card : cards){
			Card c = fromString(card);
			
			if (!c.layout.equals("token")){
				//System.out.println(c.name);
				
				String colorA = "";
				if(c.colors == null){
					colorA = "C";	
				}else{
					for (String s : c.colors){
						colorA += (s.equals("Blue")) ? "U" :s.substring(0, 1).toUpperCase();
					}
				}
				prep.setString(1, c.name);
				prep.setString(2, (c.manaCost != null) ? c.manaCost.replaceAll("\\{", "").replaceAll("\\}", "") : "");
				prep.setDouble(3, c.cmc);
				prep.setString(4, (c.colors != null) ? colorA : "C");
				prep.setString(5, String.join(" ", c.types));
				prep.setString(6, (c.subtypes != null) ? String.join(" ", c.subtypes) : "");
				prep.setString(7, c.text);
				prep.setString(8, c.flavor);
				prep.setString(9, c.power);
				prep.setString(10, c.toughness);
				prep.addBatch();
			}
		}
		prep.executeBatch();
		database.commit();
		System.out.println("Finished "+cards.size()+" cards in ("+ (System.currentTimeMillis() - time)/1000 +")");
	}
}
