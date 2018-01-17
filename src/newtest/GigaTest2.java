package newtest;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import util.Tuple;

//import ai.charles2.Charles_2;
import ai.montecarlo.MonteCarlo;
import ai.montecarloheuristic10.MonteCarloH10;
import ai.montecarloheuristic5.MonteCarloH5;
import ai.montecarloheuristic55.MonteCarloH55;
import ai.montecarloheuristic7.MonteCarloH7;
//import ai.random.LuckyAI;

import core.Board;
import core.Player;
import core.Rules;

/**
 * Classe relativa al gigatest2
 * @author Mina
 *
 */

public class GigaTest2 {

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String[] args) throws Exception {
		
				//test go
				testOne2();
		/***********************************************************************
		 * Test #1: (50,000 roll-outs) MCTS_UCT v MCTS + H(5) 3-point board.
		 **********************************************************************/
				//test go
				testTwo2();
		
		/***********************************************************************
		 * Test #2: (50,000 roll-outs) MCTS_UCT v MCTS_H(7).
		 **********************************************************************/
				//test go
				testThree2();
		/***********************************************************************
		 * Test #3: (50,000 roll-outs) MCTS (UCT) v MCTS + H(10).
		 **********************************************************************/
				//test go
				testFour2();
		/***********************************************************************
		 * Test #4: (50,000 roll-outs) MCTS_UCT v MCTS + H(5+5).
		 **********************************************************************/
				//test go
				testFive2();

		/***********************************************************************
		 * Test #5: (50,000 roll-outs) MCTS + H(5) v MCTS + H(7).
		 **********************************************************************/
				//test go
				testSix2();
		
		/***********************************************************************
		 * Test #6: (50,000 roll-outs) MCTS_H(7) v MCTS_H(10).
		 **********************************************************************/
				
	}
	
	public static Integer maxCast (int a){
		
		Integer valInteger = (Integer) a; 
			
		return valInteger;
		}
	

	
public static void testOne2 () throws Exception {
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
	
	//Board that is used in games.
			Board boardTest1 = null;

			//Board that keeps copy of initial position, used to quickly reset the 
			//board before new game take place.
			Board initialPositionTest1 = null;

			//Array of all boards that are used in the test case.
			Board[] boardCollectionTest1 = null;

			//Index of player that is entitled to make a move.
			int currentIndexTest1 = 0;

			//Number of all moves that was made during the game.
			int numberOfMoveTest1 = 0;
			int value1_grt2_50000 = 50000; 
			//Players participating in the test case.
			Player[] playersTest1 =  {
					new Player("MCTS_UCT", "MCTS_UCT", "w", value1_grt2_50000),
					new Player("MCTS_H(5)", "MCTS_H(5)", "b", value1_grt2_50000)
			};

			//Number of total moves. It is used to check whether the game is in 
			//terminate state or not (the game finishes when there is no empty 
			//fields in the board).
			int totalNumberOfMovesTest1 = 46;

			//Load board.
			String name2Board1 = "50_boards_3.sav";
			loadBoard ( boardCollectionTest1, name2Board1);
			


			//The beginning and the end of the test.
			long startTime = 0, endTime = 0;
			String nameFile1 = "results_50k_3b_MCTS_UCTvMCTS_H(5).txt";
			//Define buffers.
			BufferedWriter outputTest1 = null;
			defineBuffers ( outputTest1, nameFile1);
			//Report when games commenced.
			startTime = System.currentTimeMillis();

		
			
			MonteCarloH5 mch5 = new MonteCarloH5(
					boardTest1.duplicate(), 
					playersTest1[currentIndexTest1].getColor(), 
					numberOfMoveTest1, 
					totalNumberOfMovesTest1);
			MonteCarlo mct1 = new MonteCarlo(
					boardTest1.duplicate(), 
					playersTest1[currentIndexTest1].getColor(), 
					numberOfMoveTest1, 
					totalNumberOfMovesTest1);
			
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
				newRandomBoardTwo ( testIndex,  boardTest1, 
						boardCollectionTest1, initialPositionTest1 );


				//Run a single game.
				while(numberOfMoveTest1 < totalNumberOfMovesTest1) {
					if("MCTS_UCT".equals(playersTest1[currentIndexTest1].getType())) {
						//MCTS + H(7) to play.
						Tuple<Integer, Integer> move;
						//Pure Monte-Carlo will select a move.

						move = mct1.uct(playersTest1[currentIndexTest1].
								getSimulationNumber());

						boardTest1.makeMove(move, playersTest1[currentIndexTest1].getColor());

						//Increment number of currently made moves.
						++numberOfMoveTest1;

						//Adjust index of current player.
						currentIndexTest1 = (currentIndexTest1 + 1) % 2;
					} else if("MCTS_H(5)".equals(playersTest1[currentIndexTest1].getType())) {
						//MCTS (UCT) to play.
						Tuple<Integer, Integer> move;
						//Pure Monte-Carlo will select move.
						

						move = mch5.uct(playersTest1[currentIndexTest1].
								getSimulationNumber());


						boardTest1.makeMove(move, playersTest1[currentIndexTest1].getColor());

						//Increment number of currently made moves.
						++numberOfMoveTest1;

						//Adjust index of current player.
						currentIndexTest1 = (currentIndexTest1 + 1) % 2;
					}
				} //end of single game.

				String gameOutcome = Rules.calculateScore(boardTest1);
				
				outputTest1.append("Match #" + testIndex);
				outputTest1.newLine();
				outputTest1.append("Player 1: " + playersTest1[0].getName() + 
						" Player 2: " + playersTest1[1].getName());
				outputTest1.newLine();

				//Append the result to the text file and update counters..
				String zero = "0";
				
				if(gameOutcome.equals(zero)) {
					//The game was a draw.
					++totalDraws;
					//Append information to the file.
					outputTest1.append("Result: draw");
					outputTest1.newLine();
					outputTest1.close();

					//Update statistics.
					boolean valuePlayers1 = "MCTS_H(5)".equals(playersTest1[0].getName());
					updateStatisticsA2 ( valuePlayers1,  e1DrawAsPlayer1,
							 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

				} else {
					boolean value = true;
					//One side wins the game.
					String phrase2_1 = "MCTS_H(5)";
					OneSideWinsTheGame ( gameOutcome,  playersTest1 
							, outputTest1,  value ,  e1TotalWins,
							 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
							 e2TotalWins,  e1TotalLoses, e2WinAsPlayer2, 
							 e1LoseAsPlayer1, phrase2_1);
				}			
			} //End of the test case. (for)

			//Report when games ended.
			int value1_gt2_1000 = 1000;
			//Append total outcome of the test case to the file.
			BufferedWriter output1Test1 = null;
			defineBuffers ( output1Test1,  nameFile1);
		
		
			output1Test1.append("========================================");
			output1Test1.newLine();
			output1Test1.append("*Summary 3-point board 50k roll-outs*");
			output1Test1.newLine();
			output1Test1.append("Draw occurred: " + totalDraws);
			output1Test1.newLine();
			output1Test1.append("MCTS_UCT total wins: " + e2TotalWins);
			output1Test1.newLine();
			output1Test1.append("MCTS_H(5) total wins: " + e1TotalWins);
			output1Test1.newLine();
			output1Test1.append("Play time: " + (endTime - startTime)/value1_gt2_1000 + " seconds.");
			output1Test1.newLine();

			//Write statistics for MCTS.
			output1Test1.append("MCTS_UCT wins as player #1 : " + e2WinAsPlayer1);
			output1Test1.newLine();
			output1Test1.append("MCTS_UCT wins as player #2 : " + e2WinAsPlayer2);
			output1Test1.newLine();
			output1Test1.append("MCTS_UCT draws as player #1 : " + e2DrawAsPlayer1);
			output1Test1.newLine();
			output1Test1.append("MCTS_UCT draws as player #2 : " + e2DrawAsPlayer2);
			output1Test1.newLine();
			output1Test1.append("MCTS_UCT loses as player #1 : " + e2LoseAsPlayer1);
			output1Test1.newLine();
			output1Test1.append("MCTS_UCT loses as player #2 : " + e2LoseAsPlayer2);
			output1Test1.newLine();

			//Write statistics for Random AI.
			output1Test1.append("MCTS_H(5) wins as player #1 : " + e1WinAsPlayer1);
			output1Test1.newLine();
			output1Test1.append("MCTS_H(5) wins as player #2 : " + e1WinAsPlayer2);
			output1Test1.newLine();
			output1Test1.append("MCTS_H(5) draws as player #1 : " + e1DrawAsPlayer1);
			output1Test1.newLine();
			output1Test1.append("MCTS_H(5) draws as player #2 : " + e1DrawAsPlayer2);
			output1Test1.newLine();
			output1Test1.append("MCTS_H(5) loses as player #1 : " + e1LoseAsPlayer1);
			output1Test1.newLine();
			output1Test1.append("MCTS_H(5) loses as player #2 : " + e1LoseAsPlayer2);
			output1Test1.newLine();

			output1Test1.append("========================================");
			output1Test1.close();
	
}



