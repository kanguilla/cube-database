package main.java;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class CardListView extends JPanel {
	private static final long serialVersionUID = 1L;
	private CardListView thisFrame;
	private ArrayList<Card> cards = new ArrayList<Card>();
	private JTable cardList;
	private JButton cardFilter;
	private Font uif = new Font("Arial", Font.BOLD, 14);
	private CubeConnection databaseConnection;
	private JTextField cardFilterText;
	private Deck deck;
	private Archetype archetype;

	public CardListView(CubeConnection connection, ArrayList<Card> initialCards, Deck deckContext, Archetype archContext) {
		this.deck = deckContext;
		this.archetype = archContext;
		this.thisFrame = this;
		this.setLayout(new GridBagLayout());
		this.databaseConnection = connection;
		
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
				if(Character.isAlphabetic(arg0.getKeyChar())){
					databaseConnection.setTextFilter(cardFilterText.getText() + arg0.getKeyChar());
					update();
				}else{
					databaseConnection.setTextFilter(cardFilterText.getText());
					update();
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
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
				databaseConnection.setTextFilter(cardFilterText.getText());
				update();
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
		JCheckBox whiteBox = new ColorBox("White", "W", this);
		colorBar.add(whiteBox);
		JCheckBox blueBox = new ColorBox("Blue", "U", this);
		colorBar.add(blueBox);
		JCheckBox blackBox = new ColorBox("Black", "B", this);
		colorBar.add(blackBox);
		JCheckBox redBox = new ColorBox("Red", "R", this);
		colorBar.add(redBox);
		JCheckBox greenBox = new ColorBox("Green", "G", this);
		colorBar.add(greenBox);
		
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
		
		String col[] = {"Name","Cost", "Type", "P/T", "Text"};
		CardTableModel tableModel = new CardTableModel(col, 0);
		for (Card card : cards){
			String[] data = {card.name, card.manaCost, String.join(" ", card.types) + " - " + String.join(" ", card.subtypes), card.power + "/" + card.toughness, card.text};
			tableModel.addRow(data);
		}
		cardList = new JTable(tableModel);
		cardList.getTableHeader().setBackground(Color.black);
		cardList.getTableHeader().setForeground(Color.white);
		cardList.setFillsViewportHeight(true);
		cardList.setShowGrid(false);
		cardList.setBackground(Color.black);
		cardList.setForeground(Color.white);
		cardList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					JTable table = (JTable) event.getSource();
					int index = table.getSelectedRow();
					Card c = cards.get(index);
					CardDialog dialog = new CardDialog(c);
					dialog.setVisible(true);
				}
			}});
		JScrollPane scrollPane = new JScrollPane(cardList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);

		update(initialCards);
	}

	public void update(ArrayList<Card> newCards) {
		System.out.println("Updating with " + newCards.size() + " cards");
		this.cards = newCards;
		String col[] = { "Name", "Cost", "Type", "P/T", "Text" };
		CardTableModel tableModel = new CardTableModel(col, 0);
		for (Card card : cards) {
			String[] data = { card.name, card.manaCost,
					String.join(" ", card.types) + " - " + String.join(" ", card.subtypes),
					card.power + "/" + card.toughness, card.text };
			tableModel.addRow(data);
			cardList.setModel(tableModel);
		}
	}
	
	public void update() {	
		this.cards = databaseConnection.query();
		String col[] = { "Name", "Cost", "Type", "P/T", "Text" };
		CardTableModel tableModel = new CardTableModel(col, 0);
		for (Card card : cards) {
			String[] data = { card.name, card.manaCost,
					String.join(" ", card.types) + " - " + String.join(" ", card.subtypes),
					card.power + "/" + card.toughness, card.text };
			tableModel.addRow(data);
			cardList.setModel(tableModel);
		}
	}

	public void selectCard() {
		/*
		Card selectedCard = (Card) (cardList.getSelectedValue());
		if (selectedCard != null) {
			System.out.println("Card Selected: " + selectedCard);
			/*
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
		*/
	}

	class ColorRenderer extends JLabel implements ListCellRenderer<Object> {
		private static final long serialVersionUID = 1L;
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (value == null)return this;
			setText(value.toString());
			if (isSelected || cellHasFocus) {
				setOpaque(true);
				setBackground(Color.lightGray);
				setForeground(Color.black);
				return this;
			}
			setOpaque(false);
			Card c = (Card) value;
			setForeground(toRGB(c.getColors()));
			return this;
		}
	}

	class ColorBox extends JCheckBox {
		private static final long serialVersionUID = 1L;
		String color;
		ColorBox c;

		public ColorBox(String title, final String color, final CardListView parent) {
			super(title);
			c = this;
			this.color = color;
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					AbstractButton button = (AbstractButton) actionEvent.getSource();
					boolean selected = button.getModel().isSelected();
					if (selected){
						parent.databaseConnection.addColorFilter(color);
						System.out.println("Selected "+ color);
					}else if (!selected){
						parent.databaseConnection.removeColorFilter(color);
						System.out.println("Deselected "+ color);
					}
					parent.update();
				}
			});
		}
	}

	public Color toRGB(String[] colors) {
		Color GOLD = new Color(255, 245, 135);
		Color WHITE = new Color(240, 230, 165);
		Color BLUE = new Color(150, 205, 222);
		Color BLACK = new Color(128, 128, 128);
		Color RED = new Color(255, 110, 50);
		Color GREEN = new Color(125, 215, 95);
		Color BROWN = new Color(190, 160, 100);
		if (colors == null || colors.length > 1) {
			return(GOLD);
		}
		
		switch (colors[0]) {
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
	
	class CardTableModel extends DefaultTableModel{
		private static final long serialVersionUID = 1L;
		public CardTableModel(String[] col, int i) {
			super(col, i);
		}
		@Override
	    public boolean isCellEditable(int row, int column) {
	       return false;
	    }
	}
}
