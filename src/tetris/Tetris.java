package tetris;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Tetris extends JPanel implements Runnable{
	
	static Tetris tetris = new Tetris();
	static JFrame window = new JFrame();
	static Area area = new Area();
	static Hint hint = new Hint();
	final static short WIDTH = 600;
	final static short HEIGHT = 600;
	static Thread thread = new Thread(tetris);
	static JLabel level = new JLabel("Level: 0");
	static JLabel lines = new JLabel("Lines: 0");
	static JLabel points = new JLabel("Points: 0");
	static Font font = new Font("TimesRoman", Font.BOLD,24);
	static short time=10;
	static short timeBefore=50;
	static boolean start;
	Tetris(){
		super();
		setBackground(Color.darkGray);
		setLayout(null);
		time=10;
		start = true;
	}
	public static void main(String[] args) {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(tetris);
		window.setSize(WIDTH,HEIGHT);
		area.setLocation(10,10);
		tetris.add(area);
		hint.setLocation(300,10);
		tetris.add(hint);
		level.setBounds(300,120,350,150);
		level.setFont(font);
		level.setForeground(Color.pink);
		tetris.add(level);
		lines.setBounds(300,150,350,180);
		lines.setFont(font);
		lines.setForeground(Color.pink);
		tetris.add(lines);
		points.setBounds(300,180,350,210);
		points.setFont(font);
		points.setForeground(Color.pink);
		tetris.add(points);
		area.setFocusable(true);
		area.requestFocus();
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setVisible(true);
		thread.start();
	}
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while(start) {

			area.run();
			hint.run();
			points.setText("Points: "+(int)area.points);
			level.setText("Level: "+area.level);
			lines.setText("Lines: "+area.lines);
			if(area.lost()) {

				int input = JOptionPane.showConfirmDialog(null, "Przegra³eœ, twój wynik to: "+(int)area.points+"\nchcesz zagraæ ponownie?","Pora¿ka", JOptionPane.YES_NO_OPTION);
				if(input==0)
					Tetris.start=false;
					area.reset();
				if(input==1)
					System.exit(0);
			}
			try {
				thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
	}
	public static void setTime(short i) {
		timeBefore=time;
		time=i;
	}
	public static void undoTime() {
		time=timeBefore;
	}

}
