package student_player;

import java.util.ArrayList;

import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;

public class AndNode extends AndOrNode{
	public SaboteurCard dealedCard;
	public double prob;
	public ORNode parent;
	public ArrayList<ORNode> succesors;
	public int depth;
	
	public AndNode(String name, BoardState boardState,SaboteurCard dealedCard, double dealedCardProb) {
		super(name,boardState);
		this.dealedCard = dealedCard;
		if(boardState.getTurnPlayer() == 1) {
			boardState.player1Cards.add(dealedCard);
		}else {
			boardState.player2Cards.add(dealedCard);
		}
		this.prob = dealedCardProb;
		ArrayList<SaboteurMove> moves = boardState.getAllLegalMoves();
		int counter = 0;
		this.succesors = new ArrayList<>();
		for(SaboteurMove move: moves) {
			String nodeName = "OR,"+(depth+1)+","+counter;
			BoardState nodeBoardState = new BoardState(boardState);
			nodeBoardState.processMove(move, false);
			ORNode node = new ORNode(nodeName,nodeBoardState,move);
			node.parent = this;
			succesors.add(node);
		}
	}
	
	public double getMinHeuristicVal(int[] goalPos,int depth, int maxDepth) {
		double minHeuristicVal = Integer.MAX_VALUE-10000;
		for(ORNode node: succesors) {
			node.calculateHeuristic(goalPos);
			if(node.heuristicVal > this.parent.heuristicVal) continue;
			double curNodeHeuristicVal = node.getExpectedMinHeuistic(goalPos,depth+1, maxDepth);
			if(curNodeHeuristicVal < minHeuristicVal) {
				minHeuristicVal = curNodeHeuristicVal;
			}
			if(minHeuristicVal == Integer.MIN_VALUE)
				break;
		}
		return minHeuristicVal;
	}
	
}
