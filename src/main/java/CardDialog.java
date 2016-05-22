package main.java;

import java.awt.*;
import java.util.Arrays;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class CardDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	Font font1 = new Font("Arial", Font.BOLD, 14);
	Font font2 = new Font("Arial", Font.ITALIC, 16);
	Card card;
	
	JDialog thisFrame;
	
	public CardDialog(Card card){
		super();
		this.setSize(300, 300);
		thisFrame = this;
		this.card = card;
		this.getContentPane().setLayout(new MigLayout("fill", "[][]", "[][]"));
		JLabel aLabel = new JLabel(card.getName());
		aLabel.setFont(new Font("Arial", Font.BOLD, 20));
		aLabel.setForeground(Color.white);
		aLabel.setBackground(Color.black);
		aLabel.setOpaque(true);
		this.getContentPane().add(aLabel, "grow, cell 0 0 2 1");
		
		final JPanel details = new JPanel(new MigLayout("fill"));
        details.setBackground(CardListView.toRGB(card.getColors()));
        aLabel = new JLabel("Cost");
        aLabel.setFont(font2);
        details.add(aLabel);

        aLabel = new JLabel(card.getCost());
        aLabel.setFont(font1);
        details.add(aLabel, "wrap");
        
        aLabel = new JLabel("Type");
        aLabel.setFont(font2);
        details.add(aLabel);
        
        aLabel = new JLabel(String.join(" ", card.types) + ((card.subtypes[0].length()>1) ? " - " : "") + String.join(" ", card.subtypes));
        aLabel.setFont(font1);
        details.add(aLabel, "wrap");
        
        aLabel = new JLabel("Text");
        aLabel.setFont(font2);
        details.add(aLabel);
        
        JTextArea area = new JTextArea(card.getText());
        area.setFont(font1);
        area.setOpaque(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        details.add(area, "growx, wrap");
        
        if(Arrays.asList(card.getTypes()).contains("Creature")){
	        aLabel = new JLabel("P/T");
	        aLabel.setFont(font2);
	        details.add(aLabel);
	        
	        aLabel= new JLabel(card.getPower() + "/"+card.getToughness());
        	aLabel.setFont(font1);
	        details.add(aLabel, "wrap");
        }
        
       
        this.getContentPane().add(details, "grow, cell 0 1 2 1");
	}
}