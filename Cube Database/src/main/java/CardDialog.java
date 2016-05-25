package main.java;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CardDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	Font font1 = new Font("Arial", Font.BOLD, 14);
	Font font2 = new Font("Arial", Font.ITALIC, 16);
	Card card;
	
	JDialog thisFrame;
	DatabaseMtg connection;
	
	public CardDialog(Card card, DatabaseMtg connection){
		super();
		this.setSize(340, 500);
		thisFrame = this;
		this.connection = connection;
		this.card = card;

		JTabbedPane tabbedPane = new JTabbedPane();
		this.getContentPane().add(tabbedPane);
		
		try {
			ResultSet rs = connection.query("select setName, mciCode, number from contents join sets on sets.code=contents.setName where cardName=\"" + card.name + "\";");
			while(rs.next()){
				String mciCode = rs.getString("mciCode");
			    String number = rs.getString("number");
			    String setName = rs.getString("setName");
				System.out.println("   " + card.name + "...");
				String imageUrl = "http://magiccards.info/scans/en/" +mciCode+ "/" + number + ".jpg";
				URLConnection hc = new URL(imageUrl).openConnection();
				hc.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
				hc.connect();
				Image img = ImageIO.read(hc.getInputStream());
				ImagePanel cardImage = new ImagePanel(img);
				tabbedPane.addTab(setName, cardImage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}