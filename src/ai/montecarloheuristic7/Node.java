package ai.montecarloheuristic7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Board;

import util.Tuple;

/**
 * Classe relativa ai nodi di MonteCarloHeuristic7
 * @author Mina
 *
 */

public class Node {
	private Tuple<Integer, Integer> move;
	private String color;
	private Node parent;
	private int value, visit, moveNumber;
	private List<Node> children;
	private List<Tuple<Integer, Integer>> untriedMoves;
	
	/**
	 * 
	 * @param parent
	 * @param move
	 * @param color
	 * @param board
	 * @param moveNumber
	 * @param mcColor
	 */
	
	public Node(Node parent, Tuple<Integer, Integer> move, String color, Board board, int moveNumber, String mcColor) {
		this.move = move;
		this.color = color;
		this.parent = parent;
		this.value = 0;
		this.children = new ArrayList<Node>();
		int valuenodeh7 = 7;
		if(mcColor.equals(color)) {
			this.untriedMoves = board.heuristic_bestX_moves(mcColor, valuenodeh7);
		} else {
			//Moves for my opponent => use all.
			this.untriedMoves = board.getListValidMoves();
		}
		this.moveNumber = moveNumber;
	}

	/**
	 * 
	 * @param board
	 * @return
	 * @throws Exception
	 */
	public Node expand(Board board, String mcColor){
		String newColor;
		Random generator = new Random();
		int randomIndex = generator.nextInt(this.untriedMoves.size());
		Tuple<Integer, Integer> move = this.untriedMoves.get(randomIndex);
		this.untriedMoves.remove(randomIndex);
		try {
			board.makeMove(move, color);
		} catch (Exception e) {
			System.out.println("There's an error"); 
			// TODO Auto-generated catch block
		}
		if("w".equals(this.color)) {
			newColor = "b";
		} else {
			newColor = "w";
		}
		Node node = new Node(this, move, newColor, board, this.moveNumber + 1, mcColor);
		this.children.add(node);
		return node;
	}
	/**
	 * @return the moveNumber
	 */
	public int getMoveNumber() {
		return moveNumber;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value of which update
	 */
	public void updateValue(double update_val) {
		this.value += update_val;
	}

	/**
	 * @return the visit
	 */
	public int getVisit() {
		return visit;
	}

	/**
	 * @param visit the visit to set
	 */
	public void updateVisit() {
		this.visit += 1;
	}

	/**
	 * @return the move
	 */
	public Tuple<Integer, Integer> getMove() {
		return move;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * @return the children
	 */
	public List<Node> getChildren() {
		return children;
	}

	/**
	 * @return the untriedMoves
	 */
	public List<Tuple<Integer, Integer>> getUntriedMoves() {
		return untriedMoves;
	}
}
