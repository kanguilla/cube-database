package main.java;

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
		
		//Set attributes
		/*
		String artist;
		String flavor;
		String number;
		String set;
		String rarity;
		*/
		
		public Card(
				String name, String manaCost, String cmc, 
				String colors, String colorIdentity, 
				String types, String subTypes, String power, String toughness, 
				String text, String layout){
			this.name = name; this.manaCost = manaCost; this.cmc = cmc;
			this.colors = colors.split(",");		
			this.colorIdentity = colorIdentity.split(",");
			this.types = types.split(",");
			this.subtypes = subTypes.split(",");
			this.power = power;this.toughness = toughness;this.text = text;
			this.layout = layout;
		}
		
		public String getName(){return name;}
		public String getCost(){return manaCost;}
		public String[] getColors(){return colors;}
		public String[] getTypes(){return types;}
		public String[] getSubtypes(){return subtypes;}
		public String getText(){return text;}
		public String getPower(){return power;}
		public String getToughness(){return toughness;}
		
		public String[] toRowData() {
			return new String[]{getName(), getCost(), 
					String.join(" ", getTypes()) + " - " + String.join(" ", getSubtypes()), 
					getPower(), getText(), String.join(" ",getColors())};
		}
	}