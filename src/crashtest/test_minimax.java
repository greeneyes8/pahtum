package crashtest;

import ai.minimax.MiniMax;
import core.Board;

/**
 * Classe relativa al crashtest
 * @author Mina
 *
 */

public class test_minimax {
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String[] args) throws Exception {
		int valueboard1 = 3;
		
		int valueboard3 = 0;
		Board b = new Board(valueboard1, false, valueboard3);
		MiniMax mm = new MiniMax("b", b);
		System.out.println(mm.getMove().toString());
	}
}
