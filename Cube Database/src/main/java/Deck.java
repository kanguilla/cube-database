package main.java;

public class Deck {
	
	String name;
	String archetype;
	
	public Deck(String name, String archetype){
		this.name = name;
		this.archetype = archetype;
	}

	public String getName() {return name;}
	
	@Override
	public String toString() {return name;}
}
