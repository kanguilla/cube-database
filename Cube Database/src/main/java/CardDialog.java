package main.java;

import java.awt.*;
import javax.swing.*;

public class CardDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	Font font1 = new Font("Arial", Font.BOLD, 14);
	Font font2 = new Font("Arial", Font.ITALIC, 16);
	
	public CardDialog(Card card){
		super();
		this.setSize(250, 250);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		JLabel aLabel = new JLabel(card.getName());
		aLabel.setFont(new Font("Arial", Font.BOLD, 20));
		aLabel.setForeground(Color.white);
        JPanel header = new JPanel();
        header.add(aLabel);
        header.setBackground(Color.black);
        this.getContentPane().add(header);
		
		JPanel details = new JPanel();
        details.setLayout(new GridBagLayout());
        this.getContentPane().add(details);
        GridBagConstraints c = new GridBagConstraints(); 
       // details.setBackground(MainView.toRGB(card.getColors()));

        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridwidth = 1;
        
        aLabel = new JLabel("Cost");
        aLabel.setFont(font2);
        c.gridy++;
        details.add(aLabel, c);

        aLabel = new JLabel("Type");
        aLabel.setFont(font2);
        c.gridy++;
        details.add(aLabel, c);
        
        //if(card.getTypes().contains("Creature")){
	        aLabel = new JLabel("P/T");
	        aLabel.setFont(font2);
	        c.gridy++;
	        details.add(aLabel, c);
        //}


        aLabel = new JLabel("Set");
        aLabel.setFont(font2);
        c.gridy++;
        details.add(aLabel, c);
        
        aLabel = new JLabel("Rarity");
        aLabel.setFont(font2);
        c.gridy++;;
        details.add(aLabel, c);
   		
		
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;

        aLabel = new JLabel(card.getCost());
        aLabel.setFont(font1);
        c.gridy++;
        details.add(aLabel, c);
        
        aLabel = new JLabel(card.getTypes());
        aLabel.setFont(font1);
        c.gridy++;
        details.add(aLabel, c);
        
        //if(card.getTypes().contains("Creature")){
        	aLabel= new JLabel(card.getPower() + "/"+card.getToughness());
        	aLabel.setFont(font1);
        	c.gridy++;
	        details.add(aLabel, c);
        //}
        
        aLabel.setFont(font1);
        c.gridy++;
        details.add(aLabel, c);
        
        aLabel = new JLabel(card.getRarity());
        aLabel.setFont(font1);
        c.gridy++;
        details.add(aLabel, c);
        this.getContentPane().add(details);
	}
}