import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class MainMenu extends JFrame implements MouseListener {
	
	public JFrame startFrame = new JFrame();	
	JLabel playButton = new JLabel("Play");
	
	boolean clicked = false;
	
	//constructor
	public MainMenu(){
		JLabel startTitle = new JLabel("WHACK-A-MOLE");
		JPanel startPanel = new JPanel();	
		Border solidLine = BorderFactory.createLineBorder(Color.BLACK, 3);

		startPanel.setLayout(null);	
        startPanel.setBackground(new Color (0, 102, 0));
        
        
        //creating title on top
        startTitle.setHorizontalAlignment(SwingConstants.CENTER); 
        startTitle.setVerticalAlignment(SwingConstants.TOP);	
        startTitle.setFont(new Font("Courier", Font.BOLD, 40));
        startTitle.setForeground(Color.BLACK);
        startTitle.setBounds(150, 10, 400,70);	
        startTitle.setOpaque(true);
        startTitle.setBackground(Color.WHITE);
        startTitle.setBorder(solidLine);
        startPanel.add(startTitle);	
        
        //creating play button
        playButton.setHorizontalAlignment(SwingConstants.CENTER);
        playButton.setVerticalAlignment(SwingConstants.TOP);
        playButton.setFont(new Font("Courier", Font.BOLD, 40));
        playButton.setForeground(Color.YELLOW);
        playButton.setBackground(new Color(0, 103, 0));
        playButton.setOpaque(true);
        playButton.addMouseListener(this);
        playButton.setBounds(250, 240, 200,60);
        playButton.setBorder(solidLine);	
        startPanel.add(playButton);

        //create frame
        startFrame.setTitle("Whack-a-Mole");
        startFrame.setSize(700, 600);
        startFrame.setLocation(new Point(50, 50));
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.add(startPanel);
        startFrame.setVisible(true);
        
        
	}
	
	//is clicked method, returns boolean
	public boolean isClicked() {
		System.out.print("");
		return clicked;
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		startFrame.dispose();
		clicked = true;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		playButton.setBackground(Color.WHITE);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		playButton.setBackground(new Color(0, 103, 0));
		
	}


}
