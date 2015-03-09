import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Agent {
	protected int x=-1, y=-1,agentID=-1;
	int headingFor=-1,moves=0,movesSinceLastDecision=0;
	List<Integer> pathLengths= new ArrayList<Integer>();
	List<Float> agentsAhead= new ArrayList<Float>();
	boolean hasArrived=false,isMarked=false,isWaitingForExit=false,isHeadingForWaypoint=false;
	
	Agent(int x, int y) {
		this.x=x;
		this.y=y;		
	}
	
	void sideWind(Map Ma, int dx, int dy) {
		// Writes to: x and y
		
		int ncprim1[] = {-1,-1}, ncprim2[] = {-1,-1}, ncsec1[] = {-1,-1}, ncsec2[] = {-1,-1}; // New primary and secondary coordinates.
		// Check available sidewinding paths
		
		if (dy==0){
			// Straight line
			if (Ma.nodes[x+dx][y+1].isOccupied==false && Ma.nodes[x+dx][y+1].isWall==false) {
				ncprim1[0]=x+dx;
				ncprim1[1]=y+1;
			}
			if (Ma.nodes[x+dx][y-1].isOccupied==false && Ma.nodes[x+dx][y-1].isWall==false) {
				ncprim2[0]=x+dx;
				ncprim2[1]=y-1;
			}
			if (Ma.nodes[x][y+1].isOccupied==false && Ma.nodes[x][y+1].isWall==false) {
				ncsec1[0]=x;
				ncsec1[1]=y+1;
			}
			if (Ma.nodes[x][y-1].isOccupied==false && Ma.nodes[x][y-1].isWall==false) {
				ncsec2[0]=x;
				ncsec2[1]=y-1;
			}
		}
		
		else if (dx==0) {
			// Straight line
			if (Ma.nodes[x+1][y+dy].isOccupied==false && Ma.nodes[x+1][y+dy].isWall==false) {
				ncprim1[0]=x+1;
				ncprim1[1]=y+dy;
			}
			if (Ma.nodes[x-1][y+dy].isOccupied==false && Ma.nodes[x-1][y+dy].isWall==false) {
				ncprim2[0]=x-1;
				ncprim2[1]=y+dy;
			}
			if (Ma.nodes[x+1][y].isOccupied==false && Ma.nodes[x+1][y].isWall==false) {
				ncsec1[0]=x+1;
				ncsec1[1]=y;
			}
			if (Ma.nodes[x-1][y].isOccupied==false && Ma.nodes[x-1][y].isWall==false) {
				ncsec2[0]=x-1;
				ncsec2[1]=y;
			}		
		}
		
		else if (dx > 0 && dy > 0) {
			// Diagonal 
			if (Ma.nodes[x+dx][y].isOccupied==false && Ma.nodes[x+dx][y].isWall==false) {
				ncprim1[0]=x+dx;
				ncprim1[1]=y;
			}
			if (Ma.nodes[x][y+dy].isOccupied==false && Ma.nodes[x][y+dy].isWall==false) {
				ncprim2[0]=x;
				ncprim2[1]=y+dy;
			}
			if (Ma.nodes[x+dx][y-1].isOccupied==false && Ma.nodes[x+dx][y-1].isWall==false) {
				ncsec1[0]=x+dx;
				ncsec1[1]=y-1;
			}
			if (Ma.nodes[x-1][y+dy].isOccupied==false && Ma.nodes[x-1][y+dy].isWall==false) {
				ncsec2[0]=x-1;
				ncsec2[1]=y+dy;
			}		
		}
		
		else if (dx < 0 && dy > 0) {
			if (Ma.nodes[x+dx][y].isOccupied==false && Ma.nodes[x+dx][y].isWall==false) {
				ncprim1[0]=x+dx;
				ncprim1[1]=y;
			}
			if (Ma.nodes[x][y+dy].isOccupied==false && Ma.nodes[x][y+dy].isWall==false) {
				ncprim2[0]=x;
				ncprim2[1]=y+dy;
			}
			if (Ma.nodes[x+dx][y-1].isOccupied==false && Ma.nodes[x+dx][y-1].isWall==false) {
				ncsec1[0]=x+dx;
				ncsec1[1]=y-1;
			}
			if (Ma.nodes[x+1][y+dy].isOccupied==false && Ma.nodes[x+1][y+dy].isWall==false) {
				ncsec2[0]=x+1;
				ncsec2[1]=y+dy;
			}	
		}
		else if (dx < 0 && dy < 0) {
			// Diagonal 
			if (Ma.nodes[x+dx][y].isOccupied==false && Ma.nodes[x+dx][y].isWall==false) {
				ncprim1[0]=x+dx;
				ncprim1[1]=y;
			}
			if (Ma.nodes[x][y+dy].isOccupied==false && Ma.nodes[x][y+dy].isWall==false) {
				ncprim2[0]=x;
				ncprim2[1]=y+dy;
			}
			if (Ma.nodes[x+dx][y+1].isOccupied==false && Ma.nodes[x+dx][y+1].isWall==false) {
				ncsec1[0]=x+dx;
				ncsec1[1]=y+1;
			}
			if (Ma.nodes[x+1][y+dy].isOccupied==false && Ma.nodes[x+1][y+dy].isWall==false) {
				ncsec2[0]=x+1;
				ncsec2[1]=y+dy;
			}
		}
		else if (dx > 0 && dy < 0) {
			// Diagonal 
			if (Ma.nodes[x+dx][y].isOccupied==false && Ma.nodes[x+dx][y].isWall==false) {
				ncprim1[0]=x+dx;
				ncprim1[1]=y;
			}
			if (Ma.nodes[x][y+dy].isOccupied==false && Ma.nodes[x][y+dy].isWall==false) {
				ncprim2[0]=x;
				ncprim2[1]=y+dy;
			}
			if (Ma.nodes[x+dx][y+1].isOccupied==false && Ma.nodes[x+dx][y+1].isWall==false) {
				ncsec1[0]=x+dx;
				ncsec1[1]=y+1;
			}
			if (Ma.nodes[x-1][y+dy].isOccupied==false && Ma.nodes[x-1][y+dy].isWall==false) {
				ncsec2[0]=x-1;
				ncsec2[1]=y+dy;
			}	
		}		
				
		if (ncprim1[0]==-1 && ncprim2[0]==-1 && ncsec1[0]==-1 && ncsec2[0]==-1) {
			// No sidewinding possible, wait				
			return;
		}		
		// End of sidewinding check					
			
		// This randomizes sidewinding choice if there are several
		if (ncprim1[0]!=-1 && ncprim2[0]!=-1) {
			// Both primary open
			Random rand = new Random();
			if (rand.nextBoolean()) {
				x=ncprim1[0];
				y=ncprim1[1];
			}
			else {
				x=ncprim2[0];
				y=ncprim2[1];
			}
		}
		else if (ncprim1[0]!=-1) {
			x=ncprim1[0];
			y=ncprim1[1];
		}
		else if (ncprim2[0]!=-1) {
			x=ncprim2[0];
			y=ncprim2[1];
		}
		else if (ncsec1[0]!=-1 && ncsec2[0]!=-1) {
			// Both secondary open
			Random rand = new Random();
			if (rand.nextBoolean()) {
				x=ncsec1[0];
				y=ncsec1[1];
			}
			else {
				x=ncsec2[0];
				y=ncsec2[1];
			}
		}
		else if (ncsec1[0]!=-1) {
			x=ncsec1[0];
			y=ncsec1[1];
		}
		else if (ncsec2[0]!=-1) {
			x=ncsec2[0];
			y=ncsec2[1];
		}		
	}
	
	void lookAroundAndCommunicate(Map Ma) {
		// Writes to: agentsAhead
		List<Integer> visibleAgentsAhead = new ArrayList<Integer>();
		int xt=0, yt=0, xvr=0, yvr=0,side=0,nextAgentHeadingFor=-1,nextAgentID=-1,comAgentID=-1;
		float nextAgentAgentsAhead=-1,maxAgentsAhead=-1;
		String hashkey="";
		double xv=(double) x,yv=(double) y,angle=0;
		HashSet<String> hasChecked = new HashSet<String>(); // Visibility indicator	
		
		// Init visibleAgentsAhead
		for (int i=0; i< Ma.targets.size()/2 ; i++) {visibleAgentsAhead.add(i,0);}
		
		while (true) {
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
				if (xt > Ma.nodeshigh) {
					xt--;
					side=1;
				}
				
			}
			else if (side==1) {
				// Calculate angle
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
				if (yt > Ma.nodeswide) {
					yt--;
					side=2;
				}
				
			}			
			if (side==2) {
				// Calculate angle
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
				// Calculate angle
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
			
			while (true) {
				xv+=Math.cos(angle);
				yv+=Math.sin(angle);
				xvr=(int) Math.round(xv);
				yvr=(int) Math.round(yv);				
				
				if (Ma.nodes[xvr][yvr].isWall || Ma.nodes[xvr][yvr].isTarget) {
					xv=(double) x;
					yv=(double) y;
					break;
				}				
				
				hashkey = "x" + Integer.toString(xvr) + "y" + Integer.toString(yvr);
				
				if (hasChecked.contains(hashkey)) { continue; }
				else { hasChecked.add(hashkey); }
							
				if (Ma.nodes[xvr][yvr].isOccupied && Ma.agents[Ma.nodes[xvr][yvr].occupyingAgentID].headingFor > -1) {
					nextAgentID = Ma.nodes[xvr][yvr].occupyingAgentID;
					nextAgentHeadingFor = Ma.agents[nextAgentID].headingFor;
					nextAgentAgentsAhead = Ma.agents[nextAgentID].agentsAhead.get(headingFor);
					
					
					if (Ma.agents[nextAgentID].pathLengths.get(nextAgentHeadingFor) < pathLengths.get(nextAgentHeadingFor)) {
						visibleAgentsAhead.set(nextAgentHeadingFor, visibleAgentsAhead.get(nextAgentHeadingFor) +1 );
						// Add to agentsAhead if next agent has shorter path to exit. If agent has status !makeDecision, its pathlength for exits other
						// than its own will be -1. Therefore agents other than it own heading will not be added to agents ahead, since its own path is always shorter
					}
					
					if ( Ma.agents[nextAgentID].pathLengths.get(headingFor)>-1 && Ma.agents[nextAgentID].pathLengths.get(headingFor) < pathLengths.get(headingFor) && (maxAgentsAhead==-1 || nextAgentAgentsAhead > maxAgentsAhead)) {
						// Find an agent closer to headingFor than the preset agent. Ask if he has more agents ahead
						// This means, he is heading away from the target the present agent is heading to, which means that exit is too jammed. This info 
						// must be belayed to the present agent. The closest agent also needs to be updated recently					
						
						maxAgentsAhead = nextAgentAgentsAhead;		
						comAgentID=nextAgentID;		
					}	
				}
			}
		}		

//		if ( comAgentID>-1 && Math.pow( (double) (Ma.agents[comAgentID].x-x), 2) + Math.pow( (double) (Ma.agents[comAgentID].y-y), 2) > 25) {comAgentID=-1; } // Communication range
//		comAgentID=-1; // Use to turn off communication
		
		for (int i=0; i< Ma.targets.size()/2 ; i++) {
			// Set agents ahead to whatever is in the memory of what the agent can see
			agentsAhead.set(i, Math.max(visibleAgentsAhead.get(i), agentsAhead.get(i))); 
		}						
		
		if (comAgentID > -1) {
			// If closestAgentID > -1, then there is an agent ahead that is not going towards the exit, and his information of the situation ahead is shared. 
			agentsAhead.set(headingFor, Math.max(agentsAhead.get(headingFor), maxAgentsAhead));
		}
		
		
//		for (int i=0; i< Ma.targets.size()/2 ; i++) {
//			// If exit is visible, memory = agents seen. 
//			if (Ma.nodes[x][y].isVisibleFromExit.contains(i)) { memoryOfAgentsAhead.set(i, agentsAhead.get(i)); }
//		}
		
	}
	
	void posupdate(Map Ma) {				
		if (hasArrived) { return; }	
		
		boolean makeDecision=false;
		int xprev=x,yprev=y,dx=0,dy=0;
		aStar As = new aStar();		
		
		Ma.nodes[x][y].isOccupied=false;
		Ma.nodes[x][y].occupyingAgentID=-1;	
		
		movesSinceLastDecision++;
		moves++;	
		
		if (moves==1) {makeDecision=true;}
		if (movesSinceLastDecision==Ma.decisionWait) { 
			movesSinceLastDecision=0;
			makeDecision=true;
		}	
		
		if (isHeadingForWaypoint==true && ((Ma.map == 1 && x < 40) || (Ma.map==2 && x > 39 && x < 61)) ) { 
			// Arrived to waypoint 
			isHeadingForWaypoint = false; 	
			makeDecision=true;
		} 
		
		pathLengths.clear();
		for (int i=0; i<Ma.targets.size()/2;i++) {		
			if (moves==1) {	agentsAhead.add(i,(float) 0); }
			if (agentsAhead.get(i) > 0 ) { agentsAhead.set(i, (float) (agentsAhead.get(i) - Ma.targetCapacity.get(i) * 1.25)); }
			// Decrease memory by one per turn, to compensate for agents exiting
		}			
		
		if (isHeadingForWaypoint) {
			As.StarCalc(Ma,x,y,-2); 
			// Head to waypoint		
			
			dx = As.pathLists.get(0).get(0) - x; 
			dy = As.pathLists.get(0).get(1) - y;				
		}
		else if (isWaitingForExit) {				
			As.StarCalc(Ma,x,y,headingFor); 
			agentsAhead.set(headingFor, Ma.areWaitingForExit.get(headingFor));	
			
			for (int i=0; i<Ma.targets.size()/2;i++) {		
				pathLengths.add(i,-1);
				if (i==headingFor) { pathLengths.set(i,As.pathLists.get(0).size()/2);
				}
			}		
			
			dx = As.pathLists.get(0).get(0) - x; 
			dy = As.pathLists.get(0).get(1) - y;					
		}		
		else if (!makeDecision) {	
			As.StarCalc(Ma,x,y,headingFor);  // One exit	
			
			for (int i=0; i<Ma.targets.size()/2;i++) {		
				pathLengths.add(i,-1);
				if (i==headingFor) { pathLengths.set(i,As.pathLists.get(0).size()/2);
				}
			}		
			
			lookAroundAndCommunicate(Ma);				
			
			dx = As.pathLists.get(0).get(0) - x; 
			dy = As.pathLists.get(0).get(1) - y;				
		}		
		else if (makeDecision) {
			int shortestLength=-1,bestExit=-1;
			
			As.StarCalc(Ma,x,y,-1);  // All exits		
			
			for (int i=0; i<Ma.targets.size()/2;i++) {		
				pathLengths.add(As.pathLists.get(i).size()/2);
				if (shortestLength==-1 || (As.pathLists.get(i).size()/2 < shortestLength)) {
					shortestLength=As.pathLists.get(i).size()/2;
					bestExit=i;
				}
			}	
			
			if (headingFor==-1) {headingFor = bestExit; }			
		
			lookAroundAndCommunicate(Ma);
			
			float shortestPath=0,agentsCompAhead=0;
		
			for (int i=0; i<pathLengths.size();i++) {
				agentsCompAhead = agentsAhead.get(i) / Ma.targetCapacity.get(i);
			
				if (i==0 || Math.max(pathLengths.get(i),agentsCompAhead)  < shortestPath) {
					shortestPath=  Math.max(pathLengths.get(i),agentsCompAhead);
					bestExit=i;
				}			
			}			
		
			int oldHeadingFor = headingFor;
			headingFor = bestExit;
		
			if (oldHeadingFor > -1 && oldHeadingFor != headingFor) {
				// If the agent changes heading for, it will perform a new check to keep it from changing back and forth
				lookAroundAndCommunicate(Ma);			
				shortestPath=0;
				agentsCompAhead=0;
				bestExit=0;
				
				for (int i=0; i<pathLengths.size();i++) {
					agentsCompAhead = agentsAhead.get(i) / Ma.targetCapacity.get(i);
					
					if (i==0 || Math.max(pathLengths.get(i),agentsCompAhead)  < shortestPath) {
						shortestPath=  Math.max(pathLengths.get(i),agentsCompAhead);
						bestExit=i;
					}			
				}	
				headingFor = bestExit;
			}
			
			dx = As.pathLists.get(headingFor).get(0) - x;
			dy = As.pathLists.get(headingFor).get(1) -y; 	
		} // cases
		
		if (Ma.nodes[x+dx][y+dy].isOccupied) {			
			
			if (isWaitingForExit==false && Ma.agents[Ma.nodes[x+dx][y+dy].occupyingAgentID].isWaitingForExit) {	
				headingFor = Ma.agents[Ma.nodes[x+dx][y+dy].occupyingAgentID].headingFor;
				isWaitingForExit=true; 
				Ma.areWaitingForExit.set(headingFor, Ma.areWaitingForExit.get(headingFor)+1);
				}						
			sideWind(Ma,dx,dy);			
		}
		else {		
			x=x+dx;
			y=y+dy;	
		}	
		
		if (!isHeadingForWaypoint && !isWaitingForExit && pathLengths.get(headingFor) < 7) { 
			// Wait to exit if path length is short enough
			isWaitingForExit=true; 
			Ma.areWaitingForExit.set(headingFor, Ma.areWaitingForExit.get(headingFor)+1);
		}			
		
		if (!isHeadingForWaypoint && x==Ma.targets.get(2*headingFor) && y==Ma.targets.get(2*headingFor+1)) {
			hasArrived=true;
			Ma.agentsOut.set(headingFor, Ma.agentsOut.get(headingFor)+1);
			Ma.areWaitingForExit.set(headingFor, Ma.areWaitingForExit.get(headingFor)-1);
			return;
		}
		
		if (Math.abs(xprev-x)>1 || Math.abs(yprev-y)>1 || Ma.nodes[x][y].isOccupied || Ma.nodes[x][y].isWall) {
			System.out.println("Illegal agent move");
		}
		
		if (!isWaitingForExit) {Ma.agentTurnsInField++; } // If not waiting for exit or not escaped, 
		
		Ma.nodes[x][y].isOccupied=true;
		Ma.nodes[x][y].occupyingAgentID=agentID;			
		
	}
}