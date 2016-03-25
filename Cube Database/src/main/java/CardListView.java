package main.java;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CardListView extends JPanel {
	private static final long serialVersionUID = 1L;
	private MainView parent;
	private CardListView thisFrame;
	private ArrayList<Card> cards;
	private JList<Card> cardList;
	private JButton cardFilter;
	private Font uif = new Font("Arial", Font.BOLD, 14);
	private Connection databaseConnection;
	private Set<String> selectedColors = new TreeSet<String>();
	private JTextField cardFilterText;
	private Deck deck;
	private Archetype archetype;
	
	public JList<Card> getList() {
		return cardList;
	}

	@SuppressWarnings("unchecked")
	public CardListView(MainView parent, Connection connection, ArrayList<Card> initialCards, Deck deckContext, Archetype archContext) {
		this.databaseConnection = connection;
		this.parent = parent;
		this.deck = deckContext;
		this.archetype = archContext;
		this.thisFrame = this;
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		//Text filter for name and type
		cardFilterText = new JTextField("");
		cardFilterText.setFont(uif);
		cardFilterText.setBackground(Color.black);
		cardFilterText.setForeground(Color.white);
		cardFilterText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyChar() == KeyEvent.VK_ENTER)filterCards();
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		add(cardFilterText, c);
		cardFilter = new JButton("Filter");
		cardFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				filterCards();
			}
		});
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		add(cardFilter, c);
		
		//check boxes for color filter
		JPanel colorBar = new JPanel();
		colorBar.setBackground(Color.cyan);
		colorBar.setLayout(new BoxLayout(colorBar, BoxLayout.X_AXIS));
		JCheckBox whiteBox = new ColorBox("White", "W");
		colorBar.add(whiteBox);
		JCheckBox blueBox = new ColorBox("Blue", "U");
		colorBar.add(blueBox);
		JCheckBox blackBox = new ColorBox("Black", "B");
		colorBar.add(blackBox);
		JCheckBox redBox = new ColorBox("Red", "R");
		colorBar.add(redBox);
		JCheckBox greenBox = new ColorBox("Green", "G");
		colorBar.add(greenBox);
		
		String[] options = new String[]{"All Rarities", "Common", "Uncommon", "Rare", "Mythic Rare"};
		JComboBox<String> rarityBox = new JComboBox<String>(options);
		rarityBox.setSelectedIndex(0);
		rarityBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				String s = (String) ((JComboBox<String>)event.getSource()).getSelectedItem();
				filterByRarity(s);
			}
		});
		colorBar.add(rarityBox);
		JButton clearSelection = new JButton("Deselect Card");
		clearSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				cardList.clearSelection();
			}
		});
		colorBar.add(clearSelection);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		add(colorBar, c);

		
		
		
		cardList = new JList<Card>();
		cardList.setCellRenderer(new ColorRenderer());
		cardList.setFont(uif);
		cardList.setBackground(Color.black);
		cardList.setForeground(Color.white);
		cardList.setPrototypeCellValue(new Card("*", "*", "*", "*", "*", 0, 0, "*"));
		cardList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					selectCard();
				}
			}
		});
		cardList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					JList<Card> theList = (JList<Card>) event.getSource();
					int index = theList.locationToIndex(event.getPoint());
					Card c = (Card) theList.getModel().getElementAt(index);
					CardDialog dialog = new CardDialog((Frame) thisFrame.parent, "Card Details", c);
					dialog.setVisible(true);
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(cardList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);
		update(initialCards);
	}

	private void filterCards() {
		ArrayList<Card> cardSearchResults = new ArrayList<Card>();
		String searchPrototype = cardFilterText.getText().trim();
		PreparedStatement prep;
		try {
			prep = databaseConnection.prepareStatement("select * from cards where name like ? or typeline like ? order by name asc;");
			prep.setString(1, "%" + searchPrototype + "%");
			prep.setString(2, "%" + searchPrototype + "%");
			if (searchPrototype.equals("*"))
				prep = databaseConnection.prepareStatement("select * from cards;");
			if (searchPrototype.equals("%"))
				prep = databaseConnection.prepareStatement("select * from cards;");
			if (searchPrototype.equals(""))
				prep = databaseConnection.prepareStatement("select * from cards;");
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				Card card = new Card(rs.getString("name"), rs.getString("cost"), rs.getString("color"),
						rs.getString("typeline"), rs.getString("rarity"), rs.getInt("power"), rs.getInt("toughness"),
						rs.getString("mset"));
				cardSearchResults.add(card);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cardList.clearSelection();
		update(cardSearchResults);
	}

	private void filterByColor() {
		ArrayList<Card> cardSearchResults = new ArrayList<Card>();

		String searchPrototype = "%";
		for (String s : selectedColors) {
			searchPrototype += (s + "%");
		}
		System.out.println("Filter: " + searchPrototype);
		PreparedStatement prep;
		try {
			if(deck != null){
				prep = databaseConnection.prepareStatement("select * from deckLists join cards on cardName=name where deckName=? and color like ? order by name asc;");
				prep.setString(1, deck.name);
				prep.setString(2, searchPrototype);
			}else if(archetype != null){
				prep = databaseConnection.prepareStatement("select * from archetypeMembers join cards on cardName=name where archetypeName=? and color like ? order by name asc;");
				prep.setString(1, archetype.name);
				prep.setString(2, searchPrototype);
			}else{
				prep = databaseConnection.prepareStatement("select * from cards where color like ? order by name asc;");
				prep.setString(1, searchPrototype);
			}

			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				Card card = new Card(rs.getString("name"), rs.getString("cost"), rs.getString("color"),
						rs.getString("typeline"), rs.getString("rarity"), rs.getInt("power"), rs.getInt("toughness"),
						rs.getString("mset"));
				cardSearchResults.add(card);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cardList.clearSelection();
		update(cardSearchResults);
	}
	
	private void filterByRarity(String rarity) {
		ArrayList<Card> cardSearchResults = new ArrayList<Card>();
		if(rarity.equals("All Rarities")){
			rarity = "%CURM%";
		}
		System.out.println("Filter: " + rarity);
		PreparedStatement prep;
		try {
			if(deck != null){
				prep = databaseConnection.prepareStatement("select * from deckLists join cards on cardName=name where deckName=? and rarity like ? order by name asc;");
				prep.setString(1, deck.name);
				prep.setString(2, rarity.substring(0, 1).toUpperCase());
			}else if(archetype != null){
				prep = databaseConnection.prepareStatement("select * from archetypeMembers join cards on cardName=name where archetypeName=? and rarity like ? order by name asc;");
				prep.setString(1, archetype.name);
				prep.setString(2, rarity.substring(0, 1).toUpperCase());
			}else{
				prep = databaseConnection.prepareStatement("select * from cards where rarity like ? order by name asc;");
				prep.setString(1, rarity.substring(0, 1).toUpperCase());
			}

			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				Card card = new Card(rs.getString("name"), rs.getString("cost"), rs.getString("color"),
						rs.getString("typeline"), rs.getString("rarity"), rs.getInt("power"), rs.getInt("toughness"),
						rs.getString("mset"));
				cardSearchResults.add(card);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cardList.clearSelection();
		update(cardSearchResults);
	}

	public void update(ArrayList<Card> newCards) {
		this.cards = newCards;

		if (!cards.isEmpty()) {
			cardList.setListData(cards.toArray(new Card[1]));
		} else {
			cardList.setListData(new Card[] {});
		}
	}

	public void selectCard() {
		Card selectedCard = (Card) (cardList.getSelectedValue());
		if (selectedCard != null) {
			System.out.println("Card Selected: " + selectedCard);
			ArrayList<Integer> values = new ArrayList<Integer>();
			for (int i = 0; i < parent.archetypes.size(); i++) {
				if (parent.archetypes.get(i).members.contains(selectedCard.toString())) {
					values.add(i);
				}
			}
			int[] indices = new int[values.size()];
			for (int i = 0; i < values.size(); i++) {
				indices[i] = values.get(i);
			}
			parent.archList.setSelectedIndices(indices);
		}
	}

	class ColorRenderer extends JLabel implements ListCellRenderer<Object> {
		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			if (value == null) {
				return this;
			}

			setText(value.toString());
			setFont(new Font("Arial", Font.BOLD, 14));
			if (isSelected || cellHasFocus) {
				setOpaque(true);
				setBackground(Color.lightGray);
				setForeground(Color.black);
				return this;
			}
			setOpaque(false);
			Card c = (Card) value;
			setForeground(MainView.toRGB(c.getColors()));
			return this;
		}
	}

	class ColorBox extends JCheckBox {
		private static final long serialVersionUID = 1L;
		String color;
		ColorBox c;

		public ColorBox(String title, String color) {
			super(title);
			c = this;
			this.color = color;
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					AbstractButton button = (AbstractButton) actionEvent.getSource();
					boolean selected = button.getModel().isSelected();
					if (selected)
						selectedColors.add(c.color);
					if (!selected)
						selectedColors.remove(c.color);
					thisFrame.filterByColor();
				}
			});
		}
	}
}
