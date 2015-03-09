import java.awt.Color;
import java.util.Timer;

import javax.swing.JFrame;

public class Main extends JFrame {
	
	// Parameters. nodeswide, number of nodes widthwise, nodeshigh, number of nodes heightwise. Square is the displayed size of each node
	int nodeswide=100, nodeshigh=80, agents=800, decisionWait=1, square=10, map=1;
	
	public static void main (String[] args) {
		new Main();
	}
	
	Main() {
		setSize(nodeswide*square+50, nodeshigh*square+50);
		setLocation(0, 0);
		getContentPane().setBackground(Color.black);
		
		// Timer to update the view every 100 milliseconds
		Timer T = new Timer();		
		Loop L = new Loop(this);
		setVisible(true);
		T.schedule(L, 0, 100);
	}

}
