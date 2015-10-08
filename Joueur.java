package Participants.AssunDista;

import Othello.Move;

// Vous devrez étendre Othello.Joueur pour implémenter votre propre joueur...
public class Joueur extends Othello.Joueur {

	private int depth;
	private int enemyID;
	private GameBoard gameBoard;

	public Joueur(int depth, int playerID) {
		super();
		this.depth = depth;
		this.playerID = playerID;
		this.enemyID = 1 - playerID;
		this.gameBoard = new GameBoard();
	}

	public Move nextPlay(Move move) {

		// Add enemy coin to the gameboard
		gameBoard.addCoin(move, enemyID);

		// Get the best move (null if no move possible)
		Node bestMove = null;//ab_max.getBestMove(gameBoard., depth, playerID);

		// Add player coin to the gameboard
		gameBoard.addCoin(bestMove.getMove(), playerID);
		return bestMove.getMove();
	}

	/*public Node ab_max(Node root, int depth) {
		if (depth == 0 || root.isLeaf()) {
			return root;
		}

		Float maxVal = Float.NEGATIVE_INFINITY;
		Move maxOp = null;
		GameBoard tmp;

		for (Node node : root.getChildNodeList()) {
			tmp = gameBoard.clone();
			gameBoard.setCoin(node.getMove(), playerID);
			gameBoard = tmp;

			Node score = ab_min(node, depth - 1);

			if (score.getEvaluation() > maxVal) {
				maxVal = (float) score.getEvaluation();
				maxOp = score.getMove();
			}
		}

		return maxOp;
	}

	public Node ab_min(Node root, int depth) {
		if (depth == 0 || root.isLeaf()) {
			return root;
		}

		Float maxVal = Float.POSITIVE_INFINITY;
		Move maxOp = null;
		GameBoard tmp;

		for (Node node : root.getChildNodeList()) {
			tmp = gameBoard.clone();
			gameBoard.setCoin(node.getMove(), playerID);
			gameBoard = tmp;

			Node score = ab_max(node, depth - 1);

			if (score.getEvaluation() < maxVal) {
				maxVal = (float) score.getEvaluation();
				maxOp = score.getMove();
			}
		}
		return maxOp;
	}*/

}