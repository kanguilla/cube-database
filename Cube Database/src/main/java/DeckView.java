package main.java;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DeckView extends JFrame{
	private static final long serialVersionUID = 1L;
	private MainView view;
	private ArrayList<Card> cards = new ArrayList<Card>();
	private CardListView cardList;
	private Connection databaseConnection;
	private Deck deck;
	
	public DeckView(Deck deck, final MainView view, Connection databaseConnection){
		super("'" + deck.name + "' Contents");
		this.deck = deck;
		this.databaseConnection = databaseConnection;
		this.view = view;
		this.setSize(400, 600);
		this.setLayout(new GridBagLayout());
		this.setVisible(true);
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		
		loadCards();
		cardList = new CardListView(view, cards, deck, null);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 4;
		c.weightx = 1.0;
		c.weighty = 1.0;
		this.add(cardList, c);
		
		JButton addCard = new JButton("Add Card");
		addCard.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCard();
			}
		});
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.5;
		c.weighty = 0.0;
		add(addCard, c);
		
		JButton removeCard = new JButton("Remove Card");
		removeCard.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteCard();
			}
		});
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.5;
		c.weighty = 0.0;
		add(removeCard, c);
	}
	
	private void loadCards(){
		PreparedStatement prep;
		try {
			prep = databaseConnection.prepareStatement("select * from deckLists where deckName=? order by deckName asc;");
			prep.setString(1, deck.name);
			ResultSet rs = prep.executeQuery();
			ArrayList<String> cardResults = new ArrayList<String>();
			
			while (rs.next()) {
				for(int i=0;i<rs.getInt("occurences"); i++){
					cardResults.add(rs.getString("cardName"));
				}
				System.out.println("Card: " + rs.getString("cardName") + " " + rs.getInt("occurences"));
			}
			
			for (String s : cardResults){
				prep = databaseConnection.prepareStatement("select * from cards where name=? order by name asc;");
				prep.setString(1, s);
				rs = prep.executeQuery();
				Card card = new Card(
						rs.getString("name"),
						rs.getString("manaCost"),
						rs.getString("colors"),
						rs.getString("types"),
						rs.getString("subtypes"),
						rs.getString("text"),
						rs.getString("flavor"),
						rs.getString("power"),
						rs.getString("toughness"));
				cards.add(card);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteCard(){
		Card selectedCard = cardList.getList().getSelectedValue();	
		cards.remove(selectedCard);
		int count = 0;
		for (Card c : cards){
			if (c.getName().equals(selectedCard.getName()))count++;
		}
		
		System.out.println(selectedCard.toString() + "=" + count);
		if(count > 0){
			try {
				PreparedStatement prep = databaseConnection.prepareStatement("update deckLists set occurences=? where deckName=? and cardName=?;");
				prep.setInt(1, count);
				prep.setString(2, deck.name);
				prep.setString(3, selectedCard.getName());
				prep.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			try {
				PreparedStatement prep = databaseConnection.prepareStatement("delete from deckLists where deckName=? and cardName=?;");
				prep.setString(1, deck.name);
				prep.setString(2, selectedCard.getName());
				prep.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		updateList();
	}
	
	private void addCard(){
		Card newCard = null;
		
		boolean found = false;
		while(!found){
			String cardName = JOptionPane.showInputDialog("Enter Card Name");
			for(Card c : view.cards){
				if(c.getName().equalsIgnoreCase(cardName)){
					newCard = c;
					found = true; 
				}
			}
		}
		cards.add(newCard);
		int count = 0;
		for (Card c : cards){
			if (c.getName().equals(newCard.getName()))count++;
		}
		
		System.out.println(newCard.toString() + "=" + count);
		if(count < 2){
			try {
				PreparedStatement prep = databaseConnection.prepareStatement("insert into deckLists values (?, ?, 1);");
				prep.setString(1, newCard.getName());
				prep.setString(2, deck.name);
				prep.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			try {
				PreparedStatement prep = databaseConnection.prepareStatement("update deckLists set occurences=? where deckName=? and cardName=?");
				prep.setInt(1, count);
				prep.setString(2, deck.name);
				prep.setString(3, newCard.getName());
				prep.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		updateList();
	}
	
	public void updateList(){
		if(!cards.isEmpty()){
			cardList.getList().setListData(cards.toArray(new Card[1]));
		}else{
			cardList.getList().setListData(new Card[]{});
		}
	}
}
