package crashtest;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;

import util.Tuple;

//import ai.charles2.Charles_2;
import ai.charles2.Charles_2;
import ai.mch5boltzmann.MonteCarloH5Boltzmann;

//import ai.random.LuckyAI;

import core.Board;
import core.Player;
import core.Rules;

/**
 * Classe relativa al test di boltzman
 * @author Mina
 *
 */

public class test_boltzman {

	private static Board[] boardCollectionTest1s;

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//Statistical variables.
		int e1TotalWins = 0,
		totalDraws = 0,
		e1TotalLoses = 0,
		e1WinAsPlayer1 = 0,
		e1DrawAsPlayer1 = 0,
		e1LoseAsPlayer1 = 0,
		e1WinAsPlayer2 = 0,
		e1DrawAsPlayer2 = 0,
		e1LoseAsPlayer2 = 0,
		e2TotalWins = 0,
		e2TotalLoses = 0,
		e2WinAsPlayer1 = 0,
		e2DrawAsPlayer1 = 0,
		e2LoseAsPlayer1 = 0,
		e2WinAsPlayer2 = 0,
		e2DrawAsPlayer2 = 0,
		e2LoseAsPlayer2 = 0;
		int value_1000= 1000;
		/***********************************************************************
		 * Test #1: (1050,000 roll-outs) MCTS + H(7) v Charles_2 3-point board.
		 **********************************************************************/

		//Board that is used in games.
		Board boardTest1 = null;

		//Board that keeps copy of initial position, used to quickly reset the 
		//board before new game take place.
		Board initialPositionTest1 = null;

		boardCollectionTest1s = null;

		//Index of player that is entitled to make a move.
		int currentIndexTest1 = 0;

		//Number of all moves that was made during the game.
		int numberOfMoveTest1 = 0;

		//Players participating in the test case.
		Player[] playersTest1 =  {
				new Player("Boltzmann1k", "Boltzmann1k", "w", value_1000),
				new Player("Charles_2", "Charles_2", "b", 0)
		};

		//Number of total moves. It is used to check whether the game is in 
		//terminate state or not (the game finishes when there is no empty 
		//fields in the board).
		int totalNumberOfMovesTest1 = 46;
		
		//Load board.
		String nameBoard = "50_boards_3.sav";
		//Load board.
		loadBoard ( boardCollectionTest1s, nameBoard);
		
		 
		//The beginning and the end of the test.
		long startTime = 0, endTime = 0;

		//Report when games commenced.
		startTime = System.currentTimeMillis();

		//Declare buffers.
			BufferedWriter outputTest1 = null;
			DeclareBuffers (extracted(outputTest1));
	
		MonteCarloH5Boltzmann mc = new MonteCarloH5Boltzmann(
				extracted(boardTest1).duplicate(), 
				playersTest1[currentIndexTest1].getColor(), 
				numberOfMoveTest1, 
				totalNumberOfMovesTest1);
		Charles_2 charles = new Charles_2(playersTest1[currentIndexTest1].getColor(), extracted(boardTest1));

		//Boards are OK. Proceed to testing.
		for(int testIndex = 1; testIndex <= 100; ++testIndex) {
			System.out.println("Test1: " + testIndex + " / 100");
			//Reset settings.
			currentIndexTest1 = 0;
			numberOfMoveTest1 = 0;

			//Swap players.
			Player tmp = playersTest1[0];
			playersTest1[0] = playersTest1[1];
			playersTest1[1] = tmp;

			//Reset the board to an initial state. When index is odd generate a 
			//new random board.
			if(testIndex % 2 != 0) {
				//Load a new board.
				boardTest1 = boardCollectionTest1s[(Integer) testIndex/2];
				initialPositionTest1 = extracted(boardTest1).duplicate();
			} else {
				//Reset the board.
				boardTest1 = extracted(initialPositionTest1).duplicate();
			}


			//Run a single game.
			RunASingleGame ( numberOfMoveTest1,  totalNumberOfMovesTest1
					,  currentIndexTest1,  playersTest1,
					 extracted(boardTest1),  mc,  charles);
		 //end of single game.

			String gameOutcome = Rules.calculateScore(extracted(boardTest1));
			String zero= "0";
			
			extracted(outputTest1).append("Match #" + testIndex);
			extracted(outputTest1).newLine();
			extracted(outputTest1).append("Player 1: " + playersTest1[0].getName() + 
					" Player 2: " + playersTest1[1].getName());
			extracted(outputTest1).newLine();

			//Append the result to the text file and update counters..
			if(gameOutcome.equals(zero)) {
				//The game was a draw.
				++totalDraws;
				//Append information to the file.
				extracted(outputTest1).append("Result: draw");
				extracted(outputTest1).newLine();
				extracted(outputTest1).close();

				//Update statistics.
				boolean valuePlayersTest1 = "Charles_2".equals(playersTest1[0].getName());
				updateStatisticsFirst ( valuePlayersTest1,  e1DrawAsPlayer1, 
						 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

			} else {
				//One side wins the game.
				
				if(gameOutcome.equals(playersTest1[0].getColor())) {
					//Player #1, whoever it is, wins the game.

					//Add note about the winner to the file.
					extracted(outputTest1).append("Result: " + playersTest1[0].getName() + " wins");

					//Update statistics.
					boolean valuePlayersTest1 = "Charles_2".equals(playersTest1[0].getName());
					updateStatisticsSecond ( valuePlayersTest1,  e1TotalWins,
							 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
							 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);

				} else {
					//Player #2, whoever it is, wins the game.

					//Add note about the winner to the file.
					extracted(outputTest1).append("Result: " + playersTest1[1].getName() + " wins");

					//Update statistics.
					boolean valuePlayersTest1 = "Charles_2".equals(playersTest1[1].getName());
					updateStatisticsSecond ( valuePlayersTest1,  e1TotalWins,
							 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
							 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);
				}
				extracted(outputTest1).newLine();
				extracted(outputTest1).close();
			}			
		} //End of the test case. (for)

		//Report when games ended.
		endTime = System.currentTimeMillis();	
		//Append total outcome of the test case to the file.
		BufferedWriter output1Test1 = null;
		DeclareBuffers ( outputTest1);
		
		extracted(output1Test1).append("========================================");
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("*Summary (1k/100) 3-point board*");
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Draw occurred: " + totalDraws);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Boltzmann1k total wins: " + e2TotalWins);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Charles_2 total wins: " + e1TotalWins);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Play time: " + (endTime - startTime)/value_1000 + " seconds.");
		extracted(output1Test1).newLine();

		//Write statistics for MCTS.
		extracted(output1Test1).append("Boltzmann1k wins as player #1 : " + e2WinAsPlayer1);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Boltzmann1k wins as player #2 : " + e2WinAsPlayer2);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Boltzmann1k draws as player #1 : " + e2DrawAsPlayer1);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Boltzmann1k draws as player #2 : " + e2DrawAsPlayer2);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Boltzmann1k loses as player #1 : " + e2LoseAsPlayer1);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Boltzmann1k loses as player #2 : " + e2LoseAsPlayer2);
		extracted(output1Test1).newLine();

		//Write statistics for Random AI.
		extracted(output1Test1).append("Charles_2 wins as player #1 : " + e1WinAsPlayer1);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Charles_2 wins as player #2 : " + e1WinAsPlayer2);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Charles_2 draws as player #1 : " + e1DrawAsPlayer1);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Charles_2 draws as player #2 : " + e1DrawAsPlayer2);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Charles_2 loses as player #1 : " + e1LoseAsPlayer1);
		extracted(output1Test1).newLine();
		extracted(output1Test1).append("Charles_2 loses as player #2 : " + e1LoseAsPlayer2);
		extracted(output1Test1).newLine();

		extracted(output1Test1).append("========================================");
		extracted(output1Test1).close();
		
		}

