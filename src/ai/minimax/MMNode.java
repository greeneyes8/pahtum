package ai.minimax;

import java.util.ArrayList;
import java.util.List;

import core.Board;
import core.Rules;

import util.Tuple;
/**
 * Classe che trova il min e il massimo di un nodo.
 * @author Mina
 *
 */
public class MMNode {
	private Tuple<Integer, Integer> move;
	private ArrayList<MMNode> children;
	private MMNode parent;
	private static double quality;
	private static String color;

	/**
	 * 
	 * @param parent, che è un nodo
	 * @param move, è di tipo tupla
	 * @param board, è un oggetto della classe Board
	 * @param color, è di tipo stringa
	 * @throws Exception cattura eccezioni
	 */
	
	public MMNode(MMNode parent, Tuple<Integer, Integer> move, Board board, String color) throws Exception {
		this.move = move;
		this.quality = -1;
		this.children = new ArrayList<MMNode>();
		String w = "w";
		String bs = "b";
		
		makeMovers ( move,  board,  color);
		
		this.parent = parent;
		color = (color.equals(w) ? bs : w);
		this.color = color;
		for(Tuple<Integer, Integer> item : board.getListValidMoves()) {
			Board copy_board = board.duplicate();
			String copy_color = (color.equals(w) ? w : bs);
			MMNode new_node = new MMNode(this, item, copy_board, copy_color);
			this.children.add(new_node);
		}
		if(this.children.size() == 0) {
			String[][] b = board.getState();
			for(int x = 0; x < 7; ++x) {
				for(int y = 0; y < 7; ++y) {
					System.out.print(b[x][y] + " ");
				}
				System.out.println();
			}

			result ( board);
			
			
		}

	}

	public List<MMNode> getChildren() {
		return this.children;
	}

	public MMNode getParent(){
		return this.parent;
	}

	public String getColor() { return this.color; }
	public double getQuality() { return this.quality; }
	public Tuple<Integer, Integer> getMove() { return this.move; }

	public Tuple<Integer, Integer> get_best_move() {
//		for(MMNode child : this.children) {
//			System.out.println(child.getMove().toString() + " " + child.getQuality());
//			System.out.println(">> " + child.getChildren().get(0).getChildren().get(0).getQuality());
//		}

		for(MMNode child : this.children) {
			if(child.getQuality() == 1) {
				System.out.println("move: " + child.getMove().toString());
				return child.getMove();
			}
		}
		for(MMNode child : this.children) {
			if(child.getQuality() == .5) {
				System.out.println("draw");
				return child.getMove();
			}
		}
		System.out.println("lost");
		return this.children.get(0).getMove();
	}

	/**
	 * 
	 * @param node
	 */
	public static void back_propagate(MMNode node) {
		int numberChild = node.getChildren().size();
		while(numberChild!= 0) {
			node = node.getChildren().get(0);
			numberChild = node.getChildren().size();
		}

		node = node.getParent();
		while(node != null) {
			if(node.getColor().equals(MiniMax.color)) {
				//MAX
				double maxTmpQty = 0;
				for(MMNode child : node.getChildren()) {
					if(child.getQuality() > maxTmpQty) {
						maxTmpQty = child.getQuality();
					}
				}
				node.quality = maxTmpQty;
			} else {
				//MINI
				double minTmpQty = 1;
				for(MMNode child : node.getChildren()) {
					if(child.getQuality() > minTmpQty) {
						minTmpQty = child.getQuality();
					}
				}
				node.quality = minTmpQty;
			}
			node = node.getParent();
			
		}
	}

	public static void update_tree(MMNode node) {
		List<MMNode> children = node.getChildren();
		if(children != null) {
			for(MMNode child : children) {
				if(child.getQuality() == -1) {
					update_tree(child);
				}
				System.out.println("+");
				if(child.getQuality() == .5) {
					node.quality = .5;
				} else {
					node.quality = child.getQuality() == 1 ? 0 : 1;
				}
			}

		}
	}
	
	public static void makeMovers (Tuple<Integer, Integer> move_mk, Board board_mk, String color_mk) throws Exception {
		
		if(move_mk != null) {
			board_mk.makeMove(move_mk, color_mk);
		}
	}
	
	public static void result (Board board) {
		
		String result = Rules.calculateScore(board);
		System.out.println(result);
		double value;
		String zero = "0";
		
		if(result.equals(zero)) {
			System.out.println(">> .5");
			value = .5;
		} else if(!result.equals(color)) {
			System.out.println(">> 1");
			value = 1;
		} else {
			System.out.println(">> 0");
			value = 0;
		}
		quality = value;
		System.out.println("===========================");
		
	}

}