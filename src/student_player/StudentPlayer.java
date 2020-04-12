package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.cardClasses.SaboteurTile;

import java.util.ArrayList;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;


/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("Linda");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        //MyTools.getSomething();

        // Is random the best you can do?
        //Move myMove = boardState.getRandomMove();
        
    	int val = -1000000;
    	ArrayList<SaboteurMove> list = boardState.getAllLegalMoves();
    	SaboteurMove finalmove = list.get(0);
    	int self = boardState.getTurnPlayer();
    	SaboteurTile[][] hboard = boardState.getHiddenBoard();
    	int[][] hintboard = boardState.getHiddenIntBoard();
    	
    	/*for(SaboteurMove move : list) {
    		SaboteurBoardState clone = (SaboteurBoardState) boardState.clone();
    		clone.processMove(move);
    		
    		int value = MyTools.alpha_beta_pruning(val,1000000,0,boardState);
    		
    		if(value > val) {
    			finalmove = move;
    			val = value;
    		}
    	}*/
    	
    	
    	SaboteurMove tile =  MyTools.tile(list, hboard, hintboard);
    	
    	Move myMove = list.get(0);
    	if(tile != null) myMove=tile;
    	if(tile ==null ||MyTools.GetHeuristic(tile, hboard, hintboard) <1000) {
    		SaboteurMove map =  MyTools.map(list, hboard);
        	SaboteurMove destroy =  MyTools.destroy(list, hboard);
	    	if(destroy != null) myMove = destroy;
	    	if(map != null) myMove = map;
    		SaboteurMove bonus = MyTools.Bonus(list, boardState.getNbMalus(self));
        	SaboteurMove malus = MyTools.Malus(list);
        	if(bonus!=null) myMove = bonus;
        	if(malus!=null) myMove = malus;
    	}
    	
    	
        // Return your move to be processed by the server.
        return myMove;
    }
}