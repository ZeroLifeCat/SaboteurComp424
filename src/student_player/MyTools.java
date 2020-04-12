package student_player;

import java.util.ArrayList;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurMap;
import Saboteur.cardClasses.SaboteurTile;
import boardgame.Board;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }
    
    /*public static int alpha_beta_pruning(int alpha, int beta,int depth ,SaboteurBoardState state) {
    	if(depth == 2 || state.getWinner() != Board.NOBODY) {
    		 if (depth %2 == 0) {
    			 return GetHeuristic(state)/(depth+1);
    		 }
    		 return - GetHeuristic(state)/(depth+1);
    	}
    	
    	int val = -1000000;
    	if(depth%2 ==0) {
    		val = 1000000;
    	}
    	
    	ArrayList<SaboteurMove> list = state.getAllLegalMoves();
    	for(SaboteurMove move : list) {
    		SaboteurBoardState clone = (SaboteurBoardState) state.clone();
    		clone.processMove(move);
    		
    		int value = alpha_beta_pruning(alpha, beta, depth+1, clone);
    		
    		if(alpha>= beta) {
    			return depth%2 ==0? alpha: beta;
    		}
    		
    		if(depth%2 ==0) {
    			if(value < val) {
    				val = value;
    			}
    			if(beta > val) {
    				beta = val;
    			}
    		}
    		else {
    			if(value > val) {
    				val = value;	
    			}
    			if(alpha < val) {
    				alpha = val;
    			}
    		}
    		
    		if(alpha >= beta) {
    			return depth%2 ==0? alpha:beta;
    		}
    	}
    		
    	return val;
    }
    
    
    //TODO: Observe the board state and calculate the heuristic value.
    /*public static int GetHeuristic(SaboteurBoardState state) {
    	
    	int score = 0;
    	
    	int origin = 5;
    	int goal = 12;
    	int opponent = state.getTurnPlayer();
    	int self = 1-opponent;
    	SaboteurTile[][] board = state.getBoardForDisplay();
    	
    	//check winner state
    	if(state.getWinner() == opponent) {
    		return -100000;
    	}
    	if(state.getWinner() == self) {
    		return 100000;
    	}
    	
    	//Malus state
    	int nbmalusself = state.getNbMalus(self);
    	int nbmalusoppo = state.getNbMalus(opponent);
    	if(nbmalusself > 0) {
    		score -= 50;
    	}
    	if(nbmalusoppo > 0) {
    		score += 50;
    	}
    	
    	//Hidden Tile
    	boolean GoalHidden = true;
    	int[] nugget = new int[2];
    	if(board[goal][origin].getIdx().equals("nugget")) {
    		nugget[0] = goal;
    		nugget[1] = origin;
    		GoalHidden = false;
    	}
    	else if(board[goal][origin-2].getIdx().equals("nugget")) {
    		nugget[0] = goal;
    		nugget[1] = origin-2;
    		GoalHidden = false;
    	}
    	else if(board[goal][origin+2].getIdx().equals("nugget")) {
    		nugget[0] = goal;
    		nugget[1] = origin+2;
    		GoalHidden = false;
    	}
    	
    	int[][] tilemap = new int[board.length][board[0].length];
    	for(int i = 0;i<board.length;i++) {
    		for (int j = 0; j<board[0].length;j++) {
    			if(board[i][j] != null) {
    				tilemap[i][j] = 1;
    			}
    			else {
    				tilemap[i][j] = 0;
    			}
    		}
    	}
    	
    	PathTree pathTree = new PathTree(board, tilemap);
    	ArrayList<TileNode> leaves = pathTree.leaves;
    	//two mode
    	if(GoalHidden) {
    		int mindist = 100;
    		int mindistleft = 100;
    		int mindistright = 100;
    		for(TileNode leaf : leaves) {
    			int dist = checkDist(leaf.x(),leaf.y(),5, 12);
    			int dist2 = checkDist(leaf.x(),leaf.y(),3,12);
    			int dist3 = checkDist(leaf.x(),leaf.y(),7, 12);
    			boolean isDeadEnd = checkDeadEnd(leaf.tile);
    			if(dist < mindist && !isDeadEnd) {
    				mindist = dist;
    			}
    			if(dist2 < mindistleft && !isDeadEnd) {
    				mindistleft = dist2;
    			}
    			if(dist3 < mindistright && !isDeadEnd) {
    				mindistright = dist3;
    			}
    		}
    		score += 3/(mindist+mindistleft+mindistright);
    	}
    	else {
    		int mindist = 100;
    		for(TileNode leaf : leaves) {
    			int dist = checkDist(leaf.x(),leaf.y(),nugget[0], nugget[1]);
    			if(dist < mindist && !checkDeadEnd(leaf.tile)) {
    				mindist = dist;
    			}
    		}
    		score += 1/mindist;
    	}
    	
    	return score;
    }*/
    
    public static double GetHeuristic(SaboteurMove m, SaboteurTile[][] board, int[][] intboard) {
    	int[] nugget = checkNugget(board);
    	SaboteurTile card = (SaboteurTile) m.getCardPlayed();
    	
    	for(int i = 0; i<3; i++) {
    		for(int j = 0 ; j<3; j++) {
    			intboard[3*m.getPosPlayed()[0]+2-j][3*m.getPosPlayed()[1]+i] = card.getPath()[i][j];
    		}
    	}
    	
    	double res = countLiveEnd(intboard)/100.0;
    	
    	if(card.getIdx().equals("6")||card.getIdx().equals("6_flip")) {
    		res+=0.04;
    	}
    	if(card.getIdx().equals("8")) {
    		res+=0.05;
    	}
    	if(card.getIdx().equals("0")) {
    		res+=0.03;
    	}
    	
    	int x = checkDist(m.getPosPlayed()[0],m.getPosPlayed()[1],12,3);
		int x2 = checkDist(m.getPosPlayed()[0],m.getPosPlayed()[1],12,5);
		int x3 = checkDist(m.getPosPlayed()[0],m.getPosPlayed()[1],12,7);
		
		res += 3.0/(x+x2+x3);
    	
    	board[m.getPosPlayed()[0]][m.getPosPlayed()[1]] = card;
    	PathTree pt = new PathTree(board);
    	double sd = 100;
    	if(nugget != null) {
    		for(TileNode tn : pt.leaves) {
    			if(!checkDeadEnd(tn.tile)) {
    				ArrayList<SaboteurTile> p = pt.GetPath(tn);
	    			int d = checkDist(tn.x(),tn.y(),nugget[0],nugget[1]);
	    			if(d == 0) {
	    				return 10000;
	    			}
	    			else if(p.contains(board[nugget[0]][nugget[1]])) {
	    				return 10000;
	    			}
	    				
	    			else if(d == 1||d==2) {
	    				return -1;
	    			}
	    			else {
	    				if(d < sd) {
	    					sd = d;
	    				}
	    			}
    			}
    		}
    	}
    	else {
    		for(TileNode tn : pt.leaves) {
    			if(!checkDeadEnd(tn.tile)) {
	    			int d = checkDist(tn.x(),tn.y(),12,3);
	    			int d2 = checkDist(tn.x(),tn.y(),12,5);
	    			int d3 = checkDist(tn.x(),tn.y(),12,7);
	    			ArrayList<SaboteurTile> p = pt.GetPath(tn);
	    			if(p.contains(board[12][3])&&p.contains(board[12][5])&&p.contains(board[12][7])) {
	    				return 10000;
	    			}
	    			else if(d == 0&&p.contains(board[12][5])&&p.contains(board[12][7])) {
	    				return 10000;
	    			}
	    			else if(p.contains(board[12][3])&&d2 == 0&&p.contains(board[12][7])) {
	    				return 10000;
	    			}
	    			else if(p.contains(board[12][3])&&p.contains(board[12][5])&&d3 == 0) {
	    				return 10000;
	    			}
	    			
	    			else if(p.contains(board[12][3])&&p.contains(board[12][5])) {
	    				return 5000;
	    			}
	    			else if(p.contains(board[12][3])&&p.contains(board[12][7])) {
	    				return 5000;
	    			}
	    			else if(p.contains(board[12][7])&&p.contains(board[12][5])) {
	    				return 5000;
	    			}
	    			else if(d == 0 || d2 == 0 || d3==0) {
	    				return 1000;
	    			}
	    			else if(d == 1) {
	    				return -1;
	    			}
	    			else {
	    				if((d+d2+d3)/3.0 < sd) {
	    					sd = (d+d2+d3)/3.0;
	    				}
	    			}
    			}
    		}
    	}
    	
    	board[m.getPosPlayed()[0]][m.getPosPlayed()[1]] = null;
    	for(int i = 0; i<3; i++) {
    		for(int j = 0 ; j<3; j++) {
    			intboard[3*m.getPosPlayed()[0]+2-j][3*m.getPosPlayed()[1]+i] = -1;
    		}
    	}
    	
    	
    	return 1.0/sd+res;
    }
    
    public static int countLiveEnd(int[][] intboard) {
    	int n = 0;
    	for(int i = 0; i < intboard.length-1; i++) {
    		for(int j = 0; j < intboard.length-1; j++) {
    			if( Math.abs(intboard[i][j] - intboard[i+1][j])==2)n++;
    			if( Math.abs(intboard[i][j] - intboard[i][j+1])==2)n++;
    		}
    	}
    	return n;
    }
    
    public static boolean checkConnected(SaboteurTile tile1, SaboteurTile tile2, int i, int j, int i2, int j2) {
    	if(checkDist(i,j,i2,j2) == 1) {
    		if(i - i2 == -1) return tile1.getPath()[1][2] == 1&&tile2.getPath()[1][0]==1;
    		if(i - i2 == 1) return tile1.getPath()[1][0] == 1&&tile2.getPath()[1][2]==1;
    		if(j - j2 == -1) return tile1.getPath()[2][1] == 1&&tile2.getPath()[0][1]==1;
    		if(j - j2 == 1) return tile1.getPath()[0][1] == 1&&tile2.getPath()[2][1]==1;
    	}
    	return false;
    }
    
    public static int checkDist(int i, int j, int i2, int j2) {
    	return Math.abs(i-i2) + Math.abs(j-j2);
    }
    
    public static boolean checkDeadEnd(SaboteurTile tile) {
    	String idx = tile.getIdx();
    	if(idx.equals("1")||idx.equals("2")||idx.equals("2_flip")||idx.equals("3")||idx.equals("3_flip")||
    			idx.equals("4")||idx.equals("4_flip")||idx.equals("11")||idx.equals("11_flip")||
    			idx.equals("12")||idx.equals("12_flip")||idx.equals("13")||idx.equals("14")||idx.equals("14_flip")) {
    		return true;
    	}
    	return false;
    }
    
    public static int[] checkNugget(SaboteurTile[][] board) {
    	if(board[12][3] != null) {
    		if(board[12][3].getIdx().equals("nugget")) return new int[]{12,3};
    	}
    	if(board[12][5] != null) {
    		if(board[12][5].getIdx().equals("nugget")) return new int[]{12,5};
    	}
    	if(board[12][7] != null) {
    		if(board[12][7].getIdx().equals("nugget")) return new int[]{12,7};
    	}
    	return null;
    }
    
    public static SaboteurMove Bonus(ArrayList<SaboteurMove> moves, int nbMalus) {
    	for(SaboteurMove m : moves) {
    		String type = m.getCardPlayed().getName();
    		if(type.equals("Bonus")) {
    			if(nbMalus > 0) {
    				return m;
    			}
    		}
    	}
    	return null;
    }
    
    public static SaboteurMove Malus(ArrayList<SaboteurMove> moves) {
    	for(SaboteurMove m : moves) {
    		String type = m.getCardPlayed().getName();
		    if(type.equals("Malus")) {
				return m;
			}
    	}
    	return null;
    }
			
    
    
    public static SaboteurMove map(ArrayList<SaboteurMove> moves, SaboteurTile[][] boardstate) {
    	if(boardstate[12][3].getIdx().equals("nugget")||boardstate[12][5].getIdx().equals("nugget")||boardstate[12][7].getIdx().equals("nugget")) {
    		return null;
    	}
    	
    	for(SaboteurMove m : moves) {
    		String type = m.getCardPlayed().getName();
    		if(type.equals("Map")) {
    			int[] pos = m.getPosPlayed();
    			if(boardstate[pos[0]][pos[1]].getIdx().equals("8")) {
    				return m;
    			}
    		}
    	}
    	return null;
    	
    }
    
    public static SaboteurMove destroy(ArrayList<SaboteurMove> moves, SaboteurTile[][] boardstate) {
    	for(SaboteurMove m : moves) {
    		String type = m.getCardPlayed().getName();
    		if(type.equals("Destroy")) {
    			int[] pos = m.getPosPlayed();
    			if(checkDeadEnd(boardstate[pos[0]][pos[1]])) {
    				return m;
    			}
    		}
    	}
    	return null;
    }
    
    public static SaboteurMove drop(ArrayList<SaboteurMove> moves, ArrayList<SaboteurCard> hand, SaboteurTile[][] board) {
    	SaboteurMove result = null;
    	for(SaboteurMove m : moves) {
    		String type = m.getCardPlayed().getName();
    		if(type.equals("Drop")) {
    			SaboteurCard c = hand.get(m.getPosPlayed()[0]);
    			if(c instanceof SaboteurTile) {
    				SaboteurTile tmp = (SaboteurTile)c;
    				if(result == null) {
						result = m;
					}
    				else if(checkDeadEnd(tmp)) {
    					result = m;
    				}
    				else if(tmp.getIdx().equals("5")||tmp.getIdx().equals("5_flip")||tmp.getIdx().equals("7")||tmp.getIdx().equals("7_flip")) {
    					if(result.getCardPlayed() instanceof SaboteurTile) {
    						if(!checkDeadEnd((SaboteurTile)result.getCardPlayed())) {
    							result = m;
    						}
    					}
    				}
    			}
    			if(c instanceof SaboteurMap && checkNugget(board)!=null) {
    				result = m;
    			}
    		}
    	}
    	return result;
    }
    
    public static SaboteurMove tile(ArrayList<SaboteurMove> moves, SaboteurTile[][] board,int[][] intboard) {
    	ArrayList<SaboteurMove> tilemoves = new ArrayList<SaboteurMove>();
    	for(SaboteurMove m : moves) {
    		if(m.getCardPlayed() instanceof SaboteurTile) {
    			tilemoves.add(m);
    		}
    	}
    	double score = -100000;
    	SaboteurMove result = null;
    	for(SaboteurMove m : tilemoves) {
    		double tmp = GetHeuristic(m, board, intboard);
    		if(tmp > score) {
    			score = tmp;
    			result = m;
    		}
    	}
    	return result;
    }
    
    
}