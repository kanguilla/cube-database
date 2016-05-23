package main.gen;

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

	public MtgSet(String name, String code, String mcic, String releaseDate, String type){
		this.name = name;
		this.code = code;
		this.magicCardsInfoCode = mcic;
		this.releaseDate = releaseDate;
		this.type = type;
	}
}