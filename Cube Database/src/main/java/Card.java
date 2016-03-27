package main.java;


public class Card{
	String name;
	
	String manaCost;
	int cmc;
	String[] colors;
	
	String type;
	String[] supertypes;
	String[] types;
	String[] subtypes = null;
	
	String rarity;
	
	String text;
	String flavor;
	
	String artist;
	int number;
	
	int power;
	int toughness;
	
	String layout;
	String multiverseid;
	String imageName;
	String id;
	
	public Card(String name, String cost, String colors, String types, String subtypes, String text, String flavor, int power, int toughness){
		this.name = name;
		this.manaCost = cost;
		this.types = types.split(" ");
		this.subtypes = subtypes.split(" ");
		
		
		this.colors = colors.split(" ");
		this.power = power;
		this.toughness = toughness;
	}
	
	public Card(String name) {
		this.name = name;
	}

	public String getName(){return name;}
	public String getCost(){return manaCost;}
	public String[] getColors(){return colors;}
	public String getTypes(){return type;}
	public String getRarity(){return rarity;}
	public int getPower(){return power;}
	public int getToughness(){return toughness;}
	
	@Override
	public String toString(){
		return name;
	}
}
