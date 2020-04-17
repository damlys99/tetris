package tetris;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.Timer;

@SuppressWarnings("serial")
public class Area extends ACanvas implements KeyListener,MouseListener, ActionListener{
	
	final static short SIZE = 25;
	final static short WIDTH = SIZE*10;
	final static short HEIGHT = SIZE*20;
	Block block = new Block();
	
	final static Color[] COLOR = {Color.red,Color.green,Color.orange,Color.magenta,Color.cyan,Color.pink,Color.pink,Color.yellow};
	final static Color[] COLOR2 = {Color.red,Color.green,Color.orange,Color.magenta,Color.cyan,Color.pink,Color.pink,Color.yellow};
	final static Color[] BACKGROUND= {Color.blue,Color.black,Color.gray,Color.white};
	final static Color[] BACKGROUND2= {Color.blue,Color.black,Color.gray,new Color(128,0,128)};
	
	byte[][] tab = new byte[10][20];
	boolean down=true,right,left;
	boolean blockDown;
	byte velY=3;
	byte blockX=3;
	byte blockY=0;
	static byte col;
	short lines=0;
	short blocks=0;
	double points=0;
	short level = 1;
	byte currentBlock;
	double pointsChange;
	boolean space;
	short delay=700;
	Timer timer = new Timer(delay,this);
	Timer timer2 = new Timer(55,this);
	Random gen = new Random();
		Area(){
		super(WIDTH,HEIGHT);
		this.addKeyListener(this);
		this.addMouseListener(this);
		timer.start();
		timer2.start();
		for(byte x=0;x<10;x++) 
			for(byte y=0;y<20;y++) {
				tab[x][y]=-1;
			}

		block.setBlock( (byte) 7);
	}
	@Override
	public void drawImage() {
		drawArea();
		ifLost();
		line();
		
	}
	  private void shuffleArray(Color[] ar)
	  {
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      Color a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	private void ifLost() {
		if(lost()) {
			Tetris.start=false;
		}
		else
			drawBlock();
	}
	public void run() {
		if(blocks==0) {
			spaceClicked();
		}
		move();
		advance();
		drawImage();
		show();

	}
	private void drawArea() {
		for(byte x=0;x<10;x++) 
			for(byte y=0;y<20;y++) {
				g.setColor(BACKGROUND[1]);
				g.fillRect(x*SIZE, y*SIZE, SIZE, SIZE);
				if(tab[x][y]>=0) { g.setColor(COLOR[tab[x][y]]);g.fillRect(x*SIZE, y*SIZE, SIZE, SIZE); g.setColor(COLOR[1]); g.drawRect(x*SIZE, y*SIZE, SIZE-1, SIZE-1);}
			}
	}
	 void drawBloc(byte x,byte y,byte k) {
		g.setColor(COLOR[k]);
		g.fillRect(x*SIZE, y*SIZE, SIZE, SIZE);
		g.setColor(COLOR[1]);
		g.drawRect(x*SIZE, y*SIZE, SIZE-1, SIZE-1);
		}
	private void line() {
		byte lineCount=0;
		byte count=0;
		for(byte x=0;x<20;x++){
			for(byte y=0;y<10;y++) {
				if(tab[y][x]>-1)count++;
			}
			if(count==10) {
				for(byte y=0;y<10;y++)
					tab[y][x]=-1;
					lineFound(x);
					lineCount++;
			}
			count=0;
		}
		switch(lineCount) {
		case 1:points+=40*level;break;
		case 2:points+=100*level;break;
		case 3:points+=300*level;break;
		case 4:points+=1200*level;break;
		}
		lines+=lineCount;
		lineCount=0;
		
	}
	private void lineFound(byte v) {
		for(byte x=(byte)v;x>0;x--){
			for(byte y=0;y<10;y++) {
				tab[y][x]=tab[y][x-1];
			}
	}
	}
	private boolean inArea() {
		if(blockY>20-getBlockYP()) {
			return false;
		}
		return true;
	}
	private boolean inAreaLeft() {
		for(byte x=0;x<4;x++) 
			for(byte y=0;y<4;y++) {
				if(blockX+getBlockXM()<=0 || (block.curBlock[x][y] && y+blockY<20 &&tab[x+blockX-1][y+blockY]>-1)) {
					return false;
				}
			}
		return true;
	}
	
	private boolean inAreaRight() {
		for(byte x=0;x<4;x++) 
			for(byte y=0;y<4;y++) {
				if(blockX+getBlockXP()>=9 || (block.curBlock[x][y] && y+blockY<20 && tab[x+blockX+1][y+blockY]>-1)) {
					return false;
				}
			}
		return true;
	}
	private void ifRotated(){
		for(byte x=0;x<4;x++) 
			for(byte y=0;y<4;y++) {
				if(block.curBlock[x][y] &&  y+blockY<20 && tab[x+blockX][y+blockY]>-1) {
					block.unrotate();;
				}
				if(blockX+x>=9) {blockX--; left=false;}
				if(blockX+x<0) {blockX++; right=false;}
			}
	}
	
	private boolean blocked() {
		for(byte x=0;x<4;x++) 
			for(byte y=0;y<4;y++) {
				if(block.curBlock[x][y] && y+blockY<20 && x+blockX>=0 && x+blockX<10 && tab[x+blockX][y+blockY]>-1)
					return true;
			}
		return false;
	}
	private void drawBlock() {
		for(byte x=0;x<4;x++) 
			for(byte y=0;y<4;y++) {
			if(block.curBlock[x][y] && blockX+x<10 && blockX+x>=0 && blockY+y<20 && blockY+y>=0 &&tab[x+blockX][y+blockY]<0) drawBloc((byte)(blockX+x), (byte) (blockY+y),(byte) col);
			}
	}
	
	private byte getBlockYP() {
		byte max=0;
		byte min=4;
		for(byte x=0;x<4;x++)
			for(byte y=0;y<4;y++) {
				if(block.curBlock[x][y] && y>max) max=y;
				if(block.curBlock[x][y] && y<min) min=y;
			}
			
		return (byte) (max);
	}
	private byte getBlockXP() {
		byte max=0;
		byte min=4;
		for(byte x=0;x<4;x++)
			for(byte y=0;y<4;y++) {
				if(block.curBlock[x][y] && x>max) max=(byte) (x);
				if(block.curBlock[x][y] && x<min) min=(byte) (x);
			}
		return (byte) (max);
	}
	private byte getBlockXM() {
		byte max=4;
		for(byte x=0;x<4;x++)
			for(byte y=0;y<4;y++) {
				if(block.curBlock[x][y] && x<max) max=x;
			}
		return max;
	}
	private void move() {
		//ifDown();
		//ifStopDown();
		//ifSideways();


	}
	private void goDown(){
		if(blockY<20 && !blocked()) {
				
				blockY++;
				//addPoints(1);
		}
	}
	private boolean end() {
				if(blockY>19-getBlockYP()) {
			return true;
		}
		return false;
	}
	private void ifDown() {
		if(inArea()) {
		if(down) {
			blockY++;
			//addPoints(5);
		}
		if(end()|| blocked()) {
			for(byte x=0;x<4;x++)
				for(byte y=0;y<4;y++) {
					if(block.curBlock[x][y] && y+blockY-1<20 && y+blockY-1>=0)tab[x+blockX][y+blockY-1]=col;
				}
			down=false;
			blocks++;
			currentBlock=(byte) Hint.col;
			Hint.col=(short) gen.nextInt(7);
			Hint.block.setBlock((byte)(Hint.col));
			block.setBlock(currentBlock);
			blockY=0;
			blockX=3;
		}
	}
	}
	private void ifSideways() {
			if(right && inAreaRight()) {
				blockX++;
			}
			if(left && inAreaLeft()) {
				blockX--;
			}
	}
	private void spaceClicked() {
		space=true;
		for(byte x=0;x<4;x++)
			for(byte y=0;y<4;y++) {
			while(block.curBlock[x][y] && blockY+y<19 && !blocked() && !end()) {
				blockY++;
			}
		space=false;
		}
	}
	
	public boolean lost() {
		for(byte i=0;i<10;i++)
			if(tab[i][0]>0)
			return true;
		return false;
	}
	public void reset() {
		lines=0;
		points=0;
		level=1;
		timer.setDelay(700);
		for(byte x=0;x<10;x++) 
			for(byte y=0;y<20;y++) {
				tab[x][y]=-1;
			}
		for(byte i=0;i<COLOR.length;i++) {
			COLOR[i]=COLOR2[i];
		}
		for(byte i=0;i<BACKGROUND.length;i++) {
			BACKGROUND[i]=BACKGROUND2[i];
		}
		Tetris.start=true;
	}

	public void advance() {
		if(lines>=8*level) {
			level++;
			delay=(short) Math.round(0.85*delay);
			int a=delay;

			timer.setDelay(a);
			System.out.println(a);
			shuffleArray(COLOR);
			shuffleArray(BACKGROUND);
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_DOWN) {
			down=true;
		}
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
			right = true;
		if(e.getKeyCode()==KeyEvent.VK_LEFT)
			left = true;
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
			down=false;
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
			right=false;
		if(e.getKeyCode()==KeyEvent.VK_LEFT)
			left=false;
		if(e.getKeyCode()==KeyEvent.VK_UP  && currentBlock!=0) {
			block.rotate(block.curBlock);
			ifRotated();
		}
		if(e.getKeyChar()==KeyEvent.VK_SPACE) {
			down=false;
			spaceClicked();
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {

		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//tab[e.getX()/SIZE][e.getY()/SIZE]=2;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==timer)
		if(!down && !space)goDown();
		if(e.getSource()==timer2)
			ifDown();
			ifSideways();
			
		
	}

}
