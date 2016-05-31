package main.java;

import javax.swing.JFrame;

public class CubeLoader{

	public static void main(String[] args) {
		JFrame mtgList = new JFrame("MTG Cards");
		DatabaseMtg c1 = new DatabaseMtg();
		ListView mtgCardList = new oldList(c1);
		mtgCardList.update("select * from cards;");
		mtgList.getContentPane().add(mtgCardList);
		mtgList.setSize(500, 500);
		mtgList.setVisible(true);
		mtgList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		JFrame cubeList = new JFrame("Cube List");
		DatabaseCube c2 = new DatabaseCube();
		ListView cubeCardList = new CubeListView(c1, c2);
		cubeCardList.update("select * from cards;");
		cubeList.getContentPane().add(cubeCardList);
		cubeList.setSize(500, 500);
		cubeList.setVisible(true);
		cubeList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		*/
	}
}
