package main.java;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.java.Card;
import main.java.Deck;

// This is the Panel that contains represents the view of the
// Music Store

public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;
	private Connection databaseConnection;
	private Deck selectedDeck;
	private Archetype selectedArchetype;
	private MainView thisFrame;
	
	public ArrayList<Deck> decks = new ArrayList<Deck>();
	public ArrayList<Card> cards = new ArrayList<Card>();
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<Archetype> archetypes = new ArrayList<Archetype>();
	
	public JList<Deck> deckList;
	public CardListView cardList;
	public JList<Archetype> archList;

	private static Font uif = new Font("Arial", Font.BOLD, 14);

	public MainView(String title, Connection aDB, ArrayList<Deck> initialDeck, ArrayList<Card> initialCards, ArrayList<Player> initialPlayers, ArrayList<Archetype> initialArchetypes) {
		super(title);
		databaseConnection = aDB;
		thisFrame = this;
		decks = initialDeck;
		cards = initialCards;
		players = initialPlayers;
		archetypes = initialArchetypes;
		selectedArchetype = null;
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					System.out.println("Closing Database Connection");
					databaseConnection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		
		JLabel labelTitle = new JLabel("Card List");
		labelTitle.setFont(new Font("Arial", Font.BOLD, 24));
		labelTitle.setForeground(Color.white);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		add(labelTitle, c);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JButton deleteCardButton = new JButton("Delete Card");
		deleteCardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(!cardList.getList().isSelectionEmpty()){
					System.out.println("Delete card : " + cardList.getList().getSelectedValue().toString());
					if(JOptionPane.showConfirmDialog(thisFrame,"Are you sure?","Confirm Deletion", JOptionPane.YES_NO_OPTION) == 0){
						thisFrame.deleteCard(cardList.getList().getSelectedValue());
						cardList.update(cards);
					}
				}
			}
		});
		buttonPanel.add(deleteCardButton, c);
		JButton addCardButton = new JButton("Add Card");
		addCardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("Adding a new card...");
				newCardDialog();
				cardList.update(cards);
			}
		});
		buttonPanel.add(addCardButton);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		add(buttonPanel, c);
		
		labelTitle = new JLabel("Decks");
		labelTitle.setFont(new Font("Arial", Font.BOLD, 24));
		labelTitle.setForeground(Color.white);
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		add(labelTitle, c);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JButton deckDeleteButton = new JButton("Delete Deck");
		deckDeleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(!deckList.isSelectionEmpty()){
					System.out.println("Delete deck : " + deckList.getSelectedValue().toString());
					if(JOptionPane.showConfirmDialog(thisFrame,"Are you sure?","Confirm Deletion", JOptionPane.YES_NO_OPTION) == 0){
						thisFrame.deleteDeck(selectedDeck);
						thisFrame.updateList();
						deckList.setListData(players.toArray(new Deck[1]));
					}
				}
			}
		});
		buttonPanel.add(deckDeleteButton, c);
		JButton addDeckButton = new JButton("New Deck");
		addDeckButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("Creating new deck...");
				newDeckDialog();
			}
		});
		buttonPanel.add(addDeckButton);
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		add(buttonPanel, c);
		
		cardList = new CardListView(thisFrame, databaseConnection, cards, null, null);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(cardList, c);

		deckList = new JList<Deck>();
		deckList.setFont(uif);
		deckList.setBackground(Color.lightGray);
		deckList.setForeground(Color.black);
		deckList.setPrototypeCellValue(new Deck("*", "*"));
		deckList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if(!event.getValueIsAdjusting()){
					selectDeck();
				}
			}
		});
		deckList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					@SuppressWarnings("unchecked")
					JList<Deck> theList = (JList<Deck>) event.getSource();
					int index = theList.locationToIndex(event.getPoint());
					Deck d = (Deck) theList.getModel().getElementAt(index);
					new DeckView(d, thisFrame, databaseConnection);
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(deckList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 3;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);
		
		ArchetypePanel archPanel = new ArchetypePanel();
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 3;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(archPanel, c);
		
		
		

		JButton playersButton = new JButton("Players");
		playersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				new PlayersView(players, thisFrame);
			}
		});
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.1;
		add(playersButton, c);

		this.getContentPane().setBackground(Color.black);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(1000, 600);

		updateList();
	}
	private void selectDeck() {
		selectedDeck = (Deck) (deckList.getSelectedValue());
		if(selectedDeck != null){
			System.out.println("Deck Selected: " + selectedDeck);
			ArrayList<Integer> values = new ArrayList<Integer>();
			for (int i = 0; i < archetypes.size(); i++){
				if(archetypes.get(i).toString().equals(selectedDeck.archetype)){
					values.add(i);
				}
			}
			int[] indices = new int[values.size()];
			for (int i = 0; i < values.size(); i++){
				indices[i] = values.get(i);
			}
			archList.setSelectedIndices(indices);
		}
	}
	private void selectArch() {
		selectedArchetype = (Archetype) (archList.getSelectedValue());
		System.out.println("Archetype Selected: " + selectedArchetype);
	}
	private void updateList() {
		if(!decks.isEmpty()){
			deckList.setListData(decks.toArray(new Deck[1]));
		}else{
			deckList.setListData(new Deck[]{});
		}
		if(!archetypes.isEmpty()){
			if(archList != null) archList.setListData(archetypes.toArray(new Archetype[1]));
		}else{
			archList.setListData(new Archetype[]{});
		}
	}
	public void changeWinEntry(Player p, String deck, int wins){
		try {
			PreparedStatement prep;
			if(wins == 0){
				p.winLog.remove(deck.trim());
				prep = databaseConnection.prepareStatement("delete from playLog where playerId=? and deckName=?;");
				prep.setInt(1, p.getId());
				prep.setString(2, deck);
			}else{
				if (p.winLog.put(deck, wins) != null){
					prep = databaseConnection.prepareStatement("update playLog set wins=? where playerId=? and deckName=?;");
					prep.setInt(1, wins);
					prep.setInt(2, p.getId());
					prep.setString(3, deck.trim());
				}else{
					prep = databaseConnection.prepareStatement("insert into playLog values (?, ?, ?);");
					prep.setInt(1, p.getId());
					prep.setString(2, deck.trim());
					prep.setInt(3, wins);
				}
			}	
			prep.executeUpdate();
			prep.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void addPlayer(String name) {
		int nextId = 0;
		for (Player p:players){
			if(p.getId() > nextId){
				nextId = p.getId();
			}
		}
		Player newPlayer = new Player(name, nextId + 1);
		players.add(newPlayer);
		try {
			PreparedStatement prep = databaseConnection.prepareStatement("insert into players values (?,?);");
			prep.setInt(1, newPlayer.getId());
			prep.setString(2, newPlayer.name);
			prep.executeUpdate();		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deletePlayer(Player selectedPlayer) {
		players.remove(selectedPlayer);
		try {
			PreparedStatement prep = databaseConnection.prepareStatement("delete from playLog where playerId=?;");
			prep.setInt(1, selectedPlayer.getId());
			prep.executeUpdate();
			
			prep = databaseConnection.prepareStatement("delete from players where id=?;");
			prep.setInt(1, selectedPlayer.getId());
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteDeck(Deck selectedDeck) {
		decks.remove(selectedDeck);
		try {
			PreparedStatement prep = databaseConnection.prepareStatement("delete from deckLists where deckName=?;");
			prep.setString(1, selectedDeck.getName());
			prep.executeUpdate();
			
			prep = databaseConnection.prepareStatement("delete from decks where name=?;");
			prep.setString(1, selectedDeck.getName());
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteCard(Card selectedCard) {
		cards.remove(selectedCard);
		try {
			PreparedStatement prep = databaseConnection.prepareStatement("delete from deckLists where cardName=?;");
			prep.setString(1, selectedCard.getName());
			prep.executeUpdate();
			
			prep = databaseConnection.prepareStatement("delete from archetypeMembers where cardName=?;");
			prep.setString(1, selectedCard.getName());
			prep.executeUpdate();
			
			prep = databaseConnection.prepareStatement("delete from cards where name=?;");
			prep.setString(1, selectedCard.getName());
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void newDeckDialog(){
		JTextField nameField = new JTextField(20);
		JTextField archField = new JTextField(20);
		
		JPanel parent = new JPanel();
		parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
		parent.add(new JLabel("Enter the deck name and archetype."));
		parent.add(Box.createRigidArea(new Dimension(0, 10)));
		parent.add(new JLabel("Deck name:"));
		parent.add(nameField);
		parent.add(new JLabel("Archetype:"));
		parent.add(archField);
		
		int result = JOptionPane.showConfirmDialog(null, parent, "New Deck", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			System.out.println(nameField.getText() + ":" +archField.getText());
			try {
				decks.add(new Deck(nameField.getText(),archField.getText()));
				PreparedStatement prep = databaseConnection.prepareStatement("insert into decks values (?, ?);");
				prep.setString(1, nameField.getText());
				prep.setString(2, archField.getText());
				prep.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		updateList();
	}
	public void newCardDialog(){
		JTextField nameField = new JTextField(20);
		JTextField costField = new JTextField(10);
		JTextField typeField = new JTextField(20);
		JTextField colorField = new JTextField(10);
		JTextField rarityField = new JTextField(10);
		JTextField powerField = new JTextField(10);
		JTextField toughnessField = new JTextField(10);
		JTextField setField = new JTextField(3);
		JTextArea archetypeField = new JTextArea();
		archetypeField.setBackground(Color.lightGray);
		archetypeField.setPreferredSize(new Dimension(160, 100));
		
		JPanel parent = new JPanel();
		parent.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		
		c.gridy = 0; parent.add(new JLabel("Enter card details."), c);
		c.gridy = 1; parent.add(Box.createRigidArea(new Dimension(0, 10)), c);
		c.gridy = 2; parent.add(new JLabel("Name:"), c);
		c.gridy = 3; parent.add(nameField, c);
		c.gridwidth = 1;
		c.gridy = 4;
		parent.add(new JLabel("Cost"), c);
		parent.add(new JLabel("Color"), c);
		c.gridy = 5; 
		parent.add(colorField, c);
		parent.add(costField, c);
		c.gridwidth = 2;
		c.gridy = 6; parent.add(new JLabel("Type:"), c);
		c.gridy = 7; parent.add(typeField, c);
		c.gridy = 8; 
		c.gridwidth = 1;
		parent.add(new JLabel("Power"), c);
		parent.add(new JLabel("Toughness"), c);
		c.gridy = 9; 
		parent.add(powerField, c);
		parent.add(toughnessField, c);
		c.gridy = 10; 
		parent.add(new JLabel("Rarity:"), c);
		parent.add(new JLabel("Set:"), c);
		c.gridy = 11;
		parent.add(rarityField, c);
		parent.add(setField, c);
		c.gridy = 12; 
		c.gridwidth = 2;
		c.gridy = 13; parent.add(new JLabel("Archetypes (Optional, 1 per line)"), c);
		c.gridheight = 3;
		c.weighty = 1.0;
		c.gridy = 14; parent.add(archetypeField, c);
		
		int result = JOptionPane.showConfirmDialog(null, parent, "Add Card", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			Integer power = (toughnessField.getText().equals("") ? null : Integer.parseInt(toughnessField.getText()));
			Integer toughness = (toughnessField.getText().equals("") ? null : Integer.parseInt(toughnessField.getText()));
			try {
				
				//Create the card
				Card newCard = new Card(
						nameField.getText(),
						costField.getText(), 
						colorField.getText(), 
						typeField.getText(), 
						rarityField.getText(), 
						power, 
						toughness, 
						setField.getText());
				cards.add(newCard);
				//Store it
				PreparedStatement prep = databaseConnection.prepareStatement("insert into cards values (?, ?, ?, ?, ?, ?, ?, ?);");
				prep.setString(1, nameField.getText());
				prep.setString(2, costField.getText());
				prep.setString(3, typeField.getText());
				prep.setString(4, colorField.getText());
				if(power == null){
					prep.setNull(5, java.sql.Types.INTEGER); 
				}else{
					prep.setInt(5, power); 
				}
				if(toughness == null){
					prep.setNull(6, java.sql.Types.INTEGER); 
				}else{
					prep.setInt(6, toughness); 
				}
				prep.setString(7, rarityField.getText());
				prep.setString(8, setField.getText());
				prep.executeUpdate();
				
				//Assign archetypes
				String[] archs = archetypeField.getText().split("\n");
				for (String s : archs){
					//add to local arch
					for (Archetype a : archetypes){
						if(a.name.equalsIgnoreCase(s)){
							a.addCard(newCard.getName());
						}
					}
					prep = databaseConnection.prepareStatement("insert into archetypeMembers values (?, ?);");
					prep.setString(1, nameField.getText());
					prep.setString(2, s.substring(0, 1).toUpperCase() + s.substring(1));
					prep.addBatch();
				}
				prep.executeBatch();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	public static Color toRGB(String colors){
		Color GOLD = new Color(255, 245, 135);
		Color WHITE = new Color(240, 230, 165);
		Color BLUE = new Color(150, 205, 222);
		Color BLACK = new Color(128, 128, 128);
		Color RED = new Color(255, 110, 50);
		Color GREEN = new Color(125, 215, 95);
		Color BROWN = new Color(190, 160, 100);
		
		if (colors.length() > 1) {
			return(GOLD);
		}
		switch (colors) {
		case ("W"):
			return(WHITE);
		case ("U"):
			return(BLUE);
		case ("B"):
			return(BLACK);
		case ("R"):
			return(RED);
		case ("G"):
			return(GREEN);
		default:
			return(BROWN);
		}
	}
	
	//Custom Swing Classes
	class ArchetypeMemberPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		ArchetypeMemberPanel thisPanel;
		public ArchetypeMemberPanel(ArrayList<String> cardNames, Archetype arch){
			thisPanel = this;
			this.setBackground(Color.BLACK);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			ArrayList<Card> memberCards = new ArrayList<Card>();
			for (String s : cardNames){
				for (Card c : cards){
					if(c.getName().equals(s)){
						memberCards.add(c);
					}
				}
			}
			this.add(new CardListView(thisFrame, databaseConnection, memberCards, null, arch));
		}
	}
	class ArchetypePanel extends JPanel{
		private static final long serialVersionUID = 1L;
		ArchetypePanel thisPanel;
		final CardLayout layout = new CardLayout();
		final JPanel swapView = new JPanel();
		boolean lookingAtCards = false;
		
		//Components
		JPanel cardListPanel = new ArchetypeMemberPanel(new ArrayList<String>(), null);
		JPanel archListPanel = new JPanel();
		JButton showAllCards;
		JLabel archetypeTitle;
		
		public ArchetypePanel(){
			thisPanel = this;
			
			this.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			
			archetypeTitle = new JLabel("Archetypes"){
				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getPreferredSize(){
					return new Dimension (150, 30);
				}
			};
			archetypeTitle.setFont(new Font("Arial", Font.BOLD, 24));
			archetypeTitle.setForeground(Color.white);
			archetypeTitle.setBackground(Color.black);
			archetypeTitle.setOpaque(true);
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 1.0;
			c.weighty = 0.2;
			add(archetypeTitle, c);
			showAllCards = new JButton("Show cards of this type"){
				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getPreferredSize(){
					return new Dimension (150, 30);
				}
			};
			//showAllCards.setPreferredSize(preferredSize);
			showAllCards.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if(lookingAtCards){
						layout.show(swapView, "archs");
						lookingAtCards = false;
						showAllCards.setText("Show cards of this type");
						archetypeTitle.setText("Archetype");
					}else {
						if(!archList.isSelectionEmpty()){
							System.out.println("Looking at all the " + archList.getSelectedValue() + " cards.");
							cardListPanel = new ArchetypeMemberPanel(archList.getSelectedValue().members, archList.getSelectedValue());
							swapView.add("cards", cardListPanel);
							layout.show(swapView, "cards");
							lookingAtCards = true;
							showAllCards.setText("Back");
							archetypeTitle.setText(archList.getSelectedValue().toString());
						}
					}
				}
			});
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 1.0;
			c.weighty = 0.2;
			add(showAllCards, c);
			
			swapView.setLayout(layout);

			archListPanel.setLayout(new BoxLayout(archListPanel, BoxLayout.LINE_AXIS));
			archList = new JList<Archetype>();
			archList.setFont(uif);
			archList.setBackground(Color.darkGray);
			archList.setForeground(Color.white);
			archList.setPrototypeCellValue(new Archetype("*", "*"));
			archList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent event) {
					if(!event.getValueIsAdjusting()){
						selectArch();
					}
				}
			});
			JScrollPane scrollPane = new JScrollPane(archList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
			archListPanel.add(scrollPane);
			
			swapView.add("cards", cardListPanel);
			swapView.add("archs", archListPanel);
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 2;
			c.gridheight = 1;
			c.weightx = 1.0;
			c.weighty = 1.0;
			this.add(swapView, c);
			layout.show(swapView, "archs");
		}
	}
}