import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Node {
	protected int x=-1, y=-1;
	int h=0,g=0,occupyingAgentID=-1;
	boolean isWall=false, isOccupied=false, isTarget=false,isMarked=false;
	Node parent=null;
	HashSet<Integer> isVisibleFromExit = new HashSet<Integer>(); // Visibility indicator	
	
	Node(int x,int y, Map Ma) {
		this.x=x;
		this.y=y;
	}	
	
}
