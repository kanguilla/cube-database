package main.java;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
		this.setSize(300, 600);
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
        
		System.out.println("   " + card.name + "...");
		String imageUrl = "http://magiccards.info/scans/en/" + card.set + "/" + card.number + ".jpg";
		String destinationFile = "C:\\Users\\Khalil\\AppData\\Local\\Forge\\Cache\\pics\\cards\\" + card.name
				+ ".full.jpg";
		try {

			URLConnection hc = new URL(imageUrl).openConnection();
			hc.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			hc.connect();
			InputStream is = hc.getInputStream();
			OutputStream os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}