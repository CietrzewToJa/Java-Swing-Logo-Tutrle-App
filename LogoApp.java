import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

class LogoApp extends JPanel {
	int frameWidth;
	int frameHeight;
	int drawPanelWidth;
	int drawPanelHeight;
	boolean isPenDown;
	int counter = 1;
	ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
	Turtle turtle = new Turtle();
	JButton forward;
	JButton left;
	JButton right;
	JButton pen_up;
	JButton pen_down;
	JButton clean;
	JButton quit;
	JLabel positionInfo;
	JLabel directionInfo;
	JLabel penStateInfo;
	
	public LogoApp(int frameWidth, int frameHeight) {
		isPenDown = false;
		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;
		drawPanelWidth = frameWidth;
		drawPanelHeight = 2*frameHeight/3;
		
		initSurface();
		work();
	}
	
	void initSurface() {
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, new DrawPanel());
		add(BorderLayout.EAST, new ButtonPanel());
		add(BorderLayout.WEST, new TextPanel());
	}
	
	void work() {
		turtle.setPosition(drawPanelWidth/2, drawPanelHeight/2);
		moves.add(new ArrayList<Integer>(Arrays.asList(turtle.getX(), turtle.getY())));
		buttonHandler();
	}
	
	String printPenInformation() {
		if(isPenDown) {
			return "Pen is down";
		} else 
			return "Pen is up";
	}
	
	void buttonHandler() {
		forward.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				turtle.setPosition(turtle.getX()+turtle.direction.getX(), turtle.getY() + turtle.direction.getY());
				
				if(isPenDown) {
					moves.add(new ArrayList<Integer>(Arrays.asList(turtle.getX(), turtle.getY(), 1)));
				} else {
					moves.add(new ArrayList<Integer>(Arrays.asList(turtle.getX(), turtle.getY(), 0)));
				}
				repaint();
				revalidate();
				positionInfo.setText(turtle.printPosition());
				counter++;
			}
		});
		
		left.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				turtle.turnLeft();
				directionInfo.setText(turtle.printDirection());
			}
		});
		
		right.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				turtle.turnRight();
				directionInfo.setText(turtle.printDirection());
			}
		});
		
		clean.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				moves = new ArrayList<ArrayList<Integer>>();
				turtle = new Turtle();
				counter = 1;
				turtle.setPosition(drawPanelWidth/2, drawPanelHeight/2);
				moves.add(new ArrayList<Integer>(Arrays.asList(turtle.getX(), turtle.getY())));
				directionInfo.setText(turtle.printDirection());
				positionInfo.setText(turtle.printPosition());
				repaint();
			}
		});
		
		quit.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		pen_down.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				isPenDown = true;
				penStateInfo.setText(printPenInformation());
			}
		});
		
		pen_up.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				isPenDown = false;		
				penStateInfo.setText(printPenInformation());

			}
		});
	}

	class DrawPanel extends JPanel {
		
		public DrawPanel() {
			initSurface();
		}
		
		void initSurface() {
			setPreferredSize(new Dimension(drawPanelWidth, drawPanelHeight));
			setBackground(Color.white);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
				linePaint(g);
		}
		
		void linePaint(Graphics g) {			
			Graphics2D g2d = (Graphics2D) g;
			
			for(int i = 0; i+1 < counter; i++) {
				if(moves.get(i+1).get(2) == 0)
					continue;
				
				g2d.drawLine(moves.get(i).get(0), moves.get(i).get(1), moves.get(i+1).get(0), moves.get(i+1).get(1));
			}
		}
	}
	
	class TextPanel extends JPanel {
		
		public TextPanel() {
			initLabels();
			initSurface();
		}
		
		void initSurface() {
			setPreferredSize(new Dimension(3*frameWidth/6, frameHeight/3));
			setBackground(Color.cyan);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(new EmptyBorder(new Insets(40, 80, 50, 50)));
			add(positionInfo);
			add(directionInfo);
			add(penStateInfo);
		}
		
		void initLabels() {
			positionInfo = new JLabel(turtle.printPosition());
			positionInfo.setHorizontalAlignment(SwingConstants.CENTER);
			positionInfo.setFont(new Font("Verdana", Font.PLAIN, 15));
			
			directionInfo = new JLabel(turtle.printDirection());
			directionInfo.setHorizontalAlignment(SwingConstants.CENTER);
			directionInfo.setFont(new Font("Verdana", Font.PLAIN, 15));

			penStateInfo = new JLabel(printPenInformation());
			penStateInfo.setHorizontalAlignment(SwingConstants.CENTER);
			penStateInfo.setFont(new Font("Verdana", Font.PLAIN, 15));
		}
	}
	
	class ButtonPanel extends JPanel {
		
		public ButtonPanel() {
			initSurface();
		}
		
		void initSurface() {
			setPreferredSize(new Dimension(3*frameWidth/6, frameHeight/3));
			setBackground(Color.pink);
			setLayout(new BorderLayout());
			add(BorderLayout.EAST, new SettingsPanel());
			add(BorderLayout.WEST, new MovePanel());
		}
		
		class SettingsPanel extends JPanel {
			
			SettingsPanel() {
				setPreferredSize(new Dimension(3*frameWidth/12, frameHeight/3));
				setLayout(new BorderLayout());
				add(BorderLayout.NORTH, quit = new JButton("quit"));
				add(BorderLayout.WEST, pen_up = new JButton("     pen up     "));
				add(BorderLayout.EAST, pen_down = new JButton("   pen down   "));
			}
		}
		
		class MovePanel extends JPanel {
			
			MovePanel() {
				setPreferredSize(new Dimension(3*frameWidth/12, frameHeight/3));
				setLayout(new BorderLayout());
				add(BorderLayout.NORTH, clean = new JButton("clean"));
				add(BorderLayout.CENTER, forward = new JButton("forward"));
				add(BorderLayout.WEST, left = new JButton("left"));
				add(BorderLayout.EAST, right = new JButton("right"));
			}
		}
	}
}

