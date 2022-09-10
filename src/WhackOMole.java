/*
 * Name: Hassan Farhat, Vita Antonchuk, Larry Tran, Thanakrit
 * Date: 08/13/2022
 * Assignment: CSCI 143 Final Project (Whack-a-Mole)
 * Description: This is a game program that utilizes Java's Swing components (JFrame, JPanel, etc...) in order to
 * 				create a GUI program with different options for a user. A start window, game window, and end window.
 * 				The start window consists of the game title and a start button to start the game.
 * 				The game window houses the main Whack-a-Mole game in which a user has to hit the mole and try to avoid
 * 				bombs as they appear on the game field. Note that if a bomb appears, if a user waits long enough the bomb
 * 				will disappear and a new mole or bomb will appear in a different location. A user has 3 lives and the 
 * 				goal is to get as many points as you can without losing all 3 lives, if you do program redirects to end window.
 * 				The end window displays to the user that the game is over and their final score.
 * 
 * */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.JButton;

public class WhackOMole extends JFrame{
	
	private int lives = 3;
	private int score = 0;
	
	private final int WIDTH = 700; // Width of all windows
	private final int HEIGHT = 600; // Width of all windows
	
	private int[] board = new int[12];		  
	private JLabel[] holes = new JLabel[12];	// Holds images of holes to be placed on board
	
	private GameStart gameHandler = new GameStart();	// Will help to start all necessary components of the game
	
	private JFrame startFrame = new JFrame();	// Used in the initial starting screen for our game
	private JFrame endFrame = new JFrame();		// Used in end screen when game finishes
	private JFrame frame = new JFrame();		// Sets up main frame for game board
	
	private JLabel scoreLbl = new JLabel("Score: "+score);	// Creates Score label
	private JLabel livesLbl = new JLabel("Lives Left: "+lives);	// Creates Lives label

	
	// Default Constructor
	WhackOMole(){
		startScreen();
	}
	
	
	private void startScreen() {        
		
		int i = 0;
		
		MainMenu menu = new MainMenu();
		while(i < 1) {
			if(menu.isClicked() == true) {
				gameHandler.actionPerformed();
				break;
			}
		}
	}
	
	private void genRandMoleOrBomb() {
		
		Random rnd = new Random(System.currentTimeMillis());
		
        Icon mole = new ImageIcon("res/mole.PNG");	// Stores mole image
        Icon bomb = new ImageIcon("res/bomb.PNG");	// Stores bomb image
        
        // Chooses Random # between 0-12 (12 exclusive)
        // Helps give a random number to determine if a bomb will be placed
		int bombId = rnd.nextInt(12);
		
		// Chooses Random # between 0-12 (12 exclusive) 
		// Also chooses the position where mole or bomb will be on the board
		int id  = rnd.nextInt(12);	
		
		
		// if true, load image and ID of bomb on the GUI board
		if(bombId < 3) {  
			board[id] = 1;
			holes[id].setIcon(bomb);
		} 
		
		// else, load image and ID of mole on the GUI board 
		else { 
			board[id] = 2;
			holes[id].setIcon(mole);
		}
		
		// if a bomb does appear on screen, a TimerTask is set up to run after 2 seconds
		// to clear the board and generate a new sprite on the game board. 
		if(board[id] == 1) {
			Timer timer = new Timer(); 
			TimerTask task = new TimerTask() {
				public void run() {
					clearBoard();
					genRandMoleOrBomb();
				}
			};
			timer.schedule(task, 2000);	// Runs task after 2 seconds
		}
	}

	private void clearBoard() {
		
        Icon hole = new ImageIcon("res/hole.PNG");	// Stores empty hole image

		// for all items in length of array "holes", set each location to an empty hole 
        // and also set the array "board" values to 0's
		for(int i=0; i < holes.length; i++) {
			holes[i].setIcon(hole);
			board[i] = 0;
		}
	}
	
