package main.java;

public class Card{
	private String name;
	private String cost;
	private String types;
	private String colors;	
	private Integer power;
	private Integer toughness;
	private String rarity;
	private String set;
	
	public Card(String name, String cost, String colors, String types, String rarity, Integer power, Integer toughness, String set){
		this.name = name;
		this.cost = cost;
		this.types = types;
		this.colors = colors;
		this.power = power;
		this.toughness = toughness;
		this.rarity = rarity;
		this.set = set;
	}
	
	public Card(String name){
		this.name = name;
	}
	
	public String getName(){return name;}
	public String getCost(){return cost;}
	public String getColors(){return colors;}
	public String getTypes(){return types;}
	public String getRarity(){return rarity;}
	public int getPower(){return power;}
	public int getToughness(){return toughness;}
	public String getSet(){return set;}
	
	@Override
	public String toString(){
		return name;
	}
}
