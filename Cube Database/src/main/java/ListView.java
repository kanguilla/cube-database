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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public abstract class ListView extends JPanel{
	private static final long serialVersionUID = 1L;
	public ListView thisFrame;
	private ArrayList<Card> cards = new ArrayList<Card>();
	private JTable cardList;
	private JButton cardFilter;
	private Font uif = new Font("Arial", Font.BOLD, 14);
	private DatabaseMtg databaseConnection;
	private JTextField cardFilterText;
	private Set<String> args = new TreeSet<String>();
	String prototype = "select * from cards ";
	String ordering = "order by name asc";
	String textFilter = "";
	
	public abstract String[] getCol();
	public abstract String[] getRow(Card c);
	
	public ListView(DatabaseMtg connection) {
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
					setTextFilter(cardFilterText.getText() + arg0.getKeyChar());
					update();
				}else{
					setTextFilter(cardFilterText.getText());
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
				setTextFilter(cardFilterText.getText());
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
		JCheckBox whiteBox = new ColorBox("White", "White", this);
		colorBar.add(whiteBox);
		JCheckBox blueBox = new ColorBox("Blue", "Blue", this);
		colorBar.add(blueBox);
		JCheckBox blackBox = new ColorBox("Black", "Black", this);
		colorBar.add(blackBox);
		JCheckBox redBox = new ColorBox("Red", "Red", this);
		colorBar.add(redBox);
		JCheckBox greenBox = new ColorBox("Green", "Green", this);
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
		
		CardTableModel tableModel = new CardTableModel(getCol(), cards);
		cardList.setModel(tableModel);
		cardList = new JTable(tableModel);
		cardList.getTableHeader().setBackground(Color.black);
		cardList.getTableHeader().setForeground(Color.white);
		cardList.setFillsViewportHeight(true);
		cardList.setDefaultRenderer(Object.class, new ColorRenderer());
		cardList.setShowGrid(false);
		cardList.setBackground(Color.black);
		cardList.setForeground(Color.white);
		cardList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					JTable table = (JTable) event.getSource();
					int index = table.getSelectedRow();
					Card c = cards.get(index);
					CardDialog dialog = new CardDialog(c, connection);
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

	}
	
	public void update() {	
		ArrayList<Card> cards = new ArrayList<Card>();
		// Construct the query
		String sql = prototype;
		int counter = 0;
		for (String s : args) {
			System.out.println("Arg# :" + s);
			if (counter > 0) {
				sql += "and ";
			} else {
				sql += "where ";
			}
			sql += s;
			counter++;
		}
		if (textFilter.length() > 0) {
			if (counter == 0) {
				sql += "where " + textFilter;
			} else {
				sql += "and " + textFilter;
			}
		}
		sql += (ordering + ";");
		System.out.println("Search as \"" + sql + "\"");
		cards = databaseConnection.queryCards(sql);
		this.cards = cards;
		CardTableModel tableModel = new CardTableModel(getCol(), 0);
		for (Card card : cards) {
			tableModel.addRow(getRow(card));
			cardList.setModel(tableModel);
		}
	}
	
	public void update(String sql) {	
		this.cards = databaseConnection.queryCards(sql);
		CardTableModel tableModel = new CardTableModel(getCol(), 0);
		for (Card card : cards) {
			tableModel.addRow(getRow(card));
			cardList.setModel(tableModel);
		}
	}

	public void addColorFilter(String color){
		args.add("colors like \"%"+color+"%\" ");
	}
	
	public void removeColorFilter(String color){
		args.remove("colors like \"%"+color+"%\" ");
	}
	

	public void setTextFilter(String text) {
		textFilter = ("name like \"%"+text+"%\" ");
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

	class ColorRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			try {
				String cls = databaseConnection.query("select colors from cards where name=\"" + (String) table.getModel().getValueAt(row, 1) + "\";").getString("colors");
				setForeground(toRGB(cls.split(",")));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return this;
		}
	}

	class ColorBox extends JCheckBox {
		private static final long serialVersionUID = 1L;
		String color;
		ColorBox c;

		public ColorBox(String title, final String color, final ListView parent) {
			super(title);
			c = this;
			this.color = color;
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					AbstractButton button = (AbstractButton) actionEvent.getSource();
					boolean selected = button.getModel().isSelected();
					if (selected){
						addColorFilter(color);
						System.out.println("Selected "+ color);
					}else if (!selected){
						removeColorFilter(color);
						System.out.println("Deselected "+ color);
					}
					parent.update();
				}
			});
		}
	}

	public static Color toRGB(String[] strings) {
		Color GOLD = new Color(255, 245, 135);
		Color WHITE = new Color(240, 235, 215);
		Color BLUE = new Color(150, 205, 222);
		Color BLACK = new Color(128, 128, 128);
		Color RED = new Color(255, 110, 50);
		Color GREEN = new Color(125, 215, 95);
		Color BROWN = new Color(190, 160, 100);
		if (strings == null || strings.length > 1) {
			return(GOLD);
		}
		
		switch (strings[0]) {
		case ("White"):
			return(WHITE);
		case ("Blue"):
			return(BLUE);
		case ("Black"):
			return(BLACK);
		case ("Red"):
			return(RED);
		case ("Green"):
			return(GREEN);
		default:
			return(BROWN);
		}
		
	}
	
	class CardTableModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;
		
		List<Card> cards;
		String[] col;
		
		public CardTableModel(String[] col, List<Card> cards) {
			this.col = col;
			this.cards = cards;
		}
		@Override
	    public boolean isCellEditable(int row, int column) {
	       return false;
	    }
		@Override
		public int getRowCount() {
			return cards.size();
		}
		@Override
		public int getColumnCount() {
			return col.length;
		}
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Card c = cards.get(rowIndex);
			
			
			return c;
		}
	}
}
