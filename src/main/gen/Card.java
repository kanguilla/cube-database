package main.gen;

public class Card{
		
		//Identity attributes
		String name; 
		String manaCost;
		String cmc;
		String[] colors;
		String[] colorIdentity;
		String[] types;
		String[] subtypes;
		String layout;
		
		//Statistic attributes
		String power;
		String toughness;
		String text;
		
		//Flavor attributes
		String artist;
		String flavor;
		
		//Location attributes
		String id; 
		String number;
		String set;
		String rarity;
		
		public boolean isType(String type){
			if(types == null){
				return false;
			}
			for (String s : types){
				if (s.equalsIgnoreCase(type)){
					return true;
				}
			}
			if(subtypes == null){
				return false;
			}
			for (String s : subtypes){
				if (s.equalsIgnoreCase(type)){
					return true;
				}
			}
			return false;
		}
	}