class Turtle {
	Position position;
	Directions direction;
	
	Turtle() {
		direction = Directions.NORTH;
		setPosition(0, 0);
	}
	
	void setPosition(int x, int y) {
		position = new Position(x, y);
	}
	
	int getX() {
		return position.x;
	}
	
	int getY() {
		return position.y;
	}
	
	String printPosition() {
		return "Current position of turtle is:  (" + getX() + ", " + getY() + ").";
	}
	
	String printDirection() {
		return "Current direction of turtle is:  " + direction;
	}
	
	void turnRight() {
		switch(direction) {
			case NORTH:
				direction = Directions.EAST;
				break;
			case EAST:
				direction = Directions.SOUTH;
				break;
			case SOUTH:
				direction = Directions.WEST;
				break;
			case WEST:
				direction = Directions.NORTH;
				break;
		}
	}
	
	void turnLeft() {
		switch(direction) {
			case NORTH:
				direction = Directions.WEST;
				break;
			case WEST:
				direction = Directions.SOUTH;
				break;
			case SOUTH:
				direction = Directions.EAST;
				break;
			case EAST:
				direction = Directions.NORTH;
				break;
		}
	}

}

enum Directions { 
	NORTH(0,-25) {
		@Override
		public String toString() {
			return "0";
		}
	},
	
	EAST(25,0) {
		@Override
		public String toString() {
			return "90";
		}
	},
	
	SOUTH(0,25) {
		@Override
		public String toString() {
			return "180";
		}
	},
	
	WEST(-25,0) {
		@Override
		public String toString() {
			return "270";
		}
	};
	int x;
	int y;
		
	Directions(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	int getX() {
		return x;
	}
	
	int getY() {
		return y;
	}
}

class Position {
	int x;
	int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

class Start extends JFrame {
	
	public Start() {
		
		setTitle("LogoApp");
		
		getContentPane().add(new LogoApp(1000, 700));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new FlowLayout());		
	}
		
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				Start logo = new Start();
				logo.setVisible(true);
			}
		});
	}
}