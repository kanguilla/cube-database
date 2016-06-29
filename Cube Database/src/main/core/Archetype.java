package main.core;

public class Archetype {
	
	String name;
	String colors;
	
	public Archetype(String name, String colors){
		this.name = name;
		this.colors = colors;
	}
	
	public Archetype(String name){
		this.name = name;
	}
	
	
	public void addCard(String name){

	}
	
	@Override
	public String toString(){
		return name;
	}

	public Object getSize() {
		// TODO Auto-generated method stub
		return null;
	}
}
