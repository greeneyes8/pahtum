package ai.random;

import java.util.ArrayList;
import java.util.Random;

import util.Tuple;
import core.Board;

/**
 * Classe relativa ad AI
 * @author Mina
 *
 */

public class LuckyAI {
	private Board board;
	private int moveNumber, allMovesNumber;
	public LuckyAI(Board board, int moveNumber, int allMovesNumber) {
		this.board = board;
		this.moveNumber = moveNumber;
		this.allMovesNumber = allMovesNumber;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public Tuple<Integer, Integer> getMove() {
		String[][] b = board.getState();
		ArrayList<Tuple<Integer, Integer>> listOfMoves = new ArrayList<Tuple<Integer,Integer>>();
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				if("e".equals(b[x][y])) {
					listOfMoves.add(new Tuple<Integer, Integer>(x, y));
				}
			}
		}
		
		Random generator = new Random();
		return listOfMoves.get(generator.nextInt(listOfMoves.size())); 
	}
}
