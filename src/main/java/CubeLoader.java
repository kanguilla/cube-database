package main.java;

import javax.swing.JFrame;

public class CubeLoader extends JFrame{
	private static final long serialVersionUID = 1L;
	CubeConnection connection;
	
	public static void main(String[] args) {
		new CubeLoader();
		
	}
	
	public CubeLoader(){
		connection = new CubeConnection("mtg.db");
		CardListView cardList = new CardListView(connection);
		this.getContentPane().add(cardList);
		this.setSize(500, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
