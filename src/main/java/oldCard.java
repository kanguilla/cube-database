package main.java;

import java.util.ArrayList;

public class oldCard{
	String name;
	
	String manaCost;
	double cmc;
	String[] colors;
	
	String type;
	String[] supertypes;
	String[] types;
	String[] subtypes = null;
	ArrayList<String> sets = new ArrayList<String>();
	
	String rarity;
	
	String text;
	String flavor;
	
	String artist;
	int number;
	
	String power;
	String toughness;
	
	String layout;
	String multiverseid;
	String imageName;
	String id;
	
	public oldCard(String name, String cost, String colors, String types, String subtypes, String text, String flavor, String power, String toughness){
		this.name = name;
		this.manaCost = cost;
		this.types = types.split(" ");
		this.subtypes = subtypes.split(" ");
		this.text = text;
		
		this.colors = colors.split(" ");
		this.power = (power != null) ? power : "";
		this.toughness = (toughness != null) ? toughness : "";
	}
	
	public oldCard(String name) {
		this.name = name;
	}

	public String getName(){return name;}
	public String getCost(){return manaCost;}
	public String[] getColors(){return colors;}
	public String[] getTypes(){return types;}
	public String getRarity(){return rarity;}
	public String getPower(){return power;}
	public String getToughness(){return toughness;}
	public String getText(){return text;}
	
	@Override
	public String toString(){
		return name;
	}
	
	public String[] toRowData(){
		return new String[]{
				name,
				manaCost,
				String.join(" ", types) + ((subtypes[0].length()>1) ? " - " : "") + String.join(" ", subtypes),
				!(power == "" || toughness == "") ? power + "/" + toughness : "",
				text,
				String.join(" ", colors)
		};
	}
	
	
}
