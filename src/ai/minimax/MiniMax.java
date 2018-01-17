package ai.minimax;

import util.Tuple;
import core.Board;

/**
 * Classe che individua il minimo e il massimo
 * @author Mina
 *
 */

public class MiniMax {
	static String color = " ";
	private Root root;
	
	/**
	 * 
	 * @param color è di tipo stringa
	 * @param board è un oggetto della classe Board
	 * @throws Exception cattura le eccezioni
	 */
	
	public MiniMax(String color, Board board ) throws Exception {
		this.root = new Root(new MMNode(null, null, board, color));
//		Node.back_propagate(root.getRoot());
		MMNode.update_tree(this.root.getRoot());
	}
	
	public Tuple<Integer, Integer> getMove() {
		return this.root.getRoot().get_best_move();
	}
}
