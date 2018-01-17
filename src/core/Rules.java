package core;

/**
 * Classe che implementa le regole
 * @author Mina
 *
 */

public class Rules {
	
	/**
	 * Calculate score for each player.
	 * @param board Board completely covered with stones (a terminal state).
	 * @return Information which color won the game: "w" for white, "b" for 
	 * black, "0" for a draw.
	 */
	public static String calculateScore(Board board) {
		String[][] b = board.getState();
		int scoreWhite = 0, scoreBlack = 0;

		// Check horizontally.
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				int whiteLine = 1, blackLine = 1;
				try {
					stringA (b,  x,  y,  whiteLine,  blackLine);
				} catch(Exception e) { 
					System.out.println("There's an error"); 
				}
				
				whiteSwitch ( whiteLine ,  scoreWhite);
				blackSwitch ( blackLine ,  scoreBlack);		
			}
			
		}
		
		// Reverse matrix.
		reverseMatrix ( b);
		
		// Check horizontally on reversed matrix (=> vertically).
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				int whiteLine = 1, blackLine = 1;
				try {
					stringA (b,  x,  y,  whiteLine,  blackLine);
					} catch(Exception e) { 
						System.out.println("There's an error"); 
					}
				
				whiteSwitch ( whiteLine ,  scoreWhite);
				blackSwitch ( blackLine ,  scoreBlack);
							
			}
			
		}
		
		String stringScore = scoreWhiteBlack ( scoreWhite,  scoreBlack ) ;
		return stringScore;
	}

	public static void whiteLine (boolean valueW, int white_Line, 
			int y, String [][] b, int x ) {
		
		while(valueW) {
			white_Line++;
			y++;
			valueW = "w".equals(b[x][y+1]);
		}
		
	}
	
	public static void blackLine (boolean valueB, int bl_blackLine, 
			int y, String [][] b, int x ) {
		
		while(valueB) {
			bl_blackLine++;
			y++;
			valueB = "b".equals(b[x][y+1]);
		}
		
	}
	
	public static void whiteSwitch (int sw_whiteLine , int sw_scoreWhite) {
		switch(sw_whiteLine) {
		case 3:
			sw_scoreWhite += 3;
			break;
		case 4:
			sw_scoreWhite += 10;
			break;
		case 5:
			sw_scoreWhite += 25;
			break;
		case 6:
			sw_scoreWhite += 56;
			break;
		case 7:
			sw_scoreWhite += 119;
			break;
		default:
			break;
		}
		
	}
	
	public static void blackSwitch (int bs_blackLine , int bs_scoreBlack) {
		switch(bs_blackLine) {
		case 3:
			bs_scoreBlack += 3;
			break;
		case 4:
			bs_scoreBlack += 10;
			break;
		case 5:
			bs_scoreBlack += 25;
			break;
		case 6:
			bs_scoreBlack += 56;
			break;
		case 7:
			bs_scoreBlack += 119;
			break;
		default:
			break;
		}	
		
	}
	
	public static void reverseMatrix (String [][] b) {
		
		String tmp;
		for(int x = 0; x < 7; ++x) {
			for(int y = x; y < 7; ++y) {
				tmp = b[x][y];
				b[x][y] = b[y][x];
				b[y][x] = tmp; 
			}
		}
		
	}
	
	public static void stringA (String [][] b, int x, int y, int str_whiteLine, int str_blackLine) {
		
		if("w".equals(b[x][y])) {
			try {
				boolean valueW = "w".equals(b[x][y+1]);
				whiteLine ( valueW,  str_whiteLine, 
						 y, b,  x );
			}catch(Exception e) {
				System.out.println("There's an error"); 
			}
		} else if("b".equals(b[x][y])){
			try {
				boolean value_ = "b".equals(b[x][y+1]);
				blackLine ( value_,  str_blackLine, 
						 y, b,  x );
			} catch(Exception e) { 
				System.out.println("There's an error"); 
			} 
		}
		
	}
	
	public static String scoreWhiteBlack (int scoreWhite, int scoreBlack ) {
		
		if(scoreWhite == scoreBlack) {
			return "0";
		} else if(scoreWhite > scoreBlack) {
			return "w";
		} else {
			return "b";
		}
	}
	
}
