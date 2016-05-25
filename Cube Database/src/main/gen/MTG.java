package main.gen;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import com.google.gson.*;

public class MTG {
	
	HashMap<String,MtgSet> data = new HashMap<String,MtgSet>();
	
	public MTG() {
		JsonParser parser = new JsonParser();
		Gson g = new GsonBuilder().create();
		try{
            JsonObject obj = parser.parse(new FileReader("AllSets.json")).getAsJsonObject();
            for (Entry<String, JsonElement> element :obj.entrySet()){
            	data.put(element.getKey(), g.fromJson(element.getValue(), MtgSet.class));
            	System.out.println(element.getKey() + " - " + data.get(element.getKey()).cards.size());
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		data.get("VIS").sortOrder("bugrwmcl");
		data.get("HML").sortOrder("bugrwmcl");
		data.get("FEM").sortOrder("bugrwmcl");
		data.get("DRK").sortOrder("bugrwmcl");
		data.get("MIR").sortOrder("bugrwmcl");
		data.get("WTH").sortOrder("bugrwmcl");
		data.get("TMP").sortOrder("bugrwmcl");
		data.get("STH").sortOrder("bugrwmcl");
		data.get("LEB").sortOrder("bugrwmcl");
		data.get("LEA").sortOrder("bugrwmcl");
		data.get("2ED").sortOrder("bugrwmcl");
		data.get("3ED").sortOrder("bugrwmcl");
		data.get("4ED").sortOrder("bugrwmcl");
		data.get("5ED").sortOrder("bugrwmcl");
		data.get("ICE").sortOrder("bugrwclm");
		data.get("ALL").sortOrder("bugrwclm");
	}
	public List<Card> getCards(String setCode){
		return data.get(setCode).cards;
	}
}