//		/***********************************************************************
//		 * Test #2: (100,000 roll-outs) MCTS + H(7) v Charles_2.
//		 **********************************************************************/
//		e1TotalWins = 0;
//		totalDraws = 0;
//		e1TotalLoses = 0;
//		e1WinAsPlayer1 = 0;
//		e1DrawAsPlayer1 = 0;
//		e1LoseAsPlayer1 = 0;
//		e1WinAsPlayer2 = 0;
//		e1DrawAsPlayer2 = 0;
//		e1LoseAsPlayer2 = 0;
//		e2TotalWins = 0;
//		e2TotalLoses = 0;
//		e2WinAsPlayer1 = 0;
//		e2DrawAsPlayer1 = 0;
//		e2LoseAsPlayer1 = 0;
//		e2WinAsPlayer2 = 0;
//		e2DrawAsPlayer2 = 0;
//		e2LoseAsPlayer2 = 0;
//
//		//Board that is used in games.
//		Board boardTest2 = null;
//
//		//Board that keeps copy of initial position, used to quickly reset the 
//		//board before new game take place.
//		Board initialPositionTest2 = null;
//
//		//Array of all boards that are used in the test case.
//		Board[] boardCollectionTest2 = null;
//
//		//Index of player that is entitled to make a move.
//		int currentIndexTest2 = 0;
//
//		//Number of all moves that was made during the game.
//		int numberOfMoveTest2 = 0;
//
//		//Players participating in the test case.
//		Player[] playersTest2 =  {
//				new Player("MCTS + H(7)", "MCTS + H(7)", "w", 100000),
//				new Player("Charles_2", "Charles_2", "b", 0)
//		};
//
//		//Number of total moves. It is used to check whether the game is in 
//		//terminate state or not (the game finishes when there is no empty 
//		//fields in the board).
//		int totalNumberOfMovesTest2 = 38;
//
//		//Load board.
//		try {
//			FileInputStream fisTest2 = new FileInputStream("50_boards_3.sav");
//			ObjectInputStream oisTest2 = new ObjectInputStream(fisTest2);
//			boardCollectionTest2 = (Board[]) oisTest2.readObject();
//		} catch(Exception e) {
//			System.err.println("Error" + e.getMessage());
//		}
//
//		//The beginning and the end of the test.
//		long startTimeTest2 = 0, endTimeTest2 = 0;
//
//		//Report when games commenced.
//		startTimeTest2 = System.currentTimeMillis();
//
//		//Boards are OK. Proceed to testing.
//		for(int testIndex = 1; testIndex <= 100; ++testIndex) {
//			System.out.println("Test2: " + testIndex + " / 100");
//			//Reset settings.
//			currentIndexTest2 = 0;
//			numberOfMoveTest2 = 0;
//			
//			//Swap players.
//			Player tmp = playersTest2[0];
//			playersTest2[0] = playersTest2[1];
//			playersTest2[1] = tmp;
//
//			//Reset the board to an initial state. When index is odd generate a 
//			//new random board.
//			if(testIndex % 2 == 1) {
//				//Load a new board.
//				boardTest2 = boardCollectionTest2[(Integer) testIndex/2];
//				initialPositionTest2 = boardTest2.duplicate();
//			} else {
//				//Reset the board.
//				boardTest2 = initialPositionTest2.duplicate();
//			}
//
//
//			//Run a single game.
//			while(numberOfMoveTest2 < totalNumberOfMovesTest2) {
//				if(playersTest2[currentIndexTest2].getType().equals("Charles_2")) {
//					//MCTS (UCT) to play.
//					Tuple<Integer, Integer> move;
//					//Pure Monte-Carlo will select move.
//					Charles_2 charles = new Charles_2(playersTest2[currentIndexTest2].getColor(), boardTest2);
//					move = charles.getMove();
//
//
//					boardTest2.makeMove(move, playersTest2[currentIndexTest2].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest2;
//
//					//Adjust index of current player.
//					currentIndexTest2 = (currentIndexTest2 + 1) % 2;
//				} else if(playersTest2[currentIndexTest2].getType().equals("MCTS + H(7)")) {
//					//MCTS (UCT) to play.
//					Tuple<Integer, Integer> move;
//					//Pure Monte-Carlo will select move.
//					MonteCarloH7 mc = new MonteCarloH7(
//							boardTest2.duplicate(), 
//							playersTest2[currentIndexTest2].getColor(), 
//							numberOfMoveTest2, 
//							totalNumberOfMovesTest2);
//					move = mc.uct(playersTest2[currentIndexTest2].
//							getSimulationNumber());
//
//
//					boardTest2.makeMove(move, playersTest2[currentIndexTest2].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest2;
//
//					//Adjust index of current player.
//					currentIndexTest2 = (currentIndexTest2 + 1) % 2;
//				}
//			} //end of single game.
//
//			String gameOutcome = Rules.calculateScore2(boardTest2);
//			BufferedWriter outputTest2 = new BufferedWriter(
//					new FileWriter("results_100k_11b_MCTS+H(7)vCharles_2.txt", true));
//			outputTest2.append("Match #" + testIndex);
//			outputTest2.newLine();
//			outputTest2.append("Player 1: " + playersTest2[0].getName() + 
//					" Player 2: " + playersTest2[1].getName());
//			outputTest2.newLine();
//
//			//Append the result to the text file and update counters..
//			if(gameOutcome.equals("0")) {
//				//The game was a draw.
//				++totalDraws;
//				//Append information to the file.
//				outputTest2.append("Result: draw");
//				outputTest2.newLine();
//				outputTest2.close();
//
//				//Update statistics.
//				if(playersTest2[0].getName().equals("Charles_2")) {
//					e1DrawAsPlayer1++;
//					e2DrawAsPlayer2++;
//				} else {
//					e1DrawAsPlayer2++;
//					e2DrawAsPlayer1++;
//				}
//
//			} else {
//				//One side wins the game.
//				if(gameOutcome.equals(playersTest2[0].getColor())) {
//					//Player #1, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest2.append("Result: " + playersTest2[0].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest2[0].getName().equals("Charles_2")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer1++;
//						e2LoseAsPlayer2++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer1++;
//						e1LoseAsPlayer2++;
//					}
//
//				} else {
//					//Player #2, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest2.append("Result: " + playersTest2[1].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest2[1].getName().equals("Charles_2")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer2++;
//						e2LoseAsPlayer1++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer2++;
//						e1LoseAsPlayer1++;
//					}
//				}
//				outputTest2.newLine();
//				outputTest2.close();
//			}			
//		} //End of the test case. (for)
//
//		//Report when games ended.
//		endTimeTest2 = System.currentTimeMillis();
//
//		//Append total outcome of the test case to the file.
//		BufferedWriter output1Test2 = new BufferedWriter(
//				new FileWriter("results_100k_11b_MCTS+H(7)vCharles_2.txt", true));
//		output1Test2.append("========================================");
//		output1Test2.newLine();
//		output1Test2.append("*Summary (100k) 11-point board*");
//		output1Test2.newLine();
//		output1Test2.append("Draw occurred: " + totalDraws);
//		output1Test2.newLine();
//		output1Test2.append("MCTS + H(7) total wins: " + e2TotalWins);
//		output1Test2.newLine();
//		output1Test2.append("Charles_2 total wins: " + e1TotalWins);
//		output1Test2.newLine();
//		output1Test2.append("Play time: " + (endTimeTest2 - startTimeTest2)/1000 + " seconds.");
//		output1Test2.newLine();
//
//		//Write statistics for MCTS.
//		output1Test2.append("MCTS + H(7) wins as player #1 : " + e2WinAsPlayer1);
//		output1Test2.newLine();
//		output1Test2.append("MCTS + H(7) wins as player #2 : " + e2WinAsPlayer2);
//		output1Test2.newLine();
//		output1Test2.append("MCTS + H(7) draws as player #1 : " + e2DrawAsPlayer1);
//		output1Test2.newLine();
//		output1Test2.append("MCTS + H(7) draws as player #2 : " + e2DrawAsPlayer2);
//		output1Test2.newLine();
//		output1Test2.append("MCTS + H(7) loses as player #1 : " + e2LoseAsPlayer1);
//		output1Test2.newLine();
//		output1Test2.append("MCTS + H(7) loses as player #2 : " + e2LoseAsPlayer2);
//		output1Test2.newLine();
//
//		//Write statistics for Random AI.
//		output1Test2.append("Charles_2 wins as player #1 : " + e1WinAsPlayer1);
//		output1Test2.newLine();
//		output1Test2.append("Charles_2 wins as player #2 : " + e1WinAsPlayer2);
//		output1Test2.newLine();
//		output1Test2.append("Charles_2 draws as player #1 : " + e1DrawAsPlayer1);
//		output1Test2.newLine();
//		output1Test2.append("Charles_2 draws as player #2 : " + e1DrawAsPlayer2);
//		output1Test2.newLine();
//		output1Test2.append("Charles_2 loses as player #1 : " + e1LoseAsPlayer1);
//		output1Test2.newLine();
//		output1Test2.append("Charles_2 loses as player #2 : " + e1LoseAsPlayer2);
//		output1Test2.newLine();
//
//		output1Test2.append("========================================");
//		output1Test2.close();
//
//		/***********************************************************************
//		 * Test #3: (50,000 roll-outs) MCTS (UCT) v MCTS + H(5+5).
//		 **********************************************************************/
//		e1TotalWins = 0;
//		totalDraws = 0;
//		e1TotalLoses = 0;
//		e1WinAsPlayer1 = 0;
//		e1DrawAsPlayer1 = 0;
//		e1LoseAsPlayer1 = 0;
//		e1WinAsPlayer2 = 0;
//		e1DrawAsPlayer2 = 0;
//		e1LoseAsPlayer2 = 0;
//		e2TotalWins = 0;
//		e2TotalLoses = 0;
//		e2WinAsPlayer1 = 0;
//		e2DrawAsPlayer1 = 0;
//		e2LoseAsPlayer1 = 0;
//		e2WinAsPlayer2 = 0;
//		e2DrawAsPlayer2 = 0;
//		e2LoseAsPlayer2 = 0;
//
//		//Board that is used in games.
//		Board boardTest3 = null;
//
//		//Board that keeps copy of initial position, used to quickly reset the 
//		//board before new game take place.
//		Board initialPositionTest3 = null;
//
//		//Array of all boards that are used in the test case.
//		Board[] boardCollectionTest3 = null;
//
//		//Index of player that is entitled to make a move.
//		int currentIndexTest3 = 0;
//
//		//Number of all moves that was made during the game.
//		int numberOfMoveTest3 = 0;
//
//		//Players participating in the test case.
//		Player[] playersTest3 =  {
//				new Player("MCTS (UCT)", "MCTS (UCT)", "w", 50000),
//				new Player("MCTS + H(5+5)", "MCTS + H(5+5)", "b", 50000)
//		};
//
//		//Number of total moves. It is used to check whether the game is in 
//		//terminate state or not (the game finishes when there is no empty 
//		//fields in the board).
//		int totalNumberOfMovesTest3 = 38;
//
//		//Load board.
//		try {
//			FileInputStream fisTest3 = new FileInputStream("50_boards_11.sav");
//			ObjectInputStream oisTest3 = new ObjectInputStream(fisTest3);
//			boardCollectionTest3 = (Board[]) oisTest3.readObject();
//		} catch(Exception e) {
//			System.err.println("Error" + e.getMessage());
//		}
//
//		//The beginning and the end of the test.
//		long startTimeTest3 = 0, endTimeTest3 = 0;
//
//		//Report when games commenced.
//		startTimeTest3 = System.currentTimeMillis();
//
//		//Boards are OK. Proceed to testing.
//		for(int testIndex = 1; testIndex <= 100; ++testIndex) {
//			System.out.println("Test3: " + testIndex + " / 100");
//			//Reset settings.
//			currentIndexTest3 = 0;
//			numberOfMoveTest3 = 0;
//
//			//Swap players.
//			Player tmp = playersTest3[0];
//			playersTest3[0] = playersTest3[1];
//			playersTest3[1] = tmp;
//
//			//Reset the board to an initial state. When index is odd generate a 
//			//new random board.
//			if(testIndex % 2 == 1) {
//				//Load a new board.
//				boardTest3 = boardCollectionTest3[(Integer) testIndex/2];
//				initialPositionTest3 = boardTest3.duplicate();
//			} else {
//				//Reset the board.
//				boardTest3 = initialPositionTest3.duplicate();
//			}
//
//
//			//Run a single game.
//			while(numberOfMoveTest3 < totalNumberOfMovesTest3) {
//				if(playersTest3[currentIndexTest3].getType().equals("MCTS + H(5+5)")) {
//					//MCTS + H(5) to play.
//					Tuple<Integer, Integer> move;
//					//Pure Monte-Carlo + H(5) will select new move.
//					MonteCarloH55 mc = new MonteCarloH55(
//							boardTest3.duplicate(), 
//							playersTest3[currentIndexTest3].getColor(), 
//							numberOfMoveTest3, 
//							totalNumberOfMovesTest3);
//					move = mc.uct(playersTest3[currentIndexTest3].
//							getSimulationNumber());
//
//
//					boardTest3.makeMove(move, playersTest3[currentIndexTest3].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest3;
//
//					//Adjust index of current player.
//					currentIndexTest3 = (currentIndexTest3 + 1) % 2;
//				} else if(playersTest3[currentIndexTest3].getType().equals("MCTS (UCT)")) {
//					//MCTS (UCT) to play.
//					Tuple<Integer, Integer> move;
//
//					//Pure Monte-Carlo will select move.
//					MonteCarlo mc = new MonteCarlo(
//							boardTest3.duplicate(), 
//							playersTest3[currentIndexTest3].getColor(), 
//							numberOfMoveTest3, 
//							totalNumberOfMovesTest3);
//
//					move = mc.uct(playersTest3[currentIndexTest3].
//							getSimulationNumber());
//
//
//					boardTest3.makeMove(move, playersTest3[currentIndexTest3].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest3;
//
//					//Adjust index of current player.
//					currentIndexTest3 = (currentIndexTest3 + 1) % 2;
//				}
//			} //end of single game.
//
//			String gameOutcome = Rules.calculateScore2(boardTest3);
//			BufferedWriter outputTest3 = new BufferedWriter(
//					new FileWriter("results_50k_11b_MCTS(UCT)vMCTS+H(5+5).txt", true));
//			outputTest3.append("Match #" + testIndex);
//			outputTest3.newLine();
//			outputTest3.append("Player 1: " + playersTest3[0].getName() + 
//					" Player 2: " + playersTest3[1].getName());
//			outputTest3.newLine();
//
//			//Append the result to the text file and update counters..
//			if(gameOutcome.equals("0")) {
//				//The game was a draw.
//				++totalDraws;
//				//Append information to the file.
//				outputTest3.append("Result: draw");
//				outputTest3.newLine();
//				outputTest3.close();
//
//				//Update statistics.
//				if(playersTest3[0].getName().equals("MCTS + H(5+5)")) {
//					e1DrawAsPlayer1++;
//					e2DrawAsPlayer2++;
//				} else {
//					e1DrawAsPlayer2++;
//					e2DrawAsPlayer1++;
//				}
//
//			} else {
//				//One side wins the game.
//				if(gameOutcome.equals(playersTest3[0].getColor())) {
//					//Player #1, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest3.append("Result: " + playersTest3[0].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest3[0].getName().equals("MCTS + H(5+5)")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer1++;
//						e2LoseAsPlayer2++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer1++;
//						e1LoseAsPlayer2++;
//					}
//
//				} else {
//					//Player #2, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest3.append("Result: " + playersTest3[1].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest3[1].getName().equals("MCTS + H(5+5)")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer2++;
//						e2LoseAsPlayer1++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer2++;
//						e1LoseAsPlayer1++;
//					}
//				}
//				outputTest3.newLine();
//				outputTest3.close();
//			}			
//		} //End of the test case. (for)
//
//		//Report when games ended.
//		endTimeTest3 = System.currentTimeMillis();
//
//		//Append total outcome of the test case to the file.
//		BufferedWriter output1Test3 = new BufferedWriter(
//				new FileWriter("results_50k_11b_MCTS(UCT)vMCTS+H(5+5).txt", true));
//		output1Test3.append("========================================");
//		output1Test3.newLine();
//		output1Test3.append("*Summary (50k)*");
//		output1Test3.newLine();
//		output1Test3.append("Draw occurred: " + totalDraws);
//		output1Test3.newLine();
//		output1Test3.append("MCTS (UCT) total wins: " + e2TotalWins);
//		output1Test3.newLine();
//		output1Test3.append("MCTS + H(5+5) total wins: " + e1TotalWins);
//		output1Test3.newLine();
//		output1Test3.append("Play time: " + (endTimeTest3 - startTimeTest3)/1000 + " seconds.");
//		output1Test3.newLine();
//
//		//Write statistics for MCTS.
//		output1Test3.append("MCTS (UCT) wins as player #1 : " + e2WinAsPlayer1);
//		output1Test3.newLine();
//		output1Test3.append("MCTS (UCT) wins as player #2 : " + e2WinAsPlayer2);
//		output1Test3.newLine();
//		output1Test3.append("MCTS (UCT) draws as player #1 : " + e2DrawAsPlayer1);
//		output1Test3.newLine();
//		output1Test3.append("MCTS (UCT) draws as player #2 : " + e2DrawAsPlayer2);
//		output1Test3.newLine();
//		output1Test3.append("MCTS (UCT) loses as player #1 : " + e2LoseAsPlayer1);
//		output1Test3.newLine();
//		output1Test3.append("MCTS (UCT) loses as player #2 : " + e2LoseAsPlayer2);
//		output1Test3.newLine();
//
//		//Write statistics for Random AI.
//		output1Test3.append("MCTS + H(5+5) wins as player #1 : " + e1WinAsPlayer1);
//		output1Test3.newLine();
//		output1Test3.append("MCTS + H(5+5) wins as player #2 : " + e1WinAsPlayer2);
//		output1Test3.newLine();
//		output1Test3.append("MCTS + H(5+5) draws as player #1 : " + e1DrawAsPlayer1);
//		output1Test3.newLine();
//		output1Test3.append("MCTS + H(5+5) draws as player #2 : " + e1DrawAsPlayer2);
//		output1Test3.newLine();
//		output1Test3.append("MCTS + H(5+5) loses as player #1 : " + e1LoseAsPlayer1);
//		output1Test3.newLine();
//		output1Test3.append("MCTS + H(5+5) loses as player #2 : " + e1LoseAsPlayer2);
//		output1Test3.newLine();
//
//		output1Test3.append("========================================");
//		output1Test3.close();
//
//		/***********************************************************************
//		 * Test #4: (50,000 roll-outs) MCTS + H(7) v MCTS + H(10).
//		 **********************************************************************/
//		e1TotalWins = 0;
//		totalDraws = 0;
//		e1TotalLoses = 0;
//		e1WinAsPlayer1 = 0;
//		e1DrawAsPlayer1 = 0;
//		e1LoseAsPlayer1 = 0;
//		e1WinAsPlayer2 = 0;
//		e1DrawAsPlayer2 = 0;
//		e1LoseAsPlayer2 = 0;
//		e2TotalWins = 0;
//		e2TotalLoses = 0;
//		e2WinAsPlayer1 = 0;
//		e2DrawAsPlayer1 = 0;
//		e2LoseAsPlayer1 = 0;
//		e2WinAsPlayer2 = 0;
//		e2DrawAsPlayer2 = 0;
//		e2LoseAsPlayer2 = 0;
//
//		//Board that is used in games.
//		Board boardTest4 = null;
//
//		//Board that keeps copy of initial position, used to quickly reset the 
//		//board before new game take place.
//		Board initialPositionTest4 = null;
//
//		//Array of all boards that are used in the test case.
//		Board[] boardCollectionTest4 = null;
//
//		//Index of player that is entitled to make a move.
//		int currentIndexTest4 = 0;
//
//		//Number of all moves that was made during the game.
//		int numberOfMoveTest4 = 0;
//
//		//Players participating in the test case.
//		Player[] playersTest4 =  {
//				new Player("MCTS + H(7)", "MCTS + H(7)", "w", 50000),
//				new Player("MCTS + H(10)", "MCTS + H(10)", "b", 50000)
//		};
//
//		//Number of total moves. It is used to check whether the game is in 
//		//terminate state or not (the game finishes when there is no empty 
//		//fields in the board).
//		int totalNumberOfMovesTest4 = 38;
//
//		//Load board.
//		try {
//			FileInputStream fisTest4 = new FileInputStream("50_boards_11.sav");
//			ObjectInputStream oisTest4 = new ObjectInputStream(fisTest4);
//			boardCollectionTest4 = (Board[]) oisTest4.readObject();
//		} catch(Exception e) {
//			System.err.println("Error" + e.getMessage());
//		}
//
//		//The beginning and the end of the test.
//		long startTimeTest4 = 0, endTimeTest4 = 0;
//
//		//Report when games commenced.
//		startTimeTest4 = System.currentTimeMillis();
//
//		//Boards are OK. Proceed to testing.
//		for(int testIndex = 1; testIndex <= 100; ++testIndex) {
//			System.out.println("Test4: " + testIndex + " / 100");
//			//Reset settings.
//			currentIndexTest4 = 0;
//			numberOfMoveTest4 = 0;
//
//			//Swap players.
//			Player tmp = playersTest4[0];
//			playersTest4[0] = playersTest4[1];
//			playersTest4[1] = tmp;
//
//			//Reset the board to an initial state. When index is odd generate a 
//			//new random board.
//			if(testIndex % 2 == 1) {
//				//Load a new board.
//				boardTest4 = boardCollectionTest4[(Integer) testIndex/2];
//				initialPositionTest4 = boardTest4.duplicate();
//			} else {
//				//Reset the board.
//				boardTest4 = initialPositionTest4.duplicate();
//			}
//
//
//			//Run a single game.
//			while(numberOfMoveTest4 < totalNumberOfMovesTest4) {
//				if(playersTest4[currentIndexTest4].getType().equals("MCTS + H(10)")) {
//					//MCTS + H(5) to play.
//					Tuple<Integer, Integer> move;
//
//					//Pure Monte-Carlo + H(5) will select new move.
//					MonteCarloH10 mc = new MonteCarloH10(
//							boardTest4.duplicate(), 
//							playersTest4[currentIndexTest4].getColor(), 
//							numberOfMoveTest4, 
//							totalNumberOfMovesTest4);
//
//					move = mc.uct(playersTest4[currentIndexTest4].
//							getSimulationNumber());
//
//
//					boardTest4.makeMove(move, playersTest4[currentIndexTest4].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest4;
//
//					//Adjust index of current player.
//					currentIndexTest4 = (currentIndexTest4 + 1) % 2;
//				} else if(playersTest4[currentIndexTest4].getType().equals("MCTS + H(7)")) {
//					//MCTS (UCT) to play.
//					Tuple<Integer, Integer> move;
//
//					//Pure Monte-Carlo will select move.
//					MonteCarloH7 mc = new MonteCarloH7(
//							boardTest4.duplicate(), 
//							playersTest4[currentIndexTest4].getColor(), 
//							numberOfMoveTest4, 
//							totalNumberOfMovesTest4);
//
//					move = mc.uct(playersTest4[currentIndexTest4].
//							getSimulationNumber());
//
//
//					boardTest4.makeMove(move, playersTest4[currentIndexTest4].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest4;
//
//					//Adjust index of current player.
//					currentIndexTest4 = (currentIndexTest4 + 1) % 2;
//				}
//			} //end of single game.
//
//			String gameOutcome = Rules.calculateScore2(boardTest4);
//			BufferedWriter outputTest4 = new BufferedWriter(
//					new FileWriter("results_50k_11b_MCTS+H(7)vMCTS+H(10).txt", true));
//			outputTest4.append("Match #" + testIndex);
//			outputTest4.newLine();
//			outputTest4.append("Player 1: " + playersTest4[0].getName() + 
//					" Player 2: " + playersTest4[1].getName());
//			outputTest4.newLine();
//
//			//Append the result to the text file and update counters..
//			if(gameOutcome.equals("0")) {
//				//The game was a draw.
//				++totalDraws;
//				//Append information to the file.
//				outputTest4.append("Result: draw");
//				outputTest4.newLine();
//				outputTest4.close();
//
//				//Update statistics.
//				if(playersTest4[0].getName().equals("MCTS + H(10)")) {
//					e1DrawAsPlayer1++;
//					e2DrawAsPlayer2++;
//				} else {
//					e1DrawAsPlayer2++;
//					e2DrawAsPlayer1++;
//				}
//
//			} else {
//				//One side wins the game.
//				if(gameOutcome.equals(playersTest4[0].getColor())) {
//					//Player #1, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest4.append("Result: " + playersTest4[0].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest4[0].getName().equals("MCTS + H(10)")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer1++;
//						e2LoseAsPlayer2++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer1++;
//						e1LoseAsPlayer2++;
//					}
//
//				} else {
//					//Player #2, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest4.append("Result: " + playersTest4[1].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest4[1].getName().equals("MCTS + H(10)")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer2++;
//						e2LoseAsPlayer1++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer2++;
//						e1LoseAsPlayer1++;
//					}
//				}
//				outputTest4.newLine();
//				outputTest4.close();
//			}			
//		} //End of the test case. (for)
//
//		//Report when games ended.
//		endTimeTest4 = System.currentTimeMillis();
//
//		//Append total outcome of the test case to the file.
//		BufferedWriter output1Test4 = new BufferedWriter(
//				new FileWriter("results_50k_11b_MCTS+H(7)vMCTS+H(10).txt", true));
//		output1Test4.append("========================================");
//		output1Test4.newLine();
//		output1Test4.append("*Summary (50k)*");
//		output1Test4.newLine();
//		output1Test4.append("Draw occurred: " + totalDraws);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(7) total wins: " + e2TotalWins);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(10) total wins: " + e1TotalWins);
//		output1Test4.newLine();
//		output1Test4.append("Play time: " + (endTimeTest4 - startTimeTest4)/1000 + " seconds.");
//		output1Test4.newLine();
//
//		//Write statistics for MCTS.
//		output1Test4.append("MCTS + H(7) wins as player #1 : " + e2WinAsPlayer1);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(7) wins as player #2 : " + e2WinAsPlayer2);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(7) draws as player #1 : " + e2DrawAsPlayer1);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(7) draws as player #2 : " + e2DrawAsPlayer2);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(7) loses as player #1 : " + e2LoseAsPlayer1);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(7) loses as player #2 : " + e2LoseAsPlayer2);
//		output1Test4.newLine();
//
//		//Write statistics for Random AI.
//		output1Test4.append("MCTS + H(10) wins as player #1 : " + e1WinAsPlayer1);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(10) wins as player #2 : " + e1WinAsPlayer2);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(10) draws as player #1 : " + e1DrawAsPlayer1);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(10) draws as player #2 : " + e1DrawAsPlayer2);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(10) loses as player #1 : " + e1LoseAsPlayer1);
//		output1Test4.newLine();
//		output1Test4.append("MCTS + H(10) loses as player #2 : " + e1LoseAsPlayer2);
//		output1Test4.newLine();
//
//		output1Test4.append("========================================");
//		output1Test4.close();
//
//		/***********************************************************************
//		 * Test #5: (50,000 roll-outs) MCTS + H(5) v MCTS + H(10).
//		 **********************************************************************/
//		e1TotalWins = 0;
//		totalDraws = 0;
//		e1TotalLoses = 0;
//		e1WinAsPlayer1 = 0;
//		e1DrawAsPlayer1 = 0;
//		e1LoseAsPlayer1 = 0;
//		e1WinAsPlayer2 = 0;
//		e1DrawAsPlayer2 = 0;
//		e1LoseAsPlayer2 = 0;
//		e2TotalWins = 0;
//		e2TotalLoses = 0;
//		e2WinAsPlayer1 = 0;
//		e2DrawAsPlayer1 = 0;
//		e2LoseAsPlayer1 = 0;
//		e2WinAsPlayer2 = 0;
//		e2DrawAsPlayer2 = 0;
//		e2LoseAsPlayer2 = 0;
//
//		//Board that is used in games.
//		Board boardTest5 = null;
//
//		//Board that keeps copy of initial position, used to quickly reset the 
//		//board before new game take place.
//		Board initialPositionTest5 = null;
//
//		//Array of all boards that are used in the test case.
//		Board[] boardCollectionTest5 = null;
//
//		//Index of player that is entitled to make a move.
//		int currentIndexTest5 = 0;
//
//		//Number of all moves that was made during the game.
//		int numberOfMoveTest5 = 0;
//
//		//Players participating in the test case.
//		Player[] playersTest5 =  {
//				new Player("MCTS + H(5)", "MCTS + H(5)", "w", 50000),
//				new Player("MCTS + H(5+5)", "MCTS + H(5+5)", "b", 50000)
//		};
//
//		//Number of total moves. It is used to check whether the game is in 
//		//terminate state or not (the game finishes when there is no empty 
//		//fields in the board).
//		int totalNumberOfMovesTest5 = 38;
//
//		//Load board.
//		try {
//			FileInputStream fisTest5 = new FileInputStream("50_boards_11.sav");
//			ObjectInputStream oisTest5 = new ObjectInputStream(fisTest5);
//			boardCollectionTest5 = (Board[]) oisTest5.readObject();
//		} catch(Exception e) {
//			System.err.println("Error" + e.getMessage());
//		}
//
//		//The beginning and the end of the test.
//		long startTimeTest5 = 0, endTimeTest5 = 0;
//
//		//Report when games commenced.
//		startTimeTest5 = System.currentTimeMillis();
//
//		//Boards are OK. Proceed to testing.
//		for(int testIndex = 1; testIndex <= 100; ++testIndex) {
//			System.out.println("Test5: " + testIndex + " / 100");
//			//Reset settings.
//			currentIndexTest5 = 0;
//			numberOfMoveTest5 = 0;
//
//			//Swap players.
//			Player tmp = playersTest5[0];
//			playersTest5[0] = playersTest5[1];
//			playersTest5[1] = tmp;
//
//			//Reset the board to an initial state. When index is odd generate a 
//			//new random board.
//			if(testIndex % 2 == 1) {
//				//Load a new board.
//				boardTest5 = boardCollectionTest5[(Integer) testIndex/2];
//				initialPositionTest5 = boardTest5.duplicate();
//			} else {
//				//Reset the board.
//				boardTest5 = initialPositionTest5.duplicate();
//			}
//
//
//			//Run a single game.
//			while(numberOfMoveTest5 < totalNumberOfMovesTest5) {
//				if(playersTest5[currentIndexTest5].getType().equals("MCTS + H(5+5)")) {
//					//MCTS + H(10) to play.
//					Tuple<Integer, Integer> move;
//					//Pure Monte-Carlo + H(10) will select new move.
//					MonteCarloH55 mc = new MonteCarloH55(
//							boardTest5.duplicate(), 
//							playersTest5[currentIndexTest5].getColor(), 
//							numberOfMoveTest5, 
//							totalNumberOfMovesTest5);
//					move = mc.uct(playersTest5[currentIndexTest5].
//							getSimulationNumber());
//
//
//
//					boardTest5.makeMove(move, playersTest5[currentIndexTest5].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest5;
//
//					//Adjust index of current player.
//					currentIndexTest5 = (currentIndexTest5 + 1) % 2;
//				} else if(playersTest5[currentIndexTest5].getType().equals("MCTS + H(5)")) {
//					//MCTS (UCT) to play.
//					Tuple<Integer, Integer> move;
//					//Pure Monte-Carlo will select move.
//					MonteCarloH5 mc = new MonteCarloH5(
//							boardTest5.duplicate(), 
//							playersTest5[currentIndexTest5].getColor(), 
//							numberOfMoveTest5, 
//							totalNumberOfMovesTest5);
//					move = mc.uct(playersTest5[currentIndexTest5].
//							getSimulationNumber());
//
//
//					boardTest5.makeMove(move, playersTest5[currentIndexTest5].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest5;
//
//					//Adjust index of current player.
//					currentIndexTest5 = (currentIndexTest5 + 1) % 2;
//				}
//			} //end of single game.
//
//			String gameOutcome = Rules.calculateScore2(boardTest5);
//			BufferedWriter outputTest5 = new BufferedWriter(
//					new FileWriter("results_50k_11b_MCTS+H(5)vMCTS+H(5+5).txt", true));
//			outputTest5.append("Match #" + testIndex);
//			outputTest5.newLine();
//			outputTest5.append("Player 1: " + playersTest5[0].getName() + 
//					" Player 2: " + playersTest5[1].getName());
//			outputTest5.newLine();
//
//			//Append the result to the text file and update counters..
//			if(gameOutcome.equals("0")) {
//				//The game was a draw.
//				++totalDraws;
//				//Append information to the file.
//				outputTest5.append("Result: draw");
//				outputTest5.newLine();
//				outputTest5.close();
//
//				//Update statistics.
//				if(playersTest5[0].getName().equals("MCTS + H(5+5)")) {
//					e1DrawAsPlayer1++;
//					e2DrawAsPlayer2++;
//				} else {
//					e1DrawAsPlayer2++;
//					e2DrawAsPlayer1++;
//				}
//
//			} else {
//				//One side wins the game.
//				if(gameOutcome.equals(playersTest5[0].getColor())) {
//					//Player #1, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest5.append("Result: " + playersTest5[0].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest5[0].getName().equals("MCTS + H(5+5)")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer1++;
//						e2LoseAsPlayer2++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer1++;
//						e1LoseAsPlayer2++;
//					}
//
//				} else {
//					//Player #2, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest5.append("Result: " + playersTest5[1].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest5[1].getName().equals("MCTS + H(5+5)")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer2++;
//						e2LoseAsPlayer1++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer2++;
//						e1LoseAsPlayer1++;
//					}
//				}
//				outputTest5.newLine();
//				outputTest5.close();
//			}			
//		} //End of the test case. (for)
//
//		//Report when games ended.
//		endTimeTest5 = System.currentTimeMillis();
//
//		//Append total outcome of the test case to the file.
//		BufferedWriter output1Test5 = new BufferedWriter(
//				new FileWriter("results_50k_11b_MCTS+H(5)vMCTS+H(5+5).txt", true));
//		output1Test5.append("========================================");
//		output1Test5.newLine();
//		output1Test5.append("*Summary (50k)*");
//		output1Test5.newLine();
//		output1Test5.append("Draw occurred: " + totalDraws);
//		output1Test5.newLine();
//		output1Test5.append("MCTS +H (5) total wins: " + e2TotalWins);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5+5) total wins: " + e1TotalWins);
//		output1Test5.newLine();
//		output1Test5.append("Play time: " + (endTimeTest5 - startTimeTest5)/1000 + " seconds.");
//		output1Test5.newLine();
//
//		//Write statistics for MCTS.
//		output1Test5.append("MCTS + H(5) wins as player #1 : " + e2WinAsPlayer1);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5) wins as player #2 : " + e2WinAsPlayer2);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5) draws as player #1 : " + e2DrawAsPlayer1);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5) draws as player #2 : " + e2DrawAsPlayer2);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5) loses as player #1 : " + e2LoseAsPlayer1);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5) loses as player #2 : " + e2LoseAsPlayer2);
//		output1Test5.newLine();
//
//		//Write statistics for Random AI.
//		output1Test5.append("MCTS + H(5+5) wins as player #1 : " + e1WinAsPlayer1);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5+5) wins as player #2 : " + e1WinAsPlayer2);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5+5) draws as player #1 : " + e1DrawAsPlayer1);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5+5) draws as player #2 : " + e1DrawAsPlayer2);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5+5) loses as player #1 : " + e1LoseAsPlayer1);
//		output1Test5.newLine();
//		output1Test5.append("MCTS + H(5+5) loses as player #2 : " + e1LoseAsPlayer2);
//		output1Test5.newLine();
//
//		output1Test5.append("========================================");
//		output1Test5.close();
//		
//		/***********************************************************************
//		 * Test #6: (50,000 roll-outs) MCTS (UCT) v MCTS + H(5).
//		 **********************************************************************/
//		e1TotalWins = 0;
//		totalDraws = 0;
//		e1TotalLoses = 0;
//		e1WinAsPlayer1 = 0;
//		e1DrawAsPlayer1 = 0;
//		e1LoseAsPlayer1 = 0;
//		e1WinAsPlayer2 = 0;
//		e1DrawAsPlayer2 = 0;
//		e1LoseAsPlayer2 = 0;
//		e2TotalWins = 0;
//		e2TotalLoses = 0;
//		e2WinAsPlayer1 = 0;
//		e2DrawAsPlayer1 = 0;
//		e2LoseAsPlayer1 = 0;
//		e2WinAsPlayer2 = 0;
//		e2DrawAsPlayer2 = 0;
//		e2LoseAsPlayer2 = 0;
//
//		//Board that is used in games.
//		Board boardTest6 = null;
//
//		//Board that keeps copy of initial position, used to quickly reset the 
//		//board before new game take place.
//		Board initialPositionTest6 = null;
//
//		//Array of all boards that are used in the test case.
//		Board[] boardCollectionTest6 = null;
//
//		//Index of player that is entitled to make a move.
//		int currentIndexTest6 = 0;
//
//		//Number of all moves that was made during the game.
//		int numberOfMoveTest6 = 0;
//
//		//Players participating in the test case.
//		Player[] playersTest6 =  {
//				new Player("MCTS (UCT)", "MCTS (UCT)", "w", 50000),
//				new Player("MCTS + H(5)", "MCTS + H(5)", "b", 50000)
//		};
//
//		//Number of total moves. It is used to check whether the game is in 
//		//terminate state or not (the game finishes when there is no empty 
//		//fields in the board).
//		int totalNumberOfMovesTest6 = 38;
//
//		//Load board.
//		try {
//			FileInputStream fisTest6 = new FileInputStream("50_boards_11.sav");
//			ObjectInputStream oisTest6 = new ObjectInputStream(fisTest6);
//			boardCollectionTest6 = (Board[]) oisTest6.readObject();
//		} catch(Exception e) {
//			System.err.println("Error" + e.getMessage());
//		}
//
//		//The beginning and the end of the test.
//		long startTimeTest6 = 0, endTimeTest6 = 0;
//
//		//Report when games commenced.
//		startTimeTest6 = System.currentTimeMillis();
//
//		//Boards are OK. Proceed to testing.
//		for(int testIndex = 1; testIndex <= 100; ++testIndex) {
//			System.out.println("Test6: " + testIndex + " / 100");
//			//Reset settings.
//			currentIndexTest6 = 0;
//			numberOfMoveTest6 = 0;
//
//			//Swap players.
//			Player tmp = playersTest6[0];
//			playersTest6[0] = playersTest6[1];
//			playersTest6[1] = tmp;
//
//			//Reset the board to an initial state. When index is odd generate a 
//			//new random board.
//			if(testIndex % 2 == 1) {
//				//Load a new board.
//				boardTest6 = boardCollectionTest6[(Integer) testIndex/2];
//				initialPositionTest6 = boardTest6.duplicate();
//			} else {
//				//Reset the board.
//				boardTest6 = initialPositionTest6.duplicate();
//			}
//
//
//			//Run a single game.
//			while(numberOfMoveTest6 < totalNumberOfMovesTest6) {
//				if(playersTest6[currentIndexTest6].getType().equals("MCTS + H(5)")) {
//					//MCTS + H(10) to play.
//					Tuple<Integer, Integer> move;
//					//Pure Monte-Carlo + H(10) will select new move.
//					MonteCarloH5 mc = new MonteCarloH5(
//							boardTest6.duplicate(), 
//							playersTest6[currentIndexTest6].getColor(), 
//							numberOfMoveTest6, 
//							totalNumberOfMovesTest6);
//					move = mc.uct(playersTest6[currentIndexTest6].
//							getSimulationNumber());
//
//
//
//					boardTest6.makeMove(move, playersTest6[currentIndexTest6].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest6;
//
//					//Adjust index of current player.
//					currentIndexTest6 = (currentIndexTest6 + 1) % 2;
//				} else if(playersTest6[currentIndexTest6].getType().equals("MCTS (UCT)")) {
//					//MCTS (UCT) to play.
//					Tuple<Integer, Integer> move;
//					//Pure Monte-Carlo will select move.
//					MonteCarlo mc = new MonteCarlo(
//							boardTest6.duplicate(), 
//							playersTest6[currentIndexTest6].getColor(), 
//							numberOfMoveTest6, 
//							totalNumberOfMovesTest6);
//					move = mc.uct(playersTest6[currentIndexTest6].
//							getSimulationNumber());
//
//
//					boardTest6.makeMove(move, playersTest6[currentIndexTest6].getColor());
//
//					//Increment number of currently made moves.
//					++numberOfMoveTest6;
//
//					//Adjust index of current player.
//					currentIndexTest6 = (currentIndexTest6 + 1) % 2;
//				}
//			} //end of single game.
//
//			String gameOutcome = Rules.calculateScore2(boardTest6);
//			BufferedWriter outputTest6 = new BufferedWriter(
//					new FileWriter("results_50k_11b_MCTS+(UCT)vMCTS+H(5).txt", true));
//			outputTest6.append("Match #" + testIndex);
//			outputTest6.newLine();
//			outputTest6.append("Player 1: " + playersTest6[0].getName() + 
//					" Player 2: " + playersTest6[1].getName());
//			outputTest6.newLine();
//
//			//Append the result to the text file and update counters..
//			if(gameOutcome.equals("0")) {
//				//The game was a draw.
//				++totalDraws;
//				//Append information to the file.
//				outputTest6.append("Result: draw");
//				outputTest6.newLine();
//				outputTest6.close();
//
//				//Update statistics.
//				if(playersTest6[0].getName().equals("MCTS + H(5)")) {
//					e1DrawAsPlayer1++;
//					e2DrawAsPlayer2++;
//				} else {
//					e1DrawAsPlayer2++;
//					e2DrawAsPlayer1++;
//				}
//
//			} else {
//				//One side wins the game.
//				if(gameOutcome.equals(playersTest6[0].getColor())) {
//					//Player #1, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest6.append("Result: " + playersTest6[0].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest6[0].getName().equals("MCTS + H(5)")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer1++;
//						e2LoseAsPlayer2++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer1++;
//						e1LoseAsPlayer2++;
//					}
//
//				} else {
//					//Player #2, whoever it is, wins the game.
//
//					//Add note about the winner to the file.
//					outputTest6.append("Result: " + playersTest6[1].getName() + " wins");
//
//					//Update statistics.
//					if(playersTest6[1].getName().equals("MCTS + H(5)")) {
//						e1TotalWins++;
//						e2TotalLoses++;
//
//						e1WinAsPlayer2++;
//						e2LoseAsPlayer1++;
//					} else {
//						e2TotalWins++;
//						e1TotalLoses++;
//
//						e2WinAsPlayer2++;
//						e1LoseAsPlayer1++;
//					}
//				}
//				outputTest6.newLine();
//				outputTest6.close();
//			}			
//		} //End of the test case. (for)
//
//		//Report when games ended.
//		endTimeTest6 = System.currentTimeMillis();
//
//		//Append total outcome of the test case to the file.
//		BufferedWriter output1Test6 = new BufferedWriter(
//				new FileWriter("results_50k_11b_MCTS+(UCT)vMCTS+H(5).txt", true));
//		output1Test6.append("========================================");
//		output1Test6.newLine();
//		output1Test6.append("*Summary*");
//		output1Test6.newLine();
//		output1Test6.append("Draw occurred: " + totalDraws);
//		output1Test6.newLine();
//		output1Test6.append("MCTS + (UCT) total wins: " + e2TotalWins);
//		output1Test6.newLine();
//		output1Test6.append("MCTS + H(5) total wins: " + e1TotalWins);
//		output1Test6.newLine();
//		output1Test6.append("Play time: " + (endTimeTest6 - startTimeTest6)/1000 + " seconds.");
//		output1Test6.newLine();
//
//		//Write statistics for MCTS (UCT).
//		output1Test6.append("MCTS (UCT) wins as player #1 : " + e2WinAsPlayer1);
//		output1Test6.newLine();
//		output1Test6.append("MCTS (UCT) wins as player #2 : " + e2WinAsPlayer2);
//		output1Test6.newLine();
//		output1Test6.append("MCTS (UCT) draws as player #1 : " + e2DrawAsPlayer1);
//		output1Test6.newLine();
//		output1Test6.append("MCTS (UCT) draws as player #2 : " + e2DrawAsPlayer2);
//		output1Test6.newLine();
//		output1Test6.append("MCTS (UCT) loses as player #1 : " + e2LoseAsPlayer1);
//		output1Test6.newLine();
//		output1Test6.append("MCTS (UCT) loses as player #2 : " + e2LoseAsPlayer2);
//		output1Test6.newLine();
//
//		//Write statistics for MCTS + H(5).
//		output1Test6.append("MCTS + H(5) wins as player #1 : " + e1WinAsPlayer1);
//		output1Test6.newLine();
//		output1Test6.append("MCTS + H(5) wins as player #2 : " + e1WinAsPlayer2);
//		output1Test6.newLine();
//		output1Test6.append("MCTS + H(5) draws as player #1 : " + e1DrawAsPlayer1);
//		output1Test6.newLine();
//		output1Test6.append("MCTS + H(5) draws as player #2 : " + e1DrawAsPlayer2);
//		output1Test6.newLine();
//		output1Test6.append("MCTS + H(5) loses as player #1 : " + e1LoseAsPlayer1);
//		output1Test6.newLine();
//		output1Test6.append("MCTS + H(5) loses as player #2 : " + e1LoseAsPlayer2);
//		output1Test6.newLine();
//
//		output1Test6.append("========================================");
//		output1Test6.close();

	
	private static BufferedWriter extracted(BufferedWriter outputTest1) {
		return outputTest1;
	}
	private static Board extracted(Board boardTest1) {
		return boardTest1;
	}
