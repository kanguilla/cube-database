package main.java;

public class CardListView extends ListView {

	public CardListView(DatabaseMtg connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getCol() {
		return new String[]{ "Name", "Cost", "Type", "P/T", "Text", "Color"};
	}

	@Override
	public String[] getRow(Card c) {
		return new String[]{
				c.getName(), c.getCost(), 
				String.join(" ", c.getTypes()) + " - " + String.join(" ", c.getSubtypes()),
				(c.getPower() != null) ? c.getPower() + "/" + c.getToughness() : "", 
				c.getText(), String.join(" ",c.getColors())};
	}
	
}
