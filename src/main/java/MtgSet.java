package main.java;

import java.util.List;

public class MtgSet implements Comparable<MtgSet>{
	String name;
	String code;
	String magicCardsInfoCode;
	String releaseDate;
	String border;
	String type;
	List<Object> booster;
	String mkm_name;
	int mkm_id;
	List<Card> cards;
	
	@Override
	public int compareTo(MtgSet other) {
		
		return 0;
	}
	
}