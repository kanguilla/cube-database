package main.java;

import java.util.List;

public class Card{
		
		//Identity attributes
		String name;
		String manaCost;
		int cmc;
		List<String> colors;
		List<String> colorIdentity;
		List<String> types;
		List<String> subtypes;
		
		//Statistic attributes
		String power;
		String toughness;
		String text;
		String rarity;
		
		//Flavor attributes
		String artist;
		String flavor;
		
		//Location attributes
		String id; //<-primary key
		String mciNumber;
		String number;
		String layout;
		
	}