	private void lives(boolean isNotMole) {
		
		// if passed in argument is "true", then decrement total lives by 1
		if(isNotMole) {
			lives--;
			
			//Updates lives at the top of GUI
			livesLbl.setText("Lives Left: "+lives);
		}


		// Check to see if there are no lives left, if "true" display end game GUI
		if(lives == 0)
			gameOver();
		
		
		// If still remaining lives, startGame() again
		clearBoard();		
		genRandMoleOrBomb();
	}
	
	private void startGame() {
		
		// For each hole location on the board, we're adding mouse event listener to keep track of where users clicks
		for(int i=0; i < holes.length; i++) {
			holes[i].addMouseListener(new MouseAdapter() {
				
				public void mouseClicked(MouseEvent e) {
					// When user, clicks on specific location on the board, "lbl" stores the JLabel of hole that was clicked
					JLabel lbl = (JLabel)e.getSource();
					
					// Then "id", takes the String of JLabel "lbl's" name of that hole and parses it to receives it's integer value
					int id = Integer.parseInt(lbl.getName());

					// Argument "id" passed to method to determine what the user has clicked on the board
					pressedLocation(id);
				}
			});
		}
		
		clearBoard();		
		genRandMoleOrBomb();	// Generates random location of Mole or Bomb and places on board
	}

	private void pressedLocation(int id) {
		
		int value = board[id];	// Set "value" to the value of board at the hole's position the user has clicked
		
		// if "value" is equal to 1 (means user clicked on bomb), the game is over.
		if(value == 1) {
			lives = 0;
			livesLbl.setText("Lives Left: "+lives);
			gameOver();
		}
		// else if value is equal to 2 (means user clicked on mole), increment score by 10.
		else if(value == 2)
			score += 10;		
		// else, if user clicks anywhere else on the board, decrement 5 points and take away a life.
		else {
			score -= 5;
			lives(true);
		}
		
		// Updates the score at the top
		scoreLbl.setText("Score: "+score);
		
		// Game starts again if user doesn't click bomb, and has lives remaining
		clearBoard();		
		genRandMoleOrBomb();
	}

