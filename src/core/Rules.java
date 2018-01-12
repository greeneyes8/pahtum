package core;

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
				} catch(Exception e) { }
				
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
					} catch(Exception e) { }
				
				whiteSwitch ( whiteLine ,  scoreWhite);
				blackSwitch ( blackLine ,  scoreBlack);
							
			}
			
		}
		
		String stringScore = scoreWhiteBlack ( scoreWhite,  scoreBlack ) ;
		return stringScore;
	}

	public static void whiteLine (boolean valueW, int whiteLine, 
			int y, String [][] b, int x ) {
		
		while(valueW) {
			whiteLine++;
			y++;
			valueW = b[x][y+1].equals("w");
		}
		
	}
	
	public static void blackLine (boolean valueB, int blackLine, 
			int y, String [][] b, int x ) {
		
		while(valueB) {
			blackLine++;
			y++;
			valueB = b[x][y+1].equals("b");
		}
		
	}
	
	public static void whiteSwitch (int whiteLine , int scoreWhite) {
		switch(whiteLine) {
		case 3:
			scoreWhite += 3;
			break;
		case 4:
			scoreWhite += 10;
			break;
		case 5:
			scoreWhite += 25;
			break;
		case 6:
			scoreWhite += 56;
			break;
		case 7:
			scoreWhite += 119;
			break;
		default:
			break;
		}
		
	}
	
	public static void blackSwitch (int blackLine , int scoreBlack) {
		switch(blackLine) {
		case 3:
			scoreBlack += 3;
			break;
		case 4:
			scoreBlack += 10;
			break;
		case 5:
			scoreBlack += 25;
			break;
		case 6:
			scoreBlack += 56;
			break;
		case 7:
			scoreBlack += 119;
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
	
	public static void stringA (String [][] b, int x, int y, int whiteLine, int blackLine) {
		
		if(b[x][y].equals("w")) {
			try {
				boolean valueW = b[x][y+1].equals("w");
				whiteLine ( valueW,  whiteLine, 
						 y, b,  x );
			}catch(Exception e) { }
		} else if(b[x][y].equals("b")){
			try {
				boolean value_ = b[x][y+1].equals("b");
				blackLine ( value_,  blackLine, 
						 y, b,  x );
			} catch(Exception e) { } 
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
