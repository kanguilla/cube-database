package main.gen;

import java.util.ArrayList;
import java.util.List;

public class MtgSet{
	String name;
	String code;
	String magicCardsInfoCode;
	String releaseDate;
	String type;
	List<Object> booster;
	String mkm_name;
	int mkm_id;
	List<Card> cards;
	
	public void sortOrder(String order){
		
		Int
		
		List<Card> out = new ArrayList<Card>();
		
		for (int i = 0; i < order.length(); i++){
			char c = order.charAt(i);
			switch(c){
			case 'w':
				for (Card card : cards){
					if (card.colors.length == 1 && card.colors[0] == white)
				}
			}
		}
		
		cards = out;
	}
}