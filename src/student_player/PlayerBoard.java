package student_player;

import java.util.ArrayList;

import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurTile;

public class PlayerBoard {
	
	public static final int BOARD_SIZE = 14;
    public static final int originPos = 5;

    public static final int EMPTY = -1;
    public static final int TUNNEL = 1;
    public static final int WALL = 0;
	
	public SaboteurTile[][] hboard;
    public int[][] hintBoard;
    
    public ArrayList<SaboteurCard> hands;
    public ArrayList<SaboteurCard> Deck;
    
    public ArrayList<SaboteurMove> moves;
    public int self;
    
    public PlayerBoard(int s, ArrayList<SaboteurMove> m, ArrayList<SaboteurCard> h, SaboteurTile[][] b, int[][] intb){
    	self = s;
    	moves = m;
    	Deck = SaboteurCard.getDeck();
    	hands = h;
    	for(SaboteurCard c : hands) {
    		
    	}
    }
    
}
