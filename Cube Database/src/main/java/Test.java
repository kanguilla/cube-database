package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class Test {
	
	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		JsonParser json = new JsonParser();
		BufferedReader br = new BufferedReader (new FileReader(new File("ALLCARDS.json")));
		//System.out.println(br.readLine());
		
		//ArrayList ja = gson.fromJson(br.readLine(), ArrayList.class);
		Test t = new Test();
		t.toStringJson(new Card("Test Card"));
	}
	
	public void toStringJson(Card c){
		Gson gson = new Gson();
		ArrayList<Card> cards = new ArrayList<Card>();
		
		for (int i = 0; i< 5; i++){
			cards.add(c);
		}
		System.out.println(gson.toJson(cards));
	}
}
