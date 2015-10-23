package Participants.AssuncaoDiStasio;

import java.util.ArrayList;

import Othello.Move;

/**
 * Evaluation des cases (chances) et référence de stratégie :
 * 
 * http://imagine.enpc.fr/~monasse/Info/Projets/2003/othello.pdf
 * http://emmanuel.adam.free.fr/site/IMG/pdf/jeuP.pdf
 *
 */

public class AI {

	private int ennemyID;
	private int playerID;
	private boolean iBegin = false;
	
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

	public AI() 
	{
	}

	public Move getBestMove(GameBoard gameBoard, int depth, int playerID) 
	{
		this.ennemyID = 1- playerID;
		this.playerID = playerID;
		
		Node moveResult = new Node(null);
		
		moveResult = ab_max(gameBoard, new Node(null), depth, Integer.MAX_VALUE);

		/*for(Move m : gameBoard2.getPossibleMoves(playerID))
		{
			Node root = new Node(m);
			Node result = ab_max(gameBoard2, root, depth, root.getEvaluation());
			
			if(moveResult == null) moveResult = result.getMove();
			else
			{
				if(getEvaluationMove(moveResult) < result.getEvaluation())
				{
					moveResult = result.getMove();
				}
			}
		}*/
		
		return moveResult.getMove();
	}

	private Node ab_max(GameBoard gameBoard, Node root, int depth, int parentMin) {

		ArrayList<Move> listMoves = gameBoard.getPossibleMoves(playerID);
		
		if (depth == 0 || listMoves.isEmpty())
		{
			root.setEvaluation(getEvaluationMove(gameBoard));
			return root;
		}
		
		Node maxNode = new Node(null);
		int max = -Integer.MAX_VALUE;

		for(Move m : listMoves)
		{
			GameBoard newGameBoard = gameBoard.clone();
			newGameBoard.addCoin(m, playerID);
			
			Node newNode = new Node(m);
			root.addChildNode(newNode);
			
			Node resultNode = ab_min(newGameBoard, newNode, depth-1, max);
			
			if(resultNode.getEvaluation() > max)
			{
				max = resultNode.getEvaluation();
				maxNode = newNode;
				maxNode.setEvaluation(max);
				
				if(max > parentMin) break;
			}
		}
		
		return maxNode;
	}
	
	private Node ab_min(GameBoard gameBoard, Node root, int depth, int parentMax) 
	{
		ArrayList<Move> listMoves = gameBoard.getPossibleMoves(ennemyID);
		
		if (depth == 0 || listMoves.isEmpty())
		{
			root.setEvaluation(getEvaluationMove(gameBoard));
			return root;
		}
		
		Node minNode = new Node(null);
		int min = Integer.MAX_VALUE;

		for(Move m : listMoves)
		{
			GameBoard newGameBoard = gameBoard.clone();
			newGameBoard.addCoin(m, ennemyID);
			
			Node newNode = new Node(m);
			root.addChildNode(newNode);
			
			Node resultNode = ab_max(newGameBoard, newNode, depth-1, min);
			
			if(resultNode.getEvaluation() < min)
			{
				min = resultNode.getEvaluation();
				minNode = newNode;
				minNode.setEvaluation(min);
				
				if(min < parentMax) break;
			}
		}

		return minNode;
	}
	
	private int getEvaluationMove(GameBoard game)
	{
		int result = 0;
		
		int nbCaseMilieu = GameBoard.BOARD_SIZE * GameBoard.BOARD_SIZE;
		int force = getForceBoard(game);
		int materiel = game.getCoinCount(playerID);
		int mobility = game.getPossibleMoves(playerID).size();
		
		int nbPionCoin = game.getCornerCoinCount(playerID);
		int nbPionEdge = game.getEdgeCoinCount(playerID);
		
		//Ouverture
		if((materiel + game.getCoinCount(ennemyID) - 4) <= 12)
		{
			//Mobilité et position favorisées.
			//pPosition important et pMobilité au max
			result = 10 * mobility + force;
		}
		//Fin
		else if(game.getCoinCount(playerID) + game.getCoinCount(ennemyID) > nbCaseMilieu)
		{
			//pMatériel max (nombre jeton important)
			result = nbPionEdge * 2 + materiel;
		}
		//Milieu
		else
		{
			//Important bord et coins, pPosition max
			result = 10 * force - 4 * nbPionCoin;
		}
		return result;
	}

	private int getForceBoard(GameBoard game)
	{
		int result = 0;
		
		for (int i = 0; i < game.BOARD_SIZE; i++) 
		{
			for (int j = 0; j < game.BOARD_SIZE; j++)
			{
				if(playerID == game.getPlayerIDAtPos(i, j))
				{
					result += evaluation[i][j];
				}
			}
		}
		return result;
	}
}
