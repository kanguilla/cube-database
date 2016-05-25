package main.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
		System.out.println("loaded order " + order);
		
		List<Card> out = new ArrayList<Card>();
		
		if(order.equalsIgnoreCase("alpha")){
			Collections.sort(cards, new Comparator<Card>() {
		        @Override
		        public int compare(Card s1, Card s2) {
		            return s1.name.compareToIgnoreCase(s2.name);
		        }
		    });
			for (Card c : cards){
				System.out.print("    " + c.name);
			}
			return;
		}
		
		
		for (int i = 0; i < order.length(); i++){
			
			char c = order.charAt(i);
			System.out.println("  " + i + " - " + c);
			switch(c){
			case 'w':
				for (Card card : cards){
					if (card.colors != null && card.colors.length == 1 && card.colors[0].equals("White")){
						out.add(card);
						System.out.println("    " + card.name);
					}
				}
				break;
			case 'u':
				for (Card card : cards){
					if (card.colors != null && card.colors.length == 1 && card.colors[0].equals("Blue")){
						out.add(card);
						System.out.println("    " + card.name);
					}
				}
				break;
			case 'b':
				for (Card card : cards){
					if (card.colors != null && card.colors.length == 1 && card.colors[0].equals("Black")){
						out.add(card);
						System.out.println("    " + card.name);
					}
				}
				break;
			case 'r':
				for (Card card : cards){
					if (card.colors != null && card.colors.length == 1 && card.colors[0].equals("Red")){
						out.add(card);
						System.out.println("    " + card.name);
					}
				}
				break;
			case 'g':
				for (Card card : cards){
					if (card.colors != null && card.colors.length == 1 && card.colors[0].equals("Green")){
						out.add(card);
						System.out.println("    " + card.name);
					}
				}
				break;
			case 'm':
				for (Card card : cards){
					if (card.colors != null && card.colors.length > 1){
						out.add(card);
						System.out.println("    " + card.name);
					}
				}
				break;
			case 'c':
				for (Card card : cards){
					if (card.colors == null && !card.isType("Land")){
						out.add(card);
						System.out.println("    " + card.name);
					}
				}
				break;
			case 'l':
				for (Card card : cards){
					if (card.colors == null && card.isType("Land")){
						out.add(card);
						System.out.println("    " + card.name);
					}
				}
				break;
			}
			
		}
		
		cards = out;
	}
}