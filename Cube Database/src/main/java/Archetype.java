package main.java;

import java.util.ArrayList;

public class Archetype {
	
	String name;
	String colors;
	ArrayList<String> members;
	
	public Archetype(String name, String colors){
		this.name = name;
		this.colors = colors;
		members = new ArrayList<String>();
	}
	
	public void addCard(String name){
		members.add(name);
	}
	
	@Override
	public String toString(){
		return name;
	}
}
