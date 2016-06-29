package main.core;
import java.util.HashMap;

public class Player {
	int id;
	String name;
	HashMap<String, Integer> winLog = new HashMap<String, Integer>();
	
	public Player(String name, int id){
		this.name = name;
		this.id = id;
	}
	
	public void addWin(String deckName){
		if(winLog.containsKey(deckName)){
			winLog.put(deckName, winLog.get(deckName)+1);
		}else{
			winLog.put(deckName, 1);
		}
	}
	public int getId(){return id;}
	public String getName() {return name;}
	public String toString(){return name;}
	
}
