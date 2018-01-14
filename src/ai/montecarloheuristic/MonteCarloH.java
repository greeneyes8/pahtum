package ai.montecarloheuristic;

import java.util.List;
import java.util.Random;

import util.Tuple;
import core.Board;
import core.Rules;

/**
 * Tra nodi e dati estrapola infomazioni dalle query
 * @author Mina
 *
 */

public class MonteCarloH {
	private Board board;
	private String color;
	private Root root;
	private int allMovesNumber;
	private double c;
	
//	public void test() throws Exception {
//		defaultPolicy(this.root.getRoot(), this.board);
//		board.draw();
//	}
	
	/**
	 * 
	 * @param board
	 * @param color
	 */
	public MonteCarloH(Board board, String color, int moveNumber, 
			int allMovesNumber) {
		this.board = board;
		this.color = color;
		this.allMovesNumber = allMovesNumber;
		this.root = new Root(new Node(null, null, color, board, moveNumber, 
				this.color));
		int twoh = 2;
		this.c = 1 / Math.sqrt(twoh);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	public Tuple<Integer, Integer> uct(int n) throws Exception {
		while(n > 0) {
			System.out.println(n);
			Board tempBoard = this.board.duplicate();
			Node node = treePolicy(root.getRoot(), tempBoard);
			String delta = defaultPolicy(node, tempBoard);
			back_up(node, delta);
			--n;
		}
		return bestChild(root.getRoot(), 0).getMove();
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 * @throws Exception 
	 */
	private Node treePolicy(Node treePolicynode, Board treePolicyboard) throws Exception {
		int numberNode = treePolicynode.getMoveNumber() ;
		while(numberNode <= this.allMovesNumber) {
			if(treePolicynode.getUntriedMoves().size() != 0) {
				Node newNode =  treePolicynode.expand(treePolicyboard, this.color);
				numberNode = treePolicynode.getMoveNumber() ;
				return newNode;
			} else {
				String color = treePolicynode.getColor();
				try {
					treePolicynode = bestChild(treePolicynode, this.c);
				} catch(Exception e) {
					//node is a terminal state.
					return treePolicynode;
				}
				treePolicyboard.makeMove(treePolicynode.getMove(), color);
				numberNode = treePolicynode.getMoveNumber() ;
			}
		}
		return treePolicynode;
	}
	
	/**
	 * This method conducts self-played game with random moves until the board
	 * is completely fulfilled. 
	 * @param node Node from which simulation takes place.
	 * @param board Board that serves as a starting point.
	 * @return winning side: "w" for white, "b" for black, "0" for draw.
	 * @throws Exception ???
	 */
	private String defaultPolicy(Node node_policy, Board board_policy) throws Exception {
		Random generator = new Random();
		String color = node_policy.getColor();
		int moveNumber = node_policy.getMoveNumber();
		String w = "w";
		int threeh = 3;
		while(moveNumber < this.allMovesNumber) {
			List<Tuple<Integer, Integer>> listValidMoves;
			if(color.equals(this.color)) {
				listValidMoves = board_policy.heuristic_bestX_moves(color, threeh);
			} else {
				listValidMoves = board_policy.getListValidMoves();
			}
			board_policy.makeMove(listValidMoves.get(generator.nextInt(listValidMoves.size())), color);
			if(color.equals(w)) {
				color = "b";
			} else {
				color = "w";
			}
			++moveNumber;
		}
		return Rules.calculateScore(board_policy);
	}
	
	/**
	 * Propagate information whether MC won or not.
	 * @param node
	 * @param delta
	 */
	private void back_up(Node node, String delta) {
		double value;
		String zero = "0";
		
		if(delta.equals(zero)) {
			value = .5;
		} else if(delta.equals(node.getColor())) {
			value = 0;
		} else {
			value = 1;
		}
		
		while(node != null) {
			node.updateValue(value);
			node.updateVisit();
			
			if(value != .5) {
				value = (value + 1) % 2;
			}
			
			node = node.getParent();
		}
	}

	
	/**
	 * Select the best child based on evaluation function (UCT).
	 */
	private Node bestChild(Node no_de_bc, double c_bc) {
		Node bestChild = null;
		double tempScore = -1;
		for(Node child: no_de_bc.getChildren()) {
			double score = (child.getValue() / child.getVisit()) + 
					(c_bc * Math.sqrt((2 * Math.log(no_de_bc.getVisit())) / 
							(child.getVisit())));
			if(score >= tempScore) {
				bestChild = child;
				tempScore = score;
			}
		}
		return bestChild;
	}
}