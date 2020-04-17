package tetris;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class ACanvas extends Canvas{

	private static final long serialVersionUID = -8551609743004992518L;
	BufferedImage image;
	Graphics2D g;
	
	ACanvas(short width, short height){
		super();
		setSize(width,height);
		image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
	}
	
	public abstract void drawImage();
	
	public void show() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	


}
