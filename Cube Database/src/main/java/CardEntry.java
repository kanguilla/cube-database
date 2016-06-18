package main.java;

import javafx.beans.property.SimpleIntegerProperty;

public class CardEntry{
	Card card;
	String set;
	int quantity = 0;
	public CardEntry(Card c, String set, int quantity){
		this.card = c;
		this.set = set;
		this.quantity = quantity;
	}
	
	public SimpleIntegerProperty getQuantityProperty(){
		SimpleIntegerProperty si = new SimpleIntegerProperty();
		si.set(this.quantity);
		return si;
	}
}
