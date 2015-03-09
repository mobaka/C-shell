import java.util.TimerTask;

// This is the Timer class
public class Loop extends TimerTask {
	
	Main M=null;
	View V=null;
	Map Ma=null;
	
	Loop(Main M) {
		// When Loop is called by main, the constructor should create the view, the map matrix, and all the nodes
		this.M=M;
		this.Ma = new Map(M);
		// Initialize map
		this.V = new View(Ma);
		// Add View to the frame
		M.add(V);		
	}
	
	
	@Override
	public void run() {
		// The .run method override should update the nodes, and the map
		
		Ma.mapupdate();
		//this.Ma=Ma
		V.viewupdate(Ma);

		
		M.repaint();
		
		if (Ma.done) {
			this.cancel();
		}
		
//		for (int i=0;i<Ma.targets.size()/2;i++) {
//			System.out.println(Ma.targetWait.get(i));
//		}
	}
}
