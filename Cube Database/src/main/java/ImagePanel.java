package main.java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int STRETCH = 1;
	public static final int TILE = 2;
	public static final int FILL = 3;
	public static final int CENTER_SCALE = 4;
	public static final int CENTER = 5;
	public static final int FIT = 6;

	Image img;
	int mode = 0;
	int imgWidth = 0;
	int imgHeight = 0;
	double timePercentage = 0;
	
	public void setImageSize(int width, int height){
		imgWidth = width;
		imgHeight = height;
	}
	
	public ImagePanel(Image i) {img = i;}
	public ImagePanel(Image i, int m) {img = i; mode = m;}
	public ImagePanel(String i) {
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream(i));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ImagePanel(String i, int mode) {
		this(i);
		this.mode = mode;
	}
	public ImagePanel(String i, int mode, int w, int h) {
		this(i, mode);
		this.imgWidth = w;
		this.imgHeight = h;
	}
	public ImagePanel() {}
	public void setImage(Image i){
		img = i;
	}
	@Override
	protected void paintComponent(Graphics g) {
		this.setOpaque(false);
		super.paintComponent(g);
		switch (mode) {
		case STRETCH:
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
			break;
		case TILE:
			if(imgWidth == 0)imgWidth = img.getWidth(null);
			if(imgHeight == 0)imgHeight = img.getHeight(null);
			
			int j = 0;
			while (j < this.getHeight()) {
				int i = 0;
				while (i < this.getWidth()) {
					g.drawImage(img, i, j, imgWidth, imgHeight, null);
					i += imgWidth;
				}
				j = j + imgHeight;
			}

			break;
		case FILL:
			double d = img.getHeight(null) * ((double) this.getWidth() / img.getWidth(null));
			g.drawImage(img, 0, 0, this.getWidth(), (int) d, null);
			break;
		case CENTER_SCALE:
			double r = (double) this.getHeight() / img.getHeight(null);
			double x = (this.getWidth() - img.getWidth(null) * r) / 2;
			g.drawImage(img, (int) x, 0, (int) (img.getWidth(null) * r), (int) (img.getHeight(null) * r), null);
			break;
		case CENTER:
			g.drawImage(img, (this.getWidth() - img.getWidth(null)) / 2,
					(this.getHeight() - img.getHeight(null)) / 2, null);
			break;
		case FIT:
			r = (double) this.getWidth() / img.getWidth(null);
			x = (this.getWidth() - img.getWidth(null) * r) / 2;
			g.drawImage(img, (int) x, (int) -((img.getHeight(null) - this.getHeight())/2 * r), (int) (img.getWidth(null) * r), (int) (img.getHeight(null) * r), null);
			break;
		default:
			g.drawImage(img, 0, 0, null);
			break;
		}
		if(timePercentage > 0){
			g.setColor(new Color(0, 0, 0, 70));
			g.fillRect(0, 0, (int) (this.getWidth() * timePercentage), this.getHeight());
		}
		
	}

	public void setTimePercentage(double d) {
		this.timePercentage = d;
	}
}