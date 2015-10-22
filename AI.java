package Participants.AssuncaoDiStasio;

import java.util.ArrayList;

import Othello.Move;

/**
 * Evaluation des cases (chances) :
 * http://imagine.enpc.fr/~monasse/Info/Projets/2003/othello.pdf
 *
 */

public class AI {

	private int ennemyID;
	private int playerID;
	
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
	}

	public Move getBestMove(GameBoard gameBoard, int depth, int playerID) 
	{
		this.ennemyID = 1- playerID;
		this.playerID = playerID;
		
		GameBoard gameBoard2 = gameBoard.clone();
		Move moveResult = null;
		
		for(Move m : gameBoard2.getPossibleMoves(playerID))
		{
			Node root = new Node(m);
			Node result = ab_min(gameBoard2, root, depth, root.getEvaluation());
			
			if(moveResult == null) moveResult = result.getMove();
			else
			{
				if(getEvaluationMove(moveResult) < result.getEvaluation())
				{
					moveResult = result.getMove();
				}
			}
		}
		
		return moveResult;
	}

	private Node ab_max(GameBoard gameBoard, Node root, int depth, double parentMin) {

		ArrayList<Move> listMoves = gameBoard.getPossibleMoves(playerID);
		
		if (root.isLeaf() || depth == 0)
		{
			root.setEvaluation(getEvaluationMove(root.getMove()));
			return root;
		}
		
		//double maxValue = Double.NEGATIVE_INFINITY;
		Node maxNode = new Node(null);
		maxNode.setEvaluation((int)Double.NEGATIVE_INFINITY);

		for(Move m : listMoves)
		{
			GameBoard newGameBoard = gameBoard.clone();
			newGameBoard.addCoin(m, playerID);
			
			Node newNode = new Node(m);
			
			Node resultNode = ab_min(newGameBoard, newNode, depth-1, maxNode.getEvaluation());
			
			if(resultNode.getEvaluation() > maxNode.getEvaluation())
			{
				maxNode = resultNode;
				if(maxNode.getEvaluation() > parentMin) break;
			}
		}
		
		return maxNode;
	}
	
	private Node ab_min(GameBoard gameBoard, Node root, int depth, double parentMin) 
	{
		ArrayList<Move> listMoves = gameBoard.getPossibleMoves(ennemyID);
		
		if (root.isLeaf() || depth == 0)
		{
			root.setEvaluation(getEvaluationMove(root.getMove()));
			return root;
		}
		
		//double minValue = Double.POSITIVE_INFINITY;
		Node minNode = new Node(null);
		minNode.setEvaluation((int)Double.NEGATIVE_INFINITY);

		for(Move m : listMoves)
		{
			GameBoard newGameBoard = gameBoard.clone();
			newGameBoard.addCoin(m, playerID);
			
			Node newNode = new Node(m);
			
			Node resultNode = ab_max(newGameBoard, newNode, depth-1, minNode.getEvaluation());
			
			if(resultNode.getEvaluation() > minNode.getEvaluation())
			{
				minNode = resultNode;
				if(minNode.getEvaluation() > parentMin) break;
			}
		}
		
		return minNode;
	}
	
	private int getEvaluationMove(Move move)
	{
		return evaluation[move.i][move.j]; 
	}
}
