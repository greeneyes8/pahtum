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
 * Classe relativa al gigatest1
 * @author Mina
 *
 */

public class GigaTest1 {

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String[] args) throws Exception {
		
				//test go
				testOne1();
		/***********************************************************************
		 * Test #1: (20,000 roll-outs) MCTS_UCT v MCTS + H(5) 3-point board.
		 **********************************************************************/
				//test go
				testTwo1();

		/***********************************************************************
		 * Test #2: (20,000 roll-outs) MCTS_UCT v MCTS_H(7).
		 **********************************************************************/
				//test go
				testThree1();

		/***********************************************************************
		 * Test #3: (20,000 roll-outs) MCTS (UCT) v MCTS + H(10).
		 **********************************************************************/
				//test go
				testFour1();

		/***********************************************************************
		 * Test #4: (20,000 roll-outs) MCTS_UCT v MCTS + H(5+5).
		 **********************************************************************/
				//test go
				testFive1();
		/***********************************************************************
		 * Test #5: (20,000 roll-outs) MCTS + H(5) v MCTS + H(7).
		 **********************************************************************/
				//test go
				testSix1();
		
		/***********************************************************************
		 * Test #6: (20,000 roll-outs) MCTS_H(7) v MCTS_H(10).
		 **********************************************************************/
		
	}

private static Integer maxCast (int a){
	
	Integer valInteger = (Integer) a; 
		
	return valInteger;
	}
	
public static void newRandomBoardOne1 (int testIndex, Board boardTest1, 
		Board[] boardCollectionTest1, Board initialPositionTest1 ) {
	if(testIndex % 2 == 1) {
		//Load a new board.
		boardTest1 = boardCollectionTest1[(Integer) testIndex/2];
		initialPositionTest1 = boardTest1.duplicate();
	} else {
		//Reset the board.
		boardTest1 = initialPositionTest1.duplicate();
	}

	
}

