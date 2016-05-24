package main.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map.Entry;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PlayersView extends JFrame{
	private static final long serialVersionUID = 1L;
	private static Font uif = new Font("Arial", Font.BOLD, 14);
	JList<Player> playerList;
	ArrayList<Player> players;
	JPanel details;
	JLabel name;
	JLabel id;
	PlayersView thisFrame;
	MainView view;
	Player selectedPlayer;
	
	public PlayersView(ArrayList<Player> initplayers, MainView view){
		super("Players");
		thisFrame = this;
		this.players = initplayers;
		this.view = view;
		this.setSize(500, 300);
		this.getContentPane().setBackground(Color.black);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		
		playerList = new JList<Player>();
		playerList.setPreferredSize(new Dimension(200, 200));
		playerList.setFont(uif);
		playerList.setBackground(Color.darkGray);
		playerList.setForeground(Color.white);
		playerList.setPrototypeCellValue(new Player("test", 0));
		playerList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if(!playerList.getValueIsAdjusting()){
					System.out.println("Selected " + playerList.getSelectedValue());
					updateDetails(playerList.getSelectedValue());
				}
			}
		});
		Player playerArray[] = new Player[1];
		playerList.setListData(players.toArray(playerArray));
		playerList.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if(!event.getValueIsAdjusting())selectedPlayer = playerList.getSelectedValue();
			}
		});
		JScrollPane scrollPane = new JScrollPane(playerList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 3;
		add(scrollPane, c);
		
		details = new JPanel();
		details.setPreferredSize(new Dimension(200, 200));
		details.setBackground(Color.black);
		details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 3;
		add(details, c);
		
		JButton addPlayer = new JButton("Add");
		addPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String name = JOptionPane.showInputDialog("Enter the player's name:","New Player");
				
				thisFrame.view.addPlayer(name);
				thisFrame.updateDetails(null);
				
				Player playerArray[] = new Player[1];
				playerList.setListData(players.toArray(playerArray));
			}
		});
		c.gridx = 0;
		c.gridy = 3;
		c.weighty = 0.0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(addPlayer, c);
		
		JButton deletePlayer = new JButton("Delete");
		deletePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(selectedPlayer != null){
					if(JOptionPane.showConfirmDialog(thisFrame,"Are you sure?","Confirm Deletion", JOptionPane.YES_NO_OPTION) == 0){
						thisFrame.view.deletePlayer(selectedPlayer);
						thisFrame.updateDetails(null);
						
						Player playerArray[] = new Player[1];
						playerList.setListData(players.toArray(playerArray));
					}
				}
			}
		});
		c.gridx = 1;
		c.gridy = 3;
		c.weighty = 0.0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(deletePlayer, c);
		
		JButton updateWin = new JButton("Update Wins");
		updateWin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(selectedPlayer != null)updateWinsDialog(selectedPlayer);
			}
		});
		c.gridx = 2;
		c.gridy = 3;
		c.weighty = 0.0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(updateWin, c);
		
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				thisFrame.dispose();
			}
		});
		c.gridx = 3;
		c.gridy = 3;
		c.weighty = 0.0;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(close, c);
	}
	
	private void updateDetails(Player p){
		details.removeAll();
		if(p != null){
			JLabel j = new JLabel("Name: "+ p.getName());
			j.setFont(new Font("Arial", Font.BOLD ,20));
			j.setOpaque(false);
			j.setForeground(Color.white);
			details.add(j);
			j = new JLabel("ID: "+Integer.toString(p.getId())); 
			j.setFont(uif);
			j.setOpaque(false);
			j.setForeground(Color.white);
			details.add(j);
			details.add(Box.createRigidArea(new Dimension(0, 20)));
			for (Entry<String, Integer> e : p.winLog.entrySet()){
				j = new JLabel("Wins with "+e.getKey() + ": " + e.getValue());
				j.setFont(uif);
				j.setOpaque(false);
				j.setForeground(Color.white);
				details.add(j);
			}
		}
		details.repaint();
		details.revalidate();
	}
		
	private void updateWinsDialog(Player p){
		JTextField deckField = new JTextField(20);
		JTextField winsField = new JTextField(5);
		
		JPanel parent = new JPanel();
		parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
		parent.add(new JLabel("Enter the deck name and  number of wins."));
		parent.add(new JLabel("Leave empty or enter 0 to delete the entry."));
		parent.add(Box.createRigidArea(new Dimension(0, 10)));
		parent.add(new JLabel("Deck name:"));
		parent.add(deckField);
		parent.add(new JLabel("Wins:"));
		parent.add(winsField);
		
		int result = JOptionPane.showConfirmDialog(null, parent, "Change Wins",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			System.out.println(deckField.getText() + ":" +winsField.getText());
			if(winsField.getText().equals("")){
				view.changeWinEntry(p, deckField.getText(), 0);
			}else if(winsField.getText().contains("+")){
				view.changeWinEntry(p, deckField.getText(), Integer.parseInt(winsField.getText().substring(1)));
			}else{
				view.changeWinEntry(p, deckField.getText(), Integer.parseInt(winsField.getText()));
			}
		}
		updateDetails(p);
	}
}
	

