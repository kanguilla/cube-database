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
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public List<Card> getCards(String setCode){
		return data.get(setCode).cards;
	}
}
