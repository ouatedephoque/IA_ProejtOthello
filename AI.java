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
	private int iBegin = 0;

	//Tableau d'évaluation
	private int[][] evaluation = new int[][] {
			{ 500, -150, 30, 10, 10, 30, -150, 500 },
			{ -150, -250, 0, 0, 0, 0, -250, -150 },
			{ 30, 0, 1, 2, 2, 1, 0, 30 }, { 10, 0, 2, 16, 16, 2, 0, 10 },
			{ 10, 0, 2, 16, 16, 2, 0, 10 }, { 30, 0, 1, 2, 2, 1, 0, 30 },
			{ -150, -250, 0, 0, 0, 0, -250, -150 },
			{ 500, -150, 30, 10, 10, 30, -150, 500 } };

	public AI() 
	{
	}

	//Fonction pour trouver le meilleure mouvement
	public Move getBestMove(GameBoard gameBoard, int depth, int playerID) {
		this.ennemyID = 1 - playerID;
		this.playerID = playerID;
		this.iBegin = (gameBoard.getCoinCount(playerID) + gameBoard.getCoinCount(ennemyID)) % 2;	//Savoir si on commence ou non

		Node moveResult = new Node(null);

		changeValueCoinCornerOccuped(gameBoard);
		moveResult = ab_max(gameBoard, new Node(null), depth, Integer.MAX_VALUE);	//Débuter l'alpha-beta

		return moveResult.getMove();
	}

	//Fonction alpha-beta maximum
	private Node ab_max(GameBoard gameBoard, Node root, int depth, int parentMin) {

		ArrayList<Move> listMoves = gameBoard.getPossibleMoves(playerID);

		if (depth == 0 || listMoves.isEmpty()) {
			root.setEvaluation(getEvaluationMove(gameBoard));
			return root;
		}

		Node maxNode = new Node(null);
		int max = -Integer.MAX_VALUE;

		for (Move m : listMoves) {
			GameBoard newGameBoard = gameBoard.clone();
			newGameBoard.addCoin(m, playerID);

			Node newNode = new Node(m);
			root.addChildNode(newNode);

			Node resultNode = ab_min(newGameBoard, newNode, depth - 1, max);

			if (resultNode.getEvaluation() > max) {
				max = resultNode.getEvaluation();
				maxNode = newNode;
				maxNode.setEvaluation(max);

				if (max > parentMin)
					break;
			}
		}

		return maxNode;
	}

	//Fonction alpha-beta minimum
	private Node ab_min(GameBoard gameBoard, Node root, int depth, int parentMax) {
		ArrayList<Move> listMoves = gameBoard.getPossibleMoves(ennemyID);

		if (depth == 0 || listMoves.isEmpty()) {
			root.setEvaluation(getEvaluationMove(gameBoard));
			return root;
		}

		Node minNode = new Node(null);
		int min = Integer.MAX_VALUE;

		for (Move m : listMoves) {
			GameBoard newGameBoard = gameBoard.clone();
			newGameBoard.addCoin(m, ennemyID);

			Node newNode = new Node(m);
			root.addChildNode(newNode);

			Node resultNode = ab_max(newGameBoard, newNode, depth - 1, min);

			if (resultNode.getEvaluation() < min) {
				min = resultNode.getEvaluation();
				minNode = newNode;
				minNode.setEvaluation(min);

				if (min < parentMax)
					break;
			}
		}

		return minNode;
	}

	// Fonction d'évaluation pour les mouvements
	// Stratégie différente si on commence ou non
	private int getEvaluationMove(GameBoard game) {
		int result = 0;

		int force = getForceBoard(game);
		int mobility = game.getPossibleMoves(playerID).size();
		int nbPionTotal = game.getCoinCount(playerID)
				+ game.getCoinCount(ennemyID);

		int materiel = 0;
		if (game.getCoinCount(playerID) > game.getCoinCount(ennemyID)) {
			materiel = 1;
		} else if (game.getCoinCount(playerID) < game.getCoinCount(ennemyID)) {
			materiel = -1;
		}

		// Ouverture (-30 pions)
		if (nbPionTotal < 30) {
			// Mobilité et position favorisées.
			if(iBegin == 0)
				result = 50 * mobility + force + 2 * materiel;
			else
				result = 30 * mobility + force + 10 * materiel;
		}
		// Fin (+ 45 pions)
		else if (nbPionTotal > 45) {
			if(iBegin == 0)
				result = force / 30 + materiel;
			else
				result = force / 20 + materiel;

		}
		// Milieu (entre 15 et 45 pions)
		else {
			if(iBegin == 0)
				result = 50 * mobility + force + 10 * materiel;
			else
				result = 30 * mobility + force + 4 * materiel;
		}

		return result;
	}

	private int getForceBoard(GameBoard game) {
		int result = 0;

		for (int i = 0; i < GameBoard.BOARD_SIZE; i++) {
			for (int j = 0; j < GameBoard.BOARD_SIZE; j++) {
				if (playerID == game.getPlayerIDAtPos(i, j)) {
					result += evaluation[i][j];
				}
			}
		}
		return result;
	}
	
	private void changeValueCoinCornerOccuped(GameBoard game)
	{		
		if(playerID == game.getPlayerIDAtPos(0, 0))
		{
			changEvaluationTab(0, 0);
		}
		
		if(playerID == game.getPlayerIDAtPos(0, GameBoard.BOARD_SIZE-1))
		{
			changEvaluationTab(0, GameBoard.BOARD_SIZE-1);
		}
		
		if(playerID == game.getPlayerIDAtPos(GameBoard.BOARD_SIZE-1, 0))
		{
			changEvaluationTab(GameBoard.BOARD_SIZE-1, 0);
		}
		
		if(playerID == game.getPlayerIDAtPos(GameBoard.BOARD_SIZE-1, GameBoard.BOARD_SIZE-1))
		{
			changEvaluationTab(GameBoard.BOARD_SIZE-1, GameBoard.BOARD_SIZE-1);
		}
	}
	
	private void changEvaluationTab(int x, int y)
	{
		int directionX = (x == GameBoard.BOARD_SIZE-1) ? -1 : 1;
		int directionY = (y == GameBoard.BOARD_SIZE-1) ? -1 : 1;
		
		evaluation[x+directionX][y] = 150;
		evaluation[x][y+directionY] = 150;
		evaluation[x+directionX][y+directionY] = 250;
		
		for(int i = 0; i < GameBoard.BOARD_SIZE; i++)
		{
			evaluation[x][i] *= 3;
			evaluation[i][y] *= 3;
		}
	}
}
