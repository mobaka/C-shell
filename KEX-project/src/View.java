import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class View extends JPanel {
	
	Map Ma = null;
	
	View(Map Ma) {
		this.Ma=Ma;
		//setBackground(Color.GRAY);
	}	
	
	void viewupdate(Map Ma) {
		this.Ma=Ma;
		this.repaint();
	}
	
	@Override
    public void paint(Graphics g) {
		// Painting code
		super.paint(g);        
		
		// Paint map, this should only be done once, but isn't
		for(int x=0;x<Ma.nodeshigh;x++) {			
			// Row
			for(int y=0;y<Ma.nodeswide;y++) {
				// Column
				if (Ma.nodes[x][y].isWall) {
					// Discovered wall
					g.setColor(Color.black);
					
					for (int k=0;k<Ma.square;k++) {
						for (int l=0;l<Ma.square;l++) {							
							g.drawLine(Ma.square*y+k,Ma.square*x+l,Ma.square*y+k,Ma.square*x+l);														
						}
					}			
				}				
				if (Ma.nodes[x][y].isMarked) {
					g.drawLine(Ma.square*y+2,Ma.square*x+2,Ma.square*y+2,Ma.square*x+2);
				}				
				if (Ma.nodes[x][y].isVisibleFromExit.contains(0)) {
					g.setColor(Color.green);
					g.drawLine(Ma.square*y+3,Ma.square*x+3,Ma.square*y+3,Ma.square*x+3);
				}
				if (Ma.nodes[x][y].isVisibleFromExit.contains(1)) {
					g.setColor(Color.blue);
					g.drawLine(Ma.square*y+4,Ma.square*x+4,Ma.square*y+4,Ma.square*x+4);
				}
				if (Ma.nodes[x][y].isVisibleFromExit.contains(2)) {
					g.setColor(Color.red);
					g.drawLine(Ma.square*y+5,Ma.square*x+5,Ma.square*y+5,Ma.square*x+5);
				}				
			}			
		}	
		
		// Paint agents
		for (int i=0; i<Ma.agents.length;i++) {
			if (Ma.agents[i].headingFor==0) {
				 g.setColor(Color.green);
			}
			else if (Ma.agents[i].headingFor==1) {
				 g.setColor(Color.blue);
			}
			else if (Ma.agents[i].headingFor==2) {
				 g.setColor(Color.red);
			}
			
			if (Ma.agents[i].isHeadingForWaypoint) {
				 g.setColor(Color.gray);
			}
			
			for (int k=0;k<Ma.square;k++) {
				for (int l=0;l<Ma.square;l++) {							
					g.drawLine(Ma.square*Ma.agents[i].y+k,Ma.square*Ma.agents[i].x+l,Ma.square*Ma.agents[i].y+k,Ma.square*Ma.agents[i].x+l);														
				}
			}
			
			if (Ma.agents[i].isMarked || Ma.agents[i].isWaitingForExit) {
				g.setColor(Color.black);
				g.drawLine(Ma.square*Ma.agents[i].y+4,Ma.square*Ma.agents[i].x+4,Ma.square*Ma.agents[i].y+4,Ma.square*Ma.agents[i].x+4);
			}
		}
		
	}
    

}
