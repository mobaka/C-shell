import java.util.ArrayList;
import java.util.List;

// See: http://www.policyalmanac.org/games/aStarTutorial.htm
public class aStar {	
	protected int xs=0, ys=0, xt=0, yt=0; 	
	List<List<Integer>> pathLists = new ArrayList<List<Integer>>();	
	
	aStar() {
	}
	
	boolean StarCalc (Map Ma, int x, int y, int indicator) {
		this.xs=x;
		this.ys=y;		
		
		pathLists.clear();
		
		if (indicator==-1) {
			// Test all exits
			for (int i=0;i<Ma.targets.size()/2;i++) {
				// Test 
				this.pathLists.add(new ArrayList<Integer>()); // Create new pathlist
				
				this.xt = Ma.targets.get(2*i); // Targets are global in class and iterated through
				this.yt = Ma.targets.get(2*i+1);			
				
				if(!_starCalc(Ma)) {
					System.out.println("Cannot find path to exit ");	
					return false;
				}			
			}				
			return true;
		}
		else if(indicator==-2) {
			// Head for waypoint
			this.pathLists.add(new ArrayList<Integer>()); // Create new pathlist
			this.xt = Ma.waypoints.get(0); // Targets are global in class and iterated through
			this.yt = Ma.waypoints.get(1);			
			
			if(!_starCalc(Ma)) {
				System.out.println("Cannot find path to exit ");	
				return false;
			}			
			return true;			
		}
		else {
			// Head for i = indicator
			this.pathLists.add(new ArrayList<Integer>()); // Create new pathlist
			this.xt = Ma.targets.get(2*indicator); // Targets are global in class and iterated through
			this.yt = Ma.targets.get(2*indicator+1);			
			
			if(!_starCalc(Ma)) {
				System.out.println("Cannot find path to exit ");	
				return false;
			}			
			return true;			
		}
	}	
	
	
	boolean _starCalc(Map Ma) {
		Node cNode=null, pNode = Ma.nodes[xs][ys];
		List<Node> oList= new ArrayList<Node>();
		List<Node> cList= new ArrayList<Node>();
		int lowestF=0;	
		
		// Algorithm start
		while (true) {	
			
			// For each exit
			if (oList.contains(pNode)) {
				oList.remove(pNode);
			}
			cList.add(pNode);
			
			for (int x = pNode.x-1;x<=pNode.x+1;x++) {
				for(int y = pNode.y-1;y<=pNode.y+1;y++) {
					if(cList.size() > 1 && oList.size()==0) {					
						// No path found
						return false;
					}					
					if (x<0 || y<0 || x>Ma.nodeshigh-1 || y>Ma.nodeswide-1) {continue;}
					if (cList.contains(Ma.nodes[x][y])) {continue;}						
					if (Ma.nodes[x][y].isWall) {continue;}					
					
					cNode=Ma.nodes[x][y]; // current node
	
					
					if (!oList.contains(cNode)) {
						oList.add(cNode);
						cNode.parent=pNode;			
					
						// Calculate parameters g and h					
					
						cNode.h = 10*(int) (Math.abs(x-xt) + Math.abs(y-yt)); // Heuristic, H constant from A*
						
						if (cNode.x!=pNode.x && cNode.y!=pNode.y) {		
							cNode.g = 14 + pNode.g;
						}
						else {
							cNode.g = 10 + pNode.g;
						}
																	
					}
					else {					
						if (cNode.x!=pNode.x && cNode.y!=pNode.y) {						
							if (14 + pNode.g < cNode.g) {
								cNode.parent=pNode;
								cNode.g = 14 + pNode.g;
							}							
						}
						else {
							if (10  + pNode.g < cNode.g) {
								cNode.parent=pNode;
								cNode.g = 10  + pNode.g;
							}
						}
					}
				}			
			}	
			
			
			lowestF=0;
			for (int i=oList.size()-1;i>=0;i--) {
				if (lowestF==0 || oList.get(i).g + oList.get(i).h < lowestF) {
					lowestF = oList.get(i).g + oList.get(i).h;
					pNode=oList.get(i);
				}
			}
			
			if (pNode.x == xt && pNode.y==yt) {
				_writeCord(pNode);		
				nodeClear(oList,cList);
				return true;			
			}
		}
	}
	
	void _writeCord(Node cNode) {
		if (cNode.parent.parent != null) {
			_writeCord(cNode.parent);
		}
		
		pathLists.get(pathLists.size()-1).add(cNode.x);
		pathLists.get(pathLists.size()-1).add(cNode.y);						
	}
			
	void nodeClear(List<Node> oList, List<Node> cList) {
		for (int i=0; i<cList.size();i++) {
			cList.get(i).g=0;
			cList.get(i).h=0;
			cList.get(i).parent=null;
		}
		for (int i=0; i<oList.size();i++) {
			oList.get(i).g=0;
			oList.get(i).h=0;
			oList.get(i).parent=null;
		}
	}	
}
