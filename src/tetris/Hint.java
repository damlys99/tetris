package tetris;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

@SuppressWarnings("serial")
public class Hint extends Canvas{
	

	final static short SIZE=25;
	final static short WIDTH = SIZE*5;
	static short col;
	static Block block = new Block();
	BufferedImage image;
	static Graphics2D g;
	Random gen = new Random();
	
	
	Hint(){
		super();
		setSize(WIDTH,WIDTH);
		image = new BufferedImage(WIDTH,WIDTH,BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		col=(short) gen.nextInt(7);
		block.setBlock((byte)col);
	}
	public void drawImage() {
		drawHintArea();
		drawHint();
	}
	private void drawHintArea() {
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, WIDTH);
	}
	private void drawHintBlock(byte x, byte y, byte k) {
				g.setColor(Area.COLOR[k]);
				g.fillRect(x*SIZE, y*SIZE, SIZE, SIZE);
				g.setColor(Color.white);
				g.drawRect(x*SIZE, y*SIZE, SIZE-1, SIZE-1);
	}
	public void drawHint() {

		for(byte x=0;x<4;x++) 
			for(byte y=0;y<4;y++) {
			if(block.curBlock[x][y]) drawHintBlock((byte)(x+1), (byte) (y+1),(byte) col);
			}
	}
	public void show() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	public void run() {
		drawImage();
		show();
	}
	
	

}