	private void createGame() {
		
        // Exits the program once the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Whack-a-Mole");	// Sets title 
        frame.setSize(new Dimension(WIDTH, HEIGHT));	// Sets Size of frame
        frame.setLocation(new Point(50, 50));
        frame.setLayout(new BorderLayout());	// Sets layout of frame w/o gaps between components
		
        
        // Top panel holds JLabel of "Score" and "Lives Left"
        JPanel northPanel = new JPanel(new GridLayout(1,2)); // Sets layout on top pane; 
        
        // Customizes Overall Score Label
        scoreLbl.setBackground(new Color(255, 204, 51));	// Sets bg Color of Label
        scoreLbl.setForeground(new Color(0, 102, 0));	// Sets color of font
        scoreLbl.setFont(new Font("Courier", Font.BOLD, 20));	//Sets the font and size 
        scoreLbl.setOpaque(true);
        
		// Customizes Overall Lives Left Label
        livesLbl.setBackground(new Color(255, 204, 51));
        livesLbl.setForeground(new Color(0, 102, 0));
        livesLbl.setFont(new Font("Courier", Font.BOLD, 20));
        livesLbl.setOpaque(true);
        
        // Adds both labels to the "northPanel" then added to main frame and sets positions to top
        northPanel.add(scoreLbl);
        northPanel.add(livesLbl);
        frame.add(northPanel, BorderLayout.NORTH);
        
        
        /*******************************************************************************************/
        
        
        // "CenterPanel" responsible for game board with all the JLabel empty holes
        JPanel centerPanel = new JPanel(new GridLayout(3, 4));  // Sets layout of center panel
        centerPanel.setBackground(new Color(0, 102, 0));
        
        
        // Sets up 263 - 321 set up each hole on the game board to have specific names 
        // to identify which ones are to be clicked by the user. These JLabels are also
        // finally added to the centerPanel
        holes[0] = new JLabel("", JLabel. CENTER);
        holes[0].setName("0");	// This identifies the specific hole to be clicked in startGame() method
        holes[0].setBounds(100, 50, 175, 200);	// Sets the coordinate location and (W x H) of each hole
        centerPanel.add(holes[0]);
        
        holes[1] = new JLabel("", JLabel. CENTER);
        holes[1].setName("1");
        holes[1].setBounds(100, 137, 175, 200);
        centerPanel.add(holes[1]);
        
        holes[2] = new JLabel("", JLabel. CENTER);
        holes[2].setName("2");
        holes[2].setBounds(100, 225, 175, 200);
        centerPanel.add(holes[2]);
        
        holes[3] = new JLabel("", JLabel. CENTER);
        holes[3].setName("3");
        holes[3].setBounds(100, 312, 175, 200);
        centerPanel.add(holes[3]);
        
        holes[4] = new JLabel("", JLabel. CENTER);
        holes[4].setName("4");
        holes[4].setBounds(300, 50, 175, 200);
        centerPanel.add(holes[4]);
        
        holes[5] = new JLabel("", JLabel. CENTER);
        holes[5].setName("5");
        holes[5].setBounds(300, 137, 175, 200);
        centerPanel.add(holes[5]);
        
        holes[6] = new JLabel("", JLabel. CENTER);
        holes[6].setName("6");
        holes[6].setBounds(300, 225, 175, 200);
        centerPanel.add(holes[6]);
        
        holes[7] = new JLabel("", JLabel. CENTER);
        holes[7].setName("7");
        holes[7].setBounds(300, 312, 175, 200);
        centerPanel.add(holes[7]);
        
        holes[8] = new JLabel("", JLabel. CENTER);
        holes[8].setName("8");
        holes[8].setBounds(500, 50, 175, 200);
        centerPanel.add(holes[8]);
        
        holes[9] = new JLabel("", JLabel. CENTER);
        holes[9].setName("9");
        holes[9].setBounds(500, 137, 175, 200);
        centerPanel.add(holes[9]);
        
        holes[10] = new JLabel("", JLabel. CENTER);
        holes[10].setName("10");
        holes[10].setBounds(500, 225, 175, 200);
        centerPanel.add(holes[10]);
        
        holes[11] = new JLabel("", JLabel. CENTER);
        holes[11].setName("11");
        holes[11].setBounds(500, 312, 175, 200);
        centerPanel.add(holes[11]);
        
        
        // Adds the centerPanel with all the holes to the main frame
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
	}
	
	private void gameOver() {
		
		int finalScore = score;	// Stores final score to be displayed
		frame.dispose();	// Closes main game board
		
		JLabel endImage = new JLabel();
		JPanel endPanel = new JPanel();
		ImageIcon endScreen = new ImageIcon("res/endScreen (4).png");
		
		endImage.setText("Game Over! You scored: "+finalScore); // text of label
		endImage.setIcon(endScreen);
		endImage.setHorizontalTextPosition(JLabel.CENTER); //set text CENTER of ImageIcon
		endImage.setVerticalTextPosition(JLabel.TOP); // set text TOP of ImageIcon
		endImage.setForeground(Color.WHITE); // set color of text
		endImage.setFont(new Font("MV Boli",Font.PLAIN, 50)); // set font of text
		endImage.setIconTextGap(-140); // set gap of text to image
		endImage.setBackground(Color.white); // sets background color
		endImage.setOpaque(true); // displays background color\
		endImage.setVerticalAlignment(JLabel.CENTER); // set vertical pos of icon+text in label
		endImage.setHorizontalAlignment(JLabel.CENTER); //set horizontal pos
		
		endPanel.add(endImage);
        endFrame.add(endPanel); 

        endFrame.setTitle("Whack-a-Mole");	// title 
        endFrame.setSize(WIDTH, HEIGHT);	// size of frame
        endFrame.setLocation(new Point(50, 50));
        endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endFrame.setVisible(true);
	}
	
	
	
/**************************************************************/
	// This class is used to start all the game components when 
	// a user presses "start" button on the Starting Screen.
	public class GameStart{
		
		public void actionPerformed() {
			startFrame.dispose();
			createGame();
			clearBoard();
			startGame();
		}
	}
/**************************************************************/
	
	public static void main(String[] args) {
		new WhackOMole();
	}
}

