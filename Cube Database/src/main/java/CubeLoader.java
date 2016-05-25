package main.java;

import javax.swing.JFrame;

public class CubeLoader{
	
	static DatabaseMtg connection;
	public static void main(String[] args) {
		JFrame mtgList = new JFrame();
		connection = new DatabaseMtg();
		CardListView mtgCardList = new CardListView(connection);
		mtgCardList.update("select * from cards;");
		mtgList.getContentPane().add(mtgCardList);
		mtgList.setSize(500, 500);
		mtgList.setVisible(true);
		mtgList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JFrame cubeList = new JFrame();
		connection = new DatabaseMtg();
		CardListView cubeCardList = new CardListView(connection);
		cubeCardList.update("select * from cards;");
		cubeList.getContentPane().add(cubeCardList);
		cubeList.setSize(500, 500);
		cubeList.setVisible(true);
		cubeList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