public static void testTwo2 () throws Exception {
	
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

	//Board that is used in games.
	Board boardTest2 = null;

	//Board that keeps copy of initial position, used to quickly reset the 
	//board before new game take place.
	Board initialPositionTest2 = null;

	//Array of all boards that are used in the test case.
	Board[] boardCollectionTest2 = null;

	//Index of player that is entitled to make a move.
	int currentIndexTest2 = 0;

	//Number of all moves that was made during the game.
	int numberOfMoveTest2 = 0;
	int value2_grt2_50000 = 50000; 
	//Players participating in the test case.
	Player[] playersTest2 =  {
			new Player("MCTS_UCT", "MCTS_UCT", "w", value2_grt2_50000),
			new Player("MCTS_H(7)", "MCTS_H(7)", "b", value2_grt2_50000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest2 = 46;

	//Load board.
	String name2Board2 = "50_boards_3.sav";
	loadBoard ( boardCollectionTest2, name2Board2);
	

	//The beginning and the end of the test.
	long startTimeTest2 = 0, endTimeTest2 = 0;

	//Report when games commenced.
	startTimeTest2 = System.currentTimeMillis();

	String name2File2 = "results_50k_3b_MCTS_UCTvMCTS_H(7).txt";
	BufferedWriter outputTest2 = null;
	//Define buffers.
	defineBuffers ( outputTest2, name2File2);
	
	
	MonteCarloH7 mc7h = new MonteCarloH7(
			boardTest2.duplicate(), 
			playersTest2[currentIndexTest2].getColor(), 
			numberOfMoveTest2, 
			totalNumberOfMovesTest2);
	MonteCarlo mct2 = new MonteCarlo(
			boardTest2.duplicate(), 
			playersTest2[currentIndexTest2].getColor(), 
			numberOfMoveTest2, 
			totalNumberOfMovesTest2);
	
	//Boards are OK. Proceed to testing.
	for(int testIndex = 1; testIndex <= 100; ++testIndex) {
		System.out.println("Test2: " + testIndex + " / 100");
		//Reset settings.
		currentIndexTest2 = 0;
		numberOfMoveTest2 = 0;
		
		//Swap players.
		Player tmp = playersTest2[0];
		playersTest2[0] = playersTest2[1];
		playersTest2[1] = tmp;

		//Reset the board to an initial state. When index is odd generate a 
		//new random board.
		newRandomBoardTwo ( testIndex,  boardTest2, 
				boardCollectionTest2, initialPositionTest2 );

		//Run a single game.
		while(numberOfMoveTest2 < totalNumberOfMovesTest2) {
			if("MCTS_H(7)".equals(playersTest2[currentIndexTest2].getType())) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select move.
				
				move = mc7h.uct(playersTest2[currentIndexTest2].
						getSimulationNumber());


				boardTest2.makeMove(move, playersTest2[currentIndexTest2].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest2;

				//Adjust index of current player.
				currentIndexTest2 = (currentIndexTest2 + 1) % 2;
			} else if("MCTS_UCT".equals(playersTest2[currentIndexTest2].getType())) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select move.
				
				move = mct2.uct(playersTest2[currentIndexTest2].
						getSimulationNumber());


				boardTest2.makeMove(move, playersTest2[currentIndexTest2].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest2;

				//Adjust index of current player.
				currentIndexTest2 = (currentIndexTest2 + 1) % 2;
			}
		} //end of single game.

		String gameOutcome = Rules.calculateScore(boardTest2);
		
		outputTest2.append("Match #" + testIndex);
		outputTest2.newLine();
		outputTest2.append("Player 1: " + playersTest2[0].getName() + 
				" Player 2: " + playersTest2[1].getName());
		outputTest2.newLine();

		//Append the result to the text file and update counters..
		String zero = "0";
		
		if(gameOutcome.equals(zero)) {
			//The game was a draw.
			++totalDraws;
			//Append information to the file.
			outputTest2.append("Result: draw");
			outputTest2.newLine();
			outputTest2.close();

			//Update statistics.
			boolean valuePlayers2 = "MCTS_H(7)".equals(playersTest2[0].getName());
			updateStatisticsA2 ( valuePlayers2,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			boolean value = true;
			String phrase2_2 = "MCTS_H(7)";
			OneSideWinsTheGame ( gameOutcome,  playersTest2 
					, outputTest2,  value ,  e1TotalWins,
					 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
					 e2TotalWins,  e1TotalLoses,
					 e2WinAsPlayer2,  e1LoseAsPlayer1, phrase2_2 );
			
		}			
	} //End of the test case. (for)

	//Report when games ended.

	int value2_gt2_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test2 = null;	
	defineBuffers ( output1Test2,  name2File2);
	
		 
	output1Test2.append("========================================");
	output1Test2.newLine();
	output1Test2.append("*Summary (50k) 3-point board*");
	output1Test2.newLine();
	output1Test2.append("Draw occurred: " + totalDraws);
	output1Test2.newLine();
	output1Test2.append("MCTS_UCT total wins: " + e2TotalWins);
	output1Test2.newLine();
	output1Test2.append("MCTS_H(7) total wins: " + e1TotalWins);
	output1Test2.newLine();
	output1Test2.append("Play time: " + (endTimeTest2 - startTimeTest2)/value2_gt2_1000 + " seconds.");
	output1Test2.newLine();

	//Write statistics for MCTS.
	output1Test2.append("MCTS_UCT wins as player #1 : " + e2WinAsPlayer1);
	output1Test2.newLine();
	output1Test2.append("MCTS_UCT wins as player #2 : " + e2WinAsPlayer2);
	output1Test2.newLine();
	output1Test2.append("MCTS_UCT draws as player #1 : " + e2DrawAsPlayer1);
	output1Test2.newLine();
	output1Test2.append("MCTS_UCT draws as player #2 : " + e2DrawAsPlayer2);
	output1Test2.newLine();
	output1Test2.append("MCTS_UCT loses as player #1 : " + e2LoseAsPlayer1);
	output1Test2.newLine();
	output1Test2.append("MCTS_UCT loses as player #2 : " + e2LoseAsPlayer2);
	output1Test2.newLine();

	//Write statistics for Random AI.
	output1Test2.append("MCTS_H(7) wins as player #1 : " + e1WinAsPlayer1);
	output1Test2.newLine();
	output1Test2.append("MCTS_H(7) wins as player #2 : " + e1WinAsPlayer2);
	output1Test2.newLine();
	output1Test2.append("MCTS_H(7) draws as player #1 : " + e1DrawAsPlayer1);
	output1Test2.newLine();
	output1Test2.append("MCTS_H(7) draws as player #2 : " + e1DrawAsPlayer2);
	output1Test2.newLine();
	output1Test2.append("MCTS_H(7) loses as player #1 : " + e1LoseAsPlayer1);
	output1Test2.newLine();
	output1Test2.append("MCTS_H(7) loses as player #2 : " + e1LoseAsPlayer2);
	output1Test2.newLine();

	output1Test2.append("========================================");
	output1Test2.close();

}


public static void testThree2 () throws Exception {
	
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
	
	//Board that is used in games.
	Board boardTest3 = null;

	//Board that keeps copy of initial position, used to quickly reset the 
	//board before new game take place.
	Board initialPositionTest3 = null;

	//Array of all boards that are used in the test case.
	Board[] boardCollectionTest3 = null;

	//Index of player that is entitled to make a move.
	int currentIndexTest3 = 0;

	//Number of all moves that was made during the game.
	int numberOfMoveTest3 = 0;
	int value3_grt2_50000 = 50000; 
	//Players participating in the test case.
	Player[] playersTest3 =  {
			new Player("MCTS_UCT", "MCTS_UCT", "w", value3_grt2_50000),
			new Player("MCTS_H(10)", "MCTS_H(10)", "b", value3_grt2_50000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest3 = 46;
	//Load board
	String name2Board3 = "50_boards_3.sav";
	loadBoard ( boardCollectionTest3, name2Board3);
	


	//The beginning and the end of the test.
	long startTimeTest3 = 0, endTimeTest3 = 0;

	//Report when games commenced.
	startTimeTest3 = System.currentTimeMillis();
	String name2File3 = "results_50k_3b_MCTS_UCTvMCTS_H(10).txt";
	BufferedWriter outputTest3 = null;
	//Define buffers.
	defineBuffers ( outputTest3, name2File3);
	
	
	MonteCarloH10 mch10 = new MonteCarloH10(
			boardTest3.duplicate(), 
			playersTest3[currentIndexTest3].getColor(), 
			numberOfMoveTest3, 
			totalNumberOfMovesTest3);
	MonteCarlo mc = new MonteCarlo(
			boardTest3.duplicate(), 
			playersTest3[currentIndexTest3].getColor(), 
			numberOfMoveTest3, 
			totalNumberOfMovesTest3);

	//Boards are OK. Proceed to testing.
	for(int testIndex = 1; testIndex <= 100; ++testIndex) {
		System.out.println("Test3: " + testIndex + " / 100");
		//Reset settings.
		currentIndexTest3 = 0;
		numberOfMoveTest3 = 0;

		//Swap players.
		Player tmp = playersTest3[0];
		playersTest3[0] = playersTest3[1];
		playersTest3[1] = tmp;

		//Reset the board to an initial state. When index is odd generate a 
		//new random board.
		newRandomBoardTwo ( testIndex,  boardTest3, 
				boardCollectionTest3, initialPositionTest3 );


		//Run a single game.
		while(numberOfMoveTest3 < totalNumberOfMovesTest3) {
			if("MCTS_H(10)".equals(playersTest3[currentIndexTest3].getType())) {
				//MCTS + H(5) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo + H(5) will select new move.
				
				move = mch10.uct(playersTest3[currentIndexTest3].
						getSimulationNumber());


				boardTest3.makeMove(move, playersTest3[currentIndexTest3].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest3;

				//Adjust index of current player.
				currentIndexTest3 = (currentIndexTest3 + 1) % 2;
			} else if("MCTS_UCT".equals(playersTest3[currentIndexTest3].getType())) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;

				//Pure Monte-Carlo will select move.
	
				move = mc.uct(playersTest3[currentIndexTest3].
						getSimulationNumber());


				boardTest3.makeMove(move, playersTest3[currentIndexTest3].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest3;

				//Adjust index of current player.
				currentIndexTest3 = (currentIndexTest3 + 1) % 2;
			}
		} //end of single game.

		String gameOutcome = Rules.calculateScore(boardTest3);
		
		outputTest3.append("Match #" + testIndex);
		outputTest3.newLine();
		outputTest3.append("Player 1: " + playersTest3[0].getName() + 
				" Player 2: " + playersTest3[1].getName());
		outputTest3.newLine();

		//Append the result to the text file and update counters..
		String zero = "0";
		
		if(gameOutcome.equals(zero)) {
			//The game was a draw.
			++totalDraws;
			//Append information to the file.
			outputTest3.append("Result: draw");
			outputTest3.newLine();
			outputTest3.close();

			//Update statistics.
			boolean valuePlayers3 = "MCTS_H(10)".equals(playersTest3[0].getName());
			updateStatisticsA2 ( valuePlayers3,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			//One side wins the game.
			boolean value = true;
			String phrase3_2 = "MCTS_H(10)";
			OneSideWinsTheGame ( gameOutcome,  playersTest3 
					, outputTest3,  value ,  e1TotalWins,
					 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
					 e2TotalWins,  e1TotalLoses,
					 e2WinAsPlayer2,  e1LoseAsPlayer1, phrase3_2 );
			
		}			
	} //End of the test case. (for)

	//Report when games ended.
	int value3_gt2_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test3 = null;	
	defineBuffers ( output1Test3,  name2File3);
	
	
	output1Test3.append("========================================");
	output1Test3.newLine();
	output1Test3.append("*Summary 50k roll-outs 3 point board*");
	output1Test3.newLine();
	output1Test3.append("Draw occurred: " + totalDraws);
	output1Test3.newLine();
	output1Test3.append("MCTS_UCT total wins: " + e2TotalWins);
	output1Test3.newLine();
	output1Test3.append("MCTS_H(10) total wins: " + e1TotalWins);
	output1Test3.newLine();
	output1Test3.append("Play time: " + (endTimeTest3 - startTimeTest3)/value3_gt2_1000 + " seconds.");
	output1Test3.newLine();

	//Write statistics for MCTS.
	output1Test3.append("MCTS_UCT wins as player #1 : " + e2WinAsPlayer1);
	output1Test3.newLine();
	output1Test3.append("MCTS_UCT wins as player #2 : " + e2WinAsPlayer2);
	output1Test3.newLine();
	output1Test3.append("MCTS_UCT draws as player #1 : " + e2DrawAsPlayer1);
	output1Test3.newLine();
	output1Test3.append("MCTS_UCT draws as player #2 : " + e2DrawAsPlayer2);
	output1Test3.newLine();
	output1Test3.append("MCTS_UCT loses as player #1 : " + e2LoseAsPlayer1);
	output1Test3.newLine();
	output1Test3.append("MCTS_UCT loses as player #2 : " + e2LoseAsPlayer2);
	output1Test3.newLine();

	//Write statistics for Random AI.
	output1Test3.append("MCTS_H(10) wins as player #1 : " + e1WinAsPlayer1);
	output1Test3.newLine();
	output1Test3.append("MCTS_H(10) wins as player #2 : " + e1WinAsPlayer2);
	output1Test3.newLine();
	output1Test3.append("MCTS_H(10) draws as player #1 : " + e1DrawAsPlayer1);
	output1Test3.newLine();
	output1Test3.append("MCTS_H(10) draws as player #2 : " + e1DrawAsPlayer2);
	output1Test3.newLine();
	output1Test3.append("MCTS_H(10) loses as player #1 : " + e1LoseAsPlayer1);
	output1Test3.newLine();
	output1Test3.append("MCTS_H(10) loses as player #2 : " + e1LoseAsPlayer2);
	output1Test3.newLine();

	output1Test3.append("========================================");
	output1Test3.close();

	
}



public static void testFour2 () throws Exception {
	
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

	//Board that is used in games.
	Board boardTest4 = null;

	//Board that keeps copy of initial position, used to quickly reset the 
	//board before new game take place.
	Board initialPositionTest4 = null;

	//Array of all boards that are used in the test case.
	Board[] boardCollectionTest4 = null;

	//Index of player that is entitled to make a move.
	int currentIndexTest4 = 0;

	//Number of all moves that was made during the game.
	int numberOfMoveTest4 = 0;
	int value4_grt2_50000 = 50000; 
	//Players participating in the test case.
	Player[] playersTest4 =  {
			new Player("MCTS_UCT", "MCTS_UCT", "w", value4_grt2_50000),
			new Player("MCTS_H(5+5)", "MCTS_H(5+5)", "b", value4_grt2_50000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest4 = 46;
	//Load board
	String name2Board4 = "50_boards_3.sav";
	loadBoard ( boardCollectionTest4, name2Board4);
	//Load board.
	

	//The beginning and the end of the test.
	long startTimeTest4 = 0, endTimeTest4 = 0;

	//Report when games commenced.
	startTimeTest4 = System.currentTimeMillis();
	String name2File4 = "results_50k_3b_MCTS_UCTvMCTS_H(5+5).txt";
	BufferedWriter outputTest4 = null;
	//Define buffers.
	defineBuffers ( outputTest4, name2File4);
	
	
	MonteCarloH55 mch55 = new MonteCarloH55(
			boardTest4.duplicate(), 
			playersTest4[currentIndexTest4].getColor(), 
			numberOfMoveTest4, 
			totalNumberOfMovesTest4);
	MonteCarlo m_c = new MonteCarlo(
			boardTest4.duplicate(), 
			playersTest4[currentIndexTest4].getColor(), 
			numberOfMoveTest4, 
			totalNumberOfMovesTest4);

	
	//Boards are OK. Proceed to testing.
	for(int testIndex = 1; testIndex <= 100; ++testIndex) {
		System.out.println("Test4: " + testIndex + " / 100");
		//Reset settings.
		currentIndexTest4 = 0;
		numberOfMoveTest4 = 0;

		//Swap players.
		Player tmp = playersTest4[0];
		playersTest4[0] = playersTest4[1];
		playersTest4[1] = tmp;

		//Reset the board to an initial state. When index is odd generate a 
		//new random board.
		newRandomBoardTwo ( testIndex,  boardTest4, 
				boardCollectionTest4, initialPositionTest4 );


		//Run a single game.
		while(numberOfMoveTest4 < totalNumberOfMovesTest4) {
			if("MCTS_H(5+5)".equals(playersTest4[currentIndexTest4].getType())) {
				//MCTS + H(5) to play.
				Tuple<Integer, Integer> move;

				//Pure Monte-Carlo + H(5) will select new move.

				move = mch55.uct(playersTest4[currentIndexTest4].
						getSimulationNumber());


				boardTest4.makeMove(move, playersTest4[currentIndexTest4].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest4;

				//Adjust index of current player.
				currentIndexTest4 = (currentIndexTest4 + 1) % 2;
			} else if("MCTS_UCT".equals(playersTest4[currentIndexTest4].getType())) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;

				//Pure Monte-Carlo will select move.

				move = m_c.uct(playersTest4[currentIndexTest4].
						getSimulationNumber());


				boardTest4.makeMove(move, playersTest4[currentIndexTest4].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest4;

				//Adjust index of current player.
				currentIndexTest4 = (currentIndexTest4 + 1) % 2;
			}
		} //end of single game.

		String gameOutcome = Rules.calculateScore(boardTest4);
		
		outputTest4.append("Match #" + testIndex);
		outputTest4.newLine();
		outputTest4.append("Player 1: " + playersTest4[0].getName() + 
				" Player 2: " + playersTest4[1].getName());
		outputTest4.newLine();

		//Append the result to the text file and update counters..
		String zero = "0";
		
		if(gameOutcome.equals(zero)) {
			//The game was a draw.
			++totalDraws;
			//Append information to the file.
			outputTest4.append("Result: draw");
			outputTest4.newLine();
			outputTest4.close();

			//Update statistics.
			boolean valuePlayers4 = "MCTS_H(5+5)".equals(playersTest4[0].getName());
			updateStatisticsA2 ( valuePlayers4,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			//One side wins the game.
			boolean value = true;
			String phrase4_2 = "MCTS_H(5+5)";
			OneSideWinsTheGame ( gameOutcome,  playersTest4 
					, outputTest4,  value ,  e1TotalWins,
					 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
					 e2TotalWins,  e1TotalLoses,
					 e2WinAsPlayer2,  e1LoseAsPlayer1, phrase4_2 );
			
		}			
	} //End of the test case. (for)

	//Report when games ended.
	int value4_gt2_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test4 = null;	
	defineBuffers ( output1Test4,  name2File4);
	
	
	
	output1Test4.append("========================================");
	output1Test4.newLine();
	output1Test4.append("*Summary 50k roll-outs 3 point board*");
	output1Test4.newLine();
	output1Test4.append("Draw occurred: " + totalDraws);
	output1Test4.newLine();
	output1Test4.append("MCTS_UCT total wins: " + e2TotalWins);
	output1Test4.newLine();
	output1Test4.append("MCTS_H(5+5) total wins: " + e1TotalWins);
	output1Test4.newLine();
	output1Test4.append("Play time: " + (endTimeTest4 - startTimeTest4)/value4_gt2_1000 + " seconds.");
	output1Test4.newLine();

	//Write statistics for MCTS.
	output1Test4.append("MCTS_UCT wins as player #1 : " + e2WinAsPlayer1);
	output1Test4.newLine();
	output1Test4.append("MCTS_UCT wins as player #2 : " + e2WinAsPlayer2);
	output1Test4.newLine();
	output1Test4.append("MCTS_UCT draws as player #1 : " + e2DrawAsPlayer1);
	output1Test4.newLine();
	output1Test4.append("MCTS_UCT draws as player #2 : " + e2DrawAsPlayer2);
	output1Test4.newLine();
	output1Test4.append("MCTS_UCT loses as player #1 : " + e2LoseAsPlayer1);
	output1Test4.newLine();
	output1Test4.append("MCTS_UCT loses as player #2 : " + e2LoseAsPlayer2);
	output1Test4.newLine();

	//Write statistics for Random AI.
	output1Test4.append("MCTS_H(5+5) wins as player #1 : " + e1WinAsPlayer1);
	output1Test4.newLine();
	output1Test4.append("MCTS_H(5+5) wins as player #2 : " + e1WinAsPlayer2);
	output1Test4.newLine();
	output1Test4.append("MCTS_H(5+5) draws as player #1 : " + e1DrawAsPlayer1);
	output1Test4.newLine();
	output1Test4.append("MCTS_H(5+5) draws as player #2 : " + e1DrawAsPlayer2);
	output1Test4.newLine();
	output1Test4.append("MCTS_H(5+5) loses as player #1 : " + e1LoseAsPlayer1);
	output1Test4.newLine();
	output1Test4.append("MCTS_H(5+5) loses as player #2 : " + e1LoseAsPlayer2);
	output1Test4.newLine();

	output1Test4.append("========================================");
	output1Test4.close();
	
}


public static void testFive2 () throws Exception {
	
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

	//Board that is used in games.
	Board boardTest5 = null;

	//Board that keeps copy of initial position, used to quickly reset the 
	//board before new game take place.
	Board initialPositionTest5 = null;

	//Array of all boards that are used in the test case.
	Board[] boardCollectionTest5 = null;

	//Index of player that is entitled to make a move.
	int currentIndexTest5 = 0;

	//Number of all moves that was made during the game.
	int numberOfMoveTest5 = 0;
	int value5_grt2_50000 = 50000; 
	//Players participating in the test case.
	Player[] playersTest5 =  {
			new Player("MCTS_H(5)", "MCTS_H(5)", "w", value5_grt2_50000),
			new Player("MCTS_H(7)", "MCTS_H(7)", "b", value5_grt2_50000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest5 = 46;
	//Load board
	String name2Board5 = "50_boards_3.sav";
	loadBoard ( boardCollectionTest5, name2Board5);
	


	//The beginning and the end of the test.
	long startTimeTest5 = 0, endTimeTest5 = 0;

	//Report when games commenced.
	startTimeTest5 = System.currentTimeMillis();
	String name2File5 = "results_50k_3b_MCTS_UCTvMCTS_H(5+5).txt";
	//Define buffers.
	BufferedWriter outputTest5 = null;
	defineBuffers ( outputTest5, name2File5);
	
	
	MonteCarloH5 mc_h5 = new MonteCarloH5(
			boardTest5.duplicate(), 
			playersTest5[currentIndexTest5].getColor(), 
			numberOfMoveTest5, 
			totalNumberOfMovesTest5);
	MonteCarloH7 mch7 = new MonteCarloH7(
			boardTest5.duplicate(), 
			playersTest5[currentIndexTest5].getColor(), 
			numberOfMoveTest5, 
			totalNumberOfMovesTest5);
	
	//Boards are OK. Proceed to testing.
	for(int testIndex = 1; testIndex <= 100; ++testIndex) {
		System.out.println("Test5: " + testIndex + " / 100");
		//Reset settings.
		currentIndexTest5 = 0;
		numberOfMoveTest5 = 0;

		//Swap players.
		Player tmp = playersTest5[0];
		playersTest5[0] = playersTest5[1];
		playersTest5[1] = tmp;

		//Reset the board to an initial state. When index is odd generate a 
		//new random board.
		newRandomBoardTwo ( testIndex,  boardTest5, 
				boardCollectionTest5, initialPositionTest5 );


		//Run a single game.
		while(numberOfMoveTest5 < totalNumberOfMovesTest5) {
			if("MCTS_H(7)".equals(playersTest5[currentIndexTest5].getType())) {
				//MCTS + H(10) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo + H(10) will select new move.
//				Charles_2 charles = new Charles_2(playersTest5[currentIndexTest5].getColor(), boardTest5);
//				move = charles.getMove();
			
				move = mch7.uct(playersTest5[currentIndexTest5].
						getSimulationNumber());



				boardTest5.makeMove(move, playersTest5[currentIndexTest5].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest5;

				//Adjust index of current player.
				currentIndexTest5 = (currentIndexTest5 + 1) % 2;
			} else if("MCTS_H(5)".equals(playersTest5[currentIndexTest5].getType())) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select move.
				
				move = mc_h5.uct(playersTest5[currentIndexTest5].
						getSimulationNumber());


				boardTest5.makeMove(move, playersTest5[currentIndexTest5].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest5;

				//Adjust index of current player.
				currentIndexTest5 = (currentIndexTest5 + 1) % 2;
			}
		} //end of single game.

		String gameOutcome = Rules.calculateScore(boardTest5);
		
		outputTest5.append("Match #" + testIndex);
		outputTest5.newLine();
		outputTest5.append("Player 1: " + playersTest5[0].getName() + 
				" Player 2: " + playersTest5[1].getName());
		outputTest5.newLine();

		//Append the result to the text file and update counters..
		String zero = "0";
		
		if(gameOutcome.equals(zero)) {
			//The game was a draw.
			++totalDraws;
			//Append information to the file.
			outputTest5.append("Result: draw");
			outputTest5.newLine();
			outputTest5.close();

			//Update statistics.
			boolean valuePlayers5 = "MCTS_H(7)".equals(playersTest5[0].getName());
			updateStatisticsA2 ( valuePlayers5,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			//One side wins the game.
			boolean value = true;
			String phrase5_2 = "MCTS_H(7)";
			OneSideWinsTheGame ( gameOutcome,  playersTest5 
					, outputTest5,  value ,  e1TotalWins,
					 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
					 e2TotalWins,  e1TotalLoses,
					 e2WinAsPlayer2,  e1LoseAsPlayer1, phrase5_2 );
			
		}			
	} //End of the test case. (for)

	//Report when games ended.
	int value5_gt2_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test5 = null;	
	defineBuffers ( output1Test5,  name2File5);
	
	
	output1Test5.append("========================================");
	output1Test5.newLine();
	output1Test5.append("*Summary 50k roll-outs 3 point board*");
	output1Test5.newLine();
	output1Test5.append("Draw occurred: " + totalDraws);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(5) total wins: " + e2TotalWins);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(7) total wins: " + e1TotalWins);
	output1Test5.newLine();
	output1Test5.append("Play time: " + (endTimeTest5 - startTimeTest5)/value5_gt2_1000 + " seconds.");
	output1Test5.newLine();

	//Write statistics for MCTS.
	output1Test5.append("MCTS_H(5) wins as player #1 : " + e2WinAsPlayer1);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(5) wins as player #2 : " + e2WinAsPlayer2);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(5) draws as player #1 : " + e2DrawAsPlayer1);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(5) draws as player #2 : " + e2DrawAsPlayer2);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(5) loses as player #1 : " + e2LoseAsPlayer1);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(5) loses as player #2 : " + e2LoseAsPlayer2);
	output1Test5.newLine();

	//Write statistics for MCTS_H(7).
	output1Test5.append("MCTS_H(7) wins as player #1 : " + e1WinAsPlayer1);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(7) wins as player #2 : " + e1WinAsPlayer2);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(7) draws as player #1 : " + e1DrawAsPlayer1);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(7) draws as player #2 : " + e1DrawAsPlayer2);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(7) loses as player #1 : " + e1LoseAsPlayer1);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(7) loses as player #2 : " + e1LoseAsPlayer2);
	output1Test5.newLine();

	output1Test5.append("========================================");
	output1Test5.close();
	
}


public static void testSix2 () throws Exception {
	
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

	//Board that is used in games.
	Board boardTest6 = null;

	//Board that keeps copy of initial position, used to quickly reset the 
	//board before new game take place.
	Board initialPositionTest6 = null;

	//Array of all boards that are used in the test case.
	Board[] boardCollectionTest6 = null;

	//Index of player that is entitled to make a move.
	int currentIndexTest6 = 0;

	//Number of all moves that was made during the game.
	int numberOfMoveTest6 = 0;
	int value6_grt2_50000 = 50000; 
	//Players participating in the test case.
	Player[] playersTest6 =  {
			new Player("MCTS_H(7)", "MCTS_H(7)", "w", value6_grt2_50000),
			new Player("MCTS_H(10)", "MCTS_H(10)", "b", value6_grt2_50000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest6 = 46;
	//Load board
	String name2Board6 = "50_boards_3.sav";
	loadBoard ( boardCollectionTest6, name2Board6);
	//Load board.
	

	//The beginning and the end of the test.
	long startTimeTest6 = 0, endTimeTest6 = 0;

	//Report when games commenced.
	startTimeTest6 = System.currentTimeMillis();
	String name2File6 = "results_50k_3b_MCTS_H(7)vMCTS_H(10).txt";
	//Define buffers.
	BufferedWriter outputTest6 = null;
	defineBuffers ( outputTest6, name2File6);
	
	MonteCarloH7 mch_7 = new MonteCarloH7(
			boardTest6.duplicate(), 
			playersTest6[currentIndexTest6].getColor(), 
			numberOfMoveTest6, 
			totalNumberOfMovesTest6);
	MonteCarloH10 mc_h10 = new MonteCarloH10(
			boardTest6.duplicate(), 
			playersTest6[currentIndexTest6].getColor(), 
			numberOfMoveTest6, 
			totalNumberOfMovesTest6);
	
	//Boards are OK. Proceed to testing.
	for(int testIndex = 1; testIndex <= 100; ++testIndex) {
		System.out.println("Test6: " + testIndex + " / 100");
		//Reset settings.
		currentIndexTest6 = 0;
		numberOfMoveTest6 = 0;

		//Swap players.
		Player tmp = playersTest6[0];
		playersTest6[0] = playersTest6[1];
		playersTest6[1] = tmp;

		//Reset the board to an initial state. When index is odd generate a 
		//new random board.
		newRandomBoardTwo ( testIndex,  boardTest6, 
				boardCollectionTest6, initialPositionTest6 );


		//Run a single game.
		while(numberOfMoveTest6 < totalNumberOfMovesTest6) {
			if("MCTS_H(10)".equals(playersTest6[currentIndexTest6].getType())) {
				//MCTS + H(10) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo + H(10) will select new move.
//				Charles_2 charles = new Charles_2(playersTest6[currentIndexTest6].getColor(), boardTest6);
//				move = charles.getMove();
				
				move = mc_h10.uct(playersTest6[currentIndexTest6].
						getSimulationNumber());



				boardTest6.makeMove(move, playersTest6[currentIndexTest6].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest6;

				//Adjust index of current player.
				currentIndexTest6 = (currentIndexTest6 + 1) % 2;
			} else if("MCTS_H(7)".equals(playersTest6[currentIndexTest6].getType())) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select move.
				move = mch_7.uct(playersTest6[currentIndexTest6].
						getSimulationNumber());


				boardTest6.makeMove(move, playersTest6[currentIndexTest6].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest6;

				//Adjust index of current player.
				currentIndexTest6 = (currentIndexTest6 + 1) % 2;
			}
		} //end of single game.

		String gameOutcome = Rules.calculateScore(boardTest6);
		outputTest6.append("Match #" + testIndex);
		outputTest6.newLine();
		outputTest6.append("Player 1: " + playersTest6[0].getName() + 
				" Player 2: " + playersTest6[1].getName());
		outputTest6.newLine();

		//Append the result to the text file and update counters..
		String zero = "0";
		
		if(gameOutcome.equals(zero)) {
			//The game was a draw.
			++totalDraws;
			//Append information to the file.
			outputTest6.append("Result: draw");
			outputTest6.newLine();
			outputTest6.close();

			//Update statistics.
			boolean valuePlayers6 = "MCTS_H(10)".equals(playersTest6[1].getName());
			updateStatisticsA2 ( valuePlayers6,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			//One side wins the game.
			boolean value = true;
			String phrase6_2 = "MCTS_H(10)";
			OneSideWinsTheGame ( gameOutcome,  playersTest6 
					, outputTest6,  value ,  e1TotalWins,
					 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
					 e2TotalWins,  e1TotalLoses,
					 e2WinAsPlayer2,  e1LoseAsPlayer1, phrase6_2 );
			
		}			
	} //End of the test case. (for)

	//Report when games ended.
	int value6_gt2_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test6 = null;	
	defineBuffers ( output1Test6,  name2File6);
	
	
	output1Test6.append("========================================");
	output1Test6.newLine();
	output1Test6.append("*Summary 50k roll-outs 3 point board*");
	output1Test6.newLine();
	output1Test6.append("Draw occurred: " + totalDraws);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(7) total wins: " + e2TotalWins);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(10) total wins: " + e1TotalWins);
	output1Test6.newLine();
	output1Test6.append("Play time: " + (endTimeTest6 - startTimeTest6)/value6_gt2_1000 + " seconds.");
	output1Test6.newLine();

	//Write statistics for MCTS (UCT).
	output1Test6.append("MCTS_H(7) wins as player #1 : " + e2WinAsPlayer1);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(7) wins as player #2 : " + e2WinAsPlayer2);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(7) draws as player #1 : " + e2DrawAsPlayer1);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(7) draws as player #2 : " + e2DrawAsPlayer2);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(7) loses as player #1 : " + e2LoseAsPlayer1);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(7) loses as player #2 : " + e2LoseAsPlayer2);
	output1Test6.newLine();

	//Write statistics for MCTS + H(5).
	output1Test6.append("MCTS_H(10) wins as player #1 : " + e1WinAsPlayer1);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(10) wins as player #2 : " + e1WinAsPlayer2);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(10) draws as player #1 : " + e1DrawAsPlayer1);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(10) draws as player #2 : " + e1DrawAsPlayer2);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(10) loses as player #1 : " + e1LoseAsPlayer1);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(10) loses as player #2 : " + e1LoseAsPlayer2);
	output1Test6.newLine();

	output1Test6.append("========================================");
	output1Test6.close();

	
}

public static void updateStatisticsA2 (boolean value, int e1DrawAsPlayer1,
		int e2DrawAsPlayer2, int e1DrawAsPlayer2, int e2DrawAsPlayer1) {
	
	if(value) {
		e1DrawAsPlayer1++;
		e2DrawAsPlayer2++;
	} else {
		e1DrawAsPlayer2++;
		e2DrawAsPlayer1++;
	}

	
}

public static void updateStatisticsB2 (boolean value, int e1TotalWins,
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

public static void newRandomBoardTwo (int testIndex, Board boardTest, 
		Board[] boardCollectionTest, Board initialPositionTest1) {
	if(testIndex % 2 != 0) {
		//Load a new board.
		boardTest = boardCollectionTest[(Integer) testIndex/2];
		initialPositionTest1 = boardTest.duplicate();
	} else {
		//Reset the board.
		boardTest = initialPositionTest1.duplicate();
	}

	
}

public static  void defineBuffers (BufferedWriter output1Test, String nameFile) {
	
	try {
	 output1Test = new BufferedWriter(
			new FileWriter(nameFile, true));
	} catch(Exception e) {
		System.err.println("Error occured during saving.");
		System.out.println("Something was wrong");
	}finally {
           if (output1Test != null) {
               try {
            	   output1Test.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           }
	}
	
}

public static void OneSideWinsTheGame (String gameOutcome, Player[] playersTest1 
		,BufferedWriter outputTest1, boolean value, int e1TotalWins,
		int e2TotalLoses,int e1WinAsPlayer2, int e2LoseAsPlayer1,
		int e2TotalWins, int e1TotalLoses, int e2WinAsPlayer2, int e1LoseAsPlayer1, String phrase ) throws IOException {
	//One side wins the game.
	if(gameOutcome.equals(playersTest1[0].getColor())) {
		//Player #1, whoever it is, wins the game.

		//Add note about the winner to the file.
		outputTest1.append("Result: " + playersTest1[0].getName() + " wins");

		//Update statistics.
		boolean valuePlayers1 = playersTest1[0].getName().equals(phrase);
		updateStatisticsB2 ( valuePlayers1,  e1TotalWins,
				 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
				 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);

	} else {
		//Player #2, whoever it is, wins the game.

		//Add note about the winner to the file.
		outputTest1.append("Result: " + playersTest1[1].getName() + " wins");

		//Update statistics.
		boolean valuePlayers1 = playersTest1[1].getName().equals(phrase);
		updateStatisticsB2 ( valuePlayers1,  e1TotalWins,
				 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
				 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);
	}
	outputTest1.newLine();
	outputTest1.close();
	
}


}