public static void testOne1 () throws Exception {
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
	int value1_grt1_20000 = 20000; 
	boolean value = true;
	//Players participating in the test case.
	Player[] playersTest1 =  {
			new Player("MCTS_UCT", "MCTS_UCT", "w", value1_grt1_20000),
			new Player("MCTS_H(5)", "MCTS_H(5)", "b", value1_grt1_20000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest1 = 46;
	FileInputStream fisTest1 = null ;
	//Load board.
	try {
		 fisTest1 = new FileInputStream("50_boards_3.sav");
		ObjectInputStream oisTest1 = new ObjectInputStream(fisTest1);
		boardCollectionTest1 = (Board[]) oisTest1.readObject();
		oisTest1.close();
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (fisTest1 != null) {
               try {
            	   fisTest1.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}
		
	}

	//The beginning and the end of the test.
	long startTime = 0, endTime = 0;

	//Report when games commenced.
	startTime = System.currentTimeMillis();
	
	//Define buffers.
	BufferedWriter outputTest1 = null;
	try {
	 outputTest1 = new BufferedWriter(
			new FileWriter("results_20k_3b_MCTS_UCTvMCTS_H(5).txt", true));
	} catch(Exception e) {
		System.err.println("Error occured during saving.");
		System.out.println("Something was wrong");
	}finally {
           if (outputTest1 != null) {
               try {
            	   outputTest1.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}
	}
	MonteCarlo mc_t1 = new MonteCarlo(
			boardTest1.duplicate(), 
			playersTest1[currentIndexTest1].getColor(), 
			numberOfMoveTest1, 
			totalNumberOfMovesTest1);
	MonteCarloH5 mh5_mc = new MonteCarloH5(
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
		newRandomBoardOne1 ( testIndex,  boardTest1, 
				 boardCollectionTest1,  initialPositionTest1 );

		//Run a single game.
		while(numberOfMoveTest1 < totalNumberOfMovesTest1) {
			if(playersTest1[currentIndexTest1].getType().equals("MCTS_UCT")) {
				//MCTS + H(7) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select a move.
				

				move = mc_t1.uct(playersTest1[currentIndexTest1].
						getSimulationNumber());

				boardTest1.makeMove(move, playersTest1[currentIndexTest1].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest1;

				//Adjust index of current player.
				currentIndexTest1 = (currentIndexTest1 + 1) % 2;
			} else if(playersTest1[currentIndexTest1].getType().equals("MCTS_H(5)")) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select move.
				
				move = mh5_mc.uct(playersTest1[currentIndexTest1].
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
			boolean valuePlayers1 = playersTest1[0].getName().equals("MCTS_H(5)");
			updateStatisticsA1 ( valuePlayers1,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			
			//One side wins the game.
			OneSideWinsTheGame ( gameOutcome,  playersTest1 
					, outputTest1,  value ,  e1TotalWins,
					 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
					 e2TotalWins,  e1TotalLoses, e2WinAsPlayer2,  e1LoseAsPlayer1 );
		}			
	} //End of the test case. (for)

	//Report when games ended.
	int value1_gt1_1000 = 1000;
	
	
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test1 = null;
	try {
	 output1Test1 = new BufferedWriter(
			new FileWriter("results_20k_3b_MCTS_UCTvMCTS_H(5).txt", true));
	} catch(Exception e) {
		System.err.println("Error occured during saving.");
		System.out.println("Something was wrong");
	}finally {
           if (output1Test1 != null) {
               try {
            	   output1Test1.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}
	}
	output1Test1.append("========================================");
	output1Test1.newLine();
	output1Test1.append("*Summary 3-point board 20k roll-outs*");
	output1Test1.newLine();
	output1Test1.append("Draw occurred: " + totalDraws);
	output1Test1.newLine();
	output1Test1.append("MCTS_UCT total wins: " + e2TotalWins);
	output1Test1.newLine();
	output1Test1.append("MCTS_H(5) total wins: " + e1TotalWins);
	output1Test1.newLine();
	output1Test1.append("Play time: " + (endTime - startTime)/value1_gt1_1000 + " seconds.");
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
	
	
	
}

public static void newRandomBoardTwo1 (int testIndex, Board boardTest2, 
		Board[] boardCollectionTest2, Board initialPositionTest2 ) {
	if(testIndex % 2 == 1) {
		//Load a new board.
		boardTest2 = boardCollectionTest2[(Integer) testIndex/2];
		initialPositionTest2 = boardTest2.duplicate();
	} else {
		//Reset the board.
		boardTest2 = initialPositionTest2.duplicate();
	}

	
}

public static void OneSideWinsTheGame (String gameOutcome, Player[] playersTest1 
		,BufferedWriter outputTest1, boolean value, int e1TotalWins,
		int e2TotalLoses,int e1WinAsPlayer2, int e2LoseAsPlayer1,
		int e2TotalWins, int e1TotalLoses, int e2WinAsPlayer2, int e1LoseAsPlayer1 ) throws IOException {
	//One side wins the game.
	if(gameOutcome.equals(playersTest1[0].getColor())) {
		//Player #1, whoever it is, wins the game.

		//Add note about the winner to the file.
		outputTest1.append("Result: " + playersTest1[0].getName() + " wins");

		//Update statistics.
		boolean valuePlayers1 = playersTest1[0].getName().equals("MCTS_H(5)");
		updateStatisticsB1 ( valuePlayers1,  e1TotalWins,
				 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
				 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);

	} else {
		//Player #2, whoever it is, wins the game.

		//Add note about the winner to the file.
		outputTest1.append("Result: " + playersTest1[1].getName() + " wins");

		//Update statistics.
		boolean valuePlayers1 = playersTest1[1].getName().equals("MCTS_H(5)");
		updateStatisticsB1 ( valuePlayers1,  e1TotalWins,
				 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
				 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);
	}
	outputTest1.newLine();
	outputTest1.close();
	
}

public static void testTwo1 () throws Exception {
	
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
	int value2_grt1_20000 = 20000; 
	//Players participating in the test case.
	Player[] playersTest2 =  {
			new Player("MCTS_UCT", "MCTS_UCT", "w", value2_grt1_20000),
			new Player("MCTS_H(7)", "MCTS_H(7)", "b", value2_grt1_20000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest2 = 46;

	//Load board.
	FileInputStream fisTest2 = null;
	try {
		fisTest2 = new FileInputStream("50_boards_3.sav");
		ObjectInputStream oisTest2 = new ObjectInputStream(fisTest2);
		boardCollectionTest2 = (Board[]) oisTest2.readObject();
		oisTest2.close();
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	}finally {
		   if (fisTest2 != null) {
               try {
            	   fisTest2.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}

	//The beginning and the end of the test.
	long startTimeTest2 = 0, endTimeTest2 = 0;

	//Report when games commenced.
	startTimeTest2 = System.currentTimeMillis();

	//Define buffers.
	BufferedWriter outputTest2 = null;
	try{
		outputTest2 = new BufferedWriter(
				new FileWriter("results_20k_3b_MCTS_UCTvMCTS_H(7).txt", true));
		outputTest2.close();
	}catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (outputTest2 != null) {
               try {
            	   outputTest2.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}
		
	}

	
	MonteCarloH7 h7_mc = new MonteCarloH7(
			boardTest2.duplicate(), 
			playersTest2[currentIndexTest2].getColor(), 
			numberOfMoveTest2, 
			totalNumberOfMovesTest2);
	MonteCarlo mc_t2 = new MonteCarlo(
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
		newRandomBoardTwo1 ( testIndex,  boardTest2, 
				 boardCollectionTest2,  initialPositionTest2 );


		//Run a single game.
		while(numberOfMoveTest2 < totalNumberOfMovesTest2) {
			if(playersTest2[currentIndexTest2].getType().equals("MCTS_H(7)")) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select move.
				move = h7_mc.uct(playersTest2[currentIndexTest2].
						getSimulationNumber());


				boardTest2.makeMove(move, playersTest2[currentIndexTest2].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest2;

				//Adjust index of current player.
				currentIndexTest2 = (currentIndexTest2 + 1) % 2;
			} else if(playersTest2[currentIndexTest2].getType().equals("MCTS_UCT")) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select move.
				
				move = mc_t2.uct(playersTest2[currentIndexTest2].
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
			boolean valuePlayers2 = playersTest2[0].getName().equals("MCTS_H(7)");
			updateStatisticsA1 ( valuePlayers2,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			//One side wins the game.
			if(gameOutcome.equals(playersTest2[0].getColor())) {
				//Player #1, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest2.append("Result: " + playersTest2[0].getName() + " wins");

				//Update statistics.
				boolean valuePlayers2 = playersTest2[0].getName().equals("MCTS_H(7)");
				updateStatisticsB1 ( valuePlayers2,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);

			} else {
				//Player #2, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest2.append("Result: " + playersTest2[1].getName() + " wins");

				//Update statistics.
				boolean valuePlayers2 =playersTest2[1].getName().equals("MCTS_H(7)");
				updateStatisticsB1 ( valuePlayers2,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);
			}
			outputTest2.newLine();
			outputTest2.close();
		}			
	} //End of the test case. (for)

	//Report when games ended.
	int value2_gt1_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test2 = null;
	try{
	output1Test2 = new BufferedWriter(
			new FileWriter("results_20k_3b_MCTS_UCTvMCTS_H(7).txt", true));
	}catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (output1Test2 != null) {
               try {
            	   output1Test2.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}
		
	}

	
	output1Test2.append("========================================");
	output1Test2.newLine();
	output1Test2.append("*Summary (20k) 3-point board*");
	output1Test2.newLine();
	output1Test2.append("Draw occurred: " + totalDraws);
	output1Test2.newLine();
	output1Test2.append("MCTS_UCT total wins: " + e2TotalWins);
	output1Test2.newLine();
	output1Test2.append("MCTS_H(7) total wins: " + e1TotalWins);
	output1Test2.newLine();
	output1Test2.append("Play time: " + (endTimeTest2 - startTimeTest2)/value2_gt1_1000 + " seconds.");
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

public static void newRandomBoardThree1 (int testIndex, Board boardTest3, 
		Board[] boardCollectionTest3, Board initialPositionTest3 ) {
	if(testIndex % 2 == 1) {
		//Load a new board.
		boardTest3 = boardCollectionTest3[(Integer) testIndex/2];
		initialPositionTest3 = boardTest3.duplicate();
	} else {
		//Reset the board.
		boardTest3 = initialPositionTest3.duplicate();
	}

}


public static void testThree1 () throws Exception {
	
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
	int value3_grt1_20000 = 20000; 
	//Players participating in the test case.
	Player[] playersTest3 =  {
			new Player("MCTS_UCT", "MCTS_UCT", "w", value3_grt1_20000),
			new Player("MCTS_H(10)", "MCTS_H(10)", "b", value3_grt1_20000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest3 = 46;

	//Load board.
	FileInputStream fisTest3 = null;
	
	try {
		fisTest3 = new FileInputStream("50_boards_3.sav");
		ObjectInputStream oisTest3 = new ObjectInputStream(fisTest3);
		boardCollectionTest3 = (Board[]) oisTest3.readObject();
		oisTest3.close();
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	}finally {
		   if (fisTest3 != null) {
               try {
            	   fisTest3.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}
		
	}


	//The beginning and the end of the test.
	long startTimeTest3 = 0, endTimeTest3 = 0;

	//Report when games commenced.
	startTimeTest3 = System.currentTimeMillis();

	//Define buffers.
	BufferedWriter outputTest3 = null;
	
	try{
		outputTest3 = new BufferedWriter(
				new FileWriter("results_20k_3b_MCTS_UCTvMCTS_H(10).txt", true));
	}catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (outputTest3 != null) {
               try {
            	   outputTest3.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}
	
	
	MonteCarloH10 h10_mc = new MonteCarloH10(
			boardTest3.duplicate(), 
			playersTest3[currentIndexTest3].getColor(), 
			numberOfMoveTest3, 
			totalNumberOfMovesTest3);
	MonteCarlo mc_t3 = new MonteCarlo(
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
		newRandomBoardThree1 ( testIndex,  boardTest3, 
				 boardCollectionTest3,  initialPositionTest3 );


		//Run a single game.
		while(numberOfMoveTest3 < totalNumberOfMovesTest3) {
			if(playersTest3[currentIndexTest3].getType().equals("MCTS_H(10)")) {
				//MCTS + H(5) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo + H(5) will select new move.
				move = h10_mc.uct(playersTest3[currentIndexTest3].
						getSimulationNumber());


				boardTest3.makeMove(move, playersTest3[currentIndexTest3].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest3;

				//Adjust index of current player.
				currentIndexTest3 = (currentIndexTest3 + 1) % 2;
			} else if(playersTest3[currentIndexTest3].getType().equals("MCTS_UCT")) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;

				//Pure Monte-Carlo will select move.

				move = mc_t3.uct(playersTest3[currentIndexTest3].
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
			boolean valuePlayers3 = playersTest3[0].getName().equals("MCTS_H(10)");
			updateStatisticsA1 ( valuePlayers3,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			//One side wins the game.
			if(gameOutcome.equals(playersTest3[0].getColor())) {
				//Player #1, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest3.append("Result: " + playersTest3[0].getName() + " wins");

				//Update statistics.
				boolean valuePlayers3 = playersTest3[0].getName().equals("MCTS_H(10)");
				updateStatisticsB1 ( valuePlayers3,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);

			} else {
				//Player #2, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest3.append("Result: " + playersTest3[1].getName() + " wins");

				//Update statistics.
				boolean valuePlayers3 = playersTest3[1].getName().equals("MCTS_H(10)");
				updateStatisticsB1 ( valuePlayers3,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);
			}
			outputTest3.newLine();
			outputTest3.close();
		}			
	} //End of the test case. (for)

	//Report when games ended.
	int value3_gt1_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test3 = null;
	
	try{
		output1Test3 = new BufferedWriter(
				new FileWriter("results_20k_3b_MCTS_UCTvMCTS_H(10).txt", true));
	}catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (output1Test3 != null) {
               try {
            	   output1Test3.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}
	
	output1Test3.append("========================================");
	output1Test3.newLine();
	output1Test3.append("*Summary 20k roll-outs 3 point board*");
	output1Test3.newLine();
	output1Test3.append("Draw occurred: " + totalDraws);
	output1Test3.newLine();
	output1Test3.append("MCTS_UCT total wins: " + e2TotalWins);
	output1Test3.newLine();
	output1Test3.append("MCTS_H(10) total wins: " + e1TotalWins);
	output1Test3.newLine();
	output1Test3.append("Play time: " + (endTimeTest3 - startTimeTest3)/value3_gt1_1000 + " seconds.");
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

public static void newRandomBoardFour1 (int testIndex, Board boardTest4, 
		Board[] boardCollectionTest4, Board initialPositionTest4 ) {
	if(testIndex % 2 == 1) {
		//Load a new board.
		boardTest4 = boardCollectionTest4[(Integer) testIndex/2];
		initialPositionTest4 = boardTest4.duplicate();
	} else {
		//Reset the board.
		boardTest4 = initialPositionTest4.duplicate();
	}

}

public static void testFour1() throws Exception {
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
	int value4_grt1_20000 = 20000; 
	//Players participating in the test case.
	Player[] playersTest4 =  {
			new Player("MCTS_UCT", "MCTS_UCT", "w", value4_grt1_20000),
			new Player("MCTS_H(5+5)", "MCTS_H(5+5)", "b", value4_grt1_20000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest4 = 46;

	//Load board.
	FileInputStream fisTest4 = null;
	
	try {
		fisTest4 = new FileInputStream("50_boards_3.sav");
		ObjectInputStream oisTest4 = new ObjectInputStream(fisTest4);
		boardCollectionTest4 = (Board[]) oisTest4.readObject();
		oisTest4.close();
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	}finally {
		   if (fisTest4 != null) {
               try {
            	   fisTest4.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}
		
	}


	//The beginning and the end of the test.
	long startTimeTest4 = 0, endTimeTest4 = 0;

	//Report when games commenced.
	startTimeTest4 = System.currentTimeMillis();

	//Define buffers.
	BufferedWriter outputTest4 = null;
	
	try{
		outputTest4 = new BufferedWriter(
				new FileWriter("results_20k_3b_MCTS_UCTvMCTS_H(5+5).txt", true));
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (outputTest4 != null) {
               try {
            	   outputTest4.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}
	
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
		newRandomBoardFour1 ( testIndex,  boardTest4, 
				 boardCollectionTest4,  initialPositionTest4 );


		//Run a single game.
		while(numberOfMoveTest4 < totalNumberOfMovesTest4) {
			if(playersTest4[currentIndexTest4].getType().equals("MCTS_H(5+5)")) {
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
			} else if(playersTest4[currentIndexTest4].getType().equals("MCTS_UCT")) {
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
			boolean valuePlayers4 = playersTest4[0].getName().equals("MCTS_H(5+5)");
			updateStatisticsA1 ( valuePlayers4,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			//One side wins the game.
			if(gameOutcome.equals(playersTest4[0].getColor())) {
				//Player #1, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest4.append("Result: " + playersTest4[0].getName() + " wins");

				//Update statistics.
				boolean valuePlayers4 = playersTest4[0].getName().equals("MCTS_H(5+5)");
				updateStatisticsB1 ( valuePlayers4,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);

			} else {
				//Player #2, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest4.append("Result: " + playersTest4[1].getName() + " wins");

				//Update statistics.
				boolean valuePlayers4 = playersTest4[1].getName().equals("MCTS_H(5+5)");
				updateStatisticsB1 ( valuePlayers4,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);
			}
			outputTest4.newLine();
			outputTest4.close();
		}			
	} //End of the test case. (for)

	//Report when games ended.
	int value4_gt1_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test4 = null;
	
	try{
		output1Test4 = new BufferedWriter(
				new FileWriter("results_20k_3b_MCTS_UCTvMCTS_H(5+5).txt", true));
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (output1Test4 != null) {
               try {
            	   output1Test4.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}
	
	output1Test4.append("========================================");
	output1Test4.newLine();
	output1Test4.append("*Summary 20k roll-outs 3 point board*");
	output1Test4.newLine();
	output1Test4.append("Draw occurred: " + totalDraws);
	output1Test4.newLine();
	output1Test4.append("MCTS_UCT total wins: " + e2TotalWins);
	output1Test4.newLine();
	output1Test4.append("MCTS_H(5+5) total wins: " + e1TotalWins);
	output1Test4.newLine();
	output1Test4.append("Play time: " + (endTimeTest4 - startTimeTest4)/value4_gt1_1000 + " seconds.");
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

public static void newRandomBoardFive1 (int testIndex, Board boardTest5, 
		Board[] boardCollectionTest5, Board initialPositionTest5 ) {
	if(testIndex % 2 == 1) {
		//Load a new board.
		boardTest5 = boardCollectionTest5[(Integer) testIndex/2];
		initialPositionTest5 = boardTest5.duplicate();
	} else {
		//Reset the board.
		boardTest5 = initialPositionTest5.duplicate();
	}

}

public static void testFive1() throws Exception {
	
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
	int value5_grt1_20000 = 20000; 
	//Players participating in the test case.
	Player[] playersTest5 =  {
			new Player("MCTS_H(5)", "MCTS_H(5)", "w", value5_grt1_20000),
			new Player("MCTS_H(7)", "MCTS_H(7)", "b", value5_grt1_20000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest5 = 46;

	//Load board.
	FileInputStream fisTest5 = null;
	
	try {
		fisTest5 = new FileInputStream("50_boards_3.sav");
		ObjectInputStream oisTest5 = new ObjectInputStream(fisTest5);
		boardCollectionTest5 = (Board[]) oisTest5.readObject();
		oisTest5.close();
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (fisTest5 != null) {
               try {
            	   fisTest5.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}


	//The beginning and the end of the test.
	long startTimeTest5 = 0, endTimeTest5 = 0;

	//Report when games commenced.
	startTimeTest5 = System.currentTimeMillis();

	
	//Define buffers.
	BufferedWriter outputTest5 = null;
	
	try{
		outputTest5 = new BufferedWriter(
				new FileWriter("results_20k_3b_MCTS_H(5)vMCTS_H(7).txt", true));
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (outputTest5 != null) {
               try {
            	   outputTest5.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}

	
	MonteCarloH7 h7mc = new MonteCarloH7(
			boardTest5.duplicate(), 
			playersTest5[currentIndexTest5].getColor(), 
			numberOfMoveTest5, 
			totalNumberOfMovesTest5);
	MonteCarloH5 h5mc = new MonteCarloH5(
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
		newRandomBoardFive1 ( testIndex,  boardTest5, 
				 boardCollectionTest5,  initialPositionTest5 );

		//Run a single game.
		while(numberOfMoveTest5 < totalNumberOfMovesTest5) {
			if(playersTest5[currentIndexTest5].getType().equals("MCTS_H(7)")) {
				//MCTS + H(10) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo + H(10) will select new move.
//				Charles_2 charles = new Charles_2(playersTest5[currentIndexTest5].getColor(), boardTest5);
//				move = charles.getMove();
				
				move = h7mc.uct(playersTest5[currentIndexTest5].
						getSimulationNumber());



				boardTest5.makeMove(move, playersTest5[currentIndexTest5].getColor());

				//Increment number of currently made moves.
				++numberOfMoveTest5;

				//Adjust index of current player.
				currentIndexTest5 = (currentIndexTest5 + 1) % 2;
			} else if(playersTest5[currentIndexTest5].getType().equals("MCTS_H(5)")) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select move.
				
				move = h5mc.uct(playersTest5[currentIndexTest5].
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
			boolean valuePlayers5 = playersTest5[0].getName().equals("MCTS_H(7)");
			updateStatisticsA1 ( valuePlayers5,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			//One side wins the game.
			if(gameOutcome.equals(playersTest5[0].getColor())) {
				//Player #1, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest5.append("Result: " + playersTest5[0].getName() + " wins");

				//Update statistics.
				boolean valuePlayers5 = playersTest5[0].getName().equals("MCTS_H(7)");
				updateStatisticsB1 ( valuePlayers5,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);
			} else {
				//Player #2, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest5.append("Result: " + playersTest5[1].getName() + " wins");

				//Update statistics.
				boolean valuePlayers5 = playersTest5[1].getName().equals("MCTS_H(7)");
				updateStatisticsB1 ( valuePlayers5,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);
			}
			outputTest5.newLine();
			outputTest5.close();
		}			
	} //End of the test case. (for)

	//Report when games ended.

	int value5_gt1_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test5 = null;
	
	try{
		output1Test5 = new BufferedWriter(
				new FileWriter("results_20k_3b_MCTS_H(5)vMCTS_H(7).txt", true));
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (output1Test5 != null) {
               try {
            	   output1Test5.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}
	
	output1Test5.append("========================================");
	output1Test5.newLine();
	output1Test5.append("*Summary 20k roll-outs 3 point board*");
	output1Test5.newLine();
	output1Test5.append("Draw occurred: " + totalDraws);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(5) total wins: " + e2TotalWins);
	output1Test5.newLine();
	output1Test5.append("MCTS_H(7) total wins: " + e1TotalWins);
	output1Test5.newLine();
	output1Test5.append("Play time: " + (endTimeTest5 - startTimeTest5)/value5_gt1_1000 + " seconds.");
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

public static void newRandomBoardSix1 (int testIndex, Board boardTest6, 
		Board[] boardCollectionTest6, Board initialPositionTest6 ) {
	if(testIndex % 2 == 1) {
		//Load a new board.
		boardTest6 = boardCollectionTest6[(Integer) testIndex/2];
		initialPositionTest6 = boardTest6.duplicate();
	} else {
		//Reset the board.
		boardTest6 = initialPositionTest6.duplicate();
	}

}

public static void testSix1 () throws Exception {
	
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
	int value6_grt1_20000 = 20000; 
	//Players participating in the test case.
	Player[] playersTest6 =  {
			new Player("MCTS_H(7)", "MCTS_H(7)", "w", value6_grt1_20000),
			new Player("MCTS_H(10)", "MCTS_H(10)", "b", value6_grt1_20000)
	};

	//Number of total moves. It is used to check whether the game is in 
	//terminate state or not (the game finishes when there is no empty 
	//fields in the board).
	int totalNumberOfMovesTest6 = 46;

	//Load board.
	FileInputStream fisTest6 = null;
	
	try {
		fisTest6 = new FileInputStream("50_boards_3.sav");
		ObjectInputStream oisTest6 = new ObjectInputStream(fisTest6);
		boardCollectionTest6 = (Board[]) oisTest6.readObject();
		oisTest6.close();
	} catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	}finally {
		   if (fisTest6 != null) {
               try {
            	   fisTest6.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}


	//The beginning and the end of the test.
	long startTimeTest6 = 0, endTimeTest6 = 0;

	//Report when games commenced.
	startTimeTest6 = System.currentTimeMillis();

	//Define buffers.
	BufferedWriter outputTest6 = null;
	
	try{
		outputTest6 = new BufferedWriter(
				new FileWriter("results_20k_3b_MCTS_H(7)vMCTS_H(10).txt", true));
	}catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (outputTest6 != null) {
               try {
            	   outputTest6.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}

			
			MonteCarloH7 mc_h7 = new MonteCarloH7(
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
		newRandomBoardSix1 ( testIndex,  boardTest6, 
				 boardCollectionTest6,  initialPositionTest6 );


		//Run a single game.
		while(numberOfMoveTest6 < totalNumberOfMovesTest6) {
			if(playersTest6[currentIndexTest6].getType().equals("MCTS_H(10)")) {
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
			} else if(playersTest6[currentIndexTest6].getType().equals("MCTS_H(7)")) {
				//MCTS (UCT) to play.
				Tuple<Integer, Integer> move;
				//Pure Monte-Carlo will select move.
				
				move = mc_h7.uct(playersTest6[currentIndexTest6].
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
			boolean valuePlayers6 = playersTest6[1].getName().equals("MCTS_H(10)");
			updateStatisticsA1 ( valuePlayers6,  e1DrawAsPlayer1,
					 e2DrawAsPlayer2,  e1DrawAsPlayer2,  e2DrawAsPlayer1);

		} else {
			//One side wins the game.
			if(gameOutcome.equals(playersTest6[0].getColor())) {
				//Player #1, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest6.append("Result: " + playersTest6[0].getName() + " wins");

				//Update statistics.
				boolean valuePlayers6 = playersTest6[1].getName().equals("MCTS_H(10)");
				updateStatisticsB1 ( valuePlayers6,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);

			} else {
				//Player #2, whoever it is, wins the game.

				//Add note about the winner to the file.
				outputTest6.append("Result: " + playersTest6[1].getName() + " wins");

				//Update statistics.
				boolean valuePlayers6 = playersTest6[1].getName().equals("MCTS_H(10)");
				updateStatisticsB1 ( valuePlayers6,  e1TotalWins,
						 e2TotalLoses, e1WinAsPlayer2,  e2LoseAsPlayer1,
						 e2TotalWins,  e1TotalLoses,  e2WinAsPlayer2,  e1LoseAsPlayer1);
			}
			outputTest6.newLine();
			outputTest6.close();
		}			
	} //End of the test case. (for)

	//Report when games ended.
	int value6_gt1_1000 = 1000;
	//Append total outcome of the test case to the file.
	BufferedWriter output1Test6 = null;
	
	try{
		output1Test6 = new BufferedWriter(
				new FileWriter("results_20k_3b_MCTS_H(7)vMCTS_H(10).txt", true));
	}catch(Exception e) {
		System.err.println("Error" + e.getMessage());
	} finally {
		   if (output1Test6 != null) {
               try {
            	   output1Test6.close (); 
               } catch (java.io.IOException e3) {
                 System.out.println("I/O Exception");
               }	
           	}	
	}

	
	
	output1Test6.append("========================================");
	output1Test6.newLine();
	output1Test6.append("*Summary 20k roll-outs 3 point board*");
	output1Test6.newLine();
	output1Test6.append("Draw occurred: " + totalDraws);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(7) total wins: " + e2TotalWins);
	output1Test6.newLine();
	output1Test6.append("MCTS_H(10) total wins: " + e1TotalWins);
	output1Test6.newLine();
	output1Test6.append("Play time: " + (endTimeTest6 - startTimeTest6)/value6_gt1_1000 + " seconds.");
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

public static void updateStatisticsA1 (boolean value, int e1DrawAsPlayer1,
		int e2DrawAsPlayer2, int e1DrawAsPlayer2, int e2DrawAsPlayer1) {
	
	if(value) {
		e1DrawAsPlayer1++;
		e2DrawAsPlayer2++;
	} else {
		e1DrawAsPlayer2++;
		e2DrawAsPlayer1++;
	}

	
}

public static void updateStatisticsB1 (boolean value, int e1TotalWins,
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

}