public static void updateStatisticsFirst (boolean value, int e1DrawAsPlayer1, 
		int e2DrawAsPlayer2, int e1DrawAsPlayer2, int e2DrawAsPlayer1) {
	
	if(value) {
		e1DrawAsPlayer1++;
		e2DrawAsPlayer2++;
	} else {
		e1DrawAsPlayer2++;
		e2DrawAsPlayer1++;
	}
	
}

public static void updateStatisticsSecond (boolean value, int e1TotalWins,
		int e2TotalLoses,int e1WinAsPlayer2, int e2LoseAsPlayer1,
		int e2TotalWins, int e1TotalLoses, int e2WinAsPlayer2, int e1LoseAsPlayer1) {
	
	if(value) {
		e1TotalWins++;
		e2TotalLoses++;

		e1WinAsPlayer2++;
		e2LoseAsPlayer1++;
	} else {
		e2TotalWins++;
		e1TotalLoses++;

		e2WinAsPlayer2++;
		e1LoseAsPlayer1++;
	}
	
}

public static void RunASingleGame (int numberOfMoveTest1, int totalNumberOfMovesTest1
		, int currentIndexTest1, Player[] playersTest1,
		Board boardTest1, MonteCarloH5Boltzmann mc, Charles_2 charles) throws Exception {
	
	while(numberOfMoveTest1 < totalNumberOfMovesTest1) {
		if("Boltzmann1k".equals(playersTest1[currentIndexTest1].getType())) {
			//MCTS + H(7) to play.
			Tuple<Integer, Integer> move;
			//Pure Monte-Carlo will select move.
			

			
			move = mc.uct(playersTest1[currentIndexTest1].
					getSimulationNumber());


			extracted(boardTest1).makeMove(move, playersTest1[currentIndexTest1].getColor());
//			System.out.println("move >> " + move.toString());
			//Increment number of currently made moves.
			++numberOfMoveTest1;

			//Adjust index of current player.
			currentIndexTest1 = (currentIndexTest1 + 1) % 2;
		} else if("Charles_2".equals(playersTest1[currentIndexTest1].getType())) {
			//MCTS (UCT) to play.
			Tuple<Integer, Integer> move;
			//Pure Monte-Carlo will select move.
			
			/**
			 * Random AI.
			 */
//			LuckyAI random = new LuckyAI(boardTest1, numberOfMoveTest1, totalNumberOfMovesTest1);
//			move = random.getMove();
			
			/**
			 * Charles_2
			 */
			
			
			move = charles.getMove();

			/**
			 * Boltzmann
			 */
//			MonteCarloH5Boltzmann mc = new MonteCarloH5Boltzmann(
//					boardTest1.duplicate(), 
//					playersTest1[currentIndexTest1].getColor(), 
//					numberOfMoveTest1, 
//					totalNumberOfMovesTest1);
//
//			move = mc.uct(playersTest1[currentIndexTest1].
//					getSimulationNumber());


			extracted(boardTest1).makeMove(move, playersTest1[currentIndexTest1].getColor());

			//Increment number of currently made moves.
			++numberOfMoveTest1;

			//Adjust index of current player.
			currentIndexTest1 = (currentIndexTest1 + 1) % 2;
		}
	}
	
}

public static void loadBoard (Board[] boardCollectionTest1, String nameBoard) {
	FileInputStream fisTest = null ;
	//Load board.
	try {
		 fisTest = new FileInputStream(nameBoard);
		ObjectInputStream oisTest1 = new ObjectInputStream(fisTest);
		boardCollectionTest1 = (Board[]) oisTest1.readObject();
		oisTest1.close();
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (fisTest != null) {
               try {
            	   fisTest.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}
		
		}
	}

public static void DeclareBuffers (BufferedWriter outputTest1) {
	try{
		outputTest1 = new BufferedWriter(
				new FileWriter("results_100_3b_Boltzmann1kvCharles_2.txt", true));
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (extracted(outputTest1) != null) {
               try {
            	   extracted(outputTest1).close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
}
	
}

}
