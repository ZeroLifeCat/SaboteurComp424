package student_player;

import java.util.ArrayList;

import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurTile;

public class PathTree{
	public TileNode root;
	public ArrayList<TileNode> leaves = new ArrayList<TileNode>();
	public int length;
	
	//Init
	public PathTree(SaboteurTile[][] board) {
		int depth = 1;
		//root = origin
		root = new TileNode(board[5][5],5,5,0, null);
		//visited map
		boolean[][] visited = new boolean[board.length][board[0].length];
		visited[5][5] = true; 
		
		//parents list
		ArrayList<TileNode> parents = new ArrayList<TileNode>();
		parents.add(root);
		
		while(parents.size() != 0) {
			//children list
			ArrayList<TileNode> children = new ArrayList<TileNode>();
			
			for(TileNode p : parents) {
				//if p is a dead end, then there is no child
				if(checkDeadEnd(p.tile)) {
					leaves.add(p);
				}
				
				else {
    				int[][] neighbors = {{p.x()+1,p.y()},{p.x()-1,p.y()},{p.x(),p.y()+1},{p.x(),p.y()-1}};
    				int nbChild = 0;
    				//check children
    				for(int[] nei : neighbors) {
    					//check if the position exist on the board and either there is a tile(not a null object)
    					if(nei[0] < 14 && nei[0] > 0 && nei[1] < 14 && nei[1] > 0) {
    						if(board[nei[0]][nei[1]] != null) {
    							SaboteurTile neighbor = board[nei[0]][nei[1]];
    							
    							//check if connected
    							boolean connected = checkConnected(p.tile, neighbor, p.x(),p.y(),nei[0],nei[1]);
    							
    							
    							//if the node is connected and not visited
    							if(connected && !visited[nei[0]][nei[1]]) {
    								TileNode child = new TileNode(neighbor, nei[0], nei[1], depth, p);
    								p.Addchild(child);
    								children.add(child);
    								visited[nei[0]][nei[1]] = true;
    								nbChild++;
    							}
    						}
    					}
    				}
    				
    				//no child , becomes a leaf
    				if(nbChild == 0) {
    					leaves.add(p);
    				}
				}
			}
			
			//children become next parents
			parents = children;
			depth++;
		}
		
		length = depth-1;
	}
	
    public boolean checkDeadEnd(SaboteurTile tile) {
    	String idx = tile.getIdx();
    	if(idx.equals("1")||idx.equals("2")||idx.equals("2_flip")||idx.equals("3")||idx.equals("3_flip")||
    			idx.equals("4")||idx.equals("4_flip")||idx.equals("11")||idx.equals("11_flip")||
    			idx.equals("12")||idx.equals("12_flip")||idx.equals("13")||idx.equals("14")||idx.equals("14_flip")) {
    		return true;
    	}
    	return false;
    }
    
    public ArrayList<SaboteurTile> GetPath(TileNode tn){
    	ArrayList<SaboteurTile> list = new ArrayList<SaboteurTile>();
    	list.add(tn.tile);
    	TileNode p = tn.parent;
    	while(p != null) {
    		list.add(p.tile);
    		p = p.parent;
    	}
    	return list;
    }
    
    public boolean checkConnected(SaboteurTile tile1, SaboteurTile tile2, int i, int j, int i2, int j2) {
    	if(Math.abs(i-i2) + Math.abs(j-j2) == 1) {
    		if(j - j2 == -1) return tile1.getPath()[2][1] == 1&&tile2.getPath()[0][1]==1;
    		if(j - j2 == 1) return tile1.getPath()[0][1] == 1&&tile2.getPath()[2][1]==1;
    		if(i - i2 == 1) return tile1.getPath()[1][2] == 1&&tile2.getPath()[1][0]==1;
    		if(i - i2 == -1) return tile1.getPath()[1][0] == 1&&tile2.getPath()[1][2]==1;
    	}
    	return false;
    }

	
}
	

