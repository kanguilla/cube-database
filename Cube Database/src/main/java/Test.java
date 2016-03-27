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
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class Test {
	Gson gson = new Gson();
	JsonParser json = new JsonParser();
	public static void main(String[] args) throws Exception {
		
		Class.forName("org.sqlite.JDBC");
		Connection database = DriverManager.getConnection("jdbc:sqlite:cube.db");
		
		Test t = new Test();
		PreparedStatement prep = null;
		ArrayList<String> cards = t.readCards("ALLCARDS.json");
		for (String s : cards){
			Card c = t.fromString(s);
			
			
			if (!c.layout.equals("token")){
				System.out.println(c.name);
				
				prep = database.prepareStatement("insert into cards values (?, ?, ?, ?, ?, ?, ?, ?);");
				prep.setString(1, c.name);
				prep.setString(2, c.manaCost);
				prep.setString(3, (c.colors != null) ? String.join(" ", c.type) : "NULL");
				prep.setString(4, (c.colors != null) ? String.join(" ", c.colors) : "C");
				prep.setString(5, c.power);
				prep.setString(6, c.toughness);
				prep.setString(7, c.rarity);
				prep.setString(8, "TST");
				prep.execute();
				
			}
			
		}
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
						output.add(s.substring(1));
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
		char[] chars = s.toCharArray();
		int i = s.indexOf("\":");
		s = s.substring(i + 2);
		Card c = gson.fromJson(s, Card.class);
		return c;
	}
	
	public void toStringJson(Card c){
		System.out.println(gson.toJson(c));
	}
}
