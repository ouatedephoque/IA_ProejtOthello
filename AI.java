package Participants.AssuncaoDiStasio;

import java.util.ArrayList;

import Othello.Move;

/**
 * Evaluation des cases (chances) :
 * http://imagine.enpc.fr/~monasse/Info/Projets/2003/othello.pdf
 *
 */

public class AI {

	private Node root;
	private int[][] evaluation = new int[][]{
			{500, -150, 30, 10, 10, 30, -150, 500},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{500, -150, 30, 10, 10, 30, -150, 500}
	};

	public AI() {
		root = null;
	}

	public static Move getBestMove(GameBoard gameBoard, int depth, int playerID) 
	{
		ArrayList<Move> listMoves = gameBoard.getPossibleMoves(playerID);
		GameBoard gameBoard2 = gameBoard.clone();
		// alphabeta(, depth);
		return null;
	}

	private double alphabeta(Node root, int depth, int minOrMax, double parentValue) {
		if (root.isLeaf() || depth == 0)
		{
			return root.getEvaluation();
		}
		
		double value = ((double)minOrMax) * Double.POSITIVE_INFINITY;

		for(Node n : root.getChildNodeList())
		{
			double value2 = 
		}
		
		return 0.0;
	}
	
	private Node createTree()
	{
		return null;
	}
}
