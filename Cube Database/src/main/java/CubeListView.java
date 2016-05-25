package main.java;

public class CubeListView extends ListView{
	private static final long serialVersionUID = 1L;

	DatabaseCube cube;
	
	public CubeListView(DatabaseMtg c1, DatabaseCube c2) {
		super(c1);
		cube = c2;
	}

	@Override
	public String[] getCol() {
		return new String[]{"Name, Set, Quantity"};
	}
	public String[] getRow(Card c) {
		return new String[]{"Name, Set, Quantity"};
	}

}
