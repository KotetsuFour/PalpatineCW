package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import io.PCWIO;
import manager.PCWManager;

public class PCWGUI extends JFrame {

	/**Obligatory serial version*/
	private static final long serialVersionUID = 1L;

	private JPanel p;

	private CardLayout cl;

	Random rng;

	public static final ImageIcon PCW_ICON = new ImageIcon("images/pcw_icon.jpg");

	public static PCWManager manager;

	public PCWGUI() {

		p = new JPanel();
		cl = new CardLayout();
		p.setLayout(cl);
		
		rng = new Random();
		
		//Window formatting
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(1300, 700);
	    setTitle("Palpatine POV Clone Wars");
	    setIconImage(PCW_ICON.getImage());
	    
	    p.add(new IntroPanel(), "Intro");	    
	    cl.show(p, "Intro");
	    getContentPane().add(p, BorderLayout.CENTER);

	      
	    setVisible(true);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		PCWGUI hg = new PCWGUI();
	}
	
	private class IntroPanel extends JPanel {
		public IntroPanel() {
			
			setLayout(new BorderLayout());
			JPanel gameOptions = new JPanel();
			gameOptions.setLayout(new GridLayout(1, 4));
			
			for (int q = 1; q <= 3; q++) {
				JPanel gameFile = new JPanel();
				gameFile.setBackground(Color.BLACK);
				JButton loadFile = new JButton("Game " + q);
				loadFile.setBackground(Color.BLUE);
				loadFile.setForeground(Color.BLACK);
				loadFile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO ask if you want new (or load if file exists)
						//TODO add loading screen
//						if () {
//							manager = PCWIO.readFile(0);
//						} else {
//							manager = PCWIO.readFile(q);
//						}
						// TODO switch to actual screen
					}
				});
				gameFile.add(loadFile);
				gameOptions.add(gameFile);
			}
			JPanel extras = new JPanel();
			extras.setBackground(Color.BLACK);
			JButton selectExtras = new JButton("Extras");
			selectExtras.setBackground(Color.RED);
			selectExtras.setForeground(Color.BLACK);
			selectExtras.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});
			extras.add(selectExtras);
			gameOptions.add(extras);
			add(gameOptions, BorderLayout.SOUTH);
		}
	}

}