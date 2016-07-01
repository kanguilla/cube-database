package main.core;

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
		
		//Context attributes
		
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
		public String getPT() {
			if(this.power == null || this.toughness == null){
				return "";
			}
			return (this.power + "/" + this.toughness);}
		public String getColorString(){
			if (colors == null || colors.length == 0){
				return "";
			}
			String out = "";
			for (String s : colors){
				if(s.length() > 0){
					switch(s){
					case "Blue":
						out+="U";
						break;
					default:
						out+= s.toUpperCase().charAt(0);
					}
				}
			}
			return out;
		}
		public String getCSSStyle(){
			
			String ts = this.getColorString();
			
			if (ts.equalsIgnoreCase("W")){
				return "-fx-background-color: #ffffcc";
			}
			if (ts.equalsIgnoreCase("U")){
				return "-fx-background-color: #66ccff";
			}
			if (ts.equalsIgnoreCase("B")){
				return "-fx-background-color: #b3b3b3";
			}
			if (ts.equalsIgnoreCase("R")){
				return "-fx-background-color: #ff6666";
			}
			if (ts.equalsIgnoreCase("G")){
				return "-fx-background-color: #85e085";
			}
			
			if (ts.equalsIgnoreCase("")){
				return "-fx-background-color: #dfbf9f";
			}
			
			return "-fx-background-color: #ffcc66";
		}
	}