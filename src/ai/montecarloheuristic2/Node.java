package ai.montecarloheuristic2;

import java.util.ArrayList;
import java.util.List;

import core.Board;

import util.Tuple;

/**
 * Classe che individua i nodi
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
		int valuenodeh2 = 3;
		if(mcColor.equals(color)) {
			//Moves for MC => use heuristic.
//			System.out.println("First time heuristic");
			this.untriedMoves = board.heuristic_bestX_moves(mcColor, valuenodeh2);
//			System.out.println(untriedMoves);
//			InputStreamReader isr =  new InputStreamReader(System.in);
//			BufferedReader br = new BufferedReader(isr);
//			try {
//				String srt = br.readLine();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
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
		Tuple<Integer, Integer> move = this.untriedMoves.get(0);
		this.untriedMoves.remove(0);
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
	public void updateValue(double v) {
		this.value += v;
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
