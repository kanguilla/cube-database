package main.java;

public class Card{
	String name;
	
	String manaCost;
	String cmc;
	String[] colors;
	
	String type;
	String[] supertypes;
	String[] types;
	String[] subtypes;
	
	String rarity;
	
	String text;
	String flavor;
	
	String artist;
	String number;
	
	String power;
	String toughness;
	
	String layout;
	String multiverseid;
	String imageName;
	String id;
	
	
	String set;
	String types2;
	String colors2;	
	
	public Card(String name, String cost, String colors, String types, String rarity, String power, String toughness, String set){
		this.name = name;
		this.manaCost = cost;
		this.types2 = types;
		this.colors2 = colors;
		this.power = power;
		this.toughness = toughness;
		this.rarity = rarity;
		this.set = set;
	}
	
	public Card(String name){
		this.name = name;
	}
	
	public String getName(){return name;}
	public String getCost(){return manaCost;}
	public String getColors(){return colors2;}
	public String getTypes(){return types2;}
	public String getRarity(){return rarity;}
	public String getPower(){return power;}
	public String getToughness(){return toughness;}
	public String getSet(){return set;}
	
	@Override
	public String toString(){
		return name;
	}
}
