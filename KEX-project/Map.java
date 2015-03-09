import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
	
	boolean done=true;
	protected List<Integer> targets= new ArrayList<Integer>();
	protected List<Integer> targetCapacity= new ArrayList<Integer>();
	protected List<Integer> waypoints= new ArrayList<Integer>();
	List<Float> areWaitingForExit= new ArrayList<Float>();
	List<Integer> agentsOut= new ArrayList<Integer>();
	Node[][] nodes=null;
	Agent[] agents=null;	
	int nodeswide=0, nodeshigh=0, noAgents=0, square=0,	turn=0,map=0,decisionWait=0,agentTurnsInField=0;
	
	
	Map(Main M) {
		noAgents=M.agents;
		nodeswide=M.nodeswide;
		nodeshigh=M.nodeshigh;
		map=M.map;
		decisionWait=M.decisionWait;
		square=M.square;
		
		areWaitingForExit.add((float) 0);
		areWaitingForExit.add((float) 0);
		areWaitingForExit.add((float) 0);
		agentsOut.add(0);
		agentsOut.add(0);
		agentsOut.add(0);
		
		// Initialize node matrix
		nodes = new Node[nodeshigh][nodeswide];
		for(int i=0;i<nodeshigh;i++) {
			for(int j=0;j<nodeswide;j++) {
				nodes[i][j] = new Node(i,j,this);
			}
		}		
		
		if (map==1) {
			// Add targets
			targets.add(nodeshigh/3); // Target x
			targets.add(0); // Target y		
			targets.add(2*nodeshigh/3);
			targets.add(nodeswide-1);		
			targets.add(0);
			targets.add(nodeswide/3);	
			waypoints.add(30);
			waypoints.add(50);			

			// Draw walls in map
			for(int i=0;i<nodeshigh;i++) {
				// Vertical
				nodes[i][0].isWall=true;
				nodes[i][nodeswide-1].isWall=true;
					
				if (i > 20 && i < 60) {
					nodes[i][60].isWall=true;
				}		
					
			}
				
			for(int i=0;i<nodeswide;i++) {
				// Horizontal
				nodes[0][i].isWall=true;
				nodes[nodeshigh-1][i].isWall=true;
					
				if (i > 40 && i < 61) {
					nodes[20][i].isWall=true;
				}		
				if (i>59 && i<nodeswide) {
					nodes[60][i].isWall=true;
				}
					
				if (i>0 && i <40) {
					nodes[40][i].isWall=true;
				}
			}
			
			//Put walls around targets
			nodes[targets.get(0)+1][targets.get(1)+1].isWall=true; 
			nodes[targets.get(0)-1][targets.get(1)+1].isWall=true;
			nodes[targets.get(2)+1][targets.get(3)-1].isWall=true; 
//			nodes[targets.get(2)-1][targets.get(3)-1].isWall=true;
			nodes[targets.get(4)+1][targets.get(5)+1].isWall=true; 
			nodes[targets.get(4)+1][targets.get(5)-1].isWall=true; 	
			targetCapacity.add(1);
			targetCapacity.add(2);
			targetCapacity.add(1);	
				
		}
		else if (map==2) {				
			// Add targets
			targets.add(10); // Target x
			targets.add(0); // Target y		
			targets.add(10);
			targets.add(nodeswide-1);		
			targets.add(nodeshigh-1);
			targets.add(35);	
			waypoints.add(50);
			waypoints.add(62);	
			
			// Draw walls in map
			for(int i=0;i<nodeshigh;i++) {
				// Vertical
				nodes[i][0].isWall=true;
				nodes[i][nodeswide-1].isWall=true;
				
				nodes[i][40].isWall=true;
				
				if (i > 50 && i < 53) {
					nodes[i][40].isWall=false;
				}
				
				if (i < 40) {
					nodes[i][80].isWall=true;
				}		
				
				if (i > 60) {
					nodes[i][80].isWall=true;
					nodes[i][60].isWall=true;
					nodes[i][20].isWall=true;
					
					if (i>70 && i<73) {
						nodes[i][80].isWall=false;
						nodes[i][40].isWall=false;
						nodes[i][20].isWall=false;
					}
				}	
				
			}
			
			for(int i=0;i<nodeswide;i++) {
				// Horizontal
				nodes[0][i].isWall=true;
				nodes[nodeshigh-1][i].isWall=true;
				
				nodes[60][i].isWall=true;
				
				if (i < 81) {
					nodes[40][i].isWall=true;
					if (i>60 && i < 63) {
						nodes[40][i].isWall=false;
					}
				}
				
				if (i>15 && i < 18) {
					nodes[60][i].isWall=false;
					nodes[40][i].isWall=false;
				}
				
				if (i>50 && i < 53) {
					nodes[60][i].isWall=false;
				}
				
				if (i>60 && i < 63) {
					nodes[60][i].isWall=false;
				}
				
			}
			
			//Put walls around targets
			nodes[targets.get(0)+1][targets.get(1)+1].isWall=true; 
			nodes[targets.get(0)-1][targets.get(1)+1].isWall=true;
			nodes[targets.get(2)+1][targets.get(3)-1].isWall=true; 
			nodes[targets.get(2)-1][targets.get(3)-1].isWall=true;
			nodes[targets.get(4)-1][targets.get(5)+1].isWall=true; 
			nodes[targets.get(4)-1][targets.get(5)-1].isWall=true; 	
			targetCapacity.add(1);
			targetCapacity.add(1);
			targetCapacity.add(1);	
			
		}// End if specific map properties
		
		// Draw targets
		nodes[targets.get(0)][targets.get(1)].isWall=false; 
		nodes[targets.get(2)][targets.get(3)].isWall=false;
		nodes[targets.get(4)][targets.get(5)].isWall=false; 
		nodes[targets.get(0)][targets.get(1)].isTarget=true; 
		nodes[targets.get(2)][targets.get(3)].isTarget=true;
		nodes[targets.get(4)][targets.get(5)].isTarget=true;
		
		
		// Initialize agent list		
		int xs=0, ys=0;
		Random r = new Random();		
		agents = new Agent[M.agents];
		
		for(int i=0;i<M.agents;i++) {
			while (true) {
				xs=r.nextInt(nodeshigh);
				ys=r.nextInt(nodeswide);
				if (nodes[xs][ys].isWall==false && nodes[xs][ys].isOccupied==false && nodes[xs][ys].isTarget==false) {					
					break;
				}			
			}	
			agents[i] = new Agent(xs,ys);
			nodes[xs][ys].isOccupied=true;
			agents[i].agentID=i;
			if (decisionWait > 1 ) { agents[i].movesSinceLastDecision=r.nextInt(decisionWait); } // Randomize decision wait
			nodes[xs][ys].occupyingAgentID=i;
			
			// Head for waypoint
			if (map==1 && (xs > 60 || (xs>40 && ys < 60)) ) { agents[i].isHeadingForWaypoint=true;	} // Go to waypoint
			else if (map==2 && ( (ys > 40 && ys < 80 && xs < 40 ) || (xs > 60 && ys>60) ) )  { agents[i].isHeadingForWaypoint=true;	} 
		}	
		
		// Mark nodes as visible
		markExitVisibility();
		
	}
	
	void markExitVisibility() {
		int x=-1, y=-1, xt=-1, yt=-1, xvr=-1, yvr=-1, steps=-1,side=0;
		double xv=-1, yv=-1, angle=-1;
		
		for (int i=0; i<targets.size()/2; i++) {		
			x=targets.get(2*i);
			y=targets.get(2*i+1);
			
			if (x==0) {x++;}
			if (x==nodeshigh-1) {x--;}
			if (y==0) {y++;}
			if (y==nodeswide-1) {y--;}
			xt=0;
			yt=0;
			side=0;
			
			while (true) {
				xv=(double) x;
				yv=(double) y;
				
				if (side==0) {
					// Calculate angle
					if (xt<x) {
						angle = Math.PI + Math.atan((double) (y-yt) / (double) (x-xt)); 
					}
					else if (xt==x) {
						angle = 3*Math.PI/2;
					}
					else {
						angle =  3*Math.PI/2 + Math.atan((double) (xt-x) / (double) (y-yt));
					}
					
					xt++;
					if (xt > nodeshigh) {
						xt--;
						side=1;
					}
					
				}
				else if (side==1) {
				// 	Calculate angle
					if (yt<y) {
						angle = 3*Math.PI/2 + Math.atan((double) (xt-x) / (double) (y-yt)); 
					}
					else if (yt==y) {
						angle = 0;
					}
					
					else {
						angle =  Math.atan((double) (yt-y) / (double) (xt-x));
					}
					
					yt++;
					if (yt > nodeswide) {
						yt--;
						side=2;
					}
					
				}			
				if (side==2) {
				// 	Calculate angle
					if (xt>x) {
						angle = Math.atan((double) (yt-y) / (double) (xt-x)); 
					}
					else if (xt==x) {
						angle = Math.PI/2;
					}
					else {
						angle =  Math.PI/2 + Math.atan((double) (x-xt) / (double) (yt-y));
					}
					
					xt--;
					if (xt < 0) {
						xt++;
						side=3;
					}
					
				}			
				if (side==3) {
				// 	Calculate angle
					if (yt>y) {
						angle = Math.PI/2 + Math.atan((double) (x-xt) / (double) (yt-y)); 
					}
					else if (yt==y) {
						angle = Math.PI;
					}
					else {
						angle =  Math.PI + Math.atan((double) (y-yt) / (double) (x-xt));
					}
					
					yt--;
					if (yt < 0) {
						yt++;
						break;
					}				
				}			
				
				steps=0;
				while (true) {
					steps++;
					xv+=Math.cos(angle);
					yv+=Math.sin(angle);
					xvr=(int) Math.round(xv);
					yvr=(int) Math.round(yv);				
					
					if (xvr < 0 || yvr < 0 || xvr > nodeshigh-1 || yvr > nodeswide-1 || (steps > 1 && (nodes[xvr][yvr].isWall || nodes[xvr][yvr].isTarget))) {				
						break;
					}				
					
					if (nodes[xvr][yvr].isVisibleFromExit.contains(i)) { continue; }
					else {nodes[xvr][yvr].isVisibleFromExit.add(i); }
				}
			}
		}
	}
	
				
	void mapupdate() {
		done=true;
		turn++;
		System.out.println(turn);
		for(int i=0;i<noAgents;i++) {
			agents[i].posupdate(this);			
			// Update agents, that will in turn update map
			
			if (!agents[i].hasArrived) {
				done=false;
			}
		}
		if (done) {
			System.out.println("Done");
			System.out.println("Green exit out: " + agentsOut.get(0) + " Blue exit out: " + agentsOut.get(1) + " Red exit out: " + agentsOut.get(2));
			System.out.println("Green exit efficiency: " + 100 * (float) agentsOut.get(0)/ (float) (targetCapacity.get(0)*turn) + " Blue exit efficiency: " + 100 * (float) agentsOut.get(1)/ (float) (targetCapacity.get(1)*turn) + " Red exit efficiency: " + 100 * (float) agentsOut.get(2)/ (float) (targetCapacity.get(2)*turn));
			System.out.println("Total effeciency: " + 100 * (float) noAgents / (float) (turn*(targetCapacity.get(0)+targetCapacity.get(1)+targetCapacity.get(2))));
			System.out.println("Total field time: " + agentTurnsInField);
		}
	}